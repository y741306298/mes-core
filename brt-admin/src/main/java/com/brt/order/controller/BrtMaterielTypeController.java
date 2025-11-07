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
import com.brt.order.vo.BrtMaterielTypeVo;
import com.brt.order.service.IBrtMaterielTypeService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 物料类型Controller
 *
 * @author Fgn
 * @date 2024-05-07
 */
@RestController
@RequestMapping("/order/materielType")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtMaterielTypeController extends BaseController {

    private final IBrtMaterielTypeService brtMaterielTypeService;

    /**
     * @description: TODO 分页查询物料类型列表
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @param: brtMaterielTypeVo 物料类型
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtMaterielTypeVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:materielType:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtMaterielTypeVo> list(BrtMaterielTypeVo brtMaterielTypeVo) {
        return brtMaterielTypeService.queryBrtMaterielTypeList(brtMaterielTypeVo);
    }

    /**
     * @description: TODO 查询全部物料类型列表
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @param: brtMaterielTypeVo 物料类型
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:materielType:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtMaterielTypeVo brtMaterielTypeVo) {
        return AjaxResult.success("查询成功", brtMaterielTypeService.queryBrtMaterielTypeAll(brtMaterielTypeVo));
    }

    /**
     * @description: TODO 导出物料类型列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtMaterielTypeVo 物料类型
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:materielType:export')")
    @Log(title = "物料类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtMaterielTypeVo brtMaterielTypeVo){
        List<BrtMaterielTypeVo> list = brtMaterielTypeService.queryBrtMaterielTypeAll(brtMaterielTypeVo);
        ExcelUtil<BrtMaterielTypeVo> util = new ExcelUtil<BrtMaterielTypeVo>(BrtMaterielTypeVo.class);
        util.exportExcel(response, list, "物料类型数据");
    }


    /**
     * @description: TODO 获取物料类型详细信息
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:materielType:query')")
    @GetMapping(value = "/{typeId}")
    public AjaxResult getInfo(@PathVariable("typeId") String typeId) {
        return success(brtMaterielTypeService.queryBrtMaterielTypeByTypeId(typeId));
    }

    /**
     * @description: TODO 新增物料类型
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @param: brtMaterielTypeVo 物料类型
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:materielType:add')")
    @Log(title = "物料类型", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtMaterielTypeVo brtMaterielTypeVo) {
        return AjaxResult.success(brtMaterielTypeService.insertBrtMaterielType(brtMaterielTypeVo));
    }

    /**
     * @description: TODO 修改物料类型
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @param: brtMaterielTypeVo 物料类型
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:materielType:edit')")
    @Log(title = "物料类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtMaterielTypeVo brtMaterielTypeVo) {
        return AjaxResult.success(brtMaterielTypeService.updateBrtMaterielType(brtMaterielTypeVo));
    }

    /**
     * @description: TODO 删除物料类型
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @param: typeIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:materielType:remove')")
    @Log(title = "物料类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{typeIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] typeIds){
        return toAjax(brtMaterielTypeService.deleteBrtMaterielTypeByTypeIds(typeIds));
    }

}
