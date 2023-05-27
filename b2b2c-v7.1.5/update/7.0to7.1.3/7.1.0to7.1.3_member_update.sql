-- 会员评论表新增字段
ALTER TABLE `es_member_comment` ADD COLUMN `additional_status` int(10) DEFAULT 0 COMMENT '追加评论状态 0：未追加，1：已追加';
ALTER TABLE `es_member_comment` ADD COLUMN `additional_content` longtext DEFAULT NULL COMMENT '追加的评论内容';
ALTER TABLE `es_member_comment` ADD COLUMN `additional_time` bigint(20) DEFAULT NULL COMMENT '追加评论时间';
ALTER TABLE `es_member_comment` ADD COLUMN `additional_have_image` int(10) DEFAULT NULL COMMENT '追加的评论是否上传了图片 0：未上传，1：已上传';

-- 会员评论图片表新增字段
ALTER TABLE `es_comment_gallery` ADD COLUMN `img_belong` int(10) DEFAULT 0 COMMENT '图片所属 0：初评，1：追评';
