<template>
  <div class="card text-center" style="margin-bottom: 20px">
    <div class="card-body text-center">
      <h5>模型选择</h5>
      <input v-model="search" type="text" class="form-control mb-3" placeholder="搜索模型" />
      <select v-model="selectedModel" class="form-select" size="5">
        <option
          v-for="model in sortedFilteredModels"
          :key="model.id"
          :value="model.modelName">
          {{ modelName[model.modelClass] + ':' + model.modelName }}
        </option>
      </select>
      <div class="card-footer text-center">
        <button class="btn btn-primary" @click="confirm">确认</button>
      </div>
    </div>
  </div>

</template>

<script>
import axios from "axios";
import serviceRoute from "@/utils/service-route";
import ModelName from "@/utils/ModelName";

export default {
  name: "ModelChoose",

  components:{

  },

  data() {
    return {
      models: [], // 从后端获取的模型列表
      modelPath: null,
      selectedModel: null,
      search: "",
      modelName: ModelName,
    };
  },

  computed: {
    sortedFilteredModels() {
      return this.filteredModels.slice().sort((a, b) => {
        const aValue = this.modelName[a.modelClass] + ':' + a.modelName;
        const bValue = this.modelName[b.modelClass] + ':' + b.modelName;
        return aValue.localeCompare(bValue);
      });
    },

    filteredModels() {
      return this.models.filter((model) =>
        model.modelName && model.modelName.toLowerCase().includes(this.search.toLowerCase())
      );
    },
  },

  watch: {
    modelPath: {
      handler(newValue) {
        this.$emit('putModelPath', newValue);
      },
      immediate: true,
    },
  },

  mounted() {
    this.fetchModels();
  },

  methods: {
    async fetchModels() {
      try {
        const response = await axios.get(serviceRoute["java-springboot"]['model'] + "/get-all");
        this.models = response.data;
      } catch (error) {
        console.error("Error fetching model data:", error);
      }
    },

    async fetchSelectModelPath(modelName) {
      try {
        const response = await axios.get(serviceRoute['java-springboot']['model'] + "/get-by-name/" + modelName);
        this.modelPath = response.data.modelPath;
      } catch (error) {
        console.error("Error fetching model data:", error);
      }
    },

    confirm() {
        if (this.selectedModel) {
          // this.fetchSelectModelPath(this.selectedModel);
          const selectedModel = this.models.find((model) => model.modelName === this.selectedModel);
          this.modelPath = selectedModel.modelPath;
          // console.log(this.filePath)
          alert("选择成功！");
        } else {
          alert("请选择一个数据集！")
        }
    },
  }
}

</script>

<style scoped>

</style>