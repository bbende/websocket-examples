package com.bbende.ws.stomp.server;

/**
 * Wrapper class to broadcast a STOMP message over a Redis channel.
 *
 * The consumer will publish the message to the given STOMP destination, which in turn will
 * send the message to any clients connected to the simpler broker on that server.
 */
public class StompMessage {

    private String destination;
    private Object message;

    public StompMessage() {
    }

    public StompMessage(String destination, Object message) {
        this.destination = destination;
        this.message = message;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "RedisStompMessage{" +
                "destination='" + destination + '\'' +
                ", message=" + message +
                '}';
    }
}
