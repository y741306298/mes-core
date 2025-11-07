package com.brt.order.domain;

import java.math.BigDecimal;
import com.brt.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.brt.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 开票计划对象 brt_order_invoice_plan
 *
 * @author Fgn
 * @date 2024-06-19
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtOrderInvoicePlan extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String planId;

    /**
     * 节点ID
     */
    @Excel(name = "节点ID")
    private String nodeId;

    /**
     * 订单节点ID
     */
    @Excel(name = "订单节点ID")
    private String orderNodeId;

    /**
     * 订单ID
     */
    @Excel(name = "订单ID")
    private String orderId;

    /**
     * 订单编号
     */
    @Excel(name = "订单编号")
    private String orderNo;

    /**
     * 订单类型(0=报价单,1=销售单,2=采购单)
     */
    @Excel(name = "订单类型(0=报价单,1=销售单,2=采购单)")
    private String orderType;

    /**
     * 开票金额
     */
    @Excel(name = "开票金额")
    private BigDecimal invoiceAmount;

    /**
     * 计划开票金额
     */
    @Excel(name = "计划开票金额")
    private BigDecimal planAmount;

    /**
     * 订单金额
     */
    @Excel(name = "订单金额")
    private BigDecimal orderAmount;


        /***************************自定义字段*****************************/

}
