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
import com.brt.order.vo.BrtOrderDynamicVo;
import com.brt.order.service.IBrtOrderDynamicService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 订单动态Controller
 *
 * @author Fgn
 * @date 2024-05-12
 */
@RestController
@RequestMapping("/order/orderDynamic")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtOrderDynamicController extends BaseController {

    private final IBrtOrderDynamicService brtOrderDynamicService;

    /**
     * @description: TODO 分页查询订单动态列表
     * @author: Fgn
     * @date: 2024-05-12
     * @param:
     * @param: brtOrderDynamicVo 订单动态
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtOrderDynamicVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderDynamic:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtOrderDynamicVo> list(BrtOrderDynamicVo brtOrderDynamicVo) {
        return brtOrderDynamicService.queryBrtOrderDynamicList(brtOrderDynamicVo);
    }

    /**
     * @description: TODO 查询全部订单动态列表
     * @author: Fgn
     * @date: 2024-05-12
     * @param:
     * @param: brtOrderDynamicVo 订单动态
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderDynamic:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtOrderDynamicVo brtOrderDynamicVo) {
        return AjaxResult.success("查询成功", brtOrderDynamicService.queryBrtOrderDynamicAll(brtOrderDynamicVo));
    }

    /**
     * @description: TODO 导出订单动态列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtOrderDynamicVo 订单动态
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderDynamic:export')")
    @Log(title = "订单动态", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtOrderDynamicVo brtOrderDynamicVo){
        List<BrtOrderDynamicVo> list = brtOrderDynamicService.queryBrtOrderDynamicAll(brtOrderDynamicVo);
        ExcelUtil<BrtOrderDynamicVo> util = new ExcelUtil<BrtOrderDynamicVo>(BrtOrderDynamicVo.class);
        util.exportExcel(response, list, "订单动态数据");
    }


    /**
     * @description: TODO 获取订单动态详细信息
     * @author: Fgn
     * @date: 2024-05-12
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderDynamic:query')")
    @GetMapping(value = "/{dynamicId}")
    public AjaxResult getInfo(@PathVariable("dynamicId") String dynamicId) {
        return success(brtOrderDynamicService.queryBrtOrderDynamicByDynamicId(dynamicId));
    }

    /**
     * @description: TODO 新增订单动态
     * @author: Fgn
     * @date: 2024-05-12
     * @param:
     * @param: brtOrderDynamicVo 订单动态
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderDynamic:add')")
    @Log(title = "订单动态", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtOrderDynamicVo brtOrderDynamicVo) {
        return AjaxResult.success(brtOrderDynamicService.insertBrtOrderDynamic(brtOrderDynamicVo));
    }

    /**
     * @description: TODO 修改订单动态
     * @author: Fgn
     * @date: 2024-05-12
     * @param:
     * @param: brtOrderDynamicVo 订单动态
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:orderDynamic:edit')")
    @Log(title = "订单动态", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtOrderDynamicVo brtOrderDynamicVo) {
        return AjaxResult.success(brtOrderDynamicService.updateBrtOrderDynamic(brtOrderDynamicVo));
    }

    /**
     * @description: TODO 删除订单动态
     * @author: Fgn
     * @date: 2024-05-12
     * @param:
     * @param: dynamicIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderDynamic:remove')")
    @Log(title = "订单动态", businessType = BusinessType.DELETE)
	@DeleteMapping("/{dynamicIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] dynamicIds){
        return toAjax(brtOrderDynamicService.deleteBrtOrderDynamicByDynamicIds(dynamicIds));
    }

}
