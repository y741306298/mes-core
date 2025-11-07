package com.brt.order.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.enums.OrderNoEnums;
import com.brt.common.exception.ServiceException;
import com.brt.common.utils.PageUtils;
import com.brt.common.utils.StringUtils;
import com.brt.order.domain.BrtSupplier;
import com.brt.order.mapper.BrtSupplierMapper;
import com.brt.order.service.IBrtSupplierService;
import com.brt.order.utils.BrtOrderNoUtil;
import com.brt.order.vo.BrtSupplierVo;
import com.brt.order.vo.MarketRecordVo;
import com.brt.order.vo.PurchaseRecordVo;
import com.brt.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;

/**
 * 客户信息Service业务层处理
 *
 * @author Fgn
 * @date 2024-04-27
 */
@Service
public class BrtSupplierServiceImpl extends ServiceImpl<BrtSupplierMapper, BrtSupplier> implements IBrtSupplierService {

    @Autowired
    private BrtOrderNoUtil orderNoUtil;

    @Override
    public TableDataInfo<BrtSupplierVo> queryBrtSupplierList(BrtSupplierVo brtSupplierVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtSupplierList(PageUtils.buildPage(), brtSupplierVo));
    }

    @Override
    public List<BrtSupplierVo> queryBrtSupplierAll(BrtSupplierVo brtSupplierVo) {
        return this.baseMapper.queryBrtSupplierList(brtSupplierVo);
    }

    @Override
    public BrtSupplierVo queryBrtSupplierBySupplierId(String supplierId) {
        return this.baseMapper.queryBrtSupplierBySupplierId(supplierId);
    }

    @Transactional
    @Override
    public BrtSupplierVo insertBrtSupplier(BrtSupplierVo brtSupplierVo) {
        brtSupplierVo.setSupplierNo(orderNoUtil.getNoAndAdd(OrderNoEnums.供应商));
        saveBefore(brtSupplierVo);
        int i = this.baseMapper.insert(brtSupplierVo);
        return brtSupplierVo;
    }

    @Transactional
    @Override
    public BrtSupplierVo updateBrtSupplier(BrtSupplierVo brtSupplierVo) {
        saveBefore(brtSupplierVo);
        int i = this.baseMapper.updateById(brtSupplierVo);
        return brtSupplierVo;
    }

    @Transactional
    @Override
    public int deleteBrtSupplierBySupplierIds(String[] supplierIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(supplierIds));
    }

    @Override
    public String importData(List<BrtSupplierVo> supplierList, String operName) {

        return StringUtils.format("成功导入{}条数据",supplierList.size());
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param brtSupplierVo
     * @return:
     **/
    public void saveBefore(BrtSupplierVo brtSupplierVo){
        BrtSupplier supplier = this.baseMapper.selectOne(new LambdaQueryWrapper<BrtSupplier>()
                .ne(StringUtils.isNotEmpty(brtSupplierVo.getSupplierId()), BrtSupplier::getSupplierId, brtSupplierVo.getSupplierId())
                .eq(BrtSupplier::getSupplierNo, brtSupplierVo.getSupplierNo())
                .last(" limit 1")
        );

        if (ObjectUtil.isNotEmpty(supplier)){
            throw new ServiceException("用户编号已存在");
        }
    }

    /**
     * 查询客户的销售记录
     * @param marketRecordVo
     * @return
     */
    public TableDataInfo<PurchaseRecordVo> queryPurchaseRecord(PurchaseRecordVo purchaseRecordVo){
        return PageUtils.buildDataInfo(this.baseMapper.queryPurchaseRecord(PageUtils.buildPage(),purchaseRecordVo));
    }

}
