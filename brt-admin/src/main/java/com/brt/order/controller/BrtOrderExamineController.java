package com.brt.order.controller;

import com.brt.common.annotation.RepeatSubmit;
import com.brt.common.annotation.RequestSingleParam;
import com.brt.common.enums.BusinessType;
import com.brt.order.domain.BrtOrderNode;
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
import com.brt.order.vo.BrtOrderExamineVo;
import com.brt.order.service.IBrtOrderExamineService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 订单审批Controller
 *
 * @author Fgn
 * @date 2024-05-10
 */
@RestController
@RequestMapping("/order/orderExamine")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtOrderExamineController extends BaseController {

    private final IBrtOrderExamineService brtOrderExamineService;

    /**
     * @description: TODO 分页查询订单审批列表
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: brtOrderExamineVo 订单审批
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtOrderExamineVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderExamine:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtOrderExamineVo> list(BrtOrderExamineVo brtOrderExamineVo) {
        return brtOrderExamineService.queryBrtOrderExamineList(brtOrderExamineVo);
    }

    /**
     * @description: TODO 查询全部订单审批列表
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: brtOrderExamineVo 订单审批
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderExamine:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtOrderExamineVo brtOrderExamineVo) {
        return AjaxResult.success("查询成功", brtOrderExamineService.queryBrtOrderExamineAll(brtOrderExamineVo));
    }

    /**
     * @description: TODO 导出订单审批列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtOrderExamineVo 订单审批
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderExamine:export')")
    @Log(title = "订单审批", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtOrderExamineVo brtOrderExamineVo){
        List<BrtOrderExamineVo> list = brtOrderExamineService.queryBrtOrderExamineAll(brtOrderExamineVo);
        ExcelUtil<BrtOrderExamineVo> util = new ExcelUtil<BrtOrderExamineVo>(BrtOrderExamineVo.class);
        util.exportExcel(response, list, "订单审批数据");
    }


    /**
     * @description: TODO 获取订单审批详细信息
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderExamine:query')")
    @GetMapping(value = "/{examineId}")
    public AjaxResult getInfo(@PathVariable("examineId") String examineId) {
        return success(brtOrderExamineService.queryBrtOrderExamineByExamineId(examineId));
    }

    /**
     * @description: TODO 新增订单审批
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: brtOrderExamineVo 订单审批
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderExamine:add')")
    @Log(title = "订单审批", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtOrderExamineVo brtOrderExamineVo) {
        return AjaxResult.success(brtOrderExamineService.insertBrtOrderExamine(brtOrderExamineVo));
    }

    /**
     * @description: TODO 修改订单审批
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: brtOrderExamineVo 订单审批
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:orderExamine:edit')")
    @Log(title = "订单审批", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtOrderExamineVo brtOrderExamineVo) {
        return AjaxResult.success(brtOrderExamineService.updateBrtOrderExamine(brtOrderExamineVo));
    }

    /**
     * @description: TODO 删除订单审批
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: examineIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderExamine:remove')")
    @Log(title = "订单审批", businessType = BusinessType.DELETE)
	@DeleteMapping("/{examineIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] examineIds){
        return toAjax(brtOrderExamineService.deleteBrtOrderExamineByExamineIds(examineIds));
    }

    /**
     * @description: TODO 订单审批
     * @author: FanGN
     * @date: 18:34 2024/5/10
     * @param:
     * @param examineIds
     * @param auditStatus
     * @param auditRemark
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderExamine:audit')")
    @Log(title = "订单审批", businessType = BusinessType.UPDATE)
    @PostMapping("audit")
    @RepeatSubmit
    public AjaxResult audit(@RequestSingleParam("examineIds") String examineIds,
                            @RequestSingleParam("auditStatus") String auditStatus,
                            @RequestSingleParam("childId") String childId,
                            @RequestSingleParam("auditRemark") String auditRemark) {
        return AjaxResult.success(brtOrderExamineService.audit(examineIds,auditStatus,childId,auditRemark));
    }

    /**
     * @description: TODO 根据节点审批订单
     * @author: FanGN
     * @date: 18:54 2024/5/20
     * @param:
     * @param orderNode
     * @param auditStatus
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderExamine:audit')")
    @Log(title = "订单审批", businessType = BusinessType.UPDATE)
    @PostMapping("nodeAudit")
    @RepeatSubmit
    public AjaxResult nodeAudit(@RequestBody BrtOrderNode orderNode,@RequestSingleParam("childId")String childId,@RequestSingleParam("auditStatus") String auditStatus) {
        return AjaxResult.success(brtOrderExamineService.nodeAudit(orderNode,childId,auditStatus));
    }


    /**
     * @description: TODO 获取全部订单列表
     * @author: FanGN
     * @date: 00:51 2024/5/20
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
    @GetMapping(value = "/orderAllList")
    public AjaxResult orderAllList() {
        return success(brtOrderExamineService.orderAllList());
    }
}
