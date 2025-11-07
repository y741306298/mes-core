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
import com.brt.order.vo.BrtInInventoryVo;
import com.brt.order.service.IBrtInInventoryService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 入库管理Controller
 *
 * @author Fgn
 * @date 2024-07-11
 */
@RestController
@RequestMapping("/order/inInventory")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtInInventoryController extends BaseController {

    private final IBrtInInventoryService brtInInventoryService;

    /**
     * @description: TODO 分页查询入库管理列表
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtInInventoryVo 入库管理
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtInInventoryVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:inInventory:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtInInventoryVo> list(BrtInInventoryVo brtInInventoryVo) {
        return brtInInventoryService.queryBrtInInventoryList(brtInInventoryVo);
    }

    /**
     * @description: TODO 查询全部入库管理列表
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtInInventoryVo 入库管理
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:inInventory:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtInInventoryVo brtInInventoryVo) {
        return AjaxResult.success("查询成功", brtInInventoryService.queryBrtInInventoryAll(brtInInventoryVo));
    }

    /**
     * @description: TODO 导出入库管理列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtInInventoryVo 入库管理
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:inInventory:export')")
    @Log(title = "入库管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtInInventoryVo brtInInventoryVo){
        List<BrtInInventoryVo> list = brtInInventoryService.queryBrtInInventoryAll(brtInInventoryVo);
        ExcelUtil<BrtInInventoryVo> util = new ExcelUtil<BrtInInventoryVo>(BrtInInventoryVo.class);
        util.exportExcel(response, list, "入库管理数据");
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
        ExcelUtil<BrtInInventoryVo> util = new ExcelUtil<BrtInInventoryVo>(BrtInInventoryVo.class);
        util.importTemplateExcel(response, "导入");
    }


    /**
     * @description: TODO 获取入库管理详细信息
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:inInventory:query')")
    @GetMapping(value = "/{inInventoryId}")
    public AjaxResult getInfo(@PathVariable("inInventoryId") String inInventoryId) {
        return success(brtInInventoryService.queryBrtInInventoryByInInventoryId(inInventoryId));
    }

    /**
     * @description: TODO 新增入库管理
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtInInventoryVo 入库管理
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:inInventory:add')")
    @Log(title = "入库管理", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtInInventoryVo brtInInventoryVo) {
        return AjaxResult.success(brtInInventoryService.insertBrtInInventory(brtInInventoryVo));
    }

    /**
     * @description: TODO 修改入库管理
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtInInventoryVo 入库管理
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:inInventory:edit')")
    @Log(title = "入库管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtInInventoryVo brtInInventoryVo) {
        return AjaxResult.success(brtInInventoryService.updateBrtInInventory(brtInInventoryVo));
    }

    /**
     * @description: TODO 删除入库管理
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: inInventoryIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:inInventory:remove')")
    @Log(title = "入库管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{inInventoryIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] inInventoryIds){
        return toAjax(brtInInventoryService.deleteBrtInInventoryByInInventoryIds(inInventoryIds));
    }

    /**
     * @description: TODO 确认入库
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:inInventory:affirm')")
    @PostMapping(value = "/affirm")
    public AjaxResult affirm(@RequestSingleParam("inInventoryId") String inInventoryId) {
        brtInInventoryService.affirm(inInventoryId);
        return AjaxResult.success();
    }


}
