export default {
  "linear": {
    "lr": {},
    "mlp": {
      "num_epochs": "num_epochs - 迭代次数",
      "hidden_size": "hidden_size - 隐藏层大小",
      "lr": "lr - 学习率",
    },
    "svr": {
      "kernel": "kernel - 核函数类型",
      "gamma": "gamma - 核函数系数",
      "C": "C - 惩罚系数",
    },
    "rf": {
      "random_state": "random_state - 随机种子",
      "n_estimators": "n_estimators - 决策树数量",
      "max_depth": "max_depth - 最大树深",
    },
    "xgboost": {
      "num_boost_round": "num_boost_round - 提升轮数",
      "eta": "eta - 学习率",
      "max_depth": "max_depth - 最大树深",
      "subsample": "subsample - 子采样比例",
      "colsample_bytree": "colsample_bytree - 树的列采样比例",
      "objective": "objective - 目标函数",
    },
    "sl": {
      "rf": {
        "random_state": "random_state - 随机种子",
        "n_estimators": "n_estimators - 决策树数量",
        "max_depth": "max_depth - 最大树深",
      },
      "svr": {
        "kernel": "kernel - 核函数类型",
        "gamma": "gamma - 核函数系数",
        "C": "C - 惩罚系数",
      },
      "lr": {

      },
    }
  },

  "binary_classification": {
    "lg": {
      "max_iter": "max_iter - 最大迭代次数",
    },
    "xgboost": {
      "num_boost_round": "num_boost_round - 提升轮数",
      "eta": "eta - 学习率",
      "max_depth": "max_depth - 最大树深",
      "subsample": "subsample - 子采样比例",
      "colsample_bytree": "colsample_bytree - 树的列采样比例",
      "objective": "objective - 目标函数",
    },
    "svc": {
      "kernel": "kernel - 核函数类型",
      "gamma": "gamma - 核函数系数",
      "C": "C - 惩罚系数",
    },
    "mlp": {
      "num_epochs": "num_epochs - 迭代次数",
      "hidden_size": "hidden_size - 隐藏层大小",
      "lr": "lr - 学习率",
    },
    "rf": {
      "random_state": "random_state - 随机种子",
      "n_estimators": "n_estimators - 决策树数量",
      "max_depth": "max_depth - 最大树深",
    },

    "sl": {
      "rf": {
        "random_state": "random_state - 随机种子",
        "n_estimators": "n_estimators - 决策树数量",
        "max_depth": "max_depth - 最大树深",
      },
      "svc": {
        "kernel": "kernel - 核函数类型",
        "gamma": "gamma - 核函数系数",
        "C": "C - 惩罚系数",
      },
      "lg": {
        "max_iter": "max_iter - 最大迭代次数",
      },
    }
  },

  "multiple_classification": {
    "lg": {
      "max_iter": "max_iter - 最大迭代次数",
    },
    "xgboost": {
      "num_boost_round": "num_boost_round - 提升轮数",
      "eta": "eta - 学习率",
      "max_depth": "max_depth - 最大树深",
      "subsample": "subsample - 子采样比例",
      "colsample_bytree": "colsample_bytree - 树的列采样比例",
      "objective": "objective - 目标函数",
    },
    "mlp": {
      "num_epochs": "num_epochs - 迭代次数",
      "hidden_size": "hidden_size - 隐藏层大小",
      "lr": "lr - 学习率",
    },
    "svc": {
      "kernel": "kernel - 核函数类型",
      "gamma": "gamma - 核函数系数",
      "C": "C - 惩罚系数",
    },

    "rf": {
      "random_state": "random_state - 随机种子",
      "n_estimators": "n_estimators - 决策树数量",
      "max_depth": "max_depth - 最大树深",
    },

    "sl": {
      "rf": {
        "random_state": "random_state - 随机种子",
        "n_estimators": "n_estimators - 决策树数量",
        "max_depth": "max_depth - 最大树深",
      },
      "svc": {
        "kernel": "kernel - 核函数类型",
        "gamma": "gamma - 核函数系数",
        "C": "C - 惩罚系数",
      },
      "lg": {
        "max_iter": "max_iter - 最大迭代次数",
      },
    }
  },
}