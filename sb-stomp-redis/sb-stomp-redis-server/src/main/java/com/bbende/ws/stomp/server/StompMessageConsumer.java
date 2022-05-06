package com.bbende.ws.stomp.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Consumers STOMP messages received from an external channel, such as a Redis pub/sub channel, and
 * publishes the messages through the simple messaging template which uses the simple STOMP broker.
 */
@Service
public class StompMessageConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(StompMessageConsumer.class);

    private final SimpMessagingTemplate messagingTemplate;

    public StompMessageConsumer(final SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void handleMessage(final StompMessage stompMessage) {
        final String stompDestination = stompMessage.getDestination();
        final Object message = stompMessage.getMessage();
        LOGGER.info("Consuming STOMP message [{}], publishing to destination [{}]", message, stompDestination);
        messagingTemplate.convertAndSend(stompDestination, message);
    }

}