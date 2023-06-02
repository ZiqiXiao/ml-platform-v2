<template>
  <div>
    <div v-for="(chart, index) in charts" :key="index" class="card">
      <div class="card-header">
        <h3>{{ modelName[mission][chart.name] }}</h3>
        <h5>训练进度</h5>
        <progress :value="chart.progress" :max="100" show-progress></progress>
      </div>
      <div class="card-body">
        <LossChart v-if="chart.hasLossChart" :chart-name="chart.name" :chart-data="chart.chartData" />
        <result-graph v-if="chart.confusionMatrix" :graph-info="chart.confusionMatrix" :title="confusionMatrixTitle"/>
        <result-graph v-if="chart.rocCurve" :title="rocCurveTitle" :graph-info="chart.rocCurve" />
        <result-graph v-if="chart.featureImportance" :title="featureImportanceTitle" :graph-info="chart.featureImportance" />
        <div class="scrollable-table">
          <MetricTable v-if="chart.chartData" :table-data="chart.chartData.metrics" :mission="mission" />
        </div>
      </div>
    </div>
    <div class="card">
      <div class="card-header">
        <h3>总体结果</h3>
      </div>
      <div class="card-body scrollable-table">
        <metric-table-sum v-if="metricsSum.length" :metrics-sum="metricsSum" :mission="mission" />
      </div>
    </div>
    <div class="card">
      <div class="card-header">
        <h3>保存模型</h3>
      </div>
      <div class="card-body">
        <model-save :metrics-sum="metricsSum" :mission="mission" :existed-train-data-path="existedTrainDataPath"/>
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
import ResultGraph from "@/views/components/ResultGraph.vue";

export default {
  name: "TrainResult",
  components: {
    ResultGraph,
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
      mission: null,
      charts: [],
      modelName: ModelName,
      confusionMatrixTitle: "Confusion Matrix-混淆矩阵",
      featureImportanceTitle: "Feature Importance-特征重要性",
      rocCurveTitle: "ROC-受试者工作特征曲线",
      metricsSum: [],
      existedTrainDataPath: '',
    }
  },

  created() {
    this.model = JSON.parse(this.$route.params.model);
    this.filePath = this.$route.params.filePath;
    this.label = this.$route.params.label;
    this.mission = this.$route.params.mission;
    if (this.$route.params.usingExistedTrainData) {
      this.existedTrainDataPath = this.$route.params.filePath;
    }
    Object.entries(this.model).forEach(([key,]) => {
      this.charts.push({
        name: key,
        progress: 0,
        hasLossChart: false,
        chartData: null,
        confusionMatrix: null,
        rocCurve: null,
        featureImportance: null,
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
            metrics: nonReactiveData.metrics.map(metric => {
              return {
                dataset: metric.dataset,
                acc: metric.acc,
                mse: metric.mse,
                mae: metric.mae,
                rmse: metric.rmse,
                r2: metric.r2,
                auc: metric.auc,
              }
            })
          };
        } else if (chart) {
          chart.chartData = {
            metrics: nonReactiveData.metrics.map(metric => {
              return {
                dataset: metric.dataset,
                acc: metric.acc,
                mse: metric.mse,
                mae: metric.mae,
                rmse: metric.rmse,
                r2: metric.r2,
                auc: metric.auc,
              }
            })
          };
        }
        if (nonReactiveData.metrics[0].cm) {
          chart.confusionMatrix = nonReactiveData.metrics.map(metric => {
            return {
              dataset: metric.dataset,
              imgUrl: metric.cm,
            }
          })
        }
        if (nonReactiveData.metrics[0].roc) {
          chart.rocCurve = nonReactiveData.metrics.map(metric => {
            return {
              dataset: metric.dataset,
              imgUrl: metric.roc,
            }
          })
        }
        if (nonReactiveData.featureImportance) {
          chart.featureImportance = [{
            dataset: "",
            imgUrl: nonReactiveData.featureImportance}]
        }
        console.log(this.charts);
        this.updateMetricsSum(nonReactiveData.modelName, nonReactiveData.metrics);
      });
    },

    async sendTrainRequest() {
      if (this.filePath && this.model && this.mission) {
        const requestData = {
          model: JSON.parse(JSON.stringify(this.model)),
          filePath: this.filePath,
          label: this.label,
          mission: this.mission
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

.scrollable-table {
  overflow: auto;
}
</style>