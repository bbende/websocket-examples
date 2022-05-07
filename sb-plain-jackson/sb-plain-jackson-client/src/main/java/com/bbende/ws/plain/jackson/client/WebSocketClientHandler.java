package com.bbende.ws.plain.jackson.client;

import com.bbende.messaging.ws.plain.ExampleMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PreDestroy;
import java.net.URI;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class WebSocketClientHandler extends TextWebSocketHandler implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketClientHandler.class);

    private final URI webSocketUrl;
    private final String clientId;
    private final WebSocketClient webSocketClient;
    private final ObjectMapper objectMapper;
    private final ScheduledExecutorService executorService;
    private AtomicInteger messageCounter = new AtomicInteger(0);

    public WebSocketClientHandler(final WebSocketClientProperties clientProperties,
                                  final WebSocketClient webSocketClient,
                                  final ObjectMapper objectMapper) {
        this.webSocketClient = webSocketClient;
        this.objectMapper = objectMapper;
        this.clientId = clientProperties.getId();
        this.webSocketUrl = URI.create(clientProperties.getUrl());
        this.executorService = new ScheduledThreadPoolExecutor(1);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        LOGGER.info("Connection established for session [{}]", session.getId());
        scheduleMessageSendingTask(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        final ExampleMessage exampleMessage = fromWebSocketMessage(message);
        LOGGER.info("Received message [{}] from client [{}]",
                exampleMessage.getMessage(), exampleMessage.getClientId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        LOGGER.info("Connection closed for session [{}]", session.getId());
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        LOGGER.info("Connecting to WebSocket server at [{}]", webSocketUrl.toString());
        webSocketClient.doHandshake(this, null, webSocketUrl);
    }

    @PreDestroy
    public void preDestroy() {
        executorService.shutdown();
    }

    private WebSocketMessage<String> toWebSocketMessage(final ExampleMessage exampleMessage) throws JsonProcessingException {
        final String exampleMessageJson = objectMapper.writeValueAsString(exampleMessage);
        return new TextMessage(exampleMessageJson);
    }

    private ExampleMessage fromWebSocketMessage(final WebSocketMessage<String> webSocketMessage) throws JsonProcessingException {
        return objectMapper.readValue(webSocketMessage.getPayload(), ExampleMessage.class);
    }

    /**
     * Sends a message over the session every 5 seconds including a one-up message count.
     */
    private void scheduleMessageSendingTask(final WebSocketSession session) {
        executorService.scheduleAtFixedRate(() -> {
            try {
                final ExampleMessage exampleMessage = new ExampleMessage();
                exampleMessage.setClientId(clientId);
                exampleMessage.setMessage("Message # " + messageCounter.getAndIncrement());

                final WebSocketMessage<String> message = toWebSocketMessage(exampleMessage);
                LOGGER.info("Sending message [{}]", message.getPayload());
                session.sendMessage(message);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }, 5, 5, TimeUnit.SECONDS);
    }

}
