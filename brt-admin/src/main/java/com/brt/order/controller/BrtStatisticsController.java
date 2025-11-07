package com.brt.order.controller;

import com.brt.common.core.domain.AjaxResult;
import com.brt.order.service.IBrtStatisticsService;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order/statistics")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtStatisticsController {

    @Autowired
    private IBrtStatisticsService statisticsService;

    /**
     * 销售统计
     * @param map
     * @return
     */
    @PostMapping("salesStatistics")
    public AjaxResult salesStatistics(@RequestBody Map<String,Object> map){
        return AjaxResult.success(statisticsService.salesStatistics(map));
    }

    /**
     * 采购统计
     * @param map
     * @return
     */
    @PostMapping("marketStatistics")
    public AjaxResult marketStatistics(@RequestBody Map<String,Object> map){
        return AjaxResult.success(statisticsService.marketStatistics(map));
    }

    /**
     * 收支年报
     * @param map
     * @return
     */
    @PostMapping("earning")
    public AjaxResult earning(@RequestBody Map<String,Object> map){
        return AjaxResult.success(statisticsService.earning(map));
    }

    /**
     * 各部门完成情况
     * @param map
     * @return
     */
    @PostMapping("deptOnTime")
    public AjaxResult deptOnTime(@RequestBody Map<String,Object> map){
        return AjaxResult.success(statisticsService.deptOnTime(map));
    }

    /**
     * 查询首页头部数据
     * @return
     */
    @PostMapping("getHederData")
    public AjaxResult getHederData(){
        return AjaxResult.success(statisticsService.getHederData());
    }

    @GetMapping("picking")
    public AjaxResult picking(String orderId){
        return AjaxResult.success(statisticsService.picking(orderId));
    }

    @GetMapping("inInventory")
    public AjaxResult inInventory(String inInventoryId){
        return AjaxResult.success(statisticsService.inInventory(inInventoryId));
    }

    @GetMapping("outInventory")
    public AjaxResult outInventory(String outInventoryId){
        return AjaxResult.success(statisticsService.outInventory(outInventoryId));
    }

    @GetMapping("productInInventory")
    public AjaxResult productInInventory(String orderId){
        return AjaxResult.success(statisticsService.productInInventory(orderId));
    }

    @GetMapping("firstarticle")
    public AjaxResult firstarticle(String orderId){
        return AjaxResult.success(statisticsService.firstarticle(orderId));
    }

    @GetMapping("product")
    public AjaxResult product(String orderId){
        return AjaxResult.success(statisticsService.product(orderId));
    }
}
