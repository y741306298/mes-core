package com.brt.productionflow.vo;

import lombok.Data;

/**
 * 生产流查询条件
 */
@Data
public class ProductionFlowQuery {

    private String keyword;

    private String status;

    private String priority;

    private String templateId;
}
