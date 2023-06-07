import http
import os
from datetime import datetime

import pandas as pd
import importlib

from flask import jsonify

from config import Config
import time
import traceback


# pending_event = []


def train_scheduler(
        app: object,
        socketio: object,
        filename: str,
        model: dict,
        mission: str,
        label: str = Config.DEFAULT_OTHER_PARAMS['label'],
        model_dict: dict = None,
):
    """Schedule the training process for the given model class and customized parameters 
    and return the trained model in a dictionary which also includes the headers of the 
    uploaded data as the templates for further usage. 

    Args:
        app (object):  The instance of the Flask App.
        filename (str): The name of uploaded file.
        model (dict): The dictionary of model and params.
        label (str, optional): The name of target column. Defaults to None.
        model_dict (dict, optional): The dictionary of trained model. Defaults to None.
        socketio (object, optional): The instance of socketio. Defaults to None.

    Returns:
        _type_: _description_
    """
    try:
        for m, p in model.items():

            for idx, val in p.items():
                if not val and Config.DEFAULT_PARAMS[m]:
                    p[idx] = Config.DEFAULT_PARAMS[m][idx]
            model = training(app, filename, mission, m, label, p, socketio)
            model_dict[m] = model
        model_dict['label'] = label
        # socketio.emit('training-finished')
        return model_dict
    except Exception as e:
        raise


def training(app, filename, mission, model_class, label='label', param=None, socketio=None):
    app.logger.info(
        f"Training {model_class} model with {filename} dataset and label {label}")
    # Load the dataset from the uploaded file
    dataset_path = os.path.join(Config.UPLOAD_TMP_TRAIN_FOLDER, filename)

    # Load the specified model module
    model_module_name = Config.MODEL_STRUCTURE[mission][model_class]
    model_module = importlib.import_module(model_module_name)

    # Initialize the model
    model = model_module.Model(app=app, socketio=socketio)

    # Train the model
    model_file = model.train(dataset_path, label, custom_params=param)

    app.logger.info(f"Training Success")
    return model_file


def predict(app, file_path, model_path, socketio=None):
    # Load the dataset from the uploaded file
    model_class = model_path.split(os.sep)[-2]
    mission = model_path.split(os.sep)[-3]
    model_module_name = Config.MODEL_STRUCTURE[mission][model_class]

    model_module = importlib.import_module(model_module_name)

    # Initialize the model
    model = model_module.Model(app=app, socketio=socketio)
    # Load the trained model
    model.load_model(model_path)
    # Predict
    try:
        result = model.predict(file_path)
    except Exception as e:
        app.logger.error(f"Predict Error: {str(e)}\n{traceback.format_exc()}")
        return

    app.logger.info(f"Predict Success")

    app.logger.info(result)
    result = pd.DataFrame(result, columns=['prediction'])
    # Save the result to the server
    save_filename = f'{datetime.now().strftime("%Y%m%d%H%M%S")}-{file_path.split(os.sep)[-1]}.csv'
    result_path = os.path.join(Config.PREDICT_RESULT_FOLDER, save_filename)
    result.to_csv(result_path, index=False)
    return result['prediction'].tolist(), result_path
