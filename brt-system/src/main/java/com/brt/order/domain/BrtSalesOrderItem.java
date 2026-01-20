package com.brt.order.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 销售订单明细对象 brt_sales_order_items
 *
 * @author Fgn
 * @date 2025-02-19
 */
@Data
@Accessors(chain = true)
@ToString
@EqualsAndHashCode
@TableName("brt_sales_order_items")
public class BrtSalesOrderItem {

    /**
     * 来源明细ID
     */
    @TableId(type = IdType.INPUT)
    private Long id;

    /**
     * 销售订单ID
     */
    private String orderId;

    /**
     * 产品名称
     */
    private String prodName;

    /**
     * 产品编码
     */
    private String prodCode;

    /**
     * 产品类型
     */
    private Integer prodType;

    /**
     * 组件信息
     */
    private String component;

    /**
     * 宽度
     */
    private BigDecimal width;

    /**
     * 高度
     */
    private BigDecimal height;

    /**
     * 厚度
     */
    private BigDecimal thickness;

    /**
     * 重量
     */
    private BigDecimal weight;

    /**
     * 工艺描述
     */
    private String procsDesc;

    /**
     * 工艺附件
     */
    private String procsAttachpath;

    /**
     * 件数
     */
    private BigDecimal itemNumber;

    /**
     * 单价
     */
    private BigDecimal itemPrice;

    /**
     * 实际金额
     */
    private BigDecimal actualAmount;

    /**
     * 发货单号
     */
    private String deliverySn;

    /**
     * 发货ID
     */
    private String deliveryId;

    /**
     * 明细状态
     */
    private Integer itemStatus;

    /**
     * 发货时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date shippingTime;

    /**
     * 物流状态
     */
    private Integer shippingStatus;

    /**
     * 产品描述
     */
    private String prodDescription;

    /**
     * 原始文件路径
     */
    private String imagefilePath;

    /**
     * 客户备注
     */
    private String comments;

    /**
     * 预览图
     */
    private String previewImagePath;

    /**
     * 缩略图
     */
    private String thumbnailsPath;

    /**
     * 工艺编码集合
     */
    private String procCode;

    /**
     * 明细排序
     */
    private Integer itemSeq;

    /**
     * 明细流水号
     */
    @TableField("orderItem_sn")
    private String orderItemSn;

    /**
     * 是否加急
     */
    private Integer isUrgent;

    /**
     * 发货日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deliveryDate;

    /**
     * 物流类型
     */
    private String logisticsType;

    /**
     * 物料成本
     */
    private BigDecimal matCost;

    /**
     * 工艺成本
     */
    private BigDecimal procCost;

    /**
     * 毛利
     */
    private BigDecimal grossMargin;

    /**
     * 税费
     */
    private BigDecimal taxFee;

    /**
     * 色彩模式
     */
    private String colorFormat;

    /**
     * 产品属性
     */
    private String prodAttrs;

    /**
     * 来源快照
     */
    private String snapshot;
}
