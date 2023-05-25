import http
import shutil

import joblib

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
        global model_dict
        model_dict = {}
        # 解析数据
        filepath = request.get_json()['filePath']
        models = request.get_json()['model']
        label = request.get_json()['label']

        model_dict['filepath'] = filepath

        threading.Thread(target=train_scheduler, args=(
            app, socketio, filepath, models, label, model_dict)).start()
        return jsonify(status=http.HTTPStatus.OK)

    @app.route('/save-model', methods=['Post'])
    def save_model():
        # app.logger.info(f"Model Dict Is {model_dict}")
        model_names = request.get_json()['modelName']
        model_classes = request.get_json()['modelClass']
        app.logger.info(f"{str(model_classes)}")
        train_data = request.get_json()['trainData']
        file_name = train_data['fileName']
        template_name = train_data['templateName']
        app.logger.info(f"{str(model_dict)}")

        model_save_path_list = []
        # 保存模型
        for index, model_name in enumerate(model_names):
            model_save_path = os.path.join(Config.MODEL_FOLDER, model_classes[index], model_name + '.joblib')
            app.logger.info(model_dict[model_classes[index]])
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
        file_path = os.path.join(Config.MODEL_FOLDER, model_class, model_name + '.joblib')
        new_file_path = os.path.join(Config.MODEL_FOLDER, model_class, new_model_name + '.joblib')
        if os.path.exists(file_path):
            os.rename(file_path, new_file_path)
            return jsonify(message={'message': 'File renamed successfully'}, newPath=new_file_path), http.HTTPStatus.OK
        else:
            return jsonify(massage={'message': 'File not found'}, newPath=new_file_path), http.HTTPStatus.NOT_FOUND

    @app.route('/delete-model', methods=['POST'])
    def delete_model():
        model_name = request.get_json()['modelName']
        model_class = request.get_json()['modelClass']
        file_path = os.path.join(Config.MODEL_FOLDER, model_class, model_name + '.joblib')
        if os.path.exists(file_path):
            os.remove(file_path)
            return jsonify(message={'message': 'File removed successfully'}), http.HTTPStatus.OK
        else:
            return jsonify(massage={'message': 'File not found'}), http.HTTPStatus.NOT_FOUND
