<template>
  <div class="card text-center">
    <div class="card-header">
      <ul class="nav nav-tabs card-header-tabs">
        <li class="nav-item">
          <a class="nav-link" :class="{ 'active': currentTab === 'select' }" href="#"
            @click.prevent="changeTab('select')">选择数据集</a>
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
    </div>
    <div class="card-footer">
      <button class="btn btn-primary" @click="confirm">确认</button>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import serviceRoute from "@/utils/service-route";

export default {
  name: "UploadFile",
  props: {
    modelValue: {
      type: String,
      default: null,
    },
  },
  emits: ['putFilePath'],
  data() {
    return {
      currentTab: "select",
      datasets: [], // 从后端获取的数据集列表
      selectedDataset: null,
      uploadedFile: null,
      uploadedTmpPath: null,
      filePath: null,
      search: ""
    };
  },
  computed: {
    filteredDatasets() {
      return this.datasets.filter((dataset) =>
        dataset.fileName.toLowerCase().includes(this.search.toLowerCase())
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
    this.fetchUploadTmpPath()
  },

  methods: {
    changeTab(tab) {
      this.currentTab = tab;
    },

    async fetchDatasets() {
      try {
        const response = await axios.get(serviceRoute["java-springboot"]["train-data"] + "/get-all");
        this.datasets = response.data;
      } catch (error) {
        console.error("Error fetching training data:", error);
      }
    },

    async fetchUploadTmpPath() {
      try {
        const response = await axios.get(serviceRoute["python-flask"] + "/train-data-upload-path");
        this.uploadedTmpPath = response.data;
      } catch (error) {
        console.error("Error fetching upload tmp file path:", error);
      }
    },

    confirm() {
      if (this.currentTab === "select") {
        if (this.selectedDataset) {
          this.filePath = this.selectedDataset;
          alert("选择成功！");
        } else {
          alert("请选择一个数据集！")
        }

      } else if (this.currentTab === "upload") {
        this.uploadFile();
      }
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
      axios
        .post(serviceRoute["python-flask"] + "/upload", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then((response) => {
          if (response.data.status === 200) {
            alert("上传成功");
            this.filePath = this.uploadedTmpPath + "/" + this.uploadedFile.name;
          } else if (response.data.status === 409) {
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
                  if (response.data.status === 200) {
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
    }
  },
}
</script>

<style scoped>
.card {
  margin-bottom: 20px;
}
</style>