// src/utils/realtime.js
import SockJS from "sockjs-client";
import Stomp from "stompjs";

let stompClient = null;

export function connectRealtime(onMessage) {
  const socket = new SockJS("http://localhost:8080/ws");
  stompClient = Stomp.over(socket);
  stompClient.connect({}, () => {
    stompClient.subscribe("/topic/inventory", (msg) => {
      const data = JSON.parse(msg.body);
      onMessage(data);
    });
  });
}

export function disconnectRealtime() {
  if (stompClient) {
    stompClient.disconnect(); // 콜백 없이 단순히 연결 해제
    stompClient = null; // 클라이언트 초기화
  }
}
