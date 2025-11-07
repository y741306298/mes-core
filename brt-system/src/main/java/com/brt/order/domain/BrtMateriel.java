package com.brt.order.domain;

import java.math.BigDecimal;
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
 * 物料信息对象 brt_materiel
 *
 * @author Fgn
 * @date 2024-05-07
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtMateriel extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String materielId;

    /**
     * 物料编号
     */
    @Excel(name = "物料编号")
    private String materielNo;

    /**
     * 类型ID
     */
    private String typeId;

    /**
     * 物料名称
     */
    @Excel(name = "物料名称")
    private String materielName;

    /**
     * 型号规格
     */
    @Excel(name = "型号规格")
    private String materielSpec;

    /**
     * 单位
     */
    @Excel(name = "单位")
    private String materielUnit;

    /**
     * 产品尺寸
     */
    @Excel(name = "产品尺寸")
    private String materielSize;

    /**
     * 数量
     */
    @Excel(name = "库存数量")
    private Long materielNum;

    /**
     * 预警数量
     */
    @Excel(name = "库存预警")
    private Long warningNum;

    /**
     * 锁定数量
     */
    @Excel(name = "锁定数量")
    private Long lockNum;

    /**
     * 销售单价
     */
    @Excel(name = "销售单价",type = Excel.Type.IMPORT)
    private BigDecimal sellPrice;

    /**
     * 采购单价
     */
    @Excel(name = "采购单价")
    private BigDecimal purchasePrice;

    /**
     * 总价
     */
    @Excel(name = "总价")
    private BigDecimal totalPrice;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String materielRemark;

    /**
     * 附件
     */
    private String files;

    /**
     * boom单
     */
    private String boom;

    /**
     * 自定义字段
     */
    @Excel(name = "自定义字段")
    private String otherFields;

    /**
     * 库位
     */
    @Excel(name = "库位")
    private String location;

    /**
     * 供应商ID
     */
    @Excel(name = "供应商")
    private String supplier;

        /***************************自定义字段*****************************/

}
