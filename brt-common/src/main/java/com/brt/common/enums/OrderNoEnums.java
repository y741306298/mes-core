package com.brt.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderNoEnums {
    销售单("XiaoShouDan","XS"),
    采购单("CaiGouDan","CG"),
    报价单("BaoJiaDan","BJ"),
    出库单("ChuKuDan","CK"),
    入库单("RuKuDan","RK"),
    打样单("DaYangDan","DY"),
    客户("Customer_","KH"),
    供应商("Supplier_","GYS"),
    单号测试("Text","CS"),
    单号测试1("Text_1","CS");


    private String keyName;
    private String prefix;

    public static OrderNoEnums  getNoEnumsByKeyName(String keyName){
        for(OrderNoEnums orderNoEnums:OrderNoEnums.values()){
            if(keyName.equals(orderNoEnums.getKeyName())){
                return orderNoEnums;
            }
        }
        throw new RuntimeException("当前单号key："+keyName+" 不在生成范围,请在 OrderNoEnums 中添加");
    }
}
