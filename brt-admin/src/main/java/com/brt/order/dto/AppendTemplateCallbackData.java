package com.brt.order.dto;

import lombok.Data;

@Data
public class AppendTemplateCallbackData {

    private String requestId;

    private MtJson mtJson;

    private String pltFileId;

    private NestOssInfo ossInfo;
}
