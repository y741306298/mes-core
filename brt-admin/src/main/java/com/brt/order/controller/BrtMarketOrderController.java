package com.brt.order.controller;

import com.brt.common.annotation.RepeatSubmit;
import com.brt.common.annotation.RequestSingleParam;
import com.brt.common.enums.BusinessType;
import com.brt.order.vo.BrtSupplierVo;
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
import com.brt.order.vo.BrtMarketOrderVo;
import com.brt.order.service.IBrtMarketOrderService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 采购单Controller
 *
 * @author Fgn
 * @date 2024-05-09
 */
@RestController
@RequestMapping("/order/marketOrder")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtMarketOrderController extends BaseController {

    private final IBrtMarketOrderService brtMarketOrderService;

    /**
     * @description: TODO 分页查询采购单列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtMarketOrderVo 采购单
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtMarketOrderVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:marketOrder:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtMarketOrderVo> list(BrtMarketOrderVo brtMarketOrderVo) {
        return brtMarketOrderService.queryBrtMarketOrderList(brtMarketOrderVo);
    }

    /**
     * @description: TODO 查询全部采购单列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtMarketOrderVo 采购单
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:marketOrder:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtMarketOrderVo brtMarketOrderVo) {
        return AjaxResult.success("查询成功", brtMarketOrderService.queryBrtMarketOrderAll(brtMarketOrderVo));
    }

    /**
     * @description: TODO 导出采购单列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtMarketOrderVo 采购单
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:marketOrder:export')")
    @Log(title = "采购单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtMarketOrderVo brtMarketOrderVo){
        List<BrtMarketOrderVo> list = brtMarketOrderService.queryBrtMarketOrderAll(brtMarketOrderVo);
        ExcelUtil<BrtMarketOrderVo> util = new ExcelUtil<BrtMarketOrderVo>(BrtMarketOrderVo.class);
        util.exportExcel(response, list, "采购单数据");
    }


    /**
     * @description: TODO 获取采购单详细信息
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:marketOrder:query')")
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable("orderId") String orderId) {
        return success(brtMarketOrderService.queryBrtMarketOrderByOrderId(orderId));
    }

    /**
     * @description: TODO 新增采购单
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtMarketOrderVo 采购单
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:marketOrder:add')")
    @Log(title = "采购单", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtMarketOrderVo brtMarketOrderVo) {
        return AjaxResult.success(brtMarketOrderService.insertBrtMarketOrder(brtMarketOrderVo));
    }

    /**
     * @description: TODO 修改采购单
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtMarketOrderVo 采购单
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:marketOrder:edit')")
    @Log(title = "采购单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtMarketOrderVo brtMarketOrderVo) {
        return AjaxResult.success(brtMarketOrderService.updateBrtMarketOrder(brtMarketOrderVo));
    }

    /**
     * @description: TODO 删除采购单
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: orderIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:marketOrder:remove')")
    @Log(title = "采购单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{orderIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] orderIds){
        return toAjax(brtMarketOrderService.deleteBrtMarketOrderByOrderIds(orderIds));
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
        ExcelUtil<BrtMarketOrderVo> util = new ExcelUtil<BrtMarketOrderVo>(BrtMarketOrderVo.class);
        util.importTemplateExcel(response, "采购单数据");
    }

    /**
     * @param supplierId
     * @description: TODO 统计客户采购单信息
     * @author: FanGN
     * @date: 22:33 2024/5/19
     * @param:
     * @return:
     **/
    @GetMapping("/totalMarketOrder")
    public AjaxResult totalMarketOrder(String supplierId) {
        return AjaxResult.success(brtMarketOrderService.totalMarketOrder(supplierId));
    }

    /**
     * @description: TODO 复制采购单
     * @author: FanGN
     * @date: 16:55 2024/5/20
     * @param:
     * @param orderId
     * @return:
     **/
    @PostMapping("/copyMarketOrder")
    public AjaxResult copyMarketOrder(@RequestSingleParam("orderId") String orderId)
    {
        return AjaxResult.success(brtMarketOrderService.copyMarketOrder(orderId));
    }

    /**
     * 归档
     */
    @PostMapping("beNotInUseSubmit")
    public void beNotInUse(@RequestSingleParam("orderId") String orderId,@RequestSingleParam("childId")String childId){
        this.brtMarketOrderService.beNotInUse(orderId,childId);
    }


}
