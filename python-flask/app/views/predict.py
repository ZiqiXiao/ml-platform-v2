import http
import shutil

from .utils import *
from ..utils import model_utils


def init_predict_routes(app, socketio):
    @app.route('/predict-data-upload-path', methods=['GET'])
    def predict_data_upload_path():
        try:
            return jsonify(Config.UPLOAD_TMP_PREDICT_FOLDER)
        except Exception as e:
            error_msg = f"Exception occurred: {str(e)}\n{traceback.format_exc()}"
            app.logger.error(error_msg)
            return jsonify(status=http.HTTPStatus.INTERNAL_SERVER_ERROR, message=error_msg)

    @app.route('/predict-template-headers', methods=['POST'])
    def predict_template_headers():
        try:
            template_path = request.get_json()['templatePath']
            headers = pd.read_csv(template_path, nrows=0).columns.tolist()
            return jsonify(status=http.HTTPStatus.OK, columnHeaders=headers)

        except Exception as e:
            error_msg = f"Exception occurred: {str(e)}\n{traceback.format_exc()}"
            app.logger.error(error_msg)
            return jsonify(status=http.HTTPStatus.INTERNAL_SERVER_ERROR, message=error_msg)

    @app.route('/make-prediction', methods=['POST'])
    def make_prediction():
        try:
            model_path = request.get_json()['modelPath']
            file_path = request.get_json()['filePath']
            result, result_path = model_utils.predict(app, file_path, model_path, socketio)
            return jsonify(status=http.HTTPStatus.OK, predictResult=result, predictResultPath=result_path)
        except Exception as e:
            error_msg = f"Exception occurred: {str(e)}\n{traceback.format_exc()}"
            app.logger.error(error_msg)
            return jsonify(status=http.HTTPStatus.INTERNAL_SERVER_ERROR, message=error_msg)

    @app.route('/download-predict-result', methods=['POST'])
    def download_predict_result():
        try:
            predict_result_path = request.get_json()['predictResultPath']
            if os.path.exists(predict_result_path):
                app.logger.info(f"Download predict result: {predict_result_path}")
                return send_file(predict_result_path, as_attachment=True)
            else:
                return jsonify(status=http.HTTPStatus.NOT_FOUND, message='Predict result not found')
        except Exception as e:
            error_msg = f"Exception occurred: {str(e)}\n{traceback.format_exc()}"
            app.logger.error(error_msg)
            return jsonify(status=http.HTTPStatus.INTERNAL_SERVER_ERROR, message=error_msg)

    @app.route('/save-predict-data', methods=['POST'])
    def save_predict_data():
        try:
            file_name = request.get_json()['fileName']
            file_path = request.get_json()['filePath']
            target_path = os.path.join(Config.UPLOAD_PREDICT_FOLDER, file_name)
            shutil.copyfile(os.path.join(file_path, file_name), target_path)
            return jsonify(status=http.HTTPStatus.OK, filePath=target_path)
        except Exception as e:
            error_msg = f"Exception occurred: {str(e)}\n{traceback.format_exc()}"
            app.logger.error(error_msg)
            return jsonify(status=http.HTTPStatus.INTERNAL_SERVER_ERROR, message=error_msg)
