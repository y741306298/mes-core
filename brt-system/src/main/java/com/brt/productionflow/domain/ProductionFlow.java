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
 * 生产流主表
 */
@Data
@Accessors(chain = true)
@TableName("pf_production_flow")
public class ProductionFlow {

    @TableId(value = "flow_id", type = IdType.INPUT)
    private String flowId;

    @TableField("flow_status")
    private String flowStatus;

    @TableField("total_quantity")
    private Integer totalQuantity;

    @TableField("priority")
    private String priority;

    @TableField("template_id")
    private String templateId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField("scheduled_start")
    private LocalDateTime scheduledStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField("scheduled_end")
    private LocalDateTime scheduledEnd;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField("actual_start")
    private LocalDateTime actualStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField("actual_end")
    private LocalDateTime actualEnd;

    @TableField("assigned_operator")
    private String assignedOperator;

    @TableField("production_notes")
    private String productionNotes;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField("created_at")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
