package com.brt.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class SvgMattingCallbackRequest {

    @JsonProperty("img_file_name")
    private String imgFileName;

    private Boolean success;

    private List<SvgMattingResult> data;

    @JsonProperty("error_message")
    private String errorMessage;

    @JsonProperty("record_id")
    private String recordId;
}
