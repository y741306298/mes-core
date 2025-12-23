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
 * 销售单对象 brt_sales_order
 *
 * @author Fgn
 * @date 2024-05-09
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtSalesOrder extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String orderId;

    /**
     * 订单编号
     */
    @Excel(name = "订单编号")
    private String orderNo;

    /**
     * 客户ID
     */
    //@Excel(name = "客户ID")
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
     * 送货地址ID
     */
    //@Excel(name = "送货地址")
    private String deliveryAddress;

    /**
     * 下单日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "下单日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date orderTime;

    /**
     * 交货日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "交货日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deliveryTime;

    /**
     * 进度模板
     */
    //@Excel(name = "进度模板")
    private String templateId;

    /**
     * 工艺类型(0=标准品,1=非标准品)
     */
    @Excel(name = "工艺类型",dictType = "craft_type")
    private String craftType;

    /**
     * 合计数量
     */
    @Excel(name = "数量")
    private Long totalNum;

    /**
     * 合计金额
     */
    @Excel(name = "总价")
    private BigDecimal totalAmount;

    /**
     * 币种(0=人民币,1=美元)
     */
    @Excel(name = "币种",dictType = "currency_type")
    private String currencyType;

    /**
     * 附件
     */
    //@Excel(name = "附件")
    private String attachments;

    /**
     * 业务员
     */
    //@Excel(name = "业务员")
    private String userId;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String orderRemark;

    /**
     * 收款金额
     */
//    @Excel(name = "收款金额")
    private BigDecimal collectionAmount;

    /**
     * 收款状态(0=未收款,1=部分收款,2=已收款)
     */
    private String collectionStatus;

    /**
     * 订单状态(0=待交货,1=已交货)
     */
    private String orderStatus;

    /**
     * 审批状态
     */
    private String status;

    /**
     * 审核完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date finishDate;

    /**
     * 自定义字段
     */
    @Excel(name = "自定义字段")
    private String otherFields;

    /**
     * 是否延期
     */
    private String postpone;

    /**
     * 订单编号（来源系统）
     */
    private String orderSn;

    /**
     * 客户ID（来源系统）
     */
    private String clientId;

    /**
     * 收货人
     */
    private String consignee;

    /**
     * 收货地址
     */
    private String address;

    /**
     * 收货电话
     */
    private String tel;

    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

    /**
     * 运费
     */
    private BigDecimal shippingFee;

    /**
     * 已付金额
     */
    private BigDecimal moneyPaid;

    /**
     * 优惠金额
     */
    private BigDecimal discount;

    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    /**
     * 生产方ID
     */
    private String manufacturerId;

    /**
     * 交货日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deliveryDate;

    /**
     * 是否由MES创建
     */
    private String mes;

    /**
     * MES创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date mesTime;

    /**
     * 物流单号
     */
    private String logisticCode;

    /**
     * 是否撤回
     */
    private String isWithdraw;

    /**
     * 账号编码
     */
    private String userCode;

    /**
     * 账号组织编码
     */
    private String userOrgCode;

    /**
     * 订单转移状态
     */
    private String transferStatus;

    /**
     * 来源快照
     */
    private String snapshot;

    /**
     * 转移来源
     */
    private String transferFrom;

    /**
     * 结算金额
     */
    private BigDecimal settleOrderAmount;

    /**
     * 订单类型
     */
    private String type;

    /**
     * 外部订单号
     */
    private String outOrderNo;

        /***************************自定义字段*****************************/

}
