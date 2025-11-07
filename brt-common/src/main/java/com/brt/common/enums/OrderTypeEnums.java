package com.brt.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderTypeEnums {

    报价单("0","",""),
    销售单("1","brtSalesOrderService","orderCheck"),
    采购单("2","",""),
    EMPTY("","","");

    private String code;
    private String beanName;
    private String checkMethod;

    public static OrderTypeEnums getEnumsByCode(String code){
        for (OrderTypeEnums e: OrderTypeEnums.values()) {
            if (e.getCode().equals(code)){
                return e;
            }
        }
        return EMPTY;
    }
}
