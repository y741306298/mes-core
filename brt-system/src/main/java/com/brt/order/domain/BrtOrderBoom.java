package com.brt.order.domain;

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
 * boom单对象 brt_order_boom
 *
 * @author Fgn
 * @date 2024-06-20
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtOrderBoom extends BaseEntity {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String boomId;

    /**
     * 订单ID
     */
    //@Excel(name = "订单ID")
    private String orderId;

    /**
     * 订单类型(0=报价单,1=销售单,2=采购单)
     */
    //@Excel(name = "订单类型(0=报价单,1=销售单,2=采购单)")
    private String orderType;

    /**
     * 订单详情ID
     */
    private String orderDetailsId;

    /**
     * 产品id
     */
    //@Excel(name = "产品id")
    private String materielId;

    /**
     * 物料名称
     */
    @Excel(name = "物料名称",sort = 1)
    private String materielName;

    /**
     * 型号规格
     */
    @Excel(name = "型号规格",sort = 2)
    private String materielSpec;

    /**
     * 是否是标准件
     */
    @Excel(name = "类型",sort = 3)
    private String isCriterion;

    /**
     * 数量
     */
    @Excel(name = "数量",sort = 4)
    private Long boomNum;

    /**
     * 总数量
     */
    private Long totalNum;

    /**
     * 是否已锁库存
     */
    private String isLock;



        /***************************自定义字段*****************************/

}
