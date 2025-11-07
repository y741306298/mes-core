package com.brt.order.domain;

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
 * 物料数量计划对象 brt_order_materiel_plan
 *
 * @author Fgn
 * @date 2024-06-20
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtOrderMaterielPlan extends BaseEntity {

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
     * 订单详情ID
     */
    @Excel(name = "订单详情ID")
    private String orderDetailsId;

    /**
     * 项目编号
     */
    @Excel(name = "项目编号")
    private String orderDetailsNo;

    /**
     * 订单类型(0=报价单,1=销售单,2=采购单)
     */
    @Excel(name = "订单类型(0=报价单,1=销售单,2=采购单)")
    private String orderType;

    /**
     * 物料ID
     */
    @Excel(name = "物料ID")
    private String materielId;

    /**
     * 已操作数量
     */
    @Excel(name = "已操作数量")
    private Long materielNum;

    /**
     * 计划数量
     */
    @Excel(name = "计划数量")
    private Long planNum;

    /**
     * 订单数量
     */
    @Excel(name = "订单数量")
    private Long orderNum;

    /**
     * 物料类型 用于流程数量记录任务展示
     */
    private String materielType;


        /***************************自定义字段*****************************/

}
