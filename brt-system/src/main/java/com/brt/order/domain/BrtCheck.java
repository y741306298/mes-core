package com.brt.order.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.brt.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.brt.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 账单对象 brt_check
 *
 * @author Fgn
 * @date 2024-05-15
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtCheck extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String checkId;

    /**
     * 单号
     */
    @Excel(name = "单号")
    private String checkNo;

    /**
     * 分类ID
     */
    @Excel(name = "分类ID")
    private String typeId;

    /**
     * 订单类型(0=报价单,1=销售单,2=采购单)
     */
    @Excel(name = "订单类型(0=报价单,1=销售单,2=采购单)")
    private String orderType;

    /**
     * 订单ID
     */
    @Excel(name = "订单ID")
    private String orderId;

    /**
     * 订单编号
     */
    @Excel(name = "订单编号")
    private String orderNo;

    /**
     * 员工ID
     */
    @Excel(name = "员工ID")
    private String userId;

    /**
     * 金额
     */
    @Excel(name = "金额")
    private BigDecimal checkAmount;

    /**
     * 账户ID
     */
    @Excel(name = "账户ID")
    private String accountId;

    /**
     * 账单日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "账单日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date checkDate;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String checkRemark;

    /**
     * 附件
     */
    @Excel(name = "附件")
    private String attachments;

    /**
     * 账单类型(0=支出,1=收入)
     */
    @Excel(name = "账单类型(0=支出,1=收入)")
    private String checkType;


        /***************************自定义字段*****************************/

}
