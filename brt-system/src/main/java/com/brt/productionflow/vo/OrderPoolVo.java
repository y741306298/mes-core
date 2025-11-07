package com.brt.productionflow.vo;

import com.brt.order.vo.BrtFlowTemplateVo;
import com.brt.productionflow.domain.OrderPool;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单池视图对象
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class OrderPoolVo extends OrderPool {

    private BrtFlowTemplateVo flowTemplate;
}
