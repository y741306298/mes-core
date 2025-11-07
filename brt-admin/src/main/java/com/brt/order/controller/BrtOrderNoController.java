package com.brt.order.controller;

import com.brt.common.annotation.RequestSingleParam;
import com.brt.common.enums.OrderNoEnums;
import com.brt.order.utils.BrtOrderNoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/order/orderNo")
@RestController
public class BrtOrderNoController {

    @Autowired
    private BrtOrderNoUtil brtOrderNoUtil;

    @GetMapping("/getNo")
    public String getNo(String keyName){
        OrderNoEnums noEnumsByKeyName = OrderNoEnums.getNoEnumsByKeyName(keyName);
        return brtOrderNoUtil.getNo(noEnumsByKeyName);
    }

    @GetMapping("/getNoAndAdd")
    public String getNoAndAdd(String keyName){
        OrderNoEnums noEnumsByKeyName = OrderNoEnums.getNoEnumsByKeyName(keyName);
        return brtOrderNoUtil.getNoAndAdd(noEnumsByKeyName);
    }
}
