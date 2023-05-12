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
	</tr>
	</thead>
	<tbody>
    <tr v-for="(row, index) in tableData" :key="index" class="align-middle text-center text-sm">
			<td>{{ row.dataset }}</td>
			<td>{{ row.acc.toFixed(2) }}%</td>
			<td>{{ row.mse.toFixed(4) }}</td>
			<td>{{ row.mae.toFixed(4) }}</td>
			<td>{{ row.r2.toFixed(4) }}</td>
			<td>{{ row.rmse.toFixed(4) }}</td>
    </tr>
	</tbody>
</table>
</template>

<script>
export default {
	name: 'MetricTable',
	props: {
		tableData: {
			type: Array,
			default: () => [],
		},
	},
	data() {
		return {

		}
	},
	mounted() {
		console.log(this.tableData);
	},
};
</script>

<style scoped>
/*.metric-table {*/
/*		height: 100px;*/
/*		width: 100px;*/
/*}*/
</style>