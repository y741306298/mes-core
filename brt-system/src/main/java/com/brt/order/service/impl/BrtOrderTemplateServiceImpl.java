package com.brt.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.annotation.RequestSingleParam;
import com.brt.common.core.domain.AjaxResult;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.enums.*;
import com.brt.common.utils.PageUtils;
import com.brt.common.utils.SecurityUtils;
import com.brt.common.utils.StringUtils;
import com.brt.order.domain.*;
import com.brt.order.service.*;
import com.brt.order.vo.*;
import com.brt.order.mapper.BrtOrderTemplateMapper;
import com.brt.order.vo.pub.BrtOrderVo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

/**
 * 订单模板Service业务层处理
 *
 * @author Fgn
 * @date 2024-05-10
 */
@Service
public class BrtOrderTemplateServiceImpl extends ServiceImpl<BrtOrderTemplateMapper, BrtOrderTemplate> implements IBrtOrderTemplateService {

    @Autowired
    private IBrtOrderNodeService orderNodeService;

    @Autowired
    private IBrtFlowNodeService flowNodeService;

    @Autowired
    private IBrtFlowTemplateService flowTemplateService;

    @Autowired
    private IBrtSalesOrderService salesOrderService;

    @Autowired
    private IBrtMarketOrderService marketOrderService;

    @Autowired
    private IBrtPriceSheetOrderService priceSheetOrderService;

    @Autowired
    private IBrtOrderExamineService orderExamineService;

    @Autowired
    private IBrtMarketOrderDetailsService marketOrderDetailsService;

    @Autowired
    private IBrtPriceSheetOrderDetailsService priceSheetOrderDetailsService;

    @Autowired
    private IBrtSalesOrderDetailsService salesOrderDetailsService;

    @Override
    public TableDataInfo<BrtOrderTemplateVo> queryBrtOrderTemplateList(BrtOrderTemplateVo brtOrderTemplateVo) {

        if(!SecurityUtils.isAdmin(SecurityUtils.getUserId())){
            brtOrderTemplateVo.setThisUserId(SecurityUtils.getUserId());
        }

        return PageUtils.buildDataInfo(this.baseMapper.queryBrtOrderTemplateList(PageUtils.buildPage(), brtOrderTemplateVo));
    }

    @Override
    public List<BrtOrderTemplateVo> queryBrtOrderTemplateAll(BrtOrderTemplateVo brtOrderTemplateVo) {
        Long thisUser = SecurityUtils.getUserId();
        if(thisUser.intValue()!=1){
            brtOrderTemplateVo.setThisUserId(thisUser);
        }
        return this.baseMapper.queryBrtOrderTemplateList(brtOrderTemplateVo);
    }

    @Override
    public BrtOrderTemplateVo queryBrtOrderTemplateByOrderTemplateId(String orderTemplateId) {
        return this.baseMapper.queryBrtOrderTemplateByOrderTemplateId(orderTemplateId);
    }

    @Override
    public BrtOrderTemplateVo sendWorkSelect(String orderTemplateId) {
        BrtOrderTemplateVo brtOrderTemplateVo = this.baseMapper.queryBrtOrderTemplateByOrderTemplateId(orderTemplateId);
        BrtFlowTemplate flowTemplate = flowTemplateService.getById(brtOrderTemplateVo.getTemplateId());
        String userId = flowTemplate.getUserId();
        Long thisLoginUserId = SecurityUtils.getUserId();
        //判断当前登录用户是否是Admin 如果不是则只能对执行人是自己的节点派工
        if(!SecurityUtils.isAdmin(thisLoginUserId)&&!userId.equals(String.valueOf(thisLoginUserId))){
            List<BrtOrderNodeVo> newOrderNodeVoList = new LinkedList<>();
            List<BrtOrderNodeVo> orderNodeVoList = brtOrderTemplateVo.getOrderNodeVoList();
            for(BrtOrderNodeVo brtOrderNodeVo:orderNodeVoList){
                if(StringUtils.isNotEmpty(brtOrderNodeVo.getUserId())&&brtOrderNodeVo.getUserId().equals(String.valueOf(thisLoginUserId))){
                    newOrderNodeVoList.add(brtOrderNodeVo);
                }
            }
            brtOrderTemplateVo.setOrderNodeVoList(newOrderNodeVoList);
        }
        return brtOrderTemplateVo;
    }

    @Transactional
    @Override
    public BrtOrderTemplateVo insertBrtOrderTemplate(BrtOrderTemplateVo brtOrderTemplateVo) {
        saveBefore(brtOrderTemplateVo);
        int i = this.baseMapper.insert(brtOrderTemplateVo);
        return brtOrderTemplateVo;
    }

    @Transactional
    @Override
    public BrtOrderTemplateVo updateBrtOrderTemplate(BrtOrderTemplateVo brtOrderTemplateVo) {
        saveBefore(brtOrderTemplateVo);
        int i = this.baseMapper.updateById(brtOrderTemplateVo);
        return brtOrderTemplateVo;
    }

    @Transactional
    @Override
    public int deleteBrtOrderTemplateByOrderTemplateIds(String[] orderTemplateIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(orderTemplateIds));
    }

    @Override
    public int sendWork(BrtOrderTemplateVo brtOrderTemplateVo) {
        // 获取派工列表
        List<BrtOrderNodeVo> orderNodeVoList = brtOrderTemplateVo.getOrderNodeVoList();


        //查询模板信息
//        BrtFlowTemplate flowTemplate = flowTemplateService.getById(brtOrderTemplateVo.getTemplateId());

//        if (flowTemplate.getIsSeqExecute()!=null && !flowTemplate.getIsSeqExecute()){
//            orderNodeVoList.forEach(item -> {
//                if((!"2".equals(item.getNodeStatus()))&&(!"3".equals(item.getNodeStatus()))){
//                    item.setNodeStatus(NodeStatusEnums.进行中.getCode());
//                }
//            });
//        }else {
//            // 如果流程按顺序执行 同时不需要审核 需要将第一个节点设置为进行中
//            if (flowTemplate.getIsAudit()!=null && flowTemplate.getIsAudit().equals(YesOrNoEnums.NO.getCode())){
//                for(BrtOrderNodeVo orderNodeVo:orderNodeVoList){
//                    if((!"2".equals(orderNodeVo.getNodeStatus()))&&(!"3".equals(orderNodeVo.getNodeStatus()))){
//                        orderNodeVo.setNodeStatus(NodeStatusEnums.进行中.getCode());
//                        break;
//                    }
//                }
//
//            }
//        }

        return orderNodeService.saveOrUpdateBatch(BeanUtil.copyToList(orderNodeVoList, BrtOrderNode.class)) ? 1 : 0;
    }

    @Override
    @Transactional
    public int copy(String orderTemplateId) {

        // 查询订单模板
        BrtOrderTemplate orderTemplate = this.baseMapper.selectById(orderTemplateId);
        BrtOrderVo orderVo = new BrtOrderVo();
        String orderType = orderTemplate.getOrderType();
        if(orderType.equals(OrderTypeEnums.采购单.getCode())){
            BrtMarketOrderVo marketOrderVo = marketOrderService.getOrderByTemplateId(orderTemplateId);
            List<BrtMarketOrderDetailsVo> marketOrderDetailsVoList = marketOrderDetailsService.getBrtMarketOrderDetailsVoListByOrderId(marketOrderVo.getOrderId());
            marketOrderVo.setMarketOrderDetailsVoList(marketOrderDetailsVoList);
            orderVo.setMarketOrderVo(marketOrderVo);
            orderVo.setOrderType(OrderTypeEnums.采购单);
        }else if(orderType.equals(OrderTypeEnums.销售单.getCode())){
            BrtSalesOrderVo salesOrderVo = salesOrderService.getOrderByOrderTemplateId(orderTemplateId);
            List<BrtSalesOrderDetailsVo> brtSalesOrderDetailsVoList = salesOrderDetailsService.getBrtSalesOrderDetailsVoListByOrderId(salesOrderVo.getOrderId());
            salesOrderVo.setSalesOrderDetailsVoList(brtSalesOrderDetailsVoList);
            orderVo.setSalesOrderVo(salesOrderVo);
            orderVo.setOrderType(OrderTypeEnums.销售单);
        }else if(orderType.equals(OrderTypeEnums.报价单.getCode())){
            BrtPriceSheetOrderVo priceSheetOrderVo = priceSheetOrderService.getOrderByOrderTemplateId(orderTemplateId);
            List<BrtPriceSheetOrderDetailsVo> brtPriceSheetOrderDetailsVos = priceSheetOrderDetailsService.queryByOrderId(priceSheetOrderVo.getOrderId());
            priceSheetOrderVo.setPriceSheetOrderDetailsVoList(brtPriceSheetOrderDetailsVos);
            orderVo.setPriceSheetOrderVo(priceSheetOrderVo);
            orderVo.setOrderType(OrderTypeEnums.报价单);
        }
        orderExamineService.createOrderExamine(orderVo,orderVo.getOrderType(),orderTemplate.getChildId());

        orderTemplate.setOrderTemplateStatus(OrderTemplateStatusEnums.作废.getCode());
        this.baseMapper.updateById(orderTemplate);
        return 1;
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param brtOrderTemplateVo
     * @return:
     **/
    public void saveBefore(BrtOrderTemplateVo brtOrderTemplateVo){

    }

    public String getOrderType(String orderNodeId){
        return baseMapper.getOrderType(orderNodeId);
    }

    /**
     * 查询订单ID
     * @param orderType
     * @param orderTemplateId
     * @return
     */
    public String getOrderId(String orderType,String orderTemplateId){
        return baseMapper.getOrderId(orderType,orderTemplateId);
    }

    public void beNotInUseSubmit(String orderType,String orderId,String childId) {
        if(orderType.equals(OrderTypeEnums.销售单.getCode())){
            salesOrderService.beNotInUse(orderId,childId);
        }else if(orderType.equals(OrderTypeEnums.采购单.getCode())){
            marketOrderService.beNotInUse(orderId,childId);
        }
    }

    public AjaxResult getOrderForm(String orderType,String orderId){
        if(orderType.equals(OrderTypeEnums.采购单.getCode())){
            return AjaxResult.success(marketOrderService.queryBrtMarketOrderByOrderId(orderId));
        }else if(orderType.equals(OrderTypeEnums.销售单.getCode())){
            return AjaxResult.success(salesOrderService.queryBrtSalesOrderByOrderId(orderId));
        }else if (orderType.equals(OrderTypeEnums.报价单.getCode())){
            return AjaxResult.success(priceSheetOrderService.queryBrtPriceSheetOrderByOrderId(orderId));
        }
        return AjaxResult.error();
    }

    /**
     * 查询订单流程和节点
     * @param orderId
     * @param templateId
     * @param childId
     * @param isFilterVoid
     * @return
     */
    public Map<String,Object> getOrderTempleatAndOrderNode(String orderId, String templateId, String childId, String isFilterVoid){
        List<BrtOrderTemplate> list = this.list(new LambdaQueryWrapper<BrtOrderTemplate>().eq(BrtOrderTemplate::getOrderId, orderId).eq(BrtOrderTemplate::getTemplateId, templateId));
        if(ObjectUtils.isNotEmpty(list)&&list.size()>0){
            BrtOrderTemplate brtOrderTemplate = list.get(0);
            BrtOrderNodeVo orderNodeVo = new BrtOrderNodeVo();
            orderNodeVo.setOrderId(orderId);
            orderNodeVo.setTemplateId(templateId);
            orderNodeVo.setChildId(childId);
            orderNodeVo.setIsFilterVoid(isFilterVoid);
            List<BrtOrderNodeVo> brtOrderNodeVos = orderNodeService.queryBrtOrderNodeAll(orderNodeVo);

            Map<String,Object>map = new HashMap<>();
            map.put("orderTemplate",brtOrderTemplate);
            map.put("orderNodeList",brtOrderNodeVos);
            return map;
        }
        return null;
    }

}
