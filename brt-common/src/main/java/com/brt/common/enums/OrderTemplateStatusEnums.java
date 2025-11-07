package com.brt.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderTemplateStatusEnums {

    正常("0"),
    停用("1"),
    作废("2");

    private String code;
}
