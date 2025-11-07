package com.brt.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.utils.PageUtils;
import com.brt.order.domain.BrtOrderInvoicePlan;
import com.brt.order.vo.BrtOrderInvoicePlanVo;
import com.brt.order.mapper.BrtOrderInvoicePlanMapper;
import com.brt.order.service.IBrtOrderInvoicePlanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;

/**
 * 开票计划Service业务层处理
 * 
 * @author Fgn
 * @date 2024-06-19
 */
@Service
public class BrtOrderInvoicePlanServiceImpl extends ServiceImpl<BrtOrderInvoicePlanMapper, BrtOrderInvoicePlan> implements IBrtOrderInvoicePlanService {

    @Override
    public TableDataInfo<BrtOrderInvoicePlanVo> queryBrtOrderInvoicePlanList(BrtOrderInvoicePlanVo brtOrderInvoicePlanVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtOrderInvoicePlanList(PageUtils.buildPage(), brtOrderInvoicePlanVo));
    }

    @Override
    public List<BrtOrderInvoicePlanVo> queryBrtOrderInvoicePlanAll(BrtOrderInvoicePlanVo brtOrderInvoicePlanVo) {
        return this.baseMapper.queryBrtOrderInvoicePlanList(brtOrderInvoicePlanVo);
    }

    @Override
    public BrtOrderInvoicePlanVo queryBrtOrderInvoicePlanByPlanId(String planId) {
        return this.baseMapper.queryBrtOrderInvoicePlanByPlanId(planId);
    }

    @Transactional
    @Override
    public BrtOrderInvoicePlanVo insertBrtOrderInvoicePlan(BrtOrderInvoicePlanVo brtOrderInvoicePlanVo) {
        saveBefore(brtOrderInvoicePlanVo);
        int i = this.baseMapper.insert(brtOrderInvoicePlanVo);
        return brtOrderInvoicePlanVo;
    }

    @Transactional
    @Override
    public BrtOrderInvoicePlanVo updateBrtOrderInvoicePlan(BrtOrderInvoicePlanVo brtOrderInvoicePlanVo) {
        saveBefore(brtOrderInvoicePlanVo);
        int i = this.baseMapper.updateById(brtOrderInvoicePlanVo);
        return brtOrderInvoicePlanVo;
    }

    @Transactional
    @Override
    public int deleteBrtOrderInvoicePlanByPlanIds(String[] planIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(planIds));
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @param brtOrderInvoicePlanVo
     * @return:
     **/
    public void saveBefore(BrtOrderInvoicePlanVo brtOrderInvoicePlanVo){

    }

}
