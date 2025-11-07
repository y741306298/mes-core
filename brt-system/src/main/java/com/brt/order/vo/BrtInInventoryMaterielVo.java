package com.brt.order.vo;

import com.brt.common.annotation.Excel;
import com.brt.order.domain.BrtInInventoryMateriel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 入库单详情 入库单关联物料Vo对象 brt_in_inventory_materiel
 *
 * @author Fgn
 * @date 2024-07-11
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtInInventoryMaterielVo extends BrtInInventoryMateriel {

    /**
     * 物料编号
     */
    @Excel(name = "物料编号")
    private String materielNo;

    /**
     * 物料名称
     */
    @Excel(name = "物料名称")
    private String materielName;
//
//    /**
//     * 类型
//     */
//    @Excel(name = "分类名称",dictType = "")
//    private String typeName;

    /**
     * 类型ID
     */
    @Excel(name = "分类名称",dictType = "materiel_type")
    private String typeId;

    /**
     * 型号规格
     */
    @Excel(name = "型号规格")
    private String materielSpec;
}
