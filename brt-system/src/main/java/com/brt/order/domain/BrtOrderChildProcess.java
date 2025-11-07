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
 * 订单子流程对象 brt_order_child_process
 *
 * @author Fgn
 * @date 2024-06-21
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtOrderChildProcess extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String childId;

    /**
     * 订单编号
     */
    @Excel(name = "订单编号")
    private String childNo;

    /**
     * 父节点ID
     */
    @Excel(name = "父节点ID")
    private String parentChildId;

    /**
     * 模板ID
     */
    @Excel(name = "模板ID")
    private String templateId;

    /**
     * 子流程名称
     */
    @Excel(name = "子流程名称")
    private String childName;

    /**
     * 订单ID
     */
    @Excel(name = "订单ID")
    private String orderId;

    /**
     * 子流程状态(0=待审核,1=待完成,2=已完成)
     */
    @Excel(name = "子流程状态(0=待审核,1=待完成,2=已完成)")
    private String childStatus;

    /**
     * 流程节点ID
     */
    @Excel(name = "流程节点ID")
    private String nodeId;

    /**
     * 订单流程节点ID
     */
    @Excel(name = "订单流程节点ID")
    private String orderNodeId;

    /**
     * 子流程节点数量
     */
    @Excel(name = "子流程节点数量")
    private Long nodeNum;

    /**
     * 已完成节点数量
     */
    @Excel(name = "已完成节点数量")
    private Long complateNum;


        /***************************自定义字段*****************************/

}
