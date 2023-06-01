"""
Configuration file for the app.

This file contains the configuration for the app.

BASE_DIR: The base directory of the app.
Config: The configuration class for the app.
"""
import os
import secrets

BASE_DIR = os.path.abspath(os.path.dirname(__file__))


class Config:
    """Config class for the app.

    This class contains the configuration for the app.
    """
    SECRET_KEY = secrets.token_hex(16)
    """The secret key for the app."""

    # Sqlite database
    # SQLALCHEMY_DATABASE_URI = os.environ.get('SQLALCHEMY_DATABASE_URI') or 'sqlite:///' + os.path.join(BASE_DIR, 'data',
    #                                                                                                    'app.db')
    """The database URI for the app."""
    SQLALCHEMY_TRACK_MODIFICATIONS = False
    """The database track modification for the app."""

    # Upload folder
    UPLOAD_TRAIN_FOLDER = os.path.join(BASE_DIR, 'data', 'uploads', 'train')
    """The upload folder for training data."""
    UPLOAD_PREDICT_FOLDER = os.path.join(
        BASE_DIR, 'data', 'uploads', 'predict')
    """The upload folder for prediction data."""
    UPLOAD_TMP_TRAIN_FOLDER = os.path.join(
        BASE_DIR, 'data', 'uploads', 'tmp', 'train')
    UPLOAD_TMP_PREDICT_FOLDER = os.path.join(
        BASE_DIR, 'data', 'uploads', 'tmp', 'predict')

    ALLOWED_EXTENSIONS = {'csv'}
    """The allowed extensions for the uploading files."""

    """A dictionary stores all the name of model and its package to be imported."""

    # Model Storage folder
    MODEL_FOLDER = {
        'linear': os.path.join(BASE_DIR, 'data', 'models', 'linear'),
        'binary_classification': os.path.join(BASE_DIR, 'data', 'models', 'binary_classification'),
        'multiple_classification': os.path.join(BASE_DIR, 'data', 'models', 'multiple_classification')
    }
    """The folder to store the model."""

    # Template Storage folder
    TEMPLATE_FOLDER = os.path.join(BASE_DIR, 'data', 'model_templates')
    """The folder to store the data template, such that can be downloaded to be used to fill."""

    # Predict result folder
    PREDICT_RESULT_FOLDER = os.path.join(BASE_DIR, 'data', 'predict_result')
    """The folder to store the prediction result."""

    # Log folder
    LOG_FOLDER = os.path.join(BASE_DIR, 'data', 'logs')
    """The folder to store the log."""

    # Default Values
    # Default Tolerance
    DEFAULT_TOLERANCE = 0.15
    """The default tolerance to consider a prediction as correct."""

    # Model Structure files
    MODEL_STRUCTURE = {
        'linear': {
            'xgboost': 'app.models.linear.xgboost',
            'mlp': 'app.models.linear.mlp',
            'svr': 'app.models.linear.svr',
            'lr': 'app.models.linear.lr',
            'rf': 'app.models.linear.rf', },

        'binary_classification': {
            'lg': 'app.models.binary_classification.lg',
            'xgboost': 'app.models.binary_classification.xgboost',
            'svc': 'app.models.binary_classification.svc',
            'mlp': 'app.models.binary_classification.mlp',
            'rf': 'app.models.binary_classification.rf', },

        'multiple_classification': {
            'lg': 'app.models.multiple_classification.lg',
            'xgboost': 'app.models.multiple_classification.xgboost',
            'mlp': 'app.models.multiple_classification.mlp',
            'svc': 'app.models.multiple_classification.svc',
            'rf': 'app.models.multiple_classification.rf', },
    }

    DEFAULT_PARAMS = {
        'linear': {
            'xgboost': {'num_boost_round': 20,
                        'eta': 0.1,
                        'max_depth': 6,
                        'subsample': 0.8,
                        'colsample_bytree': 0.8, },

            'mlp': {'num_epochs': 200,
                    'hidden_size': 64,
                    'lr': 0.01, },

            'svr': {},

            'lr': {},

            'rf': {'n_estimators': 100,
                   'max_depth': 4,
                   'random_state': 42}, },

        'binary_classification': {
            'lg': {'max_iter': 200},

            'xgboost': {'num_boost_round': 20,
                        'eta': 0.1,
                        'max_depth': 6,
                        'subsample': 0.8,
                        'colsample_bytree': 0.8, },

            'svc': {},

            'mlp': {'num_epochs': 200,
                    'hidden_size': 32,
                    'lr': 0.001, },

            'rf': {'n_estimators': 100,
                   'max_depth': 4,
                   'random_state': 42},
        },
        'multiple_classification': {
            'lg': {'max_iter': 200},

            'xgboost': {'num_boost_round': 20,
                        'eta': 0.1,
                        'max_depth': 6,
                        'subsample': 0.8,
                        'colsample_bytree': 0.8, },

            'mlp': {'num_epochs': 200,
                    'hidden_size': 32,
                    'lr': 0.001, },

            'svc': {},

            'rf': {'n_estimators': 100,
                   'max_depth': 4,
                   'random_state': 42},
        },

    }
    """The default parameters(hyperparameter) for the models that is showed on the frontpage."""

    DEFAULT_PARAMS_UNDER = {
        'linear': {
            'xgboost': {'objective': 'reg:squarederror', },

            'mlp': {},

            'svr': {'kernel': 'linear',
                    'gamma': 'scale',
                    'C': 1.0, },

            'lr': {},

            'rf': {},
        },

        'binary_classification': {
            'lg': {'penalty': 'l2', },

            'xgboost': {'objective': 'binary:logistic', },

            'svc': {'probability': True,
                    'kernel': 'linear',
                    'gamma': 'scale',
                    'C': 1.0, },

            'mlp': {},

            'rf': {},
        },

        'multiple_classification': {
            'lg': {'penalty': 'l2', },

            'xgboost': {'objective': 'multi:softprob', },

            'mlp': {},

            'svc': {'probability': True,
                    'kernel': 'linear',
                    'gamma': 'scale',
                    'C': 1.0, },

            'rf': {},
        },

    }
    """The default parameters(hyperparameter) for the models that is not showed on the frontpage."""

    DEFAULT_OTHER_PARAMS = {
        'train_size': 0.8,
        'label': 'label',
    }
