package com.brt.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.annotation.BrtDataFiltration;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.enums.*;
import com.brt.common.exception.ServiceException;
import com.brt.common.utils.PageUtils;
import com.brt.common.utils.SecurityUtils;
import com.brt.common.utils.StringUtils;
import com.brt.order.domain.*;
import com.brt.order.service.*;
import com.brt.order.utils.BrtOrderNoUtil;
import com.brt.order.vo.BrtCheckVo;
import com.brt.order.vo.BrtOrderInvoicePlanVo;
import com.brt.order.vo.BrtMarketOrderDetailsVo;
import com.brt.order.vo.BrtMarketOrderVo;
import com.brt.order.mapper.BrtMarketOrderMapper;
import com.brt.order.vo.pub.BrtOrderVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 销售单Service业务层处理
 *
 * @author Fgn
 * @date 2024-05-09
 */
@Service("brtMarketOrderService")
public class BrtMarketOrderServiceImpl extends ServiceImpl<BrtMarketOrderMapper, BrtMarketOrder> implements IBrtMarketOrderService {

    @Autowired
    private IBrtMarketOrderDetailsService marketOrderDetailsService;

    @Autowired
    private IBrtOrderExamineService orderExamineService;

    @Autowired
    private IBrtOrderTemplateService orderTemplateService;

    @Autowired
    private IBrtOrderNodeService orderNodeService;

    @Autowired
    private IBrtMaterielService materielService;

    @Autowired
    private IBrtOrderBoomService orderBoomService;

    @Autowired
    private IBrtOrderInvoicePlanService orderInvoicePlanService;

    @Autowired
    private IBrtOrderCollectionPlanService collectionPlanService;

    @Autowired
    private IBrtOrderMaterielPlanService materielPlanService;

    @Autowired
    private BrtOrderNoUtil orderNoUtil;


    @Override
    @BrtDataFiltration(perms = {"order:marketOrder:AllList"},field = "A.user_id")
    public TableDataInfo<BrtMarketOrderVo> queryBrtMarketOrderList(BrtMarketOrderVo brtMarketOrderVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtMarketOrderList(PageUtils.buildPage(), brtMarketOrderVo));
    }

    @Override
    public List<BrtMarketOrderVo> queryBrtMarketOrderAll(BrtMarketOrderVo brtMarketOrderVo) {
        return this.baseMapper.queryBrtMarketOrderList(brtMarketOrderVo);
    }

    @Override
    public BrtMarketOrderVo queryBrtMarketOrderByOrderId(String orderId) {
        return this.baseMapper.queryBrtMarketOrderByOrderId(orderId);
    }

    @Transactional
    @Override
    public BrtMarketOrderVo insertBrtMarketOrder(BrtMarketOrderVo brtMarketOrderVo) {
        saveBefore(brtMarketOrderVo);
        brtMarketOrderVo.setOrderNo(orderNoUtil.getNoAndAdd(OrderNoEnums.采购单));

        // 保存订单详情
        List<BrtMarketOrderDetailsVo> orderDetailsVoList = brtMarketOrderVo.getMarketOrderDetailsVoList();
        if (ObjectUtil.isEmpty(orderDetailsVoList)){
            throw new ServiceException("请至少添加一个产品信息");
        }

        //统计订单数量
        long toatlNum = orderDetailsVoList.stream().mapToLong(BrtMarketOrderDetailsVo::getDetailsNum).sum();
        brtMarketOrderVo.setTotalNum(toatlNum);

        //统计订单总金额
        BigDecimal totalAmount = orderDetailsVoList.stream().map(BrtMarketOrderDetailsVo::getDetailsAmount).reduce(BigDecimal::add).get();
        brtMarketOrderVo.setTotalAmount(totalAmount);
        brtMarketOrderVo.setUserId(SecurityUtils.getUserId().toString());

//        brtMarketOrderVo.setOrderNo(orderNoUtil.getNoAndAdd(OrderNoEnums.采购单));
        int i = this.baseMapper.insert(brtMarketOrderVo);

        orderDetailsVoList.stream().forEach(item -> {
            item.setOrderId(brtMarketOrderVo.getOrderId());

            if (ObjectUtil.isEmpty(item.getMaterielId())){
                item.setMaterielId(item.getMaterielName());
            }
            marketOrderDetailsService.save(item);

        });

        BrtOrderVo orderVo = new BrtOrderVo();
        orderVo.setMarketOrderVo(brtMarketOrderVo);
        orderVo.setOrderType(OrderTypeEnums.采购单);
        // 生成订单审批信息
        orderExamineService.createOrderExamine(orderVo,OrderTypeEnums.采购单,null);
        return brtMarketOrderVo;
    }

    @Transactional
    @Override
    public BrtMarketOrderVo updateBrtMarketOrder(BrtMarketOrderVo brtMarketOrderVo) {

        // 查询原订单信息
        BrtMarketOrder marketOrder = this.baseMapper.selectById(brtMarketOrderVo.getOrderId());

        // 保存订单详情
        List<BrtMarketOrderDetailsVo> orderDetailsVoList = brtMarketOrderVo.getMarketOrderDetailsVoList();

        //统计订单数量
        long toatlNum = orderDetailsVoList.stream().mapToLong(BrtMarketOrderDetailsVo::getDetailsNum).sum();
        brtMarketOrderVo.setTotalNum(toatlNum);

        //统计订单总金额
        BigDecimal totalAmount = orderDetailsVoList.stream().map(BrtMarketOrderDetailsVo::getDetailsAmount).reduce(BigDecimal::add).get();
        brtMarketOrderVo.setTotalAmount(totalAmount);
        brtMarketOrderVo.setUserId(SecurityUtils.getUserId().toString());

        orderDetailsVoList.stream().forEach(item -> {
            item.setOrderId(brtMarketOrderVo.getOrderId());
            marketOrderDetailsService.saveOrUpdate(item);
        });

        if (!marketOrder.getTemplateId().equals(brtMarketOrderVo.getTemplateId())){
            // 删除订单模板
            orderTemplateService.remove(new LambdaQueryWrapper<BrtOrderTemplate>().eq(BrtOrderTemplate::getOrderId,marketOrder.getOrderId()));
            orderNodeService.remove(new LambdaQueryWrapper<BrtOrderNode>().eq(BrtOrderNode::getOrderId,marketOrder.getOrderId()));
            orderExamineService.remove(new LambdaQueryWrapper<BrtOrderExamine>().eq(BrtOrderExamine::getOrderId,marketOrder.getOrderId()));

            BrtOrderVo orderVo = new BrtOrderVo();
            orderVo.setMarketOrderVo(brtMarketOrderVo);
            orderVo.setOrderType(OrderTypeEnums.采购单);
            // 生成订单审批信息
            orderExamineService.createOrderExamine(orderVo, OrderTypeEnums.采购单, null);
        }

        int i = this.baseMapper.updateById(brtMarketOrderVo);

        return brtMarketOrderVo;
    }

    @Transactional
    @Override
    public int deleteBrtMarketOrderByOrderIds(String[] orderIds) {
        // 删除订单模板
        orderTemplateService.remove(new LambdaQueryWrapper<BrtOrderTemplate>().in(BrtOrderTemplate::getOrderId,orderIds));
        orderNodeService.remove(new LambdaQueryWrapper<BrtOrderNode>().in(BrtOrderNode::getOrderId,orderIds));
        orderExamineService.remove(new LambdaQueryWrapper<BrtOrderExamine>().in(BrtOrderExamine::getOrderId,orderIds));

        // 删除boom单
        int boomRow = orderBoomService.removeByOrderIds(orderIds);

        // 删除订单详情
        int orderDetailsRow = marketOrderDetailsService.removeByOrderIds(orderIds);

        // 删除开票计划
        orderInvoicePlanService.remove(new LambdaQueryWrapper<BrtOrderInvoicePlan>().in(BrtOrderInvoicePlan::getOrderId,orderIds));

        // 删除收款计划
        collectionPlanService.remove(new LambdaQueryWrapper<BrtOrderCollectionPlan>().in(BrtOrderCollectionPlan::getOrderId,orderIds));

        // 删除数量计划
        materielPlanService.remove(new LambdaQueryWrapper<BrtOrderMaterielPlan>().in(BrtOrderMaterielPlan::getOrderId,orderIds));
        return this.baseMapper.deleteBatchIds(Arrays.asList(orderIds));
    }

    @Override
    public int orderCheck(BrtCheckVo checkVo) {
        // 查询订单信息
        BrtMarketOrder marketOrder = this.baseMapper.selectById(checkVo.getOrderId());
        if (ObjectUtil.isNotEmpty(marketOrder)){
            if (checkVo.getCheckType().equals(CheckTypeEnums.收入.getCode())){
                marketOrder.setCollectionAmount(marketOrder.getCollectionAmount().add(checkVo.getCheckAmount()));
            }else {
                marketOrder.setCollectionAmount(marketOrder.getCollectionAmount().subtract(checkVo.getCheckAmount()));
            }

            // 如果收款金额小于等于0 则表示未收款
            // 如果收款金额小于合同金额 则表示部分收款
            // 如果收款金额大于等于合同金额 则表示全部收款
            if (marketOrder.getCollectionAmount().compareTo(BigDecimal.ZERO) <= 0){
                marketOrder.setCollectionStatus(CollectionStatusEnums.未收款.getCode());

            }else if (marketOrder.getCollectionAmount().compareTo(marketOrder.getTotalAmount()) < 0){
                marketOrder.setCollectionStatus(CollectionStatusEnums.部分收款.getCode());

            }else if (marketOrder.getCollectionAmount().compareTo(marketOrder.getTotalAmount()) >= 0){
                marketOrder.setCollectionStatus(CollectionStatusEnums.已收款.getCode());
            }
            this.baseMapper.updateById(marketOrder);
        }
        return 0;
    }

    @Override
    public Map<String, Object> totalMarketOrder(String supplierId) {
        Map<String, Object> result = new HashMap<>();
        result.put("totalAmount",0);
        result.put("collectionAmount",0);
        result.put("notCollectionAmount",0);
        result.put("receivingNum",0);
        // 查询客户销售单信息
        List<BrtMarketOrder> marketOrderList = this.baseMapper.selectList(new LambdaQueryWrapper<BrtMarketOrder>().eq(BrtMarketOrder::getSupplierId, supplierId));
        if (ObjectUtil.isNotEmpty(marketOrderList)){
            //  统计总金额
            BigDecimal totalAmount = marketOrderList.stream().map(BrtMarketOrder::getTotalAmount).reduce(BigDecimal::add).get();
            result.put("totalAmount",totalAmount);

            //  统计已收款金额
            BigDecimal collectionAmount = marketOrderList.stream().map(BrtMarketOrder::getCollectionAmount).reduce(BigDecimal::add).get();
            result.put("collectionAmount",collectionAmount);

            // 未收款
            result.put("notCollectionAmount",totalAmount.subtract(collectionAmount));

            String receivingNum = this.baseMapper.selectReceivingNum(supplierId);
            if(StringUtils.isNotEmpty(receivingNum)){
                result.put("receivingNum",receivingNum);
            }
        }
        return result;
    }

    @Override
    public int copyMarketOrder(String orderId) {
        // 查询订单信息
        BrtMarketOrder marketOrder = this.baseMapper.selectById(orderId);
        BrtMarketOrderVo marketOrderVo = BeanUtil.copyProperties(marketOrder, BrtMarketOrderVo.class);
        //初始化订单信息
        marketOrderVo.setOrderId(null);
        marketOrderVo.setOrderNo(orderNoUtil.getNoAndAdd(OrderNoEnums.采购单));
        marketOrderVo.setCollectionStatus(CollectionStatusEnums.未收款.getCode());
        marketOrderVo.setCollectionAmount(BigDecimal.ZERO);
        marketOrderVo.setCreateTime(null);

        // 查询订单详情
        List<BrtMarketOrderDetails> orderDetailsList = marketOrderDetailsService.list(new LambdaQueryWrapper<BrtMarketOrderDetails>().eq(BrtMarketOrderDetails::getOrderId, orderId));
        List<BrtMarketOrderDetailsVo> marketOrderDetailsVoList = BeanUtil.copyToList(orderDetailsList, BrtMarketOrderDetailsVo.class);
        marketOrderDetailsVoList.forEach(item -> {
            item.setOrderId(null);
            item.setDetailsId(null);
        });
        marketOrderVo.setMarketOrderDetailsVoList(marketOrderDetailsVoList);

        insertBrtMarketOrder(marketOrderVo);
        return 0;
    }

    public BrtMarketOrderVo getOrderByTemplateId(String orderTemplateId){
        return baseMapper.getOrderByTemplateId(orderTemplateId);
    }

    /**
     * 修改状态
     * @param brtMarketOrderVo
     */
    public void updateStatus(BrtMarketOrderVo brtMarketOrderVo){
        this.baseMapper.updateStatus(brtMarketOrderVo);
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param brtMarketOrderVo
     * @return:
     **/
    public void saveBefore(BrtMarketOrderVo brtMarketOrderVo){

    }

    /**
     * 归档
     */
    public void beNotInUse(String orderId,String childId){
        if(StringUtils.isEmpty(childId)){
            BrtMarketOrder marketOrder = this.getById(orderId);
            marketOrder.setStatus(OrderAuditStatus.归档.getCode());
            this.updateById(marketOrder);
            orderTemplateService.update(null,new LambdaUpdateWrapper<BrtOrderTemplate>().set(BrtOrderTemplate::getStatus,"5").eq(BrtOrderTemplate::getOrderId,orderId));
        }
        orderTemplateService.update(null,new LambdaUpdateWrapper<BrtOrderTemplate>().set(BrtOrderTemplate::getStatus,"5").eq(BrtOrderTemplate::getOrderId,orderId).eq(BrtOrderTemplate::getChildId,childId));
    }


}
