package com.brt.order.domain;

import com.brt.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.brt.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单模板对象 brt_order_template
 *
 * @author Fgn
 * @date 2024-05-10
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtOrderTemplate extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String orderTemplateId;

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
     * 子流程ID
     */
    @Excel(name = "子流程ID")
    private String childId;

    /**
     * 客户ID
     */
    @Excel(name = "客户ID")
    private String customerId;

    /**
     * 客户ID
     */
    @Excel(name = "供应商ID")
    private String supplierId;

    /**
     * 模板ID
     */
    @Excel(name = "模板ID")
    private String templateId;

    /**
     * 订单模板状态(0=正常,1=停用,2=作废)
     */
    @Excel(name = "订单模板状态(0=正常,1=停用,2=作废)")
    private String orderTemplateStatus;

    /**
     * 审核状态(0=待审核,1=审核通过,2=审核拒绝)
     */
    @Excel(name = "审核状态(0=待审核,1=审核通过,2=审核拒绝)")
    private String auditStatus;

    /**
     * 订单描述
     */
    @Excel(name = "订单描述")
    private String detailsDesc;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private String userId;

    /**
     * 订单数量
     */
    @Excel(name = "订单数量")
    private Long orderNum;

    /**
     * 订单价格
     */
    @Excel(name = "订单价格")
    private BigDecimal orderAmount;

    /**
     * 订单日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "订单日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date orderDate;

    /**
     * 交货日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "交货日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deliveryDate;

    /**
     * 模板类型(0=订单模板,1=订单子流程模板)
     */
    @Excel(name = "模板类型(0=订单模板,1=订单子流程模板)")
    private String templateType;

    /**
     * 流程状态
     */
    private String status;


        /***************************自定义字段*****************************/

}
