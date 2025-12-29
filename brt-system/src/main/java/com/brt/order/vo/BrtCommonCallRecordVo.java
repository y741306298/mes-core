package com.brt.order.vo;

import lombok.Data;

@Data
public class BrtCommonCallRecordVo {

    private String recordId;

    private String interfaceName;

    private String requestPath;

    private String callbackUrl;

    private String requestPayload;

    private String callbackPayload;

    private String status;

    private String errorMessage;
}

