from .train import init_train_routes
from .predict import init_predict_routes
from .upload import init_upload_routes


def init_routes(app, socketio):
    init_train_routes(app, socketio)
    init_upload_routes(app, socketio)
    init_predict_routes(app, socketio)
