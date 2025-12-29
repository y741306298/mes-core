package com.brt.order.dto;

import java.util.List;
import lombok.Data;

@Data
public class PolygonNestRequest {

    private String requestId;

    private List<PolygonNestMaterial> materials;

    private List<PolygonNestSvg> svgList;

    private Integer timeoutSeconds;

    private String callbackUrl;
}
