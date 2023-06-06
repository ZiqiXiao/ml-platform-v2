<template>
  <div>
    <h2>数据集管理</h2>
    <div class="py-4 container-fluid">
      <div class="row">
        <div class="col-12">
          <h4>预测数据集</h4>
          <dataset-table :scenario="predictDataScenario"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import DatasetTable from "@/views/components/DatasetTable.vue";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "PredictDataset",
  components: {
    DatasetTable,
  },
  data() {
    return {
      trainDataScenario: "train-data",
      predictDataScenario: "predict-data",
      templateDataScenario: "train-data",
    };
  },
}
</script>

<style scoped>

</style>