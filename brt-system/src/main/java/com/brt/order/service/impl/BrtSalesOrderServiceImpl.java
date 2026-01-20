package com.brt.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
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
import com.brt.order.vo.BrtSalesOrderDetailsVo;
import com.brt.order.vo.BrtSalesOrderVo;
import com.brt.order.mapper.BrtSalesOrderMapper;
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
@Service("brtSalesOrderService")
public class BrtSalesOrderServiceImpl extends ServiceImpl<BrtSalesOrderMapper, BrtSalesOrder> implements IBrtSalesOrderService {

    @Autowired
    private IBrtSalesOrderDetailsService salesOrderDetailsService;

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
    private IBrtSalesOrderItemService salesOrderItemService;

    @Autowired
    private IBrtSalesOrderPackageService salesOrderPackageService;

    @Autowired
    private IBrtSalesOrderItemProcService salesOrderItemProcService;

    @Autowired
    private BrtOrderNoUtil orderNoUtil;



    @Override
    @BrtDataFiltration(perms = {"order:salesOrder:AllList"},field = "A.user_id")
    public TableDataInfo<BrtSalesOrderVo> queryBrtSalesOrderList(BrtSalesOrderVo brtSalesOrderVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtSalesOrderList(PageUtils.buildPage(), brtSalesOrderVo));
    }

    @Override
    public List<BrtSalesOrderVo> queryBrtSalesOrderAll(BrtSalesOrderVo brtSalesOrderVo) {
        return this.baseMapper.queryBrtSalesOrderList(brtSalesOrderVo);
    }

    @Override
    public BrtSalesOrderVo queryBrtSalesOrderByOrderId(String orderId) {
        BrtSalesOrderVo salesOrderVo = this.baseMapper.queryBrtSalesOrderByOrderId(orderId);
        if (ObjectUtil.isEmpty(salesOrderVo)) {
            return null;
        }
        salesOrderVo.setSalesOrderItemList(salesOrderItemService.list(new LambdaQueryWrapper<BrtSalesOrderItem>()
                .eq(BrtSalesOrderItem::getOrderId, orderId)));
        salesOrderVo.setSalesOrderPackageList(salesOrderPackageService.list(new LambdaQueryWrapper<BrtSalesOrderPackage>()
                .eq(BrtSalesOrderPackage::getOrderId, orderId)));
        salesOrderVo.setSalesOrderItemProcList(salesOrderItemProcService.list(new LambdaQueryWrapper<BrtSalesOrderItemProc>()
                .eq(BrtSalesOrderItemProc::getOrderId, orderId)));
        return salesOrderVo;
    }

    @Transactional
    @Override
    public BrtSalesOrderVo insertBrtSalesOrder(BrtSalesOrderVo brtSalesOrderVo) {

        saveBefore(brtSalesOrderVo);

        // 保存订单详情/明细
        List<BrtSalesOrderDetailsVo> orderDetailsVoList = Optional.ofNullable(brtSalesOrderVo.getSalesOrderDetailsVoList())
                .orElse(Collections.emptyList());
        List<BrtSalesOrderItem> orderItemList = Optional.ofNullable(brtSalesOrderVo.getSalesOrderItemList())
                .orElse(Collections.emptyList());

        if (ObjectUtil.isEmpty(orderDetailsVoList) && ObjectUtil.isEmpty(orderItemList)) {
            throw new ServiceException("请至少添加一个产品信息");
        }

        if (ObjectUtil.isNotEmpty(orderDetailsVoList)) {
            long totalNum = orderDetailsVoList.stream().mapToLong(BrtSalesOrderDetailsVo::getDetailsNum).sum();
            brtSalesOrderVo.setTotalNum(totalNum);
            BigDecimal totalAmount = orderDetailsVoList.stream()
                    .map(BrtSalesOrderDetailsVo::getDetailsAmount)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            brtSalesOrderVo.setTotalAmount(totalAmount);
        } else {
            BigDecimal totalNum = orderItemList.stream()
                    .map(item -> Optional.ofNullable(item.getItemNumber()).orElse(BigDecimal.ZERO))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            brtSalesOrderVo.setTotalNum(totalNum.longValue());
            BigDecimal totalAmount = orderItemList.stream()
                    .map(this::resolveOrderItemAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            brtSalesOrderVo.setTotalAmount(totalAmount);
        }
        brtSalesOrderVo.setUserId(SecurityUtils.getUserId().toString());
        brtSalesOrderVo.setOrderNo(orderNoUtil.getNoAndAdd(OrderNoEnums.销售单));
        int i = this.baseMapper.insert(brtSalesOrderVo);

        if(ObjectUtil.isNotEmpty(orderDetailsVoList)){
            orderDetailsVoList.stream().forEach(item -> {

                if(ObjectUtil.isNotEmpty(item)){
                    item.setOrderId(brtSalesOrderVo.getOrderId());

                    if (ObjectUtil.isEmpty(item.getMaterielId())){
                        item.setMaterielId(item.getMaterielName());
                    }
                    salesOrderDetailsService.save(item);

                    // 保存boom单列表
                    orderBoomService.saveBoomList(item);
                }
            });
        }

        if (ObjectUtil.isNotEmpty(orderItemList)) {
            orderItemList.forEach(item -> item.setOrderId(brtSalesOrderVo.getOrderId()));
            salesOrderItemService.saveBatch(orderItemList);
        }

        List<BrtSalesOrderPackage> packageList = brtSalesOrderVo.getSalesOrderPackageList();
        if (ObjectUtil.isNotEmpty(packageList)) {
            packageList.forEach(item -> item.setOrderId(brtSalesOrderVo.getOrderId()));
            salesOrderPackageService.saveBatch(packageList);
        }

        List<BrtSalesOrderItemProc> procList = brtSalesOrderVo.getSalesOrderItemProcList();
        if (ObjectUtil.isNotEmpty(procList)) {
            procList.forEach(item -> item.setOrderId(brtSalesOrderVo.getOrderId()));
            salesOrderItemProcService.saveBatch(procList);
        }

        // 2. 锁定库存
        if (ObjectUtil.isNotEmpty(orderDetailsVoList)) {
            materielService.lockStock(brtSalesOrderVo);
        }


        BrtOrderVo orderVo = new BrtOrderVo();
        orderVo.setSalesOrderVo(brtSalesOrderVo);
        orderVo.setOrderType(OrderTypeEnums.销售单);
        // 生成订单审批信息
        orderExamineService.createOrderExamine(orderVo,OrderTypeEnums.销售单,null);
        return brtSalesOrderVo;
    }



    @Transactional
    @Override
    public BrtSalesOrderVo updateBrtSalesOrder(BrtSalesOrderVo brtSalesOrderVo) {

        // 查询原订单信息
        BrtSalesOrder salesOrder = this.baseMapper.selectById(brtSalesOrderVo.getOrderId());

        // 保存订单详情
        List<BrtSalesOrderDetailsVo> orderDetailsVoList = Optional.ofNullable(brtSalesOrderVo.getSalesOrderDetailsVoList())
                .orElse(Collections.emptyList());
        List<BrtSalesOrderItem> orderItemList = Optional.ofNullable(brtSalesOrderVo.getSalesOrderItemList())
                .orElse(Collections.emptyList());

        if (ObjectUtil.isEmpty(orderDetailsVoList) && ObjectUtil.isEmpty(orderItemList)) {
            throw new ServiceException("请至少添加一个产品信息");
        }

        if (ObjectUtil.isNotEmpty(orderDetailsVoList)) {
            long totalNum = orderDetailsVoList.stream().mapToLong(BrtSalesOrderDetailsVo::getDetailsNum).sum();
            brtSalesOrderVo.setTotalNum(totalNum);
            BigDecimal totalAmount = orderDetailsVoList.stream()
                    .map(BrtSalesOrderDetailsVo::getDetailsAmount)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            brtSalesOrderVo.setTotalAmount(totalAmount);
        } else {
            BigDecimal totalNum = orderItemList.stream()
                    .map(item -> Optional.ofNullable(item.getItemNumber()).orElse(BigDecimal.ZERO))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            brtSalesOrderVo.setTotalNum(totalNum.longValue());
            BigDecimal totalAmount = orderItemList.stream()
                    .map(this::resolveOrderItemAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            brtSalesOrderVo.setTotalAmount(totalAmount);
        }
        brtSalesOrderVo.setUserId(SecurityUtils.getUserId().toString());

        if (ObjectUtil.isNotEmpty(orderDetailsVoList)) {
            orderDetailsVoList.stream().forEach(item -> {
                if(ObjectUtil.isNotEmpty(item)){
                    item.setOrderId(brtSalesOrderVo.getOrderId());
                    salesOrderDetailsService.saveOrUpdate(item);
                }
            });
        }

        if (brtSalesOrderVo.getSalesOrderItemList() != null) {
            salesOrderItemService.remove(new LambdaQueryWrapper<BrtSalesOrderItem>()
                    .eq(BrtSalesOrderItem::getOrderId, brtSalesOrderVo.getOrderId()));
            if (ObjectUtil.isNotEmpty(orderItemList)) {
                orderItemList.forEach(item -> item.setOrderId(brtSalesOrderVo.getOrderId()));
                salesOrderItemService.saveBatch(orderItemList);
            }
        }

        if (brtSalesOrderVo.getSalesOrderPackageList() != null) {
            salesOrderPackageService.remove(new LambdaQueryWrapper<BrtSalesOrderPackage>()
                    .eq(BrtSalesOrderPackage::getOrderId, brtSalesOrderVo.getOrderId()));
            List<BrtSalesOrderPackage> packageList = brtSalesOrderVo.getSalesOrderPackageList();
            if (ObjectUtil.isNotEmpty(packageList)) {
                packageList.forEach(item -> item.setOrderId(brtSalesOrderVo.getOrderId()));
                salesOrderPackageService.saveBatch(packageList);
            }
        }

        if (brtSalesOrderVo.getSalesOrderItemProcList() != null) {
            salesOrderItemProcService.remove(new LambdaQueryWrapper<BrtSalesOrderItemProc>()
                    .eq(BrtSalesOrderItemProc::getOrderId, brtSalesOrderVo.getOrderId()));
            List<BrtSalesOrderItemProc> procList = brtSalesOrderVo.getSalesOrderItemProcList();
            if (ObjectUtil.isNotEmpty(procList)) {
                procList.forEach(item -> item.setOrderId(brtSalesOrderVo.getOrderId()));
                salesOrderItemProcService.saveBatch(procList);
            }
        }

        if (!salesOrder.getTemplateId().equals(brtSalesOrderVo.getTemplateId())){
            // 删除订单模板
            orderTemplateService.remove(new LambdaQueryWrapper<BrtOrderTemplate>().eq(BrtOrderTemplate::getOrderId,salesOrder.getOrderId()));
            orderNodeService.remove(new LambdaQueryWrapper<BrtOrderNode>().eq(BrtOrderNode::getOrderId,salesOrder.getOrderId()));
            orderExamineService.remove(new LambdaQueryWrapper<BrtOrderExamine>().eq(BrtOrderExamine::getOrderId,salesOrder.getOrderId()));

            BrtOrderVo orderVo = new BrtOrderVo();
            orderVo.setSalesOrderVo(brtSalesOrderVo);
            orderVo.setOrderType(OrderTypeEnums.销售单);
            // 生成订单审批信息
            orderExamineService.createOrderExamine(orderVo, OrderTypeEnums.销售单, null);
        }

        int i = this.baseMapper.updateById(brtSalesOrderVo);

        return brtSalesOrderVo;
    }

    @Transactional
    @Override
    public int deleteBrtSalesOrderByOrderIds(String[] orderIds) {
        // 删除订单模板
        orderTemplateService.remove(new LambdaQueryWrapper<BrtOrderTemplate>().in(BrtOrderTemplate::getOrderId,orderIds));
        orderNodeService.remove(new LambdaQueryWrapper<BrtOrderNode>().in(BrtOrderNode::getOrderId,orderIds));
        orderExamineService.remove(new LambdaQueryWrapper<BrtOrderExamine>().in(BrtOrderExamine::getOrderId,orderIds));

        // 删除boom单
        int boomRow = orderBoomService.removeByOrderIds(orderIds);

        // 删除订单详情
        int orderDetailsRow = salesOrderDetailsService.removeByOrderIds(orderIds);

        // 删除订单明细/包裹/工艺
        salesOrderItemService.remove(new LambdaQueryWrapper<BrtSalesOrderItem>().in(BrtSalesOrderItem::getOrderId, orderIds));
        salesOrderPackageService.remove(new LambdaQueryWrapper<BrtSalesOrderPackage>().in(BrtSalesOrderPackage::getOrderId, orderIds));
        salesOrderItemProcService.remove(new LambdaQueryWrapper<BrtSalesOrderItemProc>().in(BrtSalesOrderItemProc::getOrderId, orderIds));

        // 删除开票计划
        orderInvoicePlanService.remove(new LambdaQueryWrapper<BrtOrderInvoicePlan>().in(BrtOrderInvoicePlan::getOrderId,orderIds));

        // 删除收款计划
        collectionPlanService.remove(new LambdaQueryWrapper<BrtOrderCollectionPlan>().in(BrtOrderCollectionPlan::getOrderId,orderIds));

        // 删除数量计划
        materielPlanService.remove(new LambdaQueryWrapper<BrtOrderMaterielPlan>().in(BrtOrderMaterielPlan::getOrderId,orderIds));
        return this.baseMapper.deleteBatchIds(Arrays.asList(orderIds));
    }

    private BigDecimal resolveOrderItemAmount(BrtSalesOrderItem item) {
        if (ObjectUtil.isNotEmpty(item.getActualAmount())) {
            return item.getActualAmount();
        }
        BigDecimal itemPrice = Optional.ofNullable(item.getItemPrice()).orElse(BigDecimal.ZERO);
        BigDecimal itemNumber = Optional.ofNullable(item.getItemNumber()).orElse(BigDecimal.ZERO);
        return itemPrice.multiply(itemNumber);
    }

    @Override
    public int orderCheck(BrtCheckVo checkVo) {
        // 查询订单信息
        BrtSalesOrder salesOrder = this.baseMapper.selectById(checkVo.getOrderId());
        if (ObjectUtil.isNotEmpty(salesOrder)){
            if (checkVo.getCheckType().equals(CheckTypeEnums.收入.getCode())){
                salesOrder.setCollectionAmount(salesOrder.getCollectionAmount().add(checkVo.getCheckAmount()));
            }else {
                salesOrder.setCollectionAmount(salesOrder.getCollectionAmount().subtract(checkVo.getCheckAmount()));
            }

            // 如果收款金额小于等于0 则表示未收款
            // 如果收款金额小于合同金额 则表示部分收款
            // 如果收款金额大于等于合同金额 则表示全部收款
            if (salesOrder.getCollectionAmount().compareTo(BigDecimal.ZERO) <= 0){
                salesOrder.setCollectionStatus(CollectionStatusEnums.未收款.getCode());

            }else if (salesOrder.getCollectionAmount().compareTo(salesOrder.getTotalAmount()) < 0){
                salesOrder.setCollectionStatus(CollectionStatusEnums.部分收款.getCode());

            }else if (salesOrder.getCollectionAmount().compareTo(salesOrder.getTotalAmount()) >= 0){
                salesOrder.setCollectionStatus(CollectionStatusEnums.已收款.getCode());
            }
            this.baseMapper.updateById(salesOrder);
        }
        return 0;
    }

    @Override
    public Map<String, Object> totalSalesOrder(String customerId) {
        Map<String, Object> result = new HashMap<>();
        result.put("totalAmount",0);
        result.put("collectionAmount",0);
        result.put("notCollectionAmount",0);
        result.put("deliveryNum",0);
        // 查询客户销售单信息
        List<BrtSalesOrder> salesOrderList = this.baseMapper.selectList(new LambdaQueryWrapper<BrtSalesOrder>().eq(BrtSalesOrder::getCustomerId, customerId));
        if (ObjectUtil.isNotEmpty(salesOrderList)){
            //  统计总金额
            BigDecimal totalAmount = salesOrderList.stream().map(BrtSalesOrder::getTotalAmount).reduce(BigDecimal::add).get();
            result.put("totalAmount",totalAmount);

            //  统计已收款金额
            BigDecimal collectionAmount = salesOrderList.stream().map(BrtSalesOrder::getCollectionAmount).reduce(BigDecimal::add).get();
            result.put("collectionAmount",collectionAmount);

            // 未收款
            result.put("notCollectionAmount",totalAmount.subtract(collectionAmount));

            String s = this.baseMapper.selectDeliveryNum(customerId);
            if(StringUtils.isNotEmpty(s)){
                result.put("deliveryNum",s);
            }


        }
        return result;
    }

    @Override
    public int copySalesOrder(String orderId) {
        // 查询订单信息
        BrtSalesOrder salesOrder = this.baseMapper.selectById(orderId);
        BrtSalesOrderVo salesOrderVo = BeanUtil.copyProperties(salesOrder, BrtSalesOrderVo.class);
        //初始化订单信息
        salesOrderVo.setOrderId(null);
        salesOrderVo.setOrderNo(orderNoUtil.getNoAndAdd(OrderNoEnums.销售单));
        salesOrderVo.setCollectionStatus(CollectionStatusEnums.未收款.getCode());
        salesOrderVo.setCollectionAmount(BigDecimal.ZERO);
        salesOrderVo.setCreateTime(null);

        // 查询订单详情
        List<BrtSalesOrderDetails> orderDetailsList = salesOrderDetailsService.list(new LambdaQueryWrapper<BrtSalesOrderDetails>().eq(BrtSalesOrderDetails::getOrderId, orderId));
        List<BrtSalesOrderDetailsVo> salesOrderDetailsVoList = BeanUtil.copyToList(orderDetailsList, BrtSalesOrderDetailsVo.class);
        salesOrderDetailsVoList.forEach(item -> {
            item.setOrderId(null);
            item.setDetailsId(null);
        });
        salesOrderVo.setSalesOrderDetailsVoList(salesOrderDetailsVoList);

        insertBrtSalesOrder(salesOrderVo);
        return 0;
    }

    /**
     * 修改状态
     * @param brtSalesOrderVo
     */
    public void updateStatus(BrtSalesOrderVo brtSalesOrderVo){
        baseMapper.updateStatus(brtSalesOrderVo);
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param brtSalesOrderVo
     * @return:
     **/
    public void saveBefore(BrtSalesOrderVo brtSalesOrderVo){

    }

    /**
     * 归档
     */
    @Transactional
    public void beNotInUse(String orderId,String childId){
        if(!StringUtils.isNotEmpty(childId)){
            BrtSalesOrder salesOrder = this.getById(orderId);
            salesOrder.setStatus(OrderAuditStatus.归档.getCode());
            this.updateById(salesOrder);
            orderTemplateService.update(null,new LambdaUpdateWrapper<BrtOrderTemplate>().set(BrtOrderTemplate::getStatus,"5").eq(BrtOrderTemplate::getOrderId,orderId));
        }
        orderTemplateService.update(null,new LambdaUpdateWrapper<BrtOrderTemplate>().set(BrtOrderTemplate::getStatus,"5").eq(BrtOrderTemplate::getOrderId,orderId).eq(BrtOrderTemplate::getChildId,childId));
    }


    public BrtSalesOrderVo getOrderByOrderTemplateId(String orderTemplateId){
        return baseMapper.getOrderByOrderTemplateId(orderTemplateId);
    }


}
