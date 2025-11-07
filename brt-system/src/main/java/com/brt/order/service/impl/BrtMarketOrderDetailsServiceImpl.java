package com.brt.order.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.enums.CraftTypeEnums;
import com.brt.common.exception.ServiceException;
import com.brt.common.utils.PageUtils;
import com.brt.common.utils.StringUtils;
import com.brt.order.domain.BrtMateriel;
import com.brt.order.domain.BrtMarketOrder;
import com.brt.order.domain.BrtMarketOrderDetails;
import com.brt.order.mapper.BrtMarketOrderMapper;
import com.brt.order.service.IBrtMaterielService;
import com.brt.order.service.IBrtMaterielTypeService;
import com.brt.order.vo.BrtMaterielTypeVo;
import com.brt.order.vo.BrtMarketOrderDetailsVo;
import com.brt.order.mapper.BrtMarketOrderDetailsMapper;
import com.brt.order.service.IBrtMarketOrderDetailsService;
import com.brt.order.vo.BrtSalesOrderDetailsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 销售单详情Service业务层处理
 *
 * @author Fgn
 * @date 2024-05-09
 */
@Service
public class BrtMarketOrderDetailsServiceImpl extends ServiceImpl<BrtMarketOrderDetailsMapper, BrtMarketOrderDetails> implements IBrtMarketOrderDetailsService {

    @Autowired
    private BrtMarketOrderMapper marketOrderMapper;

    @Autowired
    private IBrtMaterielService materielService;

    @Autowired
    private IBrtMaterielTypeService materielTypeService;

    @Override
    public TableDataInfo<BrtMarketOrderDetailsVo> queryBrtMarketOrderDetailsList(BrtMarketOrderDetailsVo brtMarketOrderDetailsVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtMarketOrderDetailsList(PageUtils.buildPage(), brtMarketOrderDetailsVo));
    }

    @Override
    public List<BrtMarketOrderDetailsVo> queryBrtMarketOrderDetailsAll(BrtMarketOrderDetailsVo brtMarketOrderDetailsVo) {
        return this.baseMapper.queryBrtMarketOrderDetailsList(brtMarketOrderDetailsVo);
    }

    @Override
    public BrtMarketOrderDetailsVo queryBrtMarketOrderDetailsByDetailsId(String detailsId) {
        return this.baseMapper.queryBrtMarketOrderDetailsByDetailsId(detailsId);
    }

    @Transactional
    @Override
    public BrtMarketOrderDetailsVo insertBrtMarketOrderDetails(BrtMarketOrderDetailsVo brtMarketOrderDetailsVo) {
        saveBefore(brtMarketOrderDetailsVo);
        int i = this.baseMapper.insert(brtMarketOrderDetailsVo);
        return brtMarketOrderDetailsVo;
    }

    @Transactional
    @Override
    public BrtMarketOrderDetailsVo updateBrtMarketOrderDetails(BrtMarketOrderDetailsVo brtMarketOrderDetailsVo) {
        saveBefore(brtMarketOrderDetailsVo);
        int i = this.baseMapper.updateById(brtMarketOrderDetailsVo);
        return brtMarketOrderDetailsVo;
    }

    @Transactional
    @Override
    public int deleteBrtMarketOrderDetailsByDetailsIds(String[] detailsIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(detailsIds));
    }

    @Override
    public TableDataInfo<BrtMarketOrderDetailsVo> statementList(BrtMarketOrderDetailsVo brtMarketOrderDetailsVo) {
        Map<String,Object> otherData = new HashMap<>();
        otherData.put("totalAmount",0);
        List<BrtMarketOrder> marketOrderList = marketOrderMapper.selectList(new LambdaQueryWrapper<BrtMarketOrder>());
        if (ObjectUtil.isNotEmpty(marketOrderList)){
            BigDecimal totalAmount = marketOrderList.stream().map(BrtMarketOrder::getTotalAmount).reduce(BigDecimal::add).get();
            otherData.put("totalAmount",totalAmount);
        }
        return PageUtils.buildDataInfo(this.baseMapper.statementList(PageUtils.buildPage(), brtMarketOrderDetailsVo),otherData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int removeByOrderIds(String[] orderIds) {
        // 查询订单列表
        List<BrtMarketOrder> marketOrderList = marketOrderMapper.selectList(new LambdaQueryWrapper<BrtMarketOrder>().in(BrtMarketOrder::getOrderId, orderIds));

        if (ObjectUtil.isNotEmpty(marketOrderList)){
            return this.baseMapper.delete(new LambdaQueryWrapper<BrtMarketOrderDetails>().in(BrtMarketOrderDetails::getOrderId,orderIds));
        }
        return 0;
    }

    @Override
    public List<BrtMarketOrderDetailsVo> importData(List<BrtMarketOrderDetailsVo> dataList) {
        if (ObjectUtil.isEmpty(dataList)){
            throw new ServiceException("导入数据不能为空");
        }

        // 查询所有物料列表
        List<BrtMateriel> materielList = materielService.list();

        dataList.forEach(item -> {
            Optional<BrtMateriel> materielOptional = materielList.stream().filter(materiel -> materiel.getMaterielName().equals(item.getMaterielName())).findFirst();
            if (materielOptional.isPresent()){
                BrtMateriel materiel = materielOptional.get();
                item.setMaterielName(materiel.getMaterielName());
                item.setMaterielId(materiel.getMaterielId());
                item.setMaterielSize(materiel.getMaterielSize());
                item.setDetailsPrice(materiel.getSellPrice());
                String typeId = materiel.getTypeId();
                BrtMaterielTypeVo brtMaterielTypeVo = materielTypeService.queryBrtMaterielTypeByTypeId(typeId);
                item.setMaterielType(brtMaterielTypeVo.getTypeName());
            }
        });

        return dataList;
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param brtMarketOrderDetailsVo
     * @return:
     **/
    public void saveBefore(BrtMarketOrderDetailsVo brtMarketOrderDetailsVo){

    }

    /**
     * 根据订单ID查询销售单详情
     * @param
     * @return
     */
    public List<BrtMarketOrderDetailsVo> getBrtMarketOrderDetailsVoListByOrderId(String orderId){
        return this.baseMapper.getBrtMarketOrderDetailsVoListByOrderId(orderId);
    }

}
