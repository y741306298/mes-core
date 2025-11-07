package com.brt.common.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.brt.common.constant.CacheConstants;
import com.brt.common.core.redis.RedisCache;
import com.brt.common.utils.spring.SpringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: FanGN 
 * @date: 2023/3/22 16:38
 * @param: 
 * @return: 
 **/
public class JwtUtils {

    private static String  secret="MIID9jCCAt6gAwIBAgIUL";
    public static final String CLAIMNAME = "userId";

    public static final String USER_ATTR_KEY = "user";
    public static final String USER_TRADING_ATTR_KEY = "tradingStatus";

    /**
     * 生成Token
     * @param data
     * @return
     */
    public static String creactToken(Map<String,String> data,String userId,String loginId){
        JWTCreator.Builder builder = JWT.create();
        data.forEach((k,v)->{
            builder.withClaim(k,v);
        });
        RedisCache redisCache = SpringUtils.getBean(RedisCache.class);
        String token = builder.sign(Algorithm.HMAC256(secret));

        //缓存用户登录信息
        Map<String,Object> userLoginInfo = new HashMap<>();
        userLoginInfo.put("token",token);
        userLoginInfo.put("loginId",loginId);
        redisCache.setCacheObject(CacheConstants.USER_TOKEN + userId,userLoginInfo);
        return token;
    }

    public static  void verify(String token) throws Exception{
        JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
    }

    /**
     * 获取token 参数
     * @param token
     * @return
     */
    public static DecodedJWT getToken(String token){
        return JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
    }

    /**
     * 通过载荷名称获取载荷值
     *
     * @param token     令牌
     * @param claimName 载荷名称
     * @return 载荷值
     */
    public static Claim getClainByName(String token, String claimName) {
        return JWT.decode(token).getClaim(claimName);
    }

}
