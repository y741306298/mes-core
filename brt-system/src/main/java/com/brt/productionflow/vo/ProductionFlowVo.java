package com.brt.productionflow.vo;

import com.brt.order.vo.BrtFlowTemplateVo;
import com.brt.productionflow.domain.ProductionFlow;
import com.brt.productionflow.domain.ProductionFlowMaterial;
import com.brt.productionflow.domain.ProductionFlowOrderRel;
import com.brt.productionflow.domain.ProductionFlowStep;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 生产流视图对象
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ProductionFlowVo extends ProductionFlow {

    private List<ProductionFlowMaterial> materialsSummary = new ArrayList<>();

    private List<ProductionFlowOrderRel> orderAllocations = new ArrayList<>();

    private List<String> orderIds = new ArrayList<>();

    private List<ProductionFlowStep> process = new ArrayList<>();

    private BrtFlowTemplateVo flowTemplate;
}
