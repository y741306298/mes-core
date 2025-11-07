package com.brt.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ChildStatusEnums {

    待审核("0"),
    待完成("1"),
    已完成("2");

    private String code;
}
