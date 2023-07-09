export default {
  "linear": {
    "lr": {
      "train_size":
        {
          "name":"训练集比例",
          "explain": "训练集的比例设置，一般为0.7-0.9，剩余的为测试集",
        },
    },
    "mlp": {
      "num_epochs":
          {
            "name": "迭代次数",
            "explain": "迭代次数越多拟合效果越好，但是也越容易过拟合",
          },
      "hidden_size":
          {
            "name": "隐藏层大小",
            "explain": "隐藏层大小越大模型复杂程度越高",
          },
      "lr":
          {
            "name": "学习率",
            "explain": "学习率越大，模型收敛速度越快，但是也越容易错过最优解",
          },
      "train_size":
        {
          "name":"训练集比例",
          "explain": "训练集的比例设置，一般为0.7-0.9，剩余的为测试集",
        },
    },
    "svr": {
      "kernel":
          {
            "name": "核函数类型",
            "explain": ""
          },
      "gamma":
          {
            "name": "核函数系数",
            "explain": ""
          },
      "C":
          {
            "name": "惩罚系数",
            "explain": ""
          },
      "train_size":
        {
          "name":"训练集比例",
          "explain": "训练集的比例设置，一般为0.7-0.9，剩余的为测试集",
        },
    },
    "rf": {
      "random_state":
          {
            "name": "随机种子",
            "explain": "决定随机数生成器的种子，以便于结果可重复"
          },
      "n_estimators":
          {
            "name": "决策树数量",
            "explain": "决策树数量越多，模型复杂程度越高，也越容易过拟合"
          },
      "max_depth":
          {
            "name": "最大树深",
            "explain": "最大树深越大，模型复杂程度越高，也越容易过拟合"
          },
      'min_samples_split':
        {
          "name": "最小分裂样本",
          "explain": "内部节点再划分所需最小样本数"
        },
      'min_samples_leaf':
        {
          "name": "最小叶子节点样本数",
          "explain": "最小叶子节点所需的样本数，如果某叶子节点数目小于样本数，则会与兄弟节点一起被剪枝"
        },
      'min_weight_fraction_leaf':
        {
          "name": "最小叶子节点样本权重和",
          "explain": "位于叶节点所需的(所有输入样本的)权重总和的最小加权分数。当没有提供sample_weight时，样本具有相等的权重。"
        },
      'min_impurity_decrease':
        {
          "name": "最小不纯度减少量",
          "explain": "在寻找最佳分割时需要考虑的特征数量"
        },
      "train_size":
        {
          "name":"训练集比例",
          "explain": "训练集的比例设置，一般为0.7-0.9，剩余的为测试集",
        },
    },
    "xgboost": {
      "num_boost_round":
          {
            "name": "提升轮数",
            "explain": "提升轮数越多，模型复杂程度越高，也越容易过拟合"
          },
      "eta":
          {
            "name":  "学习率",
            "explain": "学习率越大，模型收敛速度越快，但是也越容易错过最优解"
          },
      "max_depth":
          {
            "name": "最大树深",
            "explain": "最大树深越大，模型复杂程度越高，也越容易过拟合"
          },
      "subsample":
          {
            "name": "子采样比例",
            "explain": "适当降低子采样比例可以防止过拟合"
          },
      "colsample_bytree":
          {
            "name": "树的列采样比例",
            "explain": "适当降低树的列采样比例可以防止过拟合"
          },
      "objective":
          {
            "name": "目标函数",
            "explain": ""
          },
      "train_size":
        {
          "name":"训练集比例",
          "explain": "训练集的比例设置，一般为0.7-0.9，剩余的为测试集",
        },
    },
    "sl": {
      "rf": {
        "random_state":
            {
              "name": "随机种子",
              "explain": "决定随机数生成器的种子，以便于结果可重复"
            },
        "n_estimators":
            {
              "name": "决策树数量",
              "explain": "决策树数量越多，模型复杂程度越高，也越容易过拟合"
            },
        "max_depth":
            {
              "name": "最大树深",
              "explain": "最大树深越大，模型复杂程度越高，也越容易过拟合"
            },
        'min_samples_split':
          {
            "name": "最小分裂样本",
            "explain": "内部节点再划分所需最小样本数"
          },
        'min_samples_leaf':
          {
            "name": "最小叶子节点样本数",
            "explain": "最小叶子节点所需的样本数，如果某叶子节点数目小于样本数，则会与兄弟节点一起被剪枝"
          },
        'min_weight_fraction_leaf':
          {
            "name": "最小叶子节点样本权重和",
            "explain": "位于叶节点所需的(所有输入样本的)权重总和的最小加权分数。当没有提供sample_weight时，样本具有相等的权重。"
          },
        'min_impurity_decrease':
          {
            "name": "最小不纯度减少量",
            "explain": "在寻找最佳分割时需要考虑的特征数量"
          },
        "train_size":
          {
            "name":"训练集比例",
            "explain": "训练集的比例设置，一般为0.7-0.9，剩余的为测试集",
          },
      },
      "svr": {
        "kernel":
            {
              "name":  "核函数类型",
              "explain": ""
            },
        "gamma":
            {
              "name":  "核函数系数",
              "explain": ""
            },
        "C":
            {
              "name": "惩罚系数",
              "explain": ""
            },
        "train_size":
          {
            "name":"训练集比例",
            "explain": "训练集的比例设置，一般为0.7-0.9，剩余的为测试集",
          },
      },
      "lr": {
        "train_size":
          {
            "name":"训练集比例",
            "explain": "训练集的比例设置，一般为0.7-0.9，剩余的为测试集",
          },
      },
    }
  },

  "binary_classification": {
    "lg": {
      "max_iter":
          {
            "name": "最大迭代次数",
            "explain": "最大迭代次数越多，模型复杂程度越高，也越容易过拟合"
          },
      "train_size":
        {
          "name":"训练集比例",
          "explain": "训练集的比例设置，一般为0.7-0.9，剩余的为测试集",
        },
    },
    "xgboost": {
      "num_boost_round":
          {
            "name": "提升轮数",
            "explain": "提升轮数越多，模型复杂程度越高，也越容易过拟合"
          },
      "eta":
          {
            "name":  "学习率",
            "explain": "学习率越大，模型收敛速度越快，但是也越容易错过最优解"
          },
      "max_depth":
          {
            "name": "最大树深",
            "explain": "最大树深越大，模型复杂程度越高，也越容易过拟合"
          },
      "subsample":
          {
            "name": "子采样比例",
            "explain": "适当降低子采样比例可以防止过拟合"
          },
      "colsample_bytree":
          {
            "name": "树的列采样比例",
            "explain": "适当降低树的列采样比例可以防止过拟合"
          },
      "objective":
          {
            "name": "目标函数",
            "explain": ""
          },
      "train_size":
        {
          "name":"训练集比例",
          "explain": "训练集的比例设置，一般为0.7-0.9，剩余的为测试集",
        },
    },
    "svc": {
      "kernel":
          {
            "name":  "核函数类型",
            "explain": ""
          },
      "gamma":
          {
            "name":  "核函数系数",
            "explain": ""
          },
      "C":
          {
            "name": "惩罚系数",
            "explain": ""
          },
      "train_size":
        {
          "name":"训练集比例",
          "explain": "训练集的比例设置，一般为0.7-0.9，剩余的为测试集",
        },
    },
    "mlp": {
      "num_epochs":
          {
            "name": "迭代次数",
            "explain": "迭代次数越多拟合效果越好，但是也越容易过拟合",
          },
      "hidden_size":
          {
            "name": "隐藏层大小",
            "explain": "隐藏层大小越大模型复杂程度越高",
          },
      "lr":
          {
            "name": "学习率",
            "explain": "学习率越大，模型收敛速度越快，但是也越容易错过最优解",
          },
      "train_size":
        {
          "name":"训练集比例",
          "explain": "训练集的比例设置，一般为0.7-0.9，剩余的为测试集",
        },
    },
    "rf": {
      "random_state":
          {
            "name": "随机种子",
            "explain": "决定随机数生成器的种子，以便于结果可重复"
          },
      "n_estimators":
          {
            "name": "决策树数量",
            "explain": "决策树数量越多，模型复杂程度越高，也越容易过拟合"
          },
      "max_depth":
          {
            "name": "最大树深",
            "explain": "最大树深越大，模型复杂程度越高，也越容易过拟合"
          },
      'min_samples_split':
        {
          "name": "最小分裂样本",
          "explain": "内部节点再划分所需最小样本数"
        },
      'min_samples_leaf':
        {
          "name": "最小叶子节点样本数",
          "explain": "最小叶子节点所需的样本数，如果某叶子节点数目小于样本数，则会与兄弟节点一起被剪枝"
        },
      'min_weight_fraction_leaf':
        {
          "name": "最小叶子节点样本权重和",
          "explain": "位于叶节点所需的(所有输入样本的)权重总和的最小加权分数。当没有提供sample_weight时，样本具有相等的权重。"
        },
      'min_impurity_decrease':
        {
          "name": "最小不纯度减少量",
          "explain": "在寻找最佳分割时需要考虑的特征数量"
        },
      "train_size":
        {
          "name":"训练集比例",
          "explain": "训练集的比例设置，一般为0.7-0.9，剩余的为测试集",
        },
    },

    "sl": {
      "rf": {
        "random_state":
            {
              "name": "随机种子",
              "explain": "决定随机数生成器的种子，以便于结果可重复"
            },
        "n_estimators":
            {
              "name": "决策树数量",
              "explain": "决策树数量越多，模型复杂程度越高，也越容易过拟合"
            },
        "max_depth":
            {
              "name": "最大树深",
              "explain": "最大树深越大，模型复杂程度越高，也越容易过拟合"
            },
        'min_samples_split':
          {
            "name": "最小分裂样本",
            "explain": "内部节点再划分所需最小样本数"
          },
        'min_samples_leaf':
          {
            "name": "最小叶子节点样本数",
            "explain": "最小叶子节点所需的样本数，如果某叶子节点数目小于样本数，则会与兄弟节点一起被剪枝"
          },
        'min_weight_fraction_leaf':
          {
            "name": "最小叶子节点样本权重和",
            "explain": "位于叶节点所需的(所有输入样本的)权重总和的最小加权分数。当没有提供sample_weight时，样本具有相等的权重。"
          },
        'min_impurity_decrease':
          {
            "name": "最小不纯度减少量",
            "explain": "在寻找最佳分割时需要考虑的特征数量"
          },
        "train_size":
          {
            "name":"训练集比例",
            "explain": "训练集的比例设置，一般为0.7-0.9，剩余的为测试集",
          },
      },
      "svc": {
        "kernel":
            {
              "name":  "核函数类型",
              "explain": ""
            },
        "gamma":
            {
              "name":  "核函数系数",
              "explain": ""
            },
        "C":
            {
              "name": "惩罚系数",
              "explain": ""
            },
        "train_size":
          {
            "name":"训练集比例",
            "explain": "训练集的比例设置，一般为0.7-0.9，剩余的为测试集",
          },
      },
      "lg": {
        "max_iter":
            {
              "name": "最大迭代次数",
              "explain": "最大迭代次数越多，模型复杂程度越高，也越容易过拟合"
            },
        "train_size":
          {
            "name":"训练集比例",
            "explain": "训练集的比例设置，一般为0.7-0.9，剩余的为测试集",
          },
      },
    }
  },

  "multiple_classification": {
    "lg": {
      "max_iter":
          {
            "name": "最大迭代次数",
            "explain": "最大迭代次数越多，模型复杂程度越高，也越容易过拟合"
          },
      "train_size":
        {
          "name":"训练集比例",
          "explain": "训练集的比例设置，一般为0.7-0.9，剩余的为测试集",
        },
    },
    "xgboost": {
      "num_boost_round":
          {
            "name": "提升轮数",
            "explain": "提升轮数越多，模型复杂程度越高，也越容易过拟合"
          },
      "eta":
          {
            "name":  "学习率",
            "explain": "学习率越大，模型收敛速度越快，但是也越容易错过最优解"
          },
      "max_depth":
          {
            "name": "最大树深",
            "explain": "最大树深越大，模型复杂程度越高，也越容易过拟合"
          },
      "subsample":
          {
            "name": "子采样比例",
            "explain": "适当降低子采样比例可以防止过拟合"
          },
      "colsample_bytree":
          {
            "name": "树的列采样比例",
            "explain": "适当降低树的列采样比例可以防止过拟合"
          },
      "objective":
          {
            "name": "目标函数",
            "explain": ""
          },
      "train_size":
        {
          "name":"训练集比例",
          "explain": "训练集的比例设置，一般为0.7-0.9，剩余的为测试集",
        },
    },
    "mlp": {
      "num_epochs":
          {
            "name": "迭代次数",
            "explain": "迭代次数越多拟合效果越好，但是也越容易过拟合",
          },
      "hidden_size":
          {
            "name": "隐藏层大小",
            "explain": "隐藏层大小越大模型复杂程度越高",
          },
      "lr":
          {
            "name": "学习率",
            "explain": "学习率越大，模型收敛速度越快，但是也越容易错过最优解",
          },
      "train_size":
        {
          "name":"训练集比例",
          "explain": "训练集的比例设置，一般为0.7-0.9，剩余的为测试集",
        },
    },
    "svc": {
      "kernel":
          {
            "name":  "核函数类型",
            "explain": ""
          },
      "gamma":
          {
            "name":  "核函数系数",
            "explain": ""
          },
      "C":
          {
            "name": "惩罚系数",
            "explain": ""
          },
      "train_size":
        {
          "name":"训练集比例",
          "explain": "训练集的比例设置，一般为0.7-0.9，剩余的为测试集",
        },
    },

    "rf": {
      "random_state":
          {
            "name": "随机种子",
            "explain": "决定随机数生成器的种子，以便于结果可重复"
          },
      "n_estimators":
          {
            "name": "决策树数量",
            "explain": "决策树数量越多，模型复杂程度越高，也越容易过拟合"
          },
      "max_depth":
          {
            "name": "最大树深",
            "explain": "最大树深越大，模型复杂程度越高，也越容易过拟合"
          },
      'min_samples_split':
        {
          "name": "最小分裂样本",
          "explain": "内部节点再划分所需最小样本数"
        },
      'min_samples_leaf':
        {
          "name": "最小叶子节点样本数",
          "explain": "最小叶子节点所需的样本数，如果某叶子节点数目小于样本数，则会与兄弟节点一起被剪枝"
        },
      'min_weight_fraction_leaf':
        {
          "name": "最小叶子节点样本权重和",
          "explain": "位于叶节点所需的(所有输入样本的)权重总和的最小加权分数。当没有提供sample_weight时，样本具有相等的权重。"
        },
      'min_impurity_decrease':
        {
          "name": "最小不纯度减少量",
          "explain": "在寻找最佳分割时需要考虑的特征数量"
        },
      "train_size":
        {
          "name":"训练集比例",
          "explain": "训练集的比例设置，一般为0.7-0.9，剩余的为测试集",
        },
    },

    "sl": {
      "rf": {
        "random_state":
            {
              "name": "随机种子",
              "explain": "决定随机数生成器的种子，以便于结果可重复"
            },
        "n_estimators":
            {
              "name": "决策树数量",
              "explain": "决策树数量越多，模型复杂程度越高，也越容易过拟合"
            },
        "max_depth":
            {
              "name": "最大树深",
              "explain": "最大树深越大，模型复杂程度越高，也越容易过拟合"
            },
        'min_samples_split':
          {
            "name": "最小分裂样本",
            "explain": "内部节点再划分所需最小样本数"
          },
        'min_samples_leaf':
          {
            "name": "最小叶子节点样本数",
            "explain": "最小叶子节点所需的样本数，如果某叶子节点数目小于样本数，则会与兄弟节点一起被剪枝"
          },
        'min_weight_fraction_leaf':
          {
            "name": "最小叶子节点样本权重和",
            "explain": "位于叶节点所需的(所有输入样本的)权重总和的最小加权分数。当没有提供sample_weight时，样本具有相等的权重。"
          },
        'min_impurity_decrease':
          {
            "name": "最小不纯度减少量",
            "explain": "在寻找最佳分割时需要考虑的特征数量"
          },
        "train_size":
          {
            "name":"训练集比例",
            "explain": "训练集的比例设置，一般为0.7-0.9，剩余的为测试集",
          },
      },
      "svc": {
        "kernel":
            {
              "name":  "核函数类型",
              "explain": ""
            },
        "gamma":
            {
              "name":  "核函数系数",
              "explain": ""
            },
        "C":
            {
              "name": "惩罚系数",
              "explain": ""
            },
        "train_size":
          {
            "name":"训练集比例",
            "explain": "训练集的比例设置，一般为0.7-0.9，剩余的为测试集",
          },
      },
      "lg": {
        "max_iter":
            {
              "name": "最大迭代次数",
              "explain": "最大迭代次数越多，模型复杂程度越高，也越容易过拟合"
            },
        "train_size":
          {
            "name":"训练集比例",
            "explain": "训练集的比例设置，一般为0.7-0.9，剩余的为测试集",
          },
      },
    }
  },
}