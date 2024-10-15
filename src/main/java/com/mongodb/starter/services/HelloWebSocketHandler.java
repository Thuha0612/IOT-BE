package com.mongodb.starter.services;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class HelloWebSocketHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Gửi dữ liệu "hello" đến client khi kết nối thành công
        session.sendMessage(new TextMessage("hello"));
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Xử lý tin nhắn từ client
        System.out.println("Received message: " + message.getPayload());
        session.sendMessage(new TextMessage("Server received: " + message.getPayload()));
    }
}
