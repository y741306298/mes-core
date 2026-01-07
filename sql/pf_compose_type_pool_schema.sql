-- 排版池与生产流建表语句

CREATE TABLE IF NOT EXISTS `pf_compose_type_pool` (
  `compose_id` varchar(64) NOT NULL COMMENT '排版ID',
  `order_ids` varchar(1024) DEFAULT NULL COMMENT '订单ID集合',
  `quantity` int NOT NULL COMMENT '数量',
  `material` varchar(128) DEFAULT NULL COMMENT '材料',
  `craft_requirements` varchar(1024) DEFAULT NULL COMMENT '工艺要求',
  `thumbnail` varchar(512) DEFAULT NULL COMMENT '缩略图',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `plt` varchar(512) DEFAULT NULL COMMENT 'PLT路径',
  `source_image` varchar(512) DEFAULT NULL COMMENT '原图',
  `order_status` varchar(32) NOT NULL COMMENT '排版状态',
  `template_id` varchar(32) NOT NULL COMMENT '流程模板ID',
  `priority` varchar(16) NOT NULL COMMENT '优先级',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`compose_id`),
  KEY `idx_compose_template` (`template_id`),
  CONSTRAINT `fk_compose_template` FOREIGN KEY (`template_id`) REFERENCES `brt_flow_template` (`template_id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='排版池';

CREATE TABLE IF NOT EXISTS `pf_compose_flow` (
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
  KEY `idx_compose_flow_template` (`template_id`),
  CONSTRAINT `fk_compose_flow_template` FOREIGN KEY (`template_id`) REFERENCES `brt_flow_template` (`template_id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='排版生产流';

CREATE TABLE IF NOT EXISTS `pf_compose_flow_material` (
  `material_id` bigint NOT NULL AUTO_INCREMENT COMMENT '材料ID',
  `flow_id` varchar(64) NOT NULL COMMENT '生产流ID',
  `material` varchar(128) NOT NULL COMMENT '材料名称',
  `quantity` int NOT NULL COMMENT '数量',
  `sort_order` int DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`material_id`),
  KEY `idx_compose_flow_material_flow` (`flow_id`),
  CONSTRAINT `fk_compose_flow_material_flow` FOREIGN KEY (`flow_id`) REFERENCES `pf_compose_flow` (`flow_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='排版生产流材料汇总';

CREATE TABLE IF NOT EXISTS `pf_compose_flow_rel` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `flow_id` varchar(64) NOT NULL COMMENT '生产流ID',
  `compose_id` varchar(64) NOT NULL COMMENT '排版ID',
  `quantity` int DEFAULT 0 COMMENT '分配数量',
  `status` varchar(32) NOT NULL DEFAULT 'pending' COMMENT '任务状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_compose_flow_rel` (`flow_id`, `compose_id`),
  CONSTRAINT `fk_compose_flow_rel_flow` FOREIGN KEY (`flow_id`) REFERENCES `pf_compose_flow` (`flow_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_compose_flow_rel_compose` FOREIGN KEY (`compose_id`) REFERENCES `pf_compose_type_pool` (`compose_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='排版生产流与排版关联';

CREATE TABLE IF NOT EXISTS `pf_compose_flow_step` (
  `step_id` bigint NOT NULL AUTO_INCREMENT COMMENT '步骤ID',
  `flow_id` varchar(64) NOT NULL COMMENT '生产流ID',
  `node_id` varchar(64) DEFAULT NULL COMMENT '模板节点ID',
  `step_name` varchar(128) NOT NULL COMMENT '步骤名称',
  `step_status` varchar(32) NOT NULL DEFAULT 'pending' COMMENT '步骤状态',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `sort_order` int DEFAULT 0 COMMENT '显示顺序',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`step_id`),
  KEY `idx_compose_flow_step_flow` (`flow_id`),
  CONSTRAINT `fk_compose_flow_step_flow` FOREIGN KEY (`flow_id`) REFERENCES `pf_compose_flow` (`flow_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='排版生产流执行步骤';
