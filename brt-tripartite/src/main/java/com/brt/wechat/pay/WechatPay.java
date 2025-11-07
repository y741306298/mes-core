package com.brt.wechat.pay;

import cn.hutool.http.HttpUtil;
import com.brt.common.core.domain.AjaxResult;
import com.brt.common.enums.WechatCallbackEnums;
import com.brt.common.exception.ServiceException;
import com.brt.common.utils.BeanToMap;
import com.brt.common.utils.spring.SpringUtils;
import com.brt.wechat.utils.WechatPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @BelongsProject: wedding-backstage
 * @BelongsPackage: com.ruoyi.wechat.pay
 * @Author: FanGN
 * @CreateTime: 2023-03-22  11:10
 * @Description: TODO
 * @Version: 1.0
 */
@Slf4j
@Component
public class WechatPay {

    @Value("${wechat.appId}")
    private String appId;

    @Value("${wechat.pay.mchId}")
    private String mchId;

    @Value("${wechat.pay.key}")
    private String key;

    @Value("${wechat.pay.unifiedorder}")
    private String unifiedorder;

    @Value("${wechat.pay.callback.domainUrl}")
    private String domainUrl;

    @Value("${wechat.pay.certificatePath}")
    private String certificatePath;

    @Value("${wechat.pay.refundUrl}")
    private String refundUrl;

    @Value("${wechat.pay.transfersUrl}")
    private String transfersUrl;

    /**
     * @description: 微信支付回调处理
     * @author: FanGN
     * @date: 2023/4/7 17:30
     * @param request
     * @param response
     * @param beanName
     * @return: void
     **/
    public void wechatNotify(HttpServletRequest request, HttpServletResponse response,String beanName) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        InputStream inputStream = request.getInputStream();
        //获取请求输入流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        outputStream.close();
        inputStream.close();
        Map<String, Object> map = BeanToMap.getMapFromXML(new String(outputStream.toByteArray(), "utf-8"));
        log.info("【小程序支付回调】 回调数据： \n" + map);
        StringBuffer resXml = new StringBuffer();
        String returnCode = (String) map.get("return_code");
        if ("SUCCESS".equalsIgnoreCase(returnCode)) {
            String returnmsg = (String) map.get("result_code");
            if ("SUCCESS".equals(returnmsg)) {
                //支付成功
                resXml.append("<xml>");
                resXml.append("<return_code><![CDATA[SUCCESS]]></return_code>");
                resXml.append("<return_msg><![CDATA[OK]]></return_msg>");
                resXml.append("</xml>");
                response.getWriter().print(resXml);

                //处理支付完成后的逻辑
                Object t = SpringUtils.getClass(beanName);
                Method callbackLogic = t.getClass().getMethod("callbackLogic", new Class[]{Map.class});
                callbackLogic.invoke(t,map);

                return;
            } else {
                resXml.append("<xml>");
                resXml.append("<return_code><![CDATA[FAIL]]></return_code>");
                resXml.append("<return_msg><![CDATA[报文为空]></return_msg>");
                resXml.append("</xml>");
                log.info("支付失败:" + resXml);
            }
        } else {
            resXml.append("<xml>");
            resXml.append("<return_code><![CDATA[FAIL]]></return_code>");
            resXml.append("<return_msg><![CDATA[报文为空]></return_msg>");
            resXml.append("</xml>");
            log.info("【订单支付失败】");
        }
        log.info("【小程序支付回调响应】 响应内容：\n" + resXml);
        response.getWriter().print(resXml);
    }

    /**
     * @description: 微信支付
     * @author: FanGN
     * @date: 2023/3/22 12:14
     * @param: [orderNum, tradeType, payAmount, openid, body, callbackEnums]
     * @return: java.util.SortedMap<java.lang.String,java.lang.String>
     **/
    public SortedMap<String, String> toPay(String orderNum, String tradeType, double payAmount, String openid,String body, WechatCallbackEnums callbackEnums) throws Exception{

        SortedMap<String, String> resultMap = new TreeMap<String, String>();

        //获取支付响应信息
        Map<String, String> responseMap = xcxUnifieldOrder(orderNum,tradeType,payAmount,openid,body,callbackEnums);
        if("SUCCESS".equals(responseMap.get("return_code")) && "SUCCESS".equals(responseMap.get("result_code"))){
            resultMap.put("appId", appId);
            resultMap.put("timeStamp", WechatPayUtil.getCurrentTimeStamp());
            resultMap.put("nonceStr", WechatPayUtil.makeUUID(32));
            resultMap.put("package", "prepay_id="+responseMap.get("prepay_id"));
            resultMap.put("signType", "MD5");
            resultMap.put("sign", WechatPayUtil.createSign(resultMap,key));
            resultMap.put("returnCode", "SUCCESS");
            resultMap.put("returnMsg", AjaxResult.SUCCESS);
            log.info("【小程序支付】统一下单成功，返回参数:"+resultMap);
            return resultMap;
        }else {
            resultMap.put("returnCode", responseMap.get("return_code"));
            resultMap.put("returnMsg", responseMap.get("return_msg"));
            log.info("【小程序支付】统一下单失败，失败原因:"+responseMap.get("return_msg"));
            throw new ServiceException("获取订单信息失败");
        }
    }

    private Map<String, String> xcxUnifieldOrder(String orderNum, String tradeType, double payAmount, String openid,String body, WechatCallbackEnums callbackEnums) throws Exception{
        //封装参数
        SortedMap<String,String> paramMap = new TreeMap<String,String>();
        paramMap.put("appid", appId);
        paramMap.put("mch_id", mchId);
        paramMap.put("nonce_str", WechatPayUtil.makeUUID(32));
        paramMap.put("body", body);
        paramMap.put("out_trade_no", orderNum);
        paramMap.put("total_fee", WechatPayUtil.moneyToIntegerStr(payAmount));
        paramMap.put("spbill_create_ip", WechatPayUtil.getLocalIp());
        paramMap.put("notify_url", domainUrl+callbackEnums.getPayUrl());
        paramMap.put("trade_type", tradeType);
        paramMap.put("openid",openid);
        paramMap.put("sign", WechatPayUtil.createSign(paramMap,key));
        //转换为xml
        String xmlData = WechatPayUtil.mapToXml(paramMap);
        //请求微信后台，获取预支付ID
        String resXml = HttpUtil.post(unifiedorder, xmlData);
        log.info("【小程序支付】 统一下单响应：\n"+resXml);
        return WechatPayUtil.xmlStrToMap(resXml);
    }

    /**
     * @description: 微信退款
     * @author: FanGN
     * @date: 2023/4/7 21:47
     * @param orderNum
     * @param transactionId
     * @param refundAmount
     * @param callbackEnums
     * @return: java.util.Map<java.lang.String,java.lang.String>
     **/
    public Map<String, String> toRefund(String orderNum,String transactionId, double refundAmount, WechatCallbackEnums callbackEnums) throws Exception {
        //封装参数
        SortedMap<String,String> paramMap = new TreeMap<String,String>();
        paramMap.put("appid", appId);
        paramMap.put("mch_id", mchId);
        paramMap.put("nonce_str", WechatPayUtil.makeUUID(32));
        paramMap.put("notify_url", domainUrl+callbackEnums.getRefundUrl());
        paramMap.put("out_refund_no", orderNum);
        paramMap.put("out_trade_no", orderNum);
        paramMap.put("refund_fee", WechatPayUtil.moneyToIntegerStr(refundAmount));
        paramMap.put("total_fee", WechatPayUtil.moneyToIntegerStr(refundAmount));

        paramMap.put("sign", WechatPayUtil.createSign(paramMap,key));

        //转换为xml
        String xmlData = WechatPayUtil.mapToXml(paramMap);
        log.info("调试模式_退款接口 请求XML数据：{}", xmlData);

        String result = payOfCertificate(refundUrl,xmlData);
        log.info("------退款回执信息{}-------" + result);

        Map<String, String> xmlStrToMap = WechatPayUtil.xmlStrToMap(result);
        log.info("----订单退款-----回执数据：{}", xmlStrToMap);
        return xmlStrToMap;
    }

    /**
     * @description: 微信提现
     * @author: FanGN
     * @date: 2023/4/10 9:56
     * @param openId
     * @param partnerTradeNo
     * @param money
     * @param name
     * @param desc
     * @return: java.util.Map<java.lang.String,java.lang.String>
     **/
    public Map<String, String> toTransfers(String openId, String partnerTradeNo, BigDecimal money, String name, String desc) throws Exception {
        SortedMap<String,String> paramMap = new TreeMap<String,String>();
        paramMap.put("mch_appid", appId);
        paramMap.put("mchid", mchId);
        paramMap.put("partner_trade_no", partnerTradeNo);
        paramMap.put("openid", openId);
        paramMap.put("check_name", "FORCE_CHECK");
        paramMap.put("re_user_name", name);
        paramMap.put("nonce_str", WechatPayUtil.makeUUID(32));
        paramMap.put("amount", money.toString());
        paramMap.put("desc", desc);
        paramMap.put("spbill_create_ip", WechatPayUtil.getLocalIp());
        paramMap.put("sign", WechatPayUtil.createSign(paramMap,key));
        //转换为xml
        String xmlData = WechatPayUtil.mapToXml(paramMap);
        log.info("调试模式_提现接口 请求XML数据：{}", xmlData);

        String result = payOfCertificate(transfersUrl,xmlData);
        log.info("------提现回执信息{}-------" + result);

        Map<String, String> xmlStrToMap = WechatPayUtil.xmlStrToMap(result);
        log.info("----提现-----回执数据：{}", xmlStrToMap);
        return xmlStrToMap;

    }

    /**
     * @description: 加载证书 发送请求
     * @author: FanGN
     * @date: 2023/4/7 17:19
     * @param data
     * @return: java.lang.String
     **/
    public String payOfCertificate(String url,String data) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        ClassPathResource cpr = new ClassPathResource(certificatePath);
        InputStream is = cpr.getInputStream();
        try {
            keyStore.load(is, mchId.toCharArray());
        } finally {
            is.close();
        }
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mchId.toCharArray()).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[]{"TLSv1.2"},
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER
        );
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        try {
            HttpPost httpost = new HttpPost(url); // 设置响应头信息
            httpost.addHeader("Connection", "keep-alive");
            httpost.addHeader("Accept", "*/*");
            httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpost.addHeader("Host", "api.mch.weixin.qq.com");
            httpost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpost.addHeader("Cache-Control", "max-age=0");
            httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            httpost.setEntity(new StringEntity(data, "UTF-8"));
            CloseableHttpResponse response = httpclient.execute(httpost);
            try {
                HttpEntity entity = response.getEntity();
                String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                EntityUtils.consume(entity);
                return jsonStr;
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }

}
