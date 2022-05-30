/*
Navicat MySQL Data Transfer

Source Server         : loan
Source Server Version : 80015
Source Host           : 192.168.248.136:3306
Source Database       : srb_core

Target Server Type    : MYSQL
Target Server Version : 80015
File Encoding         : 65001

Date: 2022-05-18 20:47:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for dict
-- ----------------------------
DROP TABLE IF EXISTS `dict`;
CREATE TABLE `dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `parent_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '上级id',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '名称',
  `value` int(11) DEFAULT NULL COMMENT '值',
  `dict_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '编码',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_parent_id_value` (`parent_id`,`value`) USING BTREE,
  KEY `idx_parent_id` (`parent_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=82316 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='数据字典';

-- ----------------------------
-- Records of dict
-- ----------------------------
INSERT INTO `dict` VALUES ('1', '0', '全部分类', null, 'ROOT', '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('20000', '1', '行业', null, 'industry', '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('20001', '20000', 'IT', '1', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('20002', '20000', '医生', '2', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('20003', '20000', '教师', '3', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('20004', '20000', '导游', '4', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('20005', '20000', '律师', '5', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('20006', '20000', '其他', '6', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('30000', '1', '学历', null, 'education', '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('30001', '30000', '高中', '1', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('30002', '30000', '大专', '2', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('30003', '30000', '本科', '3', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('30004', '30000', '研究生', '4', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('30005', '30000', '其他', '5', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('40000', '1', '收入', null, 'income', '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('40001', '40000', '0-3000', '1', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('40002', '40000', '3000-5000', '2', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('40003', '40000', '5000-10000', '3', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('40004', '40000', '10000以上', '4', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('50000', '1', '收入来源', null, 'returnSource', '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('50001', '50000', '工资', '1', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('50002', '50000', '股票', '2', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('50003', '50000', '兼职', '3', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('60000', '1', '关系', null, 'relation', '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('60001', '60000', '夫妻', '1', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('60002', '60000', '兄妹', '2', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('60003', '60000', '父母', '3', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('60004', '60000', '其他', '4', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('70000', '1', '还款方式', null, 'returnMethod', '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('70001', '70000', '等额本息', '1', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('70002', '70000', '等额本金', '2', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('70003', '70000', '每月还息一次还本', '3', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('70004', '70000', '一次还本还息', '4', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('80000', '1', '资金用途', null, 'moneyUse', '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('80001', '80000', '旅游', '1', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('80002', '80000', '买房', '2', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('80003', '80000', '装修', '3', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('80004', '80000', '医疗', '4', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('80005', '80000', '美容', '5', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('80006', '80000', '其他', '6', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('81000', '1', '借款状态', null, 'borrowStatus', '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('81001', '81000', '待审核', '0', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('81002', '81000', '审批通过', '1', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('81003', '81000', '还款中', '2', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('81004', '81000', '结束', '3', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('81005', '81000', '审批不通过', '-1', null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('82000', '1', '学校性质', null, 'SchoolStatus', '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('82001', '82000', '211/985', null, null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('82002', '82000', '一本', null, null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('82003', '82000', '二本', null, null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('82004', '82000', '三本', null, null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('82005', '82000', '高职高专', null, null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('82006', '82000', '中职中专', null, null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
INSERT INTO `dict` VALUES ('82007', '82000', '高中及以下', null, null, '2021-02-20 16:26:22', '2021-02-20 16:26:22', '0');
