from mlens.ensemble import SuperLearner
from sklearn.linear_model import LogisticRegression
from sklearn.ensemble import RandomForestClassifier
from sklearn.svm import SVC
from sklearn.model_selection import train_test_split
from sklearn.metrics import log_loss, accuracy_score
from app.models.binary_classification.utils import cal_metrics, data_preprocess, load_dataset, cal_feature_importance
import os
import joblib
import pandas as pd
import numpy as np
from config import Config


class Model:
    """
    Logistic Regression模型
    """

    def __init__(self, model=None, app=None, socketio=None):
        self.model = model
        self.app = app
        self.socketio = socketio
        self.default_params = Config.DEFAULT_PARAMS['binary_classification']['sl'].copy(
        )
        self.default_params.update(
            Config.DEFAULT_PARAMS_UNDER['binary_classification']['sl'])
        self.metric_data = {
            'modelName': 'sl',
            'epoch': [],
            'trainLoss': [],
            'validLoss': [],
        }

    def train(self, dataset_path, label, custom_params={}):
        # 加载数据集
        dataset = load_dataset(dataset_path)
        self.app.logger.info('dataset loaded')

        # 设置模型参数
        self.default_params.update(custom_params)
        train_size = self.default_params.get(
            'train_size', Config.DEFAULT_OTHER_PARAMS['train_size'])
        print(train_size)

        # 对数据集进行预处理，例如划分训练集和验证集
        train_X, valid_X, train_y, valid_y = data_preprocess(
            dataset, label, train_size=train_size)
        self.app.logger.info('dataset split')

        # 训练模型
        # self.app.logger.info('training model logreg ... ')
        # self.model = LogisticRegression(**self.default_params)
        seed = 2017

        rf_param = self.default_params['rf']
        svc_param = self.default_params['svc']
        lg_param = self.default_params['lg']

        self.model = SuperLearner(random_state=seed)
        # Build the first layer
        self.model.add([RandomForestClassifier(**rf_param), SVC(**svc_param)])
        # Attach the final meta estimator
        self.model.add_meta(LogisticRegression(**lg_param), proba=True)

        self.model.fit(train_X, train_y)

        train_pred_proba = self.model.predict_proba(train_X)
        valid_pred_proba = self.model.predict_proba(valid_X)

        train_pred = train_pred_proba.argmax(axis=1)
        valid_pred = valid_pred_proba.argmax(axis=1)

        train_metrics = cal_metrics(
            train_y,
            train_pred,
            train_pred_proba[:, 1],
            type='train')
        valid_metrics = cal_metrics(
            valid_y,
            valid_pred,
            valid_pred_proba[:, 1],
            type='valid')

        self.socketio.emit('train-message', {'modelName': 'sl', 'progress': 100})
        self.metric_data.update({"metrics": [train_metrics, valid_metrics]})
        # self.metric_data.update({'featureImportance': cal_feature_importance(self.model)})
        self.app.logger.info(train_metrics)
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
