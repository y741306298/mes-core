package com.brt.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 供应商采购记录
 */
@Data
public class PurchaseRecordVo {
    /**
     *供应商ID
     */
    private String supplierId;
    /**
     * 采购单号
     */
    private String orderNo;
    /**
     * 采购时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date orderTime;
    /**
     * 收货状态
     */
    private String receivingStatus;
    /**
     * 收货数量
     */
    private String receivingNum;
    /**
     * 合同金额
     */
    private BigDecimal totalAmount;
    /**
     * 已付金额
     */
    private BigDecimal collectionAmount ;
    /**
     * 未付金额
     */
    private BigDecimal unCollectionAmount;
    /**
     * 付款状态
     */
    private String priceStatus;

    private int pageNum;

    private int pageSize;
}
