<template>
  <div class="py-4 container-fluid">
    <div class="row">
      <div class="col-12">
        <model-table />
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
};
</script>
