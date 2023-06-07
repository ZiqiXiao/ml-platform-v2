import http
import shutil
from concurrent.futures import ThreadPoolExecutor

import joblib
import torch

from .utils import *


def init_train_routes(app, socketio):
    # Page for uploading the training data， and selecting the model

    @app.route('/model-params', methods=['GET'])
    def model_params():
        return jsonify(Config.DEFAULT_PARAMS)

    @app.route('/train-data-upload-path', methods=['GET'])
    def train_data_upload_path():
        return jsonify(Config.UPLOAD_TMP_TRAIN_FOLDER)

    @app.route('/get-train-labels', methods=['POST'])
    def get_train_labels():
        filepath = request.get_json()['filePath']
        file_path = os.path.join(Config.UPLOAD_TMP_TRAIN_FOLDER, filepath)
        df = pd.read_csv(file_path)
        return jsonify(status=http.HTTPStatus.OK, labels=df.columns.tolist())

    @app.route('/start-train', methods=['Post'])
    def start_train():
        try:
            global model_dict
            model_dict = {}
            # 解析数据
            filepath = request.get_json()['filePath']
            models = request.get_json()['model']
            label = request.get_json()['label']
            mission = request.get_json()['mission']

            model_dict['filepath'] = filepath
            model_dict['mission'] = mission

            with ThreadPoolExecutor(max_workers=1) as executor:
                future = executor.submit(
                    train_scheduler,
                    app, socketio, filepath, models, mission, label, model_dict
                )
                return_val = future.result()  # This will raise any exceptions that occurred.
            return jsonify(status=http.HTTPStatus.OK)
        except Exception as e:
            err_msg = f"Exception occurred: {str(e)}\n{traceback.format_exc()}"
            app.logger.error(err_msg)
            return jsonify(message=err_msg, status=http.HTTPStatus.INTERNAL_SERVER_ERROR)

    @app.route('/save-model', methods=['Post'])
    def save_model():
        # app.logger.info(f"Model Dict Is {model_dict}")
        model_names = request.get_json()['modelName']
        model_classes = request.get_json()['modelClass']
        # app.logger.info(f"{str(model_classes)}")
        train_data = request.get_json()['trainData']
        file_name = train_data['fileName']
        template_name = train_data['templateName']
        # app.logger.info(f"{str(model_dict)}")

        model_save_path_list = []
        # 保存模型
        for index, model_name in enumerate(model_names):
            # app.logger.info(model_classes)
            if model_classes[index] == 'mlp':
                model_save_path = os.path.join(Config.MODEL_FOLDER[model_dict['mission']], model_classes[index], model_name + '.pt')
                torch.save(model_dict[model_classes[index]], model_save_path)
            else:
                model_save_path = os.path.join(Config.MODEL_FOLDER[model_dict['mission']], model_classes[index], model_name + '.joblib')
                joblib.dump(model_dict[model_classes[index]], model_save_path)
            model_save_path_list.append(model_save_path)

        cur_file_path = os.path.join(Config.UPLOAD_TMP_TRAIN_FOLDER, model_dict['filepath'])

        # 保存模板
        template_save_path = ''
        if template_name != '':
            template_save_path = os.path.join(Config.TEMPLATE_FOLDER, template_name + '.csv')
            pd.read_csv(cur_file_path, nrows=0).drop(axis=1, columns=[model_dict['label']]) \
                .to_csv(template_save_path, index=False)

        # 保存数据集
        file_save_path = ''
        if file_name != '':
            file_save_path = os.path.join(Config.UPLOAD_TRAIN_FOLDER, file_name + '.csv')
            shutil.copyfile(cur_file_path, file_save_path)

        app.logger.info(f"Saved model: {model_save_path_list}")
        app.logger.info(f"Saved template: {template_save_path}")
        app.logger.info(f"Saved file: {file_save_path}")
        return jsonify(
            status=http.HTTPStatus.OK,
            modelPaths=model_save_path_list,
            templatePath=template_save_path,
            filePath=file_save_path
        )

    @app.route('/delete-train-data', methods=['POST'])
    def delete_train_data():
        file_name = request.get_json()['fileName']
        file_path = os.path.join(Config.UPLOAD_TRAIN_FOLDER, file_name + '.csv')
        if os.path.exists(file_path):
            os.remove(file_path)
            return jsonify(message={'message': 'File removed successfully'}), http.HTTPStatus.OK
        else:
            return jsonify(massage={'message': 'File not found'}), http.HTTPStatus.NOT_FOUND

    @app.route('/rename-train-data', methods=['POST'])
    def rename_train_data():
        file_name = request.get_json()['fileName']
        new_file_name = request.get_json()['newName']
        file_path = os.path.join(Config.UPLOAD_TRAIN_FOLDER, file_name + '.csv')
        new_file_path = os.path.join(Config.UPLOAD_TRAIN_FOLDER, new_file_name + '.csv')
        if os.path.exists(file_path):
            os.rename(file_path, new_file_path)
            return jsonify(message={'message': 'File renamed successfully'}, newPath=new_file_path), http.HTTPStatus.OK
        else:
            return jsonify(massage={'message': 'File not found'}), http.HTTPStatus.NOT_FOUND

    @app.route('/rename-template', methods=['POST'])
    def rename_template():
        file_name = request.get_json()['fileName']
        new_file_name = request.get_json()['newName']
        file_path = os.path.join(Config.TEMPLATE_FOLDER, file_name + '.csv')
        new_file_path = os.path.join(Config.TEMPLATE_FOLDER, new_file_name + '.csv')
        if os.path.exists(file_path):
            os.rename(file_path, new_file_path)
            return jsonify(message={'message': 'File renamed successfully'}, newPath=new_file_path), http.HTTPStatus.OK
        else:
            return jsonify(massage={'message': 'File not found'}, newPath=new_file_path), http.HTTPStatus.NOT_FOUND

    @app.route('/rename-model', methods=['POST'])
    def rename_model():
        model_name = request.get_json()['modelName']
        new_model_name = request.get_json()['newName']
        model_class = request.get_json()['modelClass']
        mission = request.get_json()['mission']
        file_path = os.path.join(Config.MODEL_FOLDER[mission], model_class, model_name + '.joblib')
        new_file_path = os.path.join(Config.MODEL_FOLDER[mission], model_class, new_model_name + '.joblib')
        if os.path.exists(file_path):
            os.rename(file_path, new_file_path)
            return jsonify(message={'message': 'File renamed successfully'}, newPath=new_file_path), http.HTTPStatus.OK
        else:
            return jsonify(massage={'message': 'File not found'}, newPath=new_file_path), http.HTTPStatus.NOT_FOUND

    @app.route('/delete-model', methods=['POST'])
    def delete_model():
        model_name = request.get_json()['modelName']
        model_class = request.get_json()['modelClass']
        mission = request.get_json()['mission']
        file_path = os.path.join(Config.MODEL_FOLDER[mission], model_class, model_name + '.joblib')
        if os.path.exists(file_path):
            os.remove(file_path)
            return jsonify(message={'message': 'File removed successfully'}), http.HTTPStatus.OK
        else:
            return jsonify(massage={'message': 'File not found'}), http.HTTPStatus.NOT_FOUND

    @app.route('/download-template', methods=['POST'])
    def download_template():
        print(request.get_json())
        template_path = request.get_json()['templatePath']
        if os.path.exists(template_path):
            app.logger.info(f"Download predict result: {template_path}")
            return send_file(template_path, as_attachment=True)
        else:
            return jsonify(message='Predict result not found'), http.HTTPStatus.NOT_FOUND
