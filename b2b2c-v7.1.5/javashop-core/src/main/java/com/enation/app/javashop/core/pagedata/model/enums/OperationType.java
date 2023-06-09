package com.enation.app.javashop.core.pagedata.model.enums;

/**
 * @author fk
 * @version v1.0
 * @Description: 楼层操作类型
 * @date 2018/5/21 16:05
 * @since v7.0.0
 */
public enum OperationType {

    /**
     * 链接地址
     */
    URL("链接地址"),
    /**
     * 商品
     */
    GOODS("商品"),
    /**
     * 关键字
     */
    KEYWORD("关键字"),
    /**
     * 店铺
     */
    SHOP("店铺"),
    /**
     * 商城分类
     */
    CATEGORY("商城分类"),
    /**
     * 专题
     */
    TOPIC("专题"),
    /**
     * 无操作
     */
    NONE("无操作");

    private String description;

    OperationType(String description) {
        this.description = description;

    }

    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }


}
