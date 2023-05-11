import http

from .utils import *


def init_upload_routes(app, socketio):
    @app.route('/upload', methods=['POST'])
    def upload():
        file = request.files['file']
        path = request.form['path']
        overwrite = request.form['overwrite']
        print(overwrite)

        if file and allowed_file(file.filename):
            filename = secure_filename(file.filename)

            if os.path.exists(os.path.join(path, filename)) and overwrite == 'false':
                warnings_msg = f"File already exists: {filename}"
                app.logger.warning(warnings_msg)
                return jsonify(status=http.HTTPStatus.CONFLICT, message=warnings_msg)

            file.save(os.path.join(path, filename))
            app.logger.info(f"File uploaded: {filename}")
            return jsonify(status=http.HTTPStatus.OK)
        elif not allowed_file(file.filename):
            warning_msg = f"File type not allowed: {file.filename}"
            app.logger.warning(warning_msg)
            return jsonify(status=http.HTTPStatus.BAD_REQUEST, message=warning_msg)
