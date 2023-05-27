package com.enation.app.javashop.core.member.model.enums;

/**
* @author liuyulei
 * @version 1.0
 * @Description:  评论类型枚举
 * @date 2019/6/25 9:44
 * @since v7.0
 */
public enum CommentTypeEnum {


    /**
     * 初次评论
     */
    INITIAL("初评"),

    /**
     * 追加评论
     */
    ADDITIONAL("追评");

    private String description;

    CommentTypeEnum(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
    public String value(){
        return this.name();
    }
}
