package com.brt.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NodeStatusEnums {

    待审核("-1"),
    未开始("0"),
    进行中("1"),
    已完成("2"),
    已超时("3");

    private String code;
}
