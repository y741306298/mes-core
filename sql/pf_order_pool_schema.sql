-- 订单池与生产流建表语句

CREATE TABLE IF NOT EXISTS `pf_order_pool` (
  `order_id` varchar(64) NOT NULL COMMENT '订单编号',
  `preview_image` varchar(512) DEFAULT NULL COMMENT '预览图地址',
  `quantity` int NOT NULL COMMENT '数量',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `main_material` varchar(128) DEFAULT NULL COMMENT '主材料',
  `craft_requirements` varchar(1024) DEFAULT NULL COMMENT '工艺要求',
  `order_status` varchar(32) NOT NULL COMMENT '订单状态',
  `template_id` varchar(32) NOT NULL COMMENT '流程模板ID',
  `customer_info` varchar(255) DEFAULT NULL COMMENT '客户信息',
  `priority` varchar(16) NOT NULL COMMENT '优先级',
  `delivery_date` datetime DEFAULT NULL COMMENT '交付日期',
  `size_requirement` varchar(255) DEFAULT NULL COMMENT '尺寸要求',
  `color_requirement` varchar(255) DEFAULT NULL COMMENT '颜色要求',
  `file_format` varchar(64) DEFAULT NULL COMMENT '文件格式',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`order_id`),
  KEY `idx_order_template` (`template_id`),
  CONSTRAINT `fk_order_template` FOREIGN KEY (`template_id`) REFERENCES `brt_flow_template` (`template_id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生产流订单池';

CREATE TABLE IF NOT EXISTS `pf_production_flow` (
  `flow_id` varchar(64) NOT NULL COMMENT '生产流ID',
  `flow_status` varchar(32) NOT NULL COMMENT '生产状态',
  `total_quantity` int NOT NULL COMMENT '总数量',
  `priority` varchar(16) NOT NULL COMMENT '优先级',
  `template_id` varchar(32) NOT NULL COMMENT '流程模板ID',
  `scheduled_start` datetime DEFAULT NULL COMMENT '计划开始时间',
  `scheduled_end` datetime DEFAULT NULL COMMENT '计划结束时间',
  `actual_start` datetime DEFAULT NULL COMMENT '实际开始时间',
  `actual_end` datetime DEFAULT NULL COMMENT '实际结束时间',
  `assigned_operator` varchar(64) DEFAULT NULL COMMENT '负责人',
  `production_notes` varchar(1024) DEFAULT NULL COMMENT '生产备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`flow_id`),
  KEY `idx_flow_template` (`template_id`),
  CONSTRAINT `fk_flow_template` FOREIGN KEY (`template_id`) REFERENCES `brt_flow_template` (`template_id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生产流';

CREATE TABLE IF NOT EXISTS `pf_production_flow_material` (
  `material_id` bigint NOT NULL AUTO_INCREMENT COMMENT '材料ID',
  `flow_id` varchar(64) NOT NULL COMMENT '生产流ID',
  `material` varchar(128) NOT NULL COMMENT '材料名称',
  `quantity` int NOT NULL COMMENT '数量',
  `sort_order` int DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`material_id`),
  KEY `idx_flow_material_flow` (`flow_id`),
  CONSTRAINT `fk_flow_material_flow` FOREIGN KEY (`flow_id`) REFERENCES `pf_production_flow` (`flow_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生产流材料汇总';

CREATE TABLE IF NOT EXISTS `pf_production_flow_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `flow_id` varchar(64) NOT NULL COMMENT '生产流ID',
  `order_id` varchar(64) NOT NULL COMMENT '订单编号',
  `quantity` int DEFAULT 0 COMMENT '分配数量',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_flow_order` (`flow_id`, `order_id`),
  CONSTRAINT `fk_flow_order_flow` FOREIGN KEY (`flow_id`) REFERENCES `pf_production_flow` (`flow_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_flow_order_order` FOREIGN KEY (`order_id`) REFERENCES `pf_order_pool` (`order_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生产流与订单关联';

CREATE TABLE IF NOT EXISTS `pf_production_flow_step` (
  `step_id` bigint NOT NULL AUTO_INCREMENT COMMENT '步骤ID',
  `flow_id` varchar(64) NOT NULL COMMENT '生产流ID',
  `node_id` varchar(64) DEFAULT NULL COMMENT '模板节点ID',
  `step_name` varchar(128) NOT NULL COMMENT '步骤名称',
  `step_status` varchar(32) NOT NULL DEFAULT 'pending' COMMENT '步骤状态',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `sort_order` int DEFAULT 0 COMMENT '显示顺序',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`step_id`),
  KEY `idx_flow_step_flow` (`flow_id`),
  CONSTRAINT `fk_flow_step_flow` FOREIGN KEY (`flow_id`) REFERENCES `pf_production_flow` (`flow_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生产流执行步骤';
