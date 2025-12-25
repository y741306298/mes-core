package com.brt.hub.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.brt.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 材料信息对象 brt_mat
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("brt_mat")
public class HubMat extends BaseEntity {

    /**
     * 材料编码
     */
    @TableId(value = "mat_code", type = IdType.INPUT)
    private String matCode;

    /**
     * 材料名称
     */
    @TableField("mat_name")
    private String matName;

    /**
     * 材料分类
     */
    @TableField("mat_category")
    private String matCategory;

    /**
     * 颜色
     */
    @TableField("mat_color")
    private String matColor;

    /**
     * 品牌
     */
    @TableField("mat_brand")
    private String matBrand;

    /**
     * 供应商
     */
    @TableField("mat_supplier")
    private String matSupplier;

    /**
     * 材料宽度
     */
    @TableField("mat_width")
    private String matWidth;

    /**
     * 材料长度
     */
    @TableField("mat_length")
    private String matLength;

    /**
     * 材料厚度
     */
    @TableField("mat_thickness")
    private String matThickness;

    /**
     * 包装名称
     */
    @TableField("package_name")
    private String packageName;

    /**
     * 度量单位（数字）
     */
    @TableField("measure_unit_int")
    private Integer measureUnitInt;

    /**
     * 度量单位（名称）
     */
    @TableField("measure_unit")
    private String measureUnit;

    /**
     * 单位重量
     */
    @TableField("unit_weight")
    private Float unitWeight;

    /**
     * 单价
     */
    @TableField("unit_price")
    private BigDecimal unitPrice;

    /**
     * 是否有效
     */
    @TableField("is_valid")
    private Boolean valid;

    /**
     * 备注
     */
    @TableField("comments")
    private String comments;
}
