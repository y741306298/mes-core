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
 * 客户送货单对象 brt_supplier_receiving
 *
 * @author Fgn
 * @date 2024-06-28
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtSupplierReceiving extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String receivingId;

    /**
     * 客户ID
     */
    @Excel(name = "客户ID")
    private String supplierId;

    /**
     * 订单ID
     */
    @Excel(name = "订单ID")
    private String orderId;

    /**
     * 订单节点ID
     */
    @Excel(name = "订单节点ID")
    private String orderNodeId;

    /**
     * 送货状态(0=未送货,1=部分送货,2=已送货)
     */
    @Excel(name = "送货状态(0=未送货,1=部分送货,2=已送货)")
    private String receivingStatus;


    /***************************自定义字段*****************************/

}
