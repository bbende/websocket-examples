package com.bbende.messaging.ws.plain.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * Bind 'ws.client' properties from application.properties.
 */
@ConstructorBinding
@ConfigurationProperties("ws.client")
public class PlainClientJacksonProperties {

    private final String url;
    private final String id;

    public PlainClientJacksonProperties(final String url, final String id) {
        this.url = url;
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }
}
