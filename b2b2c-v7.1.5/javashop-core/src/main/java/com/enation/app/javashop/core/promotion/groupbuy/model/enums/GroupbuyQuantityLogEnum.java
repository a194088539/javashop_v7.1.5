package com.enation.app.javashop.core.promotion.groupbuy.model.enums;

/**
 * GroupbuyQuantityLogEnum
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-09-21 上午9:01
 */
public enum  GroupbuyQuantityLogEnum {

    /** 已结束*/
    BUY("售出"),

    /** 进行中*/
    CANCEL("取消");

    private String status;


    GroupbuyQuantityLogEnum(String status) {
        this.status=status;
    }

    public String getStatus() {
        return this.status;
    }
}
