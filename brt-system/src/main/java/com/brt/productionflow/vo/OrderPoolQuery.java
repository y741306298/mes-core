package com.brt.productionflow.vo;

import lombok.Data;

/**
 * 订单池查询参数
 */
@Data
public class OrderPoolQuery {

    private String keyword;

    private String status;

    private String priority;
}
