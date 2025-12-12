package com.brt.order.controller;

import com.brt.common.core.domain.AjaxResult;
import com.brt.common.utils.StringUtils;
import com.brt.productionflow.service.IOrderPoolService;
import com.brt.productionflow.vo.ProdApiQuery;
import com.brt.productionflow.vo.ProductionFlowVo;
import java.util.ArrayList;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/script/api")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtScriptController {

    private final IOrderPoolService orderPoolService;

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
        System.out.println(param);
        return AjaxResult.success();
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

}
