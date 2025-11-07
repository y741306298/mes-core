package com.brt.productionflow.controller;

import com.brt.common.annotation.Log;
import com.brt.common.core.controller.BaseController;
import com.brt.common.core.domain.AjaxResult;
import com.brt.common.enums.BusinessType;
import com.brt.productionflow.service.IOrderPoolService;
import com.brt.productionflow.vo.OrderPoolQuery;
import com.brt.productionflow.vo.OrderPoolVo;
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
 * 订单池管理
 */
@RestController
@RequestMapping("/productionflow/orderPool")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrderPoolController extends BaseController {

    private final IOrderPoolService orderPoolService;

    /**
     * 查询订单池列表
     */
    @GetMapping("/list")
    public AjaxResult list(OrderPoolQuery query) {
        List<OrderPoolVo> data = orderPoolService.selectOrderPoolList(query);
        return AjaxResult.success(data);
    }

    /**
     * 查询订单详情
     */
    @GetMapping("/{orderId}")
    public AjaxResult getInfo(@PathVariable String orderId) {
        return AjaxResult.success(orderPoolService.selectOrderPoolById(orderId));
    }

    /**
     * 新增订单
     */
    @Log(title = "订单池", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OrderPoolVo orderPoolVo) {
        return AjaxResult.success(orderPoolService.insertOrderPool(orderPoolVo));
    }

    /**
     * 修改订单
     */
    @Log(title = "订单池", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OrderPoolVo orderPoolVo) {
        return AjaxResult.success(orderPoolService.updateOrderPool(orderPoolVo));
    }

    /**
     * 删除订单
     */
    @Log(title = "订单池", businessType = BusinessType.DELETE)
    @DeleteMapping("/{orderIds}")
    public AjaxResult remove(@PathVariable String[] orderIds) {
        return toAjax(orderPoolService.deleteOrderPoolByIds(orderIds));
    }

    /**
     * 查询生产流列表
     */
    @GetMapping("/flow/list")
    public AjaxResult flowList() {
        List<ProductionFlowVo> data = orderPoolService.selectProductionFlowList();
        return AjaxResult.success(data);
    }

    /**
     * 查询生产流详情
     */
    @GetMapping("/flow/{flowId}")
    public AjaxResult getFlow(@PathVariable String flowId) {
        return AjaxResult.success(orderPoolService.selectProductionFlowById(flowId));
    }

    /**
     * 新增生产流
     */
    @Log(title = "生产流", businessType = BusinessType.INSERT)
    @PostMapping("/flow")
    public AjaxResult addFlow(@RequestBody ProductionFlowVo productionFlowVo) {
        return AjaxResult.success(orderPoolService.insertProductionFlow(productionFlowVo));
    }

    /**
     * 修改生产流
     */
    @Log(title = "生产流", businessType = BusinessType.UPDATE)
    @PutMapping("/flow")
    public AjaxResult editFlow(@RequestBody ProductionFlowVo productionFlowVo) {
        return AjaxResult.success(orderPoolService.updateProductionFlow(productionFlowVo));
    }

    /**
     * 删除生产流
     */
    @Log(title = "生产流", businessType = BusinessType.DELETE)
    @DeleteMapping("/flow/{flowIds}")
    public AjaxResult removeFlow(@PathVariable String[] flowIds) {
        return toAjax(orderPoolService.deleteProductionFlowByIds(flowIds));
    }
}
