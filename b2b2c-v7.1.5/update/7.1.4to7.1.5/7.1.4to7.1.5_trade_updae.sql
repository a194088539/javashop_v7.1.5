-- 团购活动表新增字段  add by duanmingyu 2019-08-30
alter table es_groupbuy_active add delete_status varchar(20) default 'NORMAL' comment '是否删除 DELETED：已删除，NORMAL：正常';
alter table es_groupbuy_active add delete_reason varchar(255) default null comment '删除原因';
alter table es_groupbuy_active add delete_time bigint(20) default null comment '删除时间';
alter table es_groupbuy_active add delete_name varchar(100) default null comment '删除人';

-- 限时抢购活动表新增字段  add by duanmingyu 2019-08-31
alter table es_seckill add delete_status varchar(20) default 'NORMAL' comment '是否删除 DELETED：已删除，NORMAL：正常';

-- 结算单优惠券相关 add by fengkun 2019-09-14
ALTER TABLE `es_coupon`
ADD COLUMN `type` varchar(20) NOT NULL DEFAULT 'FREE_GET' COMMENT '优惠券类型，分为免费领取和活动赠送' AFTER `seller_name`,
ADD COLUMN `use_scope` varchar(20) DEFAULT NULL COMMENT '使用范围，全品，分类，部分商品' AFTER `type`,
ADD COLUMN `scope_id` longtext DEFAULT NULL COMMENT '全品或者商家优惠券时为0<br />分类时为分类id<br />部分商品时为商品ID集合' AFTER `use_scope`,
ADD COLUMN `shop_commission` int(5) DEFAULT NULL COMMENT '店铺承担比例，正整数' AFTER `scope_id`,
ADD COLUMN `scope_description` longtext DEFAULT NULL COMMENT '范围描述' AFTER `shop_commission`,
ADD COLUMN `activity_description` longtext DEFAULT NULL COMMENT '活动说明' AFTER `scope_description`;

-- 结算单优惠券相关 add by fengkun 2019-09-14
ALTER TABLE `es_bill_item`
ADD COLUMN `site_coupon_price` decimal(10,2) DEFAULT NULL DEFAULT '0' COMMENT '使用平台优惠券金额' AFTER `refund_time`,
ADD COLUMN `coupon_commission` int(10) DEFAULT NULL DEFAULT '0' COMMENT '优惠券佣金比例' AFTER `site_coupon_price`;

-- 结算单优惠券相关 add by fengkun 2019-09-14
ALTER TABLE `es_bill`
ADD COLUMN `site_coupon_commi` decimal(20,2) DEFAULT NULL DEFAULT '0' COMMENT '平台优惠券佣金' AFTER `distribution_return_rebate`;

-- 交易快照增加会员id
ALTER TABLE `es_goods_snapshot` ADD COLUMN `member_id` int(10) NOT NULL COMMENT '会员id' AFTER `coupon_json`;

-- ----------------------------
--  Table structure for `es_as_order`
--  新增售后服务单表 add by duanmingyu 2019-10-29
-- ----------------------------
DROP TABLE IF EXISTS `es_as_order`;
CREATE TABLE `es_as_order` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `sn` varchar(50) DEFAULT NULL COMMENT '售后单号',
  `order_sn` varchar(50) DEFAULT NULL COMMENT '订单编号',
  `member_id` int(10) DEFAULT NULL COMMENT '用户ID',
  `member_name` varchar(100) DEFAULT NULL COMMENT '用户名',
  `seller_id` int(10) DEFAULT NULL COMMENT '商家ID',
  `seller_name` varchar(255) DEFAULT NULL COMMENT '商家名称',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `mobile` varchar(50) DEFAULT NULL COMMENT '手机号',
  `service_type` varchar(50) DEFAULT NULL COMMENT '售后类型 RETURN_GOODS：退货，CHANGE_GOODS：换货，SUPPLY_AGAIN_GOODS：补发货品，ORDER_CANCEL：取消订单（订单已付款且未收货之前）',
  `service_status` varchar(50) DEFAULT NULL COMMENT '售后单状态 APPLY：申请，PASS：审核通过，REFUSE：审核拒绝，WAIT_FOR_MANUAL：待人工处理，STOCK_IN：退货入库，REFUNDING：退款中，REFUNDFAIL：退款失败，COMPLETE：完成',
  `reason` varchar(255) DEFAULT NULL COMMENT '申请原因',
  `apply_vouchers` varchar(255) DEFAULT NULL COMMENT '申请凭证',
  `problem_desc` longtext COMMENT '问题描述',
  `goods_json` longtext COMMENT '售后商品信息json',
  `disabled` varchar(50) DEFAULT NULL COMMENT '删除状态 DELETED：已删除 NORMAL：正常',
  `audit_remark` varchar(255) DEFAULT NULL COMMENT '商家审核备注',
  `stock_remark` varchar(255) DEFAULT NULL COMMENT '商家入库备注',
  `refund_remark` varchar(255) DEFAULT NULL COMMENT '平台退款备注',
  `close_reason` varchar(255) DEFAULT NULL COMMENT '取消原因',
  `return_addr` varchar(500) DEFAULT NULL COMMENT '退货地址信息',
  `new_order_sn` varchar(255) DEFAULT NULL COMMENT '新订单编号',
  PRIMARY KEY (`id`),
  KEY `ES_INDEX_ID` (`id`) USING BTREE,
  KEY `ES_INDEX_SN` (`sn`) USING BTREE,
  KEY `ES_INDEX_ORDER_SN` (`order_sn`) USING BTREE,
  KEY `ES_INDEX_SERVICE_TYPE` (`service_type`) USING BTREE,
  KEY `ES_INDEX_SERVICE_STATUS` (`service_status`) USING BTREE,
  KEY `ES_INDEX_MEMBER_ID` (`member_id`) USING BTREE,
  KEY `ES_INDEX_SELLER_ID` (`seller_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='新增售后服务单表(es_as_order)';

-- ----------------------------
--  Table structure for `es_as_refund`
--  新增售后服务退款相关信息表 add by duanmingyu 2019-10-29
-- ----------------------------
DROP TABLE IF EXISTS `es_as_refund`;
CREATE TABLE `es_as_refund` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `service_sn` varchar(50) DEFAULT NULL COMMENT '售后服务单号',
  `refund_price` decimal(20,2) DEFAULT NULL COMMENT '申请的退款金额',
  `agree_price` decimal(20,2) DEFAULT NULL COMMENT '商家同意的退款金额',
  `actual_price` decimal(20,2) DEFAULT NULL COMMENT '实际退款金额',
  `refund_way` varchar(255) DEFAULT NULL COMMENT '退款方式 ORIGINAL：原路退回，OFFLINE：线下支付',
  `account_type` varchar(255) DEFAULT NULL COMMENT '账号类型',
  `return_account` varchar(255) DEFAULT NULL COMMENT '退款账号',
  `bank_name` varchar(255) DEFAULT NULL COMMENT '银行名称',
  `bank_account_number` varchar(255) DEFAULT NULL COMMENT '银行账户',
  `bank_account_name` varchar(255) DEFAULT NULL COMMENT '银行开户名',
  `bank_deposit_name` varchar(255) DEFAULT NULL COMMENT '开户行',
  `pay_order_no` varchar(255) DEFAULT NULL COMMENT '订单支付方式返回的交易号',
  `refund_time` bigint(20) DEFAULT NULL COMMENT '退款时间',
  PRIMARY KEY (`id`),
  KEY `ES_INDEX_ID` (`id`) USING BTREE,
  KEY `ES_INDEX_SERVICE_SN` (`service_sn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='售后服务退款相关信息表(es_as_refund)';

-- ----------------------------
--  Table structure for `es_as_goods`
--  新增售后服务商品信息表 add by duanmingyu 2019-10-29
-- ----------------------------
DROP TABLE IF EXISTS `es_as_goods`;
CREATE TABLE `es_as_goods` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `service_sn` varchar(50) DEFAULT NULL COMMENT '售后服务单号',
  `goods_id` int(10) DEFAULT NULL COMMENT '商品ID',
  `sku_id` int(10) DEFAULT NULL COMMENT '商品SKUID',
  `ship_num` int(10) DEFAULT NULL COMMENT '发货数量',
  `price` decimal(20,2) DEFAULT NULL COMMENT '商品成交价',
  `return_num` int(10) DEFAULT NULL COMMENT '退还数量',
  `storage_num` int(10) DEFAULT NULL COMMENT '入库数量',
  `goods_name` varchar(255) DEFAULT NULL COMMENT '商品名称',
  `goods_sn` varchar(255) DEFAULT NULL COMMENT '商品编号',
  `goods_image` varchar(255) DEFAULT NULL COMMENT '商品缩略图',
  `spec_json` varchar(255) DEFAULT NULL COMMENT '商品规格信息',
  PRIMARY KEY (`id`),
  KEY `ES_INDEX_ID` (`id`) USING BTREE,
  KEY `ES_INDEX_SERVICE_SN` (`service_sn`) USING BTREE,
  KEY `ES_INDEX_GOODS_NAME` (`goods_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='售后服务商品信息表(es_as_goods)';

-- ----------------------------
--  Table structure for `es_as_change`
--  新增售后服务用户收货地址信息表 add by duanmingyu 2019-10-29
-- ----------------------------
DROP TABLE IF EXISTS `es_as_change`;
CREATE TABLE `es_as_change` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `service_sn` varchar(50) DEFAULT NULL COMMENT '售后服务单号',
  `ship_name` varchar(50) DEFAULT NULL COMMENT '收货人姓名',
  `province_id` int(10) DEFAULT NULL COMMENT '收货地址省份id',
  `city_id` int(10) DEFAULT NULL COMMENT '收货地址城市id',
  `county_id` int(10) DEFAULT NULL COMMENT '收货地址区县id',
  `town_id` int(10) DEFAULT NULL COMMENT '收货地址城镇id',
  `province` varchar(255) DEFAULT NULL COMMENT '收货地址省份名称',
  `city` varchar(255) DEFAULT NULL COMMENT '收货地址城市名称',
  `county` varchar(255) DEFAULT NULL COMMENT '收货地址县(区)名称',
  `town` varchar(255) DEFAULT NULL COMMENT '收货地址城镇名称',
  `ship_addr` varchar(255) DEFAULT NULL COMMENT '收货地址',
  `ship_mobile` varchar(50) DEFAULT NULL COMMENT '收货人手机号',
  PRIMARY KEY (`id`),
  KEY `ES_INDEX_ID` (`id`) USING BTREE,
  KEY `ES_INDEX_SERVICE_SN` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='售后服务用户收货地址信息表(es_as_change)';

-- ----------------------------
--  Table structure for `es_as_express`
--  新增售后服务用户退还商品物流信息表 add by duanmingyu 2019-10-29
-- ----------------------------
DROP TABLE IF EXISTS `es_as_express`;
CREATE TABLE `es_as_express` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `service_sn` varchar(50) DEFAULT NULL COMMENT '售后服务单号',
  `courier_number` varchar(255) DEFAULT NULL COMMENT '物流单号',
  `courier_company_id` int(10) DEFAULT NULL COMMENT '物流公司id',
  `courier_company` varchar(255) DEFAULT NULL COMMENT '物流公司名称',
  `ship_time` bigint(20) DEFAULT NULL COMMENT '发货时间',
  PRIMARY KEY (`id`),
  KEY `ES_INDEX_ID` (`id`) USING BTREE,
  KEY `ES_INDEX_SERVICE_SN` (`service_sn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='售后服务用户退还商品物流信息表(es_as_express)';

-- ----------------------------
--  Table structure for `es_as_gallery`
--  新增售后服务图片信息表 add by duanmingyu 2019-10-29
-- ----------------------------
DROP TABLE IF EXISTS `es_as_gallery`;
CREATE TABLE `es_as_gallery` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `service_sn` varchar(50) DEFAULT NULL COMMENT '售后单号',
  `img` varchar(255) DEFAULT NULL COMMENT '图片链接',
  PRIMARY KEY (`id`),
  KEY `ES_INDEX_ID` (`id`) USING BTREE,
  KEY `ES_INDEX_SERVICE_SN` (`service_sn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='售后服务图片信息表(es_as_gallery)';

-- ----------------------------
--  Table structure for `es_as_log`
--  新增售后服务日志记录表 add by duanmingyu 2019-10-29
-- ----------------------------
DROP TABLE IF EXISTS `es_as_log`;
CREATE TABLE `es_as_log` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `sn` varchar(255) DEFAULT NULL COMMENT '售后/退款编号',
  `log_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `log_detail` longtext COMMENT '详细信息',
  `operator` varchar(255) DEFAULT NULL COMMENT '操作者',
  PRIMARY KEY (`id`),
  KEY `ES_INDEX_ID` (`id`) USING BTREE,
  KEY `ES_INDEX_SN` (`sn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='售后服务日志记录表(es_as_log)';

-- 更新退款单表数据结构 add by duanmingyu 2019-10-29
alter table es_refund add mobile varchar(50) default null comment '手机号';
alter table es_refund add agree_price decimal(20,2) default null comment '商家同意的退款金额';
alter table es_refund add actual_price decimal(20,2) default null comment '实际退款金额';
alter table es_refund add goods_json longtext default null comment '售后商品信息json';
alter table es_refund add disabled varchar(20) default 'NORMAL' comment '删除状态 DELETED：已删除 NORMAL：正常';
alter table es_refund drop column refund_point;
alter table es_refund drop column customer_remark;
alter table es_refund drop column seller_remark;
alter table es_refund drop column warehouse_remark;
alter table es_refund drop column finance_remark;
alter table es_refund drop column refund_reason;
alter table es_refund drop column refuse_reason;
alter table es_refund drop column trade_sn;
alter table es_refund drop column refund_type;
alter table es_refund drop column refuse_type;
alter table es_refund drop column refund_gift;

-- 删除旧表 退货商品表和退款日志表 add by duanmingyu 2019-11-25
DROP TABLE es_refund_goods;
DROP TABLE es_refund_log;

-- 结算单表新增字段 add by duanmingyu 2019-12-2
alter table es_bill add order_total_price decimal(20,2) default 0.00 comment '结算周期内订单付款总金额';
alter table es_bill add refund_total_price decimal(20,2) default 0.00 comment '结算周期内订单退款总金额';

-- 修改订单类型  add by liuyulei 2019-12-3
update es_order set order_type = 'NORMAL' where order_type = 'normal';
update es_order set order_type = 'PINTUAN' where order_type = 'pintuan';

-- 交易投诉表  add by fk 2019-12-10
DROP TABLE IF EXISTS `es_order_complain`;
CREATE TABLE `es_order_complain` (
`complain_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
`complain_topic` varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '投诉主题',
`content` longtext COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '投诉内容',
`create_time` bigint(20) DEFAULT NULL COMMENT '投诉时间',
`images` longtext COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '投诉凭证图片',
`status` varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '状态，见ComplainStatusEnum.java',
`appeal_content` longtext COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '商家申诉内容',
`appeal_time` bigint(20) DEFAULT NULL COMMENT '商家申诉时间',
`appeal_images` longtext COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '商家申诉上传的图片',
`order_sn` varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '订单号',
`order_time` bigint(20) DEFAULT NULL COMMENT '下单时间',
`goods_name` varchar(255) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '商品名称',
`goods_id` int(10) DEFAULT NULL COMMENT '商品id',
`goods_price` decimal(10,2) DEFAULT NULL COMMENT '商品价格',
`num` int(10) DEFAULT NULL COMMENT '购买的商品数量',
`shipping_price` decimal(10,2) DEFAULT NULL COMMENT '运费',
`order_price` decimal(10,2) DEFAULT NULL COMMENT '订单金额',
`ship_no` varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '物流单号',
`seller_id` int(10) DEFAULT NULL COMMENT '商家id',
`seller_name` varchar(100) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '商家名称',
`member_id` int(10) DEFAULT NULL COMMENT '会员id',
`member_name` varchar(100) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '会员名称',
`ship_name` varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '收货人',
`ship_addr` varchar(255) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '收货地址',
`ship_mobile` varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '收货人手机',
`arbitration_result` longtext COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '仲裁结果',
`sku_id` int(10) DEFAULT NULL COMMENT 'sku 主键',
`goods_image` varchar(255) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '商品图片',
 PRIMARY KEY (`complain_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- 交易投诉对话表  add by fk 2019-12-10
DROP TABLE IF EXISTS `es_order_complain_communication`;
CREATE TABLE `es_order_complain_communication` (
`communication_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
`complain_id` int(10) DEFAULT NULL COMMENT '投诉id',
`content` longtext COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '对话内容',
`create_time` bigint(20) DEFAULT NULL COMMENT '对话时间',
`owner` varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '所属，买家/卖家',
`owner_name` varchar(100) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '对话所属名称',
`owner_id` int(10) DEFAULT NULL COMMENT '对话所属id,卖家id/买家id',
 PRIMARY KEY (`communication_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 售后服务单表新增字段 add by duanmingyu 2019-12-11
alter table es_as_order add create_channel varchar(255) default null comment '售后服务单创建渠道 NORMAL：正常渠道创建，PINTUAN：拼团失败自动创建';
alter table es_refund add create_channel varchar(255) default null comment '退款单创建渠道 NORMAL：正常渠道创建，PINTUAN：拼团失败自动创建';