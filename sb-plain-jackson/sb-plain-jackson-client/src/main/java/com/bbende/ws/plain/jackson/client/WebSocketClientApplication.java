package com.bbende.ws.plain.jackson.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@org.springframework.boot.autoconfigure.SpringBootApplication
@EnableConfigurationProperties({WebSocketClientProperties.class})
public class WebSocketClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebSocketClientApplication.class, args);
    }

}
