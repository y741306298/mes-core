package com.brt.order.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.enums.CraftTypeEnums;
import com.brt.common.exception.ServiceException;
import com.brt.common.utils.PageUtils;
import com.brt.common.utils.StringUtils;
import com.brt.order.domain.BrtMateriel;
import com.brt.order.domain.BrtMaterielType;
import com.brt.order.domain.BrtOrderBoom;
import com.brt.order.domain.BrtSalesOrder;
import com.brt.order.mapper.BrtMaterielTypeMapper;
import com.brt.order.service.IBrtOrderBoomService;
import com.brt.order.vo.*;
import com.brt.order.mapper.BrtMaterielMapper;
import com.brt.order.service.IBrtMaterielService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 物料信息Service业务层处理
 *
 * @author Fgn
 * @date 2024-05-07
 */
@Service
public class BrtMaterielServiceImpl extends ServiceImpl<BrtMaterielMapper, BrtMateriel> implements IBrtMaterielService {

    @Autowired
    private BrtMaterielTypeMapper materielTypeMapper;

    @Autowired
    private IBrtOrderBoomService orderBoomService;

    @Override
    public TableDataInfo<BrtMaterielVo> queryBrtMaterielList(BrtMaterielVo brtMaterielVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtMaterielList(PageUtils.buildPage(), brtMaterielVo));
    }

    @Override
    public List<BrtMaterielVo> queryBrtMaterielAll(BrtMaterielVo brtMaterielVo) {
        return this.baseMapper.queryBrtMaterielList(brtMaterielVo);
    }

    @Override
    public BrtMaterielVo queryBrtMaterielByMaterielId(String materielId) {
        return this.baseMapper.queryBrtMaterielByMaterielId(materielId);
    }

    @Transactional
    @Override
    public BrtMaterielVo insertBrtMateriel(BrtMaterielVo brtMaterielVo) {
        saveBefore(brtMaterielVo);
        int i = this.baseMapper.insert(brtMaterielVo);
        return brtMaterielVo;
    }

    @Transactional
    @Override
    public BrtMaterielVo updateBrtMateriel(BrtMaterielVo brtMaterielVo) {
        saveBefore(brtMaterielVo);
        int i = this.baseMapper.updateById(brtMaterielVo);
        return brtMaterielVo;
    }

    @Transactional
    @Override
    public int deleteBrtMaterielByMaterielIds(String[] materielIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(materielIds));
    }

    @Override
    public String importData(List<BrtMaterielVo> materielVoList, String operName) {

        if (ObjectUtil.isEmpty(materielVoList)){
            throw new ServiceException("导入数据不可为空");
        }

        // 查询物料分类列表
        List<BrtMaterielType> materielTypeList = materielTypeMapper.selectList(new LambdaQueryWrapper<BrtMaterielType>());

        materielVoList.stream().forEach(item -> {
            // 查找对应的分类
            Optional<BrtMaterielType> materielTypeOptional = materielTypeList.stream().filter(type -> type.getTypeName().equals(item.getTypeName())).findFirst();
            if (materielTypeOptional.isPresent()){
                item.setTypeId(materielTypeOptional.get().getTypeId());
            }

            this.baseMapper.insert(item);
        });
        return StringUtils.format("成功导入{}条数据",materielVoList.size());
    }

    @Override
    public int warning(String materielIds, String warningNum) {
        return this.baseMapper.update(null,new LambdaUpdateWrapper<BrtMateriel>().set(BrtMateriel::getWarningNum,warningNum).in(BrtMateriel::getMaterielId,materielIds.split(",")));
    }

    @Override
    public int lockStock(BrtSalesOrderVo brtSalesOrderVo) {

        // 查询所有产品列表
        List<BrtMateriel> materielList = this.list();

        brtSalesOrderVo.getSalesOrderDetailsVoList().forEach(item -> {

            // 保存boom单列表
            List<BrtOrderBoom> orderBoomList = orderBoomService.list(new LambdaQueryWrapper<BrtOrderBoom>().eq(BrtOrderBoom::getOrderId, brtSalesOrderVo.getOrderId()).eq(BrtOrderBoom::getOrderDetailsId, item.getDetailsId()));
            orderBoomList.forEach(boom -> {
                // 查询匹配的物料
                Optional<BrtMateriel> materielOptional = materielList.stream().filter(materiel -> materiel.getMaterielName().equals(boom.getMaterielName())).findFirst();
                if (materielOptional.isPresent()){
                    BrtMateriel materiel = materielOptional.get();
                    materiel.setMaterielNum(materiel.getMaterielNum() - boom.getTotalNum());
                    materiel.setLockNum(materiel.getLockNum() + boom.getTotalNum());
                    this.baseMapper.updateById(materiel);

                    boom.setMaterielId(materiel.getMaterielId());
                }else {
                    BrtMateriel materiel = new BrtMateriel();
                    materiel.setMaterielName(boom.getMaterielName());
                    materiel.setMaterielSpec(boom.getMaterielSpec());
                    materiel.setMaterielNum(0 - boom.getTotalNum());
                    materiel.setLockNum(boom.getTotalNum());
                    this.baseMapper.insert(materiel);
                    materielList.add(materiel);

                    boom.setMaterielId(materiel.getMaterielId());
                }
                orderBoomService.updateById(boom);
            });
        });

        return 0;
    }


    @Override
    public int lockStockNew(BrtSalesOrderVo brtSalesOrderVo) {

        // 查询所有产品列表
        List<BrtMateriel> materielList = this.list();

        brtSalesOrderVo.getSalesOrderDetailsVoList().forEach(item -> {

            // 保存boom单列表
            List<BrtOrderBoom> orderBoomList = orderBoomService.list(new LambdaQueryWrapper<BrtOrderBoom>().eq(BrtOrderBoom::getOrderId, brtSalesOrderVo.getOrderId()).eq(BrtOrderBoom::getOrderDetailsId, item.getDetailsId()).eq(BrtOrderBoom::getIsLock,"N"));
            orderBoomList.forEach(boom -> {
                boom.setIsLock("Y");
                // 查询匹配的物料
                Optional<BrtMateriel> materielOptional = materielList.stream().filter(materiel -> materiel.getMaterielName().equals(boom.getMaterielName())).findFirst();
                if (materielOptional.isPresent()){
                    BrtMateriel materiel = materielOptional.get();
                    materiel.setMaterielNum(materiel.getMaterielNum() - boom.getTotalNum());
                    materiel.setLockNum(materiel.getLockNum() + boom.getTotalNum());
                    this.baseMapper.updateById(materiel);

                    boom.setMaterielId(materiel.getMaterielId());
                }else {
                    BrtMateriel materiel = new BrtMateriel();
                    materiel.setMaterielName(boom.getMaterielName());
                    materiel.setMaterielSpec(boom.getMaterielSpec());
                    materiel.setMaterielNum(0 - boom.getTotalNum());
                    materiel.setLockNum(boom.getTotalNum());
                    this.baseMapper.insert(materiel);
                    materielList.add(materiel);

                    boom.setMaterielId(materiel.getMaterielId());
                }
                orderBoomService.updateById(boom);
            });
        });

        return 0;
    }


    @Override
    public int deductionSock(List<BrtOrderBoomVo> orderBoomVoList) {

        orderBoomVoList.forEach(boom -> {
            this.baseMapper.update(null,new LambdaUpdateWrapper<BrtMateriel>().setSql(" lock_num = lock_num - "+boom.getTotalNum()+"").eq(BrtMateriel::getMaterielId,boom.getMaterielId()));
        });
        return orderBoomVoList.size();
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @param brtMaterielVo
     * @return:
     **/
    public void saveBefore(BrtMaterielVo brtMaterielVo){

    }

    /**
     * 根据名称查询
     * @param name
     * @return
     */
    public BrtMaterielVo queryByName(String name){
        return this.baseMapper.queryByName(name);
    }

    /**
     * 修改开库存数量
     * @param
     */
    public void addNum(BrtInInventoryMaterielVo inInventoryMaterielVo){
        this.baseMapper.addNum(inInventoryMaterielVo);
    }
    public void reduceNum(BrtOutInventoryMaterielVo outInventoryMaterielVo){
        this.baseMapper.reduceNum(outInventoryMaterielVo);
    }

    /**
     * 查询出入库记录
     * @param materielRecordVo
     * @return
     */
    public TableDataInfo<BrtMaterielRecordVo> selectRecord(BrtMaterielRecordVo materielRecordVo){
        return PageUtils.buildDataInfo(this.baseMapper.selectRecord(PageUtils.buildPage(), materielRecordVo));
    }


}
