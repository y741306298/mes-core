package com.brt.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.annotation.BrtDataFiltration;
import com.brt.common.core.domain.BaseEntity;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.enums.*;
import com.brt.common.exception.ServiceException;
import com.brt.common.utils.PageUtils;
import com.brt.common.utils.SecurityUtils;
import com.brt.common.utils.StringUtils;
import com.brt.order.domain.*;
import com.brt.order.mapper.BrtPriceSheetOrderDetailsRecordMapper;
import com.brt.order.mapper.BrtPriceSheetOrderRecordMapper;
import com.brt.order.service.*;
import com.brt.order.utils.BrtBeanUtils;
import com.brt.order.utils.BrtOrderNoUtil;
import com.brt.order.vo.*;
import com.brt.order.mapper.BrtPriceSheetOrderMapper;
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
@Service("brtPriceSheetOrderService")
public class BrtPriceSheetOrderServiceImpl extends ServiceImpl<BrtPriceSheetOrderMapper, BrtPriceSheetOrder> implements IBrtPriceSheetOrderService {

    @Autowired
    private IBrtPriceSheetOrderDetailsService priceSheetOrderDetailsService;

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
    private BrtPriceSheetOrderRecordMapper priceSheetRecordMapper;

    @Autowired
    private BrtPriceSheetOrderDetailsRecordMapper priceSheetOrderDetailsRecordMapper;

    @Autowired
    private BrtBeanUtils brtBeanUtils;

    @Autowired
    private BrtOrderNoUtil orderNoUtil;

    @Override
    @BrtDataFiltration(perms = {"order:priceSheetOrder:AllList"},field = "A.user_id")
    public TableDataInfo<BrtPriceSheetOrderVo> queryBrtPriceSheetOrderList(BrtPriceSheetOrderVo brtPriceSheetOrderVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtPriceSheetOrderList(PageUtils.buildPage(), brtPriceSheetOrderVo));
    }

    @Override
    public List<BrtPriceSheetOrderVo> queryBrtPriceSheetOrderAll(BrtPriceSheetOrderVo brtPriceSheetOrderVo) {
        return this.baseMapper.queryBrtPriceSheetOrderList(brtPriceSheetOrderVo);
    }

    @Override
    public BrtPriceSheetOrderVo queryBrtPriceSheetOrderByOrderId(String orderId) {
        return this.baseMapper.queryBrtPriceSheetOrderByOrderId(orderId);
    }

    @Transactional
    @Override
    public BrtPriceSheetOrderVo insertBrtPriceSheetOrder(BrtPriceSheetOrderVo brtPriceSheetOrderVo) {
        saveBefore(brtPriceSheetOrderVo);

        // 保存订单详情
        List<BrtPriceSheetOrderDetailsVo> orderDetailsVoList = brtPriceSheetOrderVo.getPriceSheetOrderDetailsVoList();
        if (ObjectUtil.isEmpty(orderDetailsVoList)){
            throw new ServiceException("请至少添加一个产品信息");
        }

        //统计订单数量
        long toatlNum = orderDetailsVoList.stream().mapToLong(BrtPriceSheetOrderDetailsVo::getDetailsNum).sum();
        brtPriceSheetOrderVo.setTotalNum(toatlNum);

        //统计订单总金额
        BigDecimal totalAmount = orderDetailsVoList.stream().map(BrtPriceSheetOrderDetailsVo::getDetailsAmount).reduce(BigDecimal::add).get();
        brtPriceSheetOrderVo.setTotalAmount(totalAmount);
        brtPriceSheetOrderVo.setUserId(SecurityUtils.getUserId().toString());

        brtPriceSheetOrderVo.setOrderNo(orderNoUtil.getNoAndAdd(OrderNoEnums.报价单));

        int i = this.baseMapper.insert(brtPriceSheetOrderVo);

        orderDetailsVoList.stream().forEach(item -> {
            item.setOrderId(brtPriceSheetOrderVo.getOrderId());

            if (ObjectUtil.isEmpty(item.getMaterielId())){
                item.setMaterielId(item.getMaterielName());
            }
            priceSheetOrderDetailsService.save(item);

        });

        BrtOrderVo orderVo = new BrtOrderVo();
        orderVo.setPriceSheetOrderVo(brtPriceSheetOrderVo);
        orderVo.setOrderType(OrderTypeEnums.报价单);
        // 生成订单审批信息
        orderExamineService.createOrderExamine(orderVo,OrderTypeEnums.报价单,null);
        /**
         * 生成报价记录
         */
        this.addPriceSheetRecord(brtPriceSheetOrderVo.getOrderId(),orderDetailsVoList);

        return brtPriceSheetOrderVo;
    }

    @Transactional
    @Override
    public BrtPriceSheetOrderVo updateBrtPriceSheetOrder(BrtPriceSheetOrderVo brtPriceSheetOrderVo) {

        // 查询原订单信息
        BrtPriceSheetOrder priceSheetOrder = this.baseMapper.selectById(brtPriceSheetOrderVo.getOrderId());

        // 保存订单详情
        List<BrtPriceSheetOrderDetailsVo> orderDetailsVoList = brtPriceSheetOrderVo.getPriceSheetOrderDetailsVoList();

        //统计订单数量
        long toatlNum = orderDetailsVoList.stream().mapToLong(BrtPriceSheetOrderDetailsVo::getDetailsNum).sum();
        brtPriceSheetOrderVo.setTotalNum(toatlNum);

        //统计订单总金额
        BigDecimal totalAmount = orderDetailsVoList.stream().map(BrtPriceSheetOrderDetailsVo::getDetailsAmount).reduce(BigDecimal::add).get();
        brtPriceSheetOrderVo.setTotalAmount(totalAmount);
        brtPriceSheetOrderVo.setUserId(SecurityUtils.getUserId().toString());

        orderDetailsVoList.stream().forEach(item -> {
            item.setOrderId(brtPriceSheetOrderVo.getOrderId());
            priceSheetOrderDetailsService.saveOrUpdate(item);
        });

        if (!priceSheetOrder.getTemplateId().equals(brtPriceSheetOrderVo.getTemplateId())){
            // 删除订单模板
            orderTemplateService.remove(new LambdaQueryWrapper<BrtOrderTemplate>().eq(BrtOrderTemplate::getOrderId,priceSheetOrder.getOrderId()));
            orderNodeService.remove(new LambdaQueryWrapper<BrtOrderNode>().eq(BrtOrderNode::getOrderId,priceSheetOrder.getOrderId()));
            orderExamineService.remove(new LambdaQueryWrapper<BrtOrderExamine>().eq(BrtOrderExamine::getOrderId,priceSheetOrder.getOrderId()));

            BrtOrderVo orderVo = new BrtOrderVo();
            orderVo.setPriceSheetOrderVo(brtPriceSheetOrderVo);
            orderVo.setOrderType(OrderTypeEnums.报价单);
            // 生成订单审批信息
            orderExamineService.createOrderExamine(orderVo, OrderTypeEnums.报价单, null);
        }
//        brtPriceSheetOrderVo.setOrderNo(orderNoUtil.getNoAndAdd(OrderNoEnums.报价单));
        int i = this.baseMapper.updateById(brtPriceSheetOrderVo);

        /**
         * 生成报价记录
         */
        this.addPriceSheetRecord(brtPriceSheetOrderVo.getOrderId(),orderDetailsVoList);
        return brtPriceSheetOrderVo;
    }

    @Transactional
    @Override
    public int deleteBrtPriceSheetOrderByOrderIds(String[] orderIds) {
        // 删除订单模板
        orderTemplateService.remove(new LambdaQueryWrapper<BrtOrderTemplate>().in(BrtOrderTemplate::getOrderId,orderIds));
        orderNodeService.remove(new LambdaQueryWrapper<BrtOrderNode>().in(BrtOrderNode::getOrderId,orderIds));
        orderExamineService.remove(new LambdaQueryWrapper<BrtOrderExamine>().in(BrtOrderExamine::getOrderId,orderIds));

        // 删除boom单
        int boomRow = orderBoomService.removeByOrderIds(orderIds);

        // 删除订单详情
        int orderDetailsRow = priceSheetOrderDetailsService.removeByOrderIds(orderIds);

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
        BrtPriceSheetOrder priceSheetOrder = this.baseMapper.selectById(checkVo.getOrderId());
        if (ObjectUtil.isNotEmpty(priceSheetOrder)){
            if (checkVo.getCheckType().equals(CheckTypeEnums.收入.getCode())){
                priceSheetOrder.setCollectionAmount(priceSheetOrder.getCollectionAmount().add(checkVo.getCheckAmount()));
            }else {
                priceSheetOrder.setCollectionAmount(priceSheetOrder.getCollectionAmount().subtract(checkVo.getCheckAmount()));
            }

            // 如果收款金额小于等于0 则表示未收款
            // 如果收款金额小于合同金额 则表示部分收款
            // 如果收款金额大于等于合同金额 则表示全部收款
            if (priceSheetOrder.getCollectionAmount().compareTo(BigDecimal.ZERO) <= 0){
                priceSheetOrder.setCollectionStatus(CollectionStatusEnums.未收款.getCode());

            }else if (priceSheetOrder.getCollectionAmount().compareTo(priceSheetOrder.getTotalAmount()) < 0){
                priceSheetOrder.setCollectionStatus(CollectionStatusEnums.部分收款.getCode());

            }else if (priceSheetOrder.getCollectionAmount().compareTo(priceSheetOrder.getTotalAmount()) >= 0){
                priceSheetOrder.setCollectionStatus(CollectionStatusEnums.已收款.getCode());
            }
            this.baseMapper.updateById(priceSheetOrder);
        }
        return 0;
    }

    @Override
    public Map<String, Object> totalPriceSheetOrder(String customerId) {
        Map<String, Object> result = new HashMap<>();
        result.put("totalAmount",0);
        result.put("collectionAmount",0);
        result.put("notCollectionAmount",0);
        // 查询客户销售单信息
        List<BrtPriceSheetOrder> priceSheetOrderList = this.baseMapper.selectList(new LambdaQueryWrapper<BrtPriceSheetOrder>().eq(BrtPriceSheetOrder::getCustomerId, customerId));
        if (ObjectUtil.isNotEmpty(priceSheetOrderList)){
            //  统计总金额
            BigDecimal totalAmount = priceSheetOrderList.stream().map(BrtPriceSheetOrder::getTotalAmount).reduce(BigDecimal::add).get();
            result.put("totalAmount",totalAmount);

            //  统计已收款金额
            BigDecimal collectionAmount = priceSheetOrderList.stream().map(BrtPriceSheetOrder::getCollectionAmount).reduce(BigDecimal::add).get();
            result.put("collectionAmount",collectionAmount);

            // 未收款
            result.put("notCollectionAmount",totalAmount.subtract(collectionAmount));
        }
        return result;
    }

    @Override
    public int copyPriceSheetOrder(String orderId) {
        // 查询订单信息
        BrtPriceSheetOrder priceSheetOrder = this.baseMapper.selectById(orderId);
        BrtPriceSheetOrderVo priceSheetOrderVo = BeanUtil.copyProperties(priceSheetOrder, BrtPriceSheetOrderVo.class);
        //初始化订单信息
        priceSheetOrderVo.setOrderId(null);
        priceSheetOrderVo.setOrderNo(orderNoUtil.getNoAndAdd(OrderNoEnums.报价单));
        priceSheetOrderVo.setCollectionStatus(CollectionStatusEnums.未收款.getCode());
        priceSheetOrderVo.setCollectionAmount(BigDecimal.ZERO);
        priceSheetOrderVo.setCreateTime(null);

        // 查询订单详情
        List<BrtPriceSheetOrderDetails> orderDetailsList = priceSheetOrderDetailsService.list(new LambdaQueryWrapper<BrtPriceSheetOrderDetails>().eq(BrtPriceSheetOrderDetails::getOrderId, orderId));
        List<BrtPriceSheetOrderDetailsVo> priceSheetOrderDetailsVoList = BeanUtil.copyToList(orderDetailsList, BrtPriceSheetOrderDetailsVo.class);
        priceSheetOrderDetailsVoList.forEach(item -> {
            item.setOrderId(null);
            item.setDetailsId(null);
        });
        priceSheetOrderVo.setPriceSheetOrderDetailsVoList(priceSheetOrderDetailsVoList);

        insertBrtPriceSheetOrder(priceSheetOrderVo);
        return 0;
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param brtPriceSheetOrderVo
     * @return:
     **/
    public void saveBefore(BrtPriceSheetOrderVo brtPriceSheetOrderVo){

    }


    /**
     * 新增和修改的时候 添加报价记录
     */
    public void addPriceSheetRecord(String  orderId,List<BrtPriceSheetOrderDetailsVo> priceSheetOrderDetailsVos){
        BrtPriceSheetOrder priceSheetOrder = this.getById(orderId);
        List<String> shield = new LinkedList<>();
        shield.add("recordId");
        shield.add("detailsRecordId");
        BrtPriceSheetOrderRecord priceSheetOrderRecord = (BrtPriceSheetOrderRecord)brtBeanUtils.coverBean(new BrtPriceSheetOrderRecord(), priceSheetOrder, shield);
        priceSheetRecordMapper.insert(priceSheetOrderRecord);

        for(BrtPriceSheetOrderDetailsVo priceSheetOrderDetailsVo:priceSheetOrderDetailsVos){
            BrtPriceSheetOrderDetails priceSheetOrderDetails = priceSheetOrderDetailsService.getById(priceSheetOrderDetailsVo.getDetailsId());
            BrtPriceSheetOrderDetailsRecord brtPriceSheetOrderDetailsRecord = (BrtPriceSheetOrderDetailsRecord)brtBeanUtils.coverBean(new BrtPriceSheetOrderDetailsRecord(),priceSheetOrderDetails,shield);
            brtPriceSheetOrderDetailsRecord.setOrderId(priceSheetOrderRecord.getRecordId());
            priceSheetOrderDetailsRecordMapper.insert(brtPriceSheetOrderDetailsRecord);
        }

    }

    /**
     * 报价单 - 去下单  将报价单数据转为销售单数据
     * @param priceSheetOrderVo
     * @return
     */
    public Map<String,Object> priceSheetToSales(BrtPriceSheetOrderVo priceSheetOrderVo){
        BrtPriceSheetOrder priceSheetOrder = this.getById(priceSheetOrderVo.getOrderId());
        List<String> shield = new LinkedList<>();
        shield.add("orderId");
        shield.add("detailsId");
        shield.add("orderNo");
        BrtSalesOrder salesOrder = (BrtSalesOrder)brtBeanUtils.coverBean(new BrtSalesOrder(),priceSheetOrder,shield);
        List<BrtPriceSheetOrderDetails> brtPriceSheetOrderDetails = priceSheetOrderDetailsService.listAllByOrderId(priceSheetOrderVo.getOrderId());

        List<BrtSalesOrderDetailsVo> salesOrderDetailsList = new LinkedList<>();
        for(BrtPriceSheetOrderDetails priceSheetOrderDetails:brtPriceSheetOrderDetails){
//            BrtSalesOrderDetailsVo salesOrderDetails = (BrtSalesOrderDetailsVo)brtBeanUtils.coverBean(new BrtSalesOrderDetailsVo(),priceSheetOrderDetails,shield);
            BrtSalesOrderDetailsVo salesOrderDetailsVo = new BrtSalesOrderDetailsVo();
            BeanUtil.copyProperties(priceSheetOrderDetails,salesOrderDetailsVo);
            String materielId = priceSheetOrderDetails.getMaterielId();
            BrtMaterielVo materielVo = materielService.queryBrtMaterielByMaterielId(materielId);
            if(ObjectUtil.isNotEmpty(materielVo)){
                salesOrderDetailsVo.setMaterielVo(materielVo);
            }
            salesOrderDetailsList.add(salesOrderDetailsVo);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("salesOrder",salesOrder);
        map.put("salesOrderDetailsList",salesOrderDetailsList);
        return map;
    }

    public void updatePriceSheet(String orderId,String isPriceSheet){
        Map<String,Object> map = new HashMap<>();
        map.put("orderId",orderId);
        map.put("isPriceSheet",isPriceSheet);
        this.baseMapper.updatePriceSheet(map);
    }

    /**
     * 销售单导入报价单 查询报价单列表
     * @param orderNo
     * @return
     */
    public List<Map<String,Object>> exportPriceList(@Param("orderNo")String orderNo){
        return this.baseMapper.exportPriceList(orderNo);
    }

    /**
     * 修改状态
     * @param brtPriceSheetOrderVo
     */
    public void updateStatus(BrtPriceSheetOrderVo brtPriceSheetOrderVo){
        baseMapper.updateStatus(brtPriceSheetOrderVo);
    }

    /**
     * 根据流程ID查询订单
     * @param orderTemplateId
     * @return
     */
    public BrtPriceSheetOrderVo getOrderByOrderTemplateId(String orderTemplateId){
        return baseMapper.getOrderByOrderTemplateId(orderTemplateId);
    }

}
