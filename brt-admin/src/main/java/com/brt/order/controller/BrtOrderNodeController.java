package com.brt.order.controller;

import com.brt.common.annotation.RepeatSubmit;
import com.brt.common.annotation.RequestSingleParam;
import com.brt.common.enums.BusinessType;
import com.brt.common.enums.NodeTypeEnums;
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
import com.brt.order.vo.BrtOrderNodeVo;
import com.brt.order.service.IBrtOrderNodeService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 订单流程节点Controller
 *
 * @author Fgn
 * @date 2024-05-10
 */
@RestController
@RequestMapping("/order/orderNode")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtOrderNodeController extends BaseController {

    private final IBrtOrderNodeService brtOrderNodeService;

    /**
     * @description: TODO 分页查询订单流程节点列表
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: brtOrderNodeVo 订单流程节点
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtOrderNodeVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderNode:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtOrderNodeVo> list(BrtOrderNodeVo brtOrderNodeVo) {
        return brtOrderNodeService.queryBrtOrderNodeList(brtOrderNodeVo);
    }

    /**
     * @description: TODO 查询我的任务
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: brtOrderNodeVo 订单流程节点
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtOrderNodeVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderNode:list')")
    @GetMapping("/myTask")
    public TableDataInfo<BrtOrderNodeVo> myTask(BrtOrderNodeVo brtOrderNodeVo) {
        return brtOrderNodeService.myTask(brtOrderNodeVo);
    }

    /**
     * @description: TODO 查询全部订单流程节点列表
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: brtOrderNodeVo 订单流程节点
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderNode:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtOrderNodeVo brtOrderNodeVo) {
        return AjaxResult.success("查询成功", brtOrderNodeService.queryBrtOrderNodeAll(brtOrderNodeVo));
    }

    /**
     * @description: TODO 导出订单流程节点列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtOrderNodeVo 订单流程节点
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderNode:export')")
    @Log(title = "订单流程节点", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtOrderNodeVo brtOrderNodeVo){
        List<BrtOrderNodeVo> list = brtOrderNodeService.queryBrtOrderNodeAll(brtOrderNodeVo);
        ExcelUtil<BrtOrderNodeVo> util = new ExcelUtil<BrtOrderNodeVo>(BrtOrderNodeVo.class);
        util.exportExcel(response, list, "订单流程节点数据");
    }


    /**
     * @description: TODO 获取订单流程节点详细信息
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderNode:query')")
    @GetMapping(value = "/{orderNodeId}")
    public AjaxResult getInfo(@PathVariable("orderNodeId") String orderNodeId) {
        return success(brtOrderNodeService.queryBrtOrderNodeByOrderNodeId(orderNodeId));
    }

    /**
     * @description: TODO 新增订单流程节点
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: brtOrderNodeVo 订单流程节点
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderNode:add')")
    @Log(title = "订单流程节点", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtOrderNodeVo brtOrderNodeVo) {
        return AjaxResult.success(brtOrderNodeService.insertBrtOrderNode(brtOrderNodeVo));
    }

    /**
     * @description: TODO 修改订单流程节点
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: brtOrderNodeVo 订单流程节点
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:orderNode:edit')")
    @Log(title = "订单流程节点", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtOrderNodeVo brtOrderNodeVo) {
        return AjaxResult.success(brtOrderNodeService.updateBrtOrderNode(brtOrderNodeVo));
    }

    /**
     * @description: TODO 删除订单流程节点
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: orderNodeIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderNode:remove')")
    @Log(title = "订单流程节点", businessType = BusinessType.DELETE)
	@DeleteMapping("/{orderNodeIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] orderNodeIds){
        return toAjax(brtOrderNodeService.deleteBrtOrderNodeByOrderNodeIds(orderNodeIds));
    }

    /**
     * @description: TODO 完成节点
     * @author: FanGN
     * @date: 10:36 2024/6/29
     * @param:
     * @param orderId
     * @param orderNodeId
     * @param nodeType
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderNode:complate')")
    @PostMapping("/complateNode")
    @RepeatSubmit
    public AjaxResult complateNode(@RequestSingleParam("orderId") String orderId,
                                   @RequestSingleParam("nodeId") String orderNodeId,
                                   @RequestSingleParam("nodeType") String nodeType,
                                   @RequestSingleParam("nodeRemark") String nodeRemark){
        brtOrderNodeService.submitRemark(orderId,orderNodeId,nodeRemark);
        return toAjax(brtOrderNodeService.completeNode(orderId,orderNodeId, NodeTypeEnums.状态纪录任务));
    }

    /**
     * @description: TODO 修改备注
     * @author: FanGN
     * @date: 10:36 2024/6/29
     * @param:
     * @param orderId
     * @param orderNodeId
     * @param remark
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
    @PostMapping("/submitRemark")
    public AjaxResult submitRemark(@RequestSingleParam("orderId") String orderId,
                                   @RequestSingleParam("orderNodeId") String orderNodeId,
                                   @RequestSingleParam("remark") String remark){

        brtOrderNodeService.submitRemark(orderId,orderNodeId,remark);
        return AjaxResult.success();
    }

}
