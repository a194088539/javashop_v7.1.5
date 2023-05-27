-- 订单项表中添加评论状态字段  add by liuyulei 2019-06-25
alter table es_order_items add comment_status varchar (255) default 'UNFINISHED';

-- 更改驳回原因长度为500
alter table es_seckill_apply modify fail_reason varchar(500) null comment '驳回原因';
