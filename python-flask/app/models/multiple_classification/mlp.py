import copy
import random

import torch
import torch.nn as nn
import torch.nn.functional as F
from torch.utils.data import Dataset, DataLoader
from torch import optim
import pandas as pd
from sklearn.model_selection import train_test_split
from app.models.multiple_classification.utils import cal_metrics, data_preprocess, load_dataset, cal_feature_importance, \
    log_message
import numpy as np
from config import Config


class MLP(nn.Module):
    """
    PyTorch MLP模型
    """

    def __init__(self, input_size, hidden_size, output_size=2):
        super(MLP, self).__init__()
        self.input_size = input_size
        self.hidden_size = hidden_size
        self.output_size = output_size
        self.fc1 = nn.Linear(self.input_size, self.hidden_size)
        self.fc2 = nn.Linear(self.hidden_size, self.hidden_size)
        self.fc3 = nn.Linear(self.hidden_size, self.output_size)

    def forward(self, x):
        x = F.relu(self.fc1(x))
        x = F.relu(self.fc2(x))
        x = self.fc3(x)
        return x


class MyDataset(Dataset):
    """
    数据集类
    """

    def __init__(self, data, label):
        self.data = torch.tensor(data.values, dtype=torch.float32)
        self.label = torch.tensor(label.values, dtype=torch.long).squeeze()

    def __len__(self):
        return len(self.data)

    def __getitem__(self, index):
        return self.data[index], self.label[index]


class Model:
    """
    PyTorch MLP模型
    """

    def __init__(self, model=None, app=None, socketio=None):
        self.model = model
        self.app = app
        self.socketio = socketio
        self.default_params = Config.DEFAULT_PARAMS['multiple_classification']['mlp'].copy()
        self.default_params.update(Config.DEFAULT_PARAMS_UNDER['multiple_classification']['mlp'])
        self.metric_data = {
            'modelName': 'mlp',
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
        self.default_params.pop('train_size')
        print(train_size)

        # 对数据集进行预处理，例如划分训练集和验证集
        train_X, valid_X, train_y, valid_y = data_preprocess(
            dataset, label, train_size=train_size)
        # self.app.logger.info('dataset split')
        train_dataset = MyDataset(train_X, train_y)
        valid_dataset = MyDataset(valid_X, valid_y)

        def seed_worker(worker_id):
            worker_seed = torch.initial_seed() % 2 ** 32
            np.random.seed(worker_seed)
            random.seed(worker_seed)

        # 创建DataLoader
        train_dataloader = DataLoader(train_dataset,
                                      batch_size=64,
                                      shuffle=True,
                                      worker_init_fn=seed_worker,
                                      generator=torch.Generator().manual_seed(0))
        valid_dataloader = DataLoader(valid_dataset,
                                      batch_size=64,
                                      shuffle=False,
                                      worker_init_fn=seed_worker,
                                      generator=torch.Generator().manual_seed(0))
        self.app.logger.info('dataset preprocessed')

        # 设置模型参数
        input_size = train_X.shape[1]
        hidden_size = custom_params.get(
            'hidden_size', self.default_params['hidden_size'])
        lr = custom_params.get('lr', self.default_params['lr'])
        num_epochs = custom_params.get(
            'num_epochs', self.default_params['num_epochs'])
        output_size = len(set(train_y.tolist() + valid_y.tolist()))

        # 初始化模型
        self.model = MLP(input_size, hidden_size, output_size)

        # 定义损失函数和优化器
        criterion = nn.CrossEntropyLoss()
        optimizer = optim.Adam(self.model.parameters(), lr=lr)
        self.app.logger.info('training started mlp ...')
        # 训练模型
        best_acc = 0.0
        best_model = copy.deepcopy(self.model)
        best_train_metrics = {}
        best_valid_metrics = {}
        torch.manual_seed(42)

        # self.app.logger.info(self.model.fc3.weight)

        for layer in self.model.children():
            if hasattr(layer, 'reset_parameters'):
                layer.reset_parameters()

        # self.app.logger.info(self.model.fc3.weight)

        from torch import argmax
        for epoch in range(num_epochs):
            self.model.train()
            train_loss = 0
            correct_train = 0
            total_train = 0

            for i, (inputs, targets) in enumerate(train_dataloader):
                outputs = self.model(inputs)
                loss = criterion(outputs, targets)
                train_loss += loss.item()
                _, predicted = torch.max(outputs.data, 1)
                total_train += targets.size(0)
                correct_train += (predicted == targets).sum().item()
                optimizer.zero_grad()
                loss.backward()
                optimizer.step()

            train_acc = correct_train / total_train
            train_loss = train_loss / len(train_dataloader)

            self.model.eval()
            valid_loss = 0
            correct_valid = 0
            total_valid = 0

            with torch.no_grad():
                for inputs, targets in valid_dataloader:
                    outputs = self.model(inputs)
                    loss = criterion(outputs, targets)
                    valid_loss += loss.item()
                    _, predicted = torch.max(outputs.data, 1)
                    total_valid += targets.size(0)
                    correct_valid += (predicted == targets).sum().item()

            valid_acc = correct_valid / total_valid
            valid_loss = valid_loss / len(valid_dataloader)

            if valid_acc > best_acc:
                best_acc = valid_acc
                best_model = copy.deepcopy(self.model)

            # print(f'Epoch: {epoch}, Train Loss: {train_loss:.4f}, Train Accuracy: {train_acc:.4f}, Valid Loss: {valid_loss:.4f}, Valid Accuracy: {valid_acc:.4f}')

            self.metric_data['epoch'].append(epoch)
            self.metric_data['trainLoss'].append(train_loss)
            self.metric_data['validLoss'].append(valid_loss)

            progress = (epoch + 1) / num_epochs * 100
            self.socketio.emit(
                'train-message', {'modelName': 'mlp', 'progress': progress})

        with torch.no_grad():
            y_true = np.array([])
            y_pred = np.array([])
            y_pred_praba = []

            for inputs, targets in train_dataloader:
                outputs = torch.nn.functional.softmax(best_model(inputs), dim=1)
                _, predicted = torch.max(outputs.data, 1)
                y_true = np.append(y_true, targets.tolist())
                y_pred = np.append(y_pred, predicted.tolist())
                y_pred_praba.append(outputs.tolist())
            y_pred_praba = np.concatenate(y_pred_praba, axis=0)
            best_train_metrics = cal_metrics(y_true, y_pred, y_pred_praba, type='train')

            y_true = np.array([])
            y_pred = np.array([])
            y_pred_praba = []

            for inputs, targets in valid_dataloader:
                outputs = torch.nn.functional.softmax(best_model(inputs), dim=1)
                _, predicted = torch.max(outputs.data, 1)
                y_true = np.append(y_true, targets.tolist())
                y_pred = np.append(y_pred, predicted.tolist())
                y_pred_praba.append(outputs.tolist())
            y_pred_praba = np.concatenate(y_pred_praba, axis=0)
            best_valid_metrics = cal_metrics(y_true, y_pred, y_pred_praba, type='valid')

        self.metric_data.update({"metrics": [best_train_metrics, best_valid_metrics]})
        self.socketio.emit('eval-message', self.metric_data)
        # 返回best_iter模型
        return best_model

    def save_model(self, model_path):
        torch.save(self.model, model_path)
        self.app.logger.info('model saved')

    def load_model(self, model_path):
        self.model = torch.load(model_path)
        self.app.logger.info('model loaded')

    def predict(self, dataset_path):
        if self.model is None:
            self.app.logger.warning(
                "Model not trained yet. Train the model before making predictions.")
        self.app.logger.info('predicting ... ')
        data = load_dataset(dataset_path)
        data = torch.tensor(data.values, dtype=torch.float32)
        self.model.eval()
        with torch.no_grad():
            outputs = self.model(data)
            _, predicted = torch.max(outputs.data, 1)
            return predicted.tolist()
