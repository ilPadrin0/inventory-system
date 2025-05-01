<!-- src/views/Login.vue -->
<template>
  <div class="auth-form">
    <h2>로그인</h2>
    <form @submit.prevent="login">
      <input v-model="username" placeholder="이메일" required />
      <input
        v-model="password"
        type="password"
        placeholder="비밀번호"
        required
      />
      <button type="submit">로그인</button>
    </form>
    <p v-if="error" class="error">{{ error }}</p>
  </div>
</template>

<script>
import api from "../utils/axios";
import { setToken } from "../utils/auth";

export default {
  data() {
    return {
      username: "",
      password: "",
      error: "",
    };
  },
  methods: {
    async login() {
      try {
        const res = await api.post("/auth/login", {
          username: this.username,
          password: this.password,
        });
        setToken(res.data.token);
        this.$router.push({ name: "Home" });
      } catch (e) {
        this.error = e.response?.data || "로그인에 실패했습니다.";
      }
    },
  },
};
</script>

<style scoped>
.auth-form {
  max-width: 320px;
  margin: 100px auto;
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.auth-form input {
  padding: 8px;
  font-size: 14px;
}
.auth-form button {
  padding: 10px;
  font-size: 16px;
  cursor: pointer;
}
.error {
  color: red;
  font-size: 13px;
}
</style>
