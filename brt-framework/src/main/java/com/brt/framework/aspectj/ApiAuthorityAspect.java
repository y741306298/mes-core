package com.brt.framework.aspectj;

import cn.hutool.core.util.ObjectUtil;
import com.brt.common.annotation.ApiAuthorityCheck;
import com.brt.common.constant.CacheConstants;
import com.brt.common.constant.HttpStatus;
import com.brt.common.core.redis.RedisCache;
import com.brt.common.exception.api.ApiAppUserException;
import com.brt.common.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @BelongsProject: wedding-backstage
 * @BelongsPackage: com.ruoyi.framework.aspectj
 * @Author: FanGN
 * @CreateTime: 2023-03-22  23:02
 * @Description: TODO
 * @Version: 1.0
 */
@Aspect
@Component
@Slf4j
public class ApiAuthorityAspect {

    @Autowired
    private RedisCache redisCache;

    @Pointcut(value = "@annotation(com.brt.common.annotation.ApiAuthorityCheck)")
    public void annotationPointCut() {
    }

    @Before("annotationPointCut()")
    public void checkToken(JoinPoint joinPoint) {
        //获取切入点
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        ApiAuthorityCheck authorityCheck = null;
        if (method!=null){
            authorityCheck = method.getAnnotation(ApiAuthorityCheck.class);
        }

        //从请求头中获取登录凭证
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("token");
        String loginId = request.getHeader("loginId");

        //判断该接口是否一定需要token才能访问
        if (authorityCheck.isToken()||StringUtils.isNotEmpty(token)){

            try {
                JwtUtils.verify(token);
            } catch (Exception e) {
                log.info("token 解析失败");
                throw new ApiAppUserException(HttpStatus.UNAUTHORIZED, "Token验证失败！");
            }

            //通过token 解析当前登录用户的UserId
            String userId = JwtUtils.getClainByName(token, JwtUtils.CLAIMNAME).asString();

            //从缓存中取出用户登录信息
            Map<String,Object> userLoginInfo = redisCache.getCacheObject(CacheConstants.USER_TOKEN + userId);
            if (ObjectUtil.isEmpty(userLoginInfo)){
                throw new ApiAppUserException(HttpStatus.UNAUTHORIZED, "Token验证失败！");
            }
            String redisToken = userLoginInfo.get("token").toString();
            String redisLoginId = userLoginInfo.get("loginId").toString();

            if (StringUtils.isEmpty(redisToken) || !token.equals(redisToken)) {
                log.info("token 验证失败");
                throw new ApiAppUserException(HttpStatus.UNAUTHORIZED, "Token验证失败！");
            }

            //校验是否被其它设备登录
            if (StringUtils.isEmpty(redisLoginId) || !redisLoginId.equals(loginId)){
                log.error("当前账号已在其它设备登录");
                throw new ApiAppUserException(HttpStatus.DROP_THE_LINE, "当前账号已在其它设备登录！");
            }

        }
    }

}
