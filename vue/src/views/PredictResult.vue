<template>
  <div class="card">
    <div class="card-header">
      <h3>预测结果</h3>
    </div>
    <div class="card-body text-center">
      <div class="overflow-auto" style="height: 500px;">
        <table class="table align-items-center mb-0">
          <thead>
          <tr>
            <th>样本</th>
            <th>预测结果</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(row, index) in predictResult" :key="index" class="align-middle text-center text-sm">
            <td>{{ index + 1 }}</td>
            <td>{{ row }}</td>
          </tr>
          </tbody>
        </table>
      </div>
      <button class="btn btn-primary" @click="downloadPredictResult">下载预测结果</button>
    </div>
  </div>

</template>

<script>
import axios from "axios";
import serviceRoute from "@/utils/service-route";

export default {
  name: "PredictResult",
  components: {

  },

  data() {
    return {
      filePath: null,
      modelPath: null,
      predictResult: null,
      predictResultPath: null,
    };
  },

  created() {
    this.filePath = this.$route.params.filePath;
    this.modelPath = this.$route.params.modelPath;
  },

  mounted() {
    if (this.filePath && this.modelPath) {
      this.sendPredictRequest()
    }
  },

  methods: {
    async sendPredictRequest() {
      console.log('send predict request')
      await axios.post(serviceRoute['python-flask'] + '/make-prediction', {
        filePath: this.filePath,
        modelPath: this.modelPath
      }).then((response) => {
        console.log(response)
        this.predictResult = response.data.predictResult
        this.predictResultPath = response.data.predictResultPath
        console.log(this.predictResult)
        console.log(this.predictResultPath)
      }).catch((error) => {
        console.log(error)
      })
    },

    async downloadPredictResult() {
      console.log('download predict result')
      await axios.post(serviceRoute['python-flask'] + '/download-predict-result', {
        predictResultPath: this.predictResultPath}, {
        responseType: 'blob'
      }).then(res => {
        if (res.status === 200) {
          const url = window.URL.createObjectURL(new Blob([res.data]));
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', 'predict-result.csv'); // 设置你期望的文件名
          document.body.appendChild(link);
          link.click();
          console.log('download predict result success')
        } else {
          alert('下载失败')
        }
      })
    }
  }
}

</script>

<style scoped>

</style>