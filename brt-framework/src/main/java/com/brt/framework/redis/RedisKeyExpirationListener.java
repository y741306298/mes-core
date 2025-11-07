package com.brt.framework.redis;

import com.brt.common.constant.CacheConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * @BelongsProject: miaopai-mall
 * @BelongsPackage: com.brt.framework.redis
 * @Author: FanGN
 * @CreateTime: 2023/7/20 09:42
 * @Description: TODO
 * @Version: 1.0
 */
@Slf4j
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = message.toString();
        log.info("过期的KEY===>>{} " , key);

        String[] keySplit = key.split(":");
        String data = keySplit[keySplit.length - 1];

    }
}
