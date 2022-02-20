package com.bbende.messaging.ws.stomp.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@org.springframework.boot.autoconfigure.SpringBootApplication
@EnableConfigurationProperties({StompClientProperties.class})
public class StompClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(StompClientApplication.class, args);
    }

}
