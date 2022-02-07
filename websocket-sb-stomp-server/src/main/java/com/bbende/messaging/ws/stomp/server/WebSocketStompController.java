package com.bbende.messaging.ws.stomp.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketStompController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketStompController.class);

    /**
     * Receives messages sent to /app/messages, response is sent to /topic/messages.
     *
     * @param message the message received
     * @return the message to send to broker channel
     */
    @MessageMapping("/messages")
    public String handleMessage(final String message) {
        LOGGER.info("Server received message [{}]", message);
        return "Server received message: [" + message + "]";
    }

}
