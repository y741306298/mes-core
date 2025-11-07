package com.brt.productionflow.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 生产流材料汇总
 */
@Data
@Accessors(chain = true)
@TableName("pf_production_flow_material")
public class ProductionFlowMaterial {

    @TableId(value = "material_id", type = IdType.AUTO)
    private Long materialId;

    @TableField("flow_id")
    private String flowId;

    @TableField("material")
    private String material;

    @TableField("quantity")
    private Integer quantity;

    @TableField("sort_order")
    private Integer sortOrder;
}
