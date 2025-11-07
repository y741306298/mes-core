package com.brt.order.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 订单审批对象 brt_order_examine
 *
 * @author Fgn
 * @date 2024-05-10
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtOrderExamine extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String examineId;

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
     * 订单模板ID
     */
    @Excel(name = "订单模板ID")
    private String orderTemplateId;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "订单日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date orderDate;

    /**
     * 订单备注
     */
    @Excel(name = "订单备注")
    private String orderRemark;

    /**
     * 审核状态(0=待审核,1=审核通过,2=审核拒绝)
     */
    @Excel(name = "审核状态(0=待审核,1=审核通过,2=审核拒绝)")
    private String auditStatus;

    /**
     * 审核用户
     */
    @Excel(name = "审核用户")
    private String auditUserId;

    /**
     * 审核备注
     */
    @Excel(name = "审核备注")
    private String auditRemark;


        /***************************自定义字段*****************************/

}
