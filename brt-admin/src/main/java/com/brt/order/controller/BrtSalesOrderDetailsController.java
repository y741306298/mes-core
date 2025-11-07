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
import com.brt.order.vo.BrtSalesOrderDetailsVo;
import com.brt.order.service.IBrtSalesOrderDetailsService;
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
@RequestMapping("/order/salesOrderDetails")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtSalesOrderDetailsController extends BaseController {

    private final IBrtSalesOrderDetailsService brtSalesOrderDetailsService;

    /**
     * @description: TODO 分页查询销售单详情列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtSalesOrderDetailsVo 销售单详情
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtSalesOrderDetailsVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:salesOrderDetails:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtSalesOrderDetailsVo> list(BrtSalesOrderDetailsVo brtSalesOrderDetailsVo) {
        return brtSalesOrderDetailsService.queryBrtSalesOrderDetailsList(brtSalesOrderDetailsVo);
    }

    /**
     * @description: TODO 查询全部销售单详情列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtSalesOrderDetailsVo 销售单详情
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:salesOrderDetails:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtSalesOrderDetailsVo brtSalesOrderDetailsVo) {
        return AjaxResult.success("查询成功", brtSalesOrderDetailsService.queryBrtSalesOrderDetailsAll(brtSalesOrderDetailsVo));
    }

    /**
     * @description: TODO 导出销售单详情列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtSalesOrderDetailsVo 销售单详情
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:salesOrderDetails:export')")
    @Log(title = "销售单详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtSalesOrderDetailsVo brtSalesOrderDetailsVo){
        List<BrtSalesOrderDetailsVo> list = brtSalesOrderDetailsService.queryBrtSalesOrderDetailsAll(brtSalesOrderDetailsVo);
        ExcelUtil<BrtSalesOrderDetailsVo> util = new ExcelUtil<BrtSalesOrderDetailsVo>(BrtSalesOrderDetailsVo.class);
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
//    @PreAuthorize("@ss.hasPermi('order:salesOrderDetails:query')")
    @GetMapping(value = "/{detailsId}")
    public AjaxResult getInfo(@PathVariable("detailsId") String detailsId) {
        return success(brtSalesOrderDetailsService.queryBrtSalesOrderDetailsByDetailsId(detailsId));
    }

    /**
     * @description: TODO 新增销售单详情
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtSalesOrderDetailsVo 销售单详情
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:salesOrderDetails:add')")
    @Log(title = "销售单详情", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtSalesOrderDetailsVo brtSalesOrderDetailsVo) {
        return AjaxResult.success(brtSalesOrderDetailsService.insertBrtSalesOrderDetails(brtSalesOrderDetailsVo));
    }

    /**
     * @description: TODO 修改销售单详情
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtSalesOrderDetailsVo 销售单详情
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:salesOrderDetails:edit')")
    @Log(title = "销售单详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtSalesOrderDetailsVo brtSalesOrderDetailsVo) {
        return AjaxResult.success(brtSalesOrderDetailsService.updateBrtSalesOrderDetails(brtSalesOrderDetailsVo));
    }

    /**
     * @description: TODO 修改销售单详情
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtSalesOrderDetailsVo 销售单详情
     * @return:
     * @return null
     **/
    @PostMapping("/updateAndLock")
    public AjaxResult updateAndLock(@RequestBody BrtSalesOrderDetailsVo brtSalesOrderDetailsVo) {
        try {
            return AjaxResult.success(brtSalesOrderDetailsService.updateAndLock(brtSalesOrderDetailsVo));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.success();
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
//    @PreAuthorize("@ss.hasPermi('order:salesOrderDetails:remove')")
    @Log(title = "销售单详情", businessType = BusinessType.DELETE)
	@DeleteMapping("/{detailsIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] detailsIds){
        return toAjax(brtSalesOrderDetailsService.deleteBrtSalesOrderDetailsByDetailsIds(detailsIds));
    }

    /**
     * @description: TODO 销售报表列表
     * @author: FanGN
     * @date: 15:25 2024/5/18
     * @param:
     * @param brtSalesOrderDetailsVo
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<com.brt.order.vo.BrtSalesOrderDetailsVo>
     **/
    @PreAuthorize("@ss.hasPermi('order:salesOrderDetails:statement')")
    @GetMapping("/statement")
    public TableDataInfo<BrtSalesOrderDetailsVo> statement(BrtSalesOrderDetailsVo brtSalesOrderDetailsVo) {
        return brtSalesOrderDetailsService.statementList(brtSalesOrderDetailsVo);
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
        ExcelUtil<BrtSalesOrderDetailsVo> util = new ExcelUtil<BrtSalesOrderDetailsVo>(BrtSalesOrderDetailsVo.class);
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
//    @PreAuthorize("@ss.hasPermi('order:salesOrderDetails:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<BrtSalesOrderDetailsVo> util = new ExcelUtil<BrtSalesOrderDetailsVo>(BrtSalesOrderDetailsVo.class);
        List<BrtSalesOrderDetailsVo> dataList = util.importExcel(file.getInputStream());
        List<BrtSalesOrderDetailsVo> detailsVoList = brtSalesOrderDetailsService.importData(dataList);
        return success(detailsVoList);
    }
}
