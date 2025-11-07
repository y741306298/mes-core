package com.brt.order.controller;

import com.brt.common.annotation.RepeatSubmit;
import com.brt.common.annotation.RequestSingleParam;
import com.brt.common.enums.BusinessType;
import com.brt.order.vo.BrtDeliveryRemindVo;
import com.brt.order.vo.BrtReceivingRemindVo;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.brt.common.annotation.Log;
import com.brt.common.core.controller.BaseController;
import com.brt.common.core.domain.AjaxResult;
import com.brt.order.vo.BrtSupplierReceivingVo;
import com.brt.order.service.IBrtSupplierReceivingService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 客户送货单Controller
 *
 * @author Fgn
 * @date 2024-06-28
 */
@RestController
@RequestMapping("/order/supplierReceiving")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtSupplierReceivingController extends BaseController {

    private final IBrtSupplierReceivingService brtSupplierReceivingService;

    /**
     * @description: TODO 分页查询客户送货单列表
     * @author: Fgn
     * @date: 2024-06-28
     * @param:
     * @param: brtSupplierReceivingVo 客户送货单
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtSupplierReceivingVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:supplierReceiving:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtSupplierReceivingVo> list(BrtSupplierReceivingVo brtSupplierReceivingVo) {
        return brtSupplierReceivingService.queryBrtSupplierReceivingList(brtSupplierReceivingVo);
    }

    /**
     * @description: TODO 查询全部客户送货单列表
     * @author: Fgn
     * @date: 2024-06-28
     * @param:
     * @param: brtSupplierReceivingVo 客户送货单
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:supplierReceiving:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtSupplierReceivingVo brtSupplierReceivingVo) {
        return AjaxResult.success("查询成功", brtSupplierReceivingService.queryBrtSupplierReceivingAll(brtSupplierReceivingVo));
    }

    /**
     * @description: TODO 导出客户送货单列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtSupplierReceivingVo 客户送货单
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:supplierReceiving:export')")
    @Log(title = "客户送货单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtSupplierReceivingVo brtSupplierReceivingVo){
        List<BrtSupplierReceivingVo> list = brtSupplierReceivingService.queryBrtSupplierReceivingAll(brtSupplierReceivingVo);
        ExcelUtil<BrtSupplierReceivingVo> util = new ExcelUtil<BrtSupplierReceivingVo>(BrtSupplierReceivingVo.class);
        util.exportExcel(response, list, "客户送货单数据");
    }


    /**
     * @description: TODO 获取客户送货单详细信息
     * @author: Fgn
     * @date: 2024-06-28
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:supplierReceiving:query')")
    @GetMapping(value = "/{receivingId}")
    public AjaxResult getInfo(@PathVariable("receivingId") String receivingId) {
        return success(brtSupplierReceivingService.queryBrtSupplierReceivingByReceivingId(receivingId));
    }

    /**
     * @description: TODO 新增客户送货单
     * @author: Fgn
     * @date: 2024-06-28
     * @param:
     * @param: brtSupplierReceivingVo 客户送货单
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:supplierReceiving:add')")
    @Log(title = "客户送货单", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtSupplierReceivingVo brtSupplierReceivingVo) {
        return AjaxResult.success(brtSupplierReceivingService.insertBrtSupplierReceiving(brtSupplierReceivingVo));
    }

    /**
     * @description: TODO 修改客户送货单
     * @author: Fgn
     * @date: 2024-06-28
     * @param:
     * @param: brtSupplierReceivingVo 客户送货单
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:supplierReceiving:edit')")
    @Log(title = "客户送货单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtSupplierReceivingVo brtSupplierReceivingVo) {
        return AjaxResult.success(brtSupplierReceivingService.updateBrtSupplierReceiving(brtSupplierReceivingVo));
    }

    /**
     * @description: TODO 删除客户送货单
     * @author: Fgn
     * @date: 2024-06-28
     * @param:
     * @param: receivingIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:supplierReceiving:remove')")
    @Log(title = "客户送货单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{receivingIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] receivingIds){
        return toAjax(brtSupplierReceivingService.deleteBrtSupplierReceivingByReceivingIds(receivingIds));
    }

    /**
     * @description: TODO 送货提醒表查询
     * @author: lf
     * @date: 2024-07-09
     * @param:
     * @return:
     * @return Vo
     **/
    @Log(title = "客户送货提醒表查询")
    @PostMapping("receivingRemind")
    public AjaxResult receivingRemind(@RequestBody BrtReceivingRemindVo receivingRemindVo){
        return AjaxResult.success(this.brtSupplierReceivingService.receivingRemind(receivingRemindVo));
    }


    /**
     * @description: TODO 客户送货查询
     * @author: lf
     * @date: 2024-07-09
     * @param:
     * @return:
     * @return Vo
     **/
    @Log(title = "客户送货提醒表查询")
    @PostMapping("receivingByOrder")
    public AjaxResult receivingByOrder(@RequestBody BrtReceivingRemindVo receivingRemindVo){
        return AjaxResult.success(this.brtSupplierReceivingService.receivingByOrder(receivingRemindVo));
    }

    /**
     * @description: TODO 客户送货提交
     * @author: lf
     * @date: 2024-07-09
     * @param:
     * @return:
     * @return Vo
     **/
    @Log(title = "客户送货提交")
    @PostMapping("receivingSubmit")
    public AjaxResult receivingSubmit(@RequestBody List<BrtReceivingRemindVo> receivingRemindVoList){
        return brtSupplierReceivingService.receivingSubmit(receivingRemindVoList);
    }

    /**
     * @description: TODO 导出送货提醒表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtCustomerDeliveryVo 客户送货单
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:customerDelivery:exportDeliveryRemind')")
    @Log(title = "客户送货单", businessType = BusinessType.EXPORT)
    @PostMapping("/exportReceivingRemind")
    @RepeatSubmit
    public void exportReceivingRemind(HttpServletResponse response, BrtReceivingRemindVo receivingRemindVo){
        Map<String, Object> map = this.brtSupplierReceivingService.receivingRemind(receivingRemindVo);
        List<BrtReceivingRemindVo> list = (List<BrtReceivingRemindVo>)map.get("list");
        ExcelUtil<BrtReceivingRemindVo> util = new ExcelUtil<BrtReceivingRemindVo>(BrtReceivingRemindVo.class);
        util.exportExcel(response, list, "供应商收货单数据");
    }

    @PostMapping("/getPrintData")
    public AjaxResult getPrintData(@RequestSingleParam("orderId") String orderId){
        return AjaxResult.success(brtSupplierReceivingService.getPrint(orderId));
    }

}
