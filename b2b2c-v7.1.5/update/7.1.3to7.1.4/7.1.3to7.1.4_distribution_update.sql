-- 分销相关功能表修改 add_by liushui
alter table es_commission_tpl drop column num;
alter table es_commission_tpl drop column profit;
alter table es_commission_tpl drop column switch_model;
ALTER TABLE `es_withdraw_apply` ADD COLUMN `sn` varchar(255) DEFAULT null COMMENT '编号';
ALTER TABLE `es_withdraw_apply` ADD COLUMN `ip` varchar(255) DEFAULT null COMMENT 'ip地址';