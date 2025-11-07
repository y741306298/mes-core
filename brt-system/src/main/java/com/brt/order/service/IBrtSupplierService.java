package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtSupplier;
import com.brt.order.vo.BrtSupplierVo;
import com.brt.order.vo.MarketRecordVo;
import com.brt.order.vo.PurchaseRecordVo;

import java.util.List;

/**
 * 客户信息Service接口
 *
 * @author Fgn
 * @date 2024-04-27
 */
public interface IBrtSupplierService extends IService<BrtSupplier> {

    /**
     * @description: TODO 分页查询供应商信息列表
     * @author: lf
     * * @date: 2024-06-19
     * @param:
     * @param: brtSupplierVo 供应商信息
     * @return:
     * @return TableDataInfo<BrtSupplierVo>
     **/
    public TableDataInfo<BrtSupplierVo> queryBrtSupplierList(BrtSupplierVo brtSupplierVo);

    /**
     * @description: TODO 查询全部供应商信息列表
     * @author: lf
     * * @date: 2024-06-19
     * @param:
     * @param: brtSupplierVo 供应商信息
     * @return:
     * @return java.util.List<BrtSupplierVo>
     **/
    public List<BrtSupplierVo> queryBrtSupplierAll(BrtSupplierVo brtCustomerVo);

    /**
     * @description: TODO 根据supplierId查询供应商信息
     * @author: lf
     * @date: 2024-06-19
     * @param:
     * @param: customerId
     * @return:
     * @return BrtSupplierVo
     **/
    public BrtSupplierVo queryBrtSupplierBySupplierId(String supplierId);

    /**
     * @description: TODO 新增供应商信息
     * @author: lf
     * @date: 2024-06-19
     * @param:
     * @param: brtCustomerVo 客户信息
     * @return:
     * @return int
     **/
    public BrtSupplierVo insertBrtSupplier(BrtSupplierVo brtSupplierVo);

    /**
     * @description: TODO 修改供应商信息
     * @author: lf
     * @date: 2024-06-19
     * @param:
     * @param: brtSupplierVo 供应商信息
     * @return:
     * @return int
     **/
    public BrtSupplierVo updateBrtSupplier(BrtSupplierVo brtSupplierVo);

    /**
     * @description: TODO 批量删除供应商信息
     * @author: lf
     * @date: 2024-06-19
     * @param:
     * @param:  supplierIds 需要删除的供应商信息主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtSupplierBySupplierIds(String[] supplierIds);

    /**
     * @description: TODO 导入供应商数据
     * @author: lf
     * @date: 2024-06-19
     * @param:
     * @param supplierList
     * @param operName
     * @return:
     * @return java.lang.String
     **/
    String importData(List<BrtSupplierVo> supplierList, String operName);

    /**
     * 查询客户的销售记录
     * @param customerId
     * @return
     */
    TableDataInfo<PurchaseRecordVo> queryPurchaseRecord(PurchaseRecordVo purchaseRecordVo);
}
