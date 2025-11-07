package com.brt.order.vo;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.brt.order.domain.BrtCheck;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 账单Vo对象 brt_check
 *
 * @author Fgn
 * @date 2024-05-15
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtCheckVo extends BrtCheck {

    // 账单类型
    private BrtCheckTypeVo checkTypeVo;

    // 账户
    private BrtAccountVo accountVo;

    // 日期筛选类型
    private String checkDateType;

    // 日期开始时间
    private String checkDateStart;

    // 日期结束时间
    private String checkDateEnd;
}
