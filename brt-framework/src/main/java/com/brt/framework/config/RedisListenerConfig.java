package com.brt.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @BelongsProject: miaopai-mall
 * @BelongsPackage: com.brt.framework.config
 * @Author: FanGN
 * @CreateTime: 2023/7/20 09:40
 * @Description: TODO
 * @Version: 1.0
 */
@Configuration
public class RedisListenerConfig {

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }
}
