package com.brt.order.dto;

import lombok.Data;

@Data
public class CutPltRequest {

    private NestOssInfo ossInfo;

    private String pltFileId;

    private Integer position;

    private String callbackUrl;

    private String requestId;
}
