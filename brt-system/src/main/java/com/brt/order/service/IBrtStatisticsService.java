package com.brt.order.service;

import com.brt.order.domain.BrtOutInventory;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IBrtStatisticsService {
    /**
     * 销售统计
     * @param map
     * @return
     */
    List<Map<String,Object>> salesStatistics(@Param("map")Map<String,Object> map);

    /**
     * 采购统计
     * @param map
     * @return
     */
    Map<String,Object> marketStatistics( @Param("map")Map<String,Object> map);

    /**
     * 收支年报
     * @param map
     * @return
     */
    Map<String,Object> earning( @Param("map")Map<String,Object> map);

    /**
     * 各部门完成情况
     * @param map
     * @return
     */
    Map<String,List<String>> deptOnTime( @Param("map")Map<String,Object> map);

    /**
     * 查询首页头部数据
     * @return
     */
    public Map<String,String> getHederData();

    /**
     * 打印领料单
     * @param orderId
     * @return
     */
    Map<String,Object> picking(String orderId);

    /**
     *入库单
     * @param outInventoryId
     * @return
     */
    Map<String,Object> inInventory(String orderId);

    /**
     *出库单
     * @param outInventoryId
     * @return
     */
    Map<String,Object> outInventory(String orderId);

    /**
     * 产品入库单
     * @param outInventoryId
     * @return
     */
    Map<String,Object> productInInventory(String outInventoryId);

    /**
     * 首件确认表
     * @param orderId
     * @return
     */
    Map<String,Object> firstarticle(String orderId);

    /**
     * 首件确认表
     * @param orderId
     * @return
     */
    Map<String,Object> product(String orderId);
}
