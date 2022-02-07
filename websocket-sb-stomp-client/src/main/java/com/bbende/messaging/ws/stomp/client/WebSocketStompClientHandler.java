package com.bbende.messaging.ws.stomp.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.annotation.PreDestroy;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class WebSocketStompClientHandler implements StompSessionHandler, ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketStompClientHandler.class);

    private final URI webSocketUrl;
    private final String appDestination;
    private final String topicDestination;
    private final WebSocketStompClient stompClient;
    private final ScheduledExecutorService executorService;
    private final AtomicInteger messageCounter = new AtomicInteger(0);

    public WebSocketStompClientHandler(@Value("${ws.client.url}")
                                       final String webSocketUrl,
                                       @Value("${ws.client.app.destination}")
                                       final String appDestination,
                                       @Value("${ws.client.topic.destination}")
                                       final String topicDestination,
                                       final WebSocketStompClient stompClient) {
        this.webSocketUrl = URI.create(webSocketUrl);
        this.stompClient = stompClient;
        this.appDestination = appDestination;
        this.topicDestination = topicDestination;
        this.executorService = new ScheduledThreadPoolExecutor(1);
    }

    @Override
    public void afterConnected(final StompSession session, final StompHeaders connectedHeaders) {
        LOGGER.info("Connection established for session [{}]", session.getSessionId());
        session.subscribe(topicDestination, this);
        LOGGER.info("Subscribed to [{}]", topicDestination);
        scheduleMessageSendingTask(session);
    }

    @Override
    public void handleFrame(final StompHeaders headers, final Object payload) {
        final String message = payload.toString();
        final String destination = headers.getDestination();
        LOGGER.info("Received message from destination [{}] with payload [{}]", destination, message);
    }

    @Override
    public void handleException(final StompSession session, final StompCommand command,
                                final StompHeaders headers, final byte[] payload, final Throwable exception) {
        LOGGER.error("Exception: {}", exception.getMessage(), exception);
    }

    @Override
    public void handleTransportError(final StompSession session, final Throwable exception) {
        LOGGER.error("Transport Error: {}", exception.getMessage(), exception);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return String.class;
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        LOGGER.info("Connecting to WebSocket server at [{}]", webSocketUrl.toString());
        stompClient.connect(webSocketUrl.toString(), this);
    }

    @PreDestroy
    public void preDestroy() {
        executorService.shutdown();
    }

    /**
     * Sends a message over the session every 5 seconds including a one-up message count.
     */
    private void scheduleMessageSendingTask(final StompSession session) {
        executorService.scheduleAtFixedRate(() -> {
            try {
                final String message = "Message # " + messageCounter.getAndIncrement();
                LOGGER.info("Sending message [{}] to [{}]", message, appDestination);
                session.send(appDestination, message);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }, 5, 5, TimeUnit.SECONDS);
    }

}
