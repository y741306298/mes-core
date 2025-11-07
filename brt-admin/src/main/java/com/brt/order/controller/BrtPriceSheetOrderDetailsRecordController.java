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
import com.brt.order.vo.BrtPriceSheetOrderDetailsRecordVo;
import com.brt.order.service.IBrtPriceSheetOrderDetailsRecordService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 报价记录详情Controller
 *
 * @author Fgn
 * @date 2024-07-13
 */
@RestController
@RequestMapping("/order/priceSheetDetailsRecord")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtPriceSheetOrderDetailsRecordController extends BaseController {

    private final IBrtPriceSheetOrderDetailsRecordService brtPriceSheetOrderDetailsRecordService;

    /**
     * @description: TODO 分页查询报价记录详情列表
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @param: brtPriceSheetOrderDetailsRecordVo 报价记录详情
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtPriceSheetOrderDetailsRecordVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:record:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtPriceSheetOrderDetailsRecordVo> list(BrtPriceSheetOrderDetailsRecordVo brtPriceSheetOrderDetailsRecordVo) {
        return brtPriceSheetOrderDetailsRecordService.queryBrtPriceSheetOrderDetailsRecordList(brtPriceSheetOrderDetailsRecordVo);
    }

    /**
     * @description: TODO 查询全部报价记录详情列表
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @param: brtPriceSheetOrderDetailsRecordVo 报价记录详情
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:record:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtPriceSheetOrderDetailsRecordVo brtPriceSheetOrderDetailsRecordVo) {
        return AjaxResult.success("查询成功", brtPriceSheetOrderDetailsRecordService.queryBrtPriceSheetOrderDetailsRecordAll(brtPriceSheetOrderDetailsRecordVo));
    }

    /**
     * @description: TODO 导出报价记录详情列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtPriceSheetOrderDetailsRecordVo 报价记录详情
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:record:export')")
    @Log(title = "报价记录详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtPriceSheetOrderDetailsRecordVo brtPriceSheetOrderDetailsRecordVo){
        List<BrtPriceSheetOrderDetailsRecordVo> list = brtPriceSheetOrderDetailsRecordService.queryBrtPriceSheetOrderDetailsRecordAll(brtPriceSheetOrderDetailsRecordVo);
        ExcelUtil<BrtPriceSheetOrderDetailsRecordVo> util = new ExcelUtil<BrtPriceSheetOrderDetailsRecordVo>(BrtPriceSheetOrderDetailsRecordVo.class);
        util.exportExcel(response, list, "报价记录详情数据");
    }


    /**
     * @description: TODO 获取报价记录详情详细信息
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:record:query')")
    @GetMapping(value = "/{detailsId}")
    public AjaxResult getInfo(@PathVariable("detailsId") String detailsId) {
        return success(brtPriceSheetOrderDetailsRecordService.queryBrtPriceSheetOrderDetailsRecordByDetailsId(detailsId));
    }

    /**
     * @description: TODO 新增报价记录详情
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @param: brtPriceSheetOrderDetailsRecordVo 报价记录详情
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:record:add')")
    @Log(title = "报价记录详情", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtPriceSheetOrderDetailsRecordVo brtPriceSheetOrderDetailsRecordVo) {
        return AjaxResult.success(brtPriceSheetOrderDetailsRecordService.insertBrtPriceSheetOrderDetailsRecord(brtPriceSheetOrderDetailsRecordVo));
    }

    /**
     * @description: TODO 修改报价记录详情
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @param: brtPriceSheetOrderDetailsRecordVo 报价记录详情
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:record:edit')")
    @Log(title = "报价记录详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtPriceSheetOrderDetailsRecordVo brtPriceSheetOrderDetailsRecordVo) {
        return AjaxResult.success(brtPriceSheetOrderDetailsRecordService.updateBrtPriceSheetOrderDetailsRecord(brtPriceSheetOrderDetailsRecordVo));
    }

    /**
     * @description: TODO 删除报价记录详情
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @param: detailsIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:record:remove')")
    @Log(title = "报价记录详情", businessType = BusinessType.DELETE)
	@DeleteMapping("/{detailsIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] detailsIds){
        return toAjax(brtPriceSheetOrderDetailsRecordService.deleteBrtPriceSheetOrderDetailsRecordByDetailsIds(detailsIds));
    }

}
