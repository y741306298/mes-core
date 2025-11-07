package com.brt.order.vo;

import java.math.BigDecimal;
import com.brt.order.domain.BrtOrderInvoiceRecord;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 开票记录Vo对象 brt_order_invoice_record
 *
 * @author Fgn
 * @date 2024-06-19
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtOrderInvoiceRecordVo extends BrtOrderInvoiceRecord {

}
