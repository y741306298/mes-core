package com.brt.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderAuditStatus {
    待审核("0"),
    审核中("1"),
    已完成("2"),
    退回("3"),
    归档("5");

    private String code;

}
