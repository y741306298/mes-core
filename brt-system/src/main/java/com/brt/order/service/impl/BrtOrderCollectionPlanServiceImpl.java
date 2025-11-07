package com.brt.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.utils.PageUtils;
import com.brt.order.domain.BrtOrderCollectionPlan;
import com.brt.order.vo.BrtOrderCollectionPlanVo;
import com.brt.order.mapper.BrtOrderCollectionPlanMapper;
import com.brt.order.service.IBrtOrderCollectionPlanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;

/**
 * 收款计划Service业务层处理
 * 
 * @author Fgn
 * @date 2024-06-20
 */
@Service
public class BrtOrderCollectionPlanServiceImpl extends ServiceImpl<BrtOrderCollectionPlanMapper, BrtOrderCollectionPlan> implements IBrtOrderCollectionPlanService {

    @Override
    public TableDataInfo<BrtOrderCollectionPlanVo> queryBrtOrderCollectionPlanList(BrtOrderCollectionPlanVo brtOrderCollectionPlanVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtOrderCollectionPlanList(PageUtils.buildPage(), brtOrderCollectionPlanVo));
    }

    @Override
    public List<BrtOrderCollectionPlanVo> queryBrtOrderCollectionPlanAll(BrtOrderCollectionPlanVo brtOrderCollectionPlanVo) {
        return this.baseMapper.queryBrtOrderCollectionPlanList(brtOrderCollectionPlanVo);
    }

    @Override
    public BrtOrderCollectionPlanVo queryBrtOrderCollectionPlanByPlanId(String planId) {
        return this.baseMapper.queryBrtOrderCollectionPlanByPlanId(planId);
    }

    @Transactional
    @Override
    public BrtOrderCollectionPlanVo insertBrtOrderCollectionPlan(BrtOrderCollectionPlanVo brtOrderCollectionPlanVo) {
        saveBefore(brtOrderCollectionPlanVo);
        int i = this.baseMapper.insert(brtOrderCollectionPlanVo);
        return brtOrderCollectionPlanVo;
    }

    @Transactional
    @Override
    public BrtOrderCollectionPlanVo updateBrtOrderCollectionPlan(BrtOrderCollectionPlanVo brtOrderCollectionPlanVo) {
        saveBefore(brtOrderCollectionPlanVo);
        int i = this.baseMapper.updateById(brtOrderCollectionPlanVo);
        return brtOrderCollectionPlanVo;
    }

    @Transactional
    @Override
    public int deleteBrtOrderCollectionPlanByPlanIds(String[] planIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(planIds));
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param brtOrderCollectionPlanVo
     * @return:
     **/
    public void saveBefore(BrtOrderCollectionPlanVo brtOrderCollectionPlanVo){

    }

}
