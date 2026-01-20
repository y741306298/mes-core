package com.brt.order.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.brt.common.annotation.Excel;
import com.brt.common.annotation.Excels;
import com.brt.common.core.domain.entity.SysUser;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.brt.order.domain.BrtSalesOrder;
import com.brt.order.domain.BrtSalesOrderItem;
import com.brt.order.domain.BrtSalesOrderItemProc;
import com.brt.order.domain.BrtSalesOrderPackage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 销售单Vo对象 brt_sales_order
 *
 * @author Fgn
 * @date 2024-05-09
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtSalesOrderVo extends BrtSalesOrder {

    // 客户信息
    @Excels({
            @Excel(name = "客户名称",targetAttr = "customerName" ,type = Excel.Type.EXPORT)
    })
    private BrtCustomerVo customerVo;

    // 销售单列表
    private List<BrtSalesOrderDetailsVo> salesOrderDetailsVoList;

    // 订单明细列表(来源 order_items)
    private List<BrtSalesOrderItem> salesOrderItemList;

    // 订单包裹列表(来源 order_packages)
    private List<BrtSalesOrderPackage> salesOrderPackageList;

    // 订单工艺列表(来源 order_items.component)
    private List<BrtSalesOrderItemProc> salesOrderItemProcList;

    // 客户地址
    private BrtCustomerAddressVo customerAddressVo;

    // 业务员
    @Excels({
            @Excel(name = "业务员",targetAttr = "userName",type = Excel.Type.EXPORT)
    })
    private SysUser user;

    private String createTimeStart;
    private String createTimeEnd;

    // 当前节点
    private BrtOrderNodeVo nowOrderNodeVo;

    private String[] ids;
}
