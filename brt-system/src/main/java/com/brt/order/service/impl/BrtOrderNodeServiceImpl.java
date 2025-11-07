package com.brt.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.enums.*;
import com.brt.common.exception.ServiceException;
import com.brt.common.utils.DateUtils;
import com.brt.common.utils.PageUtils;
import com.brt.common.utils.SecurityUtils;
import com.brt.common.utils.StringUtils;
import com.brt.order.domain.*;
import com.brt.order.service.*;
import com.brt.order.vo.*;
import com.brt.order.mapper.BrtOrderNodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 订单流程节点Service业务层处理
 *
 * @author Fgn
 * @date 2024-05-10
 */
@Service
public class BrtOrderNodeServiceImpl extends ServiceImpl<BrtOrderNodeMapper, BrtOrderNode> implements IBrtOrderNodeService {

    @Autowired
    private IBrtOrderDynamicService orderDynamicService;

    @Autowired
    private IBrtOrderTemplateService orderTemplateService;

    @Autowired
    private IBrtOrderChildProcessService orderChildProcessService;

    @Autowired
    private IBrtFlowTemplateService flowTemplateService;

    @Autowired
    private IBrtFlowNodeService flowNodeService;

    @Autowired
    private IBrtOrderBoomService orderBoomService;

    @Autowired
    private IBrtMaterielService materielService;

    @Autowired
    private IBrtCustomerDeliveryService customerDeliveryService;

    @Autowired
    private IBrtPriceSheetOrderService priceSheetOrderService;

    @Autowired
    private IBrtSalesOrderService salesOrderService;
    @Autowired
    private IBrtMarketOrderService marketOrderService;

    @Autowired
    private IBrtSupplierService supplierService;

    @Autowired
    private IBrtSupplierReceivingService supplierReceivingService;

    @Autowired
    private IBrtInInventoryService inInventoryService;

    @Autowired
    private IBrtOutInventoryService outInventoryService;


    @Override
    public TableDataInfo<BrtOrderNodeVo> queryBrtOrderNodeList(BrtOrderNodeVo brtOrderNodeVo) {
        // 判断是否有查看全部列表权限
        if (SecurityUtils.hasPermi(SecurityUtils.getLoginUser().getPermissions(),BrtOrderNodeVo.ALL_LIST)){
            brtOrderNodeVo.setUserId(SecurityUtils.getUserId().toString());
        }
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtOrderNodeList(PageUtils.buildPage(), brtOrderNodeVo));
    }

    public TableDataInfo<BrtOrderNodeVo> myTask(BrtOrderNodeVo brtOrderNodeVo) {

        String userId = SecurityUtils.getUserId().toString();
        // 判断是否有查看全部列表权限
        if (!"1".equals(userId)){
            brtOrderNodeVo.setUserId(SecurityUtils.getUserId().toString());
        }
        return PageUtils.buildDataInfo(this.baseMapper.myTask(PageUtils.buildPage(), brtOrderNodeVo));
    }

    @Override
    public List<BrtOrderNodeVo> queryBrtOrderNodeAll(BrtOrderNodeVo brtOrderNodeVo) {
        return this.baseMapper.queryBrtOrderNodeList(brtOrderNodeVo);
    }

    @Override
    public BrtOrderNodeVo queryBrtOrderNodeByOrderNodeId(String orderNodeId) {
        return this.baseMapper.queryBrtOrderNodeByOrderNodeId(orderNodeId);
    }

    @Transactional
    @Override
    public BrtOrderNodeVo insertBrtOrderNode(BrtOrderNodeVo brtOrderNodeVo) {
        saveBefore(brtOrderNodeVo);
        int i = this.baseMapper.insert(brtOrderNodeVo);
        return brtOrderNodeVo;
    }

    @Transactional
    @Override
    public BrtOrderNodeVo updateBrtOrderNode(BrtOrderNodeVo brtOrderNodeVo) {
        saveBefore(brtOrderNodeVo);
        int i = this.baseMapper.updateById(brtOrderNodeVo);
        return brtOrderNodeVo;
    }

    @Transactional
    @Override
    public int deleteBrtOrderNodeByOrderNodeIds(String[] orderNodeIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(orderNodeIds));
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public int completeNode(String orderId, String orderNodeId, NodeTypeEnums nodeTypeEnums) {
        // 查询订单节点信息
        BrtOrderNode orderNode = this.baseMapper.selectById(orderNodeId);

        // 查询订单模板
        BrtOrderTemplate orderTemplate = orderTemplateService.getById(orderNode.getOrderTemplateId());

        if(StringUtils.isNotEmpty(orderTemplate.getOrderTemplateStatus())){
            if("1".equals(orderTemplate.getOrderTemplateStatus())){
                throw new ServiceException("流程已停用");
            }
            if("2".equals(orderTemplate.getOrderTemplateStatus())){
                throw new ServiceException("流程已作废");
            }
        }

        if (1!=SecurityUtils.getUserId()&&!orderNode.getPrincipal().equals(SecurityUtils.getUserId().toString()) && !orderNode.getNodeStatus().equals(NodeStatusEnums.进行中.getCode())){
            throw new ServiceException("当前节点未开始或已完成");
        }
        orderNode.setNodeStatus(NodeStatusEnums.已完成.getCode());

        // 保存动态记录
        BrtOrderDynamicVo orderDynamicVo = new BrtOrderDynamicVo();
        orderDynamicVo.setOrderId(orderNode.getOrderId());
        orderDynamicVo.setOrderNodeId(orderNode.getOrderNodeId());
        orderDynamicVo.setNodeId(orderNode.getNodeId());
        orderDynamicVo.setUserId(SecurityUtils.getUserId().toString());
        orderDynamicVo.setDynamicContent(nodeTypeEnums.getType()+"节点完成");
//        orderDynamicVo.setr
        orderDynamicService.save(orderDynamicVo);

        // 查询模板信息
        BrtFlowTemplate flowTemplate = flowTemplateService.getById(orderNode.getTemplateId());


        // 判断是否超时完成
        if (!nodeTypeEnums.getCode().equals(NodeTypeEnums.审批.getCode()) && orderNode.getComplateDate() !=null && orderNode.getComplateDate().compareTo(new Date()) < 0) {
            orderNode.setNodeStatus(NodeStatusEnums.已完成.getCode());
            orderDynamicVo.setDynamicContent(nodeTypeEnums.getType()+"超时完成");
            orderNode.setTimeout("1");

            // 判断是否自动延期 并且是顺序执行
            if ((flowTemplate.getIsAutoPostpone()!=null && flowTemplate.getIsAutoPostpone().equals(YesOrNoEnums.YES.getCode())) &&
                (flowTemplate.getIsSeqExecute()!=null && flowTemplate.getIsSeqExecute())){

                // 计算延期时间
                long postponeDayNum = DateUtil.betweenDay(orderNode.getComplateDate(), new Date(), false);
                this.baseMapper.update(null,new LambdaUpdateWrapper<BrtOrderNode>().setSql("complate_date = DATE_ADD(complate_date, INTERVAL "+postponeDayNum+" DAY)").gt(BrtOrderNode::getSort,orderNode.getSort()).eq(BrtOrderNode::getOrderTemplateId,orderNode.getOrderTemplateId()));
            }
        }

        // 查询节点信息
        BrtFlowNode flowNode = flowNodeService.getById(orderNode.getNodeId());

        // 判断该节点是否存在减库存操作
        if (NodeOtherSettingEnums.isExistence(flowNode.getOtherSetting(),NodeOtherSettingEnums.减库存) && orderTemplate.getOrderType().equals(OrderTypeEnums.销售单.getCode())){
            // 查询订单的节点 有没有操作过扣库存操作
            Integer count = this.baseMapper.selectCount(new LambdaQueryWrapper<BrtOrderNode>().apply("find_in_set({0},oper_setting)",NodeOtherSettingEnums.减库存.getCode()).eq(BrtOrderNode::getOrderId, orderNode.getOrderId()));
            if (count <= 0){
                List<BrtOrderBoom> orderBoomList = orderBoomService.list(new LambdaQueryWrapper<BrtOrderBoom>().eq(BrtOrderBoom::getOrderId, orderNode.getOrderId()));

                // 生成出库单
                outInventoryService.createOutInventory(orderId,orderBoomList);
                //减库存
                materielService.deductionSock(BeanUtil.copyToList(orderBoomList, BrtOrderBoomVo.class));
                orderNode.setOperSetting(orderNode.getOperSetting()+","+NodeOtherSettingEnums.减库存.getCode());

            }
        } else if (NodeOtherSettingEnums.isExistence(flowNode.getOtherSetting(),NodeOtherSettingEnums.加库存) && orderTemplate.getOrderType().equals(OrderTypeEnums.采购单.getCode())) {
            // 查询订单的节点 有没有操作过加库存操作
            Integer count = this.baseMapper.selectCount(new LambdaQueryWrapper<BrtOrderNode>().apply("find_in_set({0},oper_setting)",NodeOtherSettingEnums.加库存.getCode()).eq(BrtOrderNode::getOrderId, orderNode.getOrderId()));
            if (count <= 0){
                // 生成入库单
                inInventoryService.createInInventory(orderId);
                //加库存
                orderNode.setOperSetting(orderNode.getOperSetting()+","+NodeOtherSettingEnums.加库存.getCode());
            }
        }

        int i = this.baseMapper.updateById(orderNode);

        // 判断是否需要按照顺序执行
        if (flowTemplate.getIsSeqExecute()!=null && flowTemplate.getIsSeqExecute()){

            //---判断是否是报价单
            if(OrderTypeEnums.报价单.getCode().equals(orderTemplate.getOrderType())){
                priceSheetOrderService.updatePriceSheet(orderId,YesOrNoEnums.NO.getCode());
            }


            // 查询当前节点的下一个节点
            BrtOrderNode nextOrderNode = this.baseMapper.selectOne(new LambdaQueryWrapper<BrtOrderNode>()
                    .eq(BrtOrderNode::getOrderId, orderNode.getOrderId())
                    .eq(BrtOrderNode::getOrderTemplateId, orderNode.getOrderTemplateId())
                    .gt(BrtOrderNode::getSort, orderNode.getSort()).last(" limit 1"));

            if (ObjectUtil.isNotEmpty(nextOrderNode)){
                nextOrderNode.setNodeStatus(NodeStatusEnums.进行中.getCode());

                BrtFlowNode nextFlowNode = flowNodeService.getById(nextOrderNode.getNodeId());
                if (NodeOtherSettingEnums.isExistence(nextFlowNode.getOtherSetting(),NodeOtherSettingEnums.生成收货单) && orderTemplate.getOrderType().equals(OrderTypeEnums.采购单.getCode())){
                    //---生成收货单
                    supplierReceivingService.createSupplierReceiving(nextOrderNode);
                } else if (NodeOtherSettingEnums.isExistence(nextFlowNode.getOtherSetting(),NodeOtherSettingEnums.生成送货单) && orderTemplate.getOrderType().equals(OrderTypeEnums.销售单.getCode())) {
                    customerDeliveryService.createCustomerDelivery(nextOrderNode);
                }else {

                }
                this.baseMapper.updateById(nextOrderNode);
            }
        }

        if (StringUtils.isNotBlank(orderTemplate.getChildId())){
            // 查询子流程信息
            BrtOrderChildProcess childProcess = orderChildProcessService.getById(orderTemplate.getChildId());
            childProcess.setComplateNum(childProcess.getComplateNum() + 1);

            // 判断父节点是否所有节点都已完成
            if (childProcess.getComplateNum() == childProcess.getNodeNum()){
                childProcess.setChildStatus(ChildStatusEnums.已完成.getCode());
                orderChildProcessService.updateById(childProcess);

                complateParentNode(childProcess);
            }else {
                orderChildProcessService.updateById(childProcess);
            }
        }


        /**
         * 判断此流程模板是否所有的节点都已完成
         */
        if(this.isComplete(orderId,orderTemplate.getChildId(),orderTemplate.getOrderTemplateId())){
            if(StringUtils.isNotEmpty(orderTemplate.getChildId())){
                if(OrderTypeEnums.销售单.getCode().equals(orderTemplate.getOrderType())){
                    BrtSalesOrderVo brtSalesOrderVo = salesOrderService.queryBrtSalesOrderByOrderId(orderId);
                    brtSalesOrderVo.setStatus(OrderAuditStatus.已完成.getCode());
                    brtSalesOrderVo.setFinishDate(new Date());
                    salesOrderService.updateById(brtSalesOrderVo);

                }else if(OrderTypeEnums.采购单.getCode().equals(orderTemplate.getOrderType())){
                    BrtMarketOrderVo brtMarketOrderVo = marketOrderService.queryBrtMarketOrderByOrderId(orderId);
                    brtMarketOrderVo.setStatus(OrderAuditStatus.已完成.getCode());
                    brtMarketOrderVo.setFinishDate(new Date());
                    marketOrderService.updateById(brtMarketOrderVo);

                }else if(OrderTypeEnums.报价单.getCode().equals(orderTemplate.getOrderType())){
                    BrtPriceSheetOrderVo brtPriceSheetOrderVo = priceSheetOrderService.queryBrtPriceSheetOrderByOrderId(orderId);
                    brtPriceSheetOrderVo.setStatus(OrderAuditStatus.已完成.getCode());
                    brtPriceSheetOrderVo.setFinishDate(new Date());
                    priceSheetOrderService.updateById(brtPriceSheetOrderVo);
                }
            }
            orderTemplate.setStatus("2");
            orderTemplateService.updateById(orderTemplate);
        }

        /**
         * 如果当前审批节点是审批节点，则把状态改为审核中
         */
        if(NodeTypeEnums.审批.getCode().equals(nodeTypeEnums.getCode())){
            if(OrderTypeEnums.销售单.getCode().equals(orderTemplate.getOrderType())){
                BrtSalesOrderVo brtSalesOrderVo = salesOrderService.queryBrtSalesOrderByOrderId(orderId);
                brtSalesOrderVo.setStatus(OrderAuditStatus.审核中.getCode());
                brtSalesOrderVo.setOrderNo(null);
                salesOrderService.updateById(brtSalesOrderVo);

            }else if(OrderTypeEnums.采购单.getCode().equals(orderTemplate.getOrderType())){
                BrtMarketOrderVo brtMarketOrderVo = marketOrderService.queryBrtMarketOrderByOrderId(orderId);
                brtMarketOrderVo.setStatus(OrderAuditStatus.审核中.getCode());
                marketOrderService.updateById(brtMarketOrderVo);

            }else if(OrderTypeEnums.报价单.getCode().equals(orderTemplate.getOrderType())){
                BrtPriceSheetOrderVo brtPriceSheetOrderVo = priceSheetOrderService.queryBrtPriceSheetOrderByOrderId(orderId);
                brtPriceSheetOrderVo.setStatus(OrderAuditStatus.审核中.getCode());
                priceSheetOrderService.updateById(brtPriceSheetOrderVo);
            }
        }

        return i;
    }

    @Override
    public int isAheadComplate(String orderNodeId) {
        // 订单节点信息
        BrtOrderNode orderNode = this.baseMapper.selectById(orderNodeId);


        // 判断只有管理员和节点负责人可以操作
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId()) && !orderNode.getUserId().equals(SecurityUtils.getUserId().toString())&& !orderNode.getPrincipal().equals(SecurityUtils.getUserId().toString())){
            throw new ServiceException("暂无权限操作");
        }

        // 查询模板信息
        BrtFlowTemplate flowTemplate = flowTemplateService.getById(orderNode.getTemplateId());

        // 判断是否需要按顺序执行
        if (flowTemplate.getIsSeqExecute() != null && flowTemplate.getIsSeqExecute()){
            // 查询之前未完成的节点数量
            Integer notComplateNum = this.baseMapper.selectCount(new LambdaQueryWrapper<BrtOrderNode>()
                    .eq(BrtOrderNode::getOrderId, orderNode.getOrderId())
                    .eq(BrtOrderNode::getOrderTemplateId,orderNode.getOrderTemplateId())
                    .eq(BrtOrderNode::getTemplateId, orderNode.getTemplateId())
                    .and(wrapper -> wrapper.eq(BrtOrderNode::getNodeStatus,NodeStatusEnums.未开始.getCode()).or().eq(BrtOrderNode::getNodeStatus,NodeStatusEnums.进行中.getCode()))
                    .lt(BrtOrderNode::getSort, orderNode.getSort()));

            if (notComplateNum > 0){
                throw new ServiceException("之前有未完成的节点");
            }
        }
        return 0;
    }

    /**
     * @description: TODO 完成子流程的上级节点
     * @author: FanGN
     * @date: 13:39 2024/6/24
     * @param:
     * @return:
     **/
    @Transactional(rollbackFor = Exception.class)
    public void complateParentNode(BrtOrderChildProcess childProcess){
        // 查询是否有 没完成的节点
        int notComplateNum = orderChildProcessService.count(new LambdaQueryWrapper<BrtOrderChildProcess>().ne(BrtOrderChildProcess::getChildStatus, ChildStatusEnums.已完成.getCode()).eq(BrtOrderChildProcess::getOrderId, childProcess.getOrderId()).eq(BrtOrderChildProcess::getOrderNodeId, childProcess.getOrderNodeId()));

        if (notComplateNum <=0 ){

            if(childProcess.getParentChildId() != null){
                orderChildProcessService.update(new LambdaUpdateWrapper<BrtOrderChildProcess>().set(BrtOrderChildProcess::getChildStatus,ChildStatusEnums.已完成.getCode()).eq(BrtOrderChildProcess::getChildId,childProcess.getParentChildId()));
            }

            // 完成订单对应节点信息
            completeNode(childProcess.getOrderId(),childProcess.getOrderNodeId(),NodeTypeEnums.子流程记录任务);
        }
    }

    /**
     * 查询此流程的所有节点是否已完成
     * @param orderId
     * @return
     */
    public boolean isComplete(String orderId,String childId,String orderTemplateId){
        String complete = this.baseMapper.isComplete(orderId,childId,orderTemplateId);
        if(ObjectUtil.isEmpty(complete)||"0".equals(complete)){
            return true;
        }
        return false;
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param brtOrderNodeVo
     * @return:
     **/
    public void saveBefore(BrtOrderNodeVo brtOrderNodeVo){

    }

    /**
     * 修改订单是否延期为已延期
     * @param orderType
     * @param orderId
     */
    public void overtime(String orderType,String orderId,YesOrNoEnums yesOrNoEnums){
        if(orderType.equals(OrderTypeEnums.销售单.getCode())){
            BrtSalesOrder salesOrder = salesOrderService.getById(orderId);
            salesOrder.setPostpone(yesOrNoEnums.getCode());
            salesOrderService.updateById(salesOrder);
        }
    }

    /**
     * 提交备注
     * @param orderId
     */
    @Transactional
    public void submitRemark(String orderId,String orderNodeId,String remark){
        this.update(new LambdaUpdateWrapper<BrtOrderNode>().set(BrtOrderNode::getNodeRemark,remark).eq(BrtOrderNode::getOrderId,orderId).eq(BrtOrderNode::getOrderNodeId,orderNodeId));
        orderDynamicService.update(new LambdaUpdateWrapper<BrtOrderDynamic>().set(BrtOrderDynamic::getRemark,remark).eq(BrtOrderDynamic::getOrderId,orderId).eq(BrtOrderDynamic::getOrderNodeId,orderNodeId));
    }

    /**
     * 审批拒绝
     * @param orderId
     * @param auditStatus
     */
    public void onAudit(String orderId,String auditStatus){
        String orderNodeId = baseMapper.onAudit(orderId);
        this.update(null,new LambdaUpdateWrapper<BrtOrderNode>().set(BrtOrderNode::getNodeStatus,auditStatus).eq(BrtOrderNode::getOrderNodeId,orderNodeId));
    }

}
