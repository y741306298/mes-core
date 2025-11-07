package com.brt.order.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.domain.AjaxResult;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.enums.ReceivingStatusEnums;
import com.brt.common.enums.NodeOtherSettingEnums;
import com.brt.common.enums.NodeTypeEnums;
import com.brt.common.utils.PageUtils;
import com.brt.common.utils.StringUtils;
import com.brt.order.domain.*;
import com.brt.order.mapper.BrtMarketOrderMapper;
import com.brt.order.mapper.BrtOrderNodeMapper;
import com.brt.order.mapper.BrtSalesOrderMapper;
import com.brt.order.service.*;
import com.brt.order.vo.*;
import com.brt.order.mapper.BrtSupplierReceivingMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.*;

/**
 * 客户收货单Service业务层处理
 *
 * @author Fgn
 * @date 2024-06-28
 */
@Service
public class BrtSupplierReceivingServiceImpl extends ServiceImpl<BrtSupplierReceivingMapper, BrtSupplierReceiving> implements IBrtSupplierReceivingService {

    @Autowired
    private BrtMarketOrderMapper marketOrderMapper;

    @Autowired
    private BrtOrderNodeMapper orderNodeMapper;

    @Autowired
    private IBrtFlowNodeService flowNodeService;

    @Autowired
    private IBrtOrderMaterielPlanService orderMaterielPlanService;

    @Autowired
    private IBrtMarketOrderDetailsService marketOrderDetailsService;


    @Autowired
    private IBrtMarketOrderService marketOrderService;

    @Override
    public TableDataInfo<BrtSupplierReceivingVo> queryBrtSupplierReceivingList(BrtSupplierReceivingVo brtSupplierReceivingVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtSupplierReceivingList(PageUtils.buildPage(), brtSupplierReceivingVo));
    }

    @Override
    public List<BrtSupplierReceivingVo> queryBrtSupplierReceivingAll(BrtSupplierReceivingVo brtSupplierReceivingVo) {
        return this.baseMapper.queryBrtSupplierReceivingList(brtSupplierReceivingVo);
    }

    @Override
    public BrtSupplierReceivingVo queryBrtSupplierReceivingByReceivingId(String receivingId) {
        return this.baseMapper.queryBrtSupplierReceivingByReceivingId(receivingId);
    }

    @Transactional
    @Override
    public BrtSupplierReceivingVo insertBrtSupplierReceiving(BrtSupplierReceivingVo brtSupplierReceivingVo) {
        saveBefore(brtSupplierReceivingVo);
        int i = this.baseMapper.insert(brtSupplierReceivingVo);
        return brtSupplierReceivingVo;
    }

    @Transactional
    @Override
    public BrtSupplierReceivingVo updateBrtSupplierReceiving(BrtSupplierReceivingVo brtSupplierReceivingVo) {
        saveBefore(brtSupplierReceivingVo);
        int i = this.baseMapper.updateById(brtSupplierReceivingVo);
        return brtSupplierReceivingVo;
    }

    @Transactional
    @Override
    public int deleteBrtSupplierReceivingByReceivingIds(String[] receivingIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(receivingIds));
    }

    @Override
    public int createSupplierReceiving(BrtOrderNode orderNode) {
        // 查询订单的节点 有没有操作过生成收货单操作
        Integer count = orderNodeMapper.selectCount(new LambdaQueryWrapper<BrtOrderNode>().apply("find_in_set({0},oper_setting)", NodeOtherSettingEnums.生成收货单.getCode()).eq(BrtOrderNode::getOrderId, orderNode.getOrderId()));
        if (count <= 0){
            BrtMarketOrder brtMarketOrder = marketOrderMapper.selectById(orderNode.getOrderId());
            if (ObjectUtil.isNotEmpty(brtMarketOrder)){
                // 生成收货单
                BrtSupplierReceivingVo receivingVo = new BrtSupplierReceivingVo();
                receivingVo.setSupplierId(brtMarketOrder.getSupplierId());
                receivingVo.setOrderId(brtMarketOrder.getOrderId());
                receivingVo.setOrderNodeId(orderNode.getOrderNodeId());
                receivingVo.setReceivingStatus(ReceivingStatusEnums.未收货.getCode());
                this.baseMapper.insert(receivingVo);

                if (StringUtils.isNotBlank(orderNode.getOrderNodeId())){
                    orderNode.setOperSetting(orderNode.getOperSetting()+","+NodeOtherSettingEnums.生成收货单.getCode());
                    orderNodeMapper.updateById(orderNode);
                }else {
                    orderNode.setOperSetting(NodeOtherSettingEnums.生成收货单.getCode());
                }

            }
        }

        // 生成收货计划数量任务
        // 判断当前节点是否是一个数量记录任务类型
        String nodeId = orderNode.getNodeId();
        BrtFlowNode node = flowNodeService.getById(nodeId);
        if(node.getNodeType()!=null && !NodeTypeEnums.数量记录任务.getCode().equals(node.getNodeType())){

            // 新增数量计划
            BrtMarketOrderVo brtMarketOrderVo = marketOrderMapper.queryBrtMarketOrderByOrderId(orderNode.getOrderId());
            List<BrtMarketOrderDetailsVo> orderDetailsVoList = marketOrderDetailsService.getBrtMarketOrderDetailsVoListByOrderId(orderNode.getOrderId());
            orderDetailsVoList.forEach(orderDetails -> {
                BrtOrderMaterielPlanVo orderMaterielPlanVo = new BrtOrderMaterielPlanVo();
                orderMaterielPlanVo.setNodeId(orderNode.getNodeId());
                orderMaterielPlanVo.setOrderNodeId(orderNode.getOrderNodeId());
                orderMaterielPlanVo.setOrderId(orderNode.getOrderId());
                orderMaterielPlanVo.setOrderNo(brtMarketOrderVo.getOrderNo());
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
     * @param brtSupplierReceivingVo
     * @return:
     **/
    public void saveBefore(BrtSupplierReceivingVo brtSupplierReceivingVo){

    }

    /**
     * @description: TODO 收货提醒表查询
     * @author: lf
     * @date: 2024-07-09
     * @param:
     * @return:
     * @return Vo
     **/

    public Map<String,Object>receivingRemind(BrtReceivingRemindVo receivingRemindVo){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        String timeStartStr = year+"-01-01";
        String timeEndStr = year+"-12-31";
        receivingRemindVo.setTimeStart(timeStartStr);
        receivingRemindVo.setTimeEnd(timeEndStr);

        List<BrtReceivingRemindVo> brtReceivingRemindVos = this.baseMapper.receivingRemind(receivingRemindVo);
        BigDecimal sumNum = BigDecimal.ZERO;
        for(BrtReceivingRemindVo dr:brtReceivingRemindVos){
            BigDecimal detailsNum = dr.getDetailsNum();
            if(ObjectUtil.isNotEmpty(detailsNum)){
                sumNum = sumNum.add(detailsNum);
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("list",brtReceivingRemindVos);
        map.put("sumNum",sumNum);
        map.put("timeStart",timeStartStr);
        map.put("timeEnd",timeEndStr);
        return map;
    }

    /**
     * @description: TODO 客户收货查询
     * @author: lf
     * @date: 2024-07-09
     * @param:
     * @return:
     * @return Vo
     **/
    public Map<String,Object> receivingByOrder(BrtReceivingRemindVo receivingRemindVo){
        Map<String, Object> supplierData = baseMapper.getSupplierData(receivingRemindVo.getOrderId());
        List<BrtSupplierReceivingVo> brtSupplierReceivingVos = baseMapper.receivingByOrder(receivingRemindVo.getOrderId());
        supplierData.put("list",brtSupplierReceivingVos);
        return supplierData;
    }

    /**
     * @description: TODO 客户收货提交
     * @author: lf
     * @date: 2024-07-09
     * @param:
     * @return:
     * @return Vo
     **/
    @Transactional
    public AjaxResult receivingSubmit(List<BrtReceivingRemindVo> receivingRemindVoList ){
        for(BrtReceivingRemindVo receivingRemindVo:receivingRemindVoList){
            String s = this.baseMapper.queryMaterielNum(receivingRemindVo.getPlanId());
            BigDecimal bigDecimal = new BigDecimal(s);
            if(receivingRemindVo.getThisNum().compareTo(bigDecimal)>0){
                throw new RuntimeException("本次收货数量超出未收货数量！");
            }
            this.baseMapper.updateMaterielNum(receivingRemindVo);
        }
        String orderId = receivingRemindVoList.get(0).getOrderId();

        List<String> receivingNums = baseMapper.getReceivingNum(orderId);
        boolean b = true;

        if(ObjectUtil.isNotEmpty(receivingNums)){
            for(String receivingNum : receivingNums){
                if(StringUtils.isNotEmpty(receivingNum) && !"0".equals(receivingNum)){
                    b = false;
                }
            }
        }
        if(b){
            BrtMarketOrder marketOrder = marketOrderService.getById(orderId);
            marketOrder.setOrderStatus("1");
            marketOrderService.updateById(marketOrder);
        }

        return AjaxResult.success();
    }

    public Map<String,Object> getPrint(String orderId){
        Map<String, Object> print = this.baseMapper.getPrint(orderId);
        List<Map<String, Object>> printTable = this.baseMapper.getPrintTable(orderId);
        print.put("list",printTable);
        return print;
    }

}
