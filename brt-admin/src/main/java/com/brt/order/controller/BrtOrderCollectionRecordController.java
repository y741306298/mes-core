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
import com.brt.order.vo.BrtOrderCollectionRecordVo;
import com.brt.order.service.IBrtOrderCollectionRecordService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 收款记录Controller
 *
 * @author Fgn
 * @date 2024-06-20
 */
@RestController
@RequestMapping("/order/orderCollectionRecord")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtOrderCollectionRecordController extends BaseController {

    private final IBrtOrderCollectionRecordService brtOrderCollectionRecordService;

    /**
     * @description: TODO 分页查询收款记录列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderCollectionRecordVo 收款记录
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtOrderCollectionRecordVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderCollectionRecord:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtOrderCollectionRecordVo> list(BrtOrderCollectionRecordVo brtOrderCollectionRecordVo) {
        return brtOrderCollectionRecordService.queryBrtOrderCollectionRecordList(brtOrderCollectionRecordVo);
    }

    /**
     * @description: TODO 查询全部收款记录列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderCollectionRecordVo 收款记录
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderCollectionRecord:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtOrderCollectionRecordVo brtOrderCollectionRecordVo) {
        return AjaxResult.success("查询成功", brtOrderCollectionRecordService.queryBrtOrderCollectionRecordAll(brtOrderCollectionRecordVo));
    }

    /**
     * @description: TODO 导出收款记录列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtOrderCollectionRecordVo 收款记录
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderCollectionRecord:export')")
    @Log(title = "收款记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtOrderCollectionRecordVo brtOrderCollectionRecordVo){
        List<BrtOrderCollectionRecordVo> list = brtOrderCollectionRecordService.queryBrtOrderCollectionRecordAll(brtOrderCollectionRecordVo);
        ExcelUtil<BrtOrderCollectionRecordVo> util = new ExcelUtil<BrtOrderCollectionRecordVo>(BrtOrderCollectionRecordVo.class);
        util.exportExcel(response, list, "收款记录数据");
    }


    /**
     * @description: TODO 获取收款记录详细信息
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderCollectionRecord:query')")
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable("recordId") String recordId) {
        return success(brtOrderCollectionRecordService.queryBrtOrderCollectionRecordByRecordId(recordId));
    }

    /**
     * @description: TODO 新增收款记录
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderCollectionRecordVo 收款记录
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderCollectionRecord:add')")
    @Log(title = "收款记录", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtOrderCollectionRecordVo brtOrderCollectionRecordVo) {
        return AjaxResult.success(brtOrderCollectionRecordService.insertBrtOrderCollectionRecord(brtOrderCollectionRecordVo));
    }

    /**
     * @description: TODO 修改收款记录
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderCollectionRecordVo 收款记录
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:orderCollectionRecord:edit')")
    @Log(title = "收款记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtOrderCollectionRecordVo brtOrderCollectionRecordVo) {
        return AjaxResult.success(brtOrderCollectionRecordService.updateBrtOrderCollectionRecord(brtOrderCollectionRecordVo));
    }

    /**
     * @description: TODO 删除收款记录
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: recordIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderCollectionRecord:remove')")
    @Log(title = "收款记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{recordIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] recordIds){
        return toAjax(brtOrderCollectionRecordService.deleteBrtOrderCollectionRecordByRecordIds(recordIds));
    }

}
