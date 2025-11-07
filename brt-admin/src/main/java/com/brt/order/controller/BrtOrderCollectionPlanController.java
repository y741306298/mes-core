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
import com.brt.order.vo.BrtOrderCollectionPlanVo;
import com.brt.order.service.IBrtOrderCollectionPlanService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 收款计划Controller
 *
 * @author Fgn
 * @date 2024-06-20
 */
@RestController
@RequestMapping("/order/orderCollectionPlan")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtOrderCollectionPlanController extends BaseController {

    private final IBrtOrderCollectionPlanService brtOrderCollectionPlanService;

    /**
     * @description: TODO 分页查询收款计划列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderCollectionPlanVo 收款计划
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtOrderCollectionPlanVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderCollectionPlan:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtOrderCollectionPlanVo> list(BrtOrderCollectionPlanVo brtOrderCollectionPlanVo) {
        return brtOrderCollectionPlanService.queryBrtOrderCollectionPlanList(brtOrderCollectionPlanVo);
    }

    /**
     * @description: TODO 查询全部收款计划列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderCollectionPlanVo 收款计划
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderCollectionPlan:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtOrderCollectionPlanVo brtOrderCollectionPlanVo) {
        return AjaxResult.success("查询成功", brtOrderCollectionPlanService.queryBrtOrderCollectionPlanAll(brtOrderCollectionPlanVo));
    }

    /**
     * @description: TODO 导出收款计划列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtOrderCollectionPlanVo 收款计划
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderCollectionPlan:export')")
    @Log(title = "收款计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtOrderCollectionPlanVo brtOrderCollectionPlanVo){
        List<BrtOrderCollectionPlanVo> list = brtOrderCollectionPlanService.queryBrtOrderCollectionPlanAll(brtOrderCollectionPlanVo);
        ExcelUtil<BrtOrderCollectionPlanVo> util = new ExcelUtil<BrtOrderCollectionPlanVo>(BrtOrderCollectionPlanVo.class);
        util.exportExcel(response, list, "收款计划数据");
    }


    /**
     * @description: TODO 获取收款计划详细信息
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderCollectionPlan:query')")
    @GetMapping(value = "/{planId}")
    public AjaxResult getInfo(@PathVariable("planId") String planId) {
        return success(brtOrderCollectionPlanService.queryBrtOrderCollectionPlanByPlanId(planId));
    }

    /**
     * @description: TODO 新增收款计划
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderCollectionPlanVo 收款计划
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderCollectionPlan:add')")
    @Log(title = "收款计划", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtOrderCollectionPlanVo brtOrderCollectionPlanVo) {
        return AjaxResult.success(brtOrderCollectionPlanService.insertBrtOrderCollectionPlan(brtOrderCollectionPlanVo));
    }

    /**
     * @description: TODO 修改收款计划
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderCollectionPlanVo 收款计划
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:orderCollectionPlan:edit')")
    @Log(title = "收款计划", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtOrderCollectionPlanVo brtOrderCollectionPlanVo) {
        return AjaxResult.success(brtOrderCollectionPlanService.updateBrtOrderCollectionPlan(brtOrderCollectionPlanVo));
    }

    /**
     * @description: TODO 删除收款计划
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: planIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderCollectionPlan:remove')")
    @Log(title = "收款计划", businessType = BusinessType.DELETE)
	@DeleteMapping("/{planIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] planIds){
        return toAjax(brtOrderCollectionPlanService.deleteBrtOrderCollectionPlanByPlanIds(planIds));
    }

}
