package com.enation.app.javashop.core.shop;

/**
 * @author liuyulei
 * @version 1.0
 * @Description: 店铺分组展示类型
 * @date 2019/5/10 15:27
 * @since v7.0
 */
public enum ShopCatShowTypeEnum {

    /**
     * 全部
     */
    ALL("全部"),
    /**
     * 显示
     */
    SHOW("显示"),
    /**
     * 隐藏
     */
    HIDE("隐藏");

    private String describe;

    ShopCatShowTypeEnum(String des) {
        this.describe = des;
    }

    public String getDescribe() {
        return describe;

    }
}
