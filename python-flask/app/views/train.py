import http
import shutil

import joblib

from .utils import *


def init_train_routes(app, socketio):
    # Page for uploading the training data， and selecting the model

    @app.route('/check_label', methods=['POST'])
    def check_label():
        filename = request.form['filename']
        label = request.form['label']

        file_path = os.path.join(Config.UPLOAD_TRAIN_FOLDER, filename)
        df = pd.read_csv(file_path)

        if label in df.columns:
            app.logger.info(f"Label check passed: {label}")
            return jsonify(status='success')
        else:
            app.logger.warning(f"Label check failed: {label}")
            return jsonify(status='failure')

    @app.route('/model-params', methods=['GET'])
    def model_params():
        try:
            # print(request.headers)
            return jsonify(Config.DEFAULT_PARAMS)
        except Exception as e:
            error_msg = f"Exception occurred: {str(e)}\n{traceback.format_exc()}"
            app.logger.error(error_msg)
            return jsonify(status=http.HTTPStatus.INTERNAL_SERVER_ERROR, message=error_msg)

    @app.route('/train-data-upload-path', methods=['GET'])
    def train_data_upload_path():
        try:
            return jsonify(Config.UPLOAD_TMP_TRAIN_FOLDER)
        except Exception as e:
            error_msg = f"Exception occurred: {str(e)}\n{traceback.format_exc()}"
            app.logger.error(error_msg)
            return jsonify(status=http.HTTPStatus.INTERNAL_SERVER_ERROR, message=error_msg)

    @app.route('/get-train-labels', methods=['POST'])
    def get_train_labels():
        try:
            filepath = request.get_json()['filePath']
            file_path = os.path.join(Config.UPLOAD_TMP_TRAIN_FOLDER, filepath)
            df = pd.read_csv(file_path)
            return jsonify(status=http.HTTPStatus.OK, labels=df.columns.tolist())
        except Exception as e:
            error_msg = f"Exception occurred: {str(e)}\n{traceback.format_exc()}"
            app.logger.error(error_msg)
            return jsonify(status=http.HTTPStatus.INTERNAL_SERVER_ERROR, message=error_msg)

    @app.route('/start-train', methods=['Post'])
    def start_train():
        global model_dict
        model_dict = {}
        # 解析数据
        try:
            filepath = request.get_json()['filePath']
            models = request.get_json()['model']
            label = request.get_json()['label']

            model_dict['filepath'] = filepath

            threading.Thread(target=train_scheduler, args=(
                app, socketio, filepath, models, label, model_dict)).start()
            return jsonify(status=http.HTTPStatus.OK)
        except Exception as e:
            error_msg = f"Exception occurred: {str(e)}\n{traceback.format_exc()}"
            app.logger.error(error_msg)
            return jsonify(status=http.HTTPStatus.BAD_REQUEST, message=error_msg)

    @app.route('/save-model', methods=['Post'])
    def save_model():
        try:
            # app.logger.info(f"Model Dict Is {model_dict}")
            model_names = request.get_json()['modelName']
            model_classes = request.get_json()['modelClass']
            app.logger.info(f"{str(model_classes)}")
            upload_file = request.get_json()['uploadFile']
            file_name = upload_file['fileName']
            template_name = upload_file['templateName']
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
                pd.read_csv(cur_file_path, nrows=0).drop(axis=1, columns=[model_dict['label']])\
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
        except Exception as e:
            error_msg = f"Exception occurred: {str(e)}\n{traceback.format_exc()}"
            app.logger.error(error_msg)
            return jsonify(status=http.HTTPStatus.BAD_REQUEST, message=error_msg)
