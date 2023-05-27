package com.enation.app.javashop.core.goodssearch.enums;

/**
* @author liuyulei
 * @version 1.0
 * @Description:  搜索提示词类型
 * @date 2019/6/14 15:34
 * @since v7.0
 */
public enum GoodsWordsType {


    /**
     * 系统
     */
    SYSTEM("系统"),

    /**
     * 平台
     */
    PLATFORM("平台");

    private String description;

    GoodsWordsType(String description) {
        this.description = description;

    }
}
