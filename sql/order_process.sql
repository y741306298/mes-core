-- ----------------------------
-- Table structure for brt_task_template
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
