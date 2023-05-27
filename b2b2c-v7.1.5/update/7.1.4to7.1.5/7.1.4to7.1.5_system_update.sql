-- 物流公司表新增字段  add by duanmingyu 2019-08-31
alter table es_logistics_company add delete_status varchar(255) default 'NORMAL' comment '是否删除 DELETED：已删除，NORMAL：正常';
alter table es_logistics_company add disabled varchar(255) default 'OPEN' comment '是否禁用 OPEN：开启，CLOSE：禁用';

-- 消息模板表新增示例数据 add by duanmingyu 2019-9-3
INSERT INTO `es_message_template` VALUES ('19', 'MEMBERORDERCREATE', '订单创建提醒', 'MEMBER', 'OPEN', 'CLOSED', 'CLOSED', '订单提示：您于${createTime}成功创建了订单单号为${ordersSn}的订单，请进入用户中心商品订单查看。', '订单提示：您于${createTime}成功创建了订单单号为${ordersSn}的订单，请进入用户中心商品订单查看。', '<p><span style=\"font-family: &#39;sans serif&#39;, tahoma, verdana, helvetica; font-size: 12px; line-height: 18px;\"><span style=\"font-family: &quot;sans serif&quot;, tahoma, verdana, helvetica; font-size: 12px;\"></span>${siteName}</span><span style=\"font-family: &#39;sans serif&#39;, tahoma, verdana, helvetica; font-size: 12px; line-height: 18px;\">订单</span><span style=\"font-family: &#39;sans serif&#39;, tahoma, verdana, helvetica; font-size: 12px; line-height: 18px;\">提示：</span></p><p style=\"white-space: normal; word-wrap: break-word; font-family: &#39;sans serif&#39;, tahoma, verdana, helvetica; font-size: 12px; line-height: 18px;\">您订单单号为<span style=\"font-family: &quot;sans serif&quot;, tahoma, verdana, helvetica; font-size: 12px;\"></span>${ordersSn}的订单已于<span style=\"font-family: &quot;sans serif&quot;, tahoma, verdana, helvetica; font-size: 12px;\"></span>${createTime}创建成功，请登录用户中心商品订单查看。</p><p><br/></p>', '订单创建提醒');

-- 修改系统设置表示例数据 add by duanmingyu 2019-9-4
update `es_settings` set `cfg_value`='{\"marcket_auth\":1,\"update_auth\":1,\"comment_auth\":1,\"ask_auth\":1,\"thumbnail_width\":300,\"thumbnail_height\":300,\"small_width\":400,\"small_height\":400,\"big_width\":800,\"big_height\":800}' where `id`='2';

-- 消息模板表新增示例数据 add by duanmingyu 2019-9-5
INSERT INTO `es_message_template` VALUES ('20', 'EMAILCODESEND', '邮箱发送验证码', 'MEMBER', 'CLOSED', 'CLOSED', 'CLOSED', '您正在进行${byName}，验证码是：${code}。', '您正在进行${byName}，验证码是：${code}。', '<p style=\"word-wrap: break-word; font-family: &#39;sans serif&#39;, tahoma, verdana, helvetica; font-size: 12px; line-height: 18px; white-space: normal; text-align: left;\"><span style=\"font-family: &quot;sans serif&quot;, tahoma, verdana, helvetica; font-size: 12px;\">${siteName}</span>您正在进行${byName}，</p><p style=\"white-space: normal; word-wrap: break-word; font-family: &#39;sans serif&#39;, tahoma, verdana, helvetica; font-size: 12px; line-height: 18px;\">您的<span style=\"font-family: &#39;sans serif&#39;, tahoma, verdana, helvetica; font-size: 12px; line-height: 18px;\"></span>验证码是：<span style=\"font-family: &quot;sans serif&quot;, tahoma, verdana, helvetica; font-size: 12px;\">${code}。</span></p><p><br/></p>', '邮件验证码');

-- 消息模板表新增示例数据 add by duanmingyu 2019-9-16
INSERT INTO `es_message_template` VALUES ('21', 'SHOPGOODSASK', '买家咨询提醒', 'SHOP', 'OPEN', 'CLOSED', 'CLOSED', '商品咨询提醒：买家${memberName}于${askTime}对店铺中商品名称为${goodsName}的商品进行了咨询，请进入商户中心咨询管理查看。', '商品咨询提醒：买家${memberName}于${askTime}对店铺中商品名称为${goodsName}的商品进行了咨询，请进入商户中心咨询管理查看。', '<p>商品咨询提醒：买家${memberName}于${askTime}对店铺中商品名称为${goodsName}的商品进行了咨询，请进入商户中心咨询管理查看。</p>', '买家咨询提醒');

-- 交易投诉主题表 by fk 2019-12-10
CREATE TABLE `es_complain_topic` (
`topic_id` int(10) NOT NULL AUTO_INCREMENT COMMENT ' 主键',
`topic_name` varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '投诉主题',
`create_time` bigint(20) DEFAULT NULL COMMENT '添加时间',
`topic_remark` varchar(100) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '主题说明',
 PRIMARY KEY (`topic_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 消息模板表新增示例数据 add by duanmingyu 2019-10-29
update `es_message_template` set `tpl_name`='售后服务退款提醒',`content`='售后服务退款提醒：您编号为${serviceSn}的${serviceType}售后服务正在进行退款，请进入个人中心-售后服务中查看详细信息。',`sms_content`='售后服务退款提醒：您编号为${serviceSn}的${serviceType}售后服务正在进行退款，请进入个人中心-售后服务中查看详细信息。',`email_content`='<p style="word-wrap: break-word; font-family: &#39;sans serif&#39;, tahoma, verdana, helvetica; font-size: 12px; line-height: 18px; white-space: normal;"><span style="font-family: &quot;sans serif&quot;, tahoma, verdana, helvetica; font-size: 12px;"></span>${siteName}售后服务退款提醒：</p><p style="word-wrap: break-word; font-family: &#39;sans serif&#39;, tahoma, verdana, helvetica; font-size: 12px; line-height: 18px; white-space: normal;">您编号为<span style="font-family: &quot;sans serif&quot;, tahoma, verdana, helvetica; font-size: 12px;"></span>${serviceSn}的${serviceType}售后服务正在进行退款，请进入个人中心-售后服务中查看详细信息。</p>',`email_title`='售后服务退款提醒' where `tpl_code`='MEMBERREFUNDUPDATE';
INSERT INTO `es_message_template` VALUES ('22', 'SHOPAFTERSALE', '订单申请售后服务提醒', 'SHOP', 'OPEN', 'CLOSED', 'CLOSED', '售后申请提醒：编号为${orderSn}的订单申请了${serviceType}售后服务，售后服务单号为${serviceSn}，请进入商家中心维权订单中查看。', '售后申请提醒：编号为${orderSn}的订单申请了${serviceType}售后服务，售后服务单号为${serviceSn}，请进入商家中心维权订单中查看。', '<p>售后申请提醒：编号为${orderSn}的订单申请了${serviceType}售后服务，售后服务单号为${serviceSn}，请进入商家中心维权订单中查看。</p>', '订单申请售后服务提醒');
INSERT INTO `es_message_template` VALUES ('23', 'SHOPAFTERSALEGOODSSHIP', '买家退还售后商品提醒', 'SHOP', 'OPEN', 'CLOSED', 'CLOSED', '买家退还售后商品提醒：编号为${serviceSn}的${serviceType}售后服务，买家已将申请售后服务的商品寄出，物流单号为${courierNumber}，请进入商家中心维权订单中查看。', '买家退还售后商品提醒：编号为${serviceSn}的${serviceType}售后服务，买家已将申请售后服务的商品寄出，物流单号为${courierNumber}，请进入商家中心维权订单中查看。', '<p>买家退还售后商品提醒：编号为${serviceSn}的${serviceType}售后服务，买家已将申请售后服务的商品寄出，物流单号为${courierNumber}，请进入商家中心维权订单中查看。</p>', '买家退还售后商品提醒');
INSERT INTO `es_message_template` VALUES ('24', 'MEMBERASAUDIT', '售后服务申请审核提醒', 'MEMBER', 'OPEN', 'CLOSED', 'CLOSED', '售后服务申请审核提醒：您编号为${serviceSn}的${serviceType}售后服务申请${auditStatus}，请进入个人中心-售后服务中查看详细信息。', '售后服务申请审核提醒：您编号为${serviceSn}的${serviceType}售后服务申请${auditStatus}，请进入个人中心-售后服务中查看详细信息。', '<p>售后服务申请审核通过提醒：您编号为${serviceSn}的${serviceType}售后服务申请${auditStatus}，请进入个人中心-售后服务中查看详细信息。</p>', '售后服务申请审核提醒');
INSERT INTO `es_message_template` VALUES ('25', 'MEMBERASCOMPLETED', '售后服务完成提醒', 'MEMBER', 'OPEN', 'CLOSED', 'CLOSED', '售后服务完成提醒：您编号为${serviceSn}的${serviceType}售后服务已完成，请进入个人中心-售后服务中查看详细信息。', '售后服务完成提醒：您编号为${serviceSn}的${serviceType}售后服务已完成，请进入个人中心-售后服务中查看详细信息。', '<p>售后服务完成提醒：您编号为${serviceSn}的${serviceType}售后服务已完成，请进入个人中心-售后服务中查看详细信息。</p>', '售后服务完成提醒');
INSERT INTO `es_message_template` VALUES ('26', 'MEMBERASAUDITRETURNCHANGE', '退货和换货售后服务审核通过提醒', 'MEMBER', 'OPEN', 'OPEN', 'CLOSED', '售后服务申请审核通过提醒：您编号为${serviceSn}的${serviceType}售后服务申请商家已审核通过，请尽快将商品退还给卖家，退货地址为【${returnAddr}】。', '售后服务申请审核通过提醒：您编号为${serviceSn}的${serviceType}售后服务申请商家已审核通过，请尽快将商品退还给卖家，退货地址为【${returnAddr}】。', '<p>售后服务申请审核通过提醒：您编号为${serviceSn}的${serviceType}售后服务申请商家已审核通过，请尽快将商品退还给卖家，退货地址为【${returnAddr}】。</p>', '退货和换货售后服务审核通过提醒');
INSERT INTO `es_message_template` VALUES ('27', 'MEMBERASCLOSED', '售后服务单关闭提醒', 'MEMBER', 'OPEN', 'CLOSED', 'CLOSED', '售后服务单关闭提醒：您编号为${serviceSn}的${serviceType}售后服务单已被商家关闭，具体信息请进入售后服务详情中查看。', '售后服务单关闭提醒：您编号为${serviceSn}的${serviceType}售后服务单已被商家关闭，具体信息请进入售后服务详情中查看。', '<p>售后服务单关闭提醒：您编号为${serviceSn}的${serviceType}售后服务单已被商家关闭，具体信息请进入售后服务详情中查看。</p>', '售后服务单关闭提醒');
INSERT INTO `es_message_template` VALUES ('28', 'PINTUANORDERAUTOCANCEL', '拼团订单自动取消提醒', 'MEMBER', 'OPEN', 'CLOSED', 'CLOSED', '拼团订单自动取消提醒：您编号为${orderSn}的拼团订单在活动结束时未能成团，因此被系统自动取消并已成功退款，如有问题请联系商家或平台。', '拼团订单自动取消提醒：您编号为${orderSn}的拼团订单在活动结束时未能成团，因此被系统自动取消并已成功退款，如有问题请联系商家或平台。', '<p>拼团订单自动取消提醒：您编号为${orderSn}的拼团订单在活动结束时未能成团，因此被系统自动取消并已成功退款，如有问题请联系商家或平台。</p>', '拼团订单自动取消提醒');

-- 管理端菜单表新增菜单数据 add_by duanmingyu 2019-11-30
INSERT INTO `es_menu` VALUES ('228', '2', '售后服务列表', null, 'serviceList', '/admin/after-sales.*', '0', ',2,228,', '2');

-- 管理端菜单增加交易投诉菜单 优惠券菜单 敏感词菜单 add by fk 2019.12.16
INSERT INTO `es_menu` VALUES ('229', '2', '投诉', null, 'complaint', '/admin/trade/order-complains.*', '0', ',2,229,', '2');
INSERT INTO `es_menu` VALUES ('230', '229', '投诉列表', null, 'complaintList', '/admin/trade/order-complains.*', '0', ',2,229,230,', '3');
INSERT INTO `es_menu` VALUES ('231', '229', '投诉主题', null, 'subjectList', '/admin/systems/complain-topics.*', '0', ',2,229,231,', '3');
INSERT INTO `es_menu` VALUES ('232', '82', '敏感词', null, 'sensitiveWords', '/admin/sensitive-words.*', '0', ',8,82,232,', '3');
INSERT INTO `es_menu` VALUES ('233', '5', '优惠券', null, 'couponManage', '/admin/promotion/coupons.*', '0', ',5,233,', '2');
