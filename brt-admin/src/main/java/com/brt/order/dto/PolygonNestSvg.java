package com.brt.order.dto;

import lombok.Data;

@Data
public class PolygonNestSvg {

    private NestOssInfo ossInfo;

    private String fileId;

    private String preResultCode;

    private Integer widthMM;

    private Integer heightMM;

    private Integer nestMethod;

    private String extendInfo;
}
