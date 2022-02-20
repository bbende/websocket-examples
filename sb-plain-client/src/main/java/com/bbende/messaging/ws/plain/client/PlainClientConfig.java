package com.bbende.messaging.ws.plain.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Configuration
public class PlainClientConfig {

    @Bean
    public WebSocketClient getWebSocketClient() {
        return new StandardWebSocketClient();
    }

}
