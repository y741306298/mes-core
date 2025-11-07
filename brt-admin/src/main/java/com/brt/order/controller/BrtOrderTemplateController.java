package com.brt.order.controller;

import com.brt.common.annotation.RepeatSubmit;
import com.brt.common.annotation.RequestSingleParam;
import com.brt.common.enums.BusinessType;
import com.brt.common.enums.OrderTypeEnums;
import lombok.RequiredArgsConstructor;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.weaver.loadtime.Aj;
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
import com.brt.order.vo.BrtOrderTemplateVo;
import com.brt.order.service.IBrtOrderTemplateService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 订单模板Controller
 *
 * @author Fgn
 * @date 2024-05-10
 */
@RestController
@RequestMapping("/order/orderTemplate")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtOrderTemplateController extends BaseController {

    private final IBrtOrderTemplateService brtOrderTemplateService;

    /**
     * @description: TODO 分页查询订单模板列表
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: brtOrderTemplateVo 订单模板
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtOrderTemplateVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderTemplate:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtOrderTemplateVo> list(BrtOrderTemplateVo brtOrderTemplateVo) {
        return brtOrderTemplateService.queryBrtOrderTemplateList(brtOrderTemplateVo);
    }

    /**
     * @description: TODO 查询全部订单模板列表
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: brtOrderTemplateVo 订单模板
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderTemplate:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtOrderTemplateVo brtOrderTemplateVo) {
        return AjaxResult.success("查询成功", brtOrderTemplateService.queryBrtOrderTemplateAll(brtOrderTemplateVo));
    }

    /**
     * @description: TODO 导出订单模板列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtOrderTemplateVo 订单模板
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderTemplate:export')")
    @Log(title = "订单模板", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtOrderTemplateVo brtOrderTemplateVo){
        List<BrtOrderTemplateVo> list = brtOrderTemplateService.queryBrtOrderTemplateAll(brtOrderTemplateVo);
        ExcelUtil<BrtOrderTemplateVo> util = new ExcelUtil<BrtOrderTemplateVo>(BrtOrderTemplateVo.class);
        util.exportExcel(response, list, "订单模板数据");
    }


    /**
     * @description: TODO 获取订单模板详细信息
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderTemplate:query')")
    @GetMapping(value = "/{orderTemplateId}")
    public AjaxResult getInfo(@PathVariable("orderTemplateId") String orderTemplateId) {
        return success(brtOrderTemplateService.queryBrtOrderTemplateByOrderTemplateId(orderTemplateId));
    }

    /**
     * @description: TODO 派工查询
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
    @GetMapping(value = "/sendWorkSelect")
    public AjaxResult sendWorkSelect(String orderTemplateId) {
        return success(brtOrderTemplateService.sendWorkSelect(orderTemplateId));
    }

    /**
     * @description: TODO 新增订单模板
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: brtOrderTemplateVo 订单模板
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderTemplate:add')")
    @Log(title = "订单模板", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtOrderTemplateVo brtOrderTemplateVo) {
        return AjaxResult.success(brtOrderTemplateService.insertBrtOrderTemplate(brtOrderTemplateVo));
    }

    /**
     * @description: TODO 修改订单模板
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: brtOrderTemplateVo 订单模板
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:orderTemplate:edit')")
    @Log(title = "订单模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtOrderTemplateVo brtOrderTemplateVo) {
        return AjaxResult.success(brtOrderTemplateService.updateBrtOrderTemplate(brtOrderTemplateVo));
    }

    /**
     * @description: TODO 删除订单模板
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: orderTemplateIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderTemplate:remove')")
    @Log(title = "订单模板", businessType = BusinessType.DELETE)
	@DeleteMapping("/{orderTemplateIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] orderTemplateIds){
        return toAjax(brtOrderTemplateService.deleteBrtOrderTemplateByOrderTemplateIds(orderTemplateIds));
    }

    /**
     * @description: TODO 派工
     * @author: FanGN
     * @date: 10:25 2024/5/11
     * @param:
     * @param brtOrderTemplateVo
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:orderTemplate:sendWork')")
    @Log(title = "订单模板", businessType = BusinessType.UPDATE)
    @PostMapping("sendWork")
    public AjaxResult sendWork(@RequestBody BrtOrderTemplateVo brtOrderTemplateVo) {
        return AjaxResult.success(brtOrderTemplateService.sendWork(brtOrderTemplateVo));
    }

    /**
     * @description: TODO 复制
     * @author: FanGN
     * @date: 10:25 2024/5/11
     * @param:
     * @param orderTemplateId
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:orderTemplate:copy')")
    @Log(title = "订单模板", businessType = BusinessType.INSERT)
    @PostMapping("copy")
    public AjaxResult copy(@RequestSingleParam("orderTemplateId") String orderTemplateId) {
        return AjaxResult.success(brtOrderTemplateService.copy(orderTemplateId));
    }

    /**
     * @description: TODO 查询订单ID
     * @author: FanGN
     * @date: 10:25 2024/5/11
     * @param:
     * @param orderTemplateId
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
    @RepeatSubmit
    @PostMapping("getOrderId")
    public AjaxResult getOrderId(@RequestSingleParam("orderType") String orderType,@RequestSingleParam("orderTemplateId") String orderTemplateId) {
        return AjaxResult.success(brtOrderTemplateService.getOrderId(orderType,orderTemplateId));
    }

    /**
     * @description: TODO 订单归档
     * @author: FanGN
     * @date: 10:25 2024/5/11
     * @param:
     * @param orderTemplateId
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
    @RepeatSubmit
    @PostMapping("beNotInUseSubmit")
    public AjaxResult beNotInUseSubmit(@RequestSingleParam("orderType") String orderType,@RequestSingleParam("orderId") String orderId, @RequestSingleParam("childId") String childId) {
        brtOrderTemplateService.beNotInUseSubmit(orderType,orderId,childId);
        return AjaxResult.success();
    }

    @PostMapping("getOrderForm")
    public AjaxResult getOrderForm(@RequestSingleParam("orderType") String orderType,@RequestSingleParam("orderId") String orderId){
        return brtOrderTemplateService.getOrderForm(orderType,orderId);
    }

    @GetMapping("getOrderTempleatAndOrderNode")
    public AjaxResult getOrderTempleatAndOrderNode(
            String orderId,
            String templateId,
            String childId,
            String isFilterVoid){
        return AjaxResult.success(brtOrderTemplateService.getOrderTempleatAndOrderNode(orderId,templateId,childId,isFilterVoid));
    }
}
