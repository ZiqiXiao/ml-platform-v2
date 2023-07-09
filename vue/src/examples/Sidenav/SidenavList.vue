<template>
  <div
    id="sidenav-collapse-main"
    class="w-auto h-auto collapse navbar-collapse max-height-vh-100 h-100"
  >
    <ul class="navbar-nav">
<!--      <li class="nav-item">-->
<!--        <sidenav-collapse nav-text="Dashboard" :to="{ name: 'Dashboard' }">-->
<!--          <template #icon>-->
<!--            <icon name="dashboard" />-->
<!--          </template>-->
<!--        </sidenav-collapse>-->
<!--      </li>-->
      <li class="nav-item">
        <sidenav-collapse v-show="userRoles.includes('admin')" :nav-text="pageName['Models']" :to="{ name: 'Models' }">
          <template #icon>
            <icon name="office" />
          </template>
        </sidenav-collapse>
      </li>
      <li class="nav-item">
        <sidenav-collapse v-show="userRoles.includes('admin') || userRoles.includes('train')" :nav-text="pageName['Train']" :to="{ name: 'Train' }">
          <template #icon>
            <icon name="tables" />
          </template>
        </sidenav-collapse>
      </li>
      <li class="nav-item">
        <sidenav-collapse v-show="userRoles.includes('admin') || userRoles.includes('predict')" :nav-text="pageName['Predict']" :to="{ name: 'Predict' }">
          <template #icon>
            <icon name="tables" />
          </template>
        </sidenav-collapse>
      </li>
      <li class="nav-item">
        <sidenav-collapse v-show="userRoles.includes('admin') || userRoles.includes('train')"  :nav-text="pageName['TrainDataset']" :to="{ name: 'TrainDataset' }">
          <template #icon>
            <icon name="tables" />
          </template>
        </sidenav-collapse>
      </li>
      <li class="nav-item">
<!--        <sidenav-collapse :nav-text="pageName['PredictDataset']" :to="{ name: 'PredictDataset' }">-->
<!--          <template #icon>-->
<!--            <icon name="tables" />-->
<!--          </template>-->
<!--        </sidenav-collapse>-->
      </li>
    </ul>
  </div>
</template>
<script>
import Icon from "@/components/Icon.vue";
import SidenavCollapse from "./SidenavCollapse.vue";
import PageName from "@/utils/PageName";

export default {
  name: "SidenavList",
  components: {
    Icon,
    SidenavCollapse,
    // SidenavCard,
  },
  props: {
    cardBg: {
      type: String,
      default: ""
    },
  },
  data() {
    return {
      title: "Vite Soft UI Dashboard",
      controls: "dashboardsExamples",
      isActive: "active",
      pageName: PageName,
      userRoles: null,  // 这是用来保存用户角色的数据
    };
  },
  created() {
    this.userRoles = localStorage.getItem('userRoles');
  },
  methods: {
    getRoute() {
      const routeArr = this.$route.path.split("/");
      return routeArr[1];
    },

  },
};
</script>
