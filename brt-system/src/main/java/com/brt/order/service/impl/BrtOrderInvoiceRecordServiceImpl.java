package com.brt.order.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.enums.AuditStatusEnums;
import com.brt.common.enums.NodeTypeEnums;
import com.brt.common.exception.ServiceException;
import com.brt.common.utils.PageUtils;
import com.brt.common.utils.SecurityUtils;
import com.brt.order.domain.BrtOrderDynamic;
import com.brt.order.domain.BrtOrderInvoicePlan;
import com.brt.order.domain.BrtOrderInvoiceRecord;
import com.brt.order.service.IBrtOrderDynamicService;
import com.brt.order.service.IBrtOrderInvoicePlanService;
import com.brt.order.service.IBrtOrderNodeService;
import com.brt.order.vo.BrtOrderInvoiceRecordVo;
import com.brt.order.mapper.BrtOrderInvoiceRecordMapper;
import com.brt.order.service.IBrtOrderInvoiceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * 开票记录Service业务层处理
 * 
 * @author Fgn
 * @date 2024-06-19
 */
@Service
public class BrtOrderInvoiceRecordServiceImpl extends ServiceImpl<BrtOrderInvoiceRecordMapper, BrtOrderInvoiceRecord> implements IBrtOrderInvoiceRecordService {

    @Autowired
    private IBrtOrderInvoicePlanService orderInvoicePlanService;

    @Autowired
    private IBrtOrderDynamicService orderDynamicService;

    @Autowired
    private IBrtOrderNodeService orderNodeService;

    @Override
    public TableDataInfo<BrtOrderInvoiceRecordVo> queryBrtOrderInvoiceRecordList(BrtOrderInvoiceRecordVo brtOrderInvoiceRecordVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtOrderInvoiceRecordList(PageUtils.buildPage(), brtOrderInvoiceRecordVo));
    }

    @Override
    public List<BrtOrderInvoiceRecordVo> queryBrtOrderInvoiceRecordAll(BrtOrderInvoiceRecordVo brtOrderInvoiceRecordVo) {
        return this.baseMapper.queryBrtOrderInvoiceRecordList(brtOrderInvoiceRecordVo);
    }

    @Override
    public BrtOrderInvoiceRecordVo queryBrtOrderInvoiceRecordByRecordId(String recordId) {
        return this.baseMapper.queryBrtOrderInvoiceRecordByRecordId(recordId);
    }

    @Transactional
    @Override
    public BrtOrderInvoiceRecordVo insertBrtOrderInvoiceRecord(BrtOrderInvoiceRecordVo brtOrderInvoiceRecordVo) {
        saveBefore(brtOrderInvoiceRecordVo);

        // 判断节点是否允许提前执行
        orderNodeService.isAheadComplate(brtOrderInvoiceRecordVo.getOrderNodeId());

        BrtOrderInvoicePlan orderInvoicePlan = orderInvoicePlanService.getOne(new LambdaQueryWrapper<BrtOrderInvoicePlan>().eq(BrtOrderInvoicePlan::getOrderId, brtOrderInvoiceRecordVo.getOrderId()).eq(BrtOrderInvoicePlan::getOrderNodeId,brtOrderInvoiceRecordVo.getOrderNodeId()).last(" limit 1"));
        orderInvoicePlan.setInvoiceAmount(orderInvoicePlan.getInvoiceAmount().add(brtOrderInvoiceRecordVo.getInvoiceAmount()));
        orderInvoicePlanService.updateById(orderInvoicePlan);

        // 计算剩余开票金额
        brtOrderInvoiceRecordVo.setSurplusAmount(orderInvoicePlan.getPlanAmount().subtract(orderInvoicePlan.getInvoiceAmount()));

        if (brtOrderInvoiceRecordVo.getSurplusAmount().compareTo(BigDecimal.ZERO) < 0){
            throw new ServiceException("开票金额不允许大于订单金额");
        }

        int i = this.baseMapper.insert(brtOrderInvoiceRecordVo);

        // 保存动态信息
        BrtOrderDynamic orderDynamic = new BrtOrderDynamic();
        orderDynamic.setOrderId(brtOrderInvoiceRecordVo.getOrderId());
        orderDynamic.setOrderNodeId(brtOrderInvoiceRecordVo.getOrderNodeId());
        orderDynamic.setNodeId(brtOrderInvoiceRecordVo.getNodeId());
        orderDynamic.setUserId(SecurityUtils.getUserId().toString());
        orderDynamic.setDynamicContent("开票"+brtOrderInvoiceRecordVo.getInvoiceAmount());
        orderDynamicService.save(orderDynamic);

        // 如果剩余开票金额等于0 表示节点已完成
        if (brtOrderInvoiceRecordVo.getSurplusAmount().compareTo(BigDecimal.ZERO) == 0){
            orderNodeService.completeNode(brtOrderInvoiceRecordVo.getOrderId(),brtOrderInvoiceRecordVo.getOrderNodeId(), NodeTypeEnums.开票金额纪录任务);
        }

        return brtOrderInvoiceRecordVo;
    }

    @Transactional
    @Override
    public BrtOrderInvoiceRecordVo updateBrtOrderInvoiceRecord(BrtOrderInvoiceRecordVo brtOrderInvoiceRecordVo) {
        saveBefore(brtOrderInvoiceRecordVo);
        int i = this.baseMapper.updateById(brtOrderInvoiceRecordVo);
        return brtOrderInvoiceRecordVo;
    }

    @Transactional
    @Override
    public int deleteBrtOrderInvoiceRecordByRecordIds(String[] recordIds) {
        // 查询要删除的开票记录
        List<BrtOrderInvoiceRecord> orderInvoiceRecordList = this.listByIds(Arrays.asList(recordIds));
        if(ObjectUtil.isNotEmpty(orderInvoiceRecordList)){
            // 查询开票计划
            BrtOrderInvoiceRecord orderInvoiceRecord = orderInvoiceRecordList.get(0);
            BrtOrderInvoicePlan orderInvoicePlan = orderInvoicePlanService.getOne(new LambdaQueryWrapper<BrtOrderInvoicePlan>().eq(BrtOrderInvoicePlan::getOrderId, orderInvoiceRecord.getOrderId()).eq(BrtOrderInvoicePlan::getOrderNodeId,orderInvoiceRecord.getOrderNodeId()).last(" limit 1"));

            BigDecimal invoiceAmount = orderInvoiceRecordList.stream().map(BrtOrderInvoiceRecord::getInvoiceAmount).reduce(BigDecimal::add).get();

            orderInvoicePlan.setInvoiceAmount(orderInvoicePlan.getInvoiceAmount().subtract(invoiceAmount));
            orderInvoicePlanService.updateById(orderInvoicePlan);
        }
        return this.baseMapper.deleteBatchIds(Arrays.asList(recordIds));
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @param brtOrderInvoiceRecordVo
     * @return:
     **/
    public void saveBefore(BrtOrderInvoiceRecordVo brtOrderInvoiceRecordVo){

    }

}
