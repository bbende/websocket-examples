package com.bbende.ws.stomp.server.redis;

import com.bbende.ws.stomp.server.StompMessage;
import com.bbende.ws.stomp.server.StompMessagePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisStompMessagePublisher implements StompMessagePublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisStompMessagePublisher.class);

    private final ChannelTopic stompChannel;
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisStompMessagePublisher(final ChannelTopic stompChannel,
                                      final RedisTemplate<String, Object> redisTemplate) {
        this.stompChannel = stompChannel;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void publish(final StompMessage stompMessage) {
        LOGGER.info("Publishing [{}] to Redis channel [{}]", stompMessage, stompChannel.getTopic());
        redisTemplate.convertAndSend(stompChannel.getTopic(), stompMessage);
    }
}
