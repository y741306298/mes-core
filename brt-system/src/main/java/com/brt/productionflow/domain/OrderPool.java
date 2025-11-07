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
 * 订单池主表
 */
@Data
@Accessors(chain = true)
@TableName("pf_order_pool")
public class OrderPool {

    @TableId(value = "order_id", type = IdType.INPUT)
    private String orderId;

    @TableField("preview_image")
    private String previewImage;

    @TableField("quantity")
    private Integer quantity;

    @TableField("remark")
    private String remark;

    @TableField("main_material")
    private String mainMaterial;

    @TableField("craft_requirements")
    private String craftRequirements;

    @TableField("order_status")
    private String orderStatus;

    @TableField("template_id")
    private String templateId;

    @TableField("customer_info")
    private String customerInfo;

    @TableField("priority")
    private String priority;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField("delivery_date")
    private LocalDateTime deliveryDate;

    @TableField("size_requirement")
    private String sizeRequirement;

    @TableField("color_requirement")
    private String colorRequirement;

    @TableField("file_format")
    private String fileFormat;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField("created_at")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
