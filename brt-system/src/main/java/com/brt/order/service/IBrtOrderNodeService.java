package com.brt.order.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.enums.NodeTypeEnums;
import com.brt.order.domain.BrtOrderNode;
import com.brt.order.vo.BrtOrderNodeVo;

import java.util.List;

/**
 * 订单流程节点Service接口
 *
 * @author Fgn
 * @date 2024-05-10
 */
public interface IBrtOrderNodeService extends IService<BrtOrderNode> {

    /**
     * @description: TODO 分页查询订单流程节点列表
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: brtOrderNodeVo 订单流程节点
     * @return:
     * @return TableDataInfo<BrtOrderNodeVo>
     **/
    public TableDataInfo<BrtOrderNodeVo> queryBrtOrderNodeList(BrtOrderNodeVo brtOrderNodeVo);

    /**
     * @description: TODO 查询我的任务
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: brtOrderNodeVo 订单流程节点
     * @return:
     * @return TableDataInfo<BrtOrderNodeVo>
     **/
    public TableDataInfo<BrtOrderNodeVo> myTask(BrtOrderNodeVo brtOrderNodeVo);

    /**
     * @description: TODO 查询全部订单流程节点列表
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: brtOrderNodeVo 订单流程节点
     * @return:
     * @return java.util.List<BrtOrderNodeVo>
     **/
    public List<BrtOrderNodeVo> queryBrtOrderNodeAll(BrtOrderNodeVo brtOrderNodeVo);

    /**
     * @description: TODO 根据orderNodeId查询订单流程节点
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: orderNodeId
     * @return:
     * @return BrtOrderNodeVo
     **/
    public BrtOrderNodeVo queryBrtOrderNodeByOrderNodeId(String orderNodeId);

    /**
     * @description: TODO 新增订单流程节点
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: brtOrderNodeVo 订单流程节点
     * @return:
     * @return int
     **/
    public BrtOrderNodeVo insertBrtOrderNode(BrtOrderNodeVo brtOrderNodeVo);

    /**
     * @description: TODO 修改订单流程节点
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: brtOrderNodeVo 订单流程节点
     * @return:
     * @return int
     **/
    public BrtOrderNodeVo updateBrtOrderNode(BrtOrderNodeVo brtOrderNodeVo);

    /**
     * @description: TODO 批量删除订单流程节点
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param:  orderNodeIds 需要删除的订单流程节点主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtOrderNodeByOrderNodeIds(String[] orderNodeIds);

    /**
     * @description: TODO 完成节点
     * @author: FanGN
     * @date: 23:25 2024/6/19
     * @param:
     * @param orderId
     * @param orderNodeId
     * @return:
     * @return int
     **/
    int completeNode(String orderId, String orderNodeId, NodeTypeEnums nodeTypeEnums);

    /**
     * @description: TODO 查询节点是否允许提前执行
     * @author: FanGN
     * @date: 15:20 2024/6/24
     * @param:
     * @param orderNodeId
     * @return:
     * @return int
     **/
    int isAheadComplate(String orderNodeId);

    /**
     * 提交备注
     * @param orderId
     */
    void submitRemark(String orderId,String orderNodeId,String remark);

    /**
     * 审批拒绝
     * @param orderId
     * @param auditStatus
     */
    void onAudit(String orderId,String auditStatus);
}
