package com.brt.productionflow.controller;

import com.brt.common.annotation.Log;
import com.brt.common.core.controller.BaseController;
import com.brt.common.core.domain.AjaxResult;
import com.brt.common.enums.BusinessType;
import com.brt.productionflow.service.IOrderPoolService;
import com.brt.productionflow.vo.ProductionFlowQuery;
import com.brt.productionflow.vo.ProductionFlowVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 生产池管理
 */
@RestController
@RequestMapping("/productionflow/flowPool")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FlowPoolController extends BaseController {

    private final IOrderPoolService orderPoolService;

    /**
     * 查询生产流列表
     */
    @GetMapping("/list")
    public AjaxResult list(ProductionFlowQuery query) {
        List<ProductionFlowVo> data = orderPoolService.selectProductionFlowList(query);
        return AjaxResult.success(data);
    }

    /**
     * 查询生产流详情
     */
    @GetMapping("/{flowId}")
    public AjaxResult getInfo(@PathVariable String flowId) {
        return AjaxResult.success(orderPoolService.selectProductionFlowById(flowId));
    }

    /**
     * 新增生产流
     */
    @Log(title = "生产流", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProductionFlowVo productionFlowVo) {
        return AjaxResult.success(orderPoolService.insertProductionFlow(productionFlowVo));
    }

    /**
     * 修改生产流
     */
    @Log(title = "生产流", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProductionFlowVo productionFlowVo) {
        return AjaxResult.success(orderPoolService.updateProductionFlow(productionFlowVo));
    }

    /**
     * 删除生产流
     */
    @Log(title = "生产流", businessType = BusinessType.DELETE)
    @DeleteMapping("/{flowIds}")
    public AjaxResult remove(@PathVariable String[] flowIds) {
        return toAjax(orderPoolService.deleteProductionFlowByIds(flowIds));
    }
}
