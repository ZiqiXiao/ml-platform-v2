from sklearn.svm import SVC
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error
from app.models.multiple_classification.utils import cal_metrics, data_preprocess, load_dataset, cal_feature_importance
import os
import joblib
import pandas as pd
import numpy as np
from config import Config


class Model:
    """
    SVR模型
    """

    def __init__(self, model=None, app=None, socketio=None):
        self.model = model
        self.app = app
        self.socketio = socketio
        self.default_params = Config.DEFAULT_PARAMS['multiple_classification']['svc'].copy()
        self.default_params.update(Config.DEFAULT_PARAMS_UNDER['multiple_classification']['svc'])
        self.metric_data = {
            'modelName': 'svc',
            'epoch': [],
            'trainLoss': [],
            'validLoss': [],
        }

    def log_message(self, message):
        if self.socketio is not None:
            self.socketio.emit('training_log', {'message': message})

    def train(self, dataset_path, label, custom_params={}):
        # 加载数据集
        dataset = load_dataset(dataset_path)
        self.app.logger.info('dataset loaded')

        # 设置模型参数
        self.default_params.update(custom_params)
        train_size = self.default_params.get(
            'train_size', Config.DEFAULT_OTHER_PARAMS['train_size'])
        self.default_params.pop('train_size')
        print(train_size)

        # 对数据集进行预处理，例如划分训练集和验证集
        train_X, valid_X, train_y, valid_y = data_preprocess(
            dataset, label, train_size=train_size)
        self.app.logger.info('dataset split')

        # 训练模型
        self.app.logger.info('training model svr ... ')
        self.model = SVC(**self.default_params)
        self.model.fit(train_X, train_y)

        train_metrics = cal_metrics(
            train_y,
            self.model.predict(train_X),
            self.model.predict_proba(train_X),
            type='train')
        valid_metrics = cal_metrics(
            valid_y,
            self.model.predict(valid_X),
            self.model.predict_proba(valid_X),
            type='valid')

        self.socketio.emit('train-message',
                           {'modelName': 'svc',
                            'progress': 100,
                            })

        self.metric_data.update({"metrics": [train_metrics, valid_metrics]})
        self.metric_data.update({'featureImportance': cal_feature_importance(self.model)})

        self.socketio.emit('eval-message', self.metric_data)
        self.app.logger.info('training successfully')
        return self.model

    def predict(self, dataset_path):
        if self.model is None:
            self.app.logger.info(
                "Model not trained yet. Train the model before making predictions.")
        self.app.logger.info('predicting ... ')
        data = load_dataset(dataset_path)
        predicted = self.model.predict(data)
        return predicted

    def save_model(self, model_path):
        if self.model is None:
            self.app.logger.warning(
                "Model not trained yet. Train the model before saving.")
        joblib.dump(self.model, model_path)
        self.app.logger.info('model saved')

    def load_model(self, model_path):
        self.model = joblib.load(model_path)
        self.app.logger.info('model loaded')
