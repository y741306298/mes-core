package com.brt.order.domain;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 销售订单工艺对象 brt_sales_order_item_procs
 *
 * @author Fgn
 * @date 2025-02-19
 */
@Data
@Accessors(chain = true)
@ToString
@EqualsAndHashCode
@TableName("brt_sales_order_item_procs")
public class BrtSalesOrderItemProc {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 销售订单ID
     */
    private String orderId;

    /**
     * 订单明细ID
     */
    private Long orderItemId;

    /**
     * 工艺编码
     */
    private String procCode;

    /**
     * 工艺名称
     */
    private String procName;

    /**
     * 工艺描述(JSON)
     */
    private String procDescription;

    /**
     * 辅材编码
     */
    private String accessoryCode;

    /**
     * 辅材数量
     */
    private BigDecimal accessoryNumber;

    /**
     * 工艺附件路径
     */
    private String procAttachPath;

    /**
     * 工艺顺序
     */
    private Integer procSort;

    /**
     * 来源快照
     */
    private String snapshot;
}
