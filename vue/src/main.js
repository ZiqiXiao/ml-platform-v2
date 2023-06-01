/*
=========================================================
* Vite Soft UI Dashboard - v1.0.0
=========================================================
* Product Page: https://creative-tim.com/product/vite-soft-ui-dashboard
* Copyright 2022 Creative Tim (https://www.creative-tim.com)
Coded by www.creative-tim.com
* Licensed under MIT (https://github.com/creativetimofficial/vite-soft-ui-dashboard/blob/556f77210e261adc3ec12197dab1471a1295afd8/LICENSE.md)
=========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
*/ 

import { createApp } from 'vue'
import App from './App.vue'
import store from "./store";
import router from "./router";
import "./assets/css/nucleo-icons.css";
import "./assets/css/nucleo-svg.css";
import SoftUIDashboard from "./soft-ui-dashboard";

import Keycloak from "keycloak-js";



// 创建一个新的 Keycloak 实例
const keycloak = new Keycloak({
  url: 'http://127.0.0.1:8081',  // 替换为你的 Keycloak 服务器地址
  realm: 'vue',  // 替换为你的 realm
  clientId: 'vuejs',  // 替换为你的 client ID
  redirectUri: 'http://127.0.0.1:3000/',  // 替换为你的应用的 URL
  locale: 'zh',
});

// keycloak.updateToken(60)
// 初始化 Keycloak
keycloak.init({onLoad: 'login-required'}).then(authenticated => {
  if (authenticated) {
    // console.log(keycloak.tokenParsed)
    const userRoles = keycloak.tokenParsed.realm_access.roles;
    const username = keycloak.tokenParsed.preferred_username;
    localStorage.setItem('userRoles',userRoles);
    store.commit('setUserRoles', userRoles);
    store.commit('setUsername', username);
    router.push('/auth-callback');
  } else {
    // 如果用户未登录，那么重定向到登录页面
    window.location.reload();
  }
}).catch((error) => {
  // 如果在初始化 Keycloak 时发生错误，那么在这里处理它
  console.error('Keycloak initialization failed',error)
});

const app = createApp(App)
app.use(store)
app.use(router)
app.use(SoftUIDashboard)
app.mount('#app')
app.config.globalProperties.$keycloak = keycloak

export default keycloak;