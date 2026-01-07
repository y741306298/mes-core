package com.brt.productionflow.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 排版生产流关联
 */
@Data
@Accessors(chain = true)
@TableName("pf_compose_flow_rel")
public class ComposeFlowRel {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("flow_id")
    private String flowId;

    @TableField("compose_id")
    private String composeId;

    @TableField("quantity")
    private Integer quantity;

    @TableField("status")
    private String status;
}
