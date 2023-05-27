-- 店铺菜单表新增数据 add_by duanmingyu 2019-9-4
INSERT INTO `es_shop_menu` VALUES ('53', '1', '待审核商品', 'authGoodsList', '/seller/goods.*', '0', ',1,53', '2');

-- 结算单优惠券相关 add by fengkun 2019-09-14
ALTER TABLE `es_member_coupon`
ADD COLUMN `use_scope` varchar(20) DEFAULT NULL COMMENT '使用范围，全品，分类，部分商品' AFTER `seller_name`,
ADD COLUMN `scope_id` longtext DEFAULT NULL COMMENT '全品或者商家优惠券时为0<br />分类时为分类id<br />部分商品时为商品ID集合' AFTER `use_scope`;

-- 会员问题咨询表新增字段  add by duanmingyu 2019-09-16
alter table es_member_ask add goods_img varchar(255) default null comment '商品图片';
alter table es_member_ask add anonymous varchar(255) default 'NO' comment '是否匿名 YES:是，NO:否';
alter table es_member_ask add reply_num int(10) default 0 comment '会员问题咨询回复数量';
alter table es_member_ask modify column reply_status varchar(20);
alter table es_member_ask modify column status varchar(20);

-- ----------------------------
--  Table structure for `es_ask_message`
--  新增会员商品咨询消息表 add by duanmingyu 2019-09-16
-- ----------------------------
DROP TABLE IF EXISTS `es_ask_message`;
CREATE TABLE `es_ask_message` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `member_id` int(10) DEFAULT NULL COMMENT '会员ID',
  `goods_id` int(10) DEFAULT NULL COMMENT '商品ID',
  `goods_name` varchar(255) DEFAULT NULL COMMENT '商品名称',
  `goods_img` varchar(255) DEFAULT NULL COMMENT '商品图片',
  `ask_id` int(10) DEFAULT NULL COMMENT '会员咨询id',
  `ask` varchar(255) DEFAULT NULL COMMENT '咨询内容',
  `ask_member` varchar(50) DEFAULT NULL COMMENT '咨询会员',
  `reply_id` int(10) DEFAULT NULL COMMENT '会员回复咨询id',
  `reply` varchar(255) DEFAULT NULL COMMENT '回复内容',
  `reply_member` varchar(50) DEFAULT NULL COMMENT '回复会员',
  `send_time` bigint(20) DEFAULT NULL COMMENT '消息发送时间',
  `is_del` varchar(20) DEFAULT NULL COMMENT '删除状态 DELETED：已删除 NORMAL：正常',
  `is_read` varchar(20) DEFAULT NULL COMMENT '是否已读 YES：是 NO：否',
  `receive_time` bigint(20) DEFAULT NULL COMMENT '消息接收时间',
  `msg_type` varchar(20) DEFAULT NULL COMMENT '咨询消息类型 ASK：提问消息 REPLY：回复消息',
  `ask_anonymous` varchar(20) DEFAULT NULL COMMENT '咨询人是否匿名 YES:是，NO:否',
  `reply_anonymous` varchar(20) DEFAULT NULL COMMENT '回复咨询人是否匿名 YES:是，NO:否',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
--  Table structure for `es_ask_reply`
--  新增会员商品咨询回复表 add by duanmingyu 2019-09-16
-- ----------------------------
DROP TABLE IF EXISTS `es_ask_reply`;
CREATE TABLE `es_ask_reply` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `ask_id` int(10) DEFAULT NULL COMMENT '会员咨询id',
  `member_id` int(10) DEFAULT NULL COMMENT '会员id',
  `member_name` varchar(50) DEFAULT NULL COMMENT '会员名称',
  `content` varchar(255) DEFAULT NULL COMMENT '回复内容',
  `reply_time` bigint(20) DEFAULT NULL COMMENT '回复时间',
  `anonymous` varchar(20) DEFAULT NULL COMMENT '是否匿名 YES:是，NO:否',
  `auth_status` varchar(20) DEFAULT NULL COMMENT '审核状态 WAIT_AUDIT:待审核,PASS_AUDIT:审核通过,REFUSE_AUDIT:审核未通过',
  `is_del` varchar(20) DEFAULT NULL COMMENT '删除状态 DELETED：已删除 NORMAL：正常',
  `reply_status` varchar(20) DEFAULT NULL COMMENT '是否已回复 YES:是，NO:否',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `ES_INDEX_ASK_ID` (`ask_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 会员评论表新增字段  add by duanmingyu 2019-10-12
alter table es_member_comment add goods_img varchar(255) default null comment '商品图片';

-- 扩大经营类目字段值
ALTER TABLE `es_shop_detail` CHANGE COLUMN `goods_management_category` `goods_management_category` longtext DEFAULT NULL COMMENT '店铺经营类目';

-- 店铺菜单表新增数据 add_by duanmingyu 2019-11-30
INSERT INTO `es_shop_menu` VALUES ('54', '2', '售后管理', 'afterSale', '/seller/after-sales.*', '0', ',2,54', '2');
INSERT INTO `es_shop_menu` VALUES ('55', '54', '售后服务列表', 'serviceList', '/seller/after-sales.*', '0', ',2,54,55', '3');
INSERT INTO `es_shop_menu` VALUES ('56', '54', '退款单列表', 'refundList', '/seller/after-sales/refund.*', '0', ',2,54,56', '3');
delete from es_shop_menu where id = 15;

-- 店铺菜单增加交易投诉菜单 add by fk 2019.12.16
INSERT INTO `es_shop_menu` VALUES ('57', '2', '交易投诉', 'complaintList', '/seller/trade/order-complains.*', '0', ',2,57,', '2');
