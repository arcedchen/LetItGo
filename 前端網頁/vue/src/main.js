import { createApp } from "vue";
import { createPinia } from "pinia";
import axios from "axios";
import App from "./App.vue";
import router from "./router";
import { useMemberStore } from "./stores/memberStore";
import Loading from "./components/Loading.vue";

const OA2_URL = "http://localhost:8080/letitgo";
const API_URL = "http://localhost:8080/letitgo/shop/api";
axios.defaults.withCredentials = true;
axios.get(`${API_URL}/check`).then((rs) => {
  if (rs.data) {
    const loggedInMember = JSON.parse(sessionStorage.getItem("loggedInMember"));
    useMemberStore().loginSuccess(loggedInMember);
  }
});
const app = createApp(App);
app.use(createPinia());
app.use(router);
app.config.globalProperties.OA2_URL = OA2_URL;
app.config.globalProperties.API_URL = API_URL;
app.component("Loading", Loading);
app.mount("#app");
