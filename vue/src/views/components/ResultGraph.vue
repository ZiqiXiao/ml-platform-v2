<template>
  <div class="confusion-matrix-container" v-for="(item, index) in graphInfo" :key="index">
    <h4>{{ trainPhase[item.dataset] }} --- {{ title }}</h4>
    <img :src="item.imgUrl" :alt="title">
  </div>
</template>

<script>

import {defineComponent} from "vue";
import TrainPhase from "@/utils/TrainPhase";

export default defineComponent({
  name: "ResultGraph",

  props: {
    graphInfo: {
      type: Object,
      required: true,
    },
    title: {
      type: String,
      required: true
    },
  },

  data() {
    return {
      trainPhase: TrainPhase,
    }
  },
})
</script>

<style scoped>

</style>