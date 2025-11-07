package com.brt.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CollectionStatusEnums {

    未收款("0"),
    部分收款("1"),
    已收款("2");

    private String code;
}
