package com.brt.order.vo;

import com.brt.common.annotation.Excel;
import com.brt.order.domain.BrtOrderBoom;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * boom单Vo对象 brt_order_boom
 *
 * @author Fgn
 * @date 2024-06-20
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtOrderBoomVo extends BrtOrderBoom {

}
