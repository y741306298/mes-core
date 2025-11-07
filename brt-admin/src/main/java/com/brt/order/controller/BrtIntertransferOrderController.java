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
import com.brt.order.vo.BrtIntertransferOrderVo;
import com.brt.order.service.IBrtIntertransferOrderService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 互转单Controller
 *
 * @author Fgn
 * @date 2024-05-16
 */
@RestController
@RequestMapping("/order/intertransferOrder")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtIntertransferOrderController extends BaseController {

    private final IBrtIntertransferOrderService brtIntertransferOrderService;

    /**
     * @description: TODO 分页查询互转单列表
     * @author: Fgn
     * @date: 2024-05-16
     * @param:
     * @param: brtIntertransferOrderVo 互转单
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtIntertransferOrderVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:intertransferOrder:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtIntertransferOrderVo> list(BrtIntertransferOrderVo brtIntertransferOrderVo) {
        return brtIntertransferOrderService.queryBrtIntertransferOrderList(brtIntertransferOrderVo);
    }

    /**
     * @description: TODO 查询全部互转单列表
     * @author: Fgn
     * @date: 2024-05-16
     * @param:
     * @param: brtIntertransferOrderVo 互转单
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:intertransferOrder:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtIntertransferOrderVo brtIntertransferOrderVo) {
        return AjaxResult.success("查询成功", brtIntertransferOrderService.queryBrtIntertransferOrderAll(brtIntertransferOrderVo));
    }

    /**
     * @description: TODO 导出互转单列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtIntertransferOrderVo 互转单
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:intertransferOrder:export')")
    @Log(title = "互转单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtIntertransferOrderVo brtIntertransferOrderVo){
        List<BrtIntertransferOrderVo> list = brtIntertransferOrderService.queryBrtIntertransferOrderAll(brtIntertransferOrderVo);
        ExcelUtil<BrtIntertransferOrderVo> util = new ExcelUtil<BrtIntertransferOrderVo>(BrtIntertransferOrderVo.class);
        util.exportExcel(response, list, "互转单数据");
    }


    /**
     * @description: TODO 获取互转单详细信息
     * @author: Fgn
     * @date: 2024-05-16
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:intertransferOrder:query')")
    @GetMapping(value = "/{intertransferId}")
    public AjaxResult getInfo(@PathVariable("intertransferId") String intertransferId) {
        return success(brtIntertransferOrderService.queryBrtIntertransferOrderByIntertransferId(intertransferId));
    }

    /**
     * @description: TODO 新增互转单
     * @author: Fgn
     * @date: 2024-05-16
     * @param:
     * @param: brtIntertransferOrderVo 互转单
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:intertransferOrder:add')")
    @Log(title = "互转单", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtIntertransferOrderVo brtIntertransferOrderVo) {
        return AjaxResult.success(brtIntertransferOrderService.insertBrtIntertransferOrder(brtIntertransferOrderVo));
    }

    /**
     * @description: TODO 修改互转单
     * @author: Fgn
     * @date: 2024-05-16
     * @param:
     * @param: brtIntertransferOrderVo 互转单
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:intertransferOrder:edit')")
    @Log(title = "互转单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtIntertransferOrderVo brtIntertransferOrderVo) {
        return AjaxResult.success(brtIntertransferOrderService.updateBrtIntertransferOrder(brtIntertransferOrderVo));
    }

    /**
     * @description: TODO 删除互转单
     * @author: Fgn
     * @date: 2024-05-16
     * @param:
     * @param: intertransferIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:intertransferOrder:remove')")
    @Log(title = "互转单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{intertransferIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] intertransferIds){
        return toAjax(brtIntertransferOrderService.deleteBrtIntertransferOrderByIntertransferIds(intertransferIds));
    }

}
