package com.brt.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SvgMattingResponse {

    private boolean success;

    private String message;

    private boolean data;

    public static SvgMattingResponse ok() {
        return new SvgMattingResponse(true, null, true);
    }

    public static SvgMattingResponse fail(String message) {
        return new SvgMattingResponse(false, message, false);
    }
}
