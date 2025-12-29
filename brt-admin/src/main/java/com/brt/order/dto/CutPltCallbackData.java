package com.brt.order.dto;

import lombok.Data;

@Data
public class CutPltCallbackData {

    private NestOssInfo ossInfo;

    private String pltFileId;
}
