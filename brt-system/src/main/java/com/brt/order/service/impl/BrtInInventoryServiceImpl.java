package com.brt.order.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.enums.OrderNoEnums;
import com.brt.common.utils.PageUtils;
import com.brt.common.utils.StringUtils;
import com.brt.order.domain.BrtInInventory;
import com.brt.order.domain.BrtMateriel;
import com.brt.order.domain.BrtInInventoryMateriel;
import com.brt.order.service.*;
import com.brt.order.utils.BrtOrderNoUtil;
import com.brt.order.vo.*;
import com.brt.order.mapper.BrtInInventoryMapper;
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
public class BrtInInventoryServiceImpl extends ServiceImpl<BrtInInventoryMapper, BrtInInventory> implements IBrtInInventoryService {

    @Autowired
    private IBrtMaterielService materielService;

    @Autowired
    private IBrtInInventoryMaterielService inInventoryMaterielService;

    @Autowired
    private IBrtMaterielTypeService materielTypeService;

    @Autowired
    private IBrtMarketOrderService marketOrderService;

    @Autowired
    private IBrtMarketOrderDetailsService marketOrderDetailsService;

    @Autowired
    private BrtOrderNoUtil brtOrderNoUtil;

    @Override
    public TableDataInfo<BrtInInventoryVo> queryBrtInInventoryList(BrtInInventoryVo brtInInventoryVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtInInventoryList(PageUtils.buildPage(), brtInInventoryVo));
    }

    @Override
    public List<BrtInInventoryVo> queryBrtInInventoryAll(BrtInInventoryVo brtInInventoryVo) {
        return this.baseMapper.queryBrtInInventoryList(brtInInventoryVo);
    }

    @Override
    public BrtInInventoryVo queryBrtInInventoryByInInventoryId(String inInventoryId) {
        BrtInInventoryVo brtInInventoryVo = this.baseMapper.queryBrtInInventoryByInInventoryId(inInventoryId);
        List<BrtInInventoryMaterielVo> inInventoryMaterielVos = inInventoryMaterielService.queryByInInventoryId(inInventoryId);
        brtInInventoryVo.setInInventoryMaterielVos(inInventoryMaterielVos);
        return brtInInventoryVo;
    }

    @Transactional
    @Override
    public BrtInInventoryVo insertBrtInInventory(BrtInInventoryVo brtInInventoryVo) {
        brtInInventoryVo.setInInventoryNo(brtOrderNoUtil.getNoAndAdd(OrderNoEnums.入库单));
        if(StringUtils.isEmpty(brtInInventoryVo.getInInventoryType())){
            brtInInventoryVo.setInInventoryType("0");
        }
        brtInInventoryVo.setInInventoryStatus("0");

        List<BrtInInventoryMaterielVo> inInventoryMaterielVos = brtInInventoryVo.getInInventoryMaterielVos();
        saveBefore(brtInInventoryVo);
        int i = this.baseMapper.insert(brtInInventoryVo);

        if(ObjectUtil.isNotEmpty(inInventoryMaterielVos)){
            for(BrtInInventoryMaterielVo inInventoryMaterielVo:inInventoryMaterielVos){
                if(ObjectUtil.isNotEmpty(inInventoryMaterielVo)){
                    if(StringUtils.isEmpty(inInventoryMaterielVo.getMaterielId())){//物料ID为空，说明是导入数据，需判断库存中是否有此物料
                        BrtMaterielVo materielVo = materielService.queryByName(inInventoryMaterielVo.getMaterielName());
                        if(ObjectUtil.isEmpty(materielVo)){
                            materielVo = new BrtMaterielVo();
                            materielVo.setMaterielNo(inInventoryMaterielVo.getMaterielNo());
                            materielVo.setMaterielName(inInventoryMaterielVo.getMaterielName());
                            materielVo.setMaterielSpec(inInventoryMaterielVo.getMaterielSpec());
                            materielVo.setMaterielNum(0l);
                            materielVo.setTypeId(inInventoryMaterielVo.getTypeId());
                            materielService.insertBrtMateriel(materielVo);
                        }
                        inInventoryMaterielVo.setMaterielId(materielVo.getMaterielId());
                    }

                    //保存入库单详情
                    inInventoryMaterielVo.setInInventoryId(brtInInventoryVo.getInInventoryId());
                    if(StringUtils.isNotEmpty(inInventoryMaterielVo.getInInventoryMaterielId())){
                        inInventoryMaterielService.updateBrtInInventoryMateriel(inInventoryMaterielVo);
                    }else{
                        inInventoryMaterielService.insertBrtInInventoryMateriel(inInventoryMaterielVo);
                    }

                }
            }
        }

        return brtInInventoryVo;
    }

    @Transactional
    @Override
    public BrtInInventoryVo updateBrtInInventory(BrtInInventoryVo brtInInventoryVo) {
        saveBefore(brtInInventoryVo);
        int i = this.baseMapper.updateById(brtInInventoryVo);

        List<BrtInInventoryMaterielVo> inInventoryMaterielVos = brtInInventoryVo.getInInventoryMaterielVos();
        if(ObjectUtil.isNotEmpty(inInventoryMaterielVos)){
            for(BrtInInventoryMaterielVo inInventoryMaterielVo:inInventoryMaterielVos){
                if(ObjectUtil.isNotEmpty(inInventoryMaterielVo)){

                    if(StringUtils.isEmpty(inInventoryMaterielVo.getMaterielId())){//物料ID为空，说明是导入数据，需判断库存中是否有此物料
                        BrtMaterielVo materielVo = materielService.queryByName(inInventoryMaterielVo.getMaterielName());
                        if(ObjectUtil.isEmpty(materielVo)){
                            materielVo = new BrtMaterielVo();
                            materielVo.setMaterielNo(inInventoryMaterielVo.getMaterielNo());
                            materielVo.setMaterielName(inInventoryMaterielVo.getMaterielName());
                            materielVo.setMaterielSpec(inInventoryMaterielVo.getMaterielSpec());
                            materielVo.setMaterielNum(0l);
                            materielVo.setTypeId(inInventoryMaterielVo.getTypeId());
                            materielService.insertBrtMateriel(materielVo);
                        }
                        inInventoryMaterielVo.setMaterielId(materielVo.getMaterielId());
                    }

                    //保存入库单详情
                    inInventoryMaterielVo.setInInventoryId(brtInInventoryVo.getInInventoryId());
                    if(StringUtils.isNotEmpty(inInventoryMaterielVo.getInInventoryMaterielId())){
                        inInventoryMaterielService.updateBrtInInventoryMateriel(inInventoryMaterielVo);
                    }else{
                        inInventoryMaterielService.insertBrtInInventoryMateriel(inInventoryMaterielVo);
                    }

                }
            }
        }

        return brtInInventoryVo;
    }

    @Transactional
    @Override
    public int deleteBrtInInventoryByInInventoryIds(String[] inInventoryIds) {
        inInventoryMaterielService.deleteByInventoryId(inInventoryIds);
        return this.baseMapper.deleteBatchIds(Arrays.asList(inInventoryIds));
    }

    /**
     * @description: TODO 确认入库
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param:  inInventoryIds 需要删除的入库管理主键集合
     * @return:
     * @return int
     **/
    @Transactional
    public void affirm(String inInventoryId){
        BrtInInventoryVo inInventoryVo = this.queryBrtInInventoryByInInventoryId(inInventoryId);
        inInventoryVo.setInInventoryStatus("1");
        this.updateBrtInInventory(inInventoryVo);
        List<BrtInInventoryMaterielVo> inInventoryMaterielVos = inInventoryMaterielService.queryByInInventoryId(inInventoryId);
        for(BrtInInventoryMaterielVo inInventoryMaterielVo:inInventoryMaterielVos){
            materielService.addNum(inInventoryMaterielVo);
            BrtMateriel materiel = materielService.getById(inInventoryMaterielVo.getMaterielId());
            Long materielNum = materiel.getMaterielNum();
            String s = String.valueOf(materielNum);
            BrtInInventoryMateriel inInventoryMateriel = new BrtInInventoryMateriel();
            inInventoryMateriel.setInInventoryMaterielId(inInventoryMaterielVo.getInInventoryMaterielId());
            inInventoryMateriel.setResidueNum(new BigDecimal(s));
            inInventoryMaterielService.updateById(inInventoryMateriel);
        }

    }

    @Transactional
    public void createInInventory(String orderId){
        BrtMarketOrderVo brtMarketOrderVo = marketOrderService.queryBrtMarketOrderByOrderId(orderId);
        if(ObjectUtil.isEmpty(brtMarketOrderVo)){
            throw new RuntimeException("生成入库单失败");
        }
        BrtInInventoryVo brtInInventoryVo = new BrtInInventoryVo();
        brtInInventoryVo.setInInventoryType("1");
        brtInInventoryVo.setInInventoryStatus("1");
        brtInInventoryVo.setApplicat(brtMarketOrderVo.getUserId());
        brtInInventoryVo.setApplyTime(brtMarketOrderVo.getCreateTime());
        brtInInventoryVo.setOrderId(orderId);
        brtInInventoryVo.setInInventoryNo(brtOrderNoUtil.getNoAndAdd(OrderNoEnums.入库单));
        this.baseMapper.insert(brtInInventoryVo);
        BrtMarketOrderDetailsVo queryVo = new BrtMarketOrderDetailsVo();
        queryVo.setOrderId(orderId);
        List<BrtMarketOrderDetailsVo> brtMarketOrderDetailsVos = marketOrderDetailsService.queryBrtMarketOrderDetailsAll(queryVo);
        if(ObjectUtil.isEmpty(brtMarketOrderDetailsVos)){
            throw new RuntimeException("生成入库单失败");
        }
        for(BrtMarketOrderDetailsVo marketOrderDetailsVo:brtMarketOrderDetailsVos){
            if(ObjectUtil.isEmpty(marketOrderDetailsVo)){
                throw new RuntimeException("生成入库单失败");
            }
            BrtInInventoryMaterielVo inInventoryMaterielVo = new BrtInInventoryMaterielVo();
            inInventoryMaterielVo.setInInventoryId(brtInInventoryVo.getInInventoryId());
            inInventoryMaterielVo.setMaterielId(marketOrderDetailsVo.getMaterielId());
            inInventoryMaterielVo.setInInventoryNum(marketOrderDetailsVo.getDetailsNum());

            BrtMateriel materiel = materielService.getById(inInventoryMaterielVo.getMaterielId());
            if(ObjectUtil.isNotEmpty(materiel)){
                Long materielNum = materiel.getMaterielNum();
                materielNum+= marketOrderDetailsVo.getDetailsNum();
                materiel.setMaterielNum(materielNum);
                String s = String.valueOf(materielNum);
                inInventoryMaterielVo.setResidueNum(new BigDecimal(s));
                materielService.updateById(materiel);
            }

            inInventoryMaterielService.insertBrtInInventoryMateriel(inInventoryMaterielVo);

        }
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param brtInInventoryVo
     * @return:
     **/
    public void saveBefore(BrtInInventoryVo brtInInventoryVo){

    }

}
