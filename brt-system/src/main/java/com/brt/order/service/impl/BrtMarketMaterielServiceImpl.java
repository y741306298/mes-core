package com.brt.order.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.enums.NodeOtherSettingEnums;
import com.brt.common.enums.NodeTypeEnums;
import com.brt.common.exception.ServiceException;
import com.brt.common.utils.PageUtils;
import com.brt.common.utils.SecurityUtils;
import com.brt.order.domain.*;
import com.brt.order.service.*;
import com.brt.order.vo.BrtMarketMaterielVo;
import com.brt.order.mapper.BrtMarketMaterielMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * 数量记录Service业务层处理
 *
 * @author Fgn
 * @date 2024-06-20
 */
@Service
public class BrtMarketMaterielServiceImpl extends ServiceImpl<BrtMarketMaterielMapper, BrtMarketMateriel> implements IBrtMarketMaterielService {

    @Autowired
    private IBrtOrderMaterielPlanService orderMaterielPlanService;

    @Autowired
    private IBrtOrderDynamicService orderDynamicService;

    @Autowired
    private IBrtOrderNodeService orderNodeService;

    @Autowired
    private IBrtFlowNodeService flowNodeService;

    @Autowired
    private IBrtMaterielService materielService;

    @Override
    public TableDataInfo<BrtMarketMaterielVo> queryBrtMarketMaterielList(BrtMarketMaterielVo brtMarketMaterielVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtMarketMaterielList(PageUtils.buildPage(), brtMarketMaterielVo));
    }

    @Override
    public List<BrtMarketMaterielVo> queryBrtMarketMaterielAll(BrtMarketMaterielVo brtMarketMaterielVo) {
        return this.baseMapper.queryBrtMarketMaterielList(brtMarketMaterielVo);
    }

    @Override
    public BrtMarketMaterielVo queryBrtMarketMaterielByRecordId(String recordId) {
        return this.baseMapper.queryBrtMarketMaterielByRecordId(recordId);
    }

    @Transactional
    @Override
    public BrtMarketMaterielVo insertBrtMarketMateriel(BrtMarketMaterielVo brtMarketMaterielVo) {
        saveBefore(brtMarketMaterielVo);
        int i = this.baseMapper.insert(brtMarketMaterielVo);
        return brtMarketMaterielVo;
    }

    @Transactional
    @Override
    public BrtMarketMaterielVo updateBrtMarketMateriel(BrtMarketMaterielVo brtMarketMaterielVo) {
        saveBefore(brtMarketMaterielVo);
        int i = this.baseMapper.updateById(brtMarketMaterielVo);
        return brtMarketMaterielVo;
    }

    @Transactional
    @Override
    public int deleteBrtMarketMaterielByRecordIds(String[] recordIds) {
        // 查询要删除的数量记录
        List<BrtMarketMateriel> marketMaterielList = this.listByIds(Arrays.asList(recordIds));
        if (ObjectUtil.isNotEmpty(marketMaterielList)){
            marketMaterielList.forEach(materielRecord -> {
                BrtOrderMaterielPlan orderMaterielPlan = orderMaterielPlanService.getOne(new LambdaQueryWrapper<BrtOrderMaterielPlan>().eq(BrtOrderMaterielPlan::getOrderId, materielRecord.getOrderId()).eq(BrtOrderMaterielPlan::getMaterielId, materielRecord.getMaterielId()).eq(BrtOrderMaterielPlan::getOrderNodeId,materielRecord.getOrderNodeId()).last(" limit 1"));
                orderMaterielPlan.setMaterielNum(orderMaterielPlan.getMaterielNum() - materielRecord.getMaterielNum());
                orderMaterielPlanService.updateById(orderMaterielPlan);
            });

            // 查询删除的记录对应的节点是否包含加库存操作
            List<String> orderNodeIdList = marketMaterielList.stream().map(BrtMarketMateriel::getOrderNodeId).collect(Collectors.toList());
            List<BrtOrderNode> orderNodeList = orderNodeService.list(new LambdaQueryWrapper<BrtOrderNode>().in(BrtOrderNode::getOrderNodeId, orderNodeIdList).apply("find_in_set({0},oper_setting)", NodeOtherSettingEnums.加库存.getCode()));
            if (ObjectUtil.isNotEmpty(orderNodeList)){
                orderNodeList.forEach(orderNode -> {
                    List<BrtMarketMateriel> materielRecordList = marketMaterielList.stream().filter(record -> orderNode.getOrderNodeId().equals(record.getOrderNodeId())).collect(Collectors.toList());
                    materielRecordList.forEach(materielRecord -> {
                        materielService.update(new LambdaUpdateWrapper<BrtMateriel>().setSql(" materiel_num = materiel_num - "+materielRecord.getMaterielNum()+" ").eq(BrtMateriel::getMaterielId,materielRecord.getMaterielId()));
                    });
                });
            }
        }
        return this.baseMapper.deleteBatchIds(Arrays.asList(recordIds));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertBrtMarketMaterielList(List<BrtMarketMaterielVo> brtMarketMaterielVoList) {

        if (ObjectUtil.isEmpty(brtMarketMaterielVoList)){
            return 0;
        }

        BrtMarketMaterielVo marketMaterielVo = brtMarketMaterielVoList.get(0);

        // 校验之前的的节点是否已完成 并且是否是本人执行
        orderNodeService.isAheadComplate(marketMaterielVo.getOrderNodeId());

        // 查询订单的数量计划任务
        List<BrtOrderMaterielPlan> orderMaterielPlanList = orderMaterielPlanService.list(new LambdaQueryWrapper<BrtOrderMaterielPlan>().eq(BrtOrderMaterielPlan::getOrderId, marketMaterielVo.getOrderId()).eq(BrtOrderMaterielPlan::getOrderNodeId,marketMaterielVo.getOrderNodeId()));

        // 查询节点信息
        BrtFlowNode flowNode = flowNodeService.getById(marketMaterielVo.getNodeId());
        // 查询订单的节点 有没有操作过扣库存操作
        Integer count = orderNodeService.count(new LambdaQueryWrapper<BrtOrderNode>().apply("find_in_set({0},oper_setting)",NodeOtherSettingEnums.加库存.getCode()).eq(BrtOrderNode::getOrderId, marketMaterielVo.getOrderId()).ne(BrtOrderNode::getOrderNodeId,marketMaterielVo.getOrderNodeId()));

        for (BrtMarketMaterielVo materielRecordVo : brtMarketMaterielVoList) {
            if (materielRecordVo.getMaterielNum() == null || materielRecordVo.getMaterielNum() <= 0){
                continue;
            }

            // 如果当前操作的节点包含加库存操作 并且其他节点没有操作的情况下才记录库存
            if (NodeOtherSettingEnums.isExistence(flowNode.getOtherSetting(),NodeOtherSettingEnums.加库存) && count <= 0){
                materielService.update(new LambdaUpdateWrapper<BrtMateriel>().setSql(" materiel_num = materiel_num + "+materielRecordVo.getMaterielNum()+" ").eq(BrtMateriel::getMaterielId,materielRecordVo.getMaterielId()));

                // 记录该节点已添加库存 防止其他节点继续添加
                orderNodeService.update(new LambdaUpdateWrapper<BrtOrderNode>().setSql(" oper_setting = concat(oper_setting,',',"+NodeOtherSettingEnums.加库存.getCode()+")").eq(BrtOrderNode::getOrderNodeId,marketMaterielVo.getOrderNodeId()));
            }

            // 获取产品对应的计划任务
            orderMaterielPlanList.forEach(orderMaterielPlan -> {
                if(orderMaterielPlan.getMaterielId().equals(materielRecordVo.getMaterielId())){

                    orderMaterielPlan.setMaterielNum(orderMaterielPlan.getMaterielNum() + materielRecordVo.getMaterielNum());
                    orderMaterielPlanService.updateById(orderMaterielPlan);

                    // 计算剩余数量
                    materielRecordVo.setSurplusNum(orderMaterielPlan.getPlanNum() - orderMaterielPlan.getMaterielNum());
                    if (materielRecordVo.getSurplusNum() < 0){
                        throw new ServiceException("数量不允许大于计划数量");
                    }

                    this.baseMapper.insert(materielRecordVo);

                    // 保存动态信息
                    BrtOrderDynamic orderDynamic = new BrtOrderDynamic();
                    orderDynamic.setOrderId(materielRecordVo.getOrderId());
                    orderDynamic.setOrderNodeId(materielRecordVo.getOrderNodeId());
                    orderDynamic.setNodeId(materielRecordVo.getNodeId());
                    orderDynamic.setUserId(SecurityUtils.getUserId().toString());
                    orderDynamic.setDynamicContent("产品记录"+materielRecordVo.getMaterielNum());
                    orderDynamicService.save(orderDynamic);
                }
            });

            // 判断是否所有的计划数量都已完成
            long notPlanCount = orderMaterielPlanList.stream().filter(plan -> plan.getMaterielNum() < plan.getPlanNum()).count();

            if (notPlanCount <= 0){
                orderNodeService.completeNode(marketMaterielVo.getOrderId(),marketMaterielVo.getOrderNodeId(), NodeTypeEnums.数量记录任务);
            }

        }
        return 0;
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param brtMarketMaterielVo
     * @return:
     **/
    public void saveBefore(BrtMarketMaterielVo brtMarketMaterielVo){

    }

}
