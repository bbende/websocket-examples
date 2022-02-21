package com.bbende.messaging.ws.plain.server;

import com.bbende.messaging.ws.plain.ExampleMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class PlainJacksonServerHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlainJacksonServerHandler.class);

    private final ObjectMapper objectMapper;
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public PlainJacksonServerHandler(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        sessions.put(session.getId(), session);
        LOGGER.info("Connection established for session [{}]", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        final ExampleMessage receivedMessage = fromWebSocketMessage(message);
        LOGGER.info("Received message from client id [{}] with body [{}]",
                receivedMessage.getClientId(), receivedMessage.getMessage());

        final ExampleMessage responseMessage = new ExampleMessage();
        responseMessage.setClientId(receivedMessage.getClientId());
        responseMessage.setMessage("Server acknowledges [ " + receivedMessage.getMessage() + " ]");

        broadcastMessage(responseMessage);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessions.remove(session.getId());
        LOGGER.info("Connection closed for session [{}]", session.getId());
    }

    private WebSocketMessage<String> toWebSocketMessage(final ExampleMessage exampleMessage) throws JsonProcessingException {
        final String exampleMessageJson = objectMapper.writeValueAsString(exampleMessage);
        return new TextMessage(exampleMessageJson);
    }

    private ExampleMessage fromWebSocketMessage(final WebSocketMessage<String> webSocketMessage) throws JsonProcessingException {
        return objectMapper.readValue(webSocketMessage.getPayload(), ExampleMessage.class);
    }

    private void broadcastMessage(final ExampleMessage exampleMessage) {
        sessions.values().forEach(session -> {
            try {
                final WebSocketMessage<String> webSocketMessage = toWebSocketMessage(exampleMessage);
                LOGGER.info("Broadcasting message to session [{}] with body [{}]",
                        session.getId(), webSocketMessage.getPayload());
                session.sendMessage(webSocketMessage);
            } catch (Exception e) {
                LOGGER.error("Unable to broadcast message to session " + session.getId(), e);
            }
        });
    }

}
