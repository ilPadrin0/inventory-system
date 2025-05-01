//main.js
import { createApp } from "vue";
import "./style.css";
import App from "./App.vue";

import Toast, { POSITION } from "vue-toastification";
import "vue-toastification/dist/index.css";

import router from "./router";

const app = createApp(App);
app.use(router);

app.use(Toast, {
  position: POSITION.TOP_RIGHT,
  timeout: 2000,
  closeOnClick: true,
  pauseOnHover: true,
});

app.mount("#app");
