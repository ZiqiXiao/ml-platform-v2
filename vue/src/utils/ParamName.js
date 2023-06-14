export default {
  "linear": {
    "lr": {},
    "mlp": {
      "num_epochs":
          {
            "name": "num_epochs - 迭代次数",
            "explain": "迭代次数越多拟合效果越好，但是也越容易过拟合",
          },
      "hidden_size":
          {
            "name": "hidden_size - 隐藏层大小",
            "explain": "隐藏层大小越大模型复杂程度越高",
          },
      "lr":
          {
            "name": "lr - 学习率",
            "explain": "学习率越大，模型收敛速度越快，但是也越容易错过最优解",
          }
    },
    "svr": {
      "kernel":
          {
            "name": "kernel - 核函数类型",
            "explain": ""
          },
      "gamma":
          {
            "name": "gamma - 核函数系数",
            "explain": ""
          },
      "C":
          {
            "name": "C - 惩罚系数",
            "explain": ""
          },
    },
    "rf": {
      "random_state":
          {
            "name": "random_state - 随机种子",
            "explain": "决定随机数生成器的种子，以便于结果可重复"
          },
      "n_estimators":
          {
            "name": "n_estimators - 决策树数量",
            "explain": "决策树数量越多，模型复杂程度越高，也越容易过拟合"
          },
      "max_depth":
          {
            "name": "max_depth - 最大树深",
            "explain": "最大树深越大，模型复杂程度越高，也越容易过拟合"
          },
    },
    "xgboost": {
      "num_boost_round":
          {
            "name": "num_boost_round - 提升轮数",
            "explain": "提升轮数越多，模型复杂程度越高，也越容易过拟合"
          },
      "eta":
          {
            "name":  "eta - 学习率",
            "explain": "学习率越大，模型收敛速度越快，但是也越容易错过最优解"
          },
      "max_depth":
          {
            "name": "max_depth - 最大树深",
            "explain": "最大树深越大，模型复杂程度越高，也越容易过拟合"
          },
      "subsample":
          {
            "name": "subsample - 子采样比例",
            "explain": "适当降低子采样比例可以防止过拟合"
          },
      "colsample_bytree":
          {
            "name": "colsample_bytree - 树的列采样比例",
            "explain": "适当降低树的列采样比例可以防止过拟合"
          },
      "objective":
          {
            "name": "objective - 目标函数",
            "explain": ""
          },
    },
    "sl": {
      "rf": {
        "random_state":
            {
              "name": "random_state - 随机种子",
              "explain": "决定随机数生成器的种子，以便于结果可重复"
            },
        "n_estimators":
            {
              "name": "n_estimators - 决策树数量",
              "explain": "决策树数量越多，模型复杂程度越高，也越容易过拟合"
            },
        "max_depth":
            {
              "name": "max_depth - 最大树深",
              "explain": "最大树深越大，模型复杂程度越高，也越容易过拟合"
            },
      },
      "svr": {
        "kernel":
            {
              "name":  "kernel - 核函数类型",
              "explain": ""
            },
        "gamma":
            {
              "name":  "gamma - 核函数系数",
              "explain": ""
            },
        "C":
            {
              "name": "C - 惩罚系数",
              "explain": ""
            },
      },
      "lr": {

      },
    }
  },

  "binary_classification": {
    "lg": {
      "max_iter":
          {
            "name": "max_iter - 最大迭代次数",
            "explain": "最大迭代次数越多，模型复杂程度越高，也越容易过拟合"
          },
    },
    "xgboost": {
      "num_boost_round":
          {
            "name": "num_boost_round - 提升轮数",
            "explain": "提升轮数越多，模型复杂程度越高，也越容易过拟合"
          },
      "eta":
          {
            "name":  "eta - 学习率",
            "explain": "学习率越大，模型收敛速度越快，但是也越容易错过最优解"
          },
      "max_depth":
          {
            "name": "max_depth - 最大树深",
            "explain": "最大树深越大，模型复杂程度越高，也越容易过拟合"
          },
      "subsample":
          {
            "name": "subsample - 子采样比例",
            "explain": "适当降低子采样比例可以防止过拟合"
          },
      "colsample_bytree":
          {
            "name": "colsample_bytree - 树的列采样比例",
            "explain": "适当降低树的列采样比例可以防止过拟合"
          },
      "objective":
          {
            "name": "objective - 目标函数",
            "explain": ""
          },
    },
    "svc": {
      "kernel":
          {
            "name":  "kernel - 核函数类型",
            "explain": ""
          },
      "gamma":
          {
            "name":  "gamma - 核函数系数",
            "explain": ""
          },
      "C":
          {
            "name": "C - 惩罚系数",
            "explain": ""
          },
    },
    "mlp": {
      "num_epochs":
          {
            "name": "num_epochs - 迭代次数",
            "explain": "迭代次数越多拟合效果越好，但是也越容易过拟合",
          },
      "hidden_size":
          {
            "name": "hidden_size - 隐藏层大小",
            "explain": "隐藏层大小越大模型复杂程度越高",
          },
      "lr":
          {
            "name": "lr - 学习率",
            "explain": "学习率越大，模型收敛速度越快，但是也越容易错过最优解",
          }
    },
    "rf": {
      "random_state":
          {
            "name": "random_state - 随机种子",
            "explain": "决定随机数生成器的种子，以便于结果可重复"
          },
      "n_estimators":
          {
            "name": "n_estimators - 决策树数量",
            "explain": "决策树数量越多，模型复杂程度越高，也越容易过拟合"
          },
      "max_depth":
          {
            "name": "max_depth - 最大树深",
            "explain": "最大树深越大，模型复杂程度越高，也越容易过拟合"
          },
    },

    "sl": {
      "rf": {
        "random_state":
            {
              "name": "random_state - 随机种子",
              "explain": "决定随机数生成器的种子，以便于结果可重复"
            },
        "n_estimators":
            {
              "name": "n_estimators - 决策树数量",
              "explain": "决策树数量越多，模型复杂程度越高，也越容易过拟合"
            },
        "max_depth":
            {
              "name": "max_depth - 最大树深",
              "explain": "最大树深越大，模型复杂程度越高，也越容易过拟合"
            },
      },
      "svc": {
        "kernel":
            {
              "name":  "kernel - 核函数类型",
              "explain": ""
            },
        "gamma":
            {
              "name":  "gamma - 核函数系数",
              "explain": ""
            },
        "C":
            {
              "name": "C - 惩罚系数",
              "explain": ""
            },
      },
      "lg": {
        "max_iter":
            {
              "name": "max_iter - 最大迭代次数",
              "explain": "最大迭代次数越多，模型复杂程度越高，也越容易过拟合"
            },
      },
    }
  },

  "multiple_classification": {
    "lg": {
      "max_iter":
          {
            "name": "max_iter - 最大迭代次数",
            "explain": "最大迭代次数越多，模型复杂程度越高，也越容易过拟合"
          },
    },
    "xgboost": {
      "num_boost_round":
          {
            "name": "num_boost_round - 提升轮数",
            "explain": "提升轮数越多，模型复杂程度越高，也越容易过拟合"
          },
      "eta":
          {
            "name":  "eta - 学习率",
            "explain": "学习率越大，模型收敛速度越快，但是也越容易错过最优解"
          },
      "max_depth":
          {
            "name": "max_depth - 最大树深",
            "explain": "最大树深越大，模型复杂程度越高，也越容易过拟合"
          },
      "subsample":
          {
            "name": "subsample - 子采样比例",
            "explain": "适当降低子采样比例可以防止过拟合"
          },
      "colsample_bytree":
          {
            "name": "colsample_bytree - 树的列采样比例",
            "explain": "适当降低树的列采样比例可以防止过拟合"
          },
      "objective":
          {
            "name": "objective - 目标函数",
            "explain": ""
          },
    },
    "mlp": {
      "num_epochs":
          {
            "name": "num_epochs - 迭代次数",
            "explain": "迭代次数越多拟合效果越好，但是也越容易过拟合",
          },
      "hidden_size":
          {
            "name": "hidden_size - 隐藏层大小",
            "explain": "隐藏层大小越大模型复杂程度越高",
          },
      "lr":
          {
            "name": "lr - 学习率",
            "explain": "学习率越大，模型收敛速度越快，但是也越容易错过最优解",
          }
    },
    "svc": {
      "kernel":
          {
            "name":  "kernel - 核函数类型",
            "explain": ""
          },
      "gamma":
          {
            "name":  "gamma - 核函数系数",
            "explain": ""
          },
      "C":
          {
            "name": "C - 惩罚系数",
            "explain": ""
          },
    },

    "rf": {
      "random_state":
          {
            "name": "random_state - 随机种子",
            "explain": "决定随机数生成器的种子，以便于结果可重复"
          },
      "n_estimators":
          {
            "name": "n_estimators - 决策树数量",
            "explain": "决策树数量越多，模型复杂程度越高，也越容易过拟合"
          },
      "max_depth":
          {
            "name": "max_depth - 最大树深",
            "explain": "最大树深越大，模型复杂程度越高，也越容易过拟合"
          },
    },

    "sl": {
      "rf": {
        "random_state":
            {
              "name": "random_state - 随机种子",
              "explain": "决定随机数生成器的种子，以便于结果可重复"
            },
        "n_estimators":
            {
              "name": "n_estimators - 决策树数量",
              "explain": "决策树数量越多，模型复杂程度越高，也越容易过拟合"
            },
        "max_depth":
            {
              "name": "max_depth - 最大树深",
              "explain": "最大树深越大，模型复杂程度越高，也越容易过拟合"
            },
      },
      "svc": {
        "kernel":
            {
              "name":  "kernel - 核函数类型",
              "explain": ""
            },
        "gamma":
            {
              "name":  "gamma - 核函数系数",
              "explain": ""
            },
        "C":
            {
              "name": "C - 惩罚系数",
              "explain": ""
            },
      },
      "lg": {
        "max_iter":
            {
              "name": "max_iter - 最大迭代次数",
              "explain": "最大迭代次数越多，模型复杂程度越高，也越容易过拟合"
            },
      },
    }
  },
}