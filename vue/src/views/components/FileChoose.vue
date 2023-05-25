<template>

  <div class="card-header">
    <ul class="nav nav-tabs card-header-tabs">
      <li class="nav-item">
        <a class="nav-link" :class="{ 'active': currentTab === 'select' }" href="#"
           @click.prevent="changeTab('select')">选择数据集</a>
      </li>
      <li v-if="scenario==='predict-data'" class="nav-item">
        <a class="nav-link" :class="{ 'active': currentTab === 'fill' }" href="#"
           @click.prevent="changeTab('fill')">手动填入数据集</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" :class="{ 'active': currentTab === 'upload' }" href="#"
           @click.prevent="changeTab('upload')">上传数据集</a>
      </li>
    </ul>
  </div>
  <div class="card-body">
    <div v-if="currentTab === 'select'">
      <input v-model="search" type="text" class="form-control mb-3" placeholder="搜索数据集" />
      <select v-model="selectedDataset" class="form-select" size="5">
        <option v-for="dataset in filteredDatasets" :key="dataset.id" :value="dataset.fileName">{{ dataset.fileName }}
        </option>
      </select>
    </div>
    <div v-if="currentTab === 'upload'">
      <input type="file" @change="onFileChange" />
    </div>
    <div v-if="currentTab === 'fill'">
      <input v-model="search" type="text" class="form-control mb-3" placeholder="搜索数据集" />
      <select v-model="selectedTemplate" class="form-select" size="5">
        <option v-for="temp in filteredTemplates" :key="temp.id" :value="temp.templateName">{{ temp.templateName }}
        </option>
      </select>
      <div class="card-footer">
        <button class="btn btn-primary" @click="confirmTemplate">确认</button>
      </div>
      <table class="table">
        <thead>
        <tr>
          <th>特征</th>
          <th v-for="(row, rowIndex) in rowData" :key="rowIndex">样本 {{ rowIndex + 1 }}</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(header, columnIndex) in columnHeaders" :key="columnIndex">
          <td>{{ header }}</td>
          <td v-for="(row, rowIndex) in rowData" :key="rowIndex">
            <input v-model="row[header]" type="number" class="form-control" />
          </td>
        </tr>
        </tbody>
      </table>
      <button v-if="selectedTemplate" class="btn btn-secondary" @click="addRow" style="margin-right: 10px">添加列</button>
      <button v-if="selectedTemplate" class="btn btn-secondary" @click="removeRow">删除列</button>
    </div>
    <div class="card-footer">
      <button class="btn btn-primary" @click="confirm">确认</button>
      <button
        v-if="scenario === 'predict-data' && currentTab === 'upload'"
        class="btn btn-primary"
        style="margin-left: 20px"
        @click="saveDataToServer"
      >
        保存至服务器
      </button>
    </div>
  </div>

</template>

<script>
import axios from "axios";
import serviceRoute from "@/utils/service-route";

export default {
  name: "FileChoose",
  props: {
    modelValue: {
      type: String,
      default: null,
    },
    scenario: {
      type: String,
      default: null,
    },
  },

  emits: ['putFilePath'],

  data() {
    return {
      currentTab: "select",
      datasets: [], // 从后端获取的数据集列表
      templates: [],
      selectedDataset: null,
      selectedTemplate: null,
      uploadedFile: null,
      uploadedTmpPath: null,
      filePath: null,
      search: "",
      columnHeaders: [],
      rowData: [{}],
    };
  },

  computed: {
    filteredDatasets() {
      return this.datasets.filter((dataset) =>
        dataset.fileName && dataset.fileName.toLowerCase().includes(this.search.toLowerCase())
      );
    },

    filteredTemplates() {
      return this.templates.filter((temp) =>
        temp.templateName && temp.templateName.toLowerCase().includes(this.search.toLowerCase())
      );
    },
  },

  watch: {
    filePath: {
      handler(newValue) {
        this.$emit('putFilePath', newValue);
      },
      immediate: true,
    }

  },
  mounted() {
    this.fetchDatasets();
    this.fetchTemplates();
    this.fetchUploadTmpPath();
  },

  methods: {
    changeTab(tab) {
      this.currentTab = tab;
    },

    async fetchSelectDatasetPath(templateName) {
      try {
        const response = await axios.get(serviceRoute["java-springboot"][this.scenario] + "/get-by-name/" + templateName);
        this.filePath = response.data.filePath;
      } catch (error) {
        console.error("Error fetching select dataset path:", error);
      }
    },

    async fetchDatasets() {
      try {
        const response = await axios.get(serviceRoute["java-springboot"][this.scenario] + "/get-all");
        console.log()
        this.datasets = response.data;
      } catch (error) {
        console.error("Error fetching training data:", error);
      }
    },

    async fetchTemplates() {
      try {
        const response = await axios.get(serviceRoute["java-springboot"]['train-data'] + "/get-all");
        this.templates = response.data;
      } catch (error) {
        console.error("Error fetching training data:", error);
      }
    },

    async getTemplateColumns() {
      try {
        const selectedTemplate = this.templates.find((temp) => temp.templateName === this.selectedTemplate);
        selectedTemplate && await axios.post(serviceRoute["python-flask"] + "/predict-template-headers", {
          templatePath: selectedTemplate.templatePath,
        }).then((res) => {
          this.columnHeaders = res.data.columnHeaders;
        });
      } catch (error) {
        console.error("Error fetching template columns:", error);
      }
    },

    async saveDataToServer() {
      try {
        let fileNameWithoutExtension = this.uploadedFile.name.split('.')[0];
        if (!this.uploadedFile) {
          alert("请先上传文件！");
        } else if (this.uploadedFile && !await this.checkFileExist(fileNameWithoutExtension)) {
          let shouldOverwrite = confirm("该文件已存在，是否覆盖？");

          if (shouldOverwrite) {
            await axios.post(serviceRoute["python-flask"] + "/overwrite-predict-data", {
              fileName: fileNameWithoutExtension, // 上传的文件名
              filePath: this.uploadedTmpPath,
            }).then(res => {
              console.log(res)
              if (res.status === 200) {
                alert("保存成功！");
              } else {
                alert("保存失败！");
              }
            })
          }
        } else if (this.uploadedFile && await this.checkFileExist(fileNameWithoutExtension)) {
          await axios.post(serviceRoute["java-springboot"]['predict-data'] + "/create", {
            fileName: fileNameWithoutExtension, // 上传的文件名
            filePath: this.uploadedTmpPath,
          }).then(res => {
            if (res.status === 201) {
              this.filePath = res.data.filePath
              alert("保存成功！");
            } else {
              alert("保存失败！");
            }
          })
        }
      } catch (error) {
        console.error("Error saving data to server:", error);
      }
    },

    addRow() {
      this.rowData.push({});
    },

    removeRow() {
      this.rowData.pop();
    },

    async fetchUploadTmpPath() {
      try {
        if (this.scenario === "train-data") {
          const response = await axios.get(serviceRoute["python-flask"] + "/train-data-upload-path");
          this.uploadedTmpPath = response.data;
        } else if (this.scenario === "predict-data") {
          const response = await axios.get(serviceRoute["python-flask"] + "/predict-data-upload-path");
          this.uploadedTmpPath = response.data;
        }
      } catch (error) {
        console.error("Error fetching upload tmp file path:", error);
      }
    },

    confirmTemplate() {
      if (this.selectedTemplate) {
        this.getTemplateColumns();
        alert("选择成功！");
      } else {
        alert("请选择一个模板！")
      }
    },

    confirm() {
      if (this.currentTab === "select") {
        if (this.selectedDataset) {
          const selectedDataset = this.datasets.find((dataset) => dataset.fileName === this.selectedDataset);
          this.filePath = selectedDataset.filePath
          // this.fetchSelectDatasetPath(this.selectedDataset)
          // console.log(this.filePath)
          alert("选择成功！");
        } else {
          alert("请选择一个数据集！")
        }
      } else if (this.currentTab === "upload") {
        this.uploadFile();
      } else if (this.currentTab === "fill") {
        this.putFillData();
      }
    },

    async putFillData() {
      const fillData = [];

      // 检查rowData的数据是否完整
      for (const column of this.rowData) {
        for (const header of this.columnHeaders) {
          if (!column[header]) {
            alert("请填写完整数据！");
            return;
          } else if (typeof column[header] !== "number") {
            alert(column[header] + "请填写正确的数据格式！");
            return;
          }
        }
      }

      // 遍历每一列
      for (const column of this.rowData) {
        const columnData = {};
        // 遍历每个表头并将对应的值添加到列数据对象中
        this.columnHeaders.forEach((header) => {
          columnData[header] = column[header];
        });
        // 将列数据对象添加到填充数据数组中
        fillData.push(columnData);
      }

      await axios.post(serviceRoute["python-flask"] + '/upload-fill-data', {
        fillData: fillData
      }).then((res) => {
        if (res.status === 200) {
          this.filePath = res.data.filePath
          alert("填充成功！");
        } else {
          alert("填充失败！");
        }
      }).catch((err) => {
        console.log(err);
      })
    },

    onFileChange(e) {
      this.uploadedFile = e.target.files[0];
    },

    uploadFile() {
      const formData = new FormData();
      formData.append("file", this.uploadedFile);
      formData.append("path", this.uploadedTmpPath)
      formData.append("overwrite", false)
      console.log(formData)
      axios.post(serviceRoute["python-flask"] + "/upload", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      })
        .then((response) => {
          if (response.status === 200) {
            alert("上传成功");
            this.filePath = this.uploadedTmpPath + "/" + this.uploadedFile.name;
          } else if (response.status === 409) {
            this.filePath = this.uploadedTmpPath + "/" + this.uploadedFile.name;
            if (window.confirm(`${response.data.message}文件已存在，是否覆盖？`)) {
              formData.set('overwrite', true)
              console.log(formData)
              axios
                .post(serviceRoute["python-flask"] + "/upload", formData, {
                  headers: {
                    "Content-Type": "multipart/form-data",
                  },
                })
                .then((response) => {
                  if (response.status === 200) {
                    alert("上传成功");
                    this.filePath = this.uploadedTmpPath + "/" + this.uploadedFile.name;
                  } else {
                    alert("上传失败" + response.data.message);
                  }
                })
            }
          }
        })
        .catch((error) => {
          console.error("Error uploading training data:", error);
        });
    },

    async checkFileExist(name) {
      console.log(name)
      try {
        let res = await axios.get(serviceRoute["java-springboot"]["predict-data"] + `/check/${name}`);
        console.log(res);
        if (res.status === 200) {
          this.filePath = res.data.filePath;
          return false;
        } else {
          console.log('Unexpected status code: ' + res.status);
          return true;
        }
      } catch (err) {
        if (err.response && err.response.status === 404) {
          console.log('File not exist');
          return true;
        } else {
          console.log(err);
          return false;
        }
      }
    },
  }
}
</script>

<style scoped>
.card {
    margin-bottom: 20px;
}
</style>