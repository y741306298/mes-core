package com.brt.order.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.domain.AjaxResult;
import com.brt.common.core.domain.entity.SysUser;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.enums.DeliveryStatusEnums;
import com.brt.common.enums.NodeOtherSettingEnums;
import com.brt.common.enums.NodeTypeEnums;
import com.brt.common.enums.YesOrNoEnums;
import com.brt.common.utils.PageUtils;
import com.brt.common.utils.SecurityUtils;
import com.brt.common.utils.StringUtils;
import com.brt.order.domain.*;
import com.brt.order.mapper.BrtOrderNodeMapper;
import com.brt.order.mapper.BrtSalesOrderMapper;
import com.brt.order.service.*;
import com.brt.order.vo.*;
import com.brt.order.mapper.BrtCustomerDeliveryMapper;
import com.brt.system.service.ISysUserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.*;

/**
 * 客户送货单Service业务层处理
 *
 * @author Fgn
 * @date 2024-06-28
 */
@Service
public class BrtCustomerDeliveryServiceImpl extends ServiceImpl<BrtCustomerDeliveryMapper, BrtCustomerDelivery> implements IBrtCustomerDeliveryService {

    @Autowired
    private BrtSalesOrderMapper salesOrderMapper;

    @Autowired
    private BrtOrderNodeMapper orderNodeMapper;

    @Autowired
    private IBrtFlowNodeService flowNodeService;

    @Autowired
    private IBrtOrderMaterielPlanService orderMaterielPlanService;

    @Autowired
    private IBrtSalesOrderDetailsService salesOrderDetailsService;

    @Autowired
    private IBrtSalesOrderService salesOrderService;


    @Override
    public TableDataInfo<BrtCustomerDeliveryVo> queryBrtCustomerDeliveryList(BrtCustomerDeliveryVo brtCustomerDeliveryVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtCustomerDeliveryList(PageUtils.buildPage(), brtCustomerDeliveryVo));
    }

    @Override
    public List<BrtCustomerDeliveryVo> queryBrtCustomerDeliveryAll(BrtCustomerDeliveryVo brtCustomerDeliveryVo) {
        return this.baseMapper.queryBrtCustomerDeliveryList(brtCustomerDeliveryVo);
    }

    @Override
    public BrtCustomerDeliveryVo queryBrtCustomerDeliveryByDeliveryId(String deliveryId) {
        return this.baseMapper.queryBrtCustomerDeliveryByDeliveryId(deliveryId);
    }

    @Transactional
    @Override
    public BrtCustomerDeliveryVo insertBrtCustomerDelivery(BrtCustomerDeliveryVo brtCustomerDeliveryVo) {
        saveBefore(brtCustomerDeliveryVo);
        int i = this.baseMapper.insert(brtCustomerDeliveryVo);
        return brtCustomerDeliveryVo;
    }

    @Transactional
    @Override
    public BrtCustomerDeliveryVo updateBrtCustomerDelivery(BrtCustomerDeliveryVo brtCustomerDeliveryVo) {
        saveBefore(brtCustomerDeliveryVo);
        int i = this.baseMapper.updateById(brtCustomerDeliveryVo);
        return brtCustomerDeliveryVo;
    }

    @Transactional
    @Override
    public int deleteBrtCustomerDeliveryByDeliveryIds(String[] deliveryIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(deliveryIds));
    }

    @Override
    public int createCustomerDelivery(BrtOrderNode orderNode) {
        // 查询订单的节点 有没有操作过生成收货单操作
        Integer count = orderNodeMapper.selectCount(new LambdaQueryWrapper<BrtOrderNode>().apply("find_in_set({0},oper_setting)", NodeOtherSettingEnums.生成送货单.getCode()).eq(BrtOrderNode::getOrderId, orderNode.getOrderId()));
        if (count <= 0){
            BrtSalesOrder salesOrder = salesOrderMapper.selectById(orderNode.getOrderId());
            if (ObjectUtil.isNotEmpty(salesOrder)){
                // 生成送货单
                BrtCustomerDeliveryVo deliveryVo = new BrtCustomerDeliveryVo();
                deliveryVo.setCustomerId(salesOrder.getCustomerId());
                deliveryVo.setOrderId(salesOrder.getOrderId());
                deliveryVo.setOrderNodeId(orderNode.getOrderNodeId());
                deliveryVo.setDeliveryStatus(DeliveryStatusEnums.未送货.getCode());
                this.baseMapper.insert(deliveryVo);

                if (StringUtils.isNotBlank(orderNode.getOrderNodeId())){
                    orderNode.setOperSetting(orderNode.getOperSetting()+","+NodeOtherSettingEnums.生成送货单.getCode());
                    orderNodeMapper.updateById(orderNode);
                }else {
                    orderNode.setOperSetting(NodeOtherSettingEnums.生成送货单.getCode());
                }

            }
        }

        // 生成送货计划数量任务
        // 判断当前节点是否是一个数量记录任务类型
        String nodeId = orderNode.getNodeId();
        BrtFlowNode node = flowNodeService.getById(nodeId);
        if(node.getNodeType()!=null && !NodeTypeEnums.数量记录任务.getCode().equals(node.getNodeType())){

            // 新增数量计划
            BrtSalesOrderVo brtSalesOrderVo = salesOrderMapper.queryBrtSalesOrderByOrderId(orderNode.getOrderId());
            List<BrtSalesOrderDetailsVo> orderDetailsVoList = salesOrderDetailsService.getBrtSalesOrderDetailsVoListByOrderId(orderNode.getOrderId());
            orderDetailsVoList.forEach(orderDetails -> {
                BrtOrderMaterielPlanVo orderMaterielPlanVo = new BrtOrderMaterielPlanVo();
                orderMaterielPlanVo.setNodeId(orderNode.getNodeId());
                orderMaterielPlanVo.setOrderNodeId(orderNode.getOrderNodeId());
                orderMaterielPlanVo.setOrderId(orderNode.getOrderId());
                orderMaterielPlanVo.setOrderNo(brtSalesOrderVo.getOrderNo());
                orderMaterielPlanVo.setOrderDetailsId(orderDetails.getDetailsId());
                orderMaterielPlanVo.setOrderDetailsNo(orderDetails.getDetailsNo());
                orderMaterielPlanVo.setMaterielId(orderDetails.getMaterielId());
                orderMaterielPlanVo.setPlanNum(orderDetails.getDetailsNum());
                orderMaterielPlanVo.setOrderNum(orderDetails.getDetailsNum());
                orderMaterielPlanService.save(orderMaterielPlanVo);
            });
        }
        return 0;
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-06-28
     * @param:
     * @param brtCustomerDeliveryVo
     * @return:
     **/
    public void saveBefore(BrtCustomerDeliveryVo brtCustomerDeliveryVo){

    }

    /**
     * @description: TODO 送货提醒表查询
     * @author: lf
     * @date: 2024-07-09
     * @param:
     * @return:
     * @return Vo
     **/
    public Map<String,Object>deliveryRemind(BrtDeliveryRemindVo deliveryRemindVo){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        String timeStartStr = year+"-01-01";
        String timeEndStr = year+"-12-31";
        deliveryRemindVo.setTimeStart(timeStartStr);
        deliveryRemindVo.setTimeEnd(timeEndStr);

        List<BrtDeliveryRemindVo> brtDeliveryRemindVos = this.baseMapper.deliveryRemind(deliveryRemindVo);
        BigDecimal sumNum = BigDecimal.ZERO;
        for(BrtDeliveryRemindVo dr:brtDeliveryRemindVos){
            BigDecimal detailsNum = dr.getDetailsNum();
            if(ObjectUtil.isNotEmpty(detailsNum)){
                sumNum = sumNum.add(detailsNum);
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("list",brtDeliveryRemindVos);
        map.put("sumNum",sumNum);
        map.put("timeStart",timeStartStr);
        map.put("timeEnd",timeEndStr);
        return map;
    }

    /**
     * @description: TODO 客户送货查询
     * @author: lf
     * @date: 2024-07-09
     * @param:
     * @return:
     * @return Vo
     **/
    public Map<String,Object> deliveryByOrder(BrtDeliveryRemindVo deliveryRemindVo){
        Map<String ,Object> customerData = this.baseMapper.getCustomerData(deliveryRemindVo.getOrderId());
        List<BrtCustomerDeliveryVo> brtCustomerDeliveryVos = baseMapper.deliveryByOrder(deliveryRemindVo.getOrderId());
        customerData.put("list",brtCustomerDeliveryVos);
        return customerData;
    }

    /**
     * @description: TODO 客户送货提交
     * @author: lf
     * @date: 2024-07-09
     * @param:
     * @return:
     * @return Vo
     **/
    @Transactional
    public AjaxResult deliverySubmit(List<BrtDeliveryRemindVo> deliveryRemindVoList ){
        for(BrtDeliveryRemindVo deliveryRemindVo:deliveryRemindVoList){
            String s = this.baseMapper.queryMaterielNum(deliveryRemindVo.getPlanId());
            BigDecimal bigDecimal = new BigDecimal(s);
            if(deliveryRemindVo.getThisNum().compareTo(bigDecimal)>0){
                throw new RuntimeException("本次送货数量超出未送货数量！");
            }
            this.baseMapper.updateMaterielNum(deliveryRemindVo);
        }
        String orderId = deliveryRemindVoList.get(0).getOrderId();

        List<Map<String, Object>> dataList = baseMapper.getDeliveryNum(orderId);

        boolean b1 = true;//是否全部送货
        boolean b2 = true;//是否未送货

        //判断此送货单是否已全部送货完成
        if(ObjectUtil.isNotEmpty(dataList)){
            for(Map<String,Object> map : dataList){
                String fulfillNum = String.valueOf(map.get("fulfillNum"));//已送货数量
                String surplusNum = String.valueOf(map.get("surplusNum"));//未送货数量

                if(StringUtils.isNotEmpty(surplusNum) && !"0".equals(surplusNum)){
                    b1 = false;
                    if(StringUtils.isNotEmpty(fulfillNum) && !"0".equals(fulfillNum)){
                        b2 = false;
                    }
                }
            }
        }
        if(b1){//全部送货
            BrtSalesOrder salesOrder = salesOrderService.getById(orderId);
            salesOrder.setOrderStatus("1");
            Date deliveryTime = salesOrder.getDeliveryTime();
            Date nowDate  = new Date();
            salesOrder.setPostpone(ObjectUtil.isNotEmpty(deliveryTime)&&nowDate.compareTo(deliveryTime)>0? YesOrNoEnums.YES.getCode():YesOrNoEnums.NO.getCode());
            salesOrderService.updateById(salesOrder);
            this.update(new LambdaUpdateWrapper<BrtCustomerDelivery>().set(BrtCustomerDelivery::getDeliveryStatus,DeliveryStatusEnums.已送货.getCode()).eq(BrtCustomerDelivery::getOrderId,orderId));
        }else{
            if(!b2){//部分送货
                this.update(new LambdaUpdateWrapper<BrtCustomerDelivery>().set(BrtCustomerDelivery::getDeliveryStatus,DeliveryStatusEnums.部分送货.getCode()).eq(BrtCustomerDelivery::getOrderId,orderId));
            }else{//未送货

            }
        }

        return AjaxResult.success();
    }

    @Autowired
    private ISysUserService userService;

    public Map<String,Object> getPrint(String orderId){
        Map<String, Object> print = this.baseMapper.getPrint(orderId);
        SysUser sysUser = userService.selectUserById(SecurityUtils.getUserId());
        List<Map<String, Object>> printTable = this.baseMapper.getPrintTable(orderId);
        print.put("list",printTable);
        print.put("dataDate",new Date());
        print.put("thisUser",sysUser.getUserName());
        return print;
    }

}
