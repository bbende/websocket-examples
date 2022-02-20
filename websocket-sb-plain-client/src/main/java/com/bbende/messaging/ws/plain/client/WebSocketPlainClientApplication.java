package com.bbende.messaging.ws.plain.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@org.springframework.boot.autoconfigure.SpringBootApplication
@EnableConfigurationProperties({WebSocketPlainClientProperties.class})
public class WebSocketPlainClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebSocketPlainClientApplication.class, args);
    }

}
