package com.bbende.messaging.ws.stomp.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@org.springframework.boot.autoconfigure.SpringBootApplication
@EnableConfigurationProperties({WebSocketStompClientProperties.class})
public class WebSocketStompClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebSocketStompClientApplication.class, args);
    }

}
