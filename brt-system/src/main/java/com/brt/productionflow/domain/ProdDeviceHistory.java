package com.brt.productionflow.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.brt.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 生产设备维修保养记录对象 prod_device_history
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("prod_device_history")
public class ProdDeviceHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @TableId(value = "history_id", type = IdType.ASSIGN_ID)
    private String historyId;

    /**
     * 设备ID
     */
    @TableField("device_id")
    private String deviceId;

    /**
     * 日期
     */
    @JsonProperty("date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("maintenance_date")
    private Date maintenanceDate;

    /**
     * 故障现象
     */
    @TableField("symptom")
    private String symptom;

    /**
     * 原因分析
     */
    @TableField("cause")
    private String cause;

    /**
     * 处理措施
     */
    @TableField("action")
    private String action;

    /**
     * 维修时长
     */
    @TableField("duration")
    private String duration;

    /**
     * 维修人
     */
    @TableField("person")
    private String person;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
}
