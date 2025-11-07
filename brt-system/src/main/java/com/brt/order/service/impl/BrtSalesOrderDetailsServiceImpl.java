package com.brt.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.config.RuoYiConfig;
import com.brt.common.constant.Constants;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.enums.CraftTypeEnums;
import com.brt.common.exception.ServiceException;
import com.brt.common.utils.PageUtils;
import com.brt.common.utils.StringUtils;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.order.domain.BrtMateriel;
import com.brt.order.domain.BrtOrderBoom;
import com.brt.order.domain.BrtSalesOrder;
import com.brt.order.domain.BrtSalesOrderDetails;
import com.brt.order.mapper.BrtSalesOrderMapper;
import com.brt.order.service.IBrtMaterielService;
import com.brt.order.service.IBrtMaterielTypeService;
import com.brt.order.service.IBrtOrderBoomService;
import com.brt.order.utils.BrtDateUtils;
import com.brt.order.vo.BrtMaterielTypeVo;
import com.brt.order.vo.BrtOrderBoomVo;
import com.brt.order.vo.BrtSalesOrderDetailsVo;
import com.brt.order.mapper.BrtSalesOrderDetailsMapper;
import com.brt.order.service.IBrtSalesOrderDetailsService;
import com.brt.order.vo.BrtSalesOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
public class BrtSalesOrderDetailsServiceImpl extends ServiceImpl<BrtSalesOrderDetailsMapper, BrtSalesOrderDetails> implements IBrtSalesOrderDetailsService {

    @Autowired
    private BrtSalesOrderMapper salesOrderMapper;

    @Autowired
    private IBrtMaterielService materielService;

    @Autowired
    private IBrtMaterielTypeService materielTypeService;

    @Autowired
    private BrtDateUtils brtDateUtils;

    @Autowired
    private IBrtOrderBoomService orderBoomService;

    @Override
    public TableDataInfo<BrtSalesOrderDetailsVo> queryBrtSalesOrderDetailsList(BrtSalesOrderDetailsVo brtSalesOrderDetailsVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtSalesOrderDetailsList(PageUtils.buildPage(), brtSalesOrderDetailsVo));
    }

    @Override
    public List<BrtSalesOrderDetailsVo> queryBrtSalesOrderDetailsAll(BrtSalesOrderDetailsVo brtSalesOrderDetailsVo) {
        return this.baseMapper.queryBrtSalesOrderDetailsList(brtSalesOrderDetailsVo);
    }

    @Override
    public BrtSalesOrderDetailsVo queryBrtSalesOrderDetailsByDetailsId(String detailsId) {
        return this.baseMapper.queryBrtSalesOrderDetailsByDetailsId(detailsId);
    }

    @Transactional
    @Override
    public BrtSalesOrderDetailsVo insertBrtSalesOrderDetails(BrtSalesOrderDetailsVo brtSalesOrderDetailsVo) {
        saveBefore(brtSalesOrderDetailsVo);
        int i = this.baseMapper.insert(brtSalesOrderDetailsVo);
        return brtSalesOrderDetailsVo;
    }

    @Transactional
    @Override
    public BrtSalesOrderDetailsVo updateBrtSalesOrderDetails(BrtSalesOrderDetailsVo brtSalesOrderDetailsVo) {
        saveBefore(brtSalesOrderDetailsVo);
        int i = this.baseMapper.updateById(brtSalesOrderDetailsVo);
        return brtSalesOrderDetailsVo;
    }

//    @Transactional
//    @Override
//    public BrtSalesOrderDetailsVo update(BrtSalesOrderDetailsVo brtSalesOrderDetailsVo) {
//        if(ObjectUtil.isNotEmpty(brtSalesOrderDetailsVo)){
//            if (ObjectUtil.isEmpty(brtSalesOrderDetailsVo.getMaterielId())){
//                brtSalesOrderDetailsVo.setMaterielId(brtSalesOrderDetailsVo.getMaterielName());
//            }
//            int i = this.baseMapper.updateById(brtSalesOrderDetailsVo);
//
//            //保存boom单
//            if(ObjectUtil.isNotEmpty(brtSalesOrderDetailsVo)){
//                brtSalesOrderDetailsVo.setOrderId(brtSalesOrderDetailsVo.getOrderId());
//
//                if (ObjectUtil.isEmpty(brtSalesOrderDetailsVo.getMaterielId())){
//                    brtSalesOrderDetailsVo.setMaterielId(brtSalesOrderDetailsVo.getMaterielName());
//                }
//                // 保存boom单列表
//                orderBoomService.saveBoomList(brtSalesOrderDetailsVo);
//            }
//
//            // 保存boom单列表
//            BrtSalesOrder salesOrder = salesOrderMapper.selectById(brtSalesOrderDetailsVo.getOrderId());
//            BrtSalesOrderVo brtSalesOrderVo = new BrtSalesOrderVo();
//            BeanUtil.copyProperties(salesOrder,brtSalesOrderVo);
//            List<BrtSalesOrderDetailsVo> salesOrderDetailsVoList = new LinkedList<>();
//            salesOrderDetailsVoList.add(brtSalesOrderDetailsVo);
//            brtSalesOrderVo.setSalesOrderDetailsVoList(salesOrderDetailsVoList);
//            materielService.lockStockNew(brtSalesOrderVo);
//        }
//
//        return brtSalesOrderDetailsVo;
//    }

    @Transactional
    public List<BrtOrderBoom> updateAndLock(BrtSalesOrderDetailsVo brtSalesOrderDetailsVo) throws Exception {
        BrtSalesOrderDetails byId = this.getById(brtSalesOrderDetailsVo.getDetailsId());
        if(StringUtils.isEmpty(byId.getBoomFile())){

            String fileUrl = brtSalesOrderDetailsVo.getBoomFile().replaceAll(Constants.RESOURCE_PREFIX, RuoYiConfig.getProfile());
            InputStream inputStream = new FileInputStream(fileUrl);

            ExcelUtil<BrtOrderBoomVo> util = new ExcelUtil<BrtOrderBoomVo>(BrtOrderBoomVo.class);
            List<BrtOrderBoomVo> orderBoomVoList = util.importExcel(inputStream);


            if(ObjectUtil.isNotEmpty(brtSalesOrderDetailsVo)){
                brtSalesOrderDetailsVo.setBoomVoList(orderBoomVoList);
                if (ObjectUtil.isEmpty(brtSalesOrderDetailsVo.getMaterielId())){
                    brtSalesOrderDetailsVo.setMaterielId(brtSalesOrderDetailsVo.getMaterielName());
                }
                int i = this.baseMapper.updateById(brtSalesOrderDetailsVo);

                //保存boom单
                if(ObjectUtil.isNotEmpty(brtSalesOrderDetailsVo)){
                    brtSalesOrderDetailsVo.setOrderId(brtSalesOrderDetailsVo.getOrderId());

                    if (ObjectUtil.isEmpty(brtSalesOrderDetailsVo.getMaterielId())){
                        brtSalesOrderDetailsVo.setMaterielId(brtSalesOrderDetailsVo.getMaterielName());
                    }
                    // 保存boom单列表
                    orderBoomService.saveBoomList(brtSalesOrderDetailsVo);
                }

                // 保存boom单列表
                BrtSalesOrder salesOrder = salesOrderMapper.selectById(brtSalesOrderDetailsVo.getOrderId());
                BrtSalesOrderVo brtSalesOrderVo = new BrtSalesOrderVo();
                BeanUtil.copyProperties(salesOrder,brtSalesOrderVo);
                List<BrtSalesOrderDetailsVo> salesOrderDetailsVoList = new LinkedList<>();
                salesOrderDetailsVoList.add(brtSalesOrderDetailsVo);
                brtSalesOrderVo.setSalesOrderDetailsVoList(salesOrderDetailsVoList);
                materielService.lockStockNew(brtSalesOrderVo);
            }
        }else{
            brtSalesOrderDetailsVo.setBoomFile(null);
            int i = this.baseMapper.updateById(brtSalesOrderDetailsVo);
        }

        return orderBoomService.list(new LambdaQueryWrapper<BrtOrderBoom>().eq(BrtOrderBoom::getOrderDetailsId,brtSalesOrderDetailsVo.getDetailsId()));
    }


    @Transactional
    @Override
    public int deleteBrtSalesOrderDetailsByDetailsIds(String[] detailsIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(detailsIds));
    }

    @Override
    public TableDataInfo<BrtSalesOrderDetailsVo> statementList(BrtSalesOrderDetailsVo brtSalesOrderDetailsVo) {
        brtSalesOrderDetailsVo.setCreateTimeStart(brtDateUtils.getYearStartStr());
        brtSalesOrderDetailsVo.setCreateTimeEnd(brtDateUtils.getYearEndStr());


        Map<String,Object> otherData = new HashMap<>();
        otherData.put("createTimeStart",brtDateUtils.getYearStartStr());
        otherData.put("createTimeEnd",brtDateUtils.getYearEndStr());
        otherData.put("totalAmount",0);
        List<BrtSalesOrder> salesOrderList = salesOrderMapper.selectList(new LambdaQueryWrapper<BrtSalesOrder>());
        if (ObjectUtil.isNotEmpty(salesOrderList)){
            BigDecimal totalAmount = salesOrderList.stream().map(BrtSalesOrder::getTotalAmount).reduce(BigDecimal::add).get();
            otherData.put("totalAmount",totalAmount);
        }
        return PageUtils.buildDataInfo(this.baseMapper.statementList(PageUtils.buildPage(), brtSalesOrderDetailsVo),otherData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int removeByOrderIds(String[] orderIds) {
        // 查询订单列表
        List<BrtSalesOrder> salesOrderList = salesOrderMapper.selectList(new LambdaQueryWrapper<BrtSalesOrder>().in(BrtSalesOrder::getOrderId, orderIds));

        if (ObjectUtil.isNotEmpty(salesOrderList)){
            salesOrderList.forEach(item -> {
                if (StringUtils.isNotEmpty(item.getCraftType())&&item.getCraftType().equals(CraftTypeEnums.非标准品.getCode())){

                    BrtSalesOrderDetailsVo query = new  BrtSalesOrderDetailsVo();
                    query.setOrderId(item.getOrderId());
                    //查询订单详情
                    List<BrtSalesOrderDetailsVo> detailsVoList = this.queryBrtSalesOrderDetailsAll(query);
                    detailsVoList.forEach(detailItem->{
                        if(ObjectUtil.isNotEmpty(detailItem)&&ObjectUtil.isNotEmpty(detailItem.getBoomVoList())){
                            List<BrtOrderBoomVo> boomVoList = detailItem.getBoomVoList();
                            boomVoList.forEach(boomItem->{
                                BrtMateriel materiel = materielService.getById(boomItem.getMaterielId());
                                if(ObjectUtil.isNotEmpty(materiel)){
                                    Long totalSum = boomItem.getTotalNum();
                                    materiel.setMaterielNum(materiel.getMaterielNum() - totalSum);
                                    materiel.setLockNum(materiel.getLockNum() - totalSum);
                                    materielService.updateById(materiel);
                                }
                            });
                        }
                    });
                }
            });
            return this.baseMapper.delete(new LambdaQueryWrapper<BrtSalesOrderDetails>().in(BrtSalesOrderDetails::getOrderId,orderIds));
        }
        return 0;
    }

    @Override
    public List<BrtSalesOrderDetailsVo> importData(List<BrtSalesOrderDetailsVo> dataList) {
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
     * @param brtSalesOrderDetailsVo
     * @return:
     **/
    public void saveBefore(BrtSalesOrderDetailsVo brtSalesOrderDetailsVo){

    }

    /**
     * 根据订单ID查询销售单详情
     * @param
     * @return
     */
    public List<BrtSalesOrderDetailsVo> getBrtSalesOrderDetailsVoListByOrderId(String orderId){
        return this.baseMapper.getBrtSalesOrderDetailsVoListByOrderId(orderId);
    }


}
