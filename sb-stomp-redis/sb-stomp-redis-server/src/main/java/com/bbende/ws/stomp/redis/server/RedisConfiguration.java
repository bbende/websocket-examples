package com.bbende.ws.stomp.redis.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

    public static final String STOMP_PUBSUB_CHANNEL = "stomp-messages";

    @Bean
    public ChannelTopic stompChannel() {
        return new ChannelTopic(STOMP_PUBSUB_CHANNEL);
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(final StompMessageConsumer messageConsumer) {
        // This Jackson serializer needs to be bound to the specific class the message consumer is handling,
        // otherwise the deserializing will produce a LinkedHashMap and won't line up with the consumer
        final Jackson2JsonRedisSerializer<StompMessage> jacksonSerializer =
                new Jackson2JsonRedisSerializer<>(StompMessage.class);

        final MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(messageConsumer);
        messageListenerAdapter.setSerializer(jacksonSerializer);
        return messageListenerAdapter;
    }

    @Bean
    public RedisMessageListenerContainer listenerContainer(final ChannelTopic stompChannel,
                                                           final MessageListenerAdapter listenerAdapter,
                                                           final RedisConnectionFactory connectionFactory) {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, stompChannel);
        return container;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(final RedisConnectionFactory connectionFactory) {
        final StringRedisSerializer stringSerializer = new StringRedisSerializer();
        final Jackson2JsonRedisSerializer<Object> jacksonSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

        final RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(jacksonSerializer);
        return redisTemplate;
    }

}
