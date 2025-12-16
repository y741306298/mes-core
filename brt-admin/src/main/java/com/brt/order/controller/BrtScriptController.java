package com.brt.order.controller;

import com.brt.common.core.domain.AjaxResult;
import com.brt.common.utils.StringUtils;
import com.brt.common.constant.HttpStatus;
import com.brt.productionflow.service.IOrderPoolService;
import com.brt.productionflow.vo.ProdApiQuery;
import com.brt.productionflow.vo.ProductionFlowVo;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.brt.common.enums.NodeStatusEnums;
import com.brt.order.domain.BrtOrderNode;
import com.brt.order.mapper.BrtOrderNodeMapper;
import com.brt.order.service.IBrtOrderNodeService;
import com.brt.order.vo.BrtOrderNodeVo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/script/api")
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtScriptController {

    private final IOrderPoolService orderPoolService;
    private final IBrtOrderNodeService orderNodeService;
    private final BrtOrderNodeMapper orderNodeMapper;
    @Qualifier("scheduledExecutorService")
    private final ScheduledExecutorService scheduledExecutorService;

    @PostMapping("/first")
    public AjaxResult first(){
        return AjaxResult.success();
    }

    @PostMapping("/second")
    public AjaxResult second(){
        return AjaxResult.success();
    }

    @PostMapping("/flowFirst")
    public AjaxResult flowFirst(@RequestBody ProdApiQuery param){
        System.out.println(param);
        return AjaxResult.success();
    }

    @PostMapping("/flowSecond")
    public AjaxResult flowSecond(@RequestBody ProdApiQuery param){
        String orderId = param == null ? null : param.getOrderId();
        if (StringUtils.isBlank(orderId)) {
            return AjaxResult.error("orderId不能为空");
        }

        BrtOrderNodeVo currentNode = orderNodeMapper.selectNowOrderNodeByOrderId(orderId);
        if (currentNode == null || StringUtils.isBlank(currentNode.getOrderNodeId())) {
            return AjaxResult.error("未找到对应的订单流程节点");
        }

        boolean started = orderNodeService.update(new LambdaUpdateWrapper<BrtOrderNode>()
                .set(BrtOrderNode::getNodeStatus, NodeStatusEnums.进行中.getCode())
                .eq(BrtOrderNode::getOrderNodeId, currentNode.getOrderNodeId()));
        if (!started) {
            return AjaxResult.error("更新节点状态失败");
        }

        ProdApiQuery recallParam = buildRecallParam(param, currentNode);
        scheduledExecutorService.schedule(() -> {
            try {
                AjaxResult recallResult = handleFlowSecondRecall(recallParam);
                if (!Objects.equals(HttpStatus.SUCCESS, recallResult.get(AjaxResult.CODE_TAG))) {
                    log.warn("flowSecondRecall执行失败，orderId={}，原因:{}", orderId, recallResult.get(AjaxResult.MSG_TAG));
                }
            } catch (Exception ex) {
                log.error("flowSecondRecall异步执行异常, orderId={}", orderId, ex);
            }
        }, 1, TimeUnit.MINUTES);

        return AjaxResult.success();
    }

    @PostMapping("/flowSecondRecall")
    public AjaxResult flowSecondRecall(@RequestBody ProdApiQuery param){
        return handleFlowSecondRecall(param);
    }

    @PostMapping("/flowThird")
    public AjaxResult flowThird(@RequestBody ProdApiQuery param){
        String orderId = param == null ? null : param.getOrderId();
        if (StringUtils.isBlank(orderId)) {
            return AjaxResult.error("orderId不能为空");
        }

        String oldFlowId = "FLOW-20251209-165944";
        String newFlowId = "FLOW-20251212-161336";

        ProductionFlowVo oldFlow = orderPoolService.selectProductionFlowById(oldFlowId);
        ProductionFlowVo newFlow = orderPoolService.selectProductionFlowById(newFlowId);

        if (oldFlow == null || newFlow == null) {
            return AjaxResult.error("生产池不存在");
        }

        boolean needUpdateOld = oldFlow.getOrderIds() != null && oldFlow.getOrderIds().remove(orderId);
        if (needUpdateOld) {
            orderPoolService.updateProductionFlow(oldFlow);
        }

        if (newFlow.getOrderIds() == null) {
            newFlow.setOrderIds(new ArrayList<>());
        }
        if (!newFlow.getOrderIds().contains(orderId)) {
            newFlow.getOrderIds().add(orderId);
        }

        ProductionFlowVo updatedFlow = orderPoolService.updateProductionFlow(newFlow);
        orderPoolService.applyFlowTemplates(newFlowId, Collections.singletonList(orderId));
        return AjaxResult.success(updatedFlow);
    }

    private AjaxResult handleFlowSecondRecall(ProdApiQuery param) {
        String orderId = param == null ? null : param.getOrderId();
        if (StringUtils.isBlank(orderId)) {
            return AjaxResult.error("orderId不能为空");
        }

        LambdaUpdateWrapper<BrtOrderNode> wrapper = new LambdaUpdateWrapper<BrtOrderNode>()
                .set(BrtOrderNode::getNodeStatus, NodeStatusEnums.已完成.getCode())
                .eq(BrtOrderNode::getOrderId, orderId)
                .eq(BrtOrderNode::getNodeStatus, NodeStatusEnums.进行中.getCode());
        if (param != null && StringUtils.isNotBlank(param.getNodeId())) {
            wrapper.eq(BrtOrderNode::getNodeId, param.getNodeId());
        }

        boolean completed = orderNodeService.update(wrapper);
        if (!completed) {
            return AjaxResult.error("未找到进行中的节点");
        }
        return AjaxResult.success();
    }

    private ProdApiQuery buildRecallParam(ProdApiQuery originalParam, BrtOrderNodeVo currentNode) {
        ProdApiQuery recallParam = new ProdApiQuery();
        recallParam.setOrderId(originalParam.getOrderId());
        recallParam.setFlowId(originalParam.getFlowId());
        if (StringUtils.isNotBlank(originalParam.getNodeId())) {
            recallParam.setNodeId(originalParam.getNodeId());
        } else if (currentNode != null) {
            recallParam.setNodeId(currentNode.getNodeId());
        }
        return recallParam;
    }

}
