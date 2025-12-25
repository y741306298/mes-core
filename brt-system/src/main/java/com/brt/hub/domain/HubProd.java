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
 * 产品信息对象 brt_prod
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("brt_prod")
public class HubProd extends BaseEntity {

    /**
     * 产品编码
     */
    @TableId(value = "prod_code", type = IdType.INPUT)
    private String prodCode;

    /**
     * 产品类型
     */
    @TableField("prod_type")
    private Integer prodType;

    /**
     * 产品名称
     */
    @TableField("prod_name")
    private String prodName;

    /**
     * 最小长度
     */
    @TableField("min_length")
    private Float minLength;

    /**
     * 最大长度
     */
    @TableField("max_length")
    private Float maxLength;

    /**
     * 最小宽度
     */
    @TableField("min_width")
    private Float minWidth;

    /**
     * 最大宽度
     */
    @TableField("max_width")
    private Float maxWidth;

    /**
     * 材料编码
     */
    @TableField("material_code")
    private String materialCode;

    /**
     * 材料名称
     */
    @TableField("material_name")
    private String materialName;

    /**
     * 材料颜色
     */
    @TableField("material_color")
    private String materialColor;

    /**
     * 材料品牌
     */
    @TableField("material_brand")
    private String materialBrand;

    /**
     * 材料供应商
     */
    @TableField("material_supplier")
    private String materialSupplier;

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
     * 附加单价
     */
    @TableField("additional_unitfee")
    private BigDecimal additionalUnitfee;

    /**
     * 备注
     */
    @TableField("comments")
    private String comments;

    /**
     * 是否为商品
     */
    @TableField("is_merchandise")
    private Boolean merchandise;
}
