import http

from .train import init_train_routes
from .predict import init_predict_routes
from .upload import init_upload_routes
from .utils import *


def init_routes(app, socketio):
    @app.errorhandler(Exception)
    def handle_exception(e):
        error_msg = f"Exception occurred: {str(e)}\n{traceback.format_exc()}"
        app.logger.error(error_msg)
        return jsonify(message=error_msg), http.HTTPStatus.INTERNAL_SERVER_ERROR

    init_train_routes(app, socketio)
    init_upload_routes(app, socketio)
    init_predict_routes(app, socketio)
