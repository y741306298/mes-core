package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtSupplier;
import com.brt.order.vo.BrtSupplierVo;
import com.brt.order.vo.MarketRecordVo;
import com.brt.order.vo.PurchaseRecordVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 供应商信息Mapper接口
 *
 * @author lf
 * @date 2024-04-27
 */
public interface BrtSupplierMapper extends BaseMapper<BrtSupplier> {

    /**
     * @description: TODO 分页查询供应商信息列表
     * @author: lf
     * @date: 2024-04-27
     * @param:
     * @param: page
     * @param: brtSupplierVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtSupplierVo> queryBrtSupplierList(Page<?> page, @Param("brtSupplierVo") BrtSupplierVo brtSupplierVo);

    /**
     * @description: TODO 查询供应商信息列表
     * @author: lf
     * @date: 2024-04-27
     * @param:
     * @param: brtSupplierVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtSupplierVo> queryBrtSupplierList(@Param("brtSupplierVo") BrtSupplierVo brtSupplierVo);

    /**
     * @description: TODO 根据supplierId查询供应商信息
     * @author: lf
     * @date: 2024-04-27
     * @param:
     * @return:
     * @return Vo
     **/
    BrtSupplierVo queryBrtSupplierBySupplierId(@Param("supplierId") String supplierId);

    /**
     * 查询客户的销售记录
     * @param customerId
     * @return
     */
    Page<PurchaseRecordVo> queryPurchaseRecord(Page<?> page, @Param("purchaseRecordVo") PurchaseRecordVo purchaseRecordVo);

}
