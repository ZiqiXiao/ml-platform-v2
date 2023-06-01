from itertools import cycle

import numpy as np
import pandas as pd
from sklearn.preprocessing import label_binarize

from config import Config
from sklearn.metrics import mean_squared_error, mean_absolute_error, r2_score, accuracy_score, confusion_matrix, \
    roc_auc_score, roc_curve, ConfusionMatrixDisplay, RocCurveDisplay, auc
from sklearn.model_selection import train_test_split
from matplotlib import pyplot as plt
import datetime


def cal_confusion_matrix(y_true, y_pred, type):
    plt.figure()
    cm = confusion_matrix(y_true, y_pred)
    disp = ConfusionMatrixDisplay(confusion_matrix=cm)
    disp.plot(cmap='Blues')
    save_path = f"data/tmp/confusion_matrix/{datetime.datetime.now().strftime('%Y%m%d%H%M%S')}-{type}-confusion_matrix.png"
    plt.savefig(save_path)
    return save_path


def cal_roc_curve(y_true, y_pred_proba, type):
    plt.figure()
    y_test_bin = label_binarize(y_true, classes=np.unique(y_true))
    n_classes = y_test_bin.shape[1]
    # 计算每一类的ROC
    fpr = dict()
    tpr = dict()
    roc_auc = dict()
    for i in range(n_classes):
        fpr[i], tpr[i], _ = roc_curve(y_test_bin[:, i], y_pred_proba[:, i])
        roc_auc[i] = auc(fpr[i], tpr[i])
    # 绘制所有的ROC曲线
    colors = cycle(['aqua', 'darkorange', 'cornflowerblue', 'deeppink', 'navy', 'red', 'green', 'yellow', 'black'])
    for i, color in zip(range(n_classes), colors):
        plt.plot(fpr[i], tpr[i], color=color, label='ROC curve of class {0} (area = {1:0.2f})'.format(i, roc_auc[i]))

    plt.plot([0, 1], [0, 1], 'k--')
    plt.xlim([0.0, 1.0])
    plt.ylim([0.0, 1.05])
    plt.xlabel('False Positive Rate')
    plt.ylabel('True Positive Rate')
    plt.title('Receiver operating characteristic for multi-class data')
    plt.legend(loc="lower right")
    plt.show()
    save_path = f"data/tmp/roc_curve/{datetime.datetime.now().strftime('%Y%m%d%H%M%S')}-{type}-roc_curve.png"
    plt.savefig(save_path)
    return save_path, roc_auc


def cal_feature_importance(model):
    plt.figure()
    if hasattr(model, 'get_score'):
        fi_df = pd.DataFrame({'feature': list(model.get_score(importance_type='gain').keys()),
                              'gain': list(model.get_score(importance_type='gain').values())})
        fi_df = fi_df.sort_values(by='gain', ascending=True)
        plt.barh(fi_df.feature, fi_df.gain)
    elif hasattr(model, 'coef_'):
        fi_df = pd.DataFrame({'feature': model.feature_names_in_, 'coef': abs(model.coef_[0])})
        fi_df = fi_df.sort_values(by='coef', ascending=True)
        plt.barh(fi_df.feature, fi_df.coef)
    elif hasattr(model, 'feature_importances_'):
        fi_df = pd.DataFrame({'feature': model.feature_names_in_, 'importance': model.feature_importances_})
        fi_df = fi_df.sort_values(by='importance', ascending=True)
        plt.barh(fi_df.feature, fi_df.importance)

    plt.tight_layout()
    plt.plot()
    save_path = f"data/tmp/feature_importance/{datetime.datetime.now().strftime('%Y%m%d%H%M%S')}-feature_importance.png"
    plt.savefig(save_path)
    return save_path


def cal_metrics(y_true, y_pred, y_pred_proba, type='train'):
    print(y_pred_proba)
    acc = accuracy_score(y_true, y_pred)

    mse = mean_squared_error(y_true, y_pred)

    rmse = mean_squared_error(y_true, y_pred, squared=False)

    mae = mean_absolute_error(y_true, y_pred)

    r2 = r2_score(y_true, y_pred)

    cm = cal_confusion_matrix(y_true, y_pred, type)

    roc, auc_scores = cal_roc_curve(y_true, y_pred_proba, type)

    return {
        'dataset': f'{type}',
        'acc': float(acc),
        'mse': float(mse),
        'mae': float(mae),
        'rmse': float(rmse),
        'r2': float(r2),
        'auc': auc_scores,
        'roc': roc,
        'cm': cm,
    }


def log_message(socketio, message):
    if socketio is not None:
        socketio.emit('training_log', {'message': message})


def load_dataset(dataset_path):
    # 加载数据集
    dataset = pd.read_csv(dataset_path)
    return dataset


def data_preprocess(dataset, label, train_size):
    # 对数据集进行预处理，例如划分训练集和验证集，并转换为xgboost需要的数据格式
    train_X, valid_X, train_y, valid_y = train_test_split(dataset.drop(label, axis=1), dataset[label],
                                                          train_size=train_size, random_state=42)
    return train_X, valid_X, train_y, valid_y
