package com.brt.order.vo;

import java.math.BigDecimal;
import java.util.List;

import com.brt.common.annotation.Excel;
import com.brt.common.annotation.Excels;
import com.brt.common.core.domain.entity.SysUser;
import com.brt.order.domain.BrtPriceSheetOrderDetails;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 销售单详情Vo对象 brt_priceSheet_order_details
 *
 * @author Fgn
 * @date 2024-05-09
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtPriceSheetOrderDetailsVo extends BrtPriceSheetOrderDetails {

    // 物料信息
    @Excels({
            @Excel(name = "产品名称",targetAttr = "materielName",type = Excel.Type.EXPORT)
    })
    private BrtMaterielVo materielVo;

    //产品名称
    @Excel(name = "产品名称",type = Excel.Type.IMPORT)
    private String materielName;

    // 订单信息
    @Excels({
            @Excel(name = "关联业务",targetAttr = "orderNo",type = Excel.Type.EXPORT),
            @Excel(name = "销售日期",targetAttr = "orderTime",type = Excel.Type.EXPORT),
            @Excel(name = "总数量",targetAttr = "totalNum",type = Excel.Type.EXPORT),
            @Excel(name = "总额",targetAttr = "totalAmount",type = Excel.Type.EXPORT)
    })
    private BrtPriceSheetOrderVo priceSheetOrderVo;

    @Excels({
            @Excel(name = "业务员",targetAttr = "userName",type = Excel.Type.EXPORT)
    })
    private SysUser user;

    // boom单列表
    private List<BrtOrderBoomVo> boomVoList;

    // 客户ID
    private String customerId;
    private String customerName;

    // 客户类型ID
    private String customerTypeId;

    // 业务员
    private String userId;

    private String createTimeStart;
    private String createTimeEnd;
}
