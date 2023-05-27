package com.enation.app.javashop.core.promotion.pintuan.model;

/**
 *
 * 拼团操作枚举值
 * @author liushuai
 * @version v1.0
 * @since v7.0
 * 2019/2/26 上午10:40
 * @Description:
 *
 */

public enum PintuanOptionEnum {


    /**
     * 可以开启
     */
    CAN_OPEN ("可以开启"),

    /**
     * 可以关闭
     */
    CAN_CLOSE("可以关闭"),

    /**
     * 没有什么可以操作的
     */
    NOTHING("没有什么可以操作的");

    private String name;

    PintuanOptionEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
