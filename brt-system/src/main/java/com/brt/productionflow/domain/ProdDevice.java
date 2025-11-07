package com.brt.productionflow.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.brt.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 生产设备信息对象 prod_device
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("prod_device")
public class ProdDevice extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 设备ID
     */
    @TableId(value = "device_id", type = IdType.ASSIGN_ID)
    private String deviceId;

    /**
     * 资产编号
     */
    @TableField("asset_number")
    private String assetNumber;

    /**
     * 设备名称
     */
    @TableField("device_name")
    private String deviceName;

    /**
     * 设备型号
     */
    @TableField("model")
    private String model;

    /**
     * 设备类别
     */
    @TableField("category")
    private String category;

    /**
     * 品牌/制造商
     */
    @TableField("brand")
    private String brand;

    /**
     * 序列号
     */
    @TableField("serial_number")
    private String serialNumber;

    /**
     * 所属车间
     */
    @TableField("workshop")
    private String workshop;

    /**
     * 设备位置
     */
    @TableField("location")
    private String location;

    /**
     * 供应商
     */
    @TableField("supplier")
    private String supplier;

    /**
     * 购买日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("purchase_date")
    private Date purchaseDate;

    /**
     * 投入使用日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("start_date")
    private Date startDate;

    /**
     * 资产原值
     */
    @TableField("value")
    private String value;

    /**
     * 额定功率
     */
    @TableField("rated_power")
    private String ratedPower;

    /**
     * 加工范围
     */
    @TableField("machining_range")
    private String machiningRange;

    /**
     * 工作电压
     */
    @TableField("voltage")
    private String voltage;

    /**
     * 气压要求
     */
    @TableField("air_pressure")
    private String airPressure;

    /**
     * 定位精度
     */
    @TableField("positioning_accuracy")
    private String positioningAccuracy;

    /**
     * 重复定位精度
     */
    @TableField("repeatability")
    private String repeatability;

    /**
     * 主轴转速
     */
    @TableField("spindle_speed")
    private String spindleSpeed;

    /**
     * 数控系统
     */
    @TableField("cnc_system")
    private String cncSystem;

    /**
     * 当前状态
     */
    @TableField("current_status")
    private String currentStatus;

    /**
     * 状态颜色
     */
    @TableField("status_color")
    private String statusColor;

    /**
     * 当前操作员
     */
    @TableField("operator")
    private String operator;

    /**
     * 当前生产任务
     */
    @TableField("production_task")
    private String productionTask;

    /**
     * 班次
     */
    @TableField("shift")
    private String shift;

    /**
     * 累计运行时间
     */
    @TableField("total_runtime")
    private String totalRuntime;

    /**
     * 本月运行时间
     */
    @TableField("monthly_runtime")
    private String monthlyRuntime;

    /**
     * 维护策略
     */
    @TableField("maintenance_strategy")
    private String maintenanceStrategy;

    /**
     * 保养周期
     */
    @TableField("maintenance_cycle")
    private String maintenanceCycle;

    /**
     * 上次保养
     */
    @TableField("last_maintenance")
    private String lastMaintenance;

    /**
     * 下次保养计划
     */
    @TableField("next_maintenance")
    private String nextMaintenance;

    /**
     * 保养内容
     */
    @TableField("maintenance_content")
    private String maintenanceContent;

    /**
     * 保养负责人
     */
    @TableField("maintenance_owner")
    private String maintenanceOwner;

    /**
     * 时间开动率
     */
    @TableField("time_availability")
    private String timeAvailability;

    /**
     * 性能开动率
     */
    @TableField("performance")
    private String performance;

    /**
     * 合格品率
     */
    @TableField("quality_rate")
    private String qualityRate;

    /**
     * 综合效率
     */
    @TableField("oee")
    private String oee;

    /**
     * 计划运行时间
     */
    @TableField("planned_time")
    private String plannedTime;

    /**
     * 实际运行时间
     */
    @TableField("actual_runtime")
    private String actualRuntime;

    /**
     * 故障停机时间
     */
    @TableField("downtime")
    private String downtime;

    /**
     * 换模调试时间
     */
    @TableField("changeover")
    private String changeover;

    /**
     * 本月产量
     */
    @TableField("monthly_output")
    private String monthlyOutput;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
}
