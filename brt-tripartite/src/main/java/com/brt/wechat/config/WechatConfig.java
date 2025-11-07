package com.brt.wechat.config;

/**
 * @BelongsProject: wedding-backstage
 * @BelongsPackage: com.ruoyi.wechat.config
 * @Author: FanGN
 * @CreateTime: 2023-03-22  11:35
 * @Description: TODO
 * @Version: 1.0
 */
public class WechatConfig {
    /***********************支付类型************************/
    //NATIVE--原生支付
    public static final String TRADE_TYPE_NATIVE = "NATIVE";
    //公众号支付-小程序支付
    public static final String TRADE_TYPE_JSAPI = "JSAPI";
    //H5支付
    public static final String TRADE_TYPE_MWEB = "MWEB";
    //App支付
    public static final String TRADE_TYPE_APP = "APP";
}
