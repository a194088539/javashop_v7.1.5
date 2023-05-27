package com.enation.app.javashop.core.member.service;

import java.util.List;
import java.util.Map;

/**
 * 评论图片业务层
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-05-03 14:11:46
 */
public interface CommentGalleryManager {


    /**
     * 添加评论图片
     * @param commentId 评论ID
     * @param list 图片集合
     */
    void add(Integer commentId, List<String> list);

    /**
     * 获取评论图片
     * @param commentIds 评论ID组
     * @return
     */
    Map<Integer,List<String>> getGalleryByCommentIds(Integer commentIds);
}