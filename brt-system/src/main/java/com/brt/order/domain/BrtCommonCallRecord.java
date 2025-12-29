package com.brt.order.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.brt.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 通用接口调用记录
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("brt_common_call_record")
public class BrtCommonCallRecord extends BaseEntity {

    /**
     * 记录ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String recordId;

    /**
     * 接口名称
     */
    private String interfaceName;

    /**
     * 目标路径
     */
    private String requestPath;

    /**
     * 回调地址
     */
    private String callbackUrl;

    /**
     * 请求报文
     */
    private String requestPayload;

    /**
     * 回调报文
     */
    private String callbackPayload;

    /**
     * 状态
     */
    private String status;

    /**
     * 错误信息
     */
    private String errorMessage;
}

