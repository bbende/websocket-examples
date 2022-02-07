package com.bbende.messaging.ws.plain.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketPlainServerConfig implements WebSocketConfigurer {

    private final WebSocketPlainServerHandler webSocketPlainServerHandler;

    public WebSocketPlainServerConfig(WebSocketPlainServerHandler webSocketPlainServerHandler) {
        this.webSocketPlainServerHandler = webSocketPlainServerHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketPlainServerHandler, "/ws/events");
    }

}
