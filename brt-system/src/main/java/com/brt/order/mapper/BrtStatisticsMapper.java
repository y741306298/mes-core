package com.brt.order.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BrtStatisticsMapper {
    /**
     * 销售统计
     * @param map
     * @return
     */
    List<Map<String,Object>> salesStatistics( @Param("map")Map<String,Object> map);



    /**
     * 采购统计 采购
     * @param map
     * @return
     */
    Map<String,Object> marketStatistics( @Param("map")Map<String,Object> map);

    /**
     * 采购统计 已收
     * @param map
     * @return
     */
    Map<String,Object> marketStatisticsReceived( @Param("map")Map<String,Object> map);



    /**
     * 收支年报 收
     * @param map
     * @return
     */
    Map<String,Object> earning( @Param("map")Map<String,Object> map);

    /**
     * 收支年报 支
     * @param map
     * @return
     */
    Map<String,Object> expend( @Param("map")Map<String,Object> map);



    /**
     * 各部门完成情况
     * @param map
     * @return
     */
    List<Map<String,Object>> deptOnTime( @Param("map")Map<String,Object> map);

    /**
     * 查询首页订单数量
     * @return
     */
    String getOrderNum();

    /**
     * 查询首页未出货数量
     * @return
     */
    String getNoDelivery();

    /**
     * 查询首页准时发货数量
     * @return
     */
    String getPunctualityDelivery();

    /**\
     * 领料单打印查询
     * @param orderId
     * @return
     */
    List<Map<String,Object>> picking(@Param("orderId")String orderId);

    /**\
     * 入库单打印查询
     * @param inInventoryId
     * @return
     */
    List<Map<String,Object>> inInventory(@Param("inInventoryId")String inInventoryId);

    /**\
     * 出库单打印查询
     * @param outInventoryId
     * @return
     */
    List<Map<String,Object>> outInventory(@Param("outInventoryId")String outInventoryId);

    /**\
     * 产品入库单打印查询
     * @param orderId
     * @return
     */
    List<Map<String,Object>> productInInventory(@Param("orderId")String orderId);
}
