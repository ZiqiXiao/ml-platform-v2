<template>
<div  class="card-body">
  <input v-model="search" type="text" class="form-control mb-3" placeholder="搜索预测目标" />
  <select v-model="selectedDataset" class="form-select" size="5">
    <option v-for="dataset in filteredDatasets" :key="dataset.id" :value="dataset">{{ dataset }}
    </option>
  </select>
</div>
<div class="card-footer">
  <button class="btn btn-primary" @click="confirm">确认</button>
</div>

</template>

<script>
import axios from "axios";
import serviceRoute from "@/utils/service-route";

export default {
  name: "LabelCheck",

  props: {
    filePath: {
      type: String,
      default: null,
    },
  },

  data() {
    return {
      search: "",
      datasets: [], // 从后端获取的数据集列表
      selectedDataset: null,
      label: null
    };
  },

  computed: {
    filteredDatasets() {
      console.log(this.datasets)
      return this.datasets.filter((dataset) =>
        dataset && dataset.toLowerCase().includes(this.search.toLowerCase())
      );
    },
  },

  watch: {
    filePath: {
      handler(newValue) {
        this.fetchLabel(newValue);
      },
      immediate: true,
    },

    label: {
      handler(newValue) {
        this.$emit('putLabel', newValue);
      },
      immediate: true,
    }
  },

  methods: {
    async fetchLabel(filePath) {
      await axios.post(serviceRoute['python-flask'] + '/get-train-labels', {
        filePath: filePath
      }).then((response) => {
        this.datasets = response.data.labels;
      }).catch((error) => {
        console.log(error);
      }
      )
    },

    confirm() {
      this.label = this.selectedDataset;
      // console.log("New label value:", this.label)
      alert("确认成功！")
    }
  },
}
</script>

<style scoped>

</style>