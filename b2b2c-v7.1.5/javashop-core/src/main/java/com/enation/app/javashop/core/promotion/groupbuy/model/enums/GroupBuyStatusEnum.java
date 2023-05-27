package com.enation.app.javashop.core.promotion.groupbuy.model.enums;

/**
 * 团购状态美剧
 *
 * @author Snow create in 2018/6/13
 * @version v2.0
 * @since v7.0.0
 */
public enum GroupBuyStatusEnum {

    /** 已结束*/
    OVERDUE("已结束"),

    /** 进行中*/
    CONDUCT("进行中"),

    /** 未开始*/
    NOT_BEGIN("未开始");

    private String status;


    GroupBuyStatusEnum(String status) {
        this.status=status;
    }

    public String getStatus() {
        return this.status;
    }
}
