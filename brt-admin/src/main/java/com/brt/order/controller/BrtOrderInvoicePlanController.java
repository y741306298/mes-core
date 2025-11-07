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
import com.brt.order.vo.BrtOrderInvoicePlanVo;
import com.brt.order.service.IBrtOrderInvoicePlanService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 开票计划Controller
 *
 * @author Fgn
 * @date 2024-06-19
 */
@RestController
@RequestMapping("/order/orderInvoicePlan")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtOrderInvoicePlanController extends BaseController {

    private final IBrtOrderInvoicePlanService brtOrderInvoicePlanService;

    /**
     * @description: TODO 分页查询开票计划列表
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @param: brtOrderInvoicePlanVo 开票计划
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtOrderInvoicePlanVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderInvoicePlan:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtOrderInvoicePlanVo> list(BrtOrderInvoicePlanVo brtOrderInvoicePlanVo) {
        return brtOrderInvoicePlanService.queryBrtOrderInvoicePlanList(brtOrderInvoicePlanVo);
    }

    /**
     * @description: TODO 查询全部开票计划列表
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @param: brtOrderInvoicePlanVo 开票计划
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderInvoicePlan:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtOrderInvoicePlanVo brtOrderInvoicePlanVo) {
        return AjaxResult.success("查询成功", brtOrderInvoicePlanService.queryBrtOrderInvoicePlanAll(brtOrderInvoicePlanVo));
    }

    /**
     * @description: TODO 导出开票计划列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtOrderInvoicePlanVo 开票计划
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderInvoicePlan:export')")
    @Log(title = "开票计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtOrderInvoicePlanVo brtOrderInvoicePlanVo){
        List<BrtOrderInvoicePlanVo> list = brtOrderInvoicePlanService.queryBrtOrderInvoicePlanAll(brtOrderInvoicePlanVo);
        ExcelUtil<BrtOrderInvoicePlanVo> util = new ExcelUtil<BrtOrderInvoicePlanVo>(BrtOrderInvoicePlanVo.class);
        util.exportExcel(response, list, "开票计划数据");
    }


    /**
     * @description: TODO 获取开票计划详细信息
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderInvoicePlan:query')")
    @GetMapping(value = "/{planId}")
    public AjaxResult getInfo(@PathVariable("planId") String planId) {
        return success(brtOrderInvoicePlanService.queryBrtOrderInvoicePlanByPlanId(planId));
    }

    /**
     * @description: TODO 新增开票计划
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @param: brtOrderInvoicePlanVo 开票计划
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderInvoicePlan:add')")
    @Log(title = "开票计划", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtOrderInvoicePlanVo brtOrderInvoicePlanVo) {
        return AjaxResult.success(brtOrderInvoicePlanService.insertBrtOrderInvoicePlan(brtOrderInvoicePlanVo));
    }

    /**
     * @description: TODO 修改开票计划
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @param: brtOrderInvoicePlanVo 开票计划
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:orderInvoicePlan:edit')")
    @Log(title = "开票计划", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtOrderInvoicePlanVo brtOrderInvoicePlanVo) {
        return AjaxResult.success(brtOrderInvoicePlanService.updateBrtOrderInvoicePlan(brtOrderInvoicePlanVo));
    }

    /**
     * @description: TODO 删除开票计划
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @param: planIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderInvoicePlan:remove')")
    @Log(title = "开票计划", businessType = BusinessType.DELETE)
	@DeleteMapping("/{planIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] planIds){
        return toAjax(brtOrderInvoicePlanService.deleteBrtOrderInvoicePlanByPlanIds(planIds));
    }

}
