package com.brt.order.vo;

import com.brt.common.core.domain.entity.SysUser;
import com.brt.order.domain.BrtOrderDynamic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 订单动态Vo对象 brt_order_dynamic
 *
 * @author Fgn
 * @date 2024-05-12
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtOrderDynamicVo extends BrtOrderDynamic {

    // 用户信息
    private SysUser user;

    // 节点信息
    private BrtFlowNodeVo flowNodeVo;

}
