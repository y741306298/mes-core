package com.brt.order.vo;

import java.math.BigDecimal;
import com.brt.order.domain.BrtOrderCollectionRecord;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 收款记录Vo对象 brt_order_collection_record
 *
 * @author Fgn
 * @date 2024-06-20
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtOrderCollectionRecordVo extends BrtOrderCollectionRecord {

}
