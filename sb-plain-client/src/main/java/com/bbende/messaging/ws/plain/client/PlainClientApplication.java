package com.bbende.messaging.ws.plain.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@org.springframework.boot.autoconfigure.SpringBootApplication
@EnableConfigurationProperties({PlainClientProperties.class})
public class PlainClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlainClientApplication.class, args);
    }

}
