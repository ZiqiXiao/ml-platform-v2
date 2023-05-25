from flask import Flask, session
from flask_session import Session
from flask_cors import CORS
from config import Config
from flask_socketio import SocketIO, Namespace

from app.views import init_routes
from app.utils.log_utils import setup_logger


def create_app(config_class=Config):
    app = Flask(__name__)

    # @app.after_request
    # def add_cors_headers(response):
    #     response.headers['Access-Control-Allow-Origin'] = '*'
    #     response.headers['Access-Control-Allow-Methods'] = 'GET, POST, PUT, DELETE, OPTIONS'
    #     response.headers['Access-Control-Allow-Headers'] = 'Content-Type, Authorization'
    #     return response

    app.config.from_object(config_class)
    app.secret_key = config_class.SECRET_KEY
    CORS(app, resources={r"/*": {"origins": "http://127.0.0.1:3000", "methods": ["GET", "POST", "PUT", "DELETE"]}})
    # CORS(app, resources={r'*': {'origins': '*'}})
    socketio = SocketIO(app, cors_allowed_origins="http://127.0.0.1:3000")
    # socketio = SocketIO(app)

    # Setup logging
    setup_logger(app)

    # Initialize Session
    Session(app)

    init_routes(app, socketio)

    @app.route('/test')
    def test_page():
        return '<h1>I am rich</h1>'

    return app, socketio
