package com.enation.app.javashop.core.distribution.model.enums;

/**
 * 提现申请状态枚举类
 * @author duanmingyu
 * @since v7.1.5
 * @version V1.1
 * 2019-09-06
 */
public enum WithdrawStatusEnum {
    /**
     * 申请中
     */
    APPLY("申请中"),
    /**
     * 审核成功
     */
    VIA_AUDITING("审核通过"),
    /**
     * 审核未通过
     */
    FAIL_AUDITING("审核未通过"),
    /**
     * 已转账
     */
    TRANSFER_ACCOUNTS("已转账");

    private String description;

    WithdrawStatusEnum(String des) {
        this.description = des;
    }

    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }

}
