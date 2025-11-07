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
import com.brt.order.vo.BrtCustomerTypeVo;
import com.brt.order.service.IBrtCustomerTypeService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 客户类型管理Controller
 *
 * @author Fgn
 * @date 2024-04-27
 */
@RestController
@RequestMapping("/order/customerType")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtCustomerTypeController extends BaseController {

    private final IBrtCustomerTypeService brtCustomerTypeService;

    /**
     * @description: TODO 分页查询客户类型管理列表
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerTypeVo 客户类型管理
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtCustomerTypeVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:customerType:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtCustomerTypeVo> list(BrtCustomerTypeVo brtCustomerTypeVo) {
        return brtCustomerTypeService.queryBrtCustomerTypeList(brtCustomerTypeVo);
    }

    /**
     * @description: TODO 查询全部客户类型管理列表
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerTypeVo 客户类型管理
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:customerType:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtCustomerTypeVo brtCustomerTypeVo) {
        return AjaxResult.success("查询成功", brtCustomerTypeService.queryBrtCustomerTypeAll(brtCustomerTypeVo));
    }

    /**
     * @description: TODO 导出客户类型管理列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtCustomerTypeVo 客户类型管理
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:customerType:export')")
    @Log(title = "客户类型管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtCustomerTypeVo brtCustomerTypeVo){
        List<BrtCustomerTypeVo> list = brtCustomerTypeService.queryBrtCustomerTypeAll(brtCustomerTypeVo);
        ExcelUtil<BrtCustomerTypeVo> util = new ExcelUtil<BrtCustomerTypeVo>(BrtCustomerTypeVo.class);
        util.exportExcel(response, list, "客户类型管理数据");
    }


    /**
     * @description: TODO 获取客户类型管理详细信息
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:customerType:query')")
    @GetMapping(value = "/{typeId}")
    public AjaxResult getInfo(@PathVariable("typeId") String typeId) {
        return success(brtCustomerTypeService.queryBrtCustomerTypeByTypeId(typeId));
    }

    /**
     * @description: TODO 新增客户类型管理
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerTypeVo 客户类型管理
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:customerType:add')")
    @Log(title = "客户类型管理", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtCustomerTypeVo brtCustomerTypeVo) {
        return AjaxResult.success(brtCustomerTypeService.insertBrtCustomerType(brtCustomerTypeVo));
    }

    /**
     * @description: TODO 修改客户类型管理
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerTypeVo 客户类型管理
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:customerType:edit')")
    @Log(title = "客户类型管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtCustomerTypeVo brtCustomerTypeVo) {
        return AjaxResult.success(brtCustomerTypeService.updateBrtCustomerType(brtCustomerTypeVo));
    }

    /**
     * @description: TODO 删除客户类型管理
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: typeIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:customerType:remove')")
    @Log(title = "客户类型管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{typeIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] typeIds){
        return toAjax(brtCustomerTypeService.deleteBrtCustomerTypeByTypeIds(typeIds));
    }

}
