package com.enation.app.javashop.core.system.model.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * 快递鸟表单
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2019-05-21 上午11:17
 */
public class FormItem {

    @ApiModelProperty(name = "name", value = "表单名称")
    private String name;


    @ApiModelProperty(name = "code", value = "快递鸟 参数")
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
