package com.brt.common.utils;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @BelongsProject: miaopai-mall
 * @BelongsPackage: com.brt.common.utils
 * @Author: FanGN
 * @CreateTime: 2023/7/12 00:08
 * @Description: TODO
 * @Version: 1.0
 */
public class ApiUserUtils {

    public static String getUserId(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return ObjectUtil.toString(request.getAttribute(JwtUtils.USER_ATTR_KEY));
    }

    public static String getUserTradingStatus(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return ObjectUtil.toString(request.getAttribute(JwtUtils.USER_TRADING_ATTR_KEY));
    }
}
