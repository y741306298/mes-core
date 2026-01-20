package com.brt.order.domain;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 销售订单包裹对象 brt_sales_order_packages
 *
 * @author Fgn
 * @date 2025-02-19
 */
@Data
@Accessors(chain = true)
@ToString
@EqualsAndHashCode
@TableName("brt_sales_order_packages")
public class BrtSalesOrderPackage {

    /**
     * 来源包裹ID
     */
    @TableId(type = IdType.INPUT)
    private Long id;

    /**
     * 销售订单ID
     */
    private String orderId;

    /**
     * 包裹名称
     */
    private String packageName;

    /**
     * 收货人
     */
    private String consignee;

    /**
     * 收货地址
     */
    private String address;

    /**
     * 收货电话
     */
    private String tel;

    /**
     * 关联明细流水号集合
     */
    @TableField("orderItem_sns")
    private String orderItemSns;

    /**
     * 明细数量集合
     */
    private String counts;

    /**
     * 地址ID
     */
    private String addrId;

    /**
     * 运费
     */
    private BigDecimal shippingFee;

    /**
     * 物流网点
     */
    private String deliveryNet;

    /**
     * 来源快照
     */
    private String snapshot;
}
