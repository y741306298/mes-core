package com.brt.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReceivingStatusEnums {
    未收货("0"),
    部分收货("1"),
    已收货("2");

    private String code;
}
