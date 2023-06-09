import xgboost as xgb
from numpy import argmax
from xgboost.callback import TrainingCallback
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score, mean_squared_error
import os
import joblib
import pandas as pd
from app.models.multiple_classification.utils import cal_metrics, load_dataset, data_preprocess, cal_feature_importance
import numpy as np
from config import Config


class TrainingProgressCallback(TrainingCallback):
    """
    计算训练进度的回调函数
    """

    def __init__(self, total_rounds, socketio=None, metric_data=None):
        super().__init__()
        self.count = 0
        self.total_rounds = total_rounds
        self.socketio = socketio
        self.metric_data = metric_data

    def after_iteration(self, model, epoch, evals_log):
        self.count += 1
        progress = self.count / self.total_rounds * 100
        if self.socketio is not None:
            self.socketio.emit('train-message',
                               {'modelName': 'xgboost',
                                'progress': progress,
                                })
        else:
            print(f'training progress: {progress}%')

        self.metric_data['epoch'].append(self.count)
        self.metric_data['trainLoss'].append(evals_log['train']['mlogloss'][-1])
        self.metric_data['validLoss'].append(evals_log['valid']['mlogloss'][-1])


class Model:
    """
    XGBoost模型
    """

    def __init__(self, model=None, app=None, socketio=None):
        self.model = model
        self.app = app
        self.socketio = socketio
        self.default_params = Config.DEFAULT_PARAMS['multiple_classification']['xgboost'].copy()
        self.default_params.update(Config.DEFAULT_PARAMS_UNDER['multiple_classification']['xgboost'])
        self.metric_data = {
            'modelName': 'xgboost',
            'epoch': [],
            'trainLoss': [],
            'validLoss': [],
        }

    def train(self, dataset_path, label, custom_params={}):
        # 设置模型参数
        self.default_params.update(custom_params)
        num_boost_round = self.default_params.pop('num_boost_round')
        train_size = self.default_params.get(
            'train_size', Config.DEFAULT_OTHER_PARAMS['train_size'])
        self.default_params.pop('train_size')

        # 加载数据集
        dataset = load_dataset(dataset_path)

        self.default_params['num_class'] = len(np.unique(dataset[label]))

        # 对数据集进行预处理，例如划分训练集和验证集，并转换为xgboost需要的数据格式
        train_X, valid_X, train_y, valid_y = data_preprocess(dataset, label, train_size=train_size)
        # print('dataset split')

        dtrain = xgb.DMatrix(train_X, label=train_y)
        dvalid = xgb.DMatrix(valid_X, label=valid_y)

        # 训练模型
        # print('training model ... ')
        self.app.logger.info('training model xgb ... ')

        progress_callback = TrainingProgressCallback(num_boost_round, self.socketio, self.metric_data)

        self.model = xgb.train(
            self.default_params,
            dtrain,
            num_boost_round=num_boost_round,
            evals=[(dtrain, 'train'), (dvalid, 'valid')],
            early_stopping_rounds=5,
            verbose_eval=0,
            callbacks=[progress_callback]
        )

        train_metrics = cal_metrics(
            np.array(train_y),
            np.array(argmax(self.model.predict(dtrain), axis=1)),
            np.array(self.model.predict(dtrain)),
            type='train')
        valid_metrics = cal_metrics(
            np.array(valid_y),
            np.array(argmax(self.model.predict(dvalid), axis=1)),
            np.array(self.model.predict(dvalid)),
            type='valid')

        self.metric_data.update({"metrics": [train_metrics, valid_metrics]})
        self.metric_data.update({'featureImportance': cal_feature_importance(self.model)})
        self.socketio.emit('eval-message', self.metric_data)

        print('training successfully')
        self.app.logger.info('training successfully')
        return self.model

    def predict(self, dataset_path):
        if self.model is None:
            self.app.logger.info(
                "Model not trained yet. Train the model before making predictions.")
        self.app.logger.info('predicting ... ')
        data = load_dataset(dataset_path)
        dtest = xgb.DMatrix(data)
        predicted = np.array(list(1 if i > 0.5 else 0 for i in self.model.predict(dtest)))
        return predicted

    def save_model(self, model_path):
        if self.model is None:
            self.app.logger.warning(
                "Model not trained yet. Train the model before saving.")
        joblib.dump(self.model, model_path)
        self.app.logger.info('model saved')

    def load_model(self, model_path):
        self.model = joblib.load(model_path)
        self.app.logger.info('model loaded' + str(self.model))
