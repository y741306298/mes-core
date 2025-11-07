package com.brt.order.vo;

import com.brt.order.domain.BrtPriceSheetMateriel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 数量记录Vo对象 brt_order_materiel_record
 *
 * @author Fgn
 * @date 2024-06-20
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtPriceSheetMaterielVo extends BrtPriceSheetMateriel {

}
