package com.brt.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NodeTypeEnums {

    审批("0","审批"),
    开票金额纪录任务("1","开票金额记录任务"),
    收款金额纪录任务("2","收款金额纪录任务"),
    状态纪录任务("3","状态纪录任务"),
    数量记录任务("4","数量记录任务"),
    自定义纪录任务("5","自定义纪录任务"),
    产品纪录任务("6","产品纪录任务"),
    子流程记录任务("7","子流程记录任务");

    private String code;
    private String type;
}
