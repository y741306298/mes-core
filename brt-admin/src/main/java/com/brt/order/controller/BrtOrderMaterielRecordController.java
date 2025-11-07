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
import com.brt.order.vo.BrtOrderMaterielRecordVo;
import com.brt.order.service.IBrtOrderMaterielRecordService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 数量记录Controller
 *
 * @author Fgn
 * @date 2024-06-20
 */
@RestController
@RequestMapping("/order/orderMaterielRecord")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtOrderMaterielRecordController extends BaseController {

    private final IBrtOrderMaterielRecordService brtOrderMaterielRecordService;

    /**
     * @description: TODO 分页查询数量记录列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderMaterielRecordVo 数量记录
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtOrderMaterielRecordVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderMaterielRecord:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtOrderMaterielRecordVo> list(BrtOrderMaterielRecordVo brtOrderMaterielRecordVo) {
        return brtOrderMaterielRecordService.queryBrtOrderMaterielRecordList(brtOrderMaterielRecordVo);
    }

    /**
     * @description: TODO 查询全部数量记录列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderMaterielRecordVo 数量记录
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderMaterielRecord:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtOrderMaterielRecordVo brtOrderMaterielRecordVo) {
        return AjaxResult.success("查询成功", brtOrderMaterielRecordService.queryBrtOrderMaterielRecordAll(brtOrderMaterielRecordVo));
    }

    /**
     * @description: TODO 导出数量记录列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtOrderMaterielRecordVo 数量记录
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderMaterielRecord:export')")
    @Log(title = "数量记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtOrderMaterielRecordVo brtOrderMaterielRecordVo){
        List<BrtOrderMaterielRecordVo> list = brtOrderMaterielRecordService.queryBrtOrderMaterielRecordAll(brtOrderMaterielRecordVo);
        ExcelUtil<BrtOrderMaterielRecordVo> util = new ExcelUtil<BrtOrderMaterielRecordVo>(BrtOrderMaterielRecordVo.class);
        util.exportExcel(response, list, "数量记录数据");
    }


    /**
     * @description: TODO 获取数量记录详细信息
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderMaterielRecord:query')")
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable("recordId") String recordId) {
        return success(brtOrderMaterielRecordService.queryBrtOrderMaterielRecordByRecordId(recordId));
    }

    /**
     * @description: TODO 新增数量记录
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderMaterielRecordVo 数量记录
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderMaterielRecord:add')")
    @Log(title = "数量记录", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody List<BrtOrderMaterielRecordVo> brtOrderMaterielRecordVoList) {
        return AjaxResult.success(brtOrderMaterielRecordService.insertBrtOrderMaterielRecordList(brtOrderMaterielRecordVoList));
    }

    /**
     * @description: TODO 修改数量记录
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderMaterielRecordVo 数量记录
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:orderMaterielRecord:edit')")
    @Log(title = "数量记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtOrderMaterielRecordVo brtOrderMaterielRecordVo) {
        return AjaxResult.success(brtOrderMaterielRecordService.updateBrtOrderMaterielRecord(brtOrderMaterielRecordVo));
    }

    /**
     * @description: TODO 删除数量记录
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: recordIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderMaterielRecord:remove')")
    @Log(title = "数量记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{recordIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] recordIds){
        return toAjax(brtOrderMaterielRecordService.deleteBrtOrderMaterielRecordByRecordIds(recordIds));
    }

}
