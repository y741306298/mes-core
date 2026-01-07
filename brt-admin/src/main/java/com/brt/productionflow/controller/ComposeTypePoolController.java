package com.brt.productionflow.controller;

import com.brt.common.annotation.Log;
import com.brt.common.core.controller.BaseController;
import com.brt.common.core.domain.AjaxResult;
import com.brt.common.enums.BusinessType;
import com.brt.productionflow.service.IComposeTypePoolService;
import com.brt.productionflow.vo.ComposeFlowQuery;
import com.brt.productionflow.vo.ComposeFlowVo;
import com.brt.productionflow.vo.ComposeTypePoolQuery;
import com.brt.productionflow.vo.ComposeTypePoolVo;
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
 * 排版池管理
 */
@RestController
@RequestMapping("/productionflow/composeTypePool")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ComposeTypePoolController extends BaseController {

    private final IComposeTypePoolService composeTypePoolService;

    /**
     * 查询排版池列表
     */
    @GetMapping("/list")
    public AjaxResult list(ComposeTypePoolQuery query) {
        List<ComposeTypePoolVo> data = composeTypePoolService.selectComposeTypePoolList(query);
        return AjaxResult.success(data);
    }

    /**
     * 查询排版详情
     */
    @GetMapping("/{composeId}")
    public AjaxResult getInfo(@PathVariable String composeId) {
        return AjaxResult.success(composeTypePoolService.selectComposeTypePoolById(composeId));
    }

    /**
     * 新增排版
     */
    @Log(title = "排版池", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ComposeTypePoolVo composeTypePoolVo) {
        return AjaxResult.success(composeTypePoolService.insertComposeTypePool(composeTypePoolVo));
    }

    /**
     * 修改排版
     */
    @Log(title = "排版池", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ComposeTypePoolVo composeTypePoolVo) {
        return AjaxResult.success(composeTypePoolService.updateComposeTypePool(composeTypePoolVo));
    }

    /**
     * 删除排版
     */
    @Log(title = "排版池", businessType = BusinessType.DELETE)
    @DeleteMapping("/{composeIds}")
    public AjaxResult remove(@PathVariable String[] composeIds) {
        return toAjax(composeTypePoolService.deleteComposeTypePoolByIds(composeIds));
    }

    /**
     * 清理排版相关流程数据
     */
    @Log(title = "排版池", businessType = BusinessType.DELETE)
    @DeleteMapping("/process/{composeIds}")
    public AjaxResult clearComposeProcesses(@PathVariable String[] composeIds) {
        return toAjax(composeTypePoolService.clearComposeProcessesByIds(composeIds));
    }

    /**
     * 查询排版生产流列表
     */
    @GetMapping("/flow/list")
    public AjaxResult flowList(ComposeFlowQuery query) {
        List<ComposeFlowVo> data = composeTypePoolService.selectComposeFlowList(query);
        return AjaxResult.success(data);
    }

    /**
     * 查询排版生产流详情
     */
    @GetMapping("/flow/{flowId}")
    public AjaxResult getFlow(@PathVariable String flowId) {
        return AjaxResult.success(composeTypePoolService.selectComposeFlowById(flowId));
    }

    /**
     * 新增排版生产流
     */
    @Log(title = "排版生产流", businessType = BusinessType.INSERT)
    @PostMapping("/flow")
    public AjaxResult addFlow(@RequestBody ComposeFlowVo composeFlowVo) {
        return AjaxResult.success(composeTypePoolService.insertComposeFlow(composeFlowVo));
    }

    /**
     * 修改排版生产流
     */
    @Log(title = "排版生产流", businessType = BusinessType.UPDATE)
    @PutMapping("/flow")
    public AjaxResult editFlow(@RequestBody ComposeFlowVo composeFlowVo) {
        return AjaxResult.success(composeTypePoolService.updateComposeFlow(composeFlowVo));
    }

    /**
     * 删除排版生产流
     */
    @Log(title = "排版生产流", businessType = BusinessType.DELETE)
    @DeleteMapping("/flow/{flowIds}")
    public AjaxResult removeFlow(@PathVariable String[] flowIds) {
        return toAjax(composeTypePoolService.deleteComposeFlowByIds(flowIds));
    }
}
