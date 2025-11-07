package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtSalesOrder;
import com.brt.order.vo.BrtPriceSheetOrderVo;
import com.brt.order.vo.BrtSalesOrderVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 销售单Mapper接口
 *
 * @author Fgn
 * @date 2024-05-09
 */
public interface BrtSalesOrderMapper extends BaseMapper<BrtSalesOrder> {

    /**
     * @description: TODO 分页查询销售单列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: page
     * @param: brtSalesOrderVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtSalesOrderVo> queryBrtSalesOrderList(Page<?> page, @Param("brtSalesOrderVo") BrtSalesOrderVo brtSalesOrderVo);

    /**
     * @description: TODO 查询销售单列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtSalesOrderVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtSalesOrderVo> queryBrtSalesOrderList(@Param("brtSalesOrderVo") BrtSalesOrderVo brtSalesOrderVo);

    /**
     * @description: TODO 根据orderId查询销售单
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @return:
     * @return Vo
     **/
    BrtSalesOrderVo queryBrtSalesOrderByOrderId(@Param("OrderId") String orderId);


    /**
     * 修改状态
     * @param brtSalesOrderVo
     */
    void updateStatus(@Param("brtSalesOrderVo") BrtSalesOrderVo brtSalesOrderVo);

    /**
     * 查询客户的已送货总数量
     * @param customerId
     * @return
     */
    String selectDeliveryNum(@Param("customerId")String customerId);

    /**
     * 更加流程ID查询订单
     * @param orderTemplateId
     * @return
     */
    BrtSalesOrderVo getOrderByOrderTemplateId(@Param("orderTemplateId")String orderTemplateId);

}
