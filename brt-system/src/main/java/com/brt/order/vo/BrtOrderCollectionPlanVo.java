package com.brt.order.vo;

import java.math.BigDecimal;
import com.brt.order.domain.BrtOrderCollectionPlan;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 收款计划Vo对象 brt_order_collection_plan
 *
 * @author Fgn
 * @date 2024-06-20
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtOrderCollectionPlanVo extends BrtOrderCollectionPlan {

}
