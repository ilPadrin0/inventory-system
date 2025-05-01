// src/utils/auth.js
export function setToken(token) {
  localStorage.setItem("jwt_token", token);
}
export function getToken() {
  return localStorage.getItem("jwt_token");
}
export function clearToken() {
  localStorage.removeItem("jwt_token");
}

export function getUserRole() {
  const token = getToken();
  if (!token) return null;
  try {
    const payload = JSON.parse(atob(token.split(".")[1]));
    return payload.role;
  } catch {
    return null;
  }
}
