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
import com.brt.order.vo.BrtTestModeVo;
import com.brt.order.service.IBrtTestModeService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 测试方式管理Controller
 *
 * @author Fgn
 * @date 2024-05-09
 */
@RestController
@RequestMapping("/order/testMode")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtTestModeController extends BaseController {

    private final IBrtTestModeService brtTestModeService;

    /**
     * @description: TODO 分页查询测试方式管理列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtTestModeVo 测试方式管理
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtTestModeVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:testMode:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtTestModeVo> list(BrtTestModeVo brtTestModeVo) {
        return brtTestModeService.queryBrtTestModeList(brtTestModeVo);
    }

    /**
     * @description: TODO 查询全部测试方式管理列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtTestModeVo 测试方式管理
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:testMode:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtTestModeVo brtTestModeVo) {
        return AjaxResult.success("查询成功", brtTestModeService.queryBrtTestModeAll(brtTestModeVo));
    }

    /**
     * @description: TODO 导出测试方式管理列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtTestModeVo 测试方式管理
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:testMode:export')")
    @Log(title = "测试方式管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtTestModeVo brtTestModeVo){
        List<BrtTestModeVo> list = brtTestModeService.queryBrtTestModeAll(brtTestModeVo);
        ExcelUtil<BrtTestModeVo> util = new ExcelUtil<BrtTestModeVo>(BrtTestModeVo.class);
        util.exportExcel(response, list, "测试方式管理数据");
    }


    /**
     * @description: TODO 获取测试方式管理详细信息
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:testMode:query')")
    @GetMapping(value = "/{modeId}")
    public AjaxResult getInfo(@PathVariable("modeId") String modeId) {
        return success(brtTestModeService.queryBrtTestModeByModeId(modeId));
    }

    /**
     * @description: TODO 新增测试方式管理
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtTestModeVo 测试方式管理
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:testMode:add')")
    @Log(title = "测试方式管理", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtTestModeVo brtTestModeVo) {
        return AjaxResult.success(brtTestModeService.insertBrtTestMode(brtTestModeVo));
    }

    /**
     * @description: TODO 修改测试方式管理
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtTestModeVo 测试方式管理
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:testMode:edit')")
    @Log(title = "测试方式管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtTestModeVo brtTestModeVo) {
        return AjaxResult.success(brtTestModeService.updateBrtTestMode(brtTestModeVo));
    }

    /**
     * @description: TODO 删除测试方式管理
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: modeIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:testMode:remove')")
    @Log(title = "测试方式管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{modeIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] modeIds){
        return toAjax(brtTestModeService.deleteBrtTestModeByModeIds(modeIds));
    }

}
