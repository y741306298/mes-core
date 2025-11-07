package com.brt.order.vo;

import java.math.BigDecimal;
import com.brt.order.domain.BrtPriceSheetOrderDetailsRecord;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 报价记录详情Vo对象 brt_price_sheet_order_details_record
 *
 * @author Fgn
 * @date 2024-07-13
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtPriceSheetOrderDetailsRecordVo extends BrtPriceSheetOrderDetailsRecord {

}
