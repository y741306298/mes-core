package com.brt.order.vo;

import com.brt.order.domain.BrtOrderMaterielPlan;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 物料数量计划Vo对象 brt_order_materiel_plan
 *
 * @author Fgn
 * @date 2024-06-20
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtOrderMaterielPlanVo extends BrtOrderMaterielPlan {

    // 物料信息
    private BrtMaterielVo materielVo;

}
