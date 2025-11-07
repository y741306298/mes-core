package com.brt.order.utils;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BrtSectionUtil {

    public Map<String,Integer> getSection(int [] arr){
        int maxVal = 500;
        for(int v:arr){
            maxVal = v>maxVal?v:maxVal;
        }
        int max = ((maxVal/500)+1)*500;
        int interval = max/5;
        Map<String,Integer> resrulMap= new HashMap<>();
        resrulMap.put("max",max);
        resrulMap.put("interval",interval);
        return resrulMap;
    }

    public Map<String,Integer> getSection(List<String> strList){
        int[] arr = new int[strList.size()];
        for(int i = 0 ; i < strList.size() ; i++){
            arr[i] = new BigDecimal(strList.get(i)).intValue();
        }
        return getSection(arr);
    }


}
