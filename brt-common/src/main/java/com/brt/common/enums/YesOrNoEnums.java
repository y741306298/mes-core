package com.brt.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum YesOrNoEnums {

    YES("Y"),
    NO("N");

    private String code;
}
