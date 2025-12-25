SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for brt_mat
-- ----------------------------
DROP TABLE IF EXISTS `brt_mat`;
CREATE TABLE `brt_mat` (
  `mat_code` varchar(64) NOT NULL COMMENT '材料编码',
  `mat_name` varchar(200) NOT NULL COMMENT '材料名称',
  `mat_category` varchar(128) DEFAULT NULL COMMENT '材料分类',
  `mat_color` varchar(128) DEFAULT NULL COMMENT '颜色',
  `mat_brand` varchar(128) DEFAULT NULL COMMENT '品牌',
  `mat_supplier` varchar(128) DEFAULT NULL COMMENT '供应商',
  `mat_width` varchar(64) DEFAULT NULL COMMENT '材料宽度',
  `mat_length` varchar(64) DEFAULT NULL COMMENT '材料长度',
  `mat_thickness` varchar(64) DEFAULT NULL COMMENT '材料厚度',
  `package_name` varchar(128) DEFAULT NULL COMMENT '包装名称',
  `measure_unit_int` int DEFAULT NULL COMMENT '度量单位（数字）',
  `measure_unit` varchar(64) DEFAULT NULL COMMENT '度量单位（名称）',
  `unit_weight` decimal(18,4) DEFAULT NULL COMMENT '单位重量',
  `unit_price` decimal(18,4) DEFAULT NULL COMMENT '单价',
  `is_valid` tinyint(1) DEFAULT '1' COMMENT '是否有效',
  `comments` varchar(512) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`mat_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='材料信息';

-- ----------------------------
-- Table structure for brt_proc
-- ----------------------------
DROP TABLE IF EXISTS `brt_proc`;
CREATE TABLE `brt_proc` (
  `proc_code` varchar(64) NOT NULL COMMENT '工艺编码',
  `proc_name` varchar(200) DEFAULT NULL COMMENT '工艺名称',
  `proc_attachment_type_list` varchar(512) DEFAULT NULL COMMENT '附件类型列表',
  `measure_unit` int DEFAULT NULL COMMENT '度量单位（数字）',
  `measure_unit_str` varchar(64) DEFAULT NULL COMMENT '度量单位（名称）',
  `proc_price` decimal(18,4) DEFAULT NULL COMMENT '工艺价格',
  `is_valid` tinyint(1) DEFAULT '1' COMMENT '是否有效',
  `comments` varchar(512) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`proc_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工艺信息';

-- ----------------------------
-- Table structure for brt_prod
-- ----------------------------
DROP TABLE IF EXISTS `brt_prod`;
CREATE TABLE `brt_prod` (
  `prod_code` varchar(64) NOT NULL COMMENT '产品编码',
  `prod_type` int DEFAULT NULL COMMENT '产品类型',
  `prod_name` varchar(200) DEFAULT NULL COMMENT '产品名称',
  `min_length` decimal(18,4) DEFAULT NULL COMMENT '最小长度',
  `max_length` decimal(18,4) DEFAULT NULL COMMENT '最大长度',
  `min_width` decimal(18,4) DEFAULT NULL COMMENT '最小宽度',
  `max_width` decimal(18,4) DEFAULT NULL COMMENT '最大宽度',
  `material_code` varchar(64) DEFAULT NULL COMMENT '材料编码',
  `material_name` varchar(200) DEFAULT NULL COMMENT '材料名称',
  `material_color` varchar(128) DEFAULT NULL COMMENT '材料颜色',
  `material_brand` varchar(128) DEFAULT NULL COMMENT '材料品牌',
  `material_supplier` varchar(128) DEFAULT NULL COMMENT '材料供应商',
  `measure_unit_int` int DEFAULT NULL COMMENT '度量单位（数字）',
  `measure_unit` varchar(64) DEFAULT NULL COMMENT '度量单位（名称）',
  `unit_weight` decimal(18,4) DEFAULT NULL COMMENT '单位重量',
  `additional_unitfee` decimal(18,4) DEFAULT NULL COMMENT '附加单价',
  `comments` varchar(512) DEFAULT NULL COMMENT '备注',
  `is_merchandise` tinyint(1) DEFAULT NULL COMMENT '是否为商品',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`prod_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品信息';

SET FOREIGN_KEY_CHECKS = 1;
