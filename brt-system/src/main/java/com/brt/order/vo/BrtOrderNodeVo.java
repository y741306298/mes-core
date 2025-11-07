package com.brt.order.vo;

import java.util.Date;
import java.util.List;

import com.brt.common.core.domain.entity.SysUser;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.brt.order.domain.BrtOrderNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 订单流程节点Vo对象 brt_order_node
 *
 * @author Fgn
 * @date 2024-05-10
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtOrderNodeVo extends BrtOrderNode {

    public static final String ALL_LIST = "order:orderNode:allList";

    // 节点信息
    private BrtFlowNodeVo flowNodeVo;

    // 用户列表
    private List<SysUser> userList;

    // 订单模板
    private BrtOrderTemplateVo orderTemplateVo;

    // 用户信息
    private SysUser user;

    // 用户信息
    private SysUser nodePrincipalVo;

    // 是否过滤作废模板（Y=是，N=否）
    private String isFilterVoid;
}
