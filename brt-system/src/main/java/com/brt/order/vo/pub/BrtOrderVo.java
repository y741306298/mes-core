package com.brt.order.vo.pub;

import com.brt.common.enums.OrderAuditStatus;
import com.brt.common.enums.OrderTypeEnums;
import com.brt.order.domain.BrtSalesOrder;
import com.brt.order.service.IBrtMarketOrderService;
import com.brt.order.service.IBrtPriceSheetOrderService;
import com.brt.order.service.IBrtSalesOrderService;
import com.brt.order.vo.BrtMarketOrderVo;
import com.brt.order.vo.BrtPriceSheetOrderVo;
import com.brt.order.vo.BrtSalesOrderVo;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @BelongsProject: order-process
 * @BelongsPackage: com.brt.order.vo.pub
 * @Author: FanGN
 * @CreateTime: 2024/6/20 22:36
 * @Description: TODO
 * @Version: 1.0
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
public class BrtOrderVo {
    /**
     * 订单类型
     *     报价单("0","",""),
     *     销售单("1","brtSalesOrderService","orderCheck"),
     *     采购单("2","",""),
     */
    private OrderTypeEnums orderType;

    /**
     * 销售单
     */
    private BrtSalesOrderVo salesOrderVo;
    /**
     * 采购单
     */
    private BrtMarketOrderVo marketOrderVo;

    /**
     * 报价单
     */
    private BrtPriceSheetOrderVo priceSheetOrderVo;


    public String getTemplateId(){
        if(OrderTypeEnums.销售单.getCode().equals(this.orderType.getCode())){
            return salesOrderVo.getTemplateId();
        }else if(OrderTypeEnums.采购单.getCode().equals(this.orderType.getCode())){
            return marketOrderVo.getTemplateId();
        }else if(OrderTypeEnums.报价单.getCode().equals(this.orderType.getCode())){
            return priceSheetOrderVo.getTemplateId();
        }
        return null;
    }

    public String getOrderId(){
        if(OrderTypeEnums.销售单.getCode().equals(this.orderType.getCode())){
            return salesOrderVo.getOrderId();
        }else if(OrderTypeEnums.采购单.getCode().equals(this.orderType.getCode())){
            return marketOrderVo.getOrderId();
        }else if(OrderTypeEnums.报价单.getCode().equals(this.orderType.getCode())){
            return priceSheetOrderVo.getOrderId();
        }
        return null;
    }

    public String getOrderNo(){
        if(OrderTypeEnums.销售单.getCode().equals(this.orderType.getCode())){
            return salesOrderVo.getOrderNo();
        }else if(OrderTypeEnums.采购单.getCode().equals(this.orderType.getCode())){
            return marketOrderVo.getOrderNo();
        }else if(OrderTypeEnums.报价单.getCode().equals(this.orderType.getCode())){
            return priceSheetOrderVo.getOrderNo();
        }
        return null;
    }

    public BigDecimal getTotalAmount(){
        if(OrderTypeEnums.销售单.getCode().equals(this.orderType.getCode())){
            return salesOrderVo.getTotalAmount();
        }else if(OrderTypeEnums.采购单.getCode().equals(this.orderType.getCode())){
            return marketOrderVo.getTotalAmount();
        }else if(OrderTypeEnums.报价单.getCode().equals(this.orderType.getCode())){
            return priceSheetOrderVo.getTotalAmount();
        }
        return null;
    }

    public String getCustomerId(){
        if(OrderTypeEnums.销售单.getCode().equals(this.orderType.getCode())){
            return salesOrderVo.getCustomerId();
        }else if(OrderTypeEnums.采购单.getCode().equals(this.orderType.getCode())){
            return marketOrderVo.getSupplierId();
        }else if(OrderTypeEnums.报价单.getCode().equals(this.orderType.getCode())){
            return priceSheetOrderVo.getCustomerId();
        }
        return null;
    }

    public String getUserId(){
        if(OrderTypeEnums.销售单.getCode().equals(this.orderType.getCode())){
            return salesOrderVo.getUserId();
        }else if(OrderTypeEnums.采购单.getCode().equals(this.orderType.getCode())){
            return marketOrderVo.getUserId();
        }else if(OrderTypeEnums.报价单.getCode().equals(this.orderType.getCode())){
            return priceSheetOrderVo.getUserId();
        }
        return null;
    }

    public Long getTotalNum(){
        if(OrderTypeEnums.销售单.getCode().equals(this.orderType.getCode())){
            return salesOrderVo.getTotalNum();
        }else if(OrderTypeEnums.采购单.getCode().equals(this.orderType.getCode())){
            return marketOrderVo.getTotalNum();
        }else if(OrderTypeEnums.报价单.getCode().equals(this.orderType.getCode())){
            return priceSheetOrderVo.getTotalNum();
        }
        return null;
    }

    public Date getOrderTime(){
        if(OrderTypeEnums.销售单.getCode().equals(this.orderType.getCode())){
            return salesOrderVo.getOrderTime();
        }else if(OrderTypeEnums.采购单.getCode().equals(this.orderType.getCode())){
            return marketOrderVo.getOrderTime();
        }else if(OrderTypeEnums.报价单.getCode().equals(this.orderType.getCode())){
            return priceSheetOrderVo.getOrderTime();
        }
        return null;
    }

    public Date getDeliveryTime(){
        if(OrderTypeEnums.销售单.getCode().equals(this.orderType.getCode())){
            return salesOrderVo.getDeliveryTime();
        }else if(OrderTypeEnums.采购单.getCode().equals(this.orderType.getCode())){
            return marketOrderVo.getReceivingTime();
        }else if(OrderTypeEnums.报价单.getCode().equals(this.orderType.getCode())){
            return priceSheetOrderVo.getDeliveryTime();
        }
        return null;
    }

    public String getOrderRemark(){
        if(OrderTypeEnums.销售单.getCode().equals(this.orderType.getCode())){
            return salesOrderVo.getOrderRemark();
        }else if(OrderTypeEnums.采购单.getCode().equals(this.orderType.getCode())){
            return marketOrderVo.getOrderRemark();
        }else if(OrderTypeEnums.报价单.getCode().equals(this.orderType.getCode())){
            return priceSheetOrderVo.getOrderRemark();
        }
        return null;
    }



}
