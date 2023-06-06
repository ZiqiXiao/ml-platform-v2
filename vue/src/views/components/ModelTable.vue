<template>
  <div class="card mb-4">
    <div class="card-body px-0 pt-0 pb-2">
      <input v-model="search" type="text" class="form-control mb-3" placeholder="搜索模型" />
      <div class="table-responsive p-0 overflow-auto" style="height: 500px;">
        <table class="table align-items-center mb-0">
          <thead>
            <tr>
              <th
                class="text-uppercase text-primary text-s font-weight-bolder opacity-7 ps-2"
              >模型名称</th>
              <th
                class="text-center text-uppercase text-primary text-s font-weight-bolder opacity-7"
              >操作</th>
              <th
                class="text-center text-uppercase text-primary text-s font-weight-bolder opacity-7"
              >数据模版名称</th>
              <th
                  class="text-center text-uppercase text-primary text-s font-weight-bolder opacity-7"
              >操作</th>
              <th
                  class="text-center text-uppercase text-primary text-s font-weight-bolder opacity-7"
              >数据集</th>
              <th
                  class="text-center text-uppercase text-primary text-s font-weight-bolder opacity-7"
              >描述</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, index) in sortedFilteredModels" :key="index">
              <td>
                <p class="text-xs font-weight-bold mb-0">{{ item.modelName }}</p>
              </td>
              <td class="align-middle text-center text-sm">
                <button class="btn btn-sm btn-secondary" @click="renameField(item.id, tableData, 'modelName','model/update-model-name')">重命名</button>
                <button class="btn btn-sm btn-danger" @click="deleteModel(item.id)">删除</button>
              </td>
              <td class="align-middle text-center">
                <span class="text-secondary text-xs font-weight-bold">{{ item.uploadFile ? item.uploadFile.templateName : '' }}</span>
              </td>
              <td class="align-middle text-center text-sm">
                <button class="btn btn-sm btn-secondary" @click="renameField(item.id, tableData, 'templateName','train-data/update-template-name')">重命名</button>
                <button class="btn btn-sm btn-success" @click="downloadTemplate(item.uploadFile.templatePath, item.uploadFile.templateName)">下载</button>
              </td>
              <td>
                <p class="text-xs font-weight-bold mb-0">{{ item.uploadFile && item.uploadFile.fileName!==null ? item.uploadFile.fileName : '' }}</p>
              </td>
              <td>
                <p class="text-xs font-weight-bold mb-0">{{ item.modelDescription }}</p>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import serviceRoute from "@/utils/service-route";

export default {
  name: "ModelTable",
  components: {
  },

  data() {
    return {
      tableData: [],
      search: "",
    };
  },
  
  computed: {
    sortedFilteredModels() {
      return this.filteredModels.slice().sort((a, b) => {
        const aValue = a.modelName;
        const bValue = b.modelName;
        return aValue.localeCompare(bValue);
      });
    },

    filteredModels() {
      return this.tableData.filter((model) => {
        // const isMissionMatch = this.selectedMission ? model.mission === this.selectedMission : true;
        // const isModelClassMatch = this.selectedModelClass ? model.modelClass === this.selectedModelClass : true;

        return model.modelName && model.modelName.toLowerCase().includes(this.search.toLowerCase());
      });
    },
  },

  mounted() {
    this.fetchTableData();
    console.log(this.tableData)
  },

  methods: {
    async fetchTableData() {
      try {
        const response = await axios.get(serviceRoute['java-springboot']['model'] + '/get-all');
        this.tableData = response.data;
        console.log('Data fetched:', this.tableData);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    },

    async renameField(modelId, tableData, nameFiled, endpoint) {
      const newName = prompt("请输入新的模型名称：");
      if (newName) {
        try {
          // 在发送请求前先检查新名称是否存在于当前数据列表中
          const isDuplicate = tableData.some(item => item[nameFiled] === newName);
          if (isDuplicate) {
            alert("名称已存在，请重新输入！");
            return;
          }
          const response = await axios.post(serviceRoute['java-springboot']+`/${endpoint}`, {
            id: modelId,
            newName: newName
          });
          if (response.status === 200) {
            // 返回成功消息
            alert("重命名成功！");
            // 更新视图中的数据
            const itemToUpdate = tableData.find(item => item.id === modelId);
            itemToUpdate[nameFiled] = newName;
          } else {
            alert("重命名失败，请重试！");
          }
        } catch (error) {
          console.error("重命名请求出错：", error);
        }
      }
    },

    async deleteModel(modelId) {
      if (confirm("确定要删除此模型吗？")) {
        try {
          const response = await axios.delete(serviceRoute['java-springboot']['model']+`/delete/${modelId}`);
          if (response.status === 204) {
            // 返回成功消息
            alert("删除成功！");
            // 从视图中移除已删除的数据
            this.tableData = this.tableData.filter(item => item.id !== modelId);
          } else {
            alert("删除失败，请重试！");
          }
        } catch (error) {
          console.error("删除请求出错：", error);
        }
      }
    },

    async downloadTemplate(templatePath, templateName) {
      await axios.post(serviceRoute['python-flask'] + '/download-template', {
        templatePath: templatePath}, {
        responseType: 'blob'
      }).then(res => {
        if (res.status === 200) { 
          const url = window.URL.createObjectURL(new Blob([res.data]));
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', templateName+'.csv'); // 设置你期望的文件名
          document.body.appendChild(link);
          link.click();
          console.log('download template success')
        } else {
          alert('下载失败')
        }
      })
    }
  },
};
</script>