<template>
  <div>
    <div v-for="(algorithm, index) in algorithms" :key="index" class="card">
      <div class="card-header">
        <input :id="algorithm.name" v-model="algorithm.checked" type="checkbox" />
        <label :for="algorithm.name" class="model-name">{{ modelName[algorithm.name] }}</label>
      </div>
      <div v-if="algorithm.checked && algorithm.params.length > 0" class="card-body">
        <div v-for="(param, paramIndex) in algorithm.params" :key="paramIndex" class="input-container">
          <label class="param-name">{{ paramName[param.name] }}</label>
          <input v-model="param.value" :type="param.type" />
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
import axios from "axios";
import serviceRoute from "@/utils/service-route";

export default {
  name: "TrainModel",

  data() {
    return {
      algorithms: [],
      modelName: ModelName,
      paramName: ParamName,
      model: null,
    };
  },

  watch: {
  },

  async created() {
    try {
      const response = await axios.get(serviceRoute["python-flask"] + "/model-params");
      const responseData = response.data;
      this.algorithms = Object.entries(responseData).map(([name, params]) => ({
        name,
        params: Object.entries(params).map(([paramName, paramValue]) => ({
          name: paramName,
          value: paramValue,
          type: typeof paramValue
        })),
        checked: false,
      }));
      console.log(this.algorithms)
    } catch (error) {
      console.error("Error fetching algorithm data:", error);
    }
  },

  methods: {
    confirm() {
      this.model = this.algorithms
        .filter((algorithm) => algorithm.checked)
        .reduce((acc, algorithm) => {
          acc[algorithm.name] = algorithm.params
            .filter((param) => param.value)
            .reduce((paramsAcc, param) => {
              paramsAcc[param.name] = param.value;
              return paramsAcc;
            }, {});
          return acc;
        }, {});
      console.log(this.model)
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
</style>