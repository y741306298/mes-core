package com.brt.order.vo;

import com.brt.common.core.domain.entity.SysUser;
import com.brt.order.domain.BrtFlowTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 流程模板Vo对象 brt_flow_template
 *
 * @author Fgn
 * @date 2024-04-30
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtFlowTemplateVo extends BrtFlowTemplate {

    //  节点列表
    private List<BrtFlowNodeVo> flowNodeList;

    // 负责人
    private SysUser dutyUser;

}
