--  sku表新增hash_code字段，用来标识规格唯一性
ALTER TABLE `es_goods_sku` ADD COLUMN `hash_code` int(10) NOT NULL AFTER `thumbnail`;

-- 删除无效的店铺标签
delete from goods.es_tags where seller_id not in ( select s.shop_id from member.es_shop s )
