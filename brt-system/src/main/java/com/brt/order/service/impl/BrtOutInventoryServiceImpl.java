package com.brt.order.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.enums.OrderNoEnums;
import com.brt.common.utils.PageUtils;
import com.brt.common.utils.StringUtils;
import com.brt.order.domain.BrtOrderBoom;
import com.brt.order.domain.BrtOutInventory;
import com.brt.order.domain.BrtMateriel;
import com.brt.order.domain.BrtOutInventoryMateriel;
import com.brt.order.service.*;
import com.brt.order.utils.BrtOrderNoUtil;
import com.brt.order.vo.*;
import com.brt.order.mapper.BrtOutInventoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.aspose.cells.b.a.b.r.br;
import static com.aspose.cells.b.a.b.r.t;

/**
 * 入库管理Service业务层处理
 *
 * @author Fgn
 * @date 2024-07-11
 */
@Service
public class BrtOutInventoryServiceImpl extends ServiceImpl<BrtOutInventoryMapper, BrtOutInventory> implements IBrtOutInventoryService {

    @Autowired
    private IBrtMaterielService materielService;

    @Autowired
    private IBrtOutInventoryMaterielService outInventoryMaterielService;

    @Autowired
    private IBrtMaterielTypeService materielTypeService;

    @Autowired
    private IBrtSalesOrderService salesOrderService;

    @Autowired
    private IBrtSalesOrderDetailsService salesOrderDetailsService;

    @Autowired
    private BrtOrderNoUtil orderNoUtil;

    @Override
    public TableDataInfo<BrtOutInventoryVo> queryBrtOutInventoryList(BrtOutInventoryVo brtOutInventoryVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtOutInventoryList(PageUtils.buildPage(), brtOutInventoryVo));
    }

    @Override
    public List<BrtOutInventoryVo> queryBrtOutInventoryAll(BrtOutInventoryVo brtOutInventoryVo) {
        return this.baseMapper.queryBrtOutInventoryList(brtOutInventoryVo);
    }

    @Override
    public BrtOutInventoryVo queryBrtOutInventoryByOutInventoryId(String outInventoryId) {
        BrtOutInventoryVo brtOutInventoryVo = this.baseMapper.queryBrtOutInventoryByOutInventoryId(outInventoryId);
        List<BrtOutInventoryMaterielVo> outInventoryMaterielVos = outInventoryMaterielService.queryByOutInventoryId(outInventoryId);
        brtOutInventoryVo.setOutInventoryMaterielVos(outInventoryMaterielVos);
        return brtOutInventoryVo;
    }

    @Transactional
    @Override
    public BrtOutInventoryVo insertBrtOutInventory(BrtOutInventoryVo brtOutInventoryVo) {
        brtOutInventoryVo.setOutInventoryNo(orderNoUtil.getNoAndAdd(OrderNoEnums.出库单));
        if(StringUtils.isEmpty(brtOutInventoryVo.getOutInventoryType())){
            brtOutInventoryVo.setOutInventoryType("0");
        }
        brtOutInventoryVo.setOutInventoryStatus("0");
        List<BrtOutInventoryMaterielVo> outInventoryMaterielVos = brtOutInventoryVo.getOutInventoryMaterielVos();
        saveBefore(brtOutInventoryVo);
        int i = this.baseMapper.insert(brtOutInventoryVo);

        if(ObjectUtil.isNotEmpty(outInventoryMaterielVos)){
            for(BrtOutInventoryMaterielVo outInventoryMaterielVo:outInventoryMaterielVos){
                if(ObjectUtil.isNotEmpty(outInventoryMaterielVo)){
                    if(StringUtils.isEmpty(outInventoryMaterielVo.getMaterielId())){//物料ID为空，说明是导入数据，需判断库存中是否有此物料
                        BrtMaterielVo materielVo = materielService.queryByName(outInventoryMaterielVo.getMaterielName());
                        if(ObjectUtil.isEmpty(materielVo)){
                            materielVo = new BrtMaterielVo();
                            materielVo.setMaterielNo(outInventoryMaterielVo.getMaterielNo());
                            materielVo.setMaterielName(outInventoryMaterielVo.getMaterielName());
                            materielVo.setMaterielSpec(outInventoryMaterielVo.getMaterielSpec());
                            materielVo.setMaterielNum(0l);
                            materielVo.setTypeId(outInventoryMaterielVo.getTypeId());

                            materielService.insertBrtMateriel(materielVo);
                        }
                        outInventoryMaterielVo.setMaterielId(materielVo.getMaterielId());
                    }

                    //保存入库单详情
                    outInventoryMaterielVo.setOutInventoryId(brtOutInventoryVo.getOutInventoryId());
                    if(StringUtils.isNotEmpty(outInventoryMaterielVo.getOutInventoryMaterielId())){
                        outInventoryMaterielService.updateBrtOutInventoryMateriel(outInventoryMaterielVo);
                    }else{
                        outInventoryMaterielService.insertBrtOutInventoryMateriel(outInventoryMaterielVo);
                    }

                }
            }
        }

        return brtOutInventoryVo;
    }

    @Transactional
    @Override
    public BrtOutInventoryVo updateBrtOutInventory(BrtOutInventoryVo brtOutInventoryVo) {
        saveBefore(brtOutInventoryVo);
        int i = this.baseMapper.updateById(brtOutInventoryVo);

        List<BrtOutInventoryMaterielVo> outInventoryMaterielVos = brtOutInventoryVo.getOutInventoryMaterielVos();
        if(ObjectUtil.isNotEmpty(outInventoryMaterielVos)){
            for(BrtOutInventoryMaterielVo outInventoryMaterielVo:outInventoryMaterielVos){
                if(ObjectUtil.isNotEmpty(outInventoryMaterielVo)){

                    if(StringUtils.isEmpty(outInventoryMaterielVo.getMaterielId())){//物料ID为空，说明是导入数据，需判断库存中是否有此物料
                        BrtMaterielVo materielVo = materielService.queryByName(outInventoryMaterielVo.getMaterielName());
                        if(ObjectUtil.isEmpty(materielVo)){
                            materielVo = new BrtMaterielVo();
                            materielVo.setMaterielNo(outInventoryMaterielVo.getMaterielNo());
                            materielVo.setMaterielName(outInventoryMaterielVo.getMaterielName());
                            materielVo.setMaterielSpec(outInventoryMaterielVo.getMaterielSpec());
                            materielVo.setMaterielNum(0l);
                            materielVo.setTypeId(outInventoryMaterielVo.getTypeId());
                            materielService.insertBrtMateriel(materielVo);
                        }
                        outInventoryMaterielVo.setMaterielId(materielVo.getMaterielId());
                    }

                    //保存入库单详情
                    outInventoryMaterielVo.setOutInventoryId(brtOutInventoryVo.getOutInventoryId());
                    if(StringUtils.isNotEmpty(outInventoryMaterielVo.getOutInventoryMaterielId())){
                        outInventoryMaterielService.updateBrtOutInventoryMateriel(outInventoryMaterielVo);
                    }else{
                        outInventoryMaterielService.insertBrtOutInventoryMateriel(outInventoryMaterielVo);
                    }

                }
            }
        }

        return brtOutInventoryVo;
    }

    @Transactional
    @Override
    public int deleteBrtOutInventoryByOutInventoryIds(String[] outInventoryIds) {
        outInventoryMaterielService.deleteByInventoryId(outInventoryIds);
        return this.baseMapper.deleteBatchIds(Arrays.asList(outInventoryIds));
    }

    /**
     * @description: TODO 确认入库
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param:  outInventoryIds 需要删除的入库管理主键集合
     * @return:
     * @return int
     **/
    @Transactional
    public void affirm(String outInventoryId){
        BrtOutInventoryVo outInventoryVo = this.queryBrtOutInventoryByOutInventoryId(outInventoryId);
        outInventoryVo.setOutInventoryStatus("1");
        this.updateBrtOutInventory(outInventoryVo);
        List<BrtOutInventoryMaterielVo> outInventoryMaterielVos = outInventoryMaterielService.queryByOutInventoryId(outInventoryId);
        for(BrtOutInventoryMaterielVo outInventoryMaterielVo:outInventoryMaterielVos){
            materielService.reduceNum(outInventoryMaterielVo);
            BrtMateriel materiel = materielService.getById(outInventoryMaterielVo.getMaterielId());
            Long materielNum = materiel.getMaterielNum();
            String s = String.valueOf(materielNum);
            BrtOutInventoryMateriel outInventoryMateriel = new BrtOutInventoryMateriel();
            outInventoryMateriel.setOutInventoryMaterielId(outInventoryMaterielVo.getOutInventoryMaterielId());
            outInventoryMateriel.setResidueNum(new BigDecimal(s));
            outInventoryMaterielService.updateById(outInventoryMateriel);
        }

    }

    /**
     * 生成出库单
     * @param orderId
     * @param orderBoomList
     */
    @Transactional
    public void createOutInventory(String orderId, List<BrtOrderBoom> orderBoomList){

        BrtSalesOrderVo brtSalesOrderVo = salesOrderService.queryBrtSalesOrderByOrderId(orderId);
        BrtOutInventoryVo brtOutInventoryVo = new BrtOutInventoryVo();
        brtOutInventoryVo.setOrderId(orderId);
        brtOutInventoryVo.setOutInventoryType("1");
        brtOutInventoryVo.setOutInventoryStatus("1");
        brtOutInventoryVo.setApplicat(brtSalesOrderVo.getUserId());
        brtOutInventoryVo.setApplyTime(brtSalesOrderVo.getCreateTime());
        brtOutInventoryVo.setOutInventoryNo(orderNoUtil.getNoAndAdd(OrderNoEnums.出库单));
        this.baseMapper.insert(brtOutInventoryVo);

        for(BrtOrderBoom orderBoom:orderBoomList){
            BrtOutInventoryMaterielVo outInventoryMaterielVo = new BrtOutInventoryMaterielVo();
            outInventoryMaterielVo.setOutInventoryId(brtOutInventoryVo.getOutInventoryId());
            outInventoryMaterielVo.setMaterielId(orderBoom.getMaterielId());
            outInventoryMaterielVo.setOutInventoryNum(orderBoom.getTotalNum());

            BrtMateriel materiel = materielService.getById(outInventoryMaterielVo.getMaterielId());
            if(ObjectUtil.isNotEmpty(materiel)){
                Long materielNum = materiel.getMaterielNum();
                String s = String.valueOf(materielNum);
                outInventoryMaterielVo.setResidueNum(new BigDecimal(s));
            }
            outInventoryMaterielService.insertBrtOutInventoryMateriel(outInventoryMaterielVo);


        }
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param brtOutInventoryVo
     * @return:
     **/
    public void saveBefore(BrtOutInventoryVo brtOutInventoryVo){

    }

}
