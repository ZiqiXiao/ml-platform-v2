<template>
  <div>
    <div v-for="(chart, index) in charts" :key="index" class="card">
      <div class="card-header">
        <h3>{{ modelName[chart.name] }}</h3>
        <h5>训练进度</h5>
        <progress :value="chart.progress" :max="100" show-progress></progress>
      </div>
      <div class="card-body">
        <div class="chart-container">
          <h4>Loss</h4>
          <!-- <canvas :ref="'chart-' + chart.name" class="chart-canvas"></canvas> -->
          <Line :ref="'chart-' + chart.name" :data="chart.chartData" :options="chartOptions" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { Line } from 'vue-chartjs';
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale } from 'chart.js';
import ServiceRoute from "@/utils/service-route";
import ModelName from "@/utils/ModelName";
import { io } from "socket.io-client";
import axios from "axios";

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale)

export default {
  components: {
    Line
  },
  emits: ['start-training'],
  data() {
    return {
      socket: null,
      modelName: ModelName,
      charts: [],
      model: null,
      filePath: null,
      chartOptions: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          x: {
            type: "linear",
            display: true,
            scaleLabel: {
              display: true,
              labelString: "Epoch",
            },
          },
          y: {
            type: "linear",
            display: true,
            scaleLabel: {
              display: true,
              labelString: "Loss",
            },
            beginAtZero: true,
          }
        }
      },
    };
  },

  created() {
    this.model = JSON.parse(this.$route.params.model);
    this.filePath = this.$route.params.filePath;

    Object.entries(this.model).forEach(([key,]) => {
      this.charts.push({
        name: key,
        progress: 0,
        chartData: {
          labels: [],
          datasets: [
            {
              data: [],
              label: "Train Loss"
            },
            {
              data: [],
              label: "Valid Loss"
            }
          ],
        },
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
    };


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
        };
      });

      this.socket.on("eval-message", (response) => {
        const nonReactiveData = JSON.parse(JSON.stringify(response));

        const chart = this.charts.find((chart) => chart.name === nonReactiveData.modelName);
        if (chart) {
          // console.log("Train message:", nonReactiveData);
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
          };
        };
      })

    },

    updateArray(oldArray, newData) {
      oldArray.push(newData);
      return oldArray;
    },

    async sendTrainRequest() {
      if (this.filePath && this.model) {
        const requestData = {
          model: JSON.parse(JSON.stringify(this.model)),
          filePath: this.filePath
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
  }
};
</script>

<style scoped>
.chart-container {
  width: 500px;
  height: 400px;
  margin-bottom: 50px;
}
</style>