package com.brt.common.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BrtDataFiltration {

    //权限标识
    public String [] perms() default {};

    //字段名称
    public String field() default "";

    //不过滤的条件
    public String [] noParams() default {};
}
