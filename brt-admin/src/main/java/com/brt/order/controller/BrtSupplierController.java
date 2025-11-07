package com.brt.order.controller;

import com.brt.common.annotation.RepeatSubmit;
import com.brt.common.annotation.RequestSingleParam;
import com.brt.common.core.domain.entity.SysUser;
import com.brt.common.enums.BusinessType;
import com.brt.order.vo.MarketRecordVo;
import com.brt.order.vo.PurchaseRecordVo;
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
import com.brt.order.vo.BrtSupplierVo;
import com.brt.order.service.IBrtSupplierService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 供应商信息Controller
 *
 * @author lf
 * @date 2024-06-19
 */
@RestController
@RequestMapping("/order/supplier")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtSupplierController extends BaseController {

    private final IBrtSupplierService brtSupplierService;

    /**
     * @return com.brt.common.core.page.TableDataInfo<BrtSupplierVo>
     * @description: TODO 分页查询供应商信息列表
     * @author: lf
     * @date: 2024-06-19
     * @param:
     * @param: brtSupplierVo 供应商信息
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:supplier:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtSupplierVo> list(BrtSupplierVo brtSupplierVo) {
        return brtSupplierService.queryBrtSupplierList(brtSupplierVo);
    }

    /**
     * @return com.brt.common.core.domain.AjaxResult
     * @description: TODO 查询全部供应商信息列表
     * @author: lf
     * @date: 2024-06-19
     * @param:
     * @param: brtSupplierVo 供应商信息
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:supplier:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtSupplierVo brtSupplierVo) {
        return AjaxResult.success("查询成功", brtSupplierService.queryBrtSupplierAll(brtSupplierVo));
    }

    /**
     * @description: TODO 导出供应商信息列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtSupplierVo 供应商信息
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:supplier:export')")
    @Log(title = "供应商信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtSupplierVo brtSupplierVo) {
        List<BrtSupplierVo> list = brtSupplierService.queryBrtSupplierAll(brtSupplierVo);
        ExcelUtil<BrtSupplierVo> util = new ExcelUtil<BrtSupplierVo>(BrtSupplierVo.class);
        util.exportExcel(response, list, "供应商信息数据");
    }


    /**
     * @return com.brt.common.core.domain.AjaxResult
     * @description: TODO 获取供应商信息详细信息
     * @author: lf
     * @date: 2024-06-19
     * @param:
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:supplier:query')")
    @GetMapping(value = "/{supplierId}")
    public AjaxResult getInfo(@PathVariable("supplierId") String supplierId) {
        return success(brtSupplierService.queryBrtSupplierBySupplierId(supplierId));
    }

    /**
     * @return null
     * @description: TODO 新增供应商信息
     * @author: lf
     * @date: 2024-06-19
     * @param:
     * @param: brtSupplierVo 供应商信息
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:supplier:add')")
    @Log(title = "供应商信息", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtSupplierVo brtSupplierVo) {
        return AjaxResult.success(brtSupplierService.insertBrtSupplier(brtSupplierVo));
    }

    /**
     * @return null
     * @description: TODO 修改供应商信息
     * @author: lf
     * @date: 2024-06-19
     * @param:
     * @param: brtSupplierVo 供应商信息
     * @return:
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:supplier:edit')")
    @Log(title = "供应商信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtSupplierVo brtSupplierVo) {
        return AjaxResult.success(brtSupplierService.updateBrtSupplier(brtSupplierVo));
    }

    /**
     * @return null
     * @description: TODO 删除供应商信息
     * @author: lf
     * @date: 2024-06-19
     * @param:
     * @param: supplierIds
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:supplier:remove')")
    @Log(title = "供应商信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{supplierIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] supplierIds) {
        return toAjax(brtSupplierService.deleteBrtSupplierBySupplierIds(supplierIds));
    }

    /**
     * @param response
     * @description: TODO 下载模板
     * @author: FanGN
     * @date: 00:44 2024/4/28
     * @param:
     * @return:
     **/
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<BrtSupplierVo> util = new ExcelUtil<BrtSupplierVo>(BrtSupplierVo.class);
        util.importTemplateExcel(response, "供应商数据");
    }

    /**
     * @param file
     * @param importData
     * @return com.brt.common.core.domain.AjaxResult
     * @description: TODO 导入供应商信息
     * @author: FanGN
     * @date: 00:49 2024/4/28
     * @param:
     * @return:
     **/
    @Log(title = "供应商信息", businessType = BusinessType.IMPORT)
//    @PreAuthorize("@ss.hasPermi('order:supplier:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<BrtSupplierVo> util = new ExcelUtil<BrtSupplierVo>(BrtSupplierVo.class);
        List<BrtSupplierVo> supplierList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = brtSupplierService.importData(supplierList, operName);
        return success(message);
    }

    /**
     * @return com.brt.common.core.domain.AjaxResult
     * @description: TODO 查询客户的销售记录
     * @param:
     * @return:
     **/
    @GetMapping("/queryPurchaseRecord")
    public TableDataInfo<PurchaseRecordVo> queryPurchaseRecord(PurchaseRecordVo purchaseRecordVo) throws Exception {
        return brtSupplierService.queryPurchaseRecord(purchaseRecordVo);
    }

}
