package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtOrderMaterielPlan;
import com.brt.order.vo.BrtOrderMaterielPlanVo;

import java.util.List;

/**
 * 物料数量计划Service接口
 * 
 * @author Fgn
 * @date 2024-06-20
 */
public interface IBrtOrderMaterielPlanService extends IService<BrtOrderMaterielPlan> {

    /**
     * @description: TODO 分页查询物料数量计划列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderMaterielPlanVo 物料数量计划
     * @return:
     * @return TableDataInfo<BrtOrderMaterielPlanVo>
     **/
    public TableDataInfo<BrtOrderMaterielPlanVo> queryBrtOrderMaterielPlanList(BrtOrderMaterielPlanVo brtOrderMaterielPlanVo);

    /**
     * @description: TODO 查询全部物料数量计划列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderMaterielPlanVo 物料数量计划
     * @return:
     * @return java.util.List<BrtOrderMaterielPlanVo>
     **/
    public List<BrtOrderMaterielPlanVo> queryBrtOrderMaterielPlanAll(BrtOrderMaterielPlanVo brtOrderMaterielPlanVo);

    /**
     * @description: TODO 根据planId查询物料数量计划
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: planId
     * @return:
     * @return BrtOrderMaterielPlanVo
     **/
    public BrtOrderMaterielPlanVo queryBrtOrderMaterielPlanByPlanId(String planId);

    /**
     * @description: TODO 新增物料数量计划
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderMaterielPlanVo 物料数量计划
     * @return:
     * @return int
     **/
    public BrtOrderMaterielPlanVo insertBrtOrderMaterielPlan(BrtOrderMaterielPlanVo brtOrderMaterielPlanVo);

    /**
     * @description: TODO 修改物料数量计划
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderMaterielPlanVo 物料数量计划
     * @return:
     * @return int
     **/
    public BrtOrderMaterielPlanVo updateBrtOrderMaterielPlan(BrtOrderMaterielPlanVo brtOrderMaterielPlanVo);

    /**
     * @description: TODO 批量删除物料数量计划
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param:  planIds 需要删除的物料数量计划主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtOrderMaterielPlanByPlanIds(String[] planIds);

}
