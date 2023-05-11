<template>
  <div class="card mb-4">
    <div class="card-header pb-0">
      <h6>模型列表</h6>
    </div>
    <div class="card-body px-0 pt-0 pb-2">
      <div class="table-responsive p-0">
        <table class="table align-items-center mb-0">
          <thead>
            <tr>
              <th class="text-uppercase text-secondary text-s font-weight-bolder opacity-7">模型编号</th>
              <th
                class="text-uppercase text-secondary text-s font-weight-bolder opacity-7 ps-2"
              >模型名称</th>
              <th
                class="text-center text-uppercase text-secondary text-s font-weight-bolder opacity-7"
              >操作</th>
              <th
                class="text-center text-uppercase text-secondary text-s font-weight-bolder opacity-7"
              >数据模版名称</th>
              <th
                  class="text-center text-uppercase text-secondary text-s font-weight-bolder opacity-7"
              >操作</th>
              <th
                class="text-center text-uppercase text-secondary text-s font-weight-bolder opacity-7"
              >训练数据集</th>
              <th class="text-secondary opacity-7"></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, index) in tableData" :key="index">
              <td>
                <div class="d-flex px-2 py-1">
                  <div class="d-flex flex-column justify-content-center">
                    <h6 class="mb-0 text-sm">{{ item.id }}</h6>
                  </div>
                </div>
              </td>
              <td>
                <p class="text-xs font-weight-bold mb-0">{{ item.modelName }}</p>
              </td>
              <td class="align-middle text-center text-sm">
                <button class="btn btn-sm btn-secondary" @click="renameField(item.id, tableData, 'modelName','update-model-name')">重命名</button>
                <button class="btn btn-sm btn-danger" @click="deleteModel(item.id)">删除</button>
              </td>
              <td class="align-middle text-center">
                <span class="text-secondary text-xs font-weight-bold">{{ item.uploadFile.templateName }}</span>
              </td>
              <td class="align-middle text-center text-sm">
                <button @click="renameField(item.id, tableData, 'templateName','update-template-name')" class="btn btn-sm btn-secondary">重命名</button>
                <button class="btn btn-sm btn-success" @click="downloadTemplate(item.id)">下载</button>
              </td>
              <td class="align-middle text-center">
                <span v-if="item.uploadFile" class="text-secondary text-xs font-weight-bold">{{ item.uploadFile.fileName }}</span>
                <span v-else class="text-secondary text-xs font-weight-bold">-</span>
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

export default {
  name: "ModelTable",
  components: {
  },

  data() {
    return {
      tableData: []
    };
  },

  methods: {
    async fetchTableData() {
      try {
        const response = await axios.get('http://127.0.0.1:8080/model/get-all');
        this.tableData = response.data;
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
          const response = await axios.post(`http://127.0.0.1:8080/model/${endpoint}`, {
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
          const response = await axios.delete(`http://127.0.0.1:8080/model/delete/${modelId}`);
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

    async downloadTemplate(modelId) {
    //   TODO: 下载模板
    }
  },

  mounted() {
    this.fetchTableData();
  },
};
</script>