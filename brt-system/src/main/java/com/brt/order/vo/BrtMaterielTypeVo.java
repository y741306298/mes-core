package com.brt.order.vo;

import com.brt.order.domain.BrtMaterielType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 物料类型Vo对象 brt_materiel_type
 *
 * @author Fgn
 * @date 2024-05-07
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtMaterielTypeVo extends BrtMaterielType {

}
