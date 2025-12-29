package com.brt.order.dto;

import lombok.Data;

@Data
public class PolygonNestCallbackData {

    private String requestId;

    private String name;

    private String code;

    private String materialCode;

    private NestSvgResponse svgResponse;
}
