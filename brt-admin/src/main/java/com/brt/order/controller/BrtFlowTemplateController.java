package com.brt.order.controller;

import com.brt.common.annotation.RepeatSubmit;
import com.brt.common.annotation.RequestSingleParam;
import com.brt.common.enums.BusinessType;
import lombok.RequiredArgsConstructor;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.brt.common.annotation.Log;
import com.brt.common.core.controller.BaseController;
import com.brt.common.core.domain.AjaxResult;
import com.brt.order.vo.BrtFlowTemplateVo;
import com.brt.order.service.IBrtFlowTemplateService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 流程模板Controller
 *
 * @author Fgn
 * @date 2024-04-30
 */
@RestController
@RequestMapping("/order/flowTemplate")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtFlowTemplateController extends BaseController {

    private final IBrtFlowTemplateService brtFlowTemplateService;

    /**
     * @description: TODO 分页查询流程模板列表
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: brtFlowTemplateVo 流程模板
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtFlowTemplateVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:flowTemplate:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtFlowTemplateVo> list(BrtFlowTemplateVo brtFlowTemplateVo) {
        return brtFlowTemplateService.queryBrtFlowTemplateList(brtFlowTemplateVo);
    }

    /**
     * @description: TODO 查询全部流程模板列表
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: brtFlowTemplateVo 流程模板
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:flowTemplate:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtFlowTemplateVo brtFlowTemplateVo) {
        return AjaxResult.success("查询成功", brtFlowTemplateService.queryBrtFlowTemplateAll(brtFlowTemplateVo));
    }

    /**
     * @description: TODO 导出流程模板列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtFlowTemplateVo 流程模板
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:flowTemplate:export')")
    @Log(title = "流程模板", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtFlowTemplateVo brtFlowTemplateVo){
        List<BrtFlowTemplateVo> list = brtFlowTemplateService.queryBrtFlowTemplateAll(brtFlowTemplateVo);
        ExcelUtil<BrtFlowTemplateVo> util = new ExcelUtil<BrtFlowTemplateVo>(BrtFlowTemplateVo.class);
        util.exportExcel(response, list, "流程模板数据");
    }


    /**
     * @description: TODO 获取流程模板详细信息
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:flowTemplate:query')")
    @GetMapping(value = "/{templateId}")
    public AjaxResult getInfo(@PathVariable("templateId") String templateId) {
        return success(brtFlowTemplateService.queryBrtFlowTemplateByTemplateId(templateId));
    }

    /**
     * @description: TODO 新增流程模板
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: brtFlowTemplateVo 流程模板
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:flowTemplate:add')")
    @Log(title = "流程模板", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtFlowTemplateVo brtFlowTemplateVo) {
        return AjaxResult.success(brtFlowTemplateService.insertBrtFlowTemplate(brtFlowTemplateVo));
    }

    /**
     * @description: TODO 修改流程模板
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: brtFlowTemplateVo 流程模板
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:flowTemplate:edit')")
    @Log(title = "流程模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtFlowTemplateVo brtFlowTemplateVo) {
        return AjaxResult.success(brtFlowTemplateService.updateBrtFlowTemplate(brtFlowTemplateVo));
    }

    /**
     * @description: TODO 删除流程模板
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: templateIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:flowTemplate:remove')")
    @Log(title = "流程模板", businessType = BusinessType.DELETE)
	@DeleteMapping("/{templateIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] templateIds){
        return toAjax(brtFlowTemplateService.deleteBrtFlowTemplateByTemplateIds(templateIds));
    }

    /**
     * @description: TODO 复制模板
     * @author: FanGN
     * @date: 12:00 2024/4/30
     * @param:
     * @param templateId
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:flowTemplate:copy')")
    @Log(title = "流程模板", businessType = BusinessType.INSERT)
    @PostMapping("/copy")
    @RepeatSubmit
    public AjaxResult copy(@RequestSingleParam("templateId") String templateId){
        ;
        return AjaxResult.success(brtFlowTemplateService.copyTemplate(templateId));
    }

}
