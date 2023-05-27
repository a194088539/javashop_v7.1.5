-- 微信服务消息模板，2019年06月21日10:53:46，fk
CREATE TABLE `es_wechat_msg_template` (
`id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
`msg_tmp_name` varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '模板名称',
`msg_tmp_sn` varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '消息编号',
`template_id` varchar(100) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '模板ID',
`msg_first` varchar(255) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '消息开头文字',
`msg_remark` varchar(255) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '消息结尾备注文字',
`is_open` smallint(1) DEFAULT NULL COMMENT '是否开启',
`tmp_type` varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '模板类型，枚举',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;




-- ----------------------------
--  Table structure for `es_logistics_company`
-- ----------------------------
DROP TABLE IF EXISTS `es_logistics_company`;
CREATE TABLE `es_logistics_company` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `code` varchar(100) DEFAULT NULL,
  `is_waybill` int(1) DEFAULT NULL,
  `kdcode` varchar(100) DEFAULT NULL,
  `form_items` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `es_logistics_company`
-- ----------------------------
BEGIN;
INSERT INTO `es_logistics_company` VALUES ('1', '圆通速递', 'yuantong', '1', 'YTO', '[{\"name\":\"商家代码\",\"code\":\"customer_name\"},{\"name\":\"密钥串\",\"code\":\"month_code\"}]'), ('8', '韵达快递', 'yunda', '1', 'YD', '[{\"name\":\"客户ID\",\"code\":\"customer_name\"},{\"name\":\"接口联调密码\",\"code\":\"customer_pwd\"}]'), ('9', '顺丰物流', 'shunfeng', '1', 'SF', '[{\"name\":\"月结号\",\"code\":\"month_code\"}]'), ('10', '申通快递', 'shentong', '1', 'ST', '[{\"name\":\"客户简称\",\"code\":\"customer_name\"},{\"name\":\"客户密码\",\"code\":\"customer_pwd\"},{\"name\":\"所属网点\",\"code\":\"send_site\"}]'), ('11', '邮政', 'youzheng', '0', 'YZ', '[]'), ('12', '中通', 'zhongtong', '1', 'ZT', '[{\"name\":\"商家ID\",\"code\":\"customer_name\"},{\"name\":\"商家接口密码\",\"code\":\"customer_pwd\"}]');
COMMIT;

-- 管理端菜单表新增菜单数据 add_by duanmingyu 2019-8-5
INSERT INTO `es_menu` VALUES ('220', '2', '资质审核', null, 'receiptExamine', '/admin/members/zpzz.*', '0', ',2,220,', '2');
INSERT INTO `es_menu` VALUES ('221', '5', '拼团管理', '', 'assembleManage', '/admin/promotion.*', '0', ',5,221,', '2');
INSERT INTO `es_menu` VALUES ('222', '221', '拼团活动', null, 'assembleList', '/admin/promotion/pintuan.*', '0', ',5,221,222,', '3');
INSERT INTO `es_menu` VALUES ('223', '94', '微信消息', null, 'wechatMessage', '/admin/systems/wechat-msg-tmp.*', '0', ',8,94,223,', '3');
INSERT INTO `es_menu` VALUES ('224', '8', '搜索设置', null, 'searchSettings', '/admin/goodssearch.*', '0', ',8,224,', '2');
INSERT INTO `es_menu` VALUES ('225', '224', '搜索分词', null, 'searchKeyword', '/admin/goodssearch.*', '0', ',8,224,225,', '3');
INSERT INTO `es_menu` VALUES ('226', '224', '搜索历史', null, 'searchHistory', '/admin/goodssearch.*', '0', ',8,224,226,', '3');
INSERT INTO `es_menu` VALUES ('227', '224', '搜索提示词', null, 'searchTips', '/admin/goodssearch.*', '0', ',8,224,227,', '3');

-- 站内消息表新增字段  add by duanmingyu 2019-08-27
alter table es_message add disabled int(1) default 0 comment '是否删除 0：否，1：是';