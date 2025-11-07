package com.brt.order.vo;

import com.brt.order.domain.BrtFlowNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 模板节点Vo对象 brt_flow_node
 *
 * @author Fgn
 * @date 2024-04-30
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtFlowNodeVo extends BrtFlowNode {

    // 字段列表
    private List<BrtFlowNodeFieldVo> flowNodeFieldList;

}
