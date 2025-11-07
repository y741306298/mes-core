package com.brt.common.annotation;

import org.springframework.web.bind.annotation.ValueConstants;

import java.lang.annotation.*;

/**
 * @TODO: 自定义注解接收单个参数
 * @author: FanGN
 * @date: 2023/3/25 11:10
 **/
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestSingleParam {

    String value();

    boolean required() default true;

    String defaultValue() default ValueConstants.DEFAULT_NONE;
}
