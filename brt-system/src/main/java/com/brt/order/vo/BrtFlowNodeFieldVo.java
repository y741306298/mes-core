package com.brt.order.vo;

import com.brt.order.domain.BrtFlowNodeField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 节点字段Vo对象 brt_flow_node_field
 *
 * @author Fgn
 * @date 2024-04-30
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtFlowNodeFieldVo extends BrtFlowNodeField {

}
