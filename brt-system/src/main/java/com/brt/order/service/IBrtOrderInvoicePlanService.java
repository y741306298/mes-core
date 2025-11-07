package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtOrderInvoicePlan;
import com.brt.order.vo.BrtOrderInvoicePlanVo;

import java.util.List;

/**
 * 开票计划Service接口
 * 
 * @author Fgn
 * @date 2024-06-19
 */
public interface IBrtOrderInvoicePlanService extends IService<BrtOrderInvoicePlan> {

    /**
     * @description: TODO 分页查询开票计划列表
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @param: brtOrderInvoicePlanVo 开票计划
     * @return:
     * @return TableDataInfo<BrtOrderInvoicePlanVo>
     **/
    public TableDataInfo<BrtOrderInvoicePlanVo> queryBrtOrderInvoicePlanList(BrtOrderInvoicePlanVo brtOrderInvoicePlanVo);

    /**
     * @description: TODO 查询全部开票计划列表
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @param: brtOrderInvoicePlanVo 开票计划
     * @return:
     * @return java.util.List<BrtOrderInvoicePlanVo>
     **/
    public List<BrtOrderInvoicePlanVo> queryBrtOrderInvoicePlanAll(BrtOrderInvoicePlanVo brtOrderInvoicePlanVo);

    /**
     * @description: TODO 根据planId查询开票计划
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @param: planId
     * @return:
     * @return BrtOrderInvoicePlanVo
     **/
    public BrtOrderInvoicePlanVo queryBrtOrderInvoicePlanByPlanId(String planId);

    /**
     * @description: TODO 新增开票计划
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @param: brtOrderInvoicePlanVo 开票计划
     * @return:
     * @return int
     **/
    public BrtOrderInvoicePlanVo insertBrtOrderInvoicePlan(BrtOrderInvoicePlanVo brtOrderInvoicePlanVo);

    /**
     * @description: TODO 修改开票计划
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @param: brtOrderInvoicePlanVo 开票计划
     * @return:
     * @return int
     **/
    public BrtOrderInvoicePlanVo updateBrtOrderInvoicePlan(BrtOrderInvoicePlanVo brtOrderInvoicePlanVo);

    /**
     * @description: TODO 批量删除开票计划
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @param:  planIds 需要删除的开票计划主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtOrderInvoicePlanByPlanIds(String[] planIds);

}
