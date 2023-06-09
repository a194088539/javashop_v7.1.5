package com.enation.app.javashop.core.member.model.vo;

/**
 * @author zjp
 * @version v7.0
 * @Description
 * @ClassName ConnectSettingConfigItem
 * @since v7.0 下午7:59 2018/6/28
 */
public class ConnectSettingConfigItem {
    private String key;
    private String name;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ConnectSettingConfigItem{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
