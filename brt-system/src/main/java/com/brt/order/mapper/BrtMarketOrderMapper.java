package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtMarketOrder;
import com.brt.order.vo.BrtMarketOrderVo;
import com.brt.order.vo.BrtSalesOrderVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采购单Mapper接口
 *
 * @author Fgn
 * @date 2024-05-09
 */
public interface BrtMarketOrderMapper extends BaseMapper<BrtMarketOrder> {

    /**
     * @description: TODO 分页查询采购单列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: page
     * @param: brtMarketOrderVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtMarketOrderVo> queryBrtMarketOrderList(Page<?> page, @Param("brtMarketOrderVo") BrtMarketOrderVo brtMarketOrderVo);

    /**
     * @description: TODO 查询采购单列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtMarketOrderVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtMarketOrderVo> queryBrtMarketOrderList(@Param("brtMarketOrderVo") BrtMarketOrderVo brtMarketOrderVo);

    /**
     * @description: TODO 根据orderId查询采购单
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @return:
     * @return Vo
     **/
    BrtMarketOrderVo queryBrtMarketOrderByOrderId(@Param("OrderId") String orderId);

    /**
     * 修改状态
     * @param brtMarketOrderVo
     */
    void updateStatus(@Param("brtMarketOrderVo") BrtMarketOrderVo brtMarketOrderVo);

    /**
     * 查询客户的已送货总数量
     * @param supplierId
     * @return
     */
    String selectReceivingNum(@Param("supplierId")String supplierId);

    /**
     * 查询客户的已送货总数量
     * @param orderTemplateId
     * @return
     */
    BrtMarketOrderVo getOrderByTemplateId(@Param("orderTemplateId")String orderTemplateId);
}
