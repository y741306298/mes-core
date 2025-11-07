package com.brt.order.vo;

import com.brt.order.domain.BrtSupplierReceiving;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 客户送货单Vo对象 brt_supplier_receiving
 *
 * @author Fgn
 * @date 2024-06-28
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtSupplierReceivingVo extends BrtSupplierReceiving {

}
