package com.brt.order.controller;

import com.brt.common.annotation.RepeatSubmit;
import com.brt.common.annotation.RequestSingleParam;
import com.brt.common.enums.BusinessType;
import com.brt.order.domain.BrtOrderMaterielPlan;
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
import com.brt.order.vo.BrtOrderMaterielPlanVo;
import com.brt.order.service.IBrtOrderMaterielPlanService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 物料数量计划Controller
 *
 * @author Fgn
 * @date 2024-06-20
 */
@RestController
@RequestMapping("/order/orderMaterielPlan")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtOrderMaterielPlanController extends BaseController {

    private final IBrtOrderMaterielPlanService brtOrderMaterielPlanService;

    /**
     * @description: TODO 分页查询物料数量计划列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderMaterielPlanVo 物料数量计划
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtOrderMaterielPlanVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderMaterielPlan:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtOrderMaterielPlanVo> list(BrtOrderMaterielPlanVo brtOrderMaterielPlanVo) {
        return brtOrderMaterielPlanService.queryBrtOrderMaterielPlanList(brtOrderMaterielPlanVo);
    }

    /**
     * @description: TODO 查询全部物料数量计划列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderMaterielPlanVo 物料数量计划
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderMaterielPlan:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtOrderMaterielPlanVo brtOrderMaterielPlanVo) {
        return AjaxResult.success("查询成功", brtOrderMaterielPlanService.queryBrtOrderMaterielPlanAll(brtOrderMaterielPlanVo));
    }

    /**
     * @description: TODO 导出物料数量计划列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtOrderMaterielPlanVo 物料数量计划
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderMaterielPlan:export')")
    @Log(title = "物料数量计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtOrderMaterielPlanVo brtOrderMaterielPlanVo){
        List<BrtOrderMaterielPlanVo> list = brtOrderMaterielPlanService.queryBrtOrderMaterielPlanAll(brtOrderMaterielPlanVo);
        ExcelUtil<BrtOrderMaterielPlanVo> util = new ExcelUtil<BrtOrderMaterielPlanVo>(BrtOrderMaterielPlanVo.class);
        util.exportExcel(response, list, "物料数量计划数据");
    }


    /**
     * @description: TODO 获取物料数量计划详细信息
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderMaterielPlan:query')")
    @GetMapping(value = "/{planId}")
    public AjaxResult getInfo(@PathVariable("planId") String planId) {
        return success(brtOrderMaterielPlanService.queryBrtOrderMaterielPlanByPlanId(planId));
    }

    /**
     * @description: TODO 新增物料数量计划
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderMaterielPlanVo 物料数量计划
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderMaterielPlan:add')")
    @Log(title = "物料数量计划", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtOrderMaterielPlanVo brtOrderMaterielPlanVo) {
        return AjaxResult.success(brtOrderMaterielPlanService.insertBrtOrderMaterielPlan(brtOrderMaterielPlanVo));
    }

    /**
     * @description: TODO 修改物料数量计划
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderMaterielPlanVo 物料数量计划
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:orderMaterielPlan:edit')")
    @Log(title = "物料数量计划", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody List<BrtOrderMaterielPlan> orderMaterielPlanList) {
        return AjaxResult.success(brtOrderMaterielPlanService.updateBatchById(orderMaterielPlanList));
    }

    /**
     * @description: TODO 删除物料数量计划
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: planIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderMaterielPlan:remove')")
    @Log(title = "物料数量计划", businessType = BusinessType.DELETE)
	@DeleteMapping("/{planIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] planIds){
        return toAjax(brtOrderMaterielPlanService.deleteBrtOrderMaterielPlanByPlanIds(planIds));
    }

}
