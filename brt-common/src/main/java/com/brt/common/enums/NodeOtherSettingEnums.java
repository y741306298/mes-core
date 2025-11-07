package com.brt.common.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public enum NodeOtherSettingEnums {

    生成收货单("0"),
    生成送货单("1"),
    减库存("2"),
    加库存("3")

    ;

    private String code;

    /**
     * 判断是否存在某一个设置
     * @param otherSetting
     * @param nodeOtherSettingEnums
     * @return
     */
    public static boolean isExistence(String otherSetting,NodeOtherSettingEnums nodeOtherSettingEnums){
        if (ObjectUtil.isEmpty(otherSetting)){
            return false;
        }

        List<String> settingList = Arrays.asList(otherSetting.split(","));
        return settingList.contains(nodeOtherSettingEnums.code);
    }
}
