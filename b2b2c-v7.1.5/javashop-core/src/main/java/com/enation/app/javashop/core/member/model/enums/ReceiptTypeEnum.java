package com.enation.app.javashop.core.member.model.enums;

/**
 * @author zh
 * @version v1.0
 * @Description: 发票类型枚举
 * @date 2018/5/3 11:12
 * @since v7.0.0
 */
public enum ReceiptTypeEnum {
    /**
     * 电子普通发票
     */
    ELECTRO("电子普通发票"),
    /**
     * 增值税普通发票
     */
    VATORDINARY("增值税普通发票"),
    /**
     * 增值税专用发票
     */
    VATOSPECIAL("增值税专用发票");

    private String text;

    ReceiptTypeEnum(String text) {
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
