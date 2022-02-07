package com.bbende.messaging.ws.stomp.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
public class WebSocketStompClientHandler extends TextWebSocketHandler implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketStompClientHandler.class);

    private URI webSocketUrl;
    private WebSocketClient webSocketClient;
    private ScheduledExecutorService executorService;
    private AtomicInteger messageCounter = new AtomicInteger(0);

    public WebSocketStompClientHandler(@Value("${ws.client.url}") final String webSocketUrl, final WebSocketClient webSocketClient) {
        this.webSocketUrl = URI.create(webSocketUrl);
        this.webSocketClient = webSocketClient;
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
        LOGGER.info("Received message [{}]", message.getPayload());
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

    /**
     * Sends a message over the session every 5 seconds including a one-up message count.
     */
    private void scheduleMessageSendingTask(final WebSocketSession session) {
        executorService.scheduleAtFixedRate(() -> {
            try {
                final WebSocketMessage<String> message = new TextMessage("Message # " + messageCounter.getAndIncrement());
                LOGGER.info("Sending message [{}]", message.getPayload());
                session.sendMessage(message);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }, 5, 5, TimeUnit.SECONDS);
    }

}
