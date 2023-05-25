import http

from .utils import *
from ..utils import model_utils


def init_predict_routes(app, socketio):
    @app.route('/predict-data-upload-path', methods=['GET'])
    def predict_data_upload_path():
        return jsonify(Config.UPLOAD_TMP_PREDICT_FOLDER)

    @app.route('/predict-template-headers', methods=['POST'])
    def predict_template_headers():
        template_path = request.get_json()['templatePath']
        headers = pd.read_csv(template_path, nrows=0).columns.tolist()
        return jsonify(columnHeaders=headers), http.HTTPStatus.OK

    @app.route('/make-prediction', methods=['POST'])
    def make_prediction():
        model_path = request.get_json()['modelPath']
        file_path = request.get_json()['filePath']
        result, result_path = model_utils.predict(app, file_path, model_path, socketio)
        return jsonify(predictResult=result, predictResultPath=result_path), http.HTTPStatus.OK

    @app.route('/download-predict-result', methods=['POST'])
    def download_predict_result():
        predict_result_path = request.get_json()['predictResultPath']
        if os.path.exists(predict_result_path):
            app.logger.info(f"Download predict result: {predict_result_path}")
            return send_file(predict_result_path, as_attachment=True)
        else:
            return jsonify(message='Predict result not found'), http.HTTPStatus.NOT_FOUND

    @app.route('/save-predict-data', methods=['POST'])
    @app.route('/overwrite-predict-data', methods=['POST'])
    def save_predict_data():
        file_name = request.get_json()['fileName'] + '.csv'
        file_path = request.get_json()['filePath']
        target_path = os.path.join(Config.UPLOAD_PREDICT_FOLDER, file_name)
        os.rename(os.path.join(file_path, file_name), target_path)
        return jsonify(filePath=target_path), http.HTTPStatus.OK

    @app.route('/delete-predict-data', methods=['POST'])
    def delete_predict_data():
        file_name = request.get_json()['fileName']
        file_path = os.path.join(Config.UPLOAD_PREDICT_FOLDER, file_name + '.csv')
        if os.path.exists(file_path):
            os.remove(file_path)
            return jsonify(message={'message': 'File removed successfully'}), http.HTTPStatus.OK
        else:
            return jsonify(massage={'message': 'File not found'}), http.HTTPStatus.NOT_FOUND

    @app.route('/rename-predict-data', methods=['POST'])
    def rename_predict_data():
        file_name = request.get_json()['fileName']
        new_file_name = request.get_json()['newName']
        file_path = os.path.join(Config.UPLOAD_PREDICT_FOLDER, file_name + '.csv')
        new_file_path = os.path.join(Config.UPLOAD_PREDICT_FOLDER, new_file_name + '.csv')
        if os.path.exists(file_path):
            os.rename(file_path, new_file_path)
            return jsonify(message={'message': 'File renamed successfully'}, newPath=new_file_path), http.HTTPStatus.OK
        else:
            return jsonify(massage={'message': 'File not found'}), http.HTTPStatus.NOT_FOUND
