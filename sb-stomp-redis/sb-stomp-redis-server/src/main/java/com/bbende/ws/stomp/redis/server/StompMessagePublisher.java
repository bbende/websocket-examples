package com.bbende.ws.stomp.redis.server;

/**
 * Publishes STOMP messages.
 */
public interface StompMessagePublisher {

    void publish(StompMessage stompMessage);

}
