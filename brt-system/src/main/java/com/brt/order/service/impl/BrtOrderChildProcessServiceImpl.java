package com.brt.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.enums.ChildStatusEnums;
import com.brt.common.enums.NodeTypeEnums;
import com.brt.common.enums.OrderTypeEnums;
import com.brt.common.enums.YesOrNoEnums;
import com.brt.common.utils.PageUtils;
import com.brt.order.domain.*;
import com.brt.order.service.*;
import com.brt.order.vo.*;
import com.brt.order.mapper.BrtOrderChildProcessMapper;
import com.brt.order.vo.pub.BrtOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 订单子流程Service业务层处理
 *
 * @author Fgn
 * @date 2024-06-21
 */
@Service
public class BrtOrderChildProcessServiceImpl extends ServiceImpl<BrtOrderChildProcessMapper, BrtOrderChildProcess> implements IBrtOrderChildProcessService {

    @Autowired
    private IBrtSalesOrderService salesOrderService;

    @Autowired
    private IBrtSalesOrderDetailsService salesOrderDetailsService;

    @Autowired
    private IBrtFlowNodeService flowNodeService;

    @Autowired
    private IBrtOrderExamineService orderExamineService;

    @Autowired
    private IBrtMarketOrderService marketOrderService;

    @Autowired
    private IBrtMarketOrderDetailsService marketOrderDetailsService;

    @Autowired
    private IBrtPriceSheetOrderService priceSheetOrderService;

    @Autowired
    private IBrtPriceSheetOrderDetailsService priceSheetOrderDetailsService;


    @Override
    public TableDataInfo<BrtOrderChildProcessVo> queryBrtOrderChildProcessList(BrtOrderChildProcessVo brtOrderChildProcessVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtOrderChildProcessList(PageUtils.buildPage(), brtOrderChildProcessVo));
    }

    @Override
    public List<BrtOrderChildProcessVo> queryBrtOrderChildProcessAll(BrtOrderChildProcessVo brtOrderChildProcessVo) {
        return this.baseMapper.queryBrtOrderChildProcessList(brtOrderChildProcessVo);
    }

    @Override
    public BrtOrderChildProcessVo queryBrtOrderChildProcessByChildId(String childId) {
        return this.baseMapper.queryBrtOrderChildProcessByChildId(childId);
    }

    @Transactional
    @Override
    public BrtOrderChildProcessVo insertBrtOrderChildProcess(BrtOrderChildProcessVo brtOrderChildProcessVo) {
        saveBefore(brtOrderChildProcessVo);

        String orderType = brtOrderChildProcessVo.getOrderType();
        String orderId = brtOrderChildProcessVo.getOrderId();

        BrtOrderVo orderVo = new BrtOrderVo();
        OrderTypeEnums orderTypeEnums = null;
        if(orderType.equals(OrderTypeEnums.销售单.getCode())){
            BrtSalesOrder salesOrder = salesOrderService.getById(brtOrderChildProcessVo.getOrderId());
            List<BrtSalesOrderDetails> salesOrderDetailsList = salesOrderDetailsService.list(new LambdaQueryWrapper<BrtSalesOrderDetails>().eq(BrtSalesOrderDetails::getOrderId, salesOrder.getOrderId()));
            BrtSalesOrderVo salesOrderVo = BeanUtil.copyProperties(salesOrder, BrtSalesOrderVo.class);
            salesOrderVo.setSalesOrderDetailsVoList(BeanUtil.copyToList(salesOrderDetailsList, BrtSalesOrderDetailsVo.class));
            salesOrderVo.setTemplateId(brtOrderChildProcessVo.getTemplateId());

            // 生成订单审批信息
            orderVo.setSalesOrderVo(salesOrderVo);
            orderVo.setOrderType(OrderTypeEnums.销售单);
            orderTypeEnums = OrderTypeEnums.销售单;

        }else if(orderType.equals(OrderTypeEnums.报价单.getCode())){
            BrtPriceSheetOrder priceSheetOrder = priceSheetOrderService.getById(brtOrderChildProcessVo.getOrderId());
            List<BrtPriceSheetOrderDetails> list = priceSheetOrderDetailsService.list(new LambdaQueryWrapper<BrtPriceSheetOrderDetails>().eq(BrtPriceSheetOrderDetails::getOrderId, priceSheetOrder.getOrderId()));
            BrtPriceSheetOrderVo priceSheetOrderVo = BeanUtil.copyProperties(priceSheetOrder, BrtPriceSheetOrderVo.class);
            priceSheetOrderVo.setPriceSheetOrderDetailsVoList(BeanUtil.copyToList(list, BrtPriceSheetOrderDetailsVo.class));
            priceSheetOrderVo.setTemplateId(brtOrderChildProcessVo.getTemplateId());

            // 生成订单审批信息
            orderVo.setPriceSheetOrderVo(priceSheetOrderVo);
            orderVo.setOrderType(OrderTypeEnums.报价单);
            orderTypeEnums = OrderTypeEnums.报价单;

        }else if(orderType.equals(OrderTypeEnums.采购单.getCode())){
            BrtMarketOrder marketOrder = marketOrderService.getById(brtOrderChildProcessVo.getOrderId());
            List<BrtMarketOrderDetails> marketOrderDetailsList = marketOrderDetailsService.list(new LambdaQueryWrapper<BrtMarketOrderDetails>().eq(BrtMarketOrderDetails::getOrderId, marketOrder.getOrderId()));
            BrtMarketOrderVo marketOrderVo = BeanUtil.copyProperties(marketOrder, BrtMarketOrderVo.class);
            marketOrderVo.setMarketOrderDetailsVoList(BeanUtil.copyToList(marketOrderDetailsList, BrtMarketOrderDetailsVo.class));
            marketOrderVo.setTemplateId(brtOrderChildProcessVo.getTemplateId());

            // 生成订单审批信息
            orderVo.setMarketOrderVo(marketOrderVo);
            orderVo.setOrderType(OrderTypeEnums.采购单);
            orderTypeEnums = OrderTypeEnums.采购单;
        }

        // 查询订单信息

        //查询订单产品信息


        // 查询模板节点数量
        List<BrtFlowNode> flowNodeList = flowNodeService.list(new LambdaQueryWrapper<BrtFlowNode>().eq(BrtFlowNode::getTemplateId, brtOrderChildProcessVo.getTemplateId()).eq(BrtFlowNode::getNodeStatus, YesOrNoEnums.YES.getCode()));
        brtOrderChildProcessVo.setNodeNum(Long.valueOf(flowNodeList.size()));

        // 获取节点列表中的审批节点
        Optional<BrtFlowNode> auditFlowNodeOptional = flowNodeList.stream().filter(node -> node.getNodeType().equals(NodeTypeEnums.审批.getCode())).findFirst();
        if (auditFlowNodeOptional.isPresent()){
            brtOrderChildProcessVo.setChildStatus(ChildStatusEnums.待审核.getCode());
        }else {
            brtOrderChildProcessVo.setChildStatus(ChildStatusEnums.待完成.getCode());
        }

        int i = this.baseMapper.insert(brtOrderChildProcessVo);


        orderExamineService.createOrderExamine(orderVo, orderTypeEnums,brtOrderChildProcessVo.getChildId());

        return brtOrderChildProcessVo;
    }

    @Transactional
    @Override
    public BrtOrderChildProcessVo updateBrtOrderChildProcess(BrtOrderChildProcessVo brtOrderChildProcessVo) {
        saveBefore(brtOrderChildProcessVo);
        int i = this.baseMapper.updateById(brtOrderChildProcessVo);
        return brtOrderChildProcessVo;
    }

    @Transactional
    @Override
    public int deleteBrtOrderChildProcessByChildIds(String[] childIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(childIds));
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-06-21
     * @param:
     * @param brtOrderChildProcessVo
     * @return:
     **/
    public void saveBefore(BrtOrderChildProcessVo brtOrderChildProcessVo){

    }

}
