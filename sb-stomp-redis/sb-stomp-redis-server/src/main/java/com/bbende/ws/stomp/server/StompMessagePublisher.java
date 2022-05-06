package com.bbende.ws.stomp.server;

/**
 * Publishes STOMP messages.
 */
public interface StompMessagePublisher {

    void publish(StompMessage stompMessage);

}
