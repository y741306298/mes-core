package com.brt.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CheckTypeEnums {

    支出("0"),
    收入("1")
    ;

    private String code;
}
