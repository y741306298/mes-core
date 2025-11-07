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
 * 报价单_报价记录对象 brt_price_sheet_order_record
 *
 * @author Fgn
 * @date 2024-07-13
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtPriceSheetOrderRecord extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String recordId;

    /**
     * 报价单ID
     */
    private String orderId;
    /**
     * 订单编号
     */
    @Excel(name = "订单编号")
    private String orderNo;

    /**
     * 客户ID
     */
    @Excel(name = "客户ID")
    private String customerId;

    /**
     * 联系人
     */
    @Excel(name = "联系人")
    private String contact;

    /**
     * 联系电话
     */
    @Excel(name = "联系电话")
    private String contactTel;

    /**
     * 送货地址
     */
    @Excel(name = "送货地址")
    private String deliveryAddress;

    /**
     * 下单日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "下单日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date orderTime;

    /**
     * 交货日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "交货日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date deliveryTime;

    /**
     * 进度模板
     */
    @Excel(name = "进度模板")
    private String templateId;

    /**
     * 工艺类型(0=标准品,1=非标准品)
     */
    @Excel(name = "工艺类型(0=标准品,1=非标准品)")
    private String craftType;

    /**
     * 合计数量
     */
    @Excel(name = "合计数量")
    private Long totalNum;

    /**
     * 合计金额
     */
    @Excel(name = "合计金额")
    private BigDecimal totalAmount;

    /**
     * 币种(0=人民币,1=美元)
     */
    @Excel(name = "币种(0=人民币,1=美元)")
    private String currencyType;

    /**
     * 附件
     */
    @Excel(name = "附件")
    private String attachments;

    /**
     * 业务员
     */
    @Excel(name = "业务员")
    private String userId;

    /**
     * 订单备注
     */
    @Excel(name = "订单备注")
    private String orderRemark;

    /**
     * 收款金额
     */
    @Excel(name = "收款金额")
    private BigDecimal collectionAmount;

    /**
     * 收款状态(0=未收款,1=部分收款,2=已收款)
     */
    @Excel(name = "收款状态(0=未收款,1=部分收款,2=已收款)")
    private String collectionStatus;

    /**
     * 订单状态(0=待交货,1=已交货)
     */
    @Excel(name = "订单状态(0=待交货,1=已交货)")
    private String orderStatus;


        /***************************自定义字段*****************************/

}
