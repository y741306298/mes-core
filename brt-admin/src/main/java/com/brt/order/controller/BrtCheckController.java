package com.brt.order.controller;

import com.brt.common.annotation.RepeatSubmit;
import com.brt.common.annotation.RequestSingleParam;
import com.brt.common.enums.BusinessType;
import com.brt.order.vo.BrtCheckAchievementVo;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.InvocationTargetException;
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
import com.brt.order.vo.BrtCheckVo;
import com.brt.order.service.IBrtCheckService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 账单Controller
 *
 * @author Fgn
 * @date 2024-05-15
 */
@RestController
@RequestMapping("/order/check")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtCheckController extends BaseController {

    private final IBrtCheckService brtCheckService;

    /**
     * @description: TODO 分页查询账单列表
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: brtCheckVo 账单
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtCheckVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:check:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtCheckVo> list(BrtCheckVo brtCheckVo) {
        return brtCheckService.queryBrtCheckList(brtCheckVo);
    }

    /**
     * @description: TODO 查询全部账单列表
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: brtCheckVo 账单
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:check:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtCheckVo brtCheckVo) {
        return AjaxResult.success("查询成功", brtCheckService.queryBrtCheckAll(brtCheckVo));
    }

    /**
     * @description: TODO 导出账单列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtCheckVo 账单
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:check:export')")
    @Log(title = "账单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtCheckVo brtCheckVo){
        List<BrtCheckVo> list = brtCheckService.queryBrtCheckAll(brtCheckVo);
        ExcelUtil<BrtCheckVo> util = new ExcelUtil<BrtCheckVo>(BrtCheckVo.class);
        util.exportExcel(response, list, "账单数据");
    }


    /**
     * @description: TODO 获取账单详细信息
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:check:query')")
    @GetMapping(value = "/{checkId}")
    public AjaxResult getInfo(@PathVariable("checkId") String checkId) {
        return success(brtCheckService.queryBrtCheckByCheckId(checkId));
    }

    /**
     * @description: TODO 新增账单
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: brtCheckVo 账单
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:check:add')")
    @Log(title = "账单", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtCheckVo brtCheckVo) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return AjaxResult.success(brtCheckService.insertBrtCheck(brtCheckVo));
    }

    /**
     * @description: TODO 修改账单
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: brtCheckVo 账单
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:check:edit')")
    @Log(title = "账单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtCheckVo brtCheckVo) {
        return AjaxResult.success(brtCheckService.updateBrtCheck(brtCheckVo));
    }

    /**
     * @description: TODO 删除账单
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: checkIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:check:remove')")
    @Log(title = "账单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{checkIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] checkIds){
        return toAjax(brtCheckService.deleteBrtCheckByCheckIds(checkIds));
    }

    /**
     * @description: TODO 获取业绩表
     * @author: FanGN
     * @date: 14:38 2024/5/20
     * @param:
     * @param
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:check:achievement')")
    @GetMapping(value = "/achievement")
    public AjaxResult achievement(String year,String orderType) {
        return success(brtCheckService.achievement(year,orderType));
    }

    /**
     * @description: TODO 获取业绩表
     * @author: FanGN
     * @date: 14:38 2024/5/20
     * @param:
     * @param
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
    @PostMapping(value = "/exportAchievement")
    public void exportAchievement(HttpServletResponse response,String year,String orderType){
        List<BrtCheckAchievementVo> list = brtCheckService.exportAchievement(year, orderType);
        ExcelUtil<BrtCheckAchievementVo> util = new ExcelUtil<BrtCheckAchievementVo>(BrtCheckAchievementVo.class);
        util.exportExcel(response, list, "账单数据");
    }

    /**
     * @description: TODO 查询账户业绩
     * @author: FanGN
     * @date: 15:31 2024/5/20
     * @param:
     * @param year
     * @param orderType
     * @param month
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
    @GetMapping(value = "/accountAchievement")
    public AjaxResult accountAchievement(String year,String orderType,String month) {
        return success(brtCheckService.accountAchievement(year,orderType,month));
    }

}
