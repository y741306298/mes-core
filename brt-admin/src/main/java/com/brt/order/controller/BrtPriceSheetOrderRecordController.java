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
import com.brt.order.vo.BrtPriceSheetOrderRecordVo;
import com.brt.order.service.IBrtPriceSheetOrderRecordService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 报价单_报价记录Controller
 *
 * @author Fgn
 * @date 2024-07-13
 */
@RestController
@RequestMapping("/order/priceSheetOrderRecord")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtPriceSheetOrderRecordController extends BaseController {

    private final IBrtPriceSheetOrderRecordService brtPriceSheetOrderRecordService;

    /**
     * @description: TODO 分页查询报价单_报价记录列表
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @param: brtPriceSheetOrderRecordVo 报价单_报价记录
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtPriceSheetOrderRecordVo>
     **/
    @GetMapping("/list")
    public TableDataInfo<BrtPriceSheetOrderRecordVo> list(BrtPriceSheetOrderRecordVo brtPriceSheetOrderRecordVo) {
        return brtPriceSheetOrderRecordService.queryBrtPriceSheetOrderRecordList(brtPriceSheetOrderRecordVo);
    }

    /**
     * @description: TODO 查询全部报价单_报价记录列表
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @param: brtPriceSheetOrderRecordVo 报价单_报价记录
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtPriceSheetOrderRecordVo brtPriceSheetOrderRecordVo) {
        return AjaxResult.success("查询成功", brtPriceSheetOrderRecordService.queryBrtPriceSheetOrderRecordAll(brtPriceSheetOrderRecordVo));
    }

    /**
     * @description: TODO 导出报价单_报价记录列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtPriceSheetOrderRecordVo 报价单_报价记录
     * @return:
     **/
    @Log(title = "报价单_报价记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtPriceSheetOrderRecordVo brtPriceSheetOrderRecordVo){
        List<BrtPriceSheetOrderRecordVo> list = brtPriceSheetOrderRecordService.queryBrtPriceSheetOrderRecordAll(brtPriceSheetOrderRecordVo);
        ExcelUtil<BrtPriceSheetOrderRecordVo> util = new ExcelUtil<BrtPriceSheetOrderRecordVo>(BrtPriceSheetOrderRecordVo.class);
        util.exportExcel(response, list, "报价单_报价记录数据");
    }


    /**
     * @description: TODO 获取报价单_报价记录详细信息
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable("recordId") String recordId) {
        return success(brtPriceSheetOrderRecordService.queryBrtPriceSheetOrderRecordByRecordId(recordId));
    }

    /**
     * @description: TODO 新增报价单_报价记录
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @param: brtPriceSheetOrderRecordVo 报价单_报价记录
     * @return:
     * @return null
     **/
    @Log(title = "报价单_报价记录", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtPriceSheetOrderRecordVo brtPriceSheetOrderRecordVo) {
        return AjaxResult.success(brtPriceSheetOrderRecordService.insertBrtPriceSheetOrderRecord(brtPriceSheetOrderRecordVo));
    }

    /**
     * @description: TODO 修改报价单_报价记录
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @param: brtPriceSheetOrderRecordVo 报价单_报价记录
     * @return:
     * @return null
     **/
    @RepeatSubmit
    @Log(title = "报价单_报价记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtPriceSheetOrderRecordVo brtPriceSheetOrderRecordVo) {
        return AjaxResult.success(brtPriceSheetOrderRecordService.updateBrtPriceSheetOrderRecord(brtPriceSheetOrderRecordVo));
    }

    /**
     * @description: TODO 删除报价单_报价记录
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @param: recordIds
     * @return:
     * @return null
     **/
    @Log(title = "报价单_报价记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{recordIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] recordIds){
        return toAjax(brtPriceSheetOrderRecordService.deleteBrtPriceSheetOrderRecordByRecordIds(recordIds));
    }

}
