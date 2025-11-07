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
 * 互转单对象 brt_intertransfer_order
 *
 * @author Fgn
 * @date 2024-05-16
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtIntertransferOrder extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String intertransferId;

    /**
     * 单号
     */
    @Excel(name = "单号")
    private String intertransferNo;

    /**
     * 员工ID
     */
    @Excel(name = "员工ID")
    private String userId;

    /**
     * 金额
     */
    @Excel(name = "金额")
    private BigDecimal intertransferAmount;

    /**
     * 付款账户ID
     */
    @Excel(name = "付款账户ID")
    private String payAccountId;

    /**
     * 收款账户ID
     */
    @Excel(name = "收款账户ID")
    private String collectionAccountId;

    /**
     * 账单日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "账单日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date intertransferDate;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String intertransferRemark;

    /**
     * 附件
     */
    @Excel(name = "附件")
    private String attachments;


        /***************************自定义字段*****************************/

}
