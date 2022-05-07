package com.bbende.ws.stomp.redis.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("ws.client")
public class WebSocketClientProperties {

    private final String url;
    private final String appDestination;
    private final String topicDestination;
    private final String id;

    public WebSocketClientProperties(final String url, final String appDestination,
                                     final String topicDestination, final String id) {
        this.url = url;
        this.appDestination = appDestination;
        this.topicDestination = topicDestination;
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public String getAppDestination() {
        return appDestination;
    }

    public String getTopicDestination() {
        return topicDestination;
    }

    public String getId() {
        return id;
    }
}
