<!-- src/components/NavBar.vue -->
<template>
  <nav class="navbar">
    <router-link to="/">홈</router-link>
    <router-link v-if="!loggedIn" to="/login">로그인</router-link>
    <router-link v-if="!loggedIn" to="/signup">회원가입</router-link>
    <button v-if="loggedIn" @click="logout">로그아웃</button>
  </nav>
</template>

<script>
import { getToken, clearToken } from "../utils/auth";
export default {
  computed: {
    loggedIn() {
      return !!getToken();
    },
  },
  methods: {
    logout() {
      clearToken();
      this.$router.push({ name: "Login" });
    },
  },
};
</script>

<style scoped>
.navbar {
  display: flex;
  gap: 10px;
  padding: 10px;
  background: #f5f5f5;
}
.navbar a,
.navbar button {
  text-decoration: none;
  color: #333;
  font-weight: bold;
  background: none;
  border: none;
  cursor: pointer;
}
</style>
