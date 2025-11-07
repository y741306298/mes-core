package com.brt.order.vo;

import com.brt.order.domain.BrtCustomerAddress;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 用户地址Vo对象 brt_customer_address
 *
 * @author Fgn
 * @date 2024-04-27
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtCustomerAddressVo extends BrtCustomerAddress {

}
