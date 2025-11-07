package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtPriceSheetOrder;
import com.brt.order.vo.BrtPriceSheetOrderVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 销售单Mapper接口
 *
 * @author Fgn
 * @date 2024-05-09
 */
public interface BrtPriceSheetOrderMapper extends BaseMapper<BrtPriceSheetOrder> {

    /**
     * @description: TODO 分页查询销售单列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: page
     * @param: brtPriceSheetOrderVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtPriceSheetOrderVo> queryBrtPriceSheetOrderList(Page<?> page, @Param("brtPriceSheetOrderVo") BrtPriceSheetOrderVo brtPriceSheetOrderVo);

    /**
     * @description: TODO 查询销售单列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtPriceSheetOrderVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtPriceSheetOrderVo> queryBrtPriceSheetOrderList(@Param("brtPriceSheetOrderVo") BrtPriceSheetOrderVo brtPriceSheetOrderVo);

    /**
     * @description: TODO 根据orderId查询销售单
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @return:
     * @return Vo
     **/
    BrtPriceSheetOrderVo queryBrtPriceSheetOrderByOrderId(@Param("OrderId") String orderId);

    /**
     * 修改是否可报价
     * @param map
     */
    void updatePriceSheet(@Param("map")Map<String,Object> map);

    /**
     * 销售单导入报价单 查询报价单列表
     * @param orderNo
     * @return
     */
    List<Map<String,Object>> exportPriceList(@Param("orderNo")String orderNo);

    /**
     * 修改状态
     * @param brtPriceSheetOrderVo
     */
    void updateStatus(@Param("brtPriceSheetOrderVo") BrtPriceSheetOrderVo brtPriceSheetOrderVo);

    /**
     * 根据流程ID查询订单
     * @param orderTemplateId
     * @return
     */
    BrtPriceSheetOrderVo getOrderByOrderTemplateId(@Param("orderTemplateId")String orderTemplateId);

}
