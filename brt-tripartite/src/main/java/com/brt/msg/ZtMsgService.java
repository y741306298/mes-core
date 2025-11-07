package com.brt.msg;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.brt.common.constant.CacheConstants;
import com.brt.common.core.redis.RedisCache;
import com.brt.common.exception.ServiceException;
import com.brt.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @BelongsProject: Brt
 * @BelongsPackage: com.brt.msg
 * @Author: FanGN
 * @CreateTime: 2023/6/12 11:33
 * @Description: TODO 组通短信
 * @Version: 1.0
 */
@Service
@Slf4j
public class ZtMsgService {

    @Value("${zt.userName}")
    private String userName;

    @Value("${zt.passWord}")
    private String passWord;

    @Value("${zt.signature}")
    private String signature;

    @Value("${zt.templateID}")
    private String templateID;

    @Value("${zt.url.template}")
    private String templateUrl;

    @Autowired
    private RedisCache redisCache;

    /**
     * @param phones 手机号（多个逗号分割）
     * @description: 模板发送
     * @author: FanGN
     * @date: 2023/4/7 14:51
     * @return: java.lang.String
     **/
    public JSONObject sendTemplateMsg(String phones) {
        //请求入参
        JSONObject requestJson = new JSONObject();

        //账号
        requestJson.put("username", userName);

        //tKey
        long tKey = System.currentTimeMillis() / 1000;
        requestJson.put("tKey", tKey);

        //明文密码
        requestJson.put("password", SecureUtil.md5(SecureUtil.md5(passWord) + tKey));

        //模板ID
        requestJson.put("tpId", templateID);

        //签名
        requestJson.put("signature", signature);

        //扩展号
        requestJson.put("ext", "");

        //自定义参数
        requestJson.put("extend", "");

        //发送记录集合
        JSONArray records = new JSONArray();

        //获取发送的手机号列表
        String[] phoneArr = phones.split(",");
        for (String phone : phoneArr) {
            //生成验证码
            String validCode = getValidCode();

            //替换变量
            JSONObject param = new JSONObject();
            param.put("valid_code", validCode);

            JSONObject record = new JSONObject();
            record.put("mobile", phone);
            record.put("tpContent", param);
            records.add(record);

            log.info("{}发送验证码：{}", phone, validCode);
            redisCache.setCacheObject(CacheConstants.ZT_MSG + phone, validCode, 1, TimeUnit.MINUTES);
        }
        requestJson.put("records", records);

        String result = HttpRequest.post(templateUrl)
                .timeout(60000)
                .body(requestJson.toJSONString(), MediaType.APPLICATION_JSON_UTF8_VALUE).execute().body();
        log.info(result);
        return JSONObject.parseObject(result);
    }

    /**
     * @param phone
     * @description: 校验验证码
     * @author: FanGN
     * @date: 2023/4/7 15:07
     * @return: boolean
     **/
    public boolean checkValidCode(String phone, String userValidCode) {
        //从缓存中获取验证码
        String validCode = redisCache.getCacheObject(CacheConstants.ZT_MSG + phone);
        log.info("{}校验验证码：{}，{}", phone, userValidCode, validCode);

        if (StringUtils.isEmpty(validCode)) {
            throw new ServiceException("验证码已失效");

        } else if (!validCode.equals(userValidCode)) {
            throw new ServiceException("验证码输入有误");
        }
        //删除缓存验证码 防止重复使用
        redisCache.deleteObject(CacheConstants.ZT_MSG + phone);
        return true;
    }

    /**
     * @param
     * @description: 生成短信验证码
     * @author: FanGN
     * @date: 2023/4/7 15:04
     * @return: java.lang.String
     **/
    public static String getValidCode() {
        Random randObj = new Random();
        return Integer.toString(100000 + randObj.nextInt(900000));
    }
}
