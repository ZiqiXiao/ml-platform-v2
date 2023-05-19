<template>
  <table class="table align-items-center mb-0">
    <thead>
    <tr class="text-center">
      <th scope="col">算法</th>
      <th scope="col">数据集</th>
      <th scope="col">Accuracy - 准确率</th>
      <th scope="col">MSE - 均方误差</th>
      <th scope="col">MAE - 平均绝对误差</th>
      <th scope="col">R2</th>
      <th scope="col">RMSE - 均方根误差</th>
    </tr>
    </thead>
    <tbody>
    <template v-for="(metricObj, index) in metricsSum">
      <tr v-for="(row, rowIndex) in metricObj[Object.keys(metricObj)[0]]" :key="index + '-' + rowIndex" class="align-middle text-center text-sm">
        <td v-if="rowIndex === 0" rowspan="2">{{ modelName[Object.keys(metricObj)[0]] }}</td>
        <td>{{ row.dataset }}</td>
        <td>{{ row.acc.toFixed(2) }}%</td>
        <td>{{ row.mse.toFixed(4) }}</td>
        <td>{{ row.mae.toFixed(4) }}</td>
        <td>{{ row.r2.toFixed(4) }}</td>
        <td>{{ row.rmse.toFixed(4) }}</td>
      </tr>
    </template>
    </tbody>
  </table>
</template>

<script>
import ModelName from "@/utils/ModelName";

export default {
  name: 'MetricTableSum',
  props: {
    metricsSum: {
      type: Array,
      default: () => [],
    },
  },

  data() {
    return {
      modelName: ModelName
    }
  },

  computed: {
    algorithms() {
      return Object.keys(this.tableData);
    }
  },

  mounted() {
    // console.log(this.metricsSum);
  }

}
</script>

<style scoped>

</style>