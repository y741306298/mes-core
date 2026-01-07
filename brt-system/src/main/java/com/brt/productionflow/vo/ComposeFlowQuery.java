package com.brt.productionflow.vo;

import lombok.Data;

/**
 * 排版生产流查询条件
 */
@Data
public class ComposeFlowQuery {

    private String keyword;

    private String status;

    private String priority;

    private String templateId;
}
