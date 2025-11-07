package com.brt.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CraftTypeEnums {

    标准品("0"),
    非标准品("1");

    private String code;
}
