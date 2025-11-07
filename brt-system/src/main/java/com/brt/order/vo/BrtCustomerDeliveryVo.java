package com.brt.order.vo;

import com.brt.order.domain.BrtCustomerDelivery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 客户送货单Vo对象 brt_customer_delivery
 *
 * @author Fgn
 * @date 2024-06-28
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtCustomerDeliveryVo extends BrtCustomerDelivery {

}
