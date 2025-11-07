package com.brt.framework.aspectj;

import com.brt.common.annotation.BrtDataFiltration;
import com.brt.common.annotation.DataScope;
import com.brt.common.core.domain.BaseEntity;
import com.brt.common.core.domain.entity.SysRole;
import com.brt.common.core.domain.entity.SysUser;
import com.brt.common.core.domain.model.LoginUser;
import com.brt.common.core.text.Convert;
import com.brt.common.utils.SecurityUtils;
import com.brt.common.utils.StringUtils;
import com.brt.framework.security.context.PermissionContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据过滤处理
 *
 * @author ruoyi
 */
@Aspect
@Component
public class BrtDataFiltrationAspect {

    /**
     * 数据权限过滤关键字
     */
    public static final String BRT_DATA_SCOPE = "brtDataScope";

    @Before("@annotation(serviceDataScope)")
    public void doBefore(JoinPoint point, BrtDataFiltration serviceDataScope) throws Throwable {
        clearDataScope(point);
        handleDataScope(point, serviceDataScope);
    }

    protected void handleDataScope(final JoinPoint joinPoint, BrtDataFiltration serviceDataScope) throws NoSuchFieldException, IllegalAccessException {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser)) {
            SysUser currentUser = loginUser.getUser();
            // 如果是超级管理员，则不过滤数据
            if (StringUtils.isNotNull(currentUser) && !currentUser.isAdmin()) {
                dataScopeFilter(joinPoint, currentUser, loginUser.getPermissions(), serviceDataScope.field(), serviceDataScope.perms(), serviceDataScope.noParams());
            }
        }
    }

    /**
     * 数据范围过滤
     *
     * @param joinPoint  切点
     * @param user       用户
     */
    public static void dataScopeFilter(JoinPoint joinPoint, SysUser user, Set<String> permissions, String field, String [] permissionArr,String [] noParams) throws IllegalAccessException {
        //获取方法参数
        Object params = joinPoint.getArgs()[0];
        Class<?> c = params.getClass();

        for (String param : noParams) {
            //去掉字符串中的空格
            String trimParam = param.replaceAll(" +","");

            String[] paramsArr = trimParam.split("=");
            String filedName = paramsArr[0];//参数名
            String val = paramsArr[1];//参数值

            Field filed = getField(c,filedName);
            filed.setAccessible(true);
            Object filedVal = filed.get(params);

            if (filedVal!=null&&val.equals(filedVal.toString())){
                return;
            }
        }



        for (String perm : permissionArr) {
            //判断用户是否包含某个权限
            boolean isFlag = permissions.contains(perm);
            if (!isFlag){
                if (StringUtils.isNotNull(params) && params instanceof BaseEntity) {
                    BaseEntity baseEntity = (BaseEntity) params;
                    baseEntity.getParams().put(BRT_DATA_SCOPE, " AND "+ field +" = "+user.getUserId());
                }
            }
        }
    }

    /**
     * 拼接权限sql前先清空params.dataScope参数防止注入
     */
    private void clearDataScope(final JoinPoint joinPoint) {
        Object params = joinPoint.getArgs()[0];
        if (StringUtils.isNotNull(params) && params instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) params;
            baseEntity.getParams().put(BRT_DATA_SCOPE, "");
        }
    }

    public static Field getField(Class<?> clazz, String filedName) {
        try {
            if(clazz != null) {
                Field declaredField = clazz.getDeclaredField(filedName);
                return declaredField;
            }
        } catch (NoSuchFieldException e) {
            return getField(clazz.getSuperclass(), filedName);
        }
        return null;
    }
}
