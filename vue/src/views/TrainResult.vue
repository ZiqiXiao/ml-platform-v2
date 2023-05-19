<template>
  <div>
    <div v-for="(chart, index) in charts" :key="index" class="card">
      <div class="card-header">
        <h3>{{ modelName[chart.name] }}</h3>
        <h5>训练进度</h5>
        <progress :value="chart.progress" :max="100" show-progress></progress>
      </div>
      <div class="card-body">
        <LossChart v-if="chart.hasLossChart" :chart-name="chart.name" :chart-data="chart.chartData" />
        <MetricTable v-if="chart.chartData" :table-data="chart.chartData.metrics" />
      </div>
    </div>
    <div class="card">
      <div class="card-header">
        <h3>总体结果</h3>
      </div>
      <div class="card-body">
        <MetricTableSum v-if="metricsSum.length" :metrics-sum="metricsSum" />
      </div>
    </div>
    <div class="card">
      <div class="card-header">
        <h3>保存模型</h3>
      </div>
      <div class="card-body">
        <ModelSave :metrics-sum="metricsSum" />
      </div>
    </div>
  </div>
</template>


<script>

import LossChart from "@/views/components/LossChart.vue";
import ModelName from "@/utils/ModelName";
import {io} from "socket.io-client";
import ServiceRoute from "@/utils/service-route";
import axios from "axios";
import MetricTable from "@/views/components/MetricTable.vue";
import MetricTableSum from "@/views/components/MetricTableSum.vue";
import ModelSave from "@/views/components/ModelSave.vue";

export default {
  name: "TrainResult",
  components: {
    LossChart,
    MetricTable,
    MetricTableSum,
    ModelSave,
  },

  emits: ['start-training'],

  data() {
    return {
      filePath: null,
      model: null,
      label:null,
      charts: [],
      modelName: ModelName,
      metricsSum: [],
    }
  },

  created() {
    this.model = JSON.parse(this.$route.params.model);
    this.filePath = this.$route.params.filePath;
    this.label = this.$route.params.label;
    Object.entries(this.model).forEach(([key,]) => {
      this.charts.push({
        name: key,
        progress: 0,
        hasLossChart: false,
        chartData: null,
        },
      );
    });
  },


  mounted() {
    try {
      this.sendTrainRequest();
      this.initSocket();
    } catch (error) {
      console.error("Error:", error);
    }
    this.$nextTick(() => {
      window.scrollTo(0, 0);
    });

  },

  methods: {
    initSocket() {
      this.socket = io(ServiceRoute["python-flask"]);

      this.socket.on("train-message", (response) => {
        const nonReactiveData = JSON.parse(JSON.stringify(response));

        // Find chart by name
        const chart = this.charts.find((chart) => chart.name === nonReactiveData.modelName);
        if (chart) {
          // console.log("Train message:", nonReactiveData);
          chart.progress = nonReactiveData.progress;
        }
      });

      this.socket.on("eval-message", (response) => {
        const nonReactiveData = JSON.parse(JSON.stringify(response));

        const chart = this.charts.find((chart) => chart.name === nonReactiveData.modelName);
        if (chart && nonReactiveData.epoch.length > 0) {
          chart.hasLossChart = true;
          chart.chartData = {
            labels: nonReactiveData.epoch,
            datasets: [
              {
                data: nonReactiveData.trainLoss,
                label: "Train Loss"
              },
              {
                data: nonReactiveData.validLoss,
                label: "Valid Loss"
              }
            ],
            metrics: nonReactiveData.metrics
          };
        } else if (chart) {
          chart.chartData = {
            metrics: nonReactiveData.metrics
          };
        }
        this.updateMetricsSum(nonReactiveData.modelName, nonReactiveData.metrics);
      });
    },

    async sendTrainRequest() {
      if (this.filePath && this.model) {
        const requestData = {
          model: JSON.parse(JSON.stringify(this.model)),
          filePath: this.filePath,
          label: this.label
        };
        try {
          console.log("Sending train request:", requestData);
          await axios.post(
            ServiceRoute["python-flask"] + "/start-train",
            requestData,
            { headers: { "Content-Type": "application/json" } }
          );
        } catch (error) {
          console.error("Error:", error);
        }
      } else if (this.filePath === null) {
        alert("数据路径未正确传递！");
      } else if (this.model === null) {
        alert("模型参数未正确传递！");
      }
    },
    updateMetricsSum(modelName, metrics) {
      this.metricsSum.push({
        [modelName]: metrics
        }
      )
    }
  },
};
</script>

<style scoped>
.card {
  margin-bottom: 20px;
}
</style>