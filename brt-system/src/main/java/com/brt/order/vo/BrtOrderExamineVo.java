package com.brt.order.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.brt.common.enums.OrderTypeEnums;
import com.brt.order.vo.pub.BrtOrderVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.brt.order.domain.BrtOrderExamine;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 订单审批Vo对象 brt_order_examine
 *
 * @author Fgn
 * @date 2024-05-10
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtOrderExamineVo extends BrtOrderExamine {

    // 查询客户信息
    private BrtCustomerVo customerVo;

    public BrtOrderExamineVo(){}

    public BrtOrderExamineVo(BrtOrderVo orderVo){
        super.setOrderId(orderVo.getOrderId());
        super.setOrderNo(orderVo.getOrderNo());
        super.setOrderType(orderVo.getOrderType().getCode());
        super.setCustomerId(orderVo.getCustomerId());
        super.setOrderNum(orderVo.getTotalNum());
        super.setOrderAmount(orderVo.getTotalAmount());
        super.setOrderDate(orderVo.getOrderTime());
        super.setOrderRemark(orderVo.getOrderRemark());
    }

}
