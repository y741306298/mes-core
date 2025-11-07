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
import com.brt.order.vo.BrtOrderChildProcessVo;
import com.brt.order.service.IBrtOrderChildProcessService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 订单子流程Controller
 *
 * @author Fgn
 * @date 2024-06-21
 */
@RestController
@RequestMapping("/order/orderChildProcess")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtOrderChildProcessController extends BaseController {

    private final IBrtOrderChildProcessService brtOrderChildProcessService;

    /**
     * @description: TODO 分页查询订单子流程列表
     * @author: Fgn
     * @date: 2024-06-21
     * @param:
     * @param: brtOrderChildProcessVo 订单子流程
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtOrderChildProcessVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderChildProcess:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtOrderChildProcessVo> list(BrtOrderChildProcessVo brtOrderChildProcessVo) {
        return brtOrderChildProcessService.queryBrtOrderChildProcessList(brtOrderChildProcessVo);
    }

    /**
     * @description: TODO 查询全部订单子流程列表
     * @author: Fgn
     * @date: 2024-06-21
     * @param:
     * @param: brtOrderChildProcessVo 订单子流程
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderChildProcess:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtOrderChildProcessVo brtOrderChildProcessVo) {
        return AjaxResult.success("查询成功", brtOrderChildProcessService.queryBrtOrderChildProcessAll(brtOrderChildProcessVo));
    }

    /**
     * @description: TODO 导出订单子流程列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtOrderChildProcessVo 订单子流程
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderChildProcess:export')")
    @Log(title = "订单子流程", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtOrderChildProcessVo brtOrderChildProcessVo){
        List<BrtOrderChildProcessVo> list = brtOrderChildProcessService.queryBrtOrderChildProcessAll(brtOrderChildProcessVo);
        ExcelUtil<BrtOrderChildProcessVo> util = new ExcelUtil<BrtOrderChildProcessVo>(BrtOrderChildProcessVo.class);
        util.exportExcel(response, list, "订单子流程数据");
    }


    /**
     * @description: TODO 获取订单子流程详细信息
     * @author: Fgn
     * @date: 2024-06-21
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderChildProcess:query')")
    @GetMapping(value = "/{childId}")
    public AjaxResult getInfo(@PathVariable("childId") String childId) {
        return success(brtOrderChildProcessService.queryBrtOrderChildProcessByChildId(childId));
    }

    /**
     * @description: TODO 新增订单子流程
     * @author: Fgn
     * @date: 2024-06-21
     * @param:
     * @param: brtOrderChildProcessVo 订单子流程
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderChildProcess:add')")
    @Log(title = "订单子流程", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtOrderChildProcessVo brtOrderChildProcessVo) {
        return AjaxResult.success(brtOrderChildProcessService.insertBrtOrderChildProcess(brtOrderChildProcessVo));
    }

    /**
     * @description: TODO 修改订单子流程
     * @author: Fgn
     * @date: 2024-06-21
     * @param:
     * @param: brtOrderChildProcessVo 订单子流程
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:orderChildProcess:edit')")
    @Log(title = "订单子流程", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtOrderChildProcessVo brtOrderChildProcessVo) {
        return AjaxResult.success(brtOrderChildProcessService.updateBrtOrderChildProcess(brtOrderChildProcessVo));
    }

    /**
     * @description: TODO 删除订单子流程
     * @author: Fgn
     * @date: 2024-06-21
     * @param:
     * @param: childIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderChildProcess:remove')")
    @Log(title = "订单子流程", businessType = BusinessType.DELETE)
	@DeleteMapping("/{childIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] childIds){
        return toAjax(brtOrderChildProcessService.deleteBrtOrderChildProcessByChildIds(childIds));
    }

}
