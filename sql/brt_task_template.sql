/*
Navicat MySQL Data Transfer

Source Server         : aisql
Source Server Version : 80036
Source Host           : rm-bp16bkd4uqq94p7y03o.mysql.rds.aliyuncs.com:3306
Source Database       : mes_test

Target Server Type    : MYSQL
Target Server Version : 80036
File Encoding         : 65001

Date: 2025-12-18 18:52:35
*/

SET FOREIGN_KEY_CHECKS=0;

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
  `query_sql` longtext COMMENT '查询SQL',
  `storage_sql` longtext COMMENT '存储SQL',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态(0=正常,1=停用)',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='任务模板';

-- ----------------------------
-- Records of brt_task_template
-- ----------------------------
INSERT INTO `brt_task_template` VALUES ('1988859171696992257', '测试任务', 'API', 'AUTO', '{\"requestUrl\":\"333\",\"requestParams\":[{\"paramName\":\"1\",\"paramKey\":\"1\",\"paramType\":\"string\",\"required\":\"0\",\"remark\":\"\"}],\"responseParams\":[{\"paramName\":\"2\",\"paramKey\":\"2\",\"paramType\":\"string\",\"remark\":\"\"}]}', '[{\"statusLabel\":\"成功\",\"statusValue\":\"SUCCESS\"},{\"statusLabel\":\"失败\",\"statusValue\":\"FAILED\"}]', '1111', '222', '0', null, 'admin', '2025-11-13 14:39:12', 'admin', '2025-11-13 14:39:12');
INSERT INTO `brt_task_template` VALUES ('1991023225001631745', '第一步', 'API', 'AUTO', '{\"requestUrl\":\"/script/api/first\",\"requestParams\":[],\"responseParams\":[]}', '[{\"statusLabel\":\"成功\",\"statusValue\":\"SUCCESS\"},{\"statusLabel\":\"失败\",\"statusValue\":\"FAILED\"}]', '1111', '2222', '0', null, 'admin', '2025-11-19 13:58:23', 'admin', '2025-11-19 13:58:23');
INSERT INTO `brt_task_template` VALUES ('1991023363715653633', '第二步', 'API', 'AUTO', '{\"requestUrl\":\"/script/api/second\",\"requestParams\":[],\"responseParams\":[]}', '[{\"statusLabel\":\"成功\",\"statusValue\":\"SUCCESS\"},{\"statusLabel\":\"失败\",\"statusValue\":\"FAILED\"}]', '111', '222', '0', null, 'admin', '2025-11-19 13:58:56', 'admin', '2025-11-19 13:58:56');
INSERT INTO `brt_task_template` VALUES ('1996526710222831618', '生产池第一步', 'API', 'MANUAL', '{\"requestUrl\":\"/script/api/flowFirst\",\"requestParams\":[],\"responseParams\":[]}', '[{\"statusLabel\":\"成功\",\"statusValue\":\"SUCCESS\"},{\"statusLabel\":\"失败\",\"statusValue\":\"FAILED\"}]', '1', '1', '0', null, 'admin', '2025-12-04 18:27:16', 'admin', '2025-12-04 18:27:16');
INSERT INTO `brt_task_template` VALUES ('1996526832050585602', '生产池第二步', 'API', 'AUTO', '{\"requestUrl\":\"/script/api/flowSecond\",\"requestParams\":[],\"responseParams\":[]}', '[{\"statusLabel\":\"成功\",\"statusValue\":\"SUCCESS\"},{\"statusLabel\":\"失败\",\"statusValue\":\"FAILED\"}]', '1', '1', '0', null, 'admin', '2025-12-04 18:27:45', 'admin', '2025-12-11 14:36:48');
INSERT INTO `brt_task_template` VALUES ('1996527094618210305', '生产池第三步-跳转池', 'API', 'AUTO', '{\"requestUrl\":\"/script/api/flowThird\",\"requestParams\":[{\"paramName\":\"订单id\",\"paramKey\":\"orderId\",\"paramType\":\"string\",\"required\":\"0\",\"remark\":\"\"},{\"paramName\":\"生产池id\",\"paramKey\":\"flowId\",\"paramType\":\"string\",\"required\":\"0\",\"remark\":\"\"}],\"responseParams\":[]}', '[{\"statusLabel\":\"成功\",\"statusValue\":\"SUCCESS\"},{\"statusLabel\":\"失败\",\"statusValue\":\"FAILED\"}]', '1', '1', '0', null, 'admin', '2025-12-04 18:28:48', 'admin', '2025-12-04 18:28:48');
INSERT INTO `brt_task_template` VALUES ('1999415610517196802', '生产池B第一步', 'API', 'AUTO', '{\"requestUrl\":\"/script/api/first\",\"requestParams\":[],\"responseParams\":[]}', '[{\"statusLabel\":\"成功\",\"statusValue\":\"SUCCESS\"},{\"statusLabel\":\"失败\",\"statusValue\":\"FAILED\"}]', '1', '1', '0', null, 'admin', '2025-12-12 17:46:44', 'admin', '2025-12-12 17:46:44');
INSERT INTO `brt_task_template` VALUES ('1999415702754136065', '生产池B第二步', 'API', 'AUTO', '{\"requestUrl\":\"/script/api/second\",\"requestParams\":[],\"responseParams\":[]}', '[{\"statusLabel\":\"成功\",\"statusValue\":\"SUCCESS\"},{\"statusLabel\":\"失败\",\"statusValue\":\"FAILED\"}]', '1', '1', '0', null, 'admin', '2025-12-12 17:47:06', 'admin', '2025-12-12 17:47:22');
