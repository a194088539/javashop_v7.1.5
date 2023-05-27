-- 订单表新增订单类型字段
ALTER TABLE `es_order` ADD COLUMN `order_type` varchar(50) NOT NULL DEFAULT 'normal';

-- 订单表新增订单数据字段，用于存储扩展数据
ALTER TABLE `es_order` ADD COLUMN `order_data` text;


DROP TABLE IF EXISTS `es_pintuan`;
CREATE TABLE `es_pintuan` (
  `promotion_id` int(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `promotion_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '活动名称',
  `promotion_title` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '活动标题',
  `promotion_description` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '活动描述',
  `start_time` bigint(20) DEFAULT NULL COMMENT '活动开始时间',
  `end_time` bigint(20) DEFAULT NULL COMMENT '活动结束时间',
  `required_num` int(10) DEFAULT NULL COMMENT '成团人数',
  `limit_num` int(10) DEFAULT NULL COMMENT '限购数量',
  `enable_mocker` smallint(1) DEFAULT NULL COMMENT '虚拟成团',
  `promotion_rule` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '拼团规则',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `seller_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `option_status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `seller_id` int(10) DEFAULT '1',
  PRIMARY KEY (`promotion_id`)
) ENGINE=InnoDB AUTO_INCREMENT=271 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ----------------------------
--  Table structure for `es_pintuan_order`
-- ----------------------------
DROP TABLE IF EXISTS `es_pintuan_order`;
CREATE TABLE `es_pintuan_order` (
  `order_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `order_sn` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单编号',
  `pintuan_id` int(10) DEFAULT NULL COMMENT '拼团id',
  `end_time` bigint(20) DEFAULT NULL COMMENT '结束时间',
  `sku_id` int(20) DEFAULT NULL COMMENT 'sku_id',
  `required_num` int(10) DEFAULT NULL COMMENT '成团人数',
  `offered_num` int(10) DEFAULT NULL COMMENT '已参团人数',
  `offered_persons` longtext COLLATE utf8mb4_unicode_ci COMMENT '参团人',
  `order_status` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单状态',
  `goods_id` int(20) NOT NULL DEFAULT '0',
  `thumbnail` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'http://javashop-statics.oss-cn-beijing.aliyuncs.com/demo/E74806A794874476918B0D0685BE25DF.jpg_400x400',
  `goods_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=113 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `es_pintuan_goods`;
CREATE TABLE `es_pintuan_goods` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sku_id` int(10) DEFAULT NULL COMMENT 'sku_id',
  `goods_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品名称',
  `origin_price` decimal(20,2) DEFAULT NULL COMMENT '原价',
  `sales_price` decimal(20,2) DEFAULT NULL COMMENT '活动价',
  `sn` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'sn',
  `sold_quantity` int(10) DEFAULT NULL COMMENT '已售数量',
  `locked_quantity` int(10) DEFAULT NULL COMMENT '待发货数量',
  `pintuan_id` int(10) DEFAULT NULL COMMENT '拼团活动id',
  `goods_id` int(20) DEFAULT NULL COMMENT 'goods_id',
  `specs` text COLLATE utf8mb4_unicode_ci,
  `seller_id` int(10) DEFAULT NULL,
  `seller_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `thumbnail` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=543 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
-- ----------------------------
--  Table structure for `es_pintuan_child_order`
-- ----------------------------
DROP TABLE IF EXISTS `es_pintuan_child_order`;
CREATE TABLE `es_pintuan_child_order` (
  `child_order_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '子订单id',
  `order_sn` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单编号',
  `member_id` int(20) DEFAULT NULL COMMENT '会员id',
  `sku_id` int(20) DEFAULT NULL COMMENT '会员id',
  `pintuan_id` int(20) DEFAULT NULL COMMENT '拼团活动id',
  `order_status` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单状态',
  `order_id` int(20) DEFAULT NULL COMMENT '主订单id',
  `member_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '买家名称',
  `origin_price` double(20,2) NOT NULL,
  `sales_price` double(20,2) NOT NULL,
  PRIMARY KEY (`child_order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=149 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 订单项表新增可退款金额
ALTER TABLE `es_order_items` ADD COLUMN `refund_price` decimal(20,2) DEFAULT 0.00 COMMENT '订单项可退款金额';
