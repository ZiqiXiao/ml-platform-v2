<template>
  <div>
    <div v-for="(algorithm, index) in algorithms" :key="index" class="card">
      <div class="card-header">
        <input :id="algorithm.algorithmName" v-model="algorithm.checked" type="checkbox" />
        <label :for="algorithm.algorithmName" class="model-name">{{ modelName[mission][algorithm.algorithmName] }}</label>
      </div>
      <div v-if="algorithm.checked && algorithm.params.length > 0 && algorithm.algorithmName !== 'sl'" class="card-body">
        <div v-for="(param, paramIndex) in algorithm.params" :key="paramIndex" class="input-container">
          <i class="fa fa-question-circle" :title=paramName[mission][algorithm.algorithmName][param.name][explain] style="vertical-align: middle;"></i>
          <label class="param-name">{{ paramName[mission][algorithm.algorithmName][param.name]['name'] }}</label>
          <input v-model="param.value" :type="param.type" />
        </div>
      </div>
      <div v-else-if="algorithm.checked && algorithm.params.length > 0 && algorithm.algorithmName === 'sl'" class="card-body">
        <div v-for="(learner, learnerIndex) in algorithm.params" :key="learnerIndex" class="input-container" style="display: block">
          <label class="param-name-mul">{{ modelName[mission][learner.learnerName] }}</label>
          <div style="display: block; margin-left: 100px">
            <div v-for="(param, paramIndex) in learner.params" :key="paramIndex" class="input-container">
              <i class="fa fa-question-circle" :title=paramName[mission][algorithm.algorithmName][learner.learnerName][param.name][explain] style="vertical-align: middle;"></i>
              <label class="param-name-sub">{{ paramName[mission][algorithm.algorithmName][learner.learnerName][param.name]['name'] }}</label>
              <input v-model="param.value" :type="param.type" style="width: 100px; height: 20px; margin-left: 5px;"/>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="row">
      <button type="button" class="btn-success" @click="confirm">确认参数</button>
    </div>
  </div>
</template>

<script>
import ModelName from "@/utils/ModelName";
import ParamName from "@/utils/ParamName";

export default {
  name: "TrainModel",

  props: {
    algorithms: {
      type: Array,
      required: true,
    },
    mission:{
      type: String,
      required: true,
    }
  },

  data() {
    return {
      modelName: ModelName,
      paramName: ParamName,
      model: null,
      explain: "explain",
    };
  },

  watch: {
    // algorithms(newValue) {
    //   console.log("New algorithms value:", newValue);
    // },
    //
    // mission(newValue) {
    //   console.log("New mission value:", newValue);
    // },
  },

  methods: {
    confirm() {
      this.model = this.algorithms
        .filter((algorithm) => algorithm.checked)
        .reduce((acc, algorithm) => {
          acc[algorithm.algorithmName] = algorithm.params
            .filter((param) => param.value)
            .reduce((paramsAcc, param) => {
              paramsAcc[param.name] = param.value;
              return paramsAcc;
            }, {});
          return acc;
        }, {});
      if (Object.keys(this.model).length > 0) {
        try {
          this.$emit("putModel", this.model);
          alert("参数已确认！")
        } catch (error) {
          alert("参数确认失败！")
          console.error("Error confirming params:", error);
        }
      } else {
        alert("请先选择模型并确认参数！")
      }


    }
  }
}

</script>

<style scoped>
.model-name {
  font-size: 15px;
}

.card {
  margin-bottom: 20px;
}

.input-container {
  display: flex;
  justify-content: space-between;
  max-width: 500px;
  margin-bottom: 10px;
}

.param-name {
  font-size: 15px;
}

.param-name-mul {
  display: block;
  font-size: 15px;
  margin-left: 10px;
  margin-right: 30px;
  width: 500px;
}

.param-name-sub {
  display: block;
  font-size: 13px;
  margin-left: 100px;
  width: 200px;
}
</style>