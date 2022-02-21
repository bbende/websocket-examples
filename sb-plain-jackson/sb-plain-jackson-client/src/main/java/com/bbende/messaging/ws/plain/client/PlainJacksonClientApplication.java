package com.bbende.messaging.ws.plain.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@org.springframework.boot.autoconfigure.SpringBootApplication
@EnableConfigurationProperties({PlainJacksonClientProperties.class})
public class PlainJacksonClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlainJacksonClientApplication.class, args);
    }

}
