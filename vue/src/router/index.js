import {createRouter, createWebHashHistory} from "vue-router";
import ModelsPage from "@/views/Models.vue";
import SignIn from "@/views/SignIn.vue";
import SignUp from "@/views/SignUp.vue";
import TrainModel from "@/views/Train.vue";
import TrainResult from "@/views/TrainResult.vue";
import Predict from "@/views/Predict.vue";
import PredictResult from "@/views/PredictResult.vue";
import TrainDataset from "@/views/TrainDataset.vue";
import store from "@/store";
import PredictDataset from "@/views/PredictDataset.vue";

const routes = [
  {
    path: "/",
    name: "Models",
    component: ModelsPage,
    // meta: { requiresAuth: true }
    meta: { roles: ["admin"] }
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
    path: "/train-result/:model/:filePath/:label/:mission/:usingExistedTrainData",
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
    path: "/TrainDataset",
    name: "TrainDataset",
    component: TrainDataset,
    meta: { roles: ["admin"] }
  },
  {
    path: "/PredictDataset",
    name: "PredictDataset",
    component: PredictDataset,
    meta: { roles: ["admin"] }
  },
  {
    path: '/auth-callback',
    name: 'Auth Callback',
    beforeEnter(to, from, next) {
      console.log(to)
      next('/');
    },
  }
];

const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  // history: createWebHistory(import.meta.env.BASE_URL),
  routes,
  linkActiveClass: "active",
});

router.beforeEach((to, from, next) => {
  if (to.name === 'Auth Callback') {
    next();
  } else {
    const userRoles = store.state.userRoles;
    if (to.meta.roles && !to.meta.roles.some(role => userRoles.includes(role))) {
      next({ path: "/error" });
    } else {
      store.commit("setCurrentPath", to.path);
      next();
    }
  }
});

export default router;
