package com.brt.productionflow.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 生产流与订单关联
 */
@Data
@Accessors(chain = true)
@TableName("pf_production_flow_order")
public class ProductionFlowOrderRel {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("flow_id")
    private String flowId;

    @TableField("order_id")
    private String orderId;

    @TableField("quantity")
    private Integer quantity;
}
