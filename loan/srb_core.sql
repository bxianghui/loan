/*
Navicat MySQL Data Transfer

Source Server         : loan
Source Server Version : 80015
Source Host           : 192.168.248.136:3306
Source Database       : srb_core

Target Server Type    : MYSQL
Target Server Version : 80015
File Encoding         : 65001

Date: 2022-05-29 17:07:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for borrow_info
-- ----------------------------
DROP TABLE IF EXISTS `borrow_info`;
CREATE TABLE `borrow_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '借款用户id',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '借款金额',
  `period` int(11) DEFAULT NULL COMMENT '借款期限',
  `borrow_year_rate` decimal(10,2) DEFAULT NULL COMMENT '年化利率',
  `return_method` tinyint(3) DEFAULT NULL COMMENT '还款方式 1-等额本息 2-等额本金 3-每月还息一次还本 4-一次还本',
  `money_use` tinyint(3) DEFAULT NULL COMMENT '资金用途',
  `status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '状态（0：未提交，1：审核中， 2：审核通过， -1：审核不通过）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='借款信息表';

-- ----------------------------
-- Records of borrow_info
-- ----------------------------
INSERT INTO `borrow_info` VALUES ('10', '17', '2000.00', '1', '0.12', '2', '5', '2', '2022-05-25 19:37:07', '2022-05-25 21:01:15', '0');
INSERT INTO `borrow_info` VALUES ('11', '25', '15000.00', '9', '0.10', '1', '1', '2', '2022-05-28 18:27:54', '2022-05-28 18:27:54', '0');

-- ----------------------------
-- Table structure for borrower
-- ----------------------------
DROP TABLE IF EXISTS `borrower`;
CREATE TABLE `borrower` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户id',
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '姓名',
  `id_card` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '身份证号',
  `mobile` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '手机',
  `sex` tinyint(3) DEFAULT NULL COMMENT '性别（1：男 0：女）',
  `age` tinyint(3) DEFAULT NULL COMMENT '年龄',
  `education` tinyint(3) DEFAULT NULL COMMENT '学历',
  `is_marry` tinyint(1) DEFAULT NULL COMMENT '是否结婚（1：是 0：否）',
  `industry` tinyint(3) DEFAULT NULL COMMENT '行业',
  `income` tinyint(3) DEFAULT NULL COMMENT '月收入',
  `return_source` tinyint(3) DEFAULT NULL COMMENT '还款来源',
  `contacts_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '联系人名称',
  `contacts_mobile` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '联系人手机',
  `contacts_relation` tinyint(3) DEFAULT NULL COMMENT '联系人关系',
  `status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '状态（0：未认证，1：认证中， 2：认证通过， -1：认证失败）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='借款人';

-- ----------------------------
-- Records of borrower
-- ----------------------------
INSERT INTO `borrower` VALUES ('10', '23', '小红帽', '6467498465', '14512341234', '0', '25', '4', '0', '6', '2', '3', '大灰狼', '15879645877', '4', '2', '2022-05-25 11:01:50', '2022-05-25 11:01:50', '0');
INSERT INTO `borrower` VALUES ('12', '21', '光头强', '1564654165', '15748965478', '1', '25', '2', '0', '2', '3', '2', '熊大', '15896324784', '4', '2', '2022-05-25 15:56:03', '2022-05-25 15:56:03', '0');
INSERT INTO `borrower` VALUES ('13', '19', '熊大', '15645683218916', '15893647821', '1', '33', '1', '0', '3', '3', '2', '熊大', '15896327411', '4', '2', '2022-05-25 19:12:54', '2022-05-25 19:12:54', '0');
INSERT INTO `borrower` VALUES ('14', '20', '熊二', '4646541654', '18963214871', '1', '58', '2', '0', '4', '2', '2', '光头强', '15697458961', '3', '2', '2022-05-25 19:15:55', '2022-05-25 19:15:55', '0');
INSERT INTO `borrower` VALUES ('15', '17', '李四', '46483549', '15896743148', '1', '52', '1', '1', '3', '2', '2', '王五', '18965774215', '1', '2', '2022-05-25 19:22:09', '2022-05-25 19:22:09', '0');
INSERT INTO `borrower` VALUES ('16', '25', '莫名', '789546132141234789', '15896478451', '0', '25', '4', '0', '3', '4', '1', '莫急', '18965747894', '3', '2', '2022-05-28 18:25:54', '2022-05-28 18:25:54', '0');

-- ----------------------------
-- Table structure for borrower_attach
-- ----------------------------
DROP TABLE IF EXISTS `borrower_attach`;
CREATE TABLE `borrower_attach` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `borrower_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '借款人id',
  `image_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '图片类型（idCard1：身份证正面，idCard2：身份证反面，house：房产证，car：车）',
  `image_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '图片路径',
  `image_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '图片名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_borrower_id` (`borrower_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='借款人上传资源表';

-- ----------------------------
-- Records of borrower_attach
-- ----------------------------
INSERT INTO `borrower_attach` VALUES ('13', '6', 'idCard1', 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/idCard1/2022/05/23/bc4fde08-d1b1-4684-b214-f2aa6de24ac8.png', 'wallhaven-281d5y.png', '2022-05-23 18:39:15', '2022-05-23 18:39:15', '0');
INSERT INTO `borrower_attach` VALUES ('14', '6', 'idCard2', 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/idCard2/2022/05/23/4a375910-1b41-423d-8500-b9af2c39d040.png', 'wallhaven-281d5y.png', '2022-05-23 18:39:16', '2022-05-23 18:39:16', '0');
INSERT INTO `borrower_attach` VALUES ('15', '6', 'house', 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/house/2022/05/23/100c2b86-bb43-40b7-bd11-23f015158a25.png', 'wallhaven-281d5y.png', '2022-05-23 18:39:16', '2022-05-23 18:39:16', '0');
INSERT INTO `borrower_attach` VALUES ('16', '6', 'car', 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/car/2022/05/23/d9ad3891-08d4-4e58-9c09-55b8f096e02e.png', 'wallhaven-281d5y.png', '2022-05-23 18:39:16', '2022-05-23 18:39:16', '0');
INSERT INTO `borrower_attach` VALUES ('17', '10', 'idCard1', 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/idCard1/2022/05/25/7433d4f1-6fdd-4768-8356-d0a1712db8cb.jpg', '桌面.jpg', '2022-05-25 11:01:50', '2022-05-25 11:01:50', '0');
INSERT INTO `borrower_attach` VALUES ('18', '10', 'idCard2', 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/idCard2/2022/05/25/f53691f8-65c7-43f6-ba0f-537153c3a618.jpg', '桌面.jpg', '2022-05-25 11:01:50', '2022-05-25 11:01:50', '0');
INSERT INTO `borrower_attach` VALUES ('19', '10', 'house', 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/house/2022/05/25/e27b279a-e9c0-4a6f-85f7-e5b6e1621d52.jpg', '桌面.jpg', '2022-05-25 11:01:50', '2022-05-25 11:01:50', '0');
INSERT INTO `borrower_attach` VALUES ('20', '10', 'car', 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/car/2022/05/25/78c2b0ea-ae22-439b-ab27-7bec701b0a58.jpg', '桌面.jpg', '2022-05-25 11:01:50', '2022-05-25 11:01:50', '0');
INSERT INTO `borrower_attach` VALUES ('25', '12', 'idCard1', 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/idCard1/2022/05/25/851847d2-2f3d-403c-8ba7-562bd7dbe5e5.jpg', '桌面.jpg', '2022-05-25 15:56:03', '2022-05-25 15:56:03', '0');
INSERT INTO `borrower_attach` VALUES ('26', '12', 'idCard2', 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/idCard2/2022/05/25/7fa0a24b-c7b8-4f33-adda-feca90295e94.jpg', '桌面.jpg', '2022-05-25 15:56:03', '2022-05-25 15:56:03', '0');
INSERT INTO `borrower_attach` VALUES ('27', '12', 'house', 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/house/2022/05/25/ad7b6e9e-1b7c-40a8-bf70-da63ee9785fe.jpg', '桌面.jpg', '2022-05-25 15:56:03', '2022-05-25 15:56:03', '0');
INSERT INTO `borrower_attach` VALUES ('28', '12', 'car', 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/car/2022/05/25/de9e68c7-576e-4c62-9c7f-65312604104f.jpg', '桌面.jpg', '2022-05-25 15:56:03', '2022-05-25 15:56:03', '0');
INSERT INTO `borrower_attach` VALUES ('29', '13', 'idCard1', 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/idCard1/2022/05/25/652db4d5-cdf3-49d0-8e3a-9eeabc335d38.jpg', '桌面.jpg', '2022-05-25 19:12:54', '2022-05-25 19:12:54', '0');
INSERT INTO `borrower_attach` VALUES ('30', '13', 'idCard2', 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/idCard2/2022/05/25/7fd0a91a-dd5f-430e-9196-888380a4308f.jpg', '桌面.jpg', '2022-05-25 19:12:54', '2022-05-25 19:12:54', '0');
INSERT INTO `borrower_attach` VALUES ('31', '13', 'house', 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/house/2022/05/25/72bdb3f9-ce1b-4292-9240-b2d3c60b371e.jpg', '桌面.jpg', '2022-05-25 19:12:54', '2022-05-25 19:12:54', '0');
INSERT INTO `borrower_attach` VALUES ('32', '13', 'car', 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/car/2022/05/25/6d76f5ca-23c6-4a2f-ad78-e8e1755ad449.jpg', '桌面.jpg', '2022-05-25 19:12:54', '2022-05-25 19:12:54', '0');
INSERT INTO `borrower_attach` VALUES ('33', '14', 'idCard1', 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/idCard1/2022/05/25/93213518-1504-489a-8829-0838b1868837.jpg', '头像.jpg', '2022-05-25 19:15:55', '2022-05-25 19:15:55', '0');
INSERT INTO `borrower_attach` VALUES ('34', '14', 'idCard2', 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/idCard2/2022/05/25/5933a856-0b0e-41fe-aadb-410b4ef5e3e5.jpg', '头像.jpg', '2022-05-25 19:15:55', '2022-05-25 19:15:55', '0');
INSERT INTO `borrower_attach` VALUES ('35', '14', 'house', 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/house/2022/05/25/59fab20b-1eea-460f-a9a7-1305b9252d39.jpg', '头像.jpg', '2022-05-25 19:15:55', '2022-05-25 19:15:55', '0');
INSERT INTO `borrower_attach` VALUES ('36', '14', 'car', 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/car/2022/05/25/476dc968-b7d2-4e86-b806-a1f3540030fb.jpg', '头像.jpg', '2022-05-25 19:15:55', '2022-05-25 19:15:55', '0');
INSERT INTO `borrower_attach` VALUES ('37', '15', 'idCard1', 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/idCard1/2022/05/25/e261b759-d871-4fde-a36b-115e3cc3b1d0.jpg', '头像.jpg', '2022-05-25 19:22:09', '2022-05-25 19:22:09', '0');
INSERT INTO `borrower_attach` VALUES ('38', '15', 'idCard2', 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/idCard2/2022/05/25/faaaf33f-8a7a-4cf3-830d-3a9e7a00611c.jpg', '头像.jpg', '2022-05-25 19:22:10', '2022-05-25 19:22:10', '0');
INSERT INTO `borrower_attach` VALUES ('39', '15', 'house', 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/house/2022/05/25/da627424-124c-40f7-bdf9-b7989374ecbb.jpg', '头像.jpg', '2022-05-25 19:22:10', '2022-05-25 19:22:10', '0');
INSERT INTO `borrower_attach` VALUES ('40', '15', 'car', 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/car/2022/05/25/57cf02c9-45d9-409c-8e76-4e64b4f6fcc3.jpg', '头像.jpg', '2022-05-25 19:22:10', '2022-05-25 19:22:10', '0');
INSERT INTO `borrower_attach` VALUES ('41', '16', 'idCard1', 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/idCard1/2022/05/28/be2f1493-8349-4aaf-8d90-2a88e550adfb.jpg', '桌面.jpg', '2022-05-28 18:25:54', '2022-05-28 18:25:54', '0');
INSERT INTO `borrower_attach` VALUES ('42', '16', 'idCard2', 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/idCard2/2022/05/28/b8208c6b-390e-4017-a248-e80e78aa70c9.jpg', '桌面.jpg', '2022-05-28 18:25:54', '2022-05-28 18:25:54', '0');
INSERT INTO `borrower_attach` VALUES ('43', '16', 'house', 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/house/2022/05/28/f0268983-e675-46c7-b0c9-d62ee19c6009.jpg', '桌面.jpg', '2022-05-28 18:25:54', '2022-05-28 18:25:54', '0');
INSERT INTO `borrower_attach` VALUES ('44', '16', 'car', 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/car/2022/05/28/5bc9fcd4-5957-4736-8329-a94911cccfc6.jpg', '桌面.jpg', '2022-05-28 18:25:54', '2022-05-28 18:25:54', '0');

-- ----------------------------
-- Table structure for dict
-- ----------------------------
DROP TABLE IF EXISTS `dict`;
CREATE TABLE `dict` (
  `id` bigint(20) NOT NULL COMMENT 'id',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='数据字典';

-- ----------------------------
-- Records of dict
-- ----------------------------
INSERT INTO `dict` VALUES ('1', '0', '全部分类', null, 'ROOT', '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('20000', '1', '行业', null, 'industry', '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('20001', '20000', 'IT', '1', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('20002', '20000', '医生', '2', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('20003', '20000', '教师', '3', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('20004', '20000', '导游', '4', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('20005', '20000', '律师', '5', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('20006', '20000', '其他', '6', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('30000', '1', '学历', null, 'education', '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('30001', '30000', '高中', '1', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('30002', '30000', '大专', '2', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('30003', '30000', '本科', '3', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('30004', '30000', '研究生', '4', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('30005', '30000', '其他', '5', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('40000', '1', '收入', null, 'income', '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('40001', '40000', '0-3000', '1', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('40002', '40000', '3000-5000', '2', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('40003', '40000', '5000-10000', '3', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('40004', '40000', '10000以上', '4', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('50000', '1', '收入来源', null, 'returnSource', '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('50001', '50000', '工资', '1', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('50002', '50000', '股票', '2', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('50003', '50000', '兼职', '3', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('60000', '1', '关系', null, 'relation', '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('60001', '60000', '夫妻', '1', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('60002', '60000', '兄妹', '2', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('60003', '60000', '父母', '3', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('60004', '60000', '其他', '4', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('70000', '1', '还款方式', null, 'returnMethod', '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('70001', '70000', '等额本息', '1', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('70002', '70000', '等额本金', '2', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('70003', '70000', '每月还息一次还本', '3', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('70004', '70000', '一次还本还息', '4', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('80000', '1', '资金用途', null, 'moneyUse', '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('80001', '80000', '旅游', '1', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('80002', '80000', '买房', '2', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('80003', '80000', '装修', '3', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('80004', '80000', '医疗', '4', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('80005', '80000', '美容', '5', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('80006', '80000', '其他', '6', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('81000', '1', '借款状态', null, 'borrowStatus', '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('81001', '81000', '待审核', '0', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('81002', '81000', '审批通过', '1', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('81003', '81000', '还款中', '2', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('81004', '81000', '结束', '3', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('81005', '81000', '审批不通过', '-1', null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('82000', '1', '学校性质', null, 'SchoolStatus', '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('82001', '82000', '211/985', null, null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('82002', '82000', '一本', null, null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('82003', '82000', '二本', null, null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('82004', '82000', '三本', null, null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('82005', '82000', '高职高专', null, null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('82006', '82000', '中职中专', null, null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');
INSERT INTO `dict` VALUES ('82007', '82000', '高中及以下', null, null, '2022-05-19 09:30:02', '2022-05-19 09:30:02', '0');

-- ----------------------------
-- Table structure for integral_grade
-- ----------------------------
DROP TABLE IF EXISTS `integral_grade`;
CREATE TABLE `integral_grade` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `integral_start` int(11) DEFAULT NULL COMMENT '积分区间开始',
  `integral_end` int(11) DEFAULT NULL COMMENT '积分区间结束',
  `borrow_amount` decimal(10,2) DEFAULT NULL COMMENT '借款额度',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='积分等级表';

-- ----------------------------
-- Records of integral_grade
-- ----------------------------
INSERT INTO `integral_grade` VALUES ('1', '10', '50', '10000.00', '2020-12-09 09:02:29', '2022-05-17 15:44:32', '0');
INSERT INTO `integral_grade` VALUES ('2', '51', '100', '30000.00', '2020-12-09 09:02:42', '2021-02-20 10:00:25', '0');
INSERT INTO `integral_grade` VALUES ('3', '101', '2000', '100000.00', '2020-12-09 09:02:57', '2021-02-24 13:03:03', '0');
INSERT INTO `integral_grade` VALUES ('6', '2001', '10000', '1000000.00', '2022-05-17 15:47:07', '2022-05-25 16:24:35', '0');
INSERT INTO `integral_grade` VALUES ('9', '10001', '100000', '10000000.00', '2022-05-29 11:00:58', '2022-05-29 11:00:58', '0');

-- ----------------------------
-- Table structure for lend
-- ----------------------------
DROP TABLE IF EXISTS `lend`;
CREATE TABLE `lend` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint(20) DEFAULT NULL COMMENT '借款用户id',
  `borrow_info_id` bigint(20) DEFAULT NULL COMMENT '借款信息id',
  `lend_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '标的编号',
  `title` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '标题',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '标的金额',
  `period` int(11) DEFAULT NULL COMMENT '投资期数',
  `lend_year_rate` decimal(10,2) DEFAULT NULL COMMENT '年化利率',
  `service_rate` decimal(10,2) DEFAULT NULL COMMENT '平台服务费率',
  `return_method` tinyint(3) DEFAULT NULL COMMENT '还款方式',
  `lowest_amount` decimal(10,2) DEFAULT NULL COMMENT '最低投资金额',
  `invest_amount` decimal(10,2) DEFAULT NULL COMMENT '已投金额',
  `invest_num` int(11) DEFAULT NULL COMMENT '投资人数',
  `publish_date` datetime DEFAULT NULL COMMENT '发布日期',
  `lend_start_date` date DEFAULT NULL COMMENT '开始日期',
  `lend_end_date` date DEFAULT NULL COMMENT '结束日期',
  `lend_info` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '说明',
  `expect_amount` decimal(10,2) DEFAULT NULL COMMENT '平台预期收益',
  `real_amount` decimal(10,2) DEFAULT NULL COMMENT '实际收益',
  `status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '状态',
  `check_time` datetime DEFAULT NULL COMMENT '审核时间',
  `check_admin_id` bigint(1) DEFAULT NULL COMMENT '审核用户id',
  `payment_time` datetime DEFAULT NULL COMMENT '放款时间',
  `payment_admin_id` datetime DEFAULT NULL COMMENT '放款人id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_lend_no` (`lend_no`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_borrow_info_id` (`borrow_info_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='标的准备表';

-- ----------------------------
-- Records of lend
-- ----------------------------
INSERT INTO `lend` VALUES ('4', '17', '10', 'LEND20220525210259121', '美容贷', '2000.00', '1', '0.12', '0.05', '2', '100.00', '2000.00', '2', '2022-05-25 21:03:00', '2022-05-25', '2022-06-25', '这是一个测试', '8.33', '8.33', '3', '2022-05-25 21:03:00', '1', '2022-05-27 12:54:03', null, '2022-05-25 21:03:00', '2022-05-27 12:51:27', '0');
INSERT INTO `lend` VALUES ('5', '25', '11', 'LEND20220528182859274', '旅游借款', '15000.00', '9', '0.10', '0.05', '1', '100.00', '15000.00', '2', '2022-05-28 18:29:00', '2022-05-28', '2023-02-28', '世界这么大，我想去看看', '562.50', '562.50', '2', '2022-05-28 18:29:00', '1', '2022-05-28 19:14:05', null, '2022-05-28 18:29:00', '2022-05-28 18:29:00', '0');

-- ----------------------------
-- Table structure for lend_item
-- ----------------------------
DROP TABLE IF EXISTS `lend_item`;
CREATE TABLE `lend_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `lend_item_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '投资编号',
  `lend_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '标的id',
  `invest_user_id` bigint(20) DEFAULT NULL COMMENT '投资用户id',
  `invest_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '投资人名称',
  `invest_amount` decimal(10,2) DEFAULT NULL COMMENT '投资金额',
  `lend_year_rate` decimal(10,2) DEFAULT NULL COMMENT '年化利率',
  `invest_time` datetime DEFAULT NULL COMMENT '投资时间',
  `lend_start_date` date DEFAULT NULL COMMENT '开始日期',
  `lend_end_date` date DEFAULT NULL COMMENT '结束日期',
  `expect_amount` decimal(10,2) DEFAULT NULL COMMENT '预期收益',
  `real_amount` decimal(10,2) DEFAULT NULL COMMENT '实际收益',
  `status` tinyint(3) DEFAULT NULL COMMENT '状态（0：默认 1：已支付 2：已还款）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_lend_item_no` (`lend_item_no`) USING BTREE,
  KEY `idx_lend_id` (`lend_id`) USING BTREE,
  KEY `idx_invest_user_id` (`invest_user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='标的出借记录表';

-- ----------------------------
-- Records of lend_item
-- ----------------------------
INSERT INTO `lend_item` VALUES ('11', 'INVEST20220526202720061', '4', '22', '吉吉', '200.00', '0.12', '2022-05-26 20:27:21', '2022-05-25', '2022-06-25', '2.00', '2.00', '1', '2022-05-26 20:27:21', '2022-05-26 20:27:21', '0');
INSERT INTO `lend_item` VALUES ('12', 'INVEST20220526203918651', '4', '18', '王五', '1800.00', '0.12', '2022-05-26 20:39:18', '2022-05-25', '2022-06-25', '18.00', '18.00', '1', '2022-05-26 20:39:19', '2022-05-26 20:39:19', '0');
INSERT INTO `lend_item` VALUES ('13', 'INVEST20220528183231773', '5', '24', '张佳佳', '10000.00', '0.10', '2022-05-28 18:32:31', '2022-05-28', '2023-02-28', '421.23', '157.70', '1', '2022-05-28 18:32:32', '2022-05-28 18:32:32', '0');
INSERT INTO `lend_item` VALUES ('14', 'INVEST20220528191244942', '5', '26', '15896374874', '5000.00', '0.10', '2022-05-28 19:12:45', '2022-05-28', '2023-02-28', '210.60', '78.84', '1', '2022-05-28 19:12:45', '2022-05-28 19:12:45', '0');

-- ----------------------------
-- Table structure for lend_item_return
-- ----------------------------
DROP TABLE IF EXISTS `lend_item_return`;
CREATE TABLE `lend_item_return` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `lend_return_id` bigint(20) DEFAULT NULL COMMENT '标的还款id',
  `lend_item_id` bigint(20) DEFAULT NULL COMMENT '标的项id',
  `lend_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '标的id',
  `invest_user_id` bigint(1) DEFAULT NULL COMMENT '出借用户id',
  `invest_amount` decimal(10,2) DEFAULT NULL COMMENT '出借金额',
  `current_period` int(11) DEFAULT NULL COMMENT '当前的期数',
  `lend_year_rate` decimal(10,2) DEFAULT NULL COMMENT '年化利率',
  `return_method` tinyint(3) DEFAULT NULL COMMENT '还款方式 1-等额本息 2-等额本金 3-每月还息一次还本 4-一次还本',
  `principal` decimal(10,2) DEFAULT NULL COMMENT '本金',
  `interest` decimal(10,2) DEFAULT NULL COMMENT '利息',
  `total` decimal(10,2) DEFAULT NULL COMMENT '本息',
  `fee` decimal(10,2) DEFAULT '0.00' COMMENT '手续费',
  `return_date` date DEFAULT NULL COMMENT '还款时指定的还款日期',
  `real_return_time` datetime DEFAULT NULL COMMENT '实际发生的还款时间',
  `is_overdue` tinyint(1) DEFAULT NULL COMMENT '是否逾期',
  `overdue_total` decimal(10,2) DEFAULT NULL COMMENT '逾期金额',
  `status` tinyint(3) DEFAULT NULL COMMENT '状态（0-未归还 1-已归还）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_lend_return_id` (`lend_return_id`) USING BTREE,
  KEY `idx_lend_item_id` (`lend_item_id`) USING BTREE,
  KEY `idx_lend_id` (`lend_id`) USING BTREE,
  KEY `idx_invest_user_id` (`invest_user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='标的出借回款记录表';

-- ----------------------------
-- Records of lend_item_return
-- ----------------------------
INSERT INTO `lend_item_return` VALUES ('18', '11', '11', '4', '22', '200.00', '1', '0.12', '2', '200.00', '2.00', '202.00', '0.00', '2022-06-25', '2022-05-27 21:18:55', '0', null, '1', '2022-05-27 12:54:05', '2022-05-27 12:54:05', '0');
INSERT INTO `lend_item_return` VALUES ('19', '11', '12', '4', '18', '1800.00', '1', '0.12', '2', '1800.00', '18.00', '1818.00', '0.00', '2022-06-25', '2022-05-27 21:18:55', '0', null, '1', '2022-05-27 12:54:05', '2022-05-27 12:54:05', '0');
INSERT INTO `lend_item_return` VALUES ('20', '12', '13', '5', '24', '10000.00', '1', '0.10', '1', '1074.59', '83.33', '1157.92', '0.00', '2022-06-28', '2022-05-28 19:28:50', '0', null, '1', '2022-05-28 19:14:06', '2022-05-28 19:14:06', '0');
INSERT INTO `lend_item_return` VALUES ('21', '13', '13', '5', '24', '10000.00', '2', '0.10', '1', '1083.55', '74.37', '1157.92', '0.00', '2022-07-28', '2022-05-29 11:56:40', '0', null, '1', '2022-05-28 19:14:06', '2022-05-28 19:14:06', '0');
INSERT INTO `lend_item_return` VALUES ('22', '14', '13', '5', '24', '10000.00', '3', '0.10', '1', '1092.58', '65.34', '1157.92', '0.00', '2022-08-28', null, '0', null, '0', '2022-05-28 19:14:06', '2022-05-28 19:14:06', '0');
INSERT INTO `lend_item_return` VALUES ('23', '15', '13', '5', '24', '10000.00', '4', '0.10', '1', '1101.68', '56.24', '1157.92', '0.00', '2022-09-28', null, '0', null, '0', '2022-05-28 19:14:06', '2022-05-28 19:14:06', '0');
INSERT INTO `lend_item_return` VALUES ('24', '16', '13', '5', '24', '10000.00', '5', '0.10', '1', '1110.86', '47.06', '1157.92', '0.00', '2022-10-28', null, '0', null, '0', '2022-05-28 19:14:06', '2022-05-28 19:14:06', '0');
INSERT INTO `lend_item_return` VALUES ('25', '17', '13', '5', '24', '10000.00', '6', '0.10', '1', '1120.12', '37.80', '1157.92', '0.00', '2022-11-28', null, '0', null, '0', '2022-05-28 19:14:06', '2022-05-28 19:14:06', '0');
INSERT INTO `lend_item_return` VALUES ('26', '18', '13', '5', '24', '10000.00', '7', '0.10', '1', '1129.45', '28.47', '1157.92', '0.00', '2022-12-28', null, '0', null, '0', '2022-05-28 19:14:06', '2022-05-28 19:14:06', '0');
INSERT INTO `lend_item_return` VALUES ('27', '19', '13', '5', '24', '10000.00', '8', '0.10', '1', '1138.86', '19.06', '1157.92', '0.00', '2023-01-28', null, '0', null, '0', '2022-05-28 19:14:06', '2022-05-28 19:14:06', '0');
INSERT INTO `lend_item_return` VALUES ('28', '20', '13', '5', '24', '10000.00', '9', '0.10', '1', '1074.59', '83.33', '1157.92', '0.00', '2023-02-28', null, '0', null, '0', '2022-05-28 19:14:06', '2022-05-28 19:14:06', '0');
INSERT INTO `lend_item_return` VALUES ('29', '12', '14', '5', '26', '5000.00', '1', '0.10', '1', '537.30', '41.66', '578.96', '0.00', '2022-06-28', '2022-05-28 19:28:50', '0', null, '1', '2022-05-28 19:14:06', '2022-05-28 19:14:06', '0');
INSERT INTO `lend_item_return` VALUES ('30', '13', '14', '5', '26', '5000.00', '2', '0.10', '1', '541.78', '37.18', '578.96', '0.00', '2022-07-28', '2022-05-29 11:56:40', '0', null, '1', '2022-05-28 19:14:06', '2022-05-28 19:14:06', '0');
INSERT INTO `lend_item_return` VALUES ('31', '14', '14', '5', '26', '5000.00', '3', '0.10', '1', '546.29', '32.67', '578.96', '0.00', '2022-08-28', null, '0', null, '0', '2022-05-28 19:14:06', '2022-05-28 19:14:06', '0');
INSERT INTO `lend_item_return` VALUES ('32', '15', '14', '5', '26', '5000.00', '4', '0.10', '1', '550.84', '28.12', '578.96', '0.00', '2022-09-28', null, '0', null, '0', '2022-05-28 19:14:06', '2022-05-28 19:14:06', '0');
INSERT INTO `lend_item_return` VALUES ('33', '16', '14', '5', '26', '5000.00', '5', '0.10', '1', '555.43', '23.53', '578.96', '0.00', '2022-10-28', null, '0', null, '0', '2022-05-28 19:14:06', '2022-05-28 19:14:06', '0');
INSERT INTO `lend_item_return` VALUES ('34', '17', '14', '5', '26', '5000.00', '6', '0.10', '1', '560.06', '18.90', '578.96', '0.00', '2022-11-28', null, '0', null, '0', '2022-05-28 19:14:06', '2022-05-28 19:14:06', '0');
INSERT INTO `lend_item_return` VALUES ('35', '18', '14', '5', '26', '5000.00', '7', '0.10', '1', '564.73', '14.23', '578.96', '0.00', '2022-12-28', null, '0', null, '0', '2022-05-28 19:14:06', '2022-05-28 19:14:06', '0');
INSERT INTO `lend_item_return` VALUES ('36', '19', '14', '5', '26', '5000.00', '8', '0.10', '1', '569.43', '9.53', '578.96', '0.00', '2023-01-28', null, '0', null, '0', '2022-05-28 19:14:06', '2022-05-28 19:14:06', '0');
INSERT INTO `lend_item_return` VALUES ('37', '20', '14', '5', '26', '5000.00', '9', '0.10', '1', '537.30', '41.66', '578.96', '0.00', '2023-02-28', null, '0', null, '0', '2022-05-28 19:14:06', '2022-05-28 19:14:06', '0');

-- ----------------------------
-- Table structure for lend_return
-- ----------------------------
DROP TABLE IF EXISTS `lend_return`;
CREATE TABLE `lend_return` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `lend_id` bigint(20) DEFAULT NULL COMMENT '标的id',
  `borrow_info_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '借款信息id',
  `return_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '还款批次号',
  `user_id` bigint(20) DEFAULT NULL COMMENT '借款人用户id',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '借款金额',
  `base_amount` decimal(10,2) DEFAULT NULL COMMENT '计息本金额',
  `current_period` int(11) DEFAULT NULL COMMENT '当前的期数',
  `lend_year_rate` decimal(10,2) DEFAULT NULL COMMENT '年化利率',
  `return_method` tinyint(3) DEFAULT NULL COMMENT '还款方式 1-等额本息 2-等额本金 3-每月还息一次还本 4-一次还本',
  `principal` decimal(10,2) DEFAULT NULL COMMENT '本金',
  `interest` decimal(10,2) DEFAULT NULL COMMENT '利息',
  `total` decimal(10,2) DEFAULT NULL COMMENT '本息',
  `fee` decimal(10,2) DEFAULT '0.00' COMMENT '手续费',
  `return_date` date DEFAULT NULL COMMENT '还款时指定的还款日期',
  `real_return_time` datetime DEFAULT NULL COMMENT '实际发生的还款时间',
  `is_overdue` tinyint(1) DEFAULT NULL COMMENT '是否逾期',
  `overdue_total` decimal(10,2) DEFAULT NULL COMMENT '逾期金额',
  `is_last` tinyint(1) DEFAULT NULL COMMENT '是否最后一次还款',
  `status` tinyint(3) DEFAULT NULL COMMENT '状态（0-未归还 1-已归还）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_return_no` (`return_no`) USING BTREE,
  KEY `idx_lend_id` (`lend_id`) USING BTREE,
  KEY `idx_borrow_info_id` (`borrow_info_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='还款记录表';

-- ----------------------------
-- Records of lend_return
-- ----------------------------
INSERT INTO `lend_return` VALUES ('11', '4', '10', 'RETURN20220527125403283', '17', '2000.00', '2000.00', '1', '0.12', '2', '2000.00', '20.00', '2020.00', '0.00', '2022-06-25', '2022-05-27 21:18:55', '0', null, '1', '1', '2022-05-27 12:54:04', '2022-05-27 12:54:05', '0');
INSERT INTO `lend_return` VALUES ('12', '5', '11', 'RETURN20220528191405662', '25', '15000.00', '15000.00', '1', '0.10', '1', '1611.89', '124.99', '1736.88', '0.00', '2022-06-28', '2022-05-28 19:28:50', '0', null, '0', '1', '2022-05-28 19:14:06', '2022-05-28 19:14:06', '0');
INSERT INTO `lend_return` VALUES ('13', '5', '11', 'RETURN20220528191405644', '25', '15000.00', '15000.00', '2', '0.10', '1', '1625.33', '111.55', '1736.88', '0.00', '2022-07-28', '2022-05-29 11:56:40', '0', null, '0', '1', '2022-05-28 19:14:06', '2022-05-28 19:14:06', '0');
INSERT INTO `lend_return` VALUES ('14', '5', '11', 'RETURN20220528191405811', '25', '15000.00', '15000.00', '3', '0.10', '1', '1638.87', '98.01', '1736.88', '0.00', '2022-08-28', null, '0', null, '0', '0', '2022-05-28 19:14:06', '2022-05-28 19:14:06', '0');
INSERT INTO `lend_return` VALUES ('15', '5', '11', 'RETURN20220528191405547', '25', '15000.00', '15000.00', '4', '0.10', '1', '1652.52', '84.36', '1736.88', '0.00', '2022-09-28', null, '0', null, '0', '0', '2022-05-28 19:14:06', '2022-05-28 19:14:06', '0');
INSERT INTO `lend_return` VALUES ('16', '5', '11', 'RETURN20220528191405755', '25', '15000.00', '15000.00', '5', '0.10', '1', '1666.29', '70.59', '1736.88', '0.00', '2022-10-28', null, '0', null, '0', '0', '2022-05-28 19:14:06', '2022-05-28 19:14:06', '0');
INSERT INTO `lend_return` VALUES ('17', '5', '11', 'RETURN20220528191405584', '25', '15000.00', '15000.00', '6', '0.10', '1', '1680.18', '56.70', '1736.88', '0.00', '2022-11-28', null, '0', null, '0', '0', '2022-05-28 19:14:06', '2022-05-28 19:14:06', '0');
INSERT INTO `lend_return` VALUES ('18', '5', '11', 'RETURN20220528191405553', '25', '15000.00', '15000.00', '7', '0.10', '1', '1694.18', '42.70', '1736.88', '0.00', '2022-12-28', null, '0', null, '0', '0', '2022-05-28 19:14:06', '2022-05-28 19:14:06', '0');
INSERT INTO `lend_return` VALUES ('19', '5', '11', 'RETURN20220528191405819', '25', '15000.00', '15000.00', '8', '0.10', '1', '1708.29', '28.59', '1736.88', '0.00', '2023-01-28', null, '0', null, '0', '0', '2022-05-28 19:14:06', '2022-05-28 19:14:06', '0');
INSERT INTO `lend_return` VALUES ('20', '5', '11', 'RETURN20220528191405557', '25', '15000.00', '15000.00', '9', '0.10', '1', '1611.89', '124.99', '1736.88', '0.00', '2023-02-28', null, '0', null, '1', '0', '2022-05-28 19:14:06', '2022-05-28 19:14:06', '0');

-- ----------------------------
-- Table structure for trans_flow
-- ----------------------------
DROP TABLE IF EXISTS `trans_flow`;
CREATE TABLE `trans_flow` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户id',
  `user_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户名称',
  `trans_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '交易单号',
  `trans_type` tinyint(3) NOT NULL DEFAULT '0' COMMENT '交易类型（1：充值 2：提现 3：投标 4：投资回款 ...）',
  `trans_type_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '交易类型名称',
  `trans_amount` decimal(10,2) DEFAULT NULL COMMENT '交易金额',
  `memo` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_trans_no` (`trans_no`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=95 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='交易流水表';

-- ----------------------------
-- Records of trans_flow
-- ----------------------------
INSERT INTO `trans_flow` VALUES ('57', '23', '小红帽', 'CHARGE20220522145737440', '1', '充值', '20000.00', '充值20000元', '2022-05-22 14:57:43', '2022-05-22 14:57:43', '0');
INSERT INTO `trans_flow` VALUES ('58', '23', '小红帽', 'CHARGE20220523094820650', '8', '提现', '19000.00', '充值19000元', '2022-05-23 09:48:31', '2022-05-23 09:48:31', '0');
INSERT INTO `trans_flow` VALUES ('59', '22', '吉吉', 'CHARGE20220526103843566', '1', '充值', '10000.00', '充值10000元', '2022-05-26 10:38:48', '2022-05-26 10:38:48', '0');
INSERT INTO `trans_flow` VALUES ('60', '22', '吉吉', 'CHARGE20220526104248625', '1', '充值', '2000.00', '充值2000元', '2022-05-26 10:42:56', '2022-05-26 10:42:56', '0');
INSERT INTO `trans_flow` VALUES ('61', '21', '光头强', 'CHARGE20220526130722341', '1', '充值', '100.00', '充值100元', '2022-05-26 13:07:27', '2022-05-26 13:07:27', '0');
INSERT INTO `trans_flow` VALUES ('62', '21', '光头强', 'CHARGE20220526130917023', '8', '提现', '100.00', '提现100元', '2022-05-26 13:09:22', '2022-05-26 13:09:22', '0');
INSERT INTO `trans_flow` VALUES ('63', '22', '吉吉', 'INVEST20220526202720061', '2', '投标锁定', '200.00', '投资  项目编号：LEND20220525210259121，项目名称美容贷', '2022-05-26 20:27:26', '2022-05-26 20:27:26', '0');
INSERT INTO `trans_flow` VALUES ('64', '18', '王五', 'CHARGE20220526203754435', '1', '充值', '5000.00', '充值5000元', '2022-05-26 20:38:56', '2022-05-26 20:38:56', '0');
INSERT INTO `trans_flow` VALUES ('65', '18', '王五', 'INVEST20220526203918651', '2', '投标锁定', '1800.00', '投资  项目编号：LEND20220525210259121，项目名称美容贷', '2022-05-26 20:39:24', '2022-05-26 20:39:24', '0');
INSERT INTO `trans_flow` VALUES ('73', '17', '李四', 'LOAN20220527125403104', '5', '放款到账', '1991.67', '项目放款，项目编号：LEND20220525210259121，项目名称：美容贷', '2022-05-27 12:54:04', '2022-05-27 12:54:04', '0');
INSERT INTO `trans_flow` VALUES ('74', '22', '吉吉', 'TRANS20220527125403483', '3', '投标解锁', '200.00', '项目放款投标解锁，项目编号：LEND20220525210259121，项目名称：美容贷', '2022-05-27 12:54:04', '2022-05-27 12:56:40', '0');
INSERT INTO `trans_flow` VALUES ('75', '18', '王五', 'TRANS20220527125403880', '3', '投标解锁', '1800.00', '项目放款投标解锁，项目编号：LEND20220525210259121，项目名称：美容贷', '2022-05-27 12:54:04', '2022-05-27 12:56:43', '0');
INSERT INTO `trans_flow` VALUES ('76', '17', '李四', 'CHARGE20220527211817479', '1', '充值', '1000.00', '充值1000元', '2022-05-27 21:18:38', '2022-05-27 21:18:38', '0');
INSERT INTO `trans_flow` VALUES ('77', '17', '李四', 'RETURN20220527125403283', '6', '还款扣减', '2020.00', '借款人还款扣减，项目编号：LEND20220525210259121，项目名称：美容贷', '2022-05-27 21:18:55', '2022-05-27 21:18:55', '0');
INSERT INTO `trans_flow` VALUES ('78', '22', '吉吉', 'RETURNITEM20220527211855687', '7', '出借回款', '202.00', '还款到账，项目编号：LEND20220525210259121，项目名称：美容贷', '2022-05-27 21:18:55', '2022-05-27 21:18:55', '0');
INSERT INTO `trans_flow` VALUES ('79', '18', '王五', 'RETURNITEM20220527211855133', '7', '出借回款', '1818.00', '还款到账，项目编号：LEND20220525210259121，项目名称：美容贷', '2022-05-27 21:18:55', '2022-05-27 21:18:55', '0');
INSERT INTO `trans_flow` VALUES ('80', '24', '张佳佳', 'CHARGE20220528182223889', '1', '充值', '10000.00', '充值10000元', '2022-05-28 18:22:34', '2022-05-28 18:22:34', '0');
INSERT INTO `trans_flow` VALUES ('81', '24', '张佳佳', 'CHARGE20220528182242283', '8', '提现', '2000.00', '提现2000元', '2022-05-28 18:22:49', '2022-05-28 18:22:49', '0');
INSERT INTO `trans_flow` VALUES ('82', '24', '张佳佳', 'CHARGE20220528183007071', '1', '充值', '20000.00', '充值20000元', '2022-05-28 18:30:14', '2022-05-28 18:30:14', '0');
INSERT INTO `trans_flow` VALUES ('83', '24', '张佳佳', 'INVEST20220528183231773', '2', '投标锁定', '10000.00', '投资  项目编号：LEND20220528182859274，项目名称旅游借款', '2022-05-28 18:32:38', '2022-05-28 18:32:38', '0');
INSERT INTO `trans_flow` VALUES ('84', '26', '王欣', 'CHARGE20220528191206497', '1', '充值', '50000.00', '充值50000元', '2022-05-28 19:12:12', '2022-05-28 19:12:12', '0');
INSERT INTO `trans_flow` VALUES ('85', '26', '王欣', 'INVEST20220528191244942', '2', '投标锁定', '5000.00', '投资  项目编号：LEND20220528182859274，项目名称旅游借款', '2022-05-28 19:12:51', '2022-05-28 19:12:51', '0');
INSERT INTO `trans_flow` VALUES ('86', '25', '莫名', 'LOAN20220528191404012', '5', '放款到账', '14437.50', '项目放款，项目编号：LEND20220528182859274，项目名称：旅游借款', '2022-05-28 19:14:05', '2022-05-28 19:14:05', '0');
INSERT INTO `trans_flow` VALUES ('87', '24', '张佳佳', 'TRANS20220528191404288', '3', '投标解锁', '10000.00', '项目放款投标解锁，项目编号：LEND20220528182859274，项目名称：旅游借款', '2022-05-28 19:14:05', '2022-05-28 19:14:05', '0');
INSERT INTO `trans_flow` VALUES ('88', '26', '王欣', 'TRANS20220528191405286', '3', '投标解锁', '5000.00', '项目放款投标解锁，项目编号：LEND20220528182859274，项目名称：旅游借款', '2022-05-28 19:14:05', '2022-05-28 19:14:05', '0');
INSERT INTO `trans_flow` VALUES ('89', '25', '莫名', 'RETURN20220528191405662', '6', '还款扣减', '1736.88', '借款人还款扣减，项目编号：LEND20220528182859274，项目名称：旅游借款', '2022-05-28 19:28:51', '2022-05-28 19:28:51', '0');
INSERT INTO `trans_flow` VALUES ('90', '24', '张佳佳', 'RETURNITEM20220528192850315', '7', '出借回款', '1157.92', '还款到账，项目编号：LEND20220528182859274，项目名称：旅游借款', '2022-05-28 19:28:51', '2022-05-28 19:28:51', '0');
INSERT INTO `trans_flow` VALUES ('91', '26', '王欣', 'RETURNITEM20220528192850731', '7', '出借回款', '578.96', '还款到账，项目编号：LEND20220528182859274，项目名称：旅游借款', '2022-05-28 19:28:51', '2022-05-28 19:28:51', '0');
INSERT INTO `trans_flow` VALUES ('92', '25', '莫名', 'RETURN20220528191405644', '6', '还款扣减', '1736.88', '借款人还款扣减，项目编号：LEND20220528182859274，项目名称：旅游借款', '2022-05-29 11:56:41', '2022-05-29 11:56:41', '0');
INSERT INTO `trans_flow` VALUES ('93', '24', '张佳佳', 'RETURNITEM20220529115640670', '7', '出借回款', '1157.92', '还款到账，项目编号：LEND20220528182859274，项目名称：旅游借款', '2022-05-29 11:56:41', '2022-05-29 11:56:41', '0');
INSERT INTO `trans_flow` VALUES ('94', '26', '王欣', 'RETURNITEM20220529115640394', '7', '出借回款', '578.96', '还款到账，项目编号：LEND20220528182859274，项目名称：旅游借款', '2022-05-29 11:56:41', '2022-05-29 11:56:41', '0');

-- ----------------------------
-- Table structure for user_account
-- ----------------------------
DROP TABLE IF EXISTS `user_account`;
CREATE TABLE `user_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户id',
  `amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '帐户可用余额',
  `freeze_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '冻结金额',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户账户';

-- ----------------------------
-- Records of user_account
-- ----------------------------
INSERT INTO `user_account` VALUES ('12', '16', '0.00', '0.00', '2022-05-22 12:17:52', '2022-05-22 12:17:52', '0', '0');
INSERT INTO `user_account` VALUES ('13', '17', '971.67', '0.00', '2022-05-22 12:24:40', '2022-05-27 21:18:55', '0', '0');
INSERT INTO `user_account` VALUES ('14', '18', '5018.00', '0.00', '2022-05-22 12:26:56', '2022-05-27 21:18:55', '0', '0');
INSERT INTO `user_account` VALUES ('15', '19', '0.00', '0.00', '2022-05-22 12:33:13', '2022-05-22 12:33:13', '0', '0');
INSERT INTO `user_account` VALUES ('16', '20', '0.00', '0.00', '2022-05-22 12:43:06', '2022-05-22 12:43:06', '0', '0');
INSERT INTO `user_account` VALUES ('17', '21', '0.00', '0.00', '2022-05-22 12:58:02', '2022-05-26 13:09:22', '0', '0');
INSERT INTO `user_account` VALUES ('18', '22', '12002.00', '0.00', '2022-05-22 13:04:13', '2022-05-27 21:18:55', '0', '0');
INSERT INTO `user_account` VALUES ('19', '23', '1000.00', '0.00', '2022-05-22 13:12:12', '2022-05-23 09:48:31', '0', '0');
INSERT INTO `user_account` VALUES ('20', '24', '20315.84', '0.00', '2022-05-28 18:20:27', '2022-05-29 11:56:41', '0', '0');
INSERT INTO `user_account` VALUES ('21', '25', '10963.74', '0.00', '2022-05-28 18:23:25', '2022-05-29 11:56:41', '0', '0');
INSERT INTO `user_account` VALUES ('22', '26', '46157.92', '0.00', '2022-05-28 19:01:32', '2022-05-29 11:56:41', '0', '0');
INSERT INTO `user_account` VALUES ('23', '1', '0.00', '0.00', '2022-05-29 11:03:28', '2022-05-29 11:03:28', '0', '0');
INSERT INTO `user_account` VALUES ('24', '27', '0.00', '0.00', '2022-05-29 11:55:59', '2022-05-29 11:55:59', '0', '0');

-- ----------------------------
-- Table structure for user_bind
-- ----------------------------
DROP TABLE IF EXISTS `user_bind`;
CREATE TABLE `user_bind` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户id',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户姓名',
  `id_card` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '身份证号',
  `bank_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '银行卡号',
  `bank_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '银行类型',
  `mobile` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '手机号',
  `bind_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '绑定账户协议号',
  `status` tinyint(3) DEFAULT NULL COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户绑定表';

-- ----------------------------
-- Records of user_bind
-- ----------------------------
INSERT INTO `user_bind` VALUES ('15', '16', '张三', '465466', '4246846', '民生银行', '15879543145', '14e98cded78c45e59f227064269f295b', '1', '2022-05-22 12:18:40', '2022-05-22 12:18:40', '0');
INSERT INTO `user_bind` VALUES ('16', '17', '李四', '46483549', '1384964651', '交通银行', '15896743148', '275b030501704a74b64e0ed00d0098e5', '1', '2022-05-22 12:25:41', '2022-05-22 12:25:41', '0');
INSERT INTO `user_bind` VALUES ('17', '18', '王五', '465465456564', '65498649', '建设银行', '15389748941', 'd1f8cbd363ce49129fe140943d052fc7', '1', '2022-05-22 12:27:37', '2022-05-22 12:27:37', '0');
INSERT INTO `user_bind` VALUES ('18', '19', '熊大', '15645683218916', '215468232654', '工商银行', '15893647821', '0dcfbc7ea3c84b3ca4c8ff7d35a38668', '1', '2022-05-22 12:34:04', '2022-05-22 12:34:04', '0');
INSERT INTO `user_bind` VALUES ('19', '20', '熊二', '4646541654', '65468463', '桂林银行', '18963214871', 'c2c21d60893c4b72bf1b829311894c16', '1', '2022-05-22 12:43:49', '2022-05-22 12:43:49', '0');
INSERT INTO `user_bind` VALUES ('20', '21', '光头强', '1564654165', '13546784162', '中国银行', '15748965478', '25a71966051d4c5796da7c663f0d201a', '1', '2022-05-22 12:58:40', '2022-05-22 12:58:40', '0');
INSERT INTO `user_bind` VALUES ('21', '22', '吉吉', '1356464', '135431', '广发银行', '17874129412', '83469e7f0e484f47be740988e02946a1', '1', '2022-05-22 13:04:50', '2022-05-22 13:04:50', '0');
INSERT INTO `user_bind` VALUES ('22', '23', '小红帽', '6467498465', '5468431863', '广发银行', '14512341234', 'a9c4d86aec91494b9f4036e834fa70f3', '1', '2022-05-22 13:12:51', '2022-05-22 13:12:51', '0');
INSERT INTO `user_bind` VALUES ('23', '24', '张佳佳', '478965896541234785', '4578935478951235478', '广发银行', '15912391234', '1d1913e5fb694a6db0d414c8580a016a', '1', '2022-05-28 18:21:52', '2022-05-28 18:21:52', '0');
INSERT INTO `user_bind` VALUES ('24', '25', '莫名', '789546132141234789', '589646133124578964', '中国银行', '15896478451', '8254297d77834a6cb5e15e98266787a6', '1', '2022-05-28 18:24:40', '2022-05-28 18:24:40', '0');
INSERT INTO `user_bind` VALUES ('25', '26', '王欣', '489745643487895413', '4897445612478954189', '交通银行', '15896374874', '84104f19064242afb01d8f1634b9ffb5', '1', '2022-05-28 19:11:43', '2022-05-28 19:11:43', '0');

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_type` tinyint(3) NOT NULL DEFAULT '0' COMMENT '1：出借人 2：借款人',
  `mobile` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '手机号',
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户密码',
  `nick_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户昵称',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户姓名',
  `id_card` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '身份证号',
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮箱',
  `openid` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '微信用户标识openid',
  `head_img` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '头像',
  `bind_status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '绑定状态（0：未绑定，1：绑定成功 -1：绑定失败）',
  `borrow_auth_status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '借款人认证状态（0：未认证 1：认证中 2：认证通过 -1：认证失败）',
  `bind_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '绑定账户协议号',
  `integral` int(11) NOT NULL DEFAULT '0' COMMENT '用户积分',
  `status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态（0：锁定 1：正常）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `uk_mobile` (`mobile`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户基本信息';

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES ('16', '1', '15879543145', 'd93a5def7511da3d0f2d171d9c344e91', '15879543145', '张三', '465466', null, null, 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/avatar/2022/05/23/85be180b-21d1-42cb-b305-74afdaf76e28.jpg', '1', '0', '14e98cded78c45e59f227064269f295b', '0', '1', '2022-05-22 12:17:52', '2022-05-23 19:45:38', '0');
INSERT INTO `user_info` VALUES ('17', '2', '15896743148', 'd93a5def7511da3d0f2d171d9c344e91', '15896743148', '李四', '46483549', null, null, 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/avatar/2022/05/23/85be180b-21d1-42cb-b305-74afdaf76e28.jpg', '1', '2', '275b030501704a74b64e0ed00d0098e5', '220', '1', '2022-05-22 12:24:40', '2022-05-23 19:45:43', '0');
INSERT INTO `user_info` VALUES ('18', '1', '15389748941', 'd93a5def7511da3d0f2d171d9c344e91', '15389748941', '王五', '465465456564', null, null, 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/avatar/2022/05/23/85be180b-21d1-42cb-b305-74afdaf76e28.jpg', '1', '0', 'd1f8cbd363ce49129fe140943d052fc7', '0', '1', '2022-05-22 12:26:56', '2022-05-23 19:45:58', '0');
INSERT INTO `user_info` VALUES ('19', '2', '15893647821', 'd93a5def7511da3d0f2d171d9c344e91', '15893647821', '熊大', '15645683218916', null, null, 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/avatar/2022/05/23/85be180b-21d1-42cb-b305-74afdaf76e28.jpg', '1', '2', '0dcfbc7ea3c84b3ca4c8ff7d35a38668', '30', '1', '2022-05-22 12:33:13', '2022-05-23 19:46:01', '0');
INSERT INTO `user_info` VALUES ('20', '2', '18963214871', 'd93a5def7511da3d0f2d171d9c344e91', '18963214871', '熊二', '4646541654', null, null, 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/avatar/2022/05/23/85be180b-21d1-42cb-b305-74afdaf76e28.jpg', '1', '2', 'c2c21d60893c4b72bf1b829311894c16', '30', '1', '2022-05-22 12:43:06', '2022-05-25 19:15:11', '0');
INSERT INTO `user_info` VALUES ('21', '2', '15748965478', 'd93a5def7511da3d0f2d171d9c344e91', '15748965478', '光头强', '1564654165', null, null, 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/avatar/2022/05/23/85be180b-21d1-42cb-b305-74afdaf76e28.jpg', '1', '2', '25a71966051d4c5796da7c663f0d201a', '25', '1', '2022-05-22 12:58:02', '2022-05-23 19:46:05', '0');
INSERT INTO `user_info` VALUES ('22', '1', '17874129412', 'd93a5def7511da3d0f2d171d9c344e91', '17874129412', '吉吉', '1356464', null, null, 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/avatar/2022/05/23/85be180b-21d1-42cb-b305-74afdaf76e28.jpg', '1', '0', '83469e7f0e484f47be740988e02946a1', '0', '1', '2022-05-22 13:04:13', '2022-05-23 19:46:07', '0');
INSERT INTO `user_info` VALUES ('23', '2', '14512341234', 'd93a5def7511da3d0f2d171d9c344e91', '14512341234', '小红帽', '6467498465', null, null, 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/avatar/2022/05/23/85be180b-21d1-42cb-b305-74afdaf76e28.jpg', '1', '2', 'a9c4d86aec91494b9f4036e834fa70f3', '30', '1', '2022-05-22 13:12:12', '2022-05-25 19:23:46', '0');
INSERT INTO `user_info` VALUES ('24', '1', '15912391234', 'd93a5def7511da3d0f2d171d9c344e91', '15912391234', '张佳佳', '478965896541234785', null, null, 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/avatar/2022/05/23/85be180b-21d1-42cb-b305-74afdaf76e28.jpg', '1', '0', '1d1913e5fb694a6db0d414c8580a016a', '0', '1', '2022-05-28 18:20:27', '2022-05-28 18:20:27', '0');
INSERT INTO `user_info` VALUES ('25', '2', '15896478451', 'd93a5def7511da3d0f2d171d9c344e91', '15896478451', '莫名', '789546132141234789', null, null, 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/avatar/2022/05/23/85be180b-21d1-42cb-b305-74afdaf76e28.jpg', '1', '2', '8254297d77834a6cb5e15e98266787a6', '220', '1', '2022-05-28 18:23:25', '2022-05-28 18:23:25', '0');
INSERT INTO `user_info` VALUES ('26', '1', '15896374874', 'd93a5def7511da3d0f2d171d9c344e91', '15896374874', '王欣', '489745643487895413', null, null, 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/avatar/2022/05/23/85be180b-21d1-42cb-b305-74afdaf76e28.jpg', '1', '0', '84104f19064242afb01d8f1634b9ffb5', '0', '1', '2022-05-28 19:01:32', '2022-05-28 19:01:32', '0');
INSERT INTO `user_info` VALUES ('27', '1', '18965474121', 'd93a5def7511da3d0f2d171d9c344e91', '18965474121', '18965474121', null, null, null, 'https://loan-202205.oss-cn-hangzhou.aliyuncs.com/avatar/2022/05/23/85be180b-21d1-42cb-b305-74afdaf76e28.jpg', '0', '0', null, '0', '1', '2022-05-29 11:55:59', '2022-05-29 11:55:59', '0');

-- ----------------------------
-- Table structure for user_integral
-- ----------------------------
DROP TABLE IF EXISTS `user_integral`;
CREATE TABLE `user_integral` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `integral` int(11) DEFAULT NULL COMMENT '积分',
  `content` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '获取积分说明',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户积分记录表';

-- ----------------------------
-- Records of user_integral
-- ----------------------------
INSERT INTO `user_integral` VALUES ('22', '23', '30', '身份信息获取30积分;', '2022-05-25 11:04:09', '2022-05-25 11:04:09', '0');
INSERT INTO `user_integral` VALUES ('23', '21', '25', '身份信息获取25积分;', '2022-05-25 15:57:07', '2022-05-25 15:57:07', '0');
INSERT INTO `user_integral` VALUES ('24', '19', '30', '身份信息获取30积分;', '2022-05-25 19:13:11', '2022-05-25 19:13:11', '0');
INSERT INTO `user_integral` VALUES ('25', '20', '30', '身份信息获取30积分;', '2022-05-25 19:16:37', '2022-05-25 19:16:37', '0');
INSERT INTO `user_integral` VALUES ('26', '17', '220', '身份信息获取30积分;身份证信息获取30积分;车辆信息获取60积分;房产证信息获取100积分;', '2022-05-25 19:22:23', '2022-05-25 19:22:23', '0');
INSERT INTO `user_integral` VALUES ('27', '25', '220', '身份信息获取30积分;身份证信息获取30积分;车辆信息获取60积分;房产证信息获取100积分;', '2022-05-28 18:26:33', '2022-05-28 18:26:33', '0');

-- ----------------------------
-- Table structure for user_login_record
-- ----------------------------
DROP TABLE IF EXISTS `user_login_record`;
CREATE TABLE `user_login_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'ip',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户登录记录表';

-- ----------------------------
-- Records of user_login_record
-- ----------------------------
INSERT INTO `user_login_record` VALUES ('35', '13', '202.193.74.80', '2022-05-21 13:48:37', '2022-05-21 13:48:37', '0');
INSERT INTO `user_login_record` VALUES ('36', '13', '202.193.74.80', '2022-05-21 16:13:52', '2022-05-21 16:13:52', '0');
INSERT INTO `user_login_record` VALUES ('37', '15', '202.193.74.80', '2022-05-21 17:01:10', '2022-05-21 17:01:10', '0');
INSERT INTO `user_login_record` VALUES ('38', '15', '202.193.74.80', '2022-05-21 17:05:21', '2022-05-21 17:05:21', '0');
INSERT INTO `user_login_record` VALUES ('39', '13', '202.193.74.80', '2022-05-22 09:33:57', '2022-05-22 09:33:57', '0');
INSERT INTO `user_login_record` VALUES ('40', '13', '202.193.74.80', '2022-05-22 12:11:45', '2022-05-22 12:11:45', '0');
INSERT INTO `user_login_record` VALUES ('41', '16', '202.193.74.80', '2022-05-22 12:18:05', '2022-05-22 12:18:05', '0');
INSERT INTO `user_login_record` VALUES ('42', '17', '202.193.74.80', '2022-05-22 12:25:04', '2022-05-22 12:25:04', '0');
INSERT INTO `user_login_record` VALUES ('43', '18', '202.193.74.80', '2022-05-22 12:27:10', '2022-05-22 12:27:10', '0');
INSERT INTO `user_login_record` VALUES ('44', '19', '202.193.74.80', '2022-05-22 12:33:21', '2022-05-22 12:33:21', '0');
INSERT INTO `user_login_record` VALUES ('45', '20', '202.193.74.80', '2022-05-22 12:43:21', '2022-05-22 12:43:21', '0');
INSERT INTO `user_login_record` VALUES ('46', '21', '202.193.74.80', '2022-05-22 12:58:08', '2022-05-22 12:58:08', '0');
INSERT INTO `user_login_record` VALUES ('47', '22', '202.193.74.80', '2022-05-22 13:04:23', '2022-05-22 13:04:23', '0');
INSERT INTO `user_login_record` VALUES ('48', '23', '202.193.74.80', '2022-05-22 13:12:17', '2022-05-22 13:12:17', '0');
INSERT INTO `user_login_record` VALUES ('49', '16', '202.193.74.80', '2022-05-23 15:16:15', '2022-05-23 15:16:15', '0');
INSERT INTO `user_login_record` VALUES ('50', '23', '202.193.74.80', '2022-05-23 15:19:27', '2022-05-23 15:19:27', '0');
INSERT INTO `user_login_record` VALUES ('51', '22', '202.193.74.80', '2022-05-23 19:37:37', '2022-05-23 19:37:37', '0');
INSERT INTO `user_login_record` VALUES ('52', '21', '202.193.74.80', '2022-05-23 19:38:28', '2022-05-23 19:38:28', '0');
INSERT INTO `user_login_record` VALUES ('53', '21', '202.193.74.80', '2022-05-23 21:56:16', '2022-05-23 21:56:16', '0');
INSERT INTO `user_login_record` VALUES ('54', '21', '202.193.74.80', '2022-05-23 21:58:14', '2022-05-23 21:58:14', '0');
INSERT INTO `user_login_record` VALUES ('55', '21', '202.193.74.80', '2022-05-23 21:59:08', '2022-05-23 21:59:08', '0');
INSERT INTO `user_login_record` VALUES ('56', '23', '202.193.74.80', '2022-05-23 23:00:51', '2022-05-23 23:00:51', '0');
INSERT INTO `user_login_record` VALUES ('57', '23', '202.193.74.80', '2022-05-23 23:28:03', '2022-05-23 23:28:03', '0');
INSERT INTO `user_login_record` VALUES ('58', '23', '202.193.74.80', '2022-05-25 10:05:21', '2022-05-25 10:05:21', '0');
INSERT INTO `user_login_record` VALUES ('59', '21', '202.193.74.80', '2022-05-25 15:35:14', '2022-05-25 15:35:14', '0');
INSERT INTO `user_login_record` VALUES ('60', '23', '202.193.74.80', '2022-05-25 15:35:44', '2022-05-25 15:35:44', '0');
INSERT INTO `user_login_record` VALUES ('61', '21', '202.193.74.80', '2022-05-25 15:50:01', '2022-05-25 15:50:01', '0');
INSERT INTO `user_login_record` VALUES ('62', '19', '202.193.74.80', '2022-05-25 19:12:05', '2022-05-25 19:12:05', '0');
INSERT INTO `user_login_record` VALUES ('63', '20', '202.193.74.80', '2022-05-25 19:14:54', '2022-05-25 19:14:54', '0');
INSERT INTO `user_login_record` VALUES ('64', '17', '202.193.74.80', '2022-05-25 19:21:20', '2022-05-25 19:21:20', '0');
INSERT INTO `user_login_record` VALUES ('65', '22', '202.193.74.80', '2022-05-25 22:27:42', '2022-05-25 22:27:42', '0');
INSERT INTO `user_login_record` VALUES ('66', '23', '202.193.74.80', '2022-05-25 22:29:39', '2022-05-25 22:29:39', '0');
INSERT INTO `user_login_record` VALUES ('67', '22', '202.193.74.80', '2022-05-25 22:30:02', '2022-05-25 22:30:02', '0');
INSERT INTO `user_login_record` VALUES ('68', '22', '202.193.74.80', '2022-05-26 10:59:10', '2022-05-26 10:59:10', '0');
INSERT INTO `user_login_record` VALUES ('69', '21', '202.193.74.80', '2022-05-26 13:02:46', '2022-05-26 13:02:46', '0');
INSERT INTO `user_login_record` VALUES ('70', '22', '202.193.74.80', '2022-05-26 15:06:11', '2022-05-26 15:06:11', '0');
INSERT INTO `user_login_record` VALUES ('71', '17', '202.193.74.80', '2022-05-26 20:35:24', '2022-05-26 20:35:24', '0');
INSERT INTO `user_login_record` VALUES ('72', '18', '202.193.74.80', '2022-05-26 20:37:43', '2022-05-26 20:37:43', '0');
INSERT INTO `user_login_record` VALUES ('73', '17', '202.193.74.80', '2022-05-27 16:29:40', '2022-05-27 16:29:40', '0');
INSERT INTO `user_login_record` VALUES ('74', '22', '202.193.74.80', '2022-05-27 17:05:33', '2022-05-27 17:05:33', '0');
INSERT INTO `user_login_record` VALUES ('75', '23', '202.193.74.80', '2022-05-27 20:01:57', '2022-05-27 20:01:57', '0');
INSERT INTO `user_login_record` VALUES ('76', '17', '202.193.74.80', '2022-05-27 20:13:42', '2022-05-27 20:13:42', '0');
INSERT INTO `user_login_record` VALUES ('77', '22', '202.193.74.80', '2022-05-27 20:17:09', '2022-05-27 20:17:09', '0');
INSERT INTO `user_login_record` VALUES ('78', '17', '202.193.74.80', '2022-05-27 20:27:06', '2022-05-27 20:27:06', '0');
INSERT INTO `user_login_record` VALUES ('79', '23', '202.193.74.80', '2022-05-28 16:10:31', '2022-05-28 16:10:31', '0');
INSERT INTO `user_login_record` VALUES ('80', '24', '202.193.74.80', '2022-05-28 18:20:38', '2022-05-28 18:20:38', '0');
INSERT INTO `user_login_record` VALUES ('81', '25', '202.193.74.80', '2022-05-28 18:23:44', '2022-05-28 18:23:44', '0');
INSERT INTO `user_login_record` VALUES ('82', '24', '202.193.74.80', '2022-05-28 18:29:49', '2022-05-28 18:29:49', '0');
INSERT INTO `user_login_record` VALUES ('83', '26', '202.193.74.80', '2022-05-28 19:01:46', '2022-05-28 19:01:46', '0');
INSERT INTO `user_login_record` VALUES ('84', '24', '202.193.74.80', '2022-05-28 19:30:23', '2022-05-28 19:30:23', '0');
INSERT INTO `user_login_record` VALUES ('85', '24', '202.193.74.80', '2022-05-29 10:57:53', '2022-05-29 10:57:53', '0');
INSERT INTO `user_login_record` VALUES ('86', '24', '202.193.74.80', '2022-05-29 10:58:10', '2022-05-29 10:58:10', '0');
INSERT INTO `user_login_record` VALUES ('87', '24', '202.193.74.80', '2022-05-29 11:55:13', '2022-05-29 11:55:13', '0');
INSERT INTO `user_login_record` VALUES ('88', '27', '202.193.74.80', '2022-05-29 11:56:11', '2022-05-29 11:56:11', '0');
INSERT INTO `user_login_record` VALUES ('89', '25', '202.193.74.80', '2022-05-29 11:56:25', '2022-05-29 11:56:25', '0');
