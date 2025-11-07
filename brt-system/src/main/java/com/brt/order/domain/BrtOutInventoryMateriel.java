package com.brt.order.domain;

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

import java.math.BigDecimal;

/**
 * 入库单详情 入库单关联物料对象 brt_out_inventory_materiel
 *
 * @author Fgn
 * @date 2024-07-11
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtOutInventoryMateriel extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String outInventoryMaterielId;

    /**
     * 出库单ID
     */
    private String outInventoryId;

    /**
     * 物料ID
     */
    private String materielId;

    /**
     * 入库数量
     */
    @Excel(name = "入库数量")
    private Long outInventoryNum;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;

    /**
     * 库存结余
     */
    private BigDecimal residueNum;

        /***************************自定义字段*****************************/

}
