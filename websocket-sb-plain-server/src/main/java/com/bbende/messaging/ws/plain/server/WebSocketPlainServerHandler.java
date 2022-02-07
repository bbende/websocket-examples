package com.bbende.messaging.ws.plain.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketPlainServerHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketPlainServerHandler.class);

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        sessions.put(session.getId(), session);
        LOGGER.info("Connection established for session [{}]", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        LOGGER.info("Received message from client session [{}] with body [{}]", session.getId(), message.getPayload());
        final WebSocketMessage<String> responseMessage = new TextMessage("Server acknowledges \"" + message.getPayload() + "\"");
        session.sendMessage(responseMessage);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessions.remove(session.getId());
        LOGGER.info("Connection closed for session [{}]", session.getId());
    }

    public void broadcastMessage(String message) {
        sessions.values().forEach(session -> {
            try {
                LOGGER.info("Broadcasting message to session [{}] with body [{}]", session.getId(), message);
                final WebSocketMessage<String> responseMessage = new TextMessage(message);
                session.sendMessage(responseMessage);
            } catch (Exception e) {
                LOGGER.error("Unable to broadcast message to session " + session.getId(), e);
            }
        });
    }

}
