package com.enation.app.javashop.core.member.model.enums;

/**
* @author liuyulei
 * @version 1.0
 * @Description: 审核状态
 * @date 2019/6/25 9:42
 * @since v7.0
 */
public enum AuditEnum {

    /**
     * 待审核状态
     */
    WAIT_AUDIT("待审核"),


    /**
     * 审核通过状态
     */
    PASS_AUDIT("审核通过"),

    /**
     * 审核拒绝状态
     */
    REFUSE_AUDIT("审核拒绝");

    private String description;

    AuditEnum(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
    public String value(){
        return this.name();
    }
}
