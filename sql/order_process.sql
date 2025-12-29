-- ----------------------------
-- Table structure for brt_task_template
-- ----------------------------
DROP TABLE IF EXISTS `brt_common_call_record`;
CREATE TABLE `brt_common_call_record` (
  `record_id` varchar(64) NOT NULL COMMENT '记录ID',
  `interface_name` varchar(128) NOT NULL COMMENT '接口名称',
  `request_path` varchar(512) DEFAULT NULL COMMENT '目标路径',
  `callback_url` varchar(512) DEFAULT NULL COMMENT '回调地址',
  `request_payload` longtext COMMENT '请求报文',
  `callback_payload` longtext COMMENT '回调报文',
  `status` varchar(32) DEFAULT NULL COMMENT '状态',
  `error_message` varchar(512) DEFAULT NULL COMMENT '错误信息',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='通用接口调用记录';

-- ----------------------------
DROP TABLE IF EXISTS `brt_task_template`;
CREATE TABLE `brt_task_template` (
  `template_id` varchar(64) NOT NULL COMMENT '模板ID',
  `template_name` varchar(200) NOT NULL COMMENT '模板名称',
  `template_type` varchar(32) NOT NULL DEFAULT 'API' COMMENT '模板类型(API/FUNCTION)',
  `trigger_mode` varchar(32) NOT NULL DEFAULT 'AUTO' COMMENT '触发模式(AUTO/MANUAL)',
  `config` longtext COMMENT '模板配置(JSON)',
  `result_statuses` longtext COMMENT '结果状态配置(JSON)',
  `interface_type` varchar(16) NOT NULL DEFAULT 'SYNC' COMMENT '接口类型(SYNC/ASYNC)',
  `callback_url` varchar(512) DEFAULT NULL COMMENT '回调URL',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态(0=正常,1=停用)',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='任务模板';

-- ----------------------------
-- brt_order_node字段补充
-- ----------------------------
ALTER TABLE `brt_order_node`
    ADD COLUMN IF NOT EXISTS `oper_setting` varchar(32) DEFAULT NULL COMMENT '操作设置(0=生成收货单,1=生成送货单,2=减库存,3=加库存)' AFTER `sort`;

ALTER TABLE `brt_order_node`
    ADD COLUMN IF NOT EXISTS `trigger_mode` varchar(32) DEFAULT 'MANUAL' COMMENT '触发方式(AUTO=自动触发,MANUAL=人工触发)' AFTER `oper_setting`;

ALTER TABLE `brt_order_node`
    ADD COLUMN `interface_type` varchar(16) NOT NULL DEFAULT 'SYNC' COMMENT '接口类型(SYNC/ASYNC)' AFTER `trigger_mode`;

ALTER TABLE `brt_order_node`
    ADD COLUMN `callback_url` varchar(512) DEFAULT NULL COMMENT '回调URL' AFTER `interface_type`;

ALTER TABLE `brt_flow_node`
    ADD COLUMN `interface_type` varchar(16) NOT NULL DEFAULT 'SYNC' COMMENT '接口类型(SYNC/ASYNC)' AFTER `other_setting`;

ALTER TABLE `brt_flow_node`
    ADD COLUMN `callback_url` varchar(512) DEFAULT NULL COMMENT '回调URL' AFTER `interface_type`;
