package com.brt.order.dto;

import lombok.Data;

@Data
public class AppendTemplateCallbackRequest {

    private String responseCode;

    private String responseMsg;

    private AppendTemplateCallbackData data;

    private Boolean success;
}
