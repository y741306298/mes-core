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
import com.brt.order.vo.BrtPackageTypeVo;
import com.brt.order.service.IBrtPackageTypeService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 封装类型管理Controller
 *
 * @author Fgn
 * @date 2024-05-09
 */
@RestController
@RequestMapping("/order/packageType")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtPackageTypeController extends BaseController {

    private final IBrtPackageTypeService brtPackageTypeService;

    /**
     * @description: TODO 分页查询封装类型管理列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtPackageTypeVo 封装类型管理
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtPackageTypeVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:packageType:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtPackageTypeVo> list(BrtPackageTypeVo brtPackageTypeVo) {
        return brtPackageTypeService.queryBrtPackageTypeList(brtPackageTypeVo);
    }

    /**
     * @description: TODO 查询全部封装类型管理列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtPackageTypeVo 封装类型管理
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:packageType:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtPackageTypeVo brtPackageTypeVo) {
        return AjaxResult.success("查询成功", brtPackageTypeService.queryBrtPackageTypeAll(brtPackageTypeVo));
    }

    /**
     * @description: TODO 导出封装类型管理列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtPackageTypeVo 封装类型管理
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:packageType:export')")
    @Log(title = "封装类型管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtPackageTypeVo brtPackageTypeVo){
        List<BrtPackageTypeVo> list = brtPackageTypeService.queryBrtPackageTypeAll(brtPackageTypeVo);
        ExcelUtil<BrtPackageTypeVo> util = new ExcelUtil<BrtPackageTypeVo>(BrtPackageTypeVo.class);
        util.exportExcel(response, list, "封装类型管理数据");
    }


    /**
     * @description: TODO 获取封装类型管理详细信息
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:packageType:query')")
    @GetMapping(value = "/{typeId}")
    public AjaxResult getInfo(@PathVariable("typeId") String typeId) {
        return success(brtPackageTypeService.queryBrtPackageTypeByTypeId(typeId));
    }

    /**
     * @description: TODO 新增封装类型管理
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtPackageTypeVo 封装类型管理
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:packageType:add')")
    @Log(title = "封装类型管理", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtPackageTypeVo brtPackageTypeVo) {
        return AjaxResult.success(brtPackageTypeService.insertBrtPackageType(brtPackageTypeVo));
    }

    /**
     * @description: TODO 修改封装类型管理
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtPackageTypeVo 封装类型管理
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:packageType:edit')")
    @Log(title = "封装类型管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtPackageTypeVo brtPackageTypeVo) {
        return AjaxResult.success(brtPackageTypeService.updateBrtPackageType(brtPackageTypeVo));
    }

    /**
     * @description: TODO 删除封装类型管理
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: typeIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:packageType:remove')")
    @Log(title = "封装类型管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{typeIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] typeIds){
        return toAjax(brtPackageTypeService.deleteBrtPackageTypeByTypeIds(typeIds));
    }

}
