package com.brt.order.vo;

import com.brt.order.domain.BrtOrderChildProcess;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 订单子流程Vo对象 brt_order_child_process
 *
 * @author Fgn
 * @date 2024-06-21
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtOrderChildProcessVo extends BrtOrderChildProcess {

    private BrtFlowTemplateVo flowTemplateVo;
    private String orderType;

}
