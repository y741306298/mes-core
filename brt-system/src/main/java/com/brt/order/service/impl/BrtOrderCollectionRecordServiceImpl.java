package com.brt.order.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.enums.NodeTypeEnums;
import com.brt.common.exception.ServiceException;
import com.brt.common.utils.PageUtils;
import com.brt.common.utils.SecurityUtils;
import com.brt.order.domain.BrtOrderCollectionPlan;
import com.brt.order.domain.BrtOrderCollectionRecord;
import com.brt.order.domain.BrtOrderDynamic;
import com.brt.order.service.IBrtOrderCollectionPlanService;
import com.brt.order.service.IBrtOrderDynamicService;
import com.brt.order.service.IBrtOrderNodeService;
import com.brt.order.vo.BrtOrderCollectionPlanVo;
import com.brt.order.vo.BrtOrderCollectionRecordVo;
import com.brt.order.mapper.BrtOrderCollectionRecordMapper;
import com.brt.order.service.IBrtOrderCollectionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * 收款记录Service业务层处理
 * 
 * @author Fgn
 * @date 2024-06-20
 */
@Service
public class BrtOrderCollectionRecordServiceImpl extends ServiceImpl<BrtOrderCollectionRecordMapper, BrtOrderCollectionRecord> implements IBrtOrderCollectionRecordService {

    @Autowired
    private IBrtOrderCollectionPlanService orderCollectionPlanService;

    @Autowired
    private IBrtOrderDynamicService orderDynamicService;

    @Autowired
    private IBrtOrderNodeService orderNodeService;

    @Override
    public TableDataInfo<BrtOrderCollectionRecordVo> queryBrtOrderCollectionRecordList(BrtOrderCollectionRecordVo brtOrderCollectionRecordVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtOrderCollectionRecordList(PageUtils.buildPage(), brtOrderCollectionRecordVo));
    }

    @Override
    public List<BrtOrderCollectionRecordVo> queryBrtOrderCollectionRecordAll(BrtOrderCollectionRecordVo brtOrderCollectionRecordVo) {
        return this.baseMapper.queryBrtOrderCollectionRecordList(brtOrderCollectionRecordVo);
    }

    @Override
    public BrtOrderCollectionRecordVo queryBrtOrderCollectionRecordByRecordId(String recordId) {
        return this.baseMapper.queryBrtOrderCollectionRecordByRecordId(recordId);
    }

    @Transactional
    @Override
    public BrtOrderCollectionRecordVo insertBrtOrderCollectionRecord(BrtOrderCollectionRecordVo brtOrderCollectionRecordVo) {
        saveBefore(brtOrderCollectionRecordVo);

        // 校验之前的的节点是否已完成 并且是否是本人执行
        orderNodeService.isAheadComplate(brtOrderCollectionRecordVo.getOrderNodeId());

        BrtOrderCollectionPlan orderCollectionPlan = orderCollectionPlanService.getOne(new LambdaQueryWrapper<BrtOrderCollectionPlan>().eq(BrtOrderCollectionPlan::getOrderId,brtOrderCollectionRecordVo.getOrderId()).eq(BrtOrderCollectionPlan::getOrderNodeId,brtOrderCollectionRecordVo.getOrderNodeId()).last(" limit 1"));
        orderCollectionPlan.setCollectionAmount(orderCollectionPlan.getCollectionAmount().add(brtOrderCollectionRecordVo.getCollectionAmount()));
        orderCollectionPlanService.updateById(orderCollectionPlan);

        // 计算剩余收款金额
        brtOrderCollectionRecordVo.setSurplusAmount(orderCollectionPlan.getPlanAmount().subtract(orderCollectionPlan.getCollectionAmount()));
        int i = this.baseMapper.insert(brtOrderCollectionRecordVo);

        // 保存动态信息
        BrtOrderDynamic orderDynamic = new BrtOrderDynamic();
        orderDynamic.setOrderId(brtOrderCollectionRecordVo.getOrderId());
        orderDynamic.setOrderNodeId(brtOrderCollectionRecordVo.getOrderNodeId());
        orderDynamic.setNodeId(brtOrderCollectionRecordVo.getNodeId());
        orderDynamic.setUserId(SecurityUtils.getUserId().toString());
        orderDynamic.setDynamicContent("收款"+brtOrderCollectionRecordVo.getCollectionAmount());
        orderDynamicService.save(orderDynamic);

        if (brtOrderCollectionRecordVo.getSurplusAmount().compareTo(BigDecimal.ZERO) < 0){
            throw new ServiceException("收款金额不允许大于订单金额");
        }

        // 如果剩余收款金额等于0 表示节点已完成
        if (brtOrderCollectionRecordVo.getSurplusAmount().compareTo(BigDecimal.ZERO) == 0){
            orderNodeService.completeNode(brtOrderCollectionRecordVo.getOrderId(),brtOrderCollectionRecordVo.getOrderNodeId(), NodeTypeEnums.收款金额纪录任务);
        }
        return brtOrderCollectionRecordVo;
    }

    @Transactional
    @Override
    public BrtOrderCollectionRecordVo updateBrtOrderCollectionRecord(BrtOrderCollectionRecordVo brtOrderCollectionRecordVo) {
        saveBefore(brtOrderCollectionRecordVo);
        int i = this.baseMapper.updateById(brtOrderCollectionRecordVo);
        return brtOrderCollectionRecordVo;
    }

    @Transactional
    @Override
    public int deleteBrtOrderCollectionRecordByRecordIds(String[] recordIds) {
        // 查询要删除的收款记录
        List<BrtOrderCollectionRecord> orderCollectionRecordList = this.listByIds(Arrays.asList(recordIds));
        if (ObjectUtil.isNotEmpty(orderCollectionRecordList)){
            // 查询收款计划
            BrtOrderCollectionRecord orderCollectionRecord = orderCollectionRecordList.get(0);
            BrtOrderCollectionPlan orderCollectionPlan = orderCollectionPlanService.getOne(new LambdaQueryWrapper<BrtOrderCollectionPlan>().eq(BrtOrderCollectionPlan::getOrderId, orderCollectionRecord.getOrderId()).eq(BrtOrderCollectionPlan::getOrderNodeId,orderCollectionRecord.getOrderNodeId()).last(" limit 1"));

            BigDecimal collectionAmount = orderCollectionRecordList.stream().map(BrtOrderCollectionRecord::getCollectionAmount).reduce(BigDecimal::add).get();

            orderCollectionPlan.setCollectionAmount(orderCollectionPlan.getCollectionAmount().subtract(collectionAmount));
            orderCollectionPlanService.updateById(orderCollectionPlan);
        }

        return this.baseMapper.deleteBatchIds(Arrays.asList(recordIds));
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param brtOrderCollectionRecordVo
     * @return:
     **/
    public void saveBefore(BrtOrderCollectionRecordVo brtOrderCollectionRecordVo){

    }

}
