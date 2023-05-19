import { createRouter, createWebHashHistory } from "vue-router";
import Dashboard from "@/views/Dashboard.vue";
import ModelsPage from "@/views/Models.vue";
import Billing from "@/views/Billing.vue";
import VirtualReality from "@/views/VirtualReality.vue";
import Profile from "@/views/Profile.vue";
import Rtl from "@/views/Rtl.vue";
import SignIn from "@/views/SignIn.vue";
import SignUp from "@/views/SignUp.vue";
import TrainModel from "@/views/Train.vue";
import TrainResult from "@/views/TrainResult.vue";
import Predict from "@/views/Predict.vue";
import PredictResult from "@/views/PredictResult.vue";

const routes = [
  {
    path: "/",
    name: "/",
    redirect: "/dashboard",
  },
  {
    path: "/dashboard",
    name: "Dashboard",
    component: Dashboard,
  },
  {
    path: "/models",
    name: "Models",
    component: ModelsPage,
  },
  {
    path: "/billing",
    name: "Billing",
    component: Billing,
  },
  {
    path: "/virtual-reality",
    name: "Virtual Reality",
    component: VirtualReality,
  },
  {
    path: "/profile",
    name: "Profile",
    component: Profile,
  },
  {
    path: "/rtl-page",
    name: "Rtl",
    component: Rtl,
  },
  {
    path: "/sign-in",
    name: "Sign In",
    component: SignIn,
  },
  {
    path: "/sign-up",
    name: "Sign Up",
    component: SignUp,
  },
  {
    path: "/train",
    name: "Train",
    component: TrainModel,
  },
  {
    path: "/train-result/:model/:filePath/:label",
    name: "Train Result",
    component: TrainResult,
    meta: { isFromTrain: false }
  },
  {
    path: "/predict",
    name: "Predict",
    component: Predict,
  },
  {
    path: "/predict-result/:modelPath/:filePath",
    name: "Predict Result",
    component: PredictResult,
  }
];

const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes,
  linkActiveClass: "active",
});

export default router;
