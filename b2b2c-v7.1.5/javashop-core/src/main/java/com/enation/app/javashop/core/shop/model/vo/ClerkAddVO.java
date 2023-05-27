package com.enation.app.javashop.core.shop.model.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


/**
 * 店员添加vo
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-08-04 18:48:39
 */
public class ClerkAddVO implements Serializable {

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码", required = true)
    private String mobile;
    /**
     * 当前手机号码是否已经是会员
     */
    @ApiModelProperty(value = "结果,值exist为当前手机号码已经是平台老会员", required = true)
    private String result;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}