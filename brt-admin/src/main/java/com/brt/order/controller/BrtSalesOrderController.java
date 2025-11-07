package com.brt.order.controller;

import com.brt.common.annotation.RepeatSubmit;
import com.brt.common.annotation.RequestSingleParam;
import com.brt.common.enums.BusinessType;
import com.brt.common.enums.OrderAuditStatus;
import com.brt.common.enums.OrderNoEnums;
import com.brt.order.domain.BrtSalesOrder;
import com.brt.order.utils.BrtOrderNoUtil;
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
import com.brt.order.vo.BrtSalesOrderVo;
import com.brt.order.service.IBrtSalesOrderService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 销售单Controller
 *
 * @author Fgn
 * @date 2024-05-09
 */
@RestController
@RequestMapping("/order/salesOrder")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtSalesOrderController extends BaseController {

    private final IBrtSalesOrderService brtSalesOrderService;

    /**
     * @description: TODO 分页查询销售单列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtSalesOrderVo 销售单
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtSalesOrderVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:salesOrder:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtSalesOrderVo> list(BrtSalesOrderVo brtSalesOrderVo) {
        return brtSalesOrderService.queryBrtSalesOrderList(brtSalesOrderVo);
    }

    @Autowired
    private BrtOrderNoUtil brtOrderNoUtil;

    /**
     * @description: TODO 查询全部销售单列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtSalesOrderVo 销售单
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:salesOrder:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtSalesOrderVo brtSalesOrderVo) {
        return AjaxResult.success("查询成功", brtSalesOrderService.queryBrtSalesOrderAll(brtSalesOrderVo));
    }

    /**
     * @description: TODO 导出销售单列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtSalesOrderVo 销售单
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:salesOrder:export')")
    @Log(title = "销售单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtSalesOrderVo brtSalesOrderVo){
        List<BrtSalesOrderVo> list = brtSalesOrderService.queryBrtSalesOrderAll(brtSalesOrderVo);
        ExcelUtil<BrtSalesOrderVo> util = new ExcelUtil<BrtSalesOrderVo>(BrtSalesOrderVo.class);
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
//    @PreAuthorize("@ss.hasPermi('order:salesOrder:query')")
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable("orderId") String orderId) {
        return success(brtSalesOrderService.queryBrtSalesOrderByOrderId(orderId));
    }

    /**
     * @description: TODO 新增销售单
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtSalesOrderVo 销售单
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:salesOrder:add')")
    @Log(title = "销售单", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtSalesOrderVo brtSalesOrderVo) {
        return AjaxResult.success(brtSalesOrderService.insertBrtSalesOrder(brtSalesOrderVo));
    }

    /**
     * @description: TODO 修改销售单
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtSalesOrderVo 销售单
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:salesOrder:edit')")
    @Log(title = "销售单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtSalesOrderVo brtSalesOrderVo) {
        return AjaxResult.success(brtSalesOrderService.updateBrtSalesOrder(brtSalesOrderVo));
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
//    @PreAuthorize("@ss.hasPermi('order:salesOrder:remove')")
    @Log(title = "销售单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{orderIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] orderIds){
        return toAjax(brtSalesOrderService.deleteBrtSalesOrderByOrderIds(orderIds));
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
        ExcelUtil<BrtSalesOrderVo> util = new ExcelUtil<BrtSalesOrderVo>(BrtSalesOrderVo.class);
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
    @GetMapping("/totalSalesOrder")
    public AjaxResult totalSalesOrder(String customerId) {
        return AjaxResult.success(brtSalesOrderService.totalSalesOrder(customerId));
    }

    /**
     * @description: TODO 复制销售单
     * @author: FanGN
     * @date: 16:55 2024/5/20
     * @param:
     * @param orderId
     * @return:
     **/
    @PostMapping("/copySalesOrder")
    public AjaxResult copySalesOrder(@RequestSingleParam("orderId") String orderId)
    {
        return AjaxResult.success(brtSalesOrderService.copySalesOrder(orderId));
    }
    /**
     * @description: TODO 导入仓库管理数据
     * @author: Fgn
     * @date: 2024-06-30
     * @param:
     * @param: warehouseIds
     * @return:
     * @return null
     **/
    @Log(title = "仓库管理", businessType = BusinessType.IMPORT)
//    @PreAuthorize("@ss.hasPermi('sales:salesOrder:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<BrtSalesOrderVo> util = new ExcelUtil<BrtSalesOrderVo>(BrtSalesOrderVo.class);
        List<BrtSalesOrderVo> dataList = util.importExcel(file.getInputStream());
        for(BrtSalesOrderVo brtSalesOrderVo:dataList){
            brtSalesOrderService.save(brtSalesOrderVo);
        }
        return success();
    }

    /**
     * 归档
     */
    @PostMapping("beNotInUseSubmit")
    public void beNotInUse(@RequestSingleParam("orderId") String orderId,@RequestSingleParam("childId")String childId){
        this.brtSalesOrderService.beNotInUse(orderId,childId);
    }


}
