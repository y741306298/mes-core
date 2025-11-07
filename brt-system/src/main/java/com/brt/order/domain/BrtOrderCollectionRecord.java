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
 * 收款记录对象 brt_order_collection_record
 *
 * @author Fgn
 * @date 2024-06-20
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtOrderCollectionRecord extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String recordId;

    // 流程节点ID
    private String nodeId;

    // 订单流程节点ID
    private String orderNodeId;

    /**
     * 订单ID
     */
    @Excel(name = "订单ID")
    private String orderId;

    /**
     * 订单类型(0=报价单,1=销售单,2=采购单)
     */
    @Excel(name = "订单类型(0=报价单,1=销售单,2=采购单)")
    private String orderType;

    /**
     * 收款金额
     */
    @Excel(name = "收款金额")
    private BigDecimal collectionAmount;

    /**
     * 剩余收款金额
     */
    @Excel(name = "剩余收款金额")
    private BigDecimal surplusAmount;


        /***************************自定义字段*****************************/

}
