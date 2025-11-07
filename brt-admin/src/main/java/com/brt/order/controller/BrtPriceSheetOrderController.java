package com.brt.order.controller;

import com.brt.common.annotation.RepeatSubmit;
import com.brt.common.annotation.RequestSingleParam;
import com.brt.common.enums.BusinessType;
import com.brt.order.vo.BrtCustomerVo;
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
import com.brt.order.vo.BrtPriceSheetOrderVo;
import com.brt.order.service.IBrtPriceSheetOrderService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 销售单Controller
 *
 * @author Fgn
 * @date 2024-05-09
 */
@RestController
@RequestMapping("/order/priceSheetOrder")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtPriceSheetOrderController extends BaseController {

    private final IBrtPriceSheetOrderService brtPriceSheetOrderService;

    /**
     * @description: TODO 分页查询销售单列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtPriceSheetOrderVo 销售单
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtPriceSheetOrderVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:priceSheetOrder:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtPriceSheetOrderVo> list(BrtPriceSheetOrderVo brtPriceSheetOrderVo) {
        return brtPriceSheetOrderService.queryBrtPriceSheetOrderList(brtPriceSheetOrderVo);
    }

    /**
     * @description: TODO 查询全部销售单列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtPriceSheetOrderVo 销售单
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:priceSheetOrder:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtPriceSheetOrderVo brtPriceSheetOrderVo) {
        return AjaxResult.success("查询成功", brtPriceSheetOrderService.queryBrtPriceSheetOrderAll(brtPriceSheetOrderVo));
    }

    /**
     * @description: TODO 导出销售单列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtPriceSheetOrderVo 销售单
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:priceSheetOrder:export')")
    @Log(title = "销售单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtPriceSheetOrderVo brtPriceSheetOrderVo){
        List<BrtPriceSheetOrderVo> list = brtPriceSheetOrderService.queryBrtPriceSheetOrderAll(brtPriceSheetOrderVo);
        ExcelUtil<BrtPriceSheetOrderVo> util = new ExcelUtil<BrtPriceSheetOrderVo>(BrtPriceSheetOrderVo.class);
        util.exportExcel(response, list, "销售单数据");
    }


    /**
     * @description: TODO 获取销售单详细信息
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:priceSheetOrder:query')")
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable("orderId") String orderId) {
        return success(brtPriceSheetOrderService.queryBrtPriceSheetOrderByOrderId(orderId));
    }

    /**
     * @description: TODO 新增销售单
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtPriceSheetOrderVo 销售单
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:priceSheetOrder:add')")
    @Log(title = "销售单", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtPriceSheetOrderVo brtPriceSheetOrderVo) {
        return AjaxResult.success(brtPriceSheetOrderService.insertBrtPriceSheetOrder(brtPriceSheetOrderVo));
    }

    /**
     * @description: TODO 修改销售单
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtPriceSheetOrderVo 销售单
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:priceSheetOrder:edit')")
    @Log(title = "销售单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtPriceSheetOrderVo brtPriceSheetOrderVo) {
        return AjaxResult.success(brtPriceSheetOrderService.updateBrtPriceSheetOrder(brtPriceSheetOrderVo));
    }

    /**
     * @description: TODO 删除销售单
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: orderIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:priceSheetOrder:remove')")
    @Log(title = "销售单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{orderIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] orderIds){
        return toAjax(brtPriceSheetOrderService.deleteBrtPriceSheetOrderByOrderIds(orderIds));
    }

    /**
     * @description: TODO 下载模板
     * @author: FanGN
     * @date: 01:28 2024/5/19
     * @param:
     * @param response
     * @return:
     **/
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<BrtPriceSheetOrderVo> util = new ExcelUtil<BrtPriceSheetOrderVo>(BrtPriceSheetOrderVo.class);
        util.importTemplateExcel(response, "销售单数据");
    }

    /**
     * @param customerId
     * @description: TODO 统计客户销售单信息
     * @author: FanGN
     * @date: 22:33 2024/5/19
     * @param:
     * @return:
     **/
    @GetMapping("/totalPriceSheetOrder")
    public AjaxResult totalPriceSheetOrder(String customerId) {
        return AjaxResult.success(brtPriceSheetOrderService.totalPriceSheetOrder(customerId));
    }

    /**
     * @description: TODO 复制销售单
     * @author: FanGN
     * @date: 16:55 2024/5/20
     * @param:
     * @param orderId
     * @return:
     **/
    @PostMapping("/copyPriceSheetOrder")
    public AjaxResult copyPriceSheetOrder(@RequestSingleParam("orderId") String orderId)
    {
        return AjaxResult.success(brtPriceSheetOrderService.copyPriceSheetOrder(orderId));
    }

    /**
     * @description: TODO 报价单去下单
     * @author: FanGN
     * @date: 16:55 2024/5/20
     * @param:
     * @param orderId
     * @return:
     **/
    @PostMapping("/priceSheetToSales")
    public AjaxResult priceSheetToSales(@RequestBody BrtPriceSheetOrderVo priceSheetOrderVo)
    {
        return AjaxResult.success(brtPriceSheetOrderService.priceSheetToSales(priceSheetOrderVo));
    }

    /**
     * @description: TODO 销售单导入报价单 查询报价单列表
     * @author: FanGN
     * @date: 16:55 2024/5/20
     * @param:
     * @param
     * @return:
     **/
    @PostMapping("/exportPriceList")
    public AjaxResult exportPriceList(@RequestSingleParam("orderNo") String orderNo)
    {
        return AjaxResult.success(brtPriceSheetOrderService.exportPriceList(orderNo));
    }

}
