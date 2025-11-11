package com.brt.productionflow.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 生产流步骤进度
 */
@Data
@Accessors(chain = true)
@TableName("pf_production_flow_step")
public class ProductionFlowStep {

    @TableId(value = "step_id", type = IdType.AUTO)
    private Long stepId;

    @TableField("flow_id")
    private String flowId;

    @TableField("node_id")
    private String nodeId;

    @TableField("step_name")
    private String stepName;

    @TableField("step_status")
    private String stepStatus;

    @TableField("remark")
    private String remark;

    @TableField("sort_order")
    private Integer sortOrder;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
