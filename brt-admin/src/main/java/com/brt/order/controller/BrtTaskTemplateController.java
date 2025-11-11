package com.brt.order.controller;

import cn.hutool.core.util.StrUtil;
import com.brt.common.annotation.Log;
import com.brt.common.core.controller.BaseController;
import com.brt.common.core.domain.AjaxResult;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.enums.BusinessType;
import com.brt.order.service.IBrtTaskTemplateService;
import com.brt.order.vo.BrtTaskTemplateVo;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 任务模板Controller
 */
@RestController
@RequestMapping("/order/taskTemplate")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtTaskTemplateController extends BaseController {

    private final IBrtTaskTemplateService taskTemplateService;

    private final IOrderPoolService orderPoolService;

    /**
     * 分页查询任务模板列表
     */
    @GetMapping("/list")
    public TableDataInfo<BrtTaskTemplateVo> list(BrtTaskTemplateVo query) {
        return taskTemplateService.queryTaskTemplateList(query);
    }

    /**
     * 查询全部任务模板
     */
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtTaskTemplateVo query) {
        return AjaxResult.success("查询成功", taskTemplateService.queryTaskTemplateAll(query));
    }

    /**
     * 获取任务模板详情
     */
    @GetMapping("/{templateId}")
    public AjaxResult getInfo(@PathVariable String templateId) {
        return AjaxResult.success(taskTemplateService.queryTaskTemplateById(templateId));
    }

    /**
     * 新增任务模板
     */
    @Log(title = "任务模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BrtTaskTemplateVo vo) {
        return AjaxResult.success(taskTemplateService.insertTaskTemplate(vo));
    }

    /**
     * 修改任务模板
     */
    @Log(title = "任务模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtTaskTemplateVo vo) {
        return AjaxResult.success(taskTemplateService.updateTaskTemplate(vo));
    }

    /**
     * 删除任务模板
     */
    @Log(title = "任务模板", businessType = BusinessType.DELETE)
    @DeleteMapping("/{templateIds}")
    public AjaxResult remove(@PathVariable String[] templateIds) {
        return toAjax(taskTemplateService.deleteTaskTemplateByIds(templateIds));
    }

    /**
     * 查询功能组合可选卡片
     */
    @GetMapping("/functionCard/list")
    public AjaxResult listFunctionCards() {
        ProductionFlowQuery query = new ProductionFlowQuery();
        List<ProductionFlowVo> flows = orderPoolService.selectProductionFlowList(query);
        List<Map<String, Object>> cards = (flows == null ? Collections.<ProductionFlowVo>emptyList() : flows)
            .stream()
            .map(this::buildFunctionCard)
            .collect(Collectors.toList());
        return AjaxResult.success(cards);
    }

    private Map<String, Object> buildFunctionCard(ProductionFlowVo flow) {
        Map<String, Object> card = new HashMap<>(16);
        if (flow == null) {
            return card;
        }
        String cardId = flow.getFlowId();
        String cardName = cardId;
        if (flow.getFlowTemplate() != null && StrUtil.isNotBlank(flow.getFlowTemplate().getTemplateName())) {
            cardName = flow.getFlowTemplate().getTemplateName();
        }
        String description = StrUtil.blankToDefault(flow.getProductionNotes(), cardName);
        card.put("cardId", cardId);
        card.put("cardCode", cardId);
        card.put("cardName", cardName);
        card.put("description", description);
        card.put("flowStatus", flow.getFlowStatus());
        card.put("priority", flow.getPriority());
        card.put("orderIds", flow.getOrderIds());
        card.put("process", flow.getProcess());
        card.put("materialsSummary", flow.getMaterialsSummary());
        card.put("assignedOperator", flow.getAssignedOperator());
        card.put("templateId", flow.getTemplateId());
        return card;
    }
}
