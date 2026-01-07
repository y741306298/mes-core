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
 * 排版池主表
 */
@Data
@Accessors(chain = true)
@TableName("pf_compose_type_pool")
public class ComposeTypePool {

    @TableId(value = "compose_id", type = IdType.INPUT)
    private String composeId;

    @TableField("order_ids")
    private String orderIds;

    @TableField("quantity")
    private Integer quantity;

    @TableField("material")
    private String material;

    @TableField("craft_requirements")
    private String craftRequirements;

    @TableField("thumbnail")
    private String thumbnail;

    @TableField("remark")
    private String remark;

    @TableField("plt")
    private String plt;

    @TableField("source_image")
    private String sourceImage;

    @TableField("order_status")
    private String orderStatus;

    @TableField("template_id")
    private String templateId;

    @TableField("priority")
    private String priority;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField("created_at")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
