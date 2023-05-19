<template>
  <div class="card text-center" style="margin-bottom: 20px">
    <h5>选择数据集</h5>
    <file-choose :scenario="scenario" @putFilePath="getFilePath" />
  </div>
</template>

<script>
import FileChoose from "@/views/components/FileChoose.vue";

export default {
  name: "PredictDatasetChoose",
  components: {
    FileChoose,
  },

  data() {
    return {
      filePath: null,
      label: null,
      scenario: 'predict-data',
    };
  },

  watch: {
    filePath: {
      handler(newValue) {
        this.$emit('putFilePath', newValue);
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
    getFilePath(value) {
      this.filePath = value;
      // console.log("New filePath value:", value)
    },

    getLabel(value) {
      this.label = value;
      // console.log("New label value:", value)
    },
  },
}

</script>

<style scoped>

</style>