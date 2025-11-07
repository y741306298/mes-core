package com.brt.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.domain.entity.SysUser;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.enums.*;
import com.brt.common.utils.PageUtils;
import com.brt.common.utils.SecurityUtils;
import com.brt.common.utils.StringUtils;
import com.brt.order.domain.*;
import com.brt.order.mapper.*;
import com.brt.order.service.*;
import com.brt.order.vo.*;
import com.brt.order.vo.pub.BrtOrderVo;
import com.brt.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.NodeList;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单审批Service业务层处理
 *
 * @author Fgn
 * @date 2024-05-10
 */
@Service
public class BrtOrderExamineServiceImpl extends ServiceImpl<BrtOrderExamineMapper, BrtOrderExamine> implements IBrtOrderExamineService {

    @Autowired
    private IBrtOrderDynamicService orderDynamicService;

    @Autowired
    private BrtFlowTemplateMapper flowTemplateMapper;

    @Autowired
    private BrtOrderTemplateMapper orderTemplateMapper;

    @Autowired
    private IBrtOrderNodeService orderNodeService;

    @Autowired
    private BrtFlowNodeMapper flowNodeMapper;

    @Autowired
    private IBrtMaterielService materielService;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private IBrtOrderInvoicePlanService orderInvoicePlanService;

    @Autowired
    private IBrtOrderCollectionPlanService orderCollectionPlanService;

    @Autowired
    private IBrtOrderMaterielPlanService orderMaterielPlanService;

    @Autowired
    private IBrtSalesOrderService salesOrderService;

    @Autowired
    private IBrtSalesOrderDetailsService salesOrderDetailsService;

    @Autowired
    private IBrtCustomerDeliveryService customerDeliveryService;

    @Autowired
    private IBrtSupplierReceivingService supplierReceivingService;

    @Autowired
    private IBrtMarketOrderService marketOrderService;

    @Autowired
    private IBrtPriceSheetOrderService priceSheetOrderService;

    @Autowired
    private IBrtFlowNodeService flowNodeService;

    @Override
    public TableDataInfo<BrtOrderExamineVo> queryBrtOrderExamineList(BrtOrderExamineVo brtOrderExamineVo) {
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())){
            brtOrderExamineVo.setAuditUserId(SecurityUtils.getUserId().toString());
        }
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtOrderExamineList(PageUtils.buildPage(), brtOrderExamineVo));
    }

    @Override
    public List<BrtOrderExamineVo> queryBrtOrderExamineAll(BrtOrderExamineVo brtOrderExamineVo) {
        return this.baseMapper.queryBrtOrderExamineList(brtOrderExamineVo);
    }

    @Override
    public BrtOrderExamineVo queryBrtOrderExamineByExamineId(String examineId) {
        return this.baseMapper.queryBrtOrderExamineByExamineId(examineId);
    }

    @Transactional
    @Override
    public BrtOrderExamineVo insertBrtOrderExamine(BrtOrderExamineVo brtOrderExamineVo) {
        saveBefore(brtOrderExamineVo);
        int i = this.baseMapper.insert(brtOrderExamineVo);
        return brtOrderExamineVo;
    }

    @Transactional
    @Override
    public BrtOrderExamineVo updateBrtOrderExamine(BrtOrderExamineVo brtOrderExamineVo) {
        saveBefore(brtOrderExamineVo);
        int i = this.baseMapper.updateById(brtOrderExamineVo);
        return brtOrderExamineVo;
    }

    @Transactional
    @Override
    public int deleteBrtOrderExamineByExamineIds(String[] examineIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(examineIds));
    }



    public void updateStatus(BrtOrderVo orderVo,OrderAuditStatus orderAuditStatus){
        if(OrderTypeEnums.销售单.getCode().equals(orderVo.getOrderType().getCode())){
            BrtSalesOrderVo brtSalesOrderVo = new BrtSalesOrderVo();
            brtSalesOrderVo.setOrderId(orderVo.getOrderId());
            brtSalesOrderVo.setStatus(orderAuditStatus.getCode());
            salesOrderService.updateById(brtSalesOrderVo);
        }else if(OrderTypeEnums.采购单.getCode().equals(orderVo.getOrderType().getCode())){
            BrtMarketOrderVo brtMarketOrderVo = new BrtMarketOrderVo();
            brtMarketOrderVo.setOrderId(orderVo.getOrderId());
            brtMarketOrderVo.setStatus(orderAuditStatus.getCode());
            marketOrderService.updateById(brtMarketOrderVo);
        }else if(OrderTypeEnums.报价单.getCode().equals(orderVo.getOrderType().getCode())){
            BrtPriceSheetOrderVo brtPriceSheetOrderVo = new BrtPriceSheetOrderVo();
            brtPriceSheetOrderVo.setOrderId(orderVo.getOrderId());
            brtPriceSheetOrderVo.setStatus(orderAuditStatus.getCode());
            priceSheetOrderService.updateById(brtPriceSheetOrderVo);
        }
    }

    /**
     * @description: TODO 生成订单审批信息
     * @author: FanGN
     * @date: 17:42 2024/5/10
     * @param:
     * @param orderVo
     * @return:
     **/
    @Override
    public void createOrderExamine(BrtOrderVo orderVo, OrderTypeEnums orderTypeEnums, String childId) {
        // 查询流程模板信息
        BrtFlowTemplate flowTemplate = flowTemplateMapper.selectById(orderVo.getTemplateId());
        if (ObjectUtil.isNotEmpty(flowTemplate)){

            // 生成流程模板
            BrtOrderTemplateVo orderTemplateVo = new BrtOrderTemplateVo(orderVo);
            orderTemplateVo.setOrderTemplateStatus(OrderTemplateStatusEnums.正常.getCode());
            orderTemplateVo.setAuditStatus(AuditStatusEnums.通过.getCode());
            updateStatus(orderVo,OrderAuditStatus.待审核);
            orderTemplateVo.setDetailsDesc(getOrderDetailsDesc(orderVo));
            orderTemplateVo.setChildId(childId);
            orderTemplateVo.setStatus("1");
            if(orderTypeEnums.getCode().equals(OrderTypeEnums.销售单.getCode())||orderTypeEnums.getCode().equals(OrderTypeEnums.报价单.getCode())){
                orderTemplateVo.setCustomerId(orderVo.getCustomerId());
            }
            if(orderTypeEnums.getCode().equals(OrderTypeEnums.采购单.getCode())){
                orderTemplateVo.setSupplierId(orderVo.getCustomerId());
            }
            orderTemplateVo.setTemplateType(StringUtils.isEmpty(childId) ? OrderTemplateTypeEnums.订单模板.getCode() : OrderTemplateTypeEnums.订单子流程模板.getCode());
            // 保存流程模板
            orderTemplateMapper.insert(orderTemplateVo);

            //查询流程是否有启用的审批节点
            BrtFlowNodeVo examineNode = flowNodeService.getFlowNode(flowTemplate.getTemplateId(), "审批",YesOrNoEnums.YES.getCode());

            //是否需要审批
            boolean isAudit = flowTemplate.getIsAudit().equals(YesOrNoEnums.YES.getCode())&&ObjectUtil.isNotEmpty(examineNode);
            //是否顺序执行
            Boolean isSeqExecute = flowTemplate.getIsSeqExecute();

            // 判断流程模板是否需要审批
            if (flowTemplate.getIsAudit().equals(YesOrNoEnums.YES.getCode()) && ObjectUtil.isNotEmpty(examineNode)){

                // 创建审批信息
                BrtOrderExamineVo brtOrderExamineVo = new BrtOrderExamineVo(orderVo);
                brtOrderExamineVo.setAuditStatus(AuditStatusEnums.待审核.getCode());
                brtOrderExamineVo.setAuditUserId(flowTemplate.getAuditUserId());
                brtOrderExamineVo.setChildId(childId);
                brtOrderExamineVo.setOrderTemplateId(orderTemplateVo.getOrderTemplateId());
                this.baseMapper.insert(brtOrderExamineVo);

                orderTemplateVo.setAuditStatus(AuditStatusEnums.待审核.getCode());
                orderTemplateVo.setStatus("0");
                orderTemplateMapper.updateById(orderTemplateVo);
                updateStatus(orderVo,OrderAuditStatus.待审核);
            }else {

            }

            String thisUserId = SecurityUtils.getUserId().toString();
            // 生成流程节点
            // 查询模板节点
            List<BrtFlowNode> flowNodeList = flowNodeMapper.selectList(new LambdaQueryWrapper<BrtFlowNode>().eq(BrtFlowNode::getTemplateId, flowTemplate.getTemplateId()).eq(BrtFlowNode::getNodeStatus,YesOrNoEnums.YES.getCode()).orderByAsc(BrtFlowNode::getSort));
            flowNodeList.forEach(flowNode -> {
                BrtOrderNodeVo orderNodeVo = new BrtOrderNodeVo();
                String userId = flowTemplate.getUserId();//流程负责人
                orderNodeVo.setPrincipal(userId);
                orderNodeVo.setOrderTemplateId(orderTemplateVo.getOrderTemplateId());
                orderNodeVo.setChildId(childId);
                orderNodeVo.setOrderId(orderVo.getOrderId());
                orderNodeVo.setTemplateId(flowTemplate.getTemplateId());
                orderNodeVo.setNodeId(flowNode.getNodeId());
                orderNodeVo.setSort(flowNode.getSort().longValue());
                orderNodeVo.setCreateId(thisUserId);




                String deadlineType = flowNode.getDeadlineType();
                String day = StringUtils.isEmpty(flowNode.getDay())?"0":flowNode.getDay();
                String hour = StringUtils.isEmpty(flowNode.getHour())?"0":flowNode.getHour();
                String minute = StringUtils.isEmpty(flowNode.getMinute())?"0":flowNode.getMinute();

                if(StringUtils.isNotEmpty(deadlineType)){
                    if("2".equals(deadlineType)){
                        Date date = orderVo.getDeliveryTime();
                        if(ObjectUtil.isNotEmpty(date)){
                            orderNodeVo.setComplateDate(this.addDate(date,day,hour,minute,0));
                        }
                    }else if("1".equals(deadlineType)){
                        Date date = new Date();
                        orderNodeVo.setComplateDate(this.addDate(date,day,hour,minute,1));
                    }
                }


                // 判断如果节点是审批时 设置审批人
                if (flowTemplate.getIsAudit().equals(YesOrNoEnums.YES.getCode()) && ObjectUtil.isNotEmpty(examineNode) ){
                    if(flowNode.getNodeType().equals(NodeTypeEnums.审批.getCode())){
                        // 查询审核用户信息
                        SysUser user = userMapper.selectUserById(Long.valueOf(flowTemplate.getAuditUserId()));
                        orderNodeVo.setDeptId(user == null ? null : user.getDeptId()+"");
                        orderNodeVo.setUserId(flowTemplate.getAuditUserId());
                        orderNodeVo.setNodeStatus(NodeStatusEnums.进行中.getCode());
                    }else{
                        orderNodeVo.setNodeStatus(NodeStatusEnums.待审核.getCode());
                    }


                }else if ((flowTemplate.getIsAudit().equals(YesOrNoEnums.NO.getCode()) || ObjectUtil.isEmpty(examineNode)) && (!flowTemplate.getIsSeqExecute() || orderNodeVo.getNodeId().equals(flowNodeList.get(0).getNodeId()))){
                        // 如果订单不需要按照顺序执行或者是第一个节点 并且不需要审核的情况下 将所有节点都设置为进行中
                        orderNodeVo.setNodeStatus(NodeStatusEnums.未开始.getCode());

                        // 如果模板不按照顺序执行或者是第一个节点 并且不需要审核，则判断节点是否包含送货单或者发货单
                        if (NodeOtherSettingEnums.isExistence(flowNode.getOtherSetting(),NodeOtherSettingEnums.生成收货单) && orderTypeEnums.getCode().equals(OrderTypeEnums.采购单.getCode())){
                            //--生成收货单
                            supplierReceivingService.createSupplierReceiving(orderNodeVo);
                        } else if (NodeOtherSettingEnums.isExistence(flowNode.getOtherSetting(),NodeOtherSettingEnums.生成送货单) && orderTypeEnums.getCode().equals(OrderTypeEnums.销售单.getCode())) {
                            customerDeliveryService.createCustomerDelivery(orderNodeVo);
                        }
                }

                //非顺序执行且不审批
                if((!isSeqExecute)&&!isAudit){
                    orderNodeVo.setNodeStatus(NodeStatusEnums.进行中.getCode());
                }
                //顺序执行且不审批 且此节点为第一个
                if((isSeqExecute)&&!isAudit&&orderNodeVo.getNodeId().equals(flowNodeList.get(0).getNodeId())){
                    orderNodeVo.setNodeStatus(NodeStatusEnums.进行中.getCode());
                }


                orderNodeService.save(orderNodeVo);

                //判断节点是否是开票
                if (flowNode.getNodeType().equals(NodeTypeEnums.开票金额纪录任务.getCode())){

                    // 新增开票计划
                    BrtOrderInvoicePlanVo orderInvoicePlanVo = new BrtOrderInvoicePlanVo();
                    orderInvoicePlanVo.setNodeId(orderNodeVo.getNodeId());
                    orderInvoicePlanVo.setOrderNodeId(orderNodeVo.getOrderNodeId());
                    orderInvoicePlanVo.setOrderId(orderVo.getOrderId());
                    orderInvoicePlanVo.setOrderNo(orderVo.getOrderNo());
                    orderInvoicePlanVo.setOrderType(orderVo.getOrderType().getCode());
                    orderInvoicePlanVo.setPlanAmount(orderVo.getTotalAmount());
                    orderInvoicePlanVo.setOrderAmount(orderVo.getTotalAmount());
                    orderInvoicePlanService.save(orderInvoicePlanVo);

                } else if (flowNode.getNodeType().equals(NodeTypeEnums.收款金额纪录任务.getCode())) {

                    // 新增收款计划
                    BrtOrderCollectionPlanVo orderCollectionPlanVo = new BrtOrderCollectionPlanVo();
                    orderCollectionPlanVo.setNodeId(orderNodeVo.getNodeId());
                    orderCollectionPlanVo.setOrderNodeId(orderNodeVo.getOrderNodeId());
                    orderCollectionPlanVo.setOrderId(orderVo.getOrderId());
                    orderCollectionPlanVo.setOrderNo(orderVo.getOrderNo());
                    orderCollectionPlanVo.setOrderType(orderVo.getOrderType().getCode());
                    orderCollectionPlanVo.setPlanAmount(orderVo.getTotalAmount());
                    orderCollectionPlanVo.setOrderAmount(orderVo.getTotalAmount());
                    orderCollectionPlanService.save(orderCollectionPlanVo);

                } else if (flowNode.getNodeType().equals(NodeTypeEnums.数量记录任务.getCode())) {

                    if(orderTypeEnums.getCode().equals(OrderTypeEnums.销售单.getCode())){
                        // 新增数量计划
                        List<BrtSalesOrderDetailsVo> orderDetailsVoList = orderVo.getSalesOrderVo().getSalesOrderDetailsVoList();
                        orderDetailsVoList.forEach(orderDetails -> {
                            BrtOrderMaterielPlanVo orderMaterielPlanVo = new BrtOrderMaterielPlanVo();
                            orderMaterielPlanVo.setNodeId(orderNodeVo.getNodeId());
                            orderMaterielPlanVo.setOrderNodeId(orderNodeVo.getOrderNodeId());
                            orderMaterielPlanVo.setOrderId(orderVo.getOrderId());
                            orderMaterielPlanVo.setOrderNo(orderVo.getOrderNo());
                            orderMaterielPlanVo.setOrderDetailsId(orderDetails.getDetailsId());
                            orderMaterielPlanVo.setOrderDetailsNo(orderDetails.getDetailsNo());
                            orderMaterielPlanVo.setMaterielId(orderDetails.getMaterielId());
                            orderMaterielPlanVo.setPlanNum(orderDetails.getDetailsNum());
                            orderMaterielPlanVo.setOrderNum(orderDetails.getDetailsNum());
                            orderMaterielPlanService.save(orderMaterielPlanVo);
                        });
                    }else if(orderTypeEnums.getCode().equals(OrderTypeEnums.采购单.getCode())){
                        // 新增数量计划
                        List<BrtMarketOrderDetailsVo> marketOrderDetailsVoList = orderVo.getMarketOrderVo().getMarketOrderDetailsVoList();
                        marketOrderDetailsVoList.forEach(orderDetails -> {
                            BrtOrderMaterielPlanVo orderMaterielPlanVo = new BrtOrderMaterielPlanVo();
                            orderMaterielPlanVo.setNodeId(orderNodeVo.getNodeId());
                            orderMaterielPlanVo.setOrderNodeId(orderNodeVo.getOrderNodeId());
                            orderMaterielPlanVo.setOrderId(orderVo.getOrderId());
                            orderMaterielPlanVo.setOrderNo(orderVo.getOrderNo());
                            orderMaterielPlanVo.setOrderDetailsId(orderDetails.getDetailsId());
                            orderMaterielPlanVo.setOrderDetailsNo(orderDetails.getDetailsNo());
                            orderMaterielPlanVo.setMaterielId(orderDetails.getMaterielId());
                            orderMaterielPlanVo.setPlanNum(orderDetails.getDetailsNum());
                            orderMaterielPlanVo.setOrderNum(orderDetails.getDetailsNum());
                            orderMaterielPlanService.save(orderMaterielPlanVo);
                        });
                    }else if(orderTypeEnums.getCode().equals(OrderTypeEnums.报价单.getCode())){
                        // 新增数量计划
                        List<BrtPriceSheetOrderDetailsVo> priceSheetOrderDetailsVoList = orderVo.getPriceSheetOrderVo().getPriceSheetOrderDetailsVoList();
                        priceSheetOrderDetailsVoList.forEach(orderDetails -> {
                            BrtOrderMaterielPlanVo orderMaterielPlanVo = new BrtOrderMaterielPlanVo();
                            orderMaterielPlanVo.setNodeId(orderNodeVo.getNodeId());
                            orderMaterielPlanVo.setOrderNodeId(orderNodeVo.getOrderNodeId());
                            orderMaterielPlanVo.setOrderId(orderVo.getOrderId());
                            orderMaterielPlanVo.setOrderNo(orderVo.getOrderNo());
                            orderMaterielPlanVo.setOrderDetailsId(orderDetails.getDetailsId());
                            orderMaterielPlanVo.setOrderDetailsNo(orderDetails.getDetailsNo());
                            orderMaterielPlanVo.setMaterielId(orderDetails.getMaterielId());
                            orderMaterielPlanVo.setPlanNum(orderDetails.getDetailsNum());
                            orderMaterielPlanVo.setOrderNum(orderDetails.getDetailsNum());
                            orderMaterielPlanService.save(orderMaterielPlanVo);
                        });
                    }


                }
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int audit(String examineIds, String auditStatus,String childId, String auditRemark) {
        List<String> examineIdList = Arrays.asList(examineIds.split(","));
        //查询审批信息
        List<BrtOrderExamine> orderExamineList = this.baseMapper.selectBatchIds(examineIdList);
        Set<String> orderTemplateIdSet = orderExamineList.stream().map(BrtOrderExamine::getOrderTemplateId).collect(Collectors.toSet());

        orderTemplateMapper.update(null,new LambdaUpdateWrapper<BrtOrderTemplate>().set(BrtOrderTemplate::getAuditStatus,auditStatus).set(BrtOrderTemplate::getStatus,"2".equals(auditStatus)?"1":"0").in(BrtOrderTemplate::getOrderTemplateId,orderTemplateIdSet));

        // 查询模板信息
        List<BrtOrderTemplate> orderTemplates = orderTemplateMapper.selectList(new LambdaQueryWrapper<BrtOrderTemplate>().in(BrtOrderTemplate::getOrderTemplateId, orderTemplateIdSet).ne(BrtOrderTemplate::getOrderTemplateStatus, OrderTemplateStatusEnums.作废.getCode()));
        // 获取模板ID
        List<String> templateIdList = orderTemplates.stream().map(BrtOrderTemplate::getTemplateId).collect(Collectors.toList());
        // 查询审批节点
        List<BrtFlowNode> auditFlowNodeList = flowNodeMapper.selectList(new LambdaQueryWrapper<BrtFlowNode>().in(BrtFlowNode::getTemplateId, templateIdList).eq(BrtFlowNode::getNodeType, NodeTypeEnums.审批.getCode()));
        List<String> auditNodeIdList = auditFlowNodeList.stream().map(BrtFlowNode::getNodeId).collect(Collectors.toList());
        // 查询订单审批节点
        List<BrtOrderNode> orderNodeList = orderNodeService.list(new LambdaQueryWrapper<BrtOrderNode>().in(BrtOrderNode::getOrderTemplateId, orderTemplateIdSet).in(BrtOrderNode::getNodeId, auditNodeIdList));
//        List<String> orderNodeIdList = orderNodeList.stream().map(BrtOrderNode::getOrderNodeId).collect(Collectors.toList());
//        orderNodeMapper.update(null,new LambdaUpdateWrapper<BrtOrderNode>().set(BrtOrderNode::getNodeStatus,auditStatus.equals(AuditStatusEnums.通过.getCode())?NodeStatusEnums.已完成.getCode():NodeStatusEnums.已超时.getCode()).set(BrtOrderNode::getComplateDate,new Date()).in(BrtOrderNode::getOrderNodeId,orderNodeIdList));

        //保存动态信息
        orderNodeList.forEach(orderNode -> {
            orderNodeService.completeNode(orderNode.getOrderId(),orderNode.getOrderNodeId(),NodeTypeEnums.审批);

            //判断是否审核通过，如果是则所有节点改为未开始
            if(auditStatus.equals(AuditStatusEnums.通过.getCode())){
                orderNodeService.update(new LambdaUpdateWrapper<BrtOrderNode>().set(BrtOrderNode::getNodeStatus,NodeStatusEnums.未开始.getCode()).ne(BrtOrderNode::getOrderNodeId,orderNode.getOrderNodeId()).eq(BrtOrderNode::getOrderId,orderNode.getOrderId()).eq(BrtOrderNode::getOrderTemplateId,orderNode.getOrderTemplateId()).eq(BrtOrderNode::getNodeStatus,"-1"));
            }

            Optional<BrtOrderTemplate> orderTemplateOptional = orderTemplates.stream().filter(orderTemplate -> orderTemplate.getOrderTemplateId().equals(orderNode.getOrderTemplateId())).findFirst();
            if (orderTemplateOptional.isPresent()){
                // 查询模板信息
                BrtFlowTemplate flowTemplate = flowTemplateMapper.selectById(orderTemplateOptional.get().getTemplateId());

                if (flowTemplate.getIsSeqExecute()!=null && !flowTemplate.getIsSeqExecute()){

                    // 不按照顺序执行 审核通过后 需要生成收货单和送货单
                    List<BrtFlowNode> nodeList = flowNodeMapper.selectList(new LambdaQueryWrapper<BrtFlowNode>().eq(BrtFlowNode::getTemplateId, flowTemplate.getTemplateId()).eq(BrtFlowNode::getNodeStatus,YesOrNoEnums.YES.getCode()).orderByAsc(BrtFlowNode::getSort));

                    // 筛选模板是否包含生成送货单操作
                    Optional<BrtFlowNode> customerDeliveryOptional = nodeList.stream().filter(node -> NodeOtherSettingEnums.isExistence(node.getOtherSetting(), NodeOtherSettingEnums.生成送货单)).findFirst();
                    if (customerDeliveryOptional.isPresent()){
                        BrtFlowNode customerDeliveryFlowNode = customerDeliveryOptional.get();
                        BrtOrderNode customerDeliveryOrderNode = orderNodeService.getOne(new LambdaQueryWrapper<BrtOrderNode>().eq(BrtOrderNode::getOrderTemplateId, orderNode.getOrderTemplateId()).eq(BrtOrderNode::getNodeId, customerDeliveryFlowNode.getNodeId()).orderByAsc(BrtOrderNode::getSort).last(" limit 1"));
                        customerDeliveryService.createCustomerDelivery(customerDeliveryOrderNode);
                    }

                    // 筛选模板是否包含生成收货单操作
                    Optional<BrtFlowNode> receivingOptional = nodeList.stream().filter(node -> NodeOtherSettingEnums.isExistence(node.getOtherSetting(), NodeOtherSettingEnums.生成收货单)).findFirst();
                    if (receivingOptional.isPresent()){
                        BrtFlowNode receivingFlowNode = receivingOptional.get();
                        BrtOrderNode receivingOrderNode = orderNodeService.getOne(new LambdaQueryWrapper<BrtOrderNode>().eq(BrtOrderNode::getOrderTemplateId, orderNode.getOrderTemplateId()).eq(BrtOrderNode::getNodeId, receivingFlowNode.getNodeId()).orderByAsc(BrtOrderNode::getSort).last(" limit 1"));
                        supplierReceivingService.createSupplierReceiving(receivingOrderNode);
                    }
                }

                //是否顺序执行
                Boolean isSeqExecute = flowTemplate.getIsSeqExecute();

                if(auditStatus.equals(AuditStatusEnums.通过.getCode())){
                    if(!isSeqExecute){
                        orderNodeService.update(new LambdaUpdateWrapper<BrtOrderNode>().set(BrtOrderNode::getNodeStatus,NodeStatusEnums.进行中.getCode()).ne(BrtOrderNode::getOrderNodeId,orderNode.getOrderNodeId()).eq(BrtOrderNode::getOrderId,orderNode.getOrderId()).eq(BrtOrderNode::getOrderTemplateId,orderNode.getOrderTemplateId()).eq(BrtOrderNode::getNodeStatus,"0"));
                    }
                }



            }
        });


        // 修改审批信息
        BrtOrderExamine orderExamine = new BrtOrderExamine();
        orderExamine.setAuditStatus(auditStatus);
        orderExamine.setAuditRemark(auditRemark);
        return this.baseMapper.update(orderExamine,new LambdaQueryWrapper<BrtOrderExamine>().in(BrtOrderExamine::getExamineId,examineIdList));


    }

    public void updateOrderStatus(){

    }

    @Override
    public List<Map<String, String>> orderAllList() {
        return this.baseMapper.orderAllList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int nodeAudit(BrtOrderNode orderNode,String childId , String auditStatus) {
        if(auditStatus.equals(AuditStatusEnums.拒绝.getCode())||auditStatus.equals("1")){
            // 保存动态记录
            BrtOrderDynamicVo orderDynamicVo = new BrtOrderDynamicVo();
            orderDynamicVo.setOrderId(orderNode.getOrderId());
            orderDynamicVo.setOrderNodeId(orderNode.getOrderNodeId());
            orderDynamicVo.setNodeId(orderNode.getNodeId());
            orderDynamicVo.setUserId(SecurityUtils.getUserId().toString());
            orderDynamicVo.setDynamicContent(auditStatus.equals("1")?"重新提交":"审批拒绝");
            orderDynamicService.save(orderDynamicVo);
            orderNodeService.onAudit(orderNode.getOrderId(),auditStatus);
            return 1;
        }
        List<BrtOrderExamineVo> orderExamineList = null;
        if(StringUtils.isNotEmpty(childId)){
            orderExamineList = this.baseMapper.auditQuery(orderNode.getOrderId(),childId);
        }else {
            orderExamineList = this.baseMapper.auditQuery(orderNode.getOrderId(),null);
        }

        String examineIds = orderExamineList.stream().map(BrtOrderExamine::getExamineId).collect(Collectors.joining(","));
        return audit(examineIds,auditStatus,childId,orderNode.getNodeRemark());
    }

    /**
     * @description: TODO 获取订单描述
     * @author: FanGN
     * @date: 00:55 2024/5/11
     * @param:
     * @param salesOrderVo
     * @return:
     * @return java.lang.String
     **/
    public String getOrderDetailsDesc(BrtOrderVo orderVo){
        if(orderVo.getOrderType().equals(OrderTypeEnums.销售单.getCode())){
            // 获取订单详情
            List<BrtSalesOrderDetailsVo> orderDetailsVoList = orderVo.getSalesOrderVo().getSalesOrderDetailsVoList();

            StringBuffer detailsDesc = new StringBuffer();
            orderDetailsVoList.stream().forEach(item -> {
                // 查询物料信息
                BrtMateriel materiel = materielService.getById(item.getMaterielId());
                if (ObjectUtil.isNotEmpty(materiel)){
                    detailsDesc.append(materiel.getMaterielName());
                }else {
                    detailsDesc.append(item.getMaterielId());
                }
                detailsDesc.append("x"+item.getDetailsNum()+",");
            });
            return detailsDesc.toString();
        }else if(orderVo.getOrderType().equals(OrderTypeEnums.采购单.getCode())){
            // 获取订单详情
            List<BrtMarketOrderDetailsVo> marketOrderDetailsVoList = orderVo.getMarketOrderVo().getMarketOrderDetailsVoList();

            StringBuffer detailsDesc = new StringBuffer();
            marketOrderDetailsVoList.stream().forEach(item -> {
                // 查询物料信息
                BrtMateriel materiel = materielService.getById(item.getMaterielId());
                if (ObjectUtil.isNotEmpty(materiel)){
                    detailsDesc.append(materiel.getMaterielName());
                }else {
                    detailsDesc.append(item.getMaterielId());
                }
                detailsDesc.append("x"+item.getDetailsNum()+",");
            });
            return detailsDesc.toString();
        }else if(orderVo.getOrderType().equals(OrderTypeEnums.报价单.getCode())){
            // 获取订单详情
            List<BrtPriceSheetOrderDetailsVo> priceSheetOrderDetailsVoList = orderVo.getPriceSheetOrderVo().getPriceSheetOrderDetailsVoList();

            StringBuffer detailsDesc = new StringBuffer();
            priceSheetOrderDetailsVoList.stream().forEach(item -> {
                // 查询物料信息
                BrtMateriel materiel = materielService.getById(item.getMaterielId());
                if (ObjectUtil.isNotEmpty(materiel)){
                    detailsDesc.append(materiel.getMaterielName());
                }else {
                    detailsDesc.append(item.getMaterielId());
                }
                detailsDesc.append("x"+item.getDetailsNum()+",");
            });
            return detailsDesc.toString();
        }
        return null;
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param brtOrderExamineVo
     * @return:
     **/
    public void saveBefore(BrtOrderExamineVo brtOrderExamineVo){

    }

    /**
     * 在某日期加上一段时间
     * @param oldDate 原日期
     * @param day 加多少天
     * @param hour 加多少小时
     * @param minute 加多少分钟
     * @return
     */
    private Date addDate(Date oldDate,String day,String hour,String minute,int type){
        SimpleDateFormat dateTimeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
        if(type==0){
            day = "-"+day;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(oldDate);
        calendar.add(Calendar.DATE,Integer.valueOf(day));
        Date time = calendar.getTime();
        String newDate = dateSdf.format(time);
        newDate = newDate+" "+hour+":"+minute+":00";
        try {
            Date parse = dateTimeSdf.parse(newDate);
            return parse;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


}
