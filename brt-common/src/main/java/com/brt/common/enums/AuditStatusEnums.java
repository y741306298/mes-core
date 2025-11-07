package com.brt.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AuditStatusEnums {

    待审核("0","待审核"),
    通过("2","审核通过"),
    拒绝("5","审核不通过"),
    EMPTY("","")

    ;

    private String code;
    private String name;

    public static AuditStatusEnums getEnumsByCode(String code){
        for (AuditStatusEnums enums: AuditStatusEnums.values()) {
            if (enums.getCode().equals(code)){
                return enums;
            }
        }
        return EMPTY;
    }
}
