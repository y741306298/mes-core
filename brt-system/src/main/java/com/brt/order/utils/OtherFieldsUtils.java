package com.brt.order.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.util.TypeUtils;
import com.brt.common.utils.StringUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Component
public class OtherFieldsUtils {

    public <T> List<T> rinse(List<T> list,Class aClass) {
        try {
            Method getOtherFields = aClass.getMethod("getOtherFields");
            Method setOtherFields = aClass.getMethod("setOtherFields", Class.forName("java.lang.String"));

            for(T item:list){
                Object invoke = getOtherFields.invoke(item);
                String val = TypeUtils.cast(invoke,String.class);
                if(StringUtils.isEmpty(val)||"[]".equals(val)){
                    setOtherFields.invoke(item,"");
                    continue;
                }
                StringBuilder sb = new StringBuilder();
                JSONArray objects = JSONArray.parseArray(val);
                for(int i = 0 ; i < objects.size() ; i++){
                    JSONObject jsonObject = objects.getJSONObject(i);
                    sb.append(jsonObject.getString("fieldName"))
                            .append(":")
                            .append(jsonObject.getString("value"))
                            .append(";  ");
                }
                setOtherFields.invoke(item,sb.toString());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }
}
