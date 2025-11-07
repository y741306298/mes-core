package com.brt.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HttpMethodEnums {
    POST("POST"),
    GET("GET");

    private String method;
}
