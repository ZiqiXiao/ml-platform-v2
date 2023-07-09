import http
from datetime import datetime

from .utils import *


def init_upload_routes(app, socketio):
    @app.route('/upload', methods=['POST'])
    def upload():
        file = request.files['file']
        path = request.form['path']
        # overwrite = request.form['overwrite']
        # print(overwrite)

        if file and allowed_file(file.filename):
            filename = secure_filename(file.filename)

            # if os.path.exists(os.path.join(path, filename)) and overwrite == 'false':
            #     warnings_msg = f"File already exists: {filename}"
            #     app.logger.warning(warnings_msg)
            #     return jsonify(status=http.HTTPStatus.CONFLICT, message=warnings_msg)

            file.save(os.path.join(path, filename))
            app.logger.info(f"File uploaded: {filename}")
            return jsonify(status=http.HTTPStatus.OK)
        elif not allowed_file(file.filename):
            warning_msg = f"File type not allowed: {file.filename}"
            app.logger.warning(warning_msg)
            return jsonify(status=http.HTTPStatus.BAD_REQUEST, message=warning_msg)

    @app.route('/upload-fill-data', methods=['POST'])
    def upload_fill_data():
        try:
            data = request.get_json()['fillData']
            df = pd.DataFrame(data)
            file_path = os.path.join(Config.UPLOAD_TMP_PREDICT_FOLDER, f'fill_data_{datetime.timestamp(datetime.today())}.csv')
            df.to_csv(file_path, index=False)
            # app.logger.info(file_path)
            return jsonify(status=http.HTTPStatus.OK, filePath=file_path)

        except Exception as e:
            error_msg = f"Exception occurred: {str(e)}\n{traceback.format_exc()}"
            app.logger.error(error_msg)
            return jsonify(status=http.HTTPStatus.INTERNAL_SERVER_ERROR, message=error_msg)
