import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import axios from "axios";
import { library as faLibrary } from "@fortawesome/fontawesome-svg-core";
import { faHome, faSearch, faPlus } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import Vuelidate from "vuelidate";

// Bootstrap axios
axios.defaults.baseURL = "/api";
axios.defaults.headers.common.Accept = "application/json";
axios.interceptors.response.use(
  response => response,
  error => {
    return Promise.reject(error);
  }
);

// Enable Vuelidate
Vue.use(Vuelidate);

faLibrary.add(faHome);
faLibrary.add(faSearch);
faLibrary.add(faPlus);
Vue.component("font-awesome-icon", FontAwesomeIcon);

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");
