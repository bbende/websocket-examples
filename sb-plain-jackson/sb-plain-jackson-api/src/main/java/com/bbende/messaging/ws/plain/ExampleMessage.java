package com.bbende.messaging.ws.plain;

public class ExampleMessage {

    private String clientId;

    private String message;

    public ExampleMessage() {

    }

    public ExampleMessage(String clientId, String message) {
        this.clientId = clientId;
        this.message = message;
    }

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
}
