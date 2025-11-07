package com.brt.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @BelongsProject: wedding-backstage
 * @BelongsPackage: com.ruoyi.common.utils
 * @Author: FanGN
 * @CreateTime: 2023-03-22  16:21
 * @Description: TODO
 * @Version: 1.0
 */
public class MapUtil {

    /**
     * @description: 转请求参数
     * @author: FanGN
     * @date: 2023/3/22 16:21
     * @param: [map]
     * @return: java.lang.String
     **/
    public static String toParams(Map<String, String> map) {
        StringBuffer sb = new StringBuffer();
        if (map.size() > 0) {
            for (String key : map.keySet()) {
                sb.append(key + "=");
                if (StringUtils.isEmpty(map.get(key))) {
                    sb.append("&");
                } else {
                    String value = map.get(key);
                    try {
                        value = URLEncoder.encode(value, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    sb.append(value + "&");
                }
            }
        }
        return sb.toString();
    }
}
