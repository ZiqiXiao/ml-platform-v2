<template>
  <div class="card text-center" style="margin-bottom: 20px">
    <div class="card-body text-center">
      <h5>模型选择</h5>
      <input v-model="search" type="text" class="form-control mb-3" placeholder="搜索模型" />

      <select v-model="selectedMission" class="form-select mb-3">
        <option value="">全部任务类型</option>
        <!-- 这里假设 missions 是所有任务类型的数组 -->
        <option v-for="mission in missions" :key="mission" :value="mission">{{ missionName[mission] }}</option>
      </select>

<!--      <select v-model="selectedModelClass" class="form-select mb-3">-->
<!--        <option value="">全部模型分类</option>-->
<!--        &lt;!&ndash; 这里假设 modelClasses 是所有模型分类的数组 &ndash;&gt;-->
<!--        <option v-for="modelClass in modelClasses" :key="modelClass" :value="modelClass">{{ modelName[modelClass] }}</option>-->
<!--      </select>-->


      <!-- 这里开始新的表格部分 -->
      <div style="max-height: 500px; overflow-y: auto;">
        <table class="table table-striped">
          <thead>
          <tr>
            <th></th>
            <th>模型名称</th>
            <th>任务类型</th>
            <th>模型分类</th>
            <th>模型描述</th>
          </tr>
          </thead>
          <tbody >
          <tr v-for="model in sortedFilteredModels" :key="model.id">
            <td><input type="radio" v-model="selectedModel" :value="model.modelName"></td>
            <td>{{ model.modelName }}</td>
            <td>{{ missionName[model.mission]}}</td>
            <td>{{ modelName[model.mission][model.modelClass]}}</td>
            <td>{{ model.modelDescription }}</td>
          </tr>
          </tbody>
        </table>
      </div>
      <!-- 结束新的表格部分 -->

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
import missionName from "../../utils/MissionName";

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
      selectedMission: '', // 选中的任务类型
      selectedModelClass: '', // 选中的模型分类
      modelName: ModelName,
      missions: ['linear', 'binary_classification', 'multiple_classification']
    };
  },

  computed: {
    missionName() {
      return missionName
    },
    sortedFilteredModels() {
      return this.filteredModels.slice().sort((a, b) => {
        const aValue = this.modelName[a.mission][a.modelClass] + ':' + a.modelName;
        const bValue = this.modelName[b.mission][b.modelClass] + ':' + b.modelName;
        return aValue.localeCompare(bValue);
      });
    },

    filteredModels() {
      return this.models.filter((model) => {
        const isModelNameMatch = model.modelName && model.modelName.toLowerCase().includes(this.search.toLowerCase());
        const isMissionMatch = this.selectedMission ? model.mission === this.selectedMission : true;
        // const isModelClassMatch = this.selectedModelClass ? model.modelClass === this.selectedModelClass : true;

        return isModelNameMatch && isMissionMatch;
      });
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
        this.models = response.data.map(model => {
          let pathParts = model.modelPath.split("/");
          let mission = pathParts[pathParts.length - 3];
          return {
            ...model,
            mission: mission
          };
        });
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