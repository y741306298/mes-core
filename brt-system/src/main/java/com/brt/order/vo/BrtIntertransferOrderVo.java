package com.brt.order.vo;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.brt.order.domain.BrtIntertransferOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 互转单Vo对象 brt_intertransfer_order
 *
 * @author Fgn
 * @date 2024-05-16
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtIntertransferOrderVo extends BrtIntertransferOrder {

    // 付款账户
    private BrtAccountVo payAccountVo;

    // 收款账户
    private BrtAccountVo collectionAccountVo;

    // 日期筛选类型
    private String intertransferDateType;

    // 日期开始时间
    private String intertransferDateStart;

    // 日期结束时间
    private String intertransferDateEnd;
}
