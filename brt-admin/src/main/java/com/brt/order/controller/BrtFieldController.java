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
import com.brt.order.vo.BrtFieldVo;
import com.brt.order.service.IBrtFieldService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 自定义字段Controller
 *
 * @author Fgn
 * @date 2024-06-15
 */
@RestController
@RequestMapping("/order/field")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtFieldController extends BaseController {

    private final IBrtFieldService brtFieldService;

    /**
     * @description: TODO 分页查询自定义字段列表
     * @author: Fgn
     * @date: 2024-06-15
     * @param:
     * @param: brtFieldVo 自定义字段
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtFieldVo>
     **/
    @PreAuthorize("@ss.hasPermi('order:field:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtFieldVo> list(BrtFieldVo brtFieldVo) {
        return brtFieldService.queryBrtFieldList(brtFieldVo);
    }

    /**
     * @description: TODO 查询全部自定义字段列表
     * @author: Fgn
     * @date: 2024-06-15
     * @param:
     * @param: brtFieldVo 自定义字段
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:field:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtFieldVo brtFieldVo) {
        return AjaxResult.success("查询成功", brtFieldService.queryBrtFieldAll(brtFieldVo));
    }

    /**
     * @description: TODO 导出自定义字段列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtFieldVo 自定义字段
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:field:export')")
    @Log(title = "自定义字段", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtFieldVo brtFieldVo){
        List<BrtFieldVo> list = brtFieldService.queryBrtFieldAll(brtFieldVo);
        ExcelUtil<BrtFieldVo> util = new ExcelUtil<BrtFieldVo>(BrtFieldVo.class);
        util.exportExcel(response, list, "自定义字段数据");
    }


    /**
     * @description: TODO 获取自定义字段详细信息
     * @author: Fgn
     * @date: 2024-06-15
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:field:query')")
    @GetMapping(value = "/{fieldId}")
    public AjaxResult getInfo(@PathVariable("fieldId") String fieldId) {
        return success(brtFieldService.queryBrtFieldByFieldId(fieldId));
    }

    /**
     * @description: TODO 新增自定义字段
     * @author: Fgn
     * @date: 2024-06-15
     * @param:
     * @param: brtFieldVo 自定义字段
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:field:add')")
    @Log(title = "自定义字段", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtFieldVo brtFieldVo) {
        return AjaxResult.success(brtFieldService.insertBrtField(brtFieldVo));
    }

    /**
     * @description: TODO 修改自定义字段
     * @author: Fgn
     * @date: 2024-06-15
     * @param:
     * @param: brtFieldVo 自定义字段
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:field:edit')")
    @Log(title = "自定义字段", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtFieldVo brtFieldVo) {
        return AjaxResult.success(brtFieldService.updateBrtField(brtFieldVo));
    }

    /**
     * @description: TODO 删除自定义字段
     * @author: Fgn
     * @date: 2024-06-15
     * @param:
     * @param: fieldIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:field:remove')")
    @Log(title = "自定义字段", businessType = BusinessType.DELETE)
	@DeleteMapping("/{fieldIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] fieldIds){
        return toAjax(brtFieldService.deleteBrtFieldByFieldIds(fieldIds));
    }

}
