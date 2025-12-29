package com.brt.order.dto;

import lombok.Data;

@Data
public class AppendTemplateRequest {

    private String requestId;

    private String svgCode;

    private String templateCode;

    private String callbackUrl;
}
