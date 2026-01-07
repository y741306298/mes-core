package com.brt.productionflow.vo;

import com.brt.order.vo.BrtFlowTemplateVo;
import com.brt.productionflow.domain.ComposeFlow;
import com.brt.productionflow.domain.ComposeFlowMaterial;
import com.brt.productionflow.domain.ComposeFlowRel;
import com.brt.productionflow.domain.ComposeFlowStep;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 排版生产流视图对象
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ComposeFlowVo extends ComposeFlow {

    private List<ComposeFlowMaterial> materialsSummary = new ArrayList<>();

    private List<ComposeFlowRel> composeAllocations = new ArrayList<>();

    private List<String> composeIds = new ArrayList<>();

    private List<ComposeFlowStep> process = new ArrayList<>();

    private BrtFlowTemplateVo flowTemplate;
}
