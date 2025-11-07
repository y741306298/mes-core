package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtOrderCollectionPlan;
import com.brt.order.vo.BrtOrderCollectionPlanVo;

import java.util.List;

/**
 * 收款计划Service接口
 * 
 * @author Fgn
 * @date 2024-06-20
 */
public interface IBrtOrderCollectionPlanService extends IService<BrtOrderCollectionPlan> {

    /**
     * @description: TODO 分页查询收款计划列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderCollectionPlanVo 收款计划
     * @return:
     * @return TableDataInfo<BrtOrderCollectionPlanVo>
     **/
    public TableDataInfo<BrtOrderCollectionPlanVo> queryBrtOrderCollectionPlanList(BrtOrderCollectionPlanVo brtOrderCollectionPlanVo);

    /**
     * @description: TODO 查询全部收款计划列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderCollectionPlanVo 收款计划
     * @return:
     * @return java.util.List<BrtOrderCollectionPlanVo>
     **/
    public List<BrtOrderCollectionPlanVo> queryBrtOrderCollectionPlanAll(BrtOrderCollectionPlanVo brtOrderCollectionPlanVo);

    /**
     * @description: TODO 根据planId查询收款计划
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: planId
     * @return:
     * @return BrtOrderCollectionPlanVo
     **/
    public BrtOrderCollectionPlanVo queryBrtOrderCollectionPlanByPlanId(String planId);

    /**
     * @description: TODO 新增收款计划
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderCollectionPlanVo 收款计划
     * @return:
     * @return int
     **/
    public BrtOrderCollectionPlanVo insertBrtOrderCollectionPlan(BrtOrderCollectionPlanVo brtOrderCollectionPlanVo);

    /**
     * @description: TODO 修改收款计划
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderCollectionPlanVo 收款计划
     * @return:
     * @return int
     **/
    public BrtOrderCollectionPlanVo updateBrtOrderCollectionPlan(BrtOrderCollectionPlanVo brtOrderCollectionPlanVo);

    /**
     * @description: TODO 批量删除收款计划
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param:  planIds 需要删除的收款计划主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtOrderCollectionPlanByPlanIds(String[] planIds);

}
