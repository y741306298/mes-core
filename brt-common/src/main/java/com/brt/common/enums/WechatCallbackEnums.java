package com.brt.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WechatCallbackEnums {

    余额充值("","",""),
    EMPTY("","","");

    private String beanName;
    private String payUrl;
    private String refundUrl;
}
