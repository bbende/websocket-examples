package com.bbende.ws.stomp.api;

/**
 * The payload of a STOMP message sent between a client and server.
 */
public class Message {

    private String clientId;

    private String message;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "clientId='" + clientId + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
