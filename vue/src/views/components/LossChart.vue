<template>
  <div class="chart-container">
    <h4>Loss</h4>
    <Line :ref="'chart-' + chartName" :data="chartData" :options="chartOptions" />
  </div>
</template>

<script>
import { Line } from 'vue-chartjs';
import zoomPlugin from 'chartjs-plugin-zoom';
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale } from 'chart.js';

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale, zoomPlugin)
export default {
  components: {
    Line
  },
  props: {
   chartName: {
     type: String,
   },
    chartData: {
      type: Object,
    },
  },
  data() {
    return {
      chartOptions: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          x: {
            type: "linear",
            ticks: {
              autoSkip: true,
              maxTicksLimit: 10,
            },
            display: true,
            scaleLabel: {
              display: true,
              labelString: "Epoch",
            },
          },
          y: {
            type: "linear",
            display: true,
            ticks: {
              autoSkip: true,
              maxTicksLimit: 5,
            },
            scaleLabel: {
              display: true,
              labelString: "Loss",
            },
            beginAtZero: true,
          }
        },
        plugins: {
          zoom: {
            pan: {
              enabled: true,
              mode: 'xy',
              speed: 10,
            },
            zoom: {
              enabled: true,
              drag: true,
              mode: 'xy',
              speed: 0.1,
              wheel: {
                enabled: true,
              },
            },
          },
        },
      },
    };
  },

  created() {

  },


  mounted() {

  },

  methods: {

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