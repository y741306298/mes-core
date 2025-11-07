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
 * 订单动态对象 brt_order_dynamic
 *
 * @author Fgn
 * @date 2024-05-12
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtOrderDynamic extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String dynamicId;

    /**
     * 订单ID
     */
    @Excel(name = "订单ID")
    private String orderId;

    /**
     * 订单节点ID
     */
    @Excel(name = "订单节点ID")
    private String orderNodeId;

    /**
     * 节点ID
     */
    @Excel(name = "节点ID")
    private String nodeId;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private String userId;

    /**
     * 动态内容
     */
    @Excel(name = "动态内容")
    private String dynamicContent;

    /**
     * 附件
     */
    @Excel(name = "附件")
    private String attachments;

    /**
     * 备注
     */
    private String remark;

        /***************************自定义字段*****************************/

}
