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
import com.brt.order.domain.BrtPriceSheetOrder;
import com.brt.order.domain.BrtPriceSheetOrderDetails;
import com.brt.order.mapper.BrtPriceSheetOrderMapper;
import com.brt.order.service.IBrtMaterielService;
import com.brt.order.service.IBrtMaterielTypeService;
import com.brt.order.vo.BrtMaterielTypeVo;
import com.brt.order.vo.BrtPriceSheetOrderDetailsVo;
import com.brt.order.mapper.BrtPriceSheetOrderDetailsMapper;
import com.brt.order.service.IBrtPriceSheetOrderDetailsService;
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
public class BrtPriceSheetOrderDetailsServiceImpl extends ServiceImpl<BrtPriceSheetOrderDetailsMapper, BrtPriceSheetOrderDetails> implements IBrtPriceSheetOrderDetailsService {

    @Autowired
    private BrtPriceSheetOrderMapper priceSheetOrderMapper;

    @Autowired
    private IBrtMaterielService materielService;

    @Autowired
    private IBrtMaterielTypeService materielTypeService;

    @Override
    public TableDataInfo<BrtPriceSheetOrderDetailsVo> queryBrtPriceSheetOrderDetailsList(BrtPriceSheetOrderDetailsVo brtPriceSheetOrderDetailsVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtPriceSheetOrderDetailsList(PageUtils.buildPage(), brtPriceSheetOrderDetailsVo));
    }

    @Override
    public List<BrtPriceSheetOrderDetailsVo> queryBrtPriceSheetOrderDetailsAll(BrtPriceSheetOrderDetailsVo brtPriceSheetOrderDetailsVo) {
        return this.baseMapper.queryBrtPriceSheetOrderDetailsList(brtPriceSheetOrderDetailsVo);
    }

    @Override
    public BrtPriceSheetOrderDetailsVo queryBrtPriceSheetOrderDetailsByDetailsId(String detailsId) {
        return this.baseMapper.queryBrtPriceSheetOrderDetailsByDetailsId(detailsId);
    }

    @Transactional
    @Override
    public BrtPriceSheetOrderDetailsVo insertBrtPriceSheetOrderDetails(BrtPriceSheetOrderDetailsVo brtPriceSheetOrderDetailsVo) {
        saveBefore(brtPriceSheetOrderDetailsVo);
        int i = this.baseMapper.insert(brtPriceSheetOrderDetailsVo);
        return brtPriceSheetOrderDetailsVo;
    }

    @Transactional
    @Override
    public BrtPriceSheetOrderDetailsVo updateBrtPriceSheetOrderDetails(BrtPriceSheetOrderDetailsVo brtPriceSheetOrderDetailsVo) {
        saveBefore(brtPriceSheetOrderDetailsVo);
        int i = this.baseMapper.updateById(brtPriceSheetOrderDetailsVo);
        return brtPriceSheetOrderDetailsVo;
    }

    @Transactional
    @Override
    public int deleteBrtPriceSheetOrderDetailsByDetailsIds(String[] detailsIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(detailsIds));
    }

    @Override
    public TableDataInfo<BrtPriceSheetOrderDetailsVo> statementList(BrtPriceSheetOrderDetailsVo brtPriceSheetOrderDetailsVo) {
        Map<String,Object> otherData = new HashMap<>();
        otherData.put("totalAmount",0);
        List<BrtPriceSheetOrder> priceSheetOrderList = priceSheetOrderMapper.selectList(new LambdaQueryWrapper<BrtPriceSheetOrder>());
        if (ObjectUtil.isNotEmpty(priceSheetOrderList)){
            BigDecimal totalAmount = priceSheetOrderList.stream().map(BrtPriceSheetOrder::getTotalAmount).reduce(BigDecimal::add).get();
            otherData.put("totalAmount",totalAmount);
        }
        return PageUtils.buildDataInfo(this.baseMapper.statementList(PageUtils.buildPage(), brtPriceSheetOrderDetailsVo),otherData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int removeByOrderIds(String[] orderIds) {
        // 查询订单列表
        List<BrtPriceSheetOrder> priceSheetOrderList = priceSheetOrderMapper.selectList(new LambdaQueryWrapper<BrtPriceSheetOrder>().in(BrtPriceSheetOrder::getOrderId, orderIds));

        if (ObjectUtil.isNotEmpty(priceSheetOrderList)){
            return this.baseMapper.delete(new LambdaQueryWrapper<BrtPriceSheetOrderDetails>().in(BrtPriceSheetOrderDetails::getOrderId,orderIds));
        }
        return 0;
    }

    @Override
    public List<BrtPriceSheetOrderDetailsVo> importData(List<BrtPriceSheetOrderDetailsVo> dataList) {
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
     * listAllByOrderId
     * @param OrderId
     * @return
     */
    public List<BrtPriceSheetOrderDetails> listAllByOrderId(String orderId){
        return this.baseMapper.listAllByOrderId(orderId);
    }

    public List<BrtPriceSheetOrderDetailsVo> queryByOrderId(String orderId){
        return this.baseMapper.queryByOrderId(orderId);
    }


    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param brtPriceSheetOrderDetailsVo
     * @return:
     **/
    public void saveBefore(BrtPriceSheetOrderDetailsVo brtPriceSheetOrderDetailsVo){

    }

}
