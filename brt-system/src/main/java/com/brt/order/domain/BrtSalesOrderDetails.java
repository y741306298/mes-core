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
 * 销售单详情对象 brt_sales_order_details
 *
 * @author Fgn
 * @date 2024-05-09
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtSalesOrderDetails extends BaseEntity {

    /**
     * 注解ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String detailsId;

    /**
     * 编号
     */
    @Excel(name = "编号",type = Excel.Type.IMPORT)
    private String detailsNo;

    /**
     * 订单ID
     */
    //@Excel(name = "订单ID")
    private String orderId;

    /**
     * 物料ID
     */
    //@Excel(name = "物料ID")
    private String materielId;

    /**
     * 产品类型
     */
    @Excel(name = "产品类型",type = Excel.Type.EXPORT)
    private String materielType;

    /**
     * 产品尺寸
     */
    @Excel(name = "产品尺寸",type = Excel.Type.EXPORT)
    private String materielSize;

    /**
     * 生产单号
     */
    @Excel(name = "生产单号",type = Excel.Type.EXPORT)
    private String yieldNo;

    /**
     * 图纸编号
     */
    @Excel(name = "图纸编号",type = Excel.Type.EXPORT)
    private String drawingNo;

    /**
     * 客户料号
     */
    @Excel(name = "客户料号",type = Excel.Type.EXPORT)
    private String customerPn;

    /**
     * 数量
     */
    @Excel(name = "数量",type = Excel.Type.EXPORT)
    private Long detailsNum;

    /**
     * 单位
     */
    @Excel(name = "单位",type = Excel.Type.EXPORT)
    private String materielUnit;

    /**
     * 单价
     */
    @Excel(name = "单价",type = Excel.Type.EXPORT)
    private BigDecimal detailsPrice;

    /**
     * 总价
     */
    @Excel(name = "总价",type = Excel.Type.EXPORT)
    private BigDecimal detailsAmount;

    /**
     * 封装型号
     */
    @Excel(name = "封装型号",type = Excel.Type.EXPORT)
    private String packageModel;

    /**
     * 封装类型
     */
    @Excel(name = "封装类型",type = Excel.Type.EXPORT)
    private String packageTypeId;

    /**
     * 封装尺寸
     */
    @Excel(name = "封装尺寸",type = Excel.Type.EXPORT)
    private String packageSize;

    /**
     * 个/箱
     */
    //@Excel(name = "个/箱")
    private Long pagkageNum;

    /**
     * 测试类型
     */
    //@Excel(name = "测试类型")
    private String testTypeId;

    /**
     * 测试方式
     */
    @Excel(name = "测试方式",type = Excel.Type.EXPORT)
    private String testMode;

    /**
     * 备注
     */
    @Excel(name = "备注",type = Excel.Type.EXPORT)
    private String detailsRemark;

    /**
     * 附件
     */
    //@Excel(name = "附件")
    private String attachments;

    /**
     * boom单
     */
    private String boomFile;

    /**
     * 工件图纸
     */
    private String jobDrawing;

    /**
     * 工件程序
     */
    private String jobProgram;


        /***************************自定义字段*****************************/

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
     * 工艺附件路径
     */
    private String procsAttachpath;

    /**
     * 数量
     */
    private BigDecimal itemNumber;

    /**
     * 单价
     */
    private BigDecimal itemPrice;

    /**
     * 实付金额
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
    private Integer urgent;

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
