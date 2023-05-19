export default {
  "num_boost_round": {
    name: "num_boost_round - 提升轮数",
    explain: "决策树的数量，数量越多拟合效果越好，但是有可能过拟合; [10, 1000]"
  },
  "eta": {
    name: "eta - 学习率",
    explain: "每次迭代的步长，步长越大学习速度越快，但有可能无法找到全局最优; [0, 1]"
  },
  "max_depth":
    {
      name: "max_depth - 最大树深",
      explain: "树的最大深度，单棵树越复杂，拟合效果越好，但是有可能过拟合; [1, 10]"
    },
  "subsample": "subsample - 子采样比例",
  "colsample_bytree": "colsample_bytree - 树的列采样比例",
  "objective": "objective - 目标函数",
  "num_epochs": "num_epochs - 迭代次数",
  "hidden_size": "hidden_size - 隐藏层大小",
  "lr": "lr - 学习率",
  "kernel": "kernel - 核函数类型",
  "gamma": "gamma - 核函数系数",
  "C": "C - 惩罚系数",
  "n_estimators": "n_estimators - 决策树数量",
  "random_state": "random_state - 随机种子"
};