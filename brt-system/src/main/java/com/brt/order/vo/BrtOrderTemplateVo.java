package com.brt.order.vo;

import com.brt.common.enums.OrderTypeEnums;
import com.brt.order.domain.BrtFlowTemplate;
import com.brt.order.domain.BrtOrderTemplate;
import com.brt.order.vo.pub.BrtOrderVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 订单模板Vo对象 brt_order_template
 *
 * @author Fgn
 * @date 2024-05-10
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtOrderTemplateVo extends BrtOrderTemplate {

    // 模板信息
    private BrtFlowTemplateVo flowTemplateVo;

    // 客户信息
    private BrtCustomerVo customerVo;

    //供应商信息
    private BrtSupplierVo supplierVo;

    // 订单节点列表
    private List<BrtOrderNodeVo> orderNodeVoList;

    private Long thisUserId;//当前登录用户ID

    private String flowQueryAudit;

    public BrtOrderTemplateVo() {}

    public BrtOrderTemplateVo(BrtOrderVo orderVo){
        super.setOrderId(orderVo.getOrderId());
        super.setOrderNo(orderVo.getOrderNo());
        super.setOrderType(orderVo.getOrderType().getCode());
        if(orderVo.getOrderType().getCode().equals(OrderTypeEnums.采购单.getCode())){
            super.setSupplierId(orderVo.getCustomerId());
        }else{
            super.setCustomerId(orderVo.getCustomerId());
        }
        super.setTemplateId(orderVo.getTemplateId());
        super.setUserId(orderVo.getUserId());
        super.setOrderNum(orderVo.getTotalNum());
        super.setOrderAmount(orderVo.getTotalAmount());
        super.setOrderDate(orderVo.getOrderTime());
        super.setDeliveryDate(orderVo.getDeliveryTime());
    }

}
