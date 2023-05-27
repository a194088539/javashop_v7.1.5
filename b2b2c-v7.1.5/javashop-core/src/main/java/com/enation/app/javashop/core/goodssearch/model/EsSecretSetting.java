package com.enation.app.javashop.core.goodssearch.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
* @author liuyulei
 * @version 1.0
 * @Description: ES分词库秘钥设置
 * @date 2019/5/26 16:05
 * @since v7.0
 */
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class EsSecretSetting {


    /**
     * 密码
     */
    @ApiModelProperty(name = "password", value = "密码")
    private String password;

    /**
     * 秘钥
     */
    @ApiModelProperty(name = "secret_key", value = "秘钥")
    private String secretKey;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }


    @Override
    public String toString() {
        return "EsSecretSetting{" +
                "password='" + password + '\'' +
                ", secretKey='" + secretKey + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EsSecretSetting that = (EsSecretSetting) o;

        return new EqualsBuilder()
                .append(password, that.password)
                .append(secretKey, that.secretKey)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(password)
                .append(secretKey)
                .toHashCode();
    }
}
