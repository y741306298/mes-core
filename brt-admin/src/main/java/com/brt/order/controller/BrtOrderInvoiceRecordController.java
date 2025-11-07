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
import com.brt.order.vo.BrtOrderInvoiceRecordVo;
import com.brt.order.service.IBrtOrderInvoiceRecordService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 开票记录Controller
 *
 * @author Fgn
 * @date 2024-06-19
 */
@RestController
@RequestMapping("/order/orderInvoiceRecord")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtOrderInvoiceRecordController extends BaseController {

    private final IBrtOrderInvoiceRecordService brtOrderInvoiceRecordService;

    /**
     * @description: TODO 分页查询开票记录列表
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @param: brtOrderInvoiceRecordVo 开票记录
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtOrderInvoiceRecordVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderInvoiceRecord:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtOrderInvoiceRecordVo> list(BrtOrderInvoiceRecordVo brtOrderInvoiceRecordVo) {
        return brtOrderInvoiceRecordService.queryBrtOrderInvoiceRecordList(brtOrderInvoiceRecordVo);
    }

    /**
     * @description: TODO 查询全部开票记录列表
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @param: brtOrderInvoiceRecordVo 开票记录
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderInvoiceRecord:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtOrderInvoiceRecordVo brtOrderInvoiceRecordVo) {
        return AjaxResult.success("查询成功", brtOrderInvoiceRecordService.queryBrtOrderInvoiceRecordAll(brtOrderInvoiceRecordVo));
    }

    /**
     * @description: TODO 导出开票记录列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtOrderInvoiceRecordVo 开票记录
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderInvoiceRecord:export')")
    @Log(title = "开票记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtOrderInvoiceRecordVo brtOrderInvoiceRecordVo){
        List<BrtOrderInvoiceRecordVo> list = brtOrderInvoiceRecordService.queryBrtOrderInvoiceRecordAll(brtOrderInvoiceRecordVo);
        ExcelUtil<BrtOrderInvoiceRecordVo> util = new ExcelUtil<BrtOrderInvoiceRecordVo>(BrtOrderInvoiceRecordVo.class);
        util.exportExcel(response, list, "开票记录数据");
    }


    /**
     * @description: TODO 获取开票记录详细信息
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderInvoiceRecord:query')")
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable("recordId") String recordId) {
        return success(brtOrderInvoiceRecordService.queryBrtOrderInvoiceRecordByRecordId(recordId));
    }

    /**
     * @description: TODO 新增开票记录
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @param: brtOrderInvoiceRecordVo 开票记录
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderInvoiceRecord:add')")
    @Log(title = "开票记录", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtOrderInvoiceRecordVo brtOrderInvoiceRecordVo) {
        return AjaxResult.success(brtOrderInvoiceRecordService.insertBrtOrderInvoiceRecord(brtOrderInvoiceRecordVo));
    }

    /**
     * @description: TODO 修改开票记录
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @param: brtOrderInvoiceRecordVo 开票记录
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:orderInvoiceRecord:edit')")
    @Log(title = "开票记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtOrderInvoiceRecordVo brtOrderInvoiceRecordVo) {
        return AjaxResult.success(brtOrderInvoiceRecordService.updateBrtOrderInvoiceRecord(brtOrderInvoiceRecordVo));
    }

    /**
     * @description: TODO 删除开票记录
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @param: recordIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderInvoiceRecord:remove')")
    @Log(title = "开票记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{recordIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] recordIds){
        return toAjax(brtOrderInvoiceRecordService.deleteBrtOrderInvoiceRecordByRecordIds(recordIds));
    }

}
