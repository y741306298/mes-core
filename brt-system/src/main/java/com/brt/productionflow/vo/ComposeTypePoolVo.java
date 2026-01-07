package com.brt.productionflow.vo;

import com.brt.order.vo.BrtFlowTemplateVo;
import com.brt.order.vo.BrtOrderNodeVo;
import com.brt.order.vo.BrtOrderTemplateVo;
import com.brt.productionflow.domain.ComposeTypePool;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 排版池视图对象
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ComposeTypePoolVo extends ComposeTypePool {

    private BrtFlowTemplateVo flowTemplate;

    private BrtOrderTemplateVo orderTemplate;

    private java.util.List<BrtOrderNodeVo> orderNodes;
}
