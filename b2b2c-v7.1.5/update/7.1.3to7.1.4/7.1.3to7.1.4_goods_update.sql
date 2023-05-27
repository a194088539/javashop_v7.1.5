-- 关键字历史表
DROP TABLE IF EXISTS `es_keyword_search_history`;
CREATE TABLE `es_keyword_search_history` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `keyword` varchar(255) DEFAULT NULL COMMENT '关键字',
  `count` int(10) DEFAULT '0' COMMENT '搜索次数',
  `add_time` bigint(20) DEFAULT NULL COMMENT '新增时间',
  `modify_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- ----------------------------
-- Table structure for `es_custom_words`
-- ----------------------------
DROP TABLE IF EXISTS `es_custom_words`;
CREATE TABLE `es_custom_words` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `add_time` bigint(20) DEFAULT NULL COMMENT '添加时间',
  `disabled` smallint(1) DEFAULT NULL COMMENT '显示 1  隐藏 0',
  `modify_time` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='自定义分词表(es_custom_words)';



-- 商品表添加优先级字段   -- add by liuyulei 2019-06-10 14:23
ALTER TABLE `es_goods`
ADD COLUMN `priority`  int(2)  DEFAULT 1 COMMENT '优先级:高(3)、中(2)、低(1)' AFTER `under_message`;

-- 分词表添加类型和排序字段   --add by liuyulei 2019-06-14
ALTER TABLE `es_goods_words` ADD COLUMN `type`  varchar(255) NULL DEFAULT 'SYSTEM' ;
ALTER TABLE `es_goods_words` ADD COLUMN `sort`  int(10) NULL DEFAULT 0 ;
-- 补充商品分词表中的主键
ALTER TABLE `es_goods_words`
ADD COLUMN `id`  int(10) NOT NULL AUTO_INCREMENT FIRST ,
ADD PRIMARY KEY (`id`);

-- 更改下架原因长度为500
alter table es_goods modify under_message varchar(500) null comment '下架原因';