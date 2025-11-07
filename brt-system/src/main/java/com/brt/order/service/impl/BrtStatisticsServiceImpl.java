package com.brt.order.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.brt.common.utils.StringUtils;
import com.brt.order.domain.BrtCustomer;
import com.brt.order.domain.BrtInInventory;
import com.brt.order.domain.BrtOutInventory;
import com.brt.order.domain.BrtSalesOrder;
import com.brt.order.mapper.BrtStatisticsMapper;
import com.brt.order.service.*;
import com.brt.order.utils.BrtSectionUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class BrtStatisticsServiceImpl implements IBrtStatisticsService {

    @Autowired
    private BrtStatisticsMapper statisticsMapper;

    @Autowired
    private BrtSectionUtil sectionUtil;

    @Autowired
    private IBrtSalesOrderService salesOrderService;

    @Autowired
    private IBrtInInventoryService inInventoryService;

    @Autowired
    private IBrtOutInventoryService outInventoryService;

    @Autowired
    private IBrtCustomerService customerService;

    /**
     * 销售统计
     * @param map
     * @return
     */
    public List<Map<String,Object>> salesStatistics(Map<String,Object> map){
        return statisticsMapper.salesStatistics(map);
    }

    /**
     * 采购统计
     * @param map
     * @return
     */
    public Map<String,Object> marketStatistics(Map<String,Object> map){
        Map<String, Object> marketMap = statisticsMapper.marketStatistics(map);
        Map<String, Object> receivedMap = statisticsMapper.marketStatistics(map);
        List<String> marketList = new LinkedList<>();
        List<String> receivedList = new LinkedList<>();

        marketMap = ObjectUtil.isEmpty(marketMap)?new HashMap<>():marketMap;
        receivedMap = ObjectUtil.isEmpty(receivedMap)?new HashMap<>():receivedMap;

        for(int i = 1 ; i <= 12 ; i++){
            BigDecimal marketVal = new BigDecimal(marketMap.containsKey("" + i)? String.valueOf(marketMap.get("" + i)):"0");
            BigDecimal receivedVal = new BigDecimal(receivedMap.containsKey(""+i)?String.valueOf(receivedMap.get("" + i)):"0");

            marketList.add(marketVal.toString());
            receivedList.add(receivedVal.toString());
        }
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("marketList",marketList);
        resultMap.put("receivedList",receivedList);

        Map<String, Integer> section = sectionUtil.getSection(marketList);
        resultMap.put("marketMax",section.get("max"));
        resultMap.put("marketInterval",section.get("interval"));

        Map<String, Integer> section1 = sectionUtil.getSection(receivedList);
        resultMap.put("receivedMax",section1.get("max"));
        resultMap.put("receivedInterval",section1.get("interval"));

        return resultMap;
    }

    /**
     * 收支年报
     * @param map
     * @return
     */
    public Map<String,Object> earning(Map<String,Object> map){
        //收
        Map<String, Object> earningMap = statisticsMapper.earning(map);
        //支
        Map<String, Object> expendMap = statisticsMapper.expend(map);
        List<String> earningList = new LinkedList<>();
        List<String> expendList = new LinkedList<>();
        for(int i = 1 ; i <= 12 ; i++){
            BigDecimal marketVal = new BigDecimal(String.valueOf(earningMap.get("" + i)));
            BigDecimal receivedVal = new BigDecimal(String.valueOf(expendMap.get("" + i)));

            earningList.add(marketVal.toString());
            expendList.add(receivedVal.toString());
        }
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("earningList",earningList);
        resultMap.put("expendList",expendList);

        Map<String, Integer> section = sectionUtil.getSection(earningList);
        resultMap.put("earningMax",section.get("max"));
        resultMap.put("earningInterval",section.get("interval"));

        Map<String, Integer> section1 = sectionUtil.getSection(expendList);
        resultMap.put("expendMax",section1.get("max"));
        resultMap.put("expendInterval",section1.get("interval"));

        return resultMap;
    }

    /**
     * 各部门完成情况
     * @param map
     * @return
     */
    public Map<String,List<String>> deptOnTime(Map<String,Object> map){
        List<Map<String, Object>> maps = statisticsMapper.deptOnTime(map);
        List<String> deptList = new LinkedList<>();
        List<String> onTimeList = new LinkedList<>();
        List<String> unOnTimeList = new LinkedList<>();
        for(Map<String,Object> m : maps){
            deptList.add(String.valueOf(m.get("deptName")));
            onTimeList.add(String.valueOf(m.get("onTime")));
            unOnTimeList.add(String.valueOf(m.get("unOnTime")));
        }
        Map<String,List<String>> resultMap = new HashMap<>();
        resultMap.put("deptList",deptList);
        resultMap.put("onTimeList",onTimeList);
        resultMap.put("unOnTimeList",unOnTimeList);
        return resultMap;
    }

    public Map<String,String> getHederData(){
        Map<String,String> map = new HashMap<>();
        String orderNum = this.statisticsMapper.getOrderNum();
        String noDelivery = this.statisticsMapper.getNoDelivery();
        String punctualityDelivery = this.statisticsMapper.getPunctualityDelivery();
        map.put("orderNum", StringUtils.isEmpty(orderNum)?"0":orderNum);
        map.put("noDelivery", StringUtils.isEmpty(noDelivery)?"0":noDelivery);
        map.put("punctualityDelivery", StringUtils.isEmpty(punctualityDelivery)?"0":punctualityDelivery);
        return map;
    }


    public Map<String,Object> picking(String orderId){
        List<Map<String, Object>> picking = this.statisticsMapper.picking(orderId);
        if(ObjectUtil.isEmpty(picking)||ObjectUtil.isEmpty(picking.get(0))){
            picking = new LinkedList<>();
        }
        BrtSalesOrder salesOrder = salesOrderService.getById(orderId);
        Map<String,Object> map = new HashMap<>();
        map.put("orderNo",salesOrder.getOrderNo());
        map.put("dataDate",new Date());
        map.put("list",picking);
        String customerId = salesOrder.getCustomerId();
        BrtCustomer customer = customerService.getById(customerId);
        if(ObjectUtil.isNotEmpty(customer)&&StringUtils.isNotEmpty(customer.getCustomerName())){
            map.put("customerName",customer.getCustomerName());
        }
        return map;
    }

    public Map<String,Object> inInventory(String inInventoryId){
        List<Map<String, Object>> picking = this.statisticsMapper.inInventory(inInventoryId);
        if(ObjectUtil.isEmpty(picking)||ObjectUtil.isEmpty(picking.get(0))){
            picking = new LinkedList<>();
        }
        BrtInInventory inInventory = inInventoryService.getById(inInventoryId);
        Map<String,Object> map = new HashMap<>();
        map.put("orderNo",inInventory.getInInventoryNo());
        map.put("list",picking);
        map.put("dataDate",new Date());

        return map;
    }

    public Map<String,Object> outInventory(String outInventoryId){
        List<Map<String, Object>> picking = this.statisticsMapper.outInventory(outInventoryId);
        if(ObjectUtil.isEmpty(picking)||ObjectUtil.isEmpty(picking.get(0))){
            picking = new LinkedList<>();
        }
        BrtOutInventory byId = outInventoryService.getById(outInventoryId);
        Map<String,Object> map = new HashMap<>();
        map.put("orderNo",byId.getOutInventoryNo());
        map.put("list",picking);
        map.put("dataDate",new Date());

        return map;
    }

    public Map<String,Object> productInInventory(String orderId){
        List<Map<String, Object>> picking = this.statisticsMapper.productInInventory(orderId);
        if(ObjectUtil.isEmpty(picking)||ObjectUtil.isEmpty(picking.get(0))){
            picking = new LinkedList<>();
        }
        BrtSalesOrder salesOrder = salesOrderService.getById(orderId);
        Map<String,Object> map = new HashMap<>();
        map.put("orderNo",salesOrder.getOrderNo());
        map.put("list",picking);
        map.put("dataDate",new Date());

        return map;
    }

    public Map<String,Object> firstarticle(String orderId){
        List<Map<String, Object>> picking = this.statisticsMapper.productInInventory(orderId);
        if(ObjectUtil.isEmpty(picking)||ObjectUtil.isEmpty(picking.get(0))){
            picking = new LinkedList<>();
        }
        Map<String, Object> stringObjectMap = picking.get(0);
        String orderNo = String.valueOf(stringObjectMap.get("orderNo"));
        stringObjectMap.put("orderNo",orderNo.replace("XS","AP"));
        return stringObjectMap;
    }

    public Map<String,Object> product(String orderId){
        List<Map<String, Object>> picking = this.statisticsMapper.productInInventory(orderId);
        if(ObjectUtil.isEmpty(picking)||ObjectUtil.isEmpty(picking.get(0))){
            picking = new LinkedList<>();
        }
        Map<String, Object> stringObjectMap = picking.get(0);
        String orderNo = String.valueOf(stringObjectMap.get("orderNo"));
        stringObjectMap.put("orderNo",orderNo.replace("XS","AP"));
        return stringObjectMap;
    }

}
