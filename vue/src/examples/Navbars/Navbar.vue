<template>
  <nav
    v-bind="$attrs"
    id="navbarBlur"
    class="shadow-none navbar navbar-main navbar-expand-lg border-radius-xl"
    data-scroll="true"
  >
    <div class="px-3 py-1 container-fluid">
      <breadcrumbs :current-page="currentRouteName" :text-white="textWhite" />
        <ul class="navbar-nav justify-content-end">
          <li class="nav-item d-flex align-items-center">
            <span style="margin-right: 20px">当前用户：{{ store.state.username }}</span>
            <button class="btn-sm" @click="handleLoginLogout">退出</button>
          </li>
        </ul>
      </div>
  </nav>
</template>
<script>
import Breadcrumbs from "../Breadcrumbs.vue";
import { mapMutations, mapActions } from "vuex";
import PageName from "@/utils/PageName";
import store from "../../store";
import keycloak from "@/main";

export default {
  name: "NavbarComponent",

  components: {
    Breadcrumbs,
  },
  props: {
  },
  data() {
    return {

    };
  },
  computed: {
    store() {
      return store
    },
    currentRouteName() {
      const currentRoute = this.$route.name;
      return PageName[currentRoute] || currentRoute;
    },
  },
  created() {
    this.minNav;
  },
  updated() {
    const navbar = document.getElementById("navbarBlur");
    window.addEventListener("scroll", () => {
      if (window.scrollY > 10 && this.$store.state.isNavFixed) {
        navbar.classList.add("blur");
        navbar.classList.add("position-sticky");
        navbar.classList.add("shadow-blur");
      } else {
        navbar.classList.remove("blur");
        navbar.classList.remove("position-sticky");
        navbar.classList.remove("shadow-blur");
      }
    });
  },
  methods: {
    ...mapMutations(["navbarMinimize", "toggleConfigurator"]),
    ...mapActions(["toggleSidebarColor"]),

    handleLoginLogout() {
      keycloak.logout()
    },
  },
};
</script>
