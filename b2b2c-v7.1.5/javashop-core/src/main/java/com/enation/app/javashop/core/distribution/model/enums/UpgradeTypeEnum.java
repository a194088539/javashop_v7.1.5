package com.enation.app.javashop.core.distribution.model.enums;

/**
 * 模版切换状态枚举
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018/5/25 上午11:39
 * @Description:
 */
public enum UpgradeTypeEnum {
    //提现状态
    MANUAL("手动"), AUTOMATIC("自动");

    private String name;

    UpgradeTypeEnum(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }
}
