package com.brt.order.vo;

import com.brt.common.annotation.Excel;
import com.brt.common.core.domain.BaseEntity;

import java.math.BigDecimal;

public class BrtCheckAchievementVo extends BaseEntity {

    /**
     * 月份
     */
    @Excel(name = "月份")
    private String month;

    /**
     * 收入
     */
    @Excel(name = "收入")
    private BigDecimal income;

    /**
     * 支出
     */
    @Excel(name = "支出")
    private BigDecimal expend;

    /**
     * 当月结余
     */
    @Excel(name = "当月结余")
    private BigDecimal balance;

    /**
     * 累计结余
     */
    @Excel(name = "累计结余")
    private BigDecimal totalBalance;
}
