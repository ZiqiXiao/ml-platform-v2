<template>
  <div>
    <h2>模型列表</h2>
    <div class="py-4 container-fluid">
      <div class="row">
        <div class="col-12">
          <model-table />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ModelTable from "./components/ModelTable.vue";

export default {
  name: "ModelsPage",
  components: {
    ModelTable
  },
  mounted() {

  }
};
</script>
