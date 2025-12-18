package com.brt.order.domain;

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
 * 订单流程节点对象 brt_order_node
 *
 * @author Fgn
 * @date 2024-05-10
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtOrderNode extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String orderNodeId;

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
     * 订单ID
     */
    @Excel(name = "订单ID")
    private String orderId;

    /**
     * 模板ID
     */
    @Excel(name = "模板ID")
    private String templateId;

    /**
     * 节点ID
     */
    @Excel(name = "节点ID")
    private String nodeId;

    /**
     * 部门ID
     */
    @Excel(name = "部门ID")
    private String deptId;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private String userId;

    /**
     * 流程负责人
     */
    private String principal;

    /**
     * 节点负责人
     */
    private String nodePrincipal;

    /**
     * 完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date complateDate;

    /**
     * 节点状态(0=未开始,1=进行中,2=已完成,3=已超时)
     */
    @Excel(name = "节点状态(0=未开始,1=进行中,2=已完成,3=已超时)")
    private String nodeStatus;

    /**
     * 节点备注
     */
    @Excel(name = "节点备注")
    private String nodeRemark;

    /**
     * 排序
     */
    @Excel(name = "排序")
    private Long sort;

    /**
     * 操作设置(0=生成收货单,1=生成送货单,2=减库存,3=加库存)
     */
    @Excel(name = "操作设置(0=生成收货单,1=生成送货单,2=减库存,3=加库存)")
    private String operSetting;

    /**
     * 触发方式(AUTO=自动触发,MANUAL=人工触发)
     */
    private String triggerMode;

    /**
     * 接口类型(SYNC/ASYNC)
     */
    @Excel(name = "接口类型")
    private String interfaceType;

    /**
     * 回调URL
     */
    @Excel(name = "回调URL")
    private String callbackUrl;

    /**
     * 创建人ID
     */
    private String createId;

    /**
     * 是否超时
     */
    private String timeout;

        /***************************自定义字段*****************************/

}
