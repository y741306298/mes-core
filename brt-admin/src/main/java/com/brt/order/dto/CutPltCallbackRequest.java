package com.brt.order.dto;

import lombok.Data;

@Data
public class CutPltCallbackRequest {

    private String responseCode;

    private String responseMsg;

    private CutPltCallbackData data;

    private Boolean success;

    private String requestId;
}
