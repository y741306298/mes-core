package com.brt.order.vo;

import java.math.BigDecimal;

import com.brt.common.annotation.Excel;
import com.brt.common.annotation.Excels;
import com.brt.order.domain.BrtMateriel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 物料信息Vo对象 brt_materiel
 *
 * @author Fgn
 * @date 2024-05-07
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtMaterielVo extends BrtMateriel {

    // 物料类型
    @Excels({
            @Excel(name = "物料类型",targetAttr = "typeName",type = Excel.Type.EXPORT)
    })
    private BrtMaterielTypeVo materielTypeVo;

    // 类型名称
    @Excel(name = "类型名称",type = Excel.Type.IMPORT)
    private String typeName;

    private String[] ids;


}
