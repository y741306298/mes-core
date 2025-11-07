package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtOrderCollectionPlan;
import com.brt.order.vo.BrtOrderCollectionPlanVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收款计划Mapper接口
 * 
 * @author Fgn
 * @date 2024-06-20
 */
public interface BrtOrderCollectionPlanMapper extends BaseMapper<BrtOrderCollectionPlan> {

    /**
     * @description: TODO 分页查询收款计划列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: page
     * @param: brtOrderCollectionPlanVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtOrderCollectionPlanVo> queryBrtOrderCollectionPlanList(Page<?> page, @Param("brtOrderCollectionPlanVo") BrtOrderCollectionPlanVo brtOrderCollectionPlanVo);

    /**
     * @description: TODO 查询收款计划列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderCollectionPlanVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtOrderCollectionPlanVo> queryBrtOrderCollectionPlanList(@Param("brtOrderCollectionPlanVo") BrtOrderCollectionPlanVo brtOrderCollectionPlanVo);

    /**
     * @description: TODO 根据planId查询收款计划
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @return:
     * @return Vo
     **/
    BrtOrderCollectionPlanVo queryBrtOrderCollectionPlanByPlanId(@Param("PlanId") String planId);

}
