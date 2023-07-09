<template>
  <div v-if="existedTrainDataPath.length===0">
    <div class="form-check">
      <input id="saveDatasetCheckbox" v-model="saveDataset" class="form-check-input" type="checkbox">
      <label class="form-check-label" for="saveDatasetCheckbox">保存数据集到本地</label>
    </div>
    <div v-if="saveDataset" class="input-group mb-3">
      <input v-model="datasetSaveName" type="text" class="form-control" placeholder="数据集保存名称">
    </div>
    <div class="d-flex align-items-center">
      <button class="btn btn-behance" type="button" style="margin-right: 20px" @click="checkDatasetName">检查</button>
      <div v-show="saveDataset">
        <i v-if="datasetSaveName === ''" class="fas fa-times">：请输入名字并检查</i>
        <i v-else-if="validDatasetSaveName && datasetSaveName !== ''" class="fas fa-check">：可以使用该名字</i>
      </div>
    </div>
    <div class="form-check">
      <input id="saveTemplateCheckbox" v-model="saveTemplate" class="form-check-input" type="checkbox">
      <label class="form-check-label" for="saveTemplateCheckbox">保存数据集模板</label>
    </div>
    <div v-if="saveTemplate" class="input-group mb-3">
      <input v-model="templateSaveName" type="text" class="form-control" placeholder="数据集模板保存名称">
    </div>
    <div class="d-flex align-items-center">
      <button class="btn btn-behance" type="button" style="margin-right: 20px" @click="checkTemplateName">检查</button>
      <div v-show="saveTemplate">
        <i v-if="templateSaveName === ''" class="fas fa-times">：请输入名字并检查</i>
        <i v-else-if="validTemplateSaveName && templateSaveName !== ''" class="fas fa-check">：可以使用该名字</i>
      </div>
    </div>
  </div>
  <div v-else class="d-flex align-items-center">
    <h6>使用已有数据集保存的模型将直接关联</h6>
  </div>
  <div>
    <table class="table align-items-center mb-0">
      <thead>
      <tr class="text-center">
        <th scope="col">算法</th>
        <th scope="col">是否保存模型</th>
        <th scope="col">模型名称</th>
        <th scope="col">检查名称</th>
        <th scope="col">检查结果</th>
        <th scope="col">模型描述</th>
        <th scope="col">操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(metricObj, index) in metricsSum" :key="index" class="align-middle text-center text-sm">
        <td>{{ modelName[mission][Object.keys(metricObj)[0]] }}</td>
        <td><input v-model="checkboxStatus[index]" type="checkbox"/></td>
        <td><input v-show="checkboxStatus[index]" v-model="modelSaveName[index]" type="text" :disabled="!checkboxStatus[index]"/></td>
        <td><button class="btn btn-sm btn-behance" :disabled="!checkboxStatus[index]" @click="checkName(index)">检查</button></td>
        <td>
          <i v-if="checkboxStatus[index] && nameCheckStatus[index] === 'valid'" class="fas fa-check">：可以使用该名字</i>
          <i v-else-if="checkboxStatus[index] && nameCheckStatus[index] === 'invalid'" class="fas fa-times">：名字已存在</i>
        </td>
        <td><input v-show="checkboxStatus[index]" v-model="modelDescription[index]" type="text" placeholder="请输入模型的重要相关信息"/></td>
        <td>
          <button class="btn btn-sm btn-primary" :disabled="!checkboxStatus[index]" @click="fillModelDescription(index)">填充</button> <!-- 新增的按钮 -->
        </td>
      </tr>
      </tbody>
    </table>
    <div class="text-center" style="margin-top: 40px">
      <button class="btn btn-lg btn-success" @click="saveAll">保存</button>
    </div>
  </div>
</template>

<script>
import ModelName from "../../utils/ModelName";
import ServiceRoute from "@/utils/service-route";
import axios from "axios";

export default {
  name: 'ModelSave',

  props: {
    metricsSum: {
      type: Array,
      default: () => [],
    },
    mission: {
      type: String,
      default: () => {},
    },
    existedTrainDataPath: {
      type: String,
      default: '',
    },
  },

  data() {
    return {
      modelName: ModelName,
      modelSaveName: [],
      modelDescription: [],
      checkboxStatus: [],
      nameCheckStatus: [],
      saveDataset: false,
      saveTemplate: false,
      datasetSaveName: '',
      templateSaveName: '',
      validDatasetSaveName: false,
      validTemplateSaveName: false,
    }
  },

  computed: {

  },

  watch: {
    datasetSaveName() {
      this.validDatasetSaveName = false;
    },

    templateSaveName() {
      this.validTemplateSaveName = false;
    },

    saveDataset(newValue) {
      if (!newValue) {
        this.datasetSaveName = '';
      }
    },
    saveTemplate(newValue) {
      if (!newValue) {
        this.templateSaveName = '';
      }
    },
  },

  created() {

  },

  mounted() {
  },

  methods: {
    fillModelDescription(index) {
      const metricObj = this.metricsSum[index];
      const metricInfo = JSON.stringify(metricObj);
      this.modelDescription[index] = metricInfo;
    },
    async checkName(index) {
      const modelSaveName = this.modelSaveName[index];
      console.log(modelSaveName)

      try {
        await axios.get(ServiceRoute['java-springboot']['model'] + '/get-by-name/' + modelSaveName, {
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(res => {
          console.log(res);
          if (res.status === 200) {
            // alert('名字已存在')
            this.nameCheckStatus[index] = 'invalid';
          }
        })
      } catch (e) {
        console.log(e);
        // alert('可以使用该名字')
        this.nameCheckStatus[index] = 'valid';
      }
    },

    async saveAll() {
      const modelsToSave = this.metricsSum.filter((_, index) => this.checkboxStatus[index] && this.nameCheckStatus[index]);
      console.log(modelsToSave);

      const algoNameAndSaveName = modelsToSave.reduce((acc, modelObj) => {
        const algoName = Object.keys(modelObj)[0];
        const originalIndex = this.metricsSum.findIndex(obj => Object.keys(obj)[0] === algoName);
        const saveName = this.modelSaveName[originalIndex];
        const modelDescription = this.modelDescription[originalIndex];

        if (!acc.modelClass) {
          acc.modelClass = [];
        }
        if (!acc.modelName) {
          acc.modelName = [];
        }
        if (!acc.modelDescription) {
          acc.modelDescription = [];
        }

        acc.modelClass.push(algoName);
        acc.modelName.push(saveName);
        acc.modelDescription.push(modelDescription);

        return acc;
      }, {});
      console.log(algoNameAndSaveName);

      await this.saveModelAndName(
        algoNameAndSaveName.modelClass,
        algoNameAndSaveName.modelName,
        algoNameAndSaveName.modelDescription,
        this.datasetSaveName,
        this.templateSaveName);
    },

    async saveModelAndName(algoName, modelSaveName, modelDescription, uploadFileName, templateName) {
      try {
        const res = {
          modelClass: algoName,
          modelName: modelSaveName,
          modelDescription: modelDescription,
          trainData: {
            fileName: uploadFileName,
            templateName: templateName,},
          existedTrainDataPath: this.existedTrainDataPath,
        };
        await axios.post(ServiceRoute['java-springboot']['model'] + '/create', res, {
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(res => {
          if (res.status === 201) {
            alert('模型保存成功');
          }
        })
      } catch (error) {
        console.error(error);
        alert('模型保存失败: ' + error.message);
      }
    },

    async checkDatasetName() {
      const nameAvailable = await this.checkUploadFileNameAvailability(this.datasetSaveName);
      if (nameAvailable) {
        this.validDatasetSaveName = true
      } else {
        this.validDatasetSaveName = false
        alert("数据集名称已被占用，请选择其他名称");
      }
    },
    async checkUploadFileNameAvailability(name) {
      // 调用后端API检查名称是否可用，返回布尔值
      if (this.datasetSaveName) {
        try{
          await axios.get(ServiceRoute['java-springboot']['train-data'] + '/get-by-name/' + name, {
            headers: {
              'Content-Type': 'application/json;charset=UTF-8'
            }
          })
          return false;
        } catch (error) {
          if (error.response.status === 404) {
            return true; // 如果API调用返回404状态，说明名称可用
          }
          // 如果发生其他错误，可能需要处理或显示错误消息
          console.error("Error checking name availability:", error);
          return false;
        }
      } else {
        alert("请输入数据集名称")
        return false;
      }

    },

    async checkTemplateName() {
      const nameAvailable = await this.checkTemplateNameAvailability(this.templateSaveName);
      if (nameAvailable) {
        this.validTemplateSaveName = true
      } else {
        this.validTemplateSaveName = false
        alert("数据集模板名称已被占用，请选择其他名称");
      }
    },
    async checkTemplateNameAvailability(name) {
      // 调用后端API检查名称是否可用，返回布尔值
      if (this.templateSaveName) {
        try {
          await axios.get(ServiceRoute['java-springboot']['train-data'] + '/get-template-by-name/' + name, {
            headers: {
              'Content-Type': 'application/json;charset=UTF-8'
            }
          })
        } catch (error) {
            if (error.response.status === 404) {
              return true; // 如果API调用返回404状态，说明名称可用
            }
            // 如果发生其他错误，可能需要处理或显示错误消息
            console.error("Error checking name availability:", error);
            return false;
          }
        } else {
          alert("请输入数据集名称")
          return false;
        }

    },
  },


}
</script>

<style scoped>

</style>