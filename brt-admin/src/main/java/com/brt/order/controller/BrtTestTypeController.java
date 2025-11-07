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
import com.brt.order.vo.BrtTestTypeVo;
import com.brt.order.service.IBrtTestTypeService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 测试类型管理Controller
 *
 * @author Fgn
 * @date 2024-05-09
 */
@RestController
@RequestMapping("/order/testType")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtTestTypeController extends BaseController {

    private final IBrtTestTypeService brtTestTypeService;

    /**
     * @description: TODO 分页查询测试类型管理列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtTestTypeVo 测试类型管理
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtTestTypeVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:testType:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtTestTypeVo> list(BrtTestTypeVo brtTestTypeVo) {
        return brtTestTypeService.queryBrtTestTypeList(brtTestTypeVo);
    }

    /**
     * @description: TODO 查询全部测试类型管理列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtTestTypeVo 测试类型管理
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:testType:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtTestTypeVo brtTestTypeVo) {
        return AjaxResult.success("查询成功", brtTestTypeService.queryBrtTestTypeAll(brtTestTypeVo));
    }

    /**
     * @description: TODO 导出测试类型管理列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtTestTypeVo 测试类型管理
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:testType:export')")
    @Log(title = "测试类型管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtTestTypeVo brtTestTypeVo){
        List<BrtTestTypeVo> list = brtTestTypeService.queryBrtTestTypeAll(brtTestTypeVo);
        ExcelUtil<BrtTestTypeVo> util = new ExcelUtil<BrtTestTypeVo>(BrtTestTypeVo.class);
        util.exportExcel(response, list, "测试类型管理数据");
    }


    /**
     * @description: TODO 获取测试类型管理详细信息
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:testType:query')")
    @GetMapping(value = "/{typeId}")
    public AjaxResult getInfo(@PathVariable("typeId") String typeId) {
        return success(brtTestTypeService.queryBrtTestTypeByTypeId(typeId));
    }

    /**
     * @description: TODO 新增测试类型管理
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtTestTypeVo 测试类型管理
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:testType:add')")
    @Log(title = "测试类型管理", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtTestTypeVo brtTestTypeVo) {
        return AjaxResult.success(brtTestTypeService.insertBrtTestType(brtTestTypeVo));
    }

    /**
     * @description: TODO 修改测试类型管理
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtTestTypeVo 测试类型管理
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:testType:edit')")
    @Log(title = "测试类型管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtTestTypeVo brtTestTypeVo) {
        return AjaxResult.success(brtTestTypeService.updateBrtTestType(brtTestTypeVo));
    }

    /**
     * @description: TODO 删除测试类型管理
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: typeIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:testType:remove')")
    @Log(title = "测试类型管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{typeIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] typeIds){
        return toAjax(brtTestTypeService.deleteBrtTestTypeByTypeIds(typeIds));
    }

}
