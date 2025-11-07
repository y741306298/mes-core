package com.brt.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.utils.PageUtils;
import com.brt.order.domain.BrtOrderMaterielPlan;
import com.brt.order.vo.BrtOrderMaterielPlanVo;
import com.brt.order.mapper.BrtOrderMaterielPlanMapper;
import com.brt.order.service.IBrtOrderMaterielPlanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;

/**
 * 物料数量计划Service业务层处理
 *
 * @author Fgn
 * @date 2024-06-20
 */
@Service
public class BrtOrderMaterielPlanServiceImpl extends ServiceImpl<BrtOrderMaterielPlanMapper, BrtOrderMaterielPlan> implements IBrtOrderMaterielPlanService {

    @Override
    public TableDataInfo<BrtOrderMaterielPlanVo> queryBrtOrderMaterielPlanList(BrtOrderMaterielPlanVo brtOrderMaterielPlanVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtOrderMaterielPlanList(PageUtils.buildPage(), brtOrderMaterielPlanVo));
    }

    @Override
    public List<BrtOrderMaterielPlanVo> queryBrtOrderMaterielPlanAll(BrtOrderMaterielPlanVo brtOrderMaterielPlanVo) {
        return this.baseMapper.queryBrtOrderMaterielPlanList(brtOrderMaterielPlanVo);
    }

    @Override
    public BrtOrderMaterielPlanVo queryBrtOrderMaterielPlanByPlanId(String planId) {
        return this.baseMapper.queryBrtOrderMaterielPlanByPlanId(planId);
    }

    @Transactional
    @Override
    public BrtOrderMaterielPlanVo insertBrtOrderMaterielPlan(BrtOrderMaterielPlanVo brtOrderMaterielPlanVo) {
        saveBefore(brtOrderMaterielPlanVo);
        int i = this.baseMapper.insert(brtOrderMaterielPlanVo);
        return brtOrderMaterielPlanVo;
    }

    @Transactional
    @Override
    public BrtOrderMaterielPlanVo updateBrtOrderMaterielPlan(BrtOrderMaterielPlanVo brtOrderMaterielPlanVo) {
        saveBefore(brtOrderMaterielPlanVo);
        int i = this.baseMapper.updateById(brtOrderMaterielPlanVo);
        return brtOrderMaterielPlanVo;
    }

    @Transactional
    @Override
    public int deleteBrtOrderMaterielPlanByPlanIds(String[] planIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(planIds));
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param brtOrderMaterielPlanVo
     * @return:
     **/
    public void saveBefore(BrtOrderMaterielPlanVo brtOrderMaterielPlanVo){

    }

}
