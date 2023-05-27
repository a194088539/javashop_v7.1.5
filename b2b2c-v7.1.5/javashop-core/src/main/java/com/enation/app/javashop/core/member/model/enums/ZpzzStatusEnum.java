package com.enation.app.javashop.core.member.model.enums;

/**
 * 会员增票资质状态枚举类
 *
 * @author duanmingyu
 * @version v7.1.4
 * @since v7.0.0
 * 2019-06-18
 */
public enum ZpzzStatusEnum {
    /**
     * 新申请
     */
    NEW_APPLY("新申请"),
    /**
     * 审核通过
     */
    AUDIT_PASS("审核通过"),
    /**
     * 审核未通过
     */
    AUDIT_REFUSE("审核未通过");

    private String text;

    ZpzzStatusEnum(String text) {
        this.text = text;

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String value() {
        return this.name();
    }
}
