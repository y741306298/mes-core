package com.brt.order.utils;

import com.brt.common.enums.OrderNoEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BrtOrderNoUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;
    /**
     * 当前年月日
     * @return
     */
    public String getTimeStr(){
        LocalDateTime localDateTime = LocalDateTime.now();
        int year = localDateTime.getYear();
        String monthValue = localDateTime.getMonthValue()<10?"0"+String.valueOf(localDateTime.getMonthValue()):String.valueOf(localDateTime.getMonthValue());
        String dayOfMonth = localDateTime.getDayOfMonth()<10?"0"+String.valueOf(localDateTime.getDayOfMonth()):String.valueOf(localDateTime.getDayOfMonth());

        return String.valueOf(year)+monthValue+dayOfMonth;

    }

    /**
     * 获取项目编号但不会加一
     * @return
     */
    public String getNo(OrderNoEnums orderNoEnums){
        String projectNoKey = getNoKey(orderNoEnums);
        String val = redisTemplate.opsForValue().get(orderNoEnums.getKeyName()+projectNoKey);
        String no = "";
        for(int i = 0 ; i < 4-val.length(); i++){
            no+=0;
        }
        return orderNoEnums.getPrefix()+
                projectNoKey+no+val;
    }

    /**
     * 获取项目编号并加一
     * @return
     */
    public String getNoAndAdd(OrderNoEnums orderNoEnums){
        String projectNoKey = getNoKey(orderNoEnums);
        String val = redisTemplate.opsForValue().get(orderNoEnums.getKeyName()+projectNoKey);
        String no = "";
        for(int i = 0 ; i < 4-val.length(); i++){
            no+=0;
        }
        int noNum = Integer.valueOf(val)+1;
        redisTemplate.opsForValue().set(orderNoEnums.getKeyName()+projectNoKey,""+noNum);
        return orderNoEnums.getPrefix()+projectNoKey+no+val;
    }


    /**
     * 更新redis中项目编号的key
     * @return
     */
    private String getNoKey(OrderNoEnums orderNoEnums){
        String timeStr = getTimeStr();
        String noKey = orderNoEnums.getKeyName()+"NoKey";
        String noCounterKey = orderNoEnums.getKeyName()+timeStr;
        if(!redisTemplate.hasKey(noKey)){
            redisTemplate.opsForValue().set(noKey,noCounterKey);
            redisTemplate.opsForValue().set(noCounterKey,"1");
        }
        String projectNoKey = redisTemplate.opsForValue().get(noKey);
        if(!projectNoKey.equals(noCounterKey)){
            redisTemplate.delete(projectNoKey);
            redisTemplate.opsForValue().set(noCounterKey,"1");
            redisTemplate.opsForValue().set(noKey,noCounterKey);
        }
        return timeStr;
    }
}
