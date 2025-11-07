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
 * 销售单详情对象 brt_market_order_details
 *
 * @author Fgn
 * @date 2024-05-09
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtMarketOrderDetails extends BaseEntity {

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
    private String supplierPn;

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

    /**
     * 规格型号
     */
    private String materielSpec;

    /**
     * 编码
     */
    private String coding;

    /**
     * 材质
     */
    private String texture;

    /**
     * 物料编号
     */
    private String materielNo;


    /***************************自定义字段*****************************/

}
