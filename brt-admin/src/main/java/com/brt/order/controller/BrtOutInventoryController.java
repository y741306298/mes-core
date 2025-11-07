package com.brt.order.controller;

import com.brt.common.annotation.RepeatSubmit;
import com.brt.common.annotation.RequestSingleParam;
import com.brt.common.enums.BusinessType;
import com.brt.order.vo.BrtOrderBoomVo;
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
import com.brt.order.vo.BrtOutInventoryVo;
import com.brt.order.service.IBrtOutInventoryService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 出库管理Controller
 *
 * @author Fgn
 * @date 2024-07-11
 */
@RestController
@RequestMapping("/order/outInventory")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtOutInventoryController extends BaseController {

    private final IBrtOutInventoryService brtOutInventoryService;

    /**
     * @description: TODO 分页查询出库管理列表
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtOutInventoryVo 出库管理
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtOutInventoryVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:outInventory:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtOutInventoryVo> list(BrtOutInventoryVo brtOutInventoryVo) {
        return brtOutInventoryService.queryBrtOutInventoryList(brtOutInventoryVo);
    }

    /**
     * @description: TODO 查询全部出库管理列表
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtOutInventoryVo 出库管理
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:outInventory:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtOutInventoryVo brtOutInventoryVo) {
        return AjaxResult.success("查询成功", brtOutInventoryService.queryBrtOutInventoryAll(brtOutInventoryVo));
    }

    /**
     * @description: TODO 导出出库管理列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtOutInventoryVo 出库管理
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:outInventory:export')")
    @Log(title = "出库管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtOutInventoryVo brtOutInventoryVo){
        List<BrtOutInventoryVo> list = brtOutInventoryService.queryBrtOutInventoryAll(brtOutInventoryVo);
        ExcelUtil<BrtOutInventoryVo> util = new ExcelUtil<BrtOutInventoryVo>(BrtOutInventoryVo.class);
        util.exportExcel(response, list, "出库管理数据");
    }

    /**
     * @description: TODO 下载模板
     * @author: FanGN
     * @date: 00:44 2024/4/28
     * @param:
     * @param response
     * @return:
     **/
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<BrtOutInventoryVo> util = new ExcelUtil<BrtOutInventoryVo>(BrtOutInventoryVo.class);
        util.importTemplateExcel(response, "导入");
    }


    /**
     * @description: TODO 获取出库管理详细信息
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:outInventory:query')")
    @GetMapping(value = "/{outInventoryId}")
    public AjaxResult getInfo(@PathVariable("outInventoryId") String outInventoryId) {
        return success(brtOutInventoryService.queryBrtOutInventoryByOutInventoryId(outInventoryId));
    }

    /**
     * @description: TODO 新增出库管理
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtOutInventoryVo 出库管理
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:outInventory:add')")
    @Log(title = "出库管理", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtOutInventoryVo brtOutInventoryVo) {
        return AjaxResult.success(brtOutInventoryService.insertBrtOutInventory(brtOutInventoryVo));
    }

    /**
     * @description: TODO 修改出库管理
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtOutInventoryVo 出库管理
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:outInventory:edit')")
    @Log(title = "出库管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtOutInventoryVo brtOutInventoryVo) {
        return AjaxResult.success(brtOutInventoryService.updateBrtOutInventory(brtOutInventoryVo));
    }

    /**
     * @description: TODO 删除出库管理
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: outInventoryIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:outInventory:remove')")
    @Log(title = "出库管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{outInventoryIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] outInventoryIds){
        return toAjax(brtOutInventoryService.deleteBrtOutInventoryByOutInventoryIds(outInventoryIds));
    }

    /**
     * @description: TODO 确认出库
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:outInventory:affirm')")
    @PostMapping(value = "/affirm")
    public AjaxResult affirm(@RequestSingleParam("outInventoryId") String outInventoryId) {
        brtOutInventoryService.affirm(outInventoryId);
        return AjaxResult.success();
    }


}
