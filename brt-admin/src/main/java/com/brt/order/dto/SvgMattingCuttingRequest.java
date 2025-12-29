package com.brt.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SvgMattingCuttingRequest extends SvgMattingRequest {

    @JsonProperty("start_x")
    private Integer startX;

    @JsonProperty("start_y")
    private Integer startY;

    @JsonProperty("end_x")
    private Integer endX;

    @JsonProperty("end_y")
    private Integer endY;
}
