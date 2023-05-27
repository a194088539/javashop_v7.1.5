-- 店铺详情表新增字段 add_by duanmingyu 2019-06-20
ALTER TABLE `es_shop_detail` ADD COLUMN `ordin_receipt_status` int(1) DEFAULT 0 COMMENT '是否允许开具增值税普通发票 0：否，1：是';
ALTER TABLE `es_shop_detail` ADD COLUMN `elec_receipt_status` int(1) DEFAULT 0 COMMENT '是否允许开具电子普通发票 0：否，1：是';
ALTER TABLE `es_shop_detail` ADD COLUMN `tax_receipt_status` int(1) DEFAULT 0 COMMENT '是否允许开具增值税专用发票 0：否，1：是';

-- ----------------------------
--  Table structure for `es_member_zpzz`
--  新增会员增票资质表 add_by duanmingyu 2019-06-20
-- ----------------------------
DROP TABLE IF EXISTS `es_member_zpzz`;
CREATE TABLE `es_member_zpzz` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `member_id` int(10) DEFAULT NULL COMMENT '会员ID',
  `uname` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '会员用户名',
  `status` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '状态 NEW_APPLY：新申请，AUDIT_PASS：审核通过，AUDIT_REFUSE：审核未通过',
  `company_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '单位名称',
  `taxpayer_code` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '纳税人识别码',
  `register_address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '公司注册地址',
  `register_tel` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '公司注册电话',
  `bank_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '开户银行',
  `bank_account` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '银行账户',
  `audit_remark` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '平台审核备注',
  `apply_time` bigint(20) DEFAULT NULL COMMENT '申请时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
--  Table structure for `es_receipt_address`
--  新增会员收票地址表 add_by duanmingyu 2019-06-20
-- ----------------------------
DROP TABLE IF EXISTS `es_receipt_address`;
CREATE TABLE `es_receipt_address` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `member_id` int(10) DEFAULT NULL COMMENT '会员ID',
  `member_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收票人姓名',
  `member_mobile` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收票人手机号',
  `province_id` int(10) DEFAULT NULL COMMENT '收票地址-所属省份ID',
  `city_id` int(10) DEFAULT NULL COMMENT '收票地址-所属城市ID',
  `county_id` int(10) DEFAULT NULL COMMENT '收票地址-所属区县ID',
  `town_id` int(10) DEFAULT NULL COMMENT '收票地址-所属乡镇ID',
  `province` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收票地址-所属省份',
  `city` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收票地址-所属城市',
  `county` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收票地址-所属区县',
  `town` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收票地址-所属乡镇',
  `detail_addr` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收票地址-详细地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
--  Table structure for `es_member_receipt`
--  新增会员发票信息缓存表 add_by duanmingyu 2019-06-20
-- ----------------------------
DROP TABLE IF EXISTS `es_member_receipt`;
CREATE TABLE `es_member_receipt` (
  `receipt_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `member_id` int(10) DEFAULT NULL COMMENT '会员ID',
  `receipt_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发票类型 ELECTRO：电子普通发票，VATORDINARY：增值税普通发票',
  `receipt_title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发票抬头',
  `receipt_content` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发票内容',
  `tax_no` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '纳税人识别号',
  `member_mobile` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收票人手机号',
  `member_email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收票人邮箱',
  `is_default` smallint(1) DEFAULT NULL COMMENT '是否为默认选项 0：否，1：是',
  PRIMARY KEY (`receipt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
--  Table structure for `es_receipt_history`
--  新增会员开票历史记录表 add_by duanmingyu 2019-06-20
-- ----------------------------
DROP TABLE IF EXISTS `es_receipt_history`;
CREATE TABLE `es_receipt_history` (
  `history_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_sn` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单编号',
  `order_price` decimal(20,2) DEFAULT NULL COMMENT '订单金额',
  `seller_id` int(10) DEFAULT NULL COMMENT '开票商家ID',
  `seller_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '开票商家',
  `member_id` int(10) DEFAULT NULL COMMENT '会员ID',
  `status` smallint(1) DEFAULT NULL COMMENT '开票状态 0：未开，1：已开',
  `receipt_method` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '开票方式(针对增值税专用发票)',
  `receipt_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发票类型 ELECTRO：电子普通发票，VATORDINARY：增值税普通发票，VATOSPECIAL：增值税专用发票',
  `logi_id` int(10) DEFAULT NULL COMMENT '物流公司ID',
  `logi_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '物流公司名称',
  `logi_code` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '快递单号',
  `receipt_title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发票抬头',
  `receipt_content` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发票内容',
  `tax_no` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '纳税人识别号',
  `reg_addr` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '注册地址',
  `reg_tel` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '注册电话',
  `bank_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '开户银行',
  `bank_account` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '银行账户',
  `member_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收票人名称',
  `member_mobile` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收票人手机号',
  `member_email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收票人邮箱',
  `province_id` int(10) DEFAULT NULL COMMENT '收票地址-所属省份ID',
  `city_id` int(10) DEFAULT NULL COMMENT '收票地址-所属城市ID',
  `county_id` int(10) DEFAULT NULL COMMENT '收票地址-所属区县ID',
  `town_id` int(10) DEFAULT NULL COMMENT '收票地址-所属乡镇ID',
  `province` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收票地址-所属省份',
  `city` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收票地址-所属城市',
  `county` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收票地址-所属区县',
  `town` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收票地址-所属乡镇',
  `detail_addr` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收票地址-详细地址',
  `add_time` bigint(20) DEFAULT NULL COMMENT '申请开票日期',
  `goods_json` longtext COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '订单商品信息',
  `order_status` varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '订单出库状态，NEW/CONFIRM',
  `uname` varchar(100) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '会员名称',
  PRIMARY KEY (`history_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
--  Table structure for `es_receipt_file`
--  新增电子发票附件表 add_by duanmingyu 2019-06-24
-- ----------------------------
DROP TABLE IF EXISTS `es_receipt_file`;
CREATE TABLE `es_receipt_file` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `history_id` int(10) DEFAULT NULL,
  `elec_file` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `es_history` (
`id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
`goods_id` int(10) DEFAULT NULL COMMENT '商品id',
`goods_name` varchar(255) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '商品名称',
`goods_price` decimal(20,2) DEFAULT NULL COMMENT '商品价格',
`goods_img` varchar(255) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '商品主图',
`member_id` int(10) DEFAULT NULL COMMENT '会员id',
`member_name` varchar(255) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '会员名称',
`create_time` bigint(20) DEFAULT NULL COMMENT '创建时间，按天存',
`update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 会员评论表添加审核、评论类型、初评id字段 add by liuyulei 2019-06-25
alter table es_member_comment add audit_status varchar(255) default 'WAIT_AUDIT';
alter table es_member_comment add comments_type varchar(255) default 'INITIAL';
alter table es_member_comment add parent_id int(10) default 0;
-- 评论回复表添加回复类型字段 add by liuyulei 2019-06-25
alter table es_comment_reply add reply_type varchar(255) default 'INITIAL';
-- 删除没有用到的字段 add by liuyulei 2019-06-25
ALTER TABLE `es_member_comment`
DROP COLUMN `additional_status`,
DROP COLUMN `additional_content`,
DROP COLUMN `additional_time`,
DROP COLUMN `additional_have_image`;
ALTER TABLE `es_comment_gallery`
DROP COLUMN `img_belong`;

-- 会员咨询表添加审核状态字段  add by liuyulei 2019-07-29
alter table es_member_ask
	add auth_status varchar(255) default 'WAIT_AUDIT' comment '审核状态';

-- 店铺菜单表新增数据 add_by duanmingyu 2019-8-5
INSERT INTO `es_shop_menu` VALUES ('51', '4', '拼团管理', 'assembleManager', '/seller/promotion/pintuan.*', '0', ',4,51,', '2');
INSERT INTO `es_shop_menu` VALUES ('52', '7', '发票设置', 'invoiceSettings', '/seller/shops.*', '0', ',7,52', '2');

-- ----------------------------
--  Table structure for `es_shop_logistics_setting`
-- ----------------------------
DROP TABLE IF EXISTS `es_shop_logistics_setting`;
CREATE TABLE `es_shop_logistics_setting` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `shop_id` int(10) DEFAULT NULL,
  `logistics_id` int(10) DEFAULT NULL,
  `config` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8;