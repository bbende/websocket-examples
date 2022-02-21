package com.bbende.messaging.ws.plain.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class PlainServerJacksonConfig implements WebSocketConfigurer {

    private final PlainServerJacksonHandler plainServerJacksonHandler;

    public PlainServerJacksonConfig(PlainServerJacksonHandler plainServerJacksonHandler) {
        this.plainServerJacksonHandler = plainServerJacksonHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(plainServerJacksonHandler, "/ws/events");
    }

}
