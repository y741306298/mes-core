package com.brt.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderTemplateTypeEnums {

    订单模板("0"),
    订单子流程模板("1");

    private String code;
}
