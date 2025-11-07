SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for prod_device
-- ----------------------------
DROP TABLE IF EXISTS `prod_device`;
CREATE TABLE `prod_device` (
  `device_id` varchar(32) NOT NULL COMMENT '设备ID',
  `asset_number` varchar(64) NOT NULL COMMENT '资产编号',
  `device_name` varchar(128) NOT NULL COMMENT '设备名称',
  `model` varchar(128) DEFAULT NULL COMMENT '设备型号',
  `category` varchar(128) DEFAULT NULL COMMENT '设备类别',
  `brand` varchar(128) DEFAULT NULL COMMENT '品牌/制造商',
  `serial_number` varchar(128) DEFAULT NULL COMMENT '序列号',
  `workshop` varchar(128) DEFAULT NULL COMMENT '所属车间',
  `location` varchar(128) DEFAULT NULL COMMENT '设备位置',
  `supplier` varchar(128) DEFAULT NULL COMMENT '供应商',
  `purchase_date` date DEFAULT NULL COMMENT '购买日期',
  `start_date` date DEFAULT NULL COMMENT '投入使用日期',
  `value` varchar(64) DEFAULT NULL COMMENT '资产原值',
  `rated_power` varchar(64) DEFAULT NULL COMMENT '额定功率',
  `machining_range` varchar(128) DEFAULT NULL COMMENT '加工范围',
  `voltage` varchar(64) DEFAULT NULL COMMENT '工作电压',
  `air_pressure` varchar(64) DEFAULT NULL COMMENT '气压要求',
  `positioning_accuracy` varchar(64) DEFAULT NULL COMMENT '定位精度',
  `repeatability` varchar(64) DEFAULT NULL COMMENT '重复定位精度',
  `spindle_speed` varchar(64) DEFAULT NULL COMMENT '主轴转速',
  `cnc_system` varchar(64) DEFAULT NULL COMMENT '数控系统',
  `current_status` varchar(64) DEFAULT NULL COMMENT '当前状态',
  `status_color` varchar(32) DEFAULT NULL COMMENT '状态颜色',
  `operator` varchar(64) DEFAULT NULL COMMENT '当前操作员',
  `production_task` varchar(128) DEFAULT NULL COMMENT '当前生产任务',
  `shift` varchar(32) DEFAULT NULL COMMENT '班次',
  `total_runtime` varchar(64) DEFAULT NULL COMMENT '累计运行时间',
  `monthly_runtime` varchar(64) DEFAULT NULL COMMENT '本月运行时间',
  `maintenance_strategy` varchar(255) DEFAULT NULL COMMENT '维护策略',
  `maintenance_cycle` varchar(64) DEFAULT NULL COMMENT '保养周期',
  `last_maintenance` varchar(128) DEFAULT NULL COMMENT '上次保养',
  `next_maintenance` varchar(128) DEFAULT NULL COMMENT '下次保养计划',
  `maintenance_content` varchar(512) DEFAULT NULL COMMENT '保养内容',
  `maintenance_owner` varchar(64) DEFAULT NULL COMMENT '保养负责人',
  `time_availability` varchar(64) DEFAULT NULL COMMENT '时间开动率',
  `performance` varchar(64) DEFAULT NULL COMMENT '性能开动率',
  `quality_rate` varchar(64) DEFAULT NULL COMMENT '合格品率',
  `oee` varchar(64) DEFAULT NULL COMMENT '综合效率(OEE)',
  `planned_time` varchar(64) DEFAULT NULL COMMENT '计划运行时间',
  `actual_runtime` varchar(64) DEFAULT NULL COMMENT '实际运行时间',
  `downtime` varchar(64) DEFAULT NULL COMMENT '故障停机时间',
  `changeover` varchar(64) DEFAULT NULL COMMENT '换模调试时间',
  `monthly_output` varchar(64) DEFAULT NULL COMMENT '本月产量',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生产设备信息';

-- ----------------------------
-- Table structure for prod_device_history
-- ----------------------------
DROP TABLE IF EXISTS `prod_device_history`;
CREATE TABLE `prod_device_history` (
  `history_id` varchar(32) NOT NULL COMMENT '记录ID',
  `device_id` varchar(32) NOT NULL COMMENT '设备ID',
  `maintenance_date` date DEFAULT NULL COMMENT '日期',
  `symptom` varchar(255) DEFAULT NULL COMMENT '故障现象',
  `cause` varchar(255) DEFAULT NULL COMMENT '原因分析',
  `action` varchar(255) DEFAULT NULL COMMENT '处理措施',
  `duration` varchar(64) DEFAULT NULL COMMENT '维修时长',
  `person` varchar(64) DEFAULT NULL COMMENT '维修人',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`history_id`),
  KEY `idx_prod_device_history_device_id` (`device_id`),
  CONSTRAINT `fk_prod_device_history_device_id` FOREIGN KEY (`device_id`) REFERENCES `prod_device` (`device_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生产设备维修保养记录';

SET FOREIGN_KEY_CHECKS = 1;
