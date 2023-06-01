<template>
<table class="table align-items-center mb-0">
	<thead>
	<tr>
		<th>数据集</th>
		<th>Accuracy - 准确率</th>
		<th>MSE - 均方误差</th>
		<th>MAE - 平均绝对误差</th>
		<th>R2</th>
		<th>RMSE - 均方根误差</th>
    <th v-if="mission === 'binary_classification'">AUC - 曲线下面积</th>
    <th
        v-for="(auc, auc_index) in tableData[0].auc"
        v-else-if="mission === 'multiple_classification'"
        :key="auc_index"
    >类别-{{ auc_index }}-AUC - 曲线下面积</th>
	</tr>
	</thead>
	<tbody>
    <tr v-for="(row, index) in tableData" :key="index" class="align-middle text-center text-sm">
			<td>{{ trainPhase[row.dataset] }}</td>
			<td>{{ row.acc.toFixed(2) }}%</td>
			<td>{{ row.mse.toFixed(4) }}</td>
			<td>{{ row.mae.toFixed(4) }}</td>
			<td>{{ row.r2.toFixed(4) }}</td>
			<td>{{ row.rmse.toFixed(4) }}</td>
      <td v-if="mission === 'binary_classification'">{{ row.auc.toFixed(4) }}</td>
      <td
          v-for="(auc, auc_index) in row.auc"
          v-else-if="mission === 'multiple_classification'"
          :key="auc_index"
      >{{ auc.toFixed(4) }}</td>
    </tr>
	</tbody>
</table>
</template>

<script>
import TrainPhase from "@/utils/TrainPhase";

export default {
	name: 'MetricTable',
	props: {
		tableData: {
			type: Array,
			default: () => [],
		},
    mission: {
      type: String,
      default: 'linear',
    }
	},
	data() {
		return {
      trainPhase: TrainPhase,
		}
	},
	mounted() {
    console.log(this.trainData)
	},
};
</script>

<style scoped>
/*.metric-table {*/
/*		height: 100px;*/
/*		width: 100px;*/
/*}*/
</style>