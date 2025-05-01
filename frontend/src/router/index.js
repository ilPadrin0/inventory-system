//index.js
import { createRouter, createWebHistory } from "vue-router";
import Login from "../views/Login.vue";
import Register from "../views/Register.vue";
import Home from "../views/Home.vue"; // 기존 ProductList가 있는 화면
import { getToken, getUserRole } from "../utils/auth";

const routes = [
  { path: "/login", name: "Login", component: Login },
  { path: "/signup", name: "Register", component: Register },
  {
    path: "/",
    name: "Home",
    component: Home,
    meta: { requiresAuth: true },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  const token = getToken();
  if (to.meta.requiresAuth && !token) {
    return next({ name: "Login" });
  }
  next();
});

export default router;
