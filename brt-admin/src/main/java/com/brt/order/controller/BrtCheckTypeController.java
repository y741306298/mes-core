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
import com.brt.order.vo.BrtCheckTypeVo;
import com.brt.order.service.IBrtCheckTypeService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 账单类型Controller
 *
 * @author Fgn
 * @date 2024-05-15
 */
@RestController
@RequestMapping("/order/checkType")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtCheckTypeController extends BaseController {

    private final IBrtCheckTypeService brtCheckTypeService;

    /**
     * @description: TODO 分页查询账单类型列表
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: brtCheckTypeVo 账单类型
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtCheckTypeVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:checkType:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtCheckTypeVo> list(BrtCheckTypeVo brtCheckTypeVo) {
        return brtCheckTypeService.queryBrtCheckTypeList(brtCheckTypeVo);
    }

    /**
     * @description: TODO 查询全部账单类型列表
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: brtCheckTypeVo 账单类型
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:checkType:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtCheckTypeVo brtCheckTypeVo) {
        return AjaxResult.success("查询成功", brtCheckTypeService.queryBrtCheckTypeAll(brtCheckTypeVo));
    }

    /**
     * @description: TODO 导出账单类型列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtCheckTypeVo 账单类型
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:checkType:export')")
    @Log(title = "账单类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtCheckTypeVo brtCheckTypeVo){
        List<BrtCheckTypeVo> list = brtCheckTypeService.queryBrtCheckTypeAll(brtCheckTypeVo);
        ExcelUtil<BrtCheckTypeVo> util = new ExcelUtil<BrtCheckTypeVo>(BrtCheckTypeVo.class);
        util.exportExcel(response, list, "账单类型数据");
    }


    /**
     * @description: TODO 获取账单类型详细信息
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:checkType:query')")
    @GetMapping(value = "/{typeId}")
    public AjaxResult getInfo(@PathVariable("typeId") String typeId) {
        return success(brtCheckTypeService.queryBrtCheckTypeByTypeId(typeId));
    }

    /**
     * @description: TODO 新增账单类型
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: brtCheckTypeVo 账单类型
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:checkType:add')")
    @Log(title = "账单类型", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtCheckTypeVo brtCheckTypeVo) {
        return AjaxResult.success(brtCheckTypeService.insertBrtCheckType(brtCheckTypeVo));
    }

    /**
     * @description: TODO 修改账单类型
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: brtCheckTypeVo 账单类型
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:checkType:edit')")
    @Log(title = "账单类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtCheckTypeVo brtCheckTypeVo) {
        return AjaxResult.success(brtCheckTypeService.updateBrtCheckType(brtCheckTypeVo));
    }

    /**
     * @description: TODO 删除账单类型
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: typeIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:checkType:remove')")
    @Log(title = "账单类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{typeIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] typeIds){
        return toAjax(brtCheckTypeService.deleteBrtCheckTypeByTypeIds(typeIds));
    }

}
