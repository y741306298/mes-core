package com.brt.order.dto;

import java.util.List;
import lombok.Data;

@Data
public class PolygonNestCallbackRequest {

    private String responseCode;

    private String responseMsg;

    private List<PolygonNestCallbackData> data;

    private Boolean success;
}
