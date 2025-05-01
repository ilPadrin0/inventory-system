<!-- src/views/Register.vue -->
<template>
  <div class="auth-form">
    <h2>회원가입</h2>
    <form @submit.prevent="signup">
      <input v-model="username" placeholder="이메일" required />
      <input
        v-model="password"
        type="password"
        placeholder="비밀번호"
        required
      />
      <label>
        <input type="checkbox" v-model="isAdmin" />
        관리자 계정으로 가입
      </label>
      <button type="submit">가입하기</button>
    </form>
    <p v-if="message" :class="{ error: isError }">{{ message }}</p>
  </div>
</template>

<script>
import api from "../utils/axios";
import { clearToken } from "../utils/auth";

export default {
  data() {
    return {
      username: "",
      password: "",
      isAdmin: false,
      message: "",
      isError: false,
    };
  },
  methods: {
    async signup() {
      // 혹시 이전에 남은 토큰이 있다면 제거
      clearToken();
      try {
        await api.post("/auth/signup", {
          username: this.username,
          password: this.password,
          admin: this.isAdmin,
        });
        this.isError = false;
        this.message =
          "회원가입이 완료되었습니다! 1초 후 로그인 페이지로 이동합니다.";
        setTimeout(() => this.$router.push({ name: "Login" }), 1000);
      } catch (e) {
        this.isError = true;
        this.message = e.response?.data || "회원가입에 실패했습니다.";
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
