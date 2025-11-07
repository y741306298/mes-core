package com.brt.order.domain;

import java.math.BigDecimal;
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
 * 报价记录详情对象 brt_price_sheet_order_details_record
 *
 * @author Fgn
 * @date 2024-07-13
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtPriceSheetOrderDetailsRecord extends BaseEntity {


    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String detailsRecordId;
    /**
     * 报价单详情
     */
    private String detailsId;

    /**
     * 编号
     */
    @Excel(name = "编号")
    private String detailsNo;

    /**
     * 订单ID
     */
    @Excel(name = "订单ID")
    private String orderId;

    /**
     * 物料ID
     */
    @Excel(name = "物料ID")
    private String materielId;

    /**
     * 产品类型
     */
    @Excel(name = "产品类型")
    private String materielType;

    /**
     * 产品尺寸
     */
    @Excel(name = "产品尺寸")
    private String materielSize;

    /**
     * 生产单号
     */
    @Excel(name = "生产单号")
    private String yieldNo;

    /**
     * 图纸编号
     */
    @Excel(name = "图纸编号")
    private String drawingNo;

    /**
     * 客户料号
     */
    @Excel(name = "客户料号")
    private String customerPn;

    /**
     * 数量
     */
    @Excel(name = "数量")
    private Long detailsNum;

    /**
     * 单位
     */
    @Excel(name = "单位")
    private String materielUnit;

    /**
     * 单价
     */
    @Excel(name = "单价")
    private BigDecimal detailsPrice;

    /**
     * 总价
     */
    @Excel(name = "总价")
    private BigDecimal detailsAmount;

    /**
     * 封装型号
     */
    @Excel(name = "封装型号")
    private String packageModel;

    /**
     * 封装类型
     */
    @Excel(name = "封装类型")
    private String packageTypeId;

    /**
     * 封装尺寸
     */
    @Excel(name = "封装尺寸")
    private String packageSize;

    /**
     * 个/箱
     */
    @Excel(name = "个/箱")
    private Long pagkageNum;

    /**
     * 测试类型
     */
    @Excel(name = "测试类型")
    private String testTypeId;

    /**
     * 测试方式
     */
    @Excel(name = "测试方式")
    private String testMode;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String detailsRemark;

    /**
     * 附件
     */
    @Excel(name = "附件")
    private String attachments;

    /**
     * boom单
     */
    @Excel(name = "boom单")
    private String boomFile;

    /**
     * 工件图纸
     */
    @Excel(name = "工件图纸")
    private String jobDrawing;

    /**
     * 工件程序
     */
    @Excel(name = "工件程序")
    private String jobProgram;


        /***************************自定义字段*****************************/

}
