package com.enation.app.javashop.core.member.model.vo;

import java.util.List;

/**
 * @author zjp
 * @version v7.0
 * @Description 信任登录参数VO
 * @ClassName ConnectSettingParametersVO
 * @since v7.0 下午7:56 2018/6/28
 */
public class ConnectSettingParametersVO {
    private String name;
    private List<ConnectSettingConfigItem> configList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ConnectSettingConfigItem> getConfigList() {
        return configList;
    }

    public void setConfigList(List<ConnectSettingConfigItem> configList) {
        this.configList = configList;
    }

    @Override
    public String toString() {
        return "ConnectSettingParametersVO{" +
                "name='" + name + '\'' +
                ", configList=" + configList +
                '}';
    }
}
