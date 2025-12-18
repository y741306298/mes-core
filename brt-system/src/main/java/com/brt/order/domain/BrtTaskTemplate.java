package com.brt.order.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.brt.common.annotation.Excel;
import com.brt.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 任务模板对象 brt_task_template
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtTaskTemplate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 模板ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String templateId;

    /**
     * 模板名称
     */
    @Excel(name = "模板名称")
    private String templateName;

    /**
     * 模板类型(API/FUNCTION)
     */
    @Excel(name = "模板类型")
    private String templateType;

    /**
     * 触发模式(AUTO/MANUAL)
     */
    @Excel(name = "触发模式")
    private String triggerMode;

    /**
     * 模板配置(JSON)
     */
    private String config;

    /**
     * 结果状态配置(JSON)
     */
    private String resultStatuses;

    /**
     * 接口类型(SYNC/ASYNC)
     */
    @Excel(name = "接口类型")
    private String interfaceType;

    /**
     * 回调URL
     */
    @Excel(name = "回调URL")
    private String callbackUrl;

    /**
     * 状态(0=正常,1=停用)
     */
    private String status;

    /**
     * 备注
     */
    private String remark;
}
