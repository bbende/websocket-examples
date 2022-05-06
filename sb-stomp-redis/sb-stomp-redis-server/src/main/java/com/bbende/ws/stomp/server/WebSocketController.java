package com.bbende.ws.stomp.server;

import com.bbende.ws.stomp.api.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final StompMessagePublisher publisher;

    public WebSocketController(final StompMessagePublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * Receives messages sent to /app/messages and publishes them to a Redis channel with the
     * destination set to /topic/messages. The server that consumes from the Redis channel will
     * publish to the simple broker using the specified destination.
     *
     * @param message the message received
     */
    @MessageMapping("/messages")
    public void handleMessage(final Message message) {
        final StompMessage stompMessage = new StompMessage();
        stompMessage.setDestination("/topic/messages");
        stompMessage.setMessage(message);
        publisher.publish(stompMessage);
    }

}
