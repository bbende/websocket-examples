package com.bbende.messaging.ws.plain.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@org.springframework.boot.autoconfigure.SpringBootApplication
@EnableConfigurationProperties({PlainClientJacksonProperties.class})
public class PlainClientJacksonApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlainClientJacksonApplication.class, args);
    }

}
