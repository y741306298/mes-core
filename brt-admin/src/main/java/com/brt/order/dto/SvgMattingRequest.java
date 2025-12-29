package com.brt.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SvgMattingRequest {

    @JsonProperty("img_file_name")
    private String imgFileName;

    @JsonProperty("svg_file_name")
    private String svgFileName;

    @JsonProperty("oss_code")
    private String ossCode;

    @JsonProperty("result_dir")
    private String resultDir;

    @JsonProperty("callback_url")
    private String callbackUrl;

    @JsonProperty("is_split")
    private Boolean split;

    @JsonProperty("record_id")
    private String recordId;
}
