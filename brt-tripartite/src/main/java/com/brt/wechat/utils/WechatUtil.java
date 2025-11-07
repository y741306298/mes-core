package com.brt.wechat.utils;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.brt.common.utils.MapUtil;
import com.brt.common.utils.http.HttpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @BelongsProject: wedding-backstage
 * @BelongsPackage: com.ruoyi.wechat.utils
 * @Author: FanGN
 * @CreateTime: 2023-03-22  16:09
 * @Description: TODO
 * @Version: 1.0
 */
@Component
public class WechatUtil {

    @Value("${wechat.appId}")
    private String appId;

    @Value("${wechat.appSecret}")
    private String appSecret;

    @Value("${wechat.accessTokenUrl}")
    private String accessTokenUrl;

    @Value("${wechat.openIdUrl}")
    private String openIdUrl;

    @Value("${wechat.phoneUrl}")
    private String phoneUrl;

    /**
     * 获取小程序全局唯一后台接口调用凭据（access_token）
     * @return
     */
    public JSONObject getAccessToken(){
        Map<String, String> parameters = new HashMap<>();
        parameters.put("grant_type","client_credential");
        parameters.put("appid",appId);
        parameters.put("secret",appSecret);
        String s = HttpUtils.sendGet(accessTokenUrl, MapUtil.toParams(parameters));
        JSONObject object = JSON.parseObject(s);
        return object;
    }

    /**
     * @description: 获取OpenId
     * @author: FanGN
     * @date: 2023/3/22 16:50
     * @param: [code]
     * @return: com.alibaba.fastjson2.JSONObject
     **/
    public JSONObject getOpenId(String code){
        Map<String, String> parameters = new HashMap<>();
        parameters.put("appid",appId);
        parameters.put("secret",appSecret);
        parameters.put("js_code",code);
        parameters.put("grant_type","authorization_code");
        String s = HttpUtils.sendGet(openIdUrl, MapUtil.toParams(parameters));
        JSONObject object = JSON.parseObject(s);
        return object;
    }

    public JSONObject getUniodId(String code){
        Map<String, String> parameters = new HashMap<>();
        parameters.put("appid",appId);
        parameters.put("secret",appSecret);
        parameters.put("js_code",code);
        parameters.put("grant_type","authorization_code");
        String s = HttpUtils.sendGet(openIdUrl, MapUtil.toParams(parameters));
        JSONObject object = JSON.parseObject(s);
        return object;
    }

    /**
     * @description: 获取手机号
     * @author: FanGN
     * @date: 2023/3/22 16:49
     * @param: [code]
     * @return: java.lang.Object
     **/
    public Object getPhoneNumber(String code) {
        String result = null;
        try {
            JSONObject accessToken = getAccessToken();
            String access_token = accessToken.getString("access_token");

            String replaceUrlTwo = phoneUrl.replace("{0}",access_token);
            HashMap<String, Object> requestParam = new HashMap<>();
            // 手机号调用凭证
            requestParam.put("code", code);
            String jsonStr = JSON.toJSONString(requestParam);
            HttpResponse response = HttpRequest.post(replaceUrlTwo)
                    .header(Header.CONTENT_ENCODING, "UTF-8")
                    // 发送json数据需要设置contentType
                    .header(Header.CONTENT_TYPE, "application/x-www-form-urlencoded")
                    .body(jsonStr)
                    .execute();
            if (response.getStatus() == HttpStatus.HTTP_OK) {
                result = response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSONObject.parseObject(result).get("phone_info");
    }

}
