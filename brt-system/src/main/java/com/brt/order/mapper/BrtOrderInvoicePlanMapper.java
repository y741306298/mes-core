package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtOrderInvoicePlan;
import com.brt.order.vo.BrtOrderInvoicePlanVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 开票计划Mapper接口
 * 
 * @author Fgn
 * @date 2024-06-19
 */
public interface BrtOrderInvoicePlanMapper extends BaseMapper<BrtOrderInvoicePlan> {

    /**
     * @description: TODO 分页查询开票计划列表
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @param: page
     * @param: brtOrderInvoicePlanVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtOrderInvoicePlanVo> queryBrtOrderInvoicePlanList(Page<?> page, @Param("brtOrderInvoicePlanVo") BrtOrderInvoicePlanVo brtOrderInvoicePlanVo);

    /**
     * @description: TODO 查询开票计划列表
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @param: brtOrderInvoicePlanVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtOrderInvoicePlanVo> queryBrtOrderInvoicePlanList(@Param("brtOrderInvoicePlanVo") BrtOrderInvoicePlanVo brtOrderInvoicePlanVo);

    /**
     * @description: TODO 根据planId查询开票计划
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @return:
     * @return Vo
     **/
    BrtOrderInvoicePlanVo queryBrtOrderInvoicePlanByPlanId(@Param("PlanId") String planId);

}
