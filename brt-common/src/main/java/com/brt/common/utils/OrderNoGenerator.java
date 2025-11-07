package com.brt.common.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Date;
import java.util.Random;

/**
 * @BelongsProject: wedding-backstage
 * @BelongsPackage: com.ruoyi.common.utils
 * @Author: FanGN
 * @CreateTime: 2023-03-22  10:59
 * @Description: TODO 订单编号生成
 * @Version: 1.0
 */
public class OrderNoGenerator {

    public static String getOrderNo(String prefix){
        return prefix+getOrderNo();
    }

    public static String getOrderNo(){
        String time = DateUtils.parseDateToStr(DateUtils.YYYYMMDDHHMMSS,new Date());
        String numeric = RandomStringUtils.randomNumeric(8);
        return time+numeric;
    }

    public static String getStringRandom(int length) {
        String val = "";
        Random random = new Random();

        // 参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
}
