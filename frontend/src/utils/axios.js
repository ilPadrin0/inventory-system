// src/utils/axios.js
import axios from "axios";
import { getToken, clearToken } from "./auth";

const instance = axios.create({
  baseURL: "http://localhost:8080/api",
  headers: {
    "Content-Type": "application/json",
  },
});

// 요청 인터셉터: 요청할 때마다 localStorage의 JWT를 헤더에 붙입니다.
instance.interceptors.request.use(
  (config) => {
    const token = getToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error),
);

// 응답 인터셉터: 401 Unauthorized 응답이 오면 토큰을 제거하고 /login으로 리다이렉트합니다.
instance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      clearToken();
      window.location.replace("/login");
    }
    return Promise.reject(error);
  },
);

export default instance;
