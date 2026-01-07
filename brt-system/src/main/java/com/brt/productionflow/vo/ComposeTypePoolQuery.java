package com.brt.productionflow.vo;

import lombok.Data;

/**
 * 排版池查询参数
 */
@Data
public class ComposeTypePoolQuery {

    private String keyword;

    private String status;

    private String priority;
}
