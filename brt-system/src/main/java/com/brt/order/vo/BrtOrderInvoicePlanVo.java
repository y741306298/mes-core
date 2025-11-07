package com.brt.order.vo;

import java.math.BigDecimal;
import com.brt.order.domain.BrtOrderInvoicePlan;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 开票计划Vo对象 brt_order_invoice_plan
 *
 * @author Fgn
 * @date 2024-06-19
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtOrderInvoicePlanVo extends BrtOrderInvoicePlan {

}
