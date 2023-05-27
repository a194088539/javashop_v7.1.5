package com.enation.app.javashop.core.trade.complain.model.enums;

/**
 * 交易投诉状态枚举
 *
 * @author fk
 * @version v2.0
 * @since v2.0
 * 2019-11-27 16:48:27
 */
public enum ComplainStatusEnum {

    /**
     * 新投诉
     */
    NEW("新投诉"),
    /**
     * 已撤销
     */
    CANCEL("已撤销"),
    /**
     * 待申诉
     */
    WAIT_APPEAL("待申诉"),
    /**
     * 对话中
     */
    COMMUNICATION("对话中"),
    /**
     * 等待仲裁
     */
    WAIT_ARBITRATION("等待仲裁"),
    /**
     * 已完成
     */
    COMPLETE("已完成");

    private String description;

    ComplainStatusEnum(String description){
        this.description = description;
    }

    public String description() {
        return this.description;
    }




}
