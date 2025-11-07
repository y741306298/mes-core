package com.brt.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户销售记录
 */
@Data
public class MarketRecordVo {
    /**
     *客户ID
     */
    private String customerId;
    /**
     * 销售单号
     */
    private String orderNo;
    /**
     * 销售时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date orderTime;
    /**
     * 送货状态
     */
    private String deliveryStatus;
    /**
     * 已送货
     */
    private String deliveryNum;
    /**
     * 合同金额
     */
    private BigDecimal totalAmount;
    /**
     * 已收金额
     */
    private BigDecimal collectionAmount ;
    /**
     * 未收金额
     */
    private BigDecimal unCollectionAmount;
    /**
     * 收款状态
     */
    private String priceStatus;

    private int pageNum;

    private int pageSize;
}
