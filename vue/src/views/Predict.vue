<template>
  <div>
    <h2>预测准备</h2>
    <div class="py-4 container-fluid">
      <div class="row">
        <div class="col-12">
          <predict-dataset-choose @putFilePath="getFilePath"/>
          <model-choose @putModelPath="getModelPath" />
        </div>
        <button type="button" class="btn-primary" @click="submitForm">提交</button>
      </div>
    </div>
  </div>
</template>

<script>
import PredictDatasetChoose from "@/views/components/PredictDatasetChoose.vue";
import ModelChoose from "@/views/components/ModelChoose.vue";
import router from "@/router";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Predict",

  components: {
    PredictDatasetChoose,
    ModelChoose,
  },

  data() {
    return {
      filePath: null,
      modelPath: null,
    };
  },

  watch: {
    filePath(newValue) {
      console.log("New filePath value:", newValue);
    },

    modelPath(newValue) {
      console.log("New modelPath value:", newValue);
    },
  },

  methods: {
    getFilePath(value) {
      this.filePath = value;
    },

    getModelPath(value) {
      this.modelPath = value;
    },

    async submitForm() {
      if (this.filePath && this.modelPath) {
        try {
          await router.push({
            name: 'Predict Result',
            params: {
              filePath: this.filePath,
              modelPath: this.modelPath
            },
            // query: { isFromTrain: true}
          });
        } catch (error) {
          console.error("Error:", error);
        }
      } else if (this.filePath === null) {
        alert("请选择或上传数据！");
      } else if (this.model === null) {
        alert("请选择模型并点击确认！");
      }
    }
  },

};
</script>

<style scoped>

</style>