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
import com.brt.order.vo.BrtFlowNodeFieldVo;
import com.brt.order.service.IBrtFlowNodeFieldService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 节点字段Controller
 *
 * @author Fgn
 * @date 2024-04-30
 */
@RestController
@RequestMapping("/order/flowNodeField")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtFlowNodeFieldController extends BaseController {

    private final IBrtFlowNodeFieldService brtFlowNodeFieldService;

    /**
     * @description: TODO 分页查询节点字段列表
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: brtFlowNodeFieldVo 节点字段
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtFlowNodeFieldVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:flowNodeField:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtFlowNodeFieldVo> list(BrtFlowNodeFieldVo brtFlowNodeFieldVo) {
        return brtFlowNodeFieldService.queryBrtFlowNodeFieldList(brtFlowNodeFieldVo);
    }

    /**
     * @description: TODO 查询全部节点字段列表
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: brtFlowNodeFieldVo 节点字段
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:flowNodeField:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtFlowNodeFieldVo brtFlowNodeFieldVo) {
        return AjaxResult.success("查询成功", brtFlowNodeFieldService.queryBrtFlowNodeFieldAll(brtFlowNodeFieldVo));
    }

    /**
     * @description: TODO 导出节点字段列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtFlowNodeFieldVo 节点字段
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:flowNodeField:export')")
    @Log(title = "节点字段", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtFlowNodeFieldVo brtFlowNodeFieldVo){
        List<BrtFlowNodeFieldVo> list = brtFlowNodeFieldService.queryBrtFlowNodeFieldAll(brtFlowNodeFieldVo);
        ExcelUtil<BrtFlowNodeFieldVo> util = new ExcelUtil<BrtFlowNodeFieldVo>(BrtFlowNodeFieldVo.class);
        util.exportExcel(response, list, "节点字段数据");
    }


    /**
     * @description: TODO 获取节点字段详细信息
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:flowNodeField:query')")
    @GetMapping(value = "/{fieldId}")
    public AjaxResult getInfo(@PathVariable("fieldId") String fieldId) {
        return success(brtFlowNodeFieldService.queryBrtFlowNodeFieldByFieldId(fieldId));
    }

    /**
     * @description: TODO 新增节点字段
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: brtFlowNodeFieldVo 节点字段
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:flowNodeField:add')")
    @Log(title = "节点字段", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtFlowNodeFieldVo brtFlowNodeFieldVo) {
        return AjaxResult.success(brtFlowNodeFieldService.insertBrtFlowNodeField(brtFlowNodeFieldVo));
    }

    /**
     * @description: TODO 修改节点字段
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: brtFlowNodeFieldVo 节点字段
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:flowNodeField:edit')")
    @Log(title = "节点字段", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtFlowNodeFieldVo brtFlowNodeFieldVo) {
        return AjaxResult.success(brtFlowNodeFieldService.updateBrtFlowNodeField(brtFlowNodeFieldVo));
    }

    /**
     * @description: TODO 删除节点字段
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: fieldIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:flowNodeField:remove')")
    @Log(title = "节点字段", businessType = BusinessType.DELETE)
	@DeleteMapping("/{fieldIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] fieldIds){
        return toAjax(brtFlowNodeFieldService.deleteBrtFlowNodeFieldByFieldIds(fieldIds));
    }

}
