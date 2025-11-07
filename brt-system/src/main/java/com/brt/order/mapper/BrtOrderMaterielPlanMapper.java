package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtOrderMaterielPlan;
import com.brt.order.vo.BrtOrderMaterielPlanVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物料数量计划Mapper接口
 * 
 * @author Fgn
 * @date 2024-06-20
 */
public interface BrtOrderMaterielPlanMapper extends BaseMapper<BrtOrderMaterielPlan> {

    /**
     * @description: TODO 分页查询物料数量计划列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: page
     * @param: brtOrderMaterielPlanVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtOrderMaterielPlanVo> queryBrtOrderMaterielPlanList(Page<?> page, @Param("brtOrderMaterielPlanVo") BrtOrderMaterielPlanVo brtOrderMaterielPlanVo);

    /**
     * @description: TODO 查询物料数量计划列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderMaterielPlanVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtOrderMaterielPlanVo> queryBrtOrderMaterielPlanList(@Param("brtOrderMaterielPlanVo") BrtOrderMaterielPlanVo brtOrderMaterielPlanVo);

    /**
     * @description: TODO 根据planId查询物料数量计划
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @return:
     * @return Vo
     **/
    BrtOrderMaterielPlanVo queryBrtOrderMaterielPlanByPlanId(@Param("PlanId") String planId);

}
