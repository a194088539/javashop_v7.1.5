package com.enation.app.javashop.core.member.model.enums;

/**
 * @author zjp
 * @version v7.0
 * @Description
 * @ClassName ConnectPortEnum
 * @since v7.0 上午10:31 2018/6/22
 */
public enum ConnectPortEnum {
    //PC端
    PC("PC端"),
    //WAP端
    WAP("WAP端");

    private String description;

    ConnectPortEnum(String des) {
        this.description = des;
    }

    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }
}
