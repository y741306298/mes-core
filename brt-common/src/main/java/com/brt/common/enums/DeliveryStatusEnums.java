package com.brt.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeliveryStatusEnums {

    未送货("0"),
    部分送货("1"),
    已送货("2");

    private String code;

}
