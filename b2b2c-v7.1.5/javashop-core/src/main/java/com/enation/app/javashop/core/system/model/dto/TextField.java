package com.enation.app.javashop.core.system.model.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * textfield类型表单对象
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2019-05-21 上午11:17
 */
public class TextField {

    @ApiModelProperty(name = "label", value = "表单 名称")
    private String label;


    @ApiModelProperty(name = "name", value = "表单 name")
    private String name;

    @ApiModelProperty(name = "value", value = "值")
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
