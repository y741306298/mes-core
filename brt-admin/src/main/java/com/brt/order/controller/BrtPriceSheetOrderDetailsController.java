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
import com.brt.order.vo.BrtPriceSheetOrderDetailsVo;
import com.brt.order.service.IBrtPriceSheetOrderDetailsService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 销售单详情Controller
 *
 * @author Fgn
 * @date 2024-05-09
 */
@RestController
@RequestMapping("/order/priceSheetOrderDetails")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtPriceSheetOrderDetailsController extends BaseController {

    private final IBrtPriceSheetOrderDetailsService brtPriceSheetOrderDetailsService;

    /**
     * @description: TODO 分页查询销售单详情列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtPriceSheetOrderDetailsVo 销售单详情
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtPriceSheetOrderDetailsVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:priceSheetOrderDetails:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtPriceSheetOrderDetailsVo> list(BrtPriceSheetOrderDetailsVo brtPriceSheetOrderDetailsVo) {
        return brtPriceSheetOrderDetailsService.queryBrtPriceSheetOrderDetailsList(brtPriceSheetOrderDetailsVo);
    }

    /**
     * @description: TODO 查询全部销售单详情列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtPriceSheetOrderDetailsVo 销售单详情
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:priceSheetOrderDetails:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtPriceSheetOrderDetailsVo brtPriceSheetOrderDetailsVo) {
        return AjaxResult.success("查询成功", brtPriceSheetOrderDetailsService.queryBrtPriceSheetOrderDetailsAll(brtPriceSheetOrderDetailsVo));
    }

    /**
     * @description: TODO 导出销售单详情列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtPriceSheetOrderDetailsVo 销售单详情
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:priceSheetOrderDetails:export')")
    @Log(title = "销售单详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtPriceSheetOrderDetailsVo brtPriceSheetOrderDetailsVo){
        List<BrtPriceSheetOrderDetailsVo> list = brtPriceSheetOrderDetailsService.queryBrtPriceSheetOrderDetailsAll(brtPriceSheetOrderDetailsVo);
        ExcelUtil<BrtPriceSheetOrderDetailsVo> util = new ExcelUtil<BrtPriceSheetOrderDetailsVo>(BrtPriceSheetOrderDetailsVo.class);
        util.exportExcel(response, list, "销售单详情数据");
    }


    /**
     * @description: TODO 获取销售单详情详细信息
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:priceSheetOrderDetails:query')")
    @GetMapping(value = "/{detailsId}")
    public AjaxResult getInfo(@PathVariable("detailsId") String detailsId) {
        return success(brtPriceSheetOrderDetailsService.queryBrtPriceSheetOrderDetailsByDetailsId(detailsId));
    }

    /**
     * @description: TODO 新增销售单详情
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtPriceSheetOrderDetailsVo 销售单详情
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:priceSheetOrderDetails:add')")
    @Log(title = "销售单详情", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtPriceSheetOrderDetailsVo brtPriceSheetOrderDetailsVo) {
        return AjaxResult.success(brtPriceSheetOrderDetailsService.insertBrtPriceSheetOrderDetails(brtPriceSheetOrderDetailsVo));
    }

    /**
     * @description: TODO 修改销售单详情
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtPriceSheetOrderDetailsVo 销售单详情
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:priceSheetOrderDetails:edit')")
    @Log(title = "销售单详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtPriceSheetOrderDetailsVo brtPriceSheetOrderDetailsVo) {
        return AjaxResult.success(brtPriceSheetOrderDetailsService.updateBrtPriceSheetOrderDetails(brtPriceSheetOrderDetailsVo));
    }

    /**
     * @description: TODO 删除销售单详情
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: detailsIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:priceSheetOrderDetails:remove')")
    @Log(title = "销售单详情", businessType = BusinessType.DELETE)
    @DeleteMapping("/{detailsIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] detailsIds){
        return toAjax(brtPriceSheetOrderDetailsService.deleteBrtPriceSheetOrderDetailsByDetailsIds(detailsIds));
    }

    /**
     * @description: TODO 销售报表列表
     * @author: FanGN
     * @date: 15:25 2024/5/18
     * @param:
     * @param brtPriceSheetOrderDetailsVo
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<com.brt.order.vo.BrtPriceSheetOrderDetailsVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:priceSheetOrderDetails:statement')")
    @GetMapping("/statement")
    public TableDataInfo<BrtPriceSheetOrderDetailsVo> statement(BrtPriceSheetOrderDetailsVo brtPriceSheetOrderDetailsVo) {
        return brtPriceSheetOrderDetailsService.statementList(brtPriceSheetOrderDetailsVo);
    }

    /**
     * @description: TODO 下载数据模板
     * @author: Fgn
     * @date: 2024-06-30
     * @param:
     * @param: warehouseIds
     * @return:
     * @return null
     **/
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<BrtPriceSheetOrderDetailsVo> util = new ExcelUtil<BrtPriceSheetOrderDetailsVo>(BrtPriceSheetOrderDetailsVo.class);
        util.importTemplateExcel(response, "销售单详情数据");
    }

    /**
     * @description: TODO 导入数据
     * @author: Fgn
     * @date: 2024-06-30
     * @param:
     * @param: warehouseIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:priceSheetOrderDetails:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<BrtPriceSheetOrderDetailsVo> util = new ExcelUtil<BrtPriceSheetOrderDetailsVo>(BrtPriceSheetOrderDetailsVo.class);
        List<BrtPriceSheetOrderDetailsVo> dataList = util.importExcel(file.getInputStream());
        List<BrtPriceSheetOrderDetailsVo> detailsVoList = brtPriceSheetOrderDetailsService.importData(dataList);
        return success(detailsVoList);
    }
}
