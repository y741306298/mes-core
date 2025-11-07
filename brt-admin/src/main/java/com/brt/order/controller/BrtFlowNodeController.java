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
import com.brt.order.vo.BrtFlowNodeVo;
import com.brt.order.service.IBrtFlowNodeService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 模板节点Controller
 *
 * @author Fgn
 * @date 2024-04-30
 */
@RestController
@RequestMapping("/order/flowNode")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtFlowNodeController extends BaseController {

    private final IBrtFlowNodeService brtFlowNodeService;

    /**
     * @description: TODO 分页查询模板节点列表
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: brtFlowNodeVo 模板节点
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtFlowNodeVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:flowNode:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtFlowNodeVo> list(BrtFlowNodeVo brtFlowNodeVo) {
        return brtFlowNodeService.queryBrtFlowNodeList(brtFlowNodeVo);
    }

    /**
     * @description: TODO 查询全部模板节点列表
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: brtFlowNodeVo 模板节点
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:flowNode:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtFlowNodeVo brtFlowNodeVo) {
        return AjaxResult.success("查询成功", brtFlowNodeService.queryBrtFlowNodeAll(brtFlowNodeVo));
    }

    /**
     * @description: TODO 导出模板节点列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtFlowNodeVo 模板节点
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:flowNode:export')")
    @Log(title = "模板节点", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtFlowNodeVo brtFlowNodeVo){
        List<BrtFlowNodeVo> list = brtFlowNodeService.queryBrtFlowNodeAll(brtFlowNodeVo);
        ExcelUtil<BrtFlowNodeVo> util = new ExcelUtil<BrtFlowNodeVo>(BrtFlowNodeVo.class);
        util.exportExcel(response, list, "模板节点数据");
    }


    /**
     * @description: TODO 获取模板节点详细信息
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:flowNode:query')")
    @GetMapping(value = "/{nodeId}")
    public AjaxResult getInfo(@PathVariable("nodeId") String nodeId) {
        return success(brtFlowNodeService.queryBrtFlowNodeByNodeId(nodeId));
    }

    /**
     * @description: TODO 新增模板节点
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: brtFlowNodeVo 模板节点
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:flowNode:add')")
    @Log(title = "模板节点", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtFlowNodeVo brtFlowNodeVo) {
        return AjaxResult.success(brtFlowNodeService.insertBrtFlowNode(brtFlowNodeVo));
    }

    /**
     * @description: TODO 修改模板节点
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: brtFlowNodeVo 模板节点
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:flowNode:edit')")
    @Log(title = "模板节点", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtFlowNodeVo brtFlowNodeVo) {
        return AjaxResult.success(brtFlowNodeService.updateBrtFlowNode(brtFlowNodeVo));
    }

    /**
     * @description: TODO 删除模板节点
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: nodeIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:flowNode:remove')")
    @Log(title = "模板节点", businessType = BusinessType.DELETE)
	@DeleteMapping("/{nodeIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] nodeIds){
        return toAjax(brtFlowNodeService.deleteBrtFlowNodeByNodeIds(nodeIds));
    }

}
