package com.brt.order.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.brt.common.annotation.Excel;
import com.brt.common.annotation.Excels;
import com.brt.common.core.domain.entity.SysUser;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.brt.order.domain.BrtPriceSheetOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 销售单Vo对象 brt_priceSheet_order
 *
 * @author Fgn
 * @date 2024-05-09
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtPriceSheetOrderVo extends BrtPriceSheetOrder {

    // 客户信息
    @Excels({
            @Excel(name = "客户名称",targetAttr = "customerName")
    })
    private BrtCustomerVo customerVo;

    // 销售单列表
    private List<BrtPriceSheetOrderDetailsVo> priceSheetOrderDetailsVoList;

    // 客户地址
    private BrtCustomerAddressVo customerAddressVo;

    // 业务员
    @Excels({
            @Excel(name = "业务员",targetAttr = "userName")
    })
    private SysUser user;

    private String createTimeStart;
    private String createTimeEnd;

    // 当前节点
    private BrtOrderNodeVo nowOrderNodeVo;
}
