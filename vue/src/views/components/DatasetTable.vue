<template>
  <div class="card mb-4">
    <div class="card-body px-0 pt-0 pb-2">
      <div class="table-responsive p-0 overflow-auto" style="height: 500px;">
        <table class="table align-items-center mb-0">
          <thead>
          <tr>
            <th class="text-uppercase text-secondary text-s font-weight-bolder opacity-7">编号</th>
            <th
              class="text-uppercase text-secondary text-s font-weight-bolder opacity-7 ps-2"
            >名称</th>
            <th
              class="text-center text-uppercase text-secondary text-s font-weight-bolder opacity-7"
            >操作</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(item, index) in tableData"  :key="index">
            <td v-if="item.fileName">
              <div class="d-flex px-2 py-1">
                <div class="d-flex flex-column justify-content-center">
                  <h6 class="mb-0 text-sm">{{ item.id }}</h6>
                </div>
              </div>
            </td >
            <td v-if="item.fileName">
              <p class="text-xs font-weight-bold mb-0">{{ item.fileName }}</p>
            </td>
            <td v-if="item.fileName" class="align-middle text-center text-sm">
              <button class="btn btn-sm btn-secondary" @click="renameField(item.id, tableData, 'fileName',)">重命名</button>
              <button class="btn btn-sm btn-danger" @click="deleteModel(item.id)">删除</button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import serviceRoute from "@/utils/service-route";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "DatasetTable",

  props: {
    scenario: {
      type: String,
      required: true,
    },
  },

  data() {
    return {
      tableData: [],
    }
  },

  mounted() {
    this.fetchTableData();
  },

  methods: {

    async fetchTableData() {
      try {
        console.log(this.scenario)
        const response = await axios.get(serviceRoute['java-springboot'][this.scenario] + '/get-all');
        this.tableData = response.data;
        console.log('Data fetched:', this.tableData);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    },

    async renameField(modelId, tableData, nameFiled) {
      const newName = prompt("请输入新的名称：");
      if (newName) {
        try {
          // 在发送请求前先检查新名称是否存在于当前数据列表中
          const isDuplicate = tableData.some(item => item[nameFiled] === newName);
          if (isDuplicate) {
            alert("名称已存在，请重新输入！");
            return;
          }
          const response = await axios.post(serviceRoute['java-springboot'][this.scenario] + `/update-file-name`, {
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
          alert("重命名失败，请重试！");
          console.error("重命名请求出错：", error);
        }
      }
    },

    async deleteModel(modelId) {
      if (confirm("确定要删除吗？")) {
        try {
          const response = await axios.delete(serviceRoute['java-springboot'][this.scenario] +`/delete/${modelId}`);
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

    // async downloadTemplate(modelId) {
    //   //   TODO: 下载模板
    // }
  },

}

</script>

<style scoped>

</style>