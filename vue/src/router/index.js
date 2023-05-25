import { createRouter, createWebHashHistory} from "vue-router";
import ModelsPage from "@/views/Models.vue";
import SignIn from "@/views/SignIn.vue";
import SignUp from "@/views/SignUp.vue";
import TrainModel from "@/views/Train.vue";
import TrainResult from "@/views/TrainResult.vue";
import Predict from "@/views/Predict.vue";
import PredictResult from "@/views/PredictResult.vue";
import Dataset from "@/views/Dataset.vue";
import store from "@/store";

const routes = [
  {
    path: "/",
    name: "/",
    redirect: "/models",
    // meta: { requiresAuth: true }
    meta: { roles: ["admin", 'predict'] }
  },

  {
    path: "/models",
    name: "Models",
    component: ModelsPage,
    // meta: { requiresAuth: true }
    meta: { roles: ["admin", 'predict', 'train'] }
  },
  {
    path: "/sign-in",
    name: "Sign In",
    component: SignIn,
    meta: { roles: ["admin"] }
  },
  {
    path: "/sign-up",
    name: "Sign Up",
    component: SignUp,
    meta: { roles: ["admin"] }
  },
  {
    path: "/train",
    name: "Train",
    component: TrainModel,
    meta: { roles: ["admin", 'train'] }
  },
  {
    path: "/train-result/:model/:filePath/:label",
    name: "Train Result",
    component: TrainResult,
    meta: { roles: ["admin", 'train'] }
  },
  {
    path: "/predict",
    name: "Predict",
    component: Predict,
    meta: { roles: ["admin", 'predict'] }
  },
  {
    path: "/predict-result/:modelPath/:filePath",
    name: "Predict Result",
    component: PredictResult,
    meta: { roles: ["admin", 'predict'] }
  },
  {
    path: "/dataset",
    name: "Dataset",
    component: Dataset,
    meta: { roles: ["admin"] }
  }
];

const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes,
  linkActiveClass: "active",
});

router.beforeEach((to, from, next) => {
  const userRoles = store.state.userRoles;  // 从你的 Vuex store 中获取用户角色
  console.log(userRoles);
  if (to.meta.roles && !to.meta.roles.some(role => userRoles.includes(role))) {
    // 如果用户没有访问权限，可以重定向到错误页面或者首页等
    next({ path: "/error" });
  } else {
    next();
  }
});

export default router;
