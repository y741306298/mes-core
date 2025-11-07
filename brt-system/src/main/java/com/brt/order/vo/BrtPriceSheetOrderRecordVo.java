package com.brt.order.vo;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.brt.order.domain.BrtPriceSheetOrderRecord;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 报价单_报价记录Vo对象 brt_price_sheet_order_record
 *
 * @author Fgn
 * @date 2024-07-13
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtPriceSheetOrderRecordVo extends BrtPriceSheetOrderRecord {


}
