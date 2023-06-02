<template>
  <div>
    <h2>训练准备</h2>
    <div class="py-4 container-fluid">
      <div class="row">
        <div class="col-12">
          <train-dataset-choose @putFilePath="getFilePath" @putLabel="getLabel" @put-existed-train-data="getExistedTrainData"/>
          <div class="card">
            <div class="card-header">
              <ul class="nav nav-tabs card-header-tabs">
                <li v-for="tab in tabs" :key="tab" class="nav-item">
                  <a class="nav-link" :class="{ active: currentTab === tab }" @click="changeTab(tab)">{{ missionName[tab] }}</a>
                </li>
              </ul>
            </div>
            <div class="card-body">
              <train-model v-if="currentTab === 'linear'" :algorithms="linearAlgorithms" :mission="linearMission" @putModel="getModel"/>
              <train-model v-if="currentTab === 'binary_classification'" :algorithms="binaryClassificationAlgorithms" :mission="binaryClassificationMission" @putModel="getModel"/>
              <train-model v-if="currentTab === 'multiple_classification'" :algorithms="multipleClassificationAlgorithms" :mission="multipleClassificationMission" @putModel="getModel"/>
              <!-- Add more models for other tabs here -->
            </div>
          </div>

        </div>
        <button type="button" class="btn-primary" @click="submitForm">提交</button>
      </div>
    </div>
  </div>

</template>

<script>
import TrainModel from "@/views/components/TrainModel.vue";
import TrainDatasetChoose from "@/views/components/TrainDatasetChoose.vue";
import router from "@/router";
import axios from "axios";
import serviceRoute from "@/utils/service-route";
import MissionName from "@/utils/MissionName";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Train",
  components: {
    TrainModel,
    TrainDatasetChoose
  },
  data() {
    return {
      filePath: null,
      model: null,
      label: null,
      missionName: MissionName,
      currentTab: "linear",
      tabs: ["linear", "binary_classification", "multiple_classification"],
      linearAlgorithms: null,
      binaryClassificationAlgorithms: null,
      multipleClassificationAlgorithms: null,
      linearMission: "linear",
      binaryClassificationMission: "binary_classification",
      multipleClassificationMission: "multiple_classification",
      usingExistedTrainData: true,
    }
  },

  watch: {
    filePath(newValue) {
      console.log("New filePath value:", newValue);
    },

    model(newValue) {
      console.log("New model value:", newValue);
    },

    label(newValue) {
      console.log("New label value:", newValue);
    }
  },

  async created() {
    try {
      const response = await axios.get(serviceRoute["python-flask"] + "/model-params");
      console.log(response.data)
      const algorithms = Object.entries(response.data).reduce((acc, [mission, algorithm]) => {

        acc[mission] = Object.entries(algorithm).map(([algorithmName, algorithmParam]) => {
          return algorithmName === 'sl'
              ? {
                algorithmName,
                params: Object.entries(algorithmParam).map(([learnerName, learnerParam]) => ({
                  learnerName,
                  params: Object.entries(learnerParam).map(([paramName, paramValue]) => ({
                    name: paramName,
                    value: paramValue,
                    type: typeof paramValue
                  })),
                })),
                checked: false,
              }
              : {
                algorithmName,
                params: Object.entries(algorithmParam).map(([paramName, paramValue]) => ({
                  name: paramName,
                  value: paramValue,
                  type: typeof paramValue
                })),
                checked: false,
              };
        });
        return acc;
      }, {});
      this.linearAlgorithms = algorithms['linear']
      this.binaryClassificationAlgorithms = algorithms['binary_classification']
      this.multipleClassificationAlgorithms = algorithms['multiple_classification']
      console.log(this.multipleClassificationAlgorithms)
    } catch (error) {
      console.error("Error fetching algorithm data:", error);
    }

  },

  methods: {
    changeTab(tab) {
      this.currentTab = tab;
      this.model = null;
    },

    getFilePath(value) {
      this.filePath = value;
    },

    getModel(value) {
      this.model = value;
    },

    getLabel(value) {
      this.label = value;
    },

    getExistedTrainData(value) {
      this.usingExistedTrainData = value;
    },

    async submitForm() {
      if (this.filePath && this.model && this.label) {
        try {
          await router.push({
            name: 'Train Result',
            params: {
              model: JSON.stringify(this.model),
              filePath: this.filePath,
              label: this.label,
              mission: this.currentTab,
              usingExistedTrainData: this.usingExistedTrainData,
            },
            // query: { isFromTrain: true}
          });
        } catch (error) {
          console.error("Error:", error);
        }
      } else if (this.filePath === null) {
        alert("请选择或上传数据！");
      } else if (this.model === null) {
        alert("请选择模型并点击确认参数！");
      } else if (this.label === null) {
        alert("请选择标签！");
      }

    }
  }
}
</script>

<style scoped></style>