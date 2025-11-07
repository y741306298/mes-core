package com.brt.order.controller;

import com.brt.common.annotation.RepeatSubmit;
import com.brt.common.annotation.RequestSingleParam;
import com.brt.common.enums.BusinessType;
import com.brt.order.vo.BrtDeliveryRemindVo;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.weaver.loadtime.Aj;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.brt.common.annotation.Log;
import com.brt.common.core.controller.BaseController;
import com.brt.common.core.domain.AjaxResult;
import com.brt.order.vo.BrtCustomerDeliveryVo;
import com.brt.order.service.IBrtCustomerDeliveryService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 客户送货单Controller
 *
 * @author Fgn
 * @date 2024-06-28
 */
@RestController
@RequestMapping("/order/customerDelivery")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtCustomerDeliveryController extends BaseController {

    private final IBrtCustomerDeliveryService brtCustomerDeliveryService;

    /**
     * @description: TODO 分页查询客户送货单列表
     * @author: Fgn
     * @date: 2024-06-28
     * @param:
     * @param: brtCustomerDeliveryVo 客户送货单
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtCustomerDeliveryVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:customerDelivery:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtCustomerDeliveryVo> list(BrtCustomerDeliveryVo brtCustomerDeliveryVo) {
        return brtCustomerDeliveryService.queryBrtCustomerDeliveryList(brtCustomerDeliveryVo);
    }

    /**
     * @description: TODO 查询全部客户送货单列表
     * @author: Fgn
     * @date: 2024-06-28
     * @param:
     * @param: brtCustomerDeliveryVo 客户送货单
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:customerDelivery:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtCustomerDeliveryVo brtCustomerDeliveryVo) {
        return AjaxResult.success("查询成功", brtCustomerDeliveryService.queryBrtCustomerDeliveryAll(brtCustomerDeliveryVo));
    }

    /**
     * @description: TODO 导出客户送货单列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtCustomerDeliveryVo 客户送货单
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:customerDelivery:export')")
    @Log(title = "客户送货单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtCustomerDeliveryVo brtCustomerDeliveryVo){
        List<BrtCustomerDeliveryVo> list = brtCustomerDeliveryService.queryBrtCustomerDeliveryAll(brtCustomerDeliveryVo);
        ExcelUtil<BrtCustomerDeliveryVo> util = new ExcelUtil<BrtCustomerDeliveryVo>(BrtCustomerDeliveryVo.class);
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
//    @PreAuthorize("@ss.hasPermi('order:customerDelivery:query')")
    @GetMapping(value = "/{deliveryId}")
    public AjaxResult getInfo(@PathVariable("deliveryId") String deliveryId) {
        return success(brtCustomerDeliveryService.queryBrtCustomerDeliveryByDeliveryId(deliveryId));
    }

    /**
     * @description: TODO 新增客户送货单
     * @author: Fgn
     * @date: 2024-06-28
     * @param:
     * @param: brtCustomerDeliveryVo 客户送货单
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:customerDelivery:add')")
    @Log(title = "客户送货单", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtCustomerDeliveryVo brtCustomerDeliveryVo) {
        return AjaxResult.success(brtCustomerDeliveryService.insertBrtCustomerDelivery(brtCustomerDeliveryVo));
    }

    /**
     * @description: TODO 修改客户送货单
     * @author: Fgn
     * @date: 2024-06-28
     * @param:
     * @param: brtCustomerDeliveryVo 客户送货单
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:customerDelivery:edit')")
    @Log(title = "客户送货单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtCustomerDeliveryVo brtCustomerDeliveryVo) {
        return AjaxResult.success(brtCustomerDeliveryService.updateBrtCustomerDelivery(brtCustomerDeliveryVo));
    }

    /**
     * @description: TODO 删除客户送货单
     * @author: Fgn
     * @date: 2024-06-28
     * @param:
     * @param: deliveryIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:customerDelivery:remove')")
    @Log(title = "客户送货单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{deliveryIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] deliveryIds){
        return toAjax(brtCustomerDeliveryService.deleteBrtCustomerDeliveryByDeliveryIds(deliveryIds));
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
    @PostMapping("deliveryRemind")
    public AjaxResult deliveryRemind(@RequestBody BrtDeliveryRemindVo deliveryRemindVo){
        return AjaxResult.success(this.brtCustomerDeliveryService.deliveryRemind(deliveryRemindVo));
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
    @PostMapping("deliveryByOrder")
    public AjaxResult deliveryByOrder(@RequestBody BrtDeliveryRemindVo deliveryRemindVo){
        return AjaxResult.success(this.brtCustomerDeliveryService.deliveryByOrder(deliveryRemindVo));
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
    @PostMapping("deliverySubmit")
    public AjaxResult deliverySubmit(@RequestBody List<BrtDeliveryRemindVo> deliveryRemindVoList){
        return brtCustomerDeliveryService.deliverySubmit(deliveryRemindVoList);
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
    @PostMapping("/exportDeliveryRemind")
    @RepeatSubmit
    public void exportDeliveryRemind(HttpServletResponse response, BrtDeliveryRemindVo deliveryRemindVo){
        Map<String, Object> map = this.brtCustomerDeliveryService.deliveryRemind(deliveryRemindVo);
        List<BrtDeliveryRemindVo> list = (List<BrtDeliveryRemindVo>)map.get("list");
        ExcelUtil<BrtDeliveryRemindVo> util = new ExcelUtil<BrtDeliveryRemindVo>(BrtDeliveryRemindVo.class);
        util.exportExcel(response, list, "客户送货单数据");
    }

    @PostMapping("/getPrintData")
    public AjaxResult getPrintData(@RequestSingleParam("orderId") String orderId){
        return AjaxResult.success(brtCustomerDeliveryService.getPrint(orderId));
    }

}
