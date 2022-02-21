package com.bbende.messaging.ws.plain.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class PlainServerConfig implements WebSocketConfigurer {

    private final PlainServerHandler plainServerHandler;

    public PlainServerConfig(PlainServerHandler plainServerHandler) {
        this.plainServerHandler = plainServerHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(plainServerHandler, "/ws/events");
    }

}
