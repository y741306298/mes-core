package com.brt.common.constant;

/**
 * 缓存的key 常量
 * 
 * @author ruoyi
 */
public class CacheConstants
{
    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 限流 redis key
     */
    public static final String RATE_LIMIT_KEY = "rate_limit:";

    /**
     * 登录账户密码错误次数 redis key
     */
    public static final String PWD_ERR_CNT_KEY = "pwd_err_cnt:";

    /**
     * 组通短信验证码Key
     */
    public static final String ZT_MSG = "zt_msg:valid_code:";

    /**
     * 邀请码编号 redis key
     */
    public static final String INVITE_CODE = "user:invitationCodeNum";

    /**
     * 登录Token redis key
     */
    public static final String USER_TOKEN = "user_token:";

    /**
     * 拍卖会场次结束 key
     */
    public static final String AUCTION_SESSION = "auction:session:";

    /**
     * 拍卖会场次支付结束 key
     */
    public static final String AUCTION_SESSION_PAY = "pay:auction:session:";

    /**
     * 拍卖会场次申诉结束 key
     */
    public static final String AUCTION_SESSION_APPEAL = "appeal:auction:session:";
}
