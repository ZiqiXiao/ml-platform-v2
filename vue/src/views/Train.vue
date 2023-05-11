<template>
  <div class="py-4 container-fluid">
    <div class="row">
      <div class="col-12">
        <upload-file @putFilePath="getFilePath" />
        <train-model @putModel="getModel" />
      </div>
      <button type="button" class="btn-primary" @click="submitForm">提交</button>
    </div>
  </div>
</template>

<script>
import TrainModel from "@/views/components/TrainModel.vue";
import UploadFile from "@/views/components/UploadFile.vue";
import router from "@/router";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Train",
  components: {
    TrainModel,
    UploadFile
  },
  data() {
    return {
      filePath: null,
      model: null
    }
  },

  watch: {
    filePath(newValue) {
      console.log("New filePath value:", newValue);
    },

    model(newValue) {
      console.log("New model value:", newValue);
    }
  },
  methods: {
    getFilePath(value) {
      this.filePath = value;
    },

    getModel(value) {
      this.model = value;
    },

    async submitForm() {
      if (this.filePath && this.model) {
        try {
          await router.push({
            name: 'Train Result',
            params: {
              model: JSON.stringify(this.model),
              filePath: this.filePath
            },
            // query: { isFromTrain: true}
          });
        } catch (error) {
          console.error("Error:", error);
        }
      } else if (this.filePath === null) {
        alert("请选择或上传数据！");
      } else if (this.model === null) {
        alert("请选择模型并点击确认参数！");
      }

    }
  }
}
</script>

<style scoped></style>