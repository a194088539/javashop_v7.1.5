package com.enation.app.javashop.core.system.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;

/**
 * app推送设置
 *
 * @author zh
 * @version v7.0
 * @date 18/6/6 下午4:36
 * @since v7.0
 */
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AppPushSetting {

    @ApiModelProperty(name = "android_push_secret", value = "Android Secret")
    private String androidPushSecret;
    @ApiModelProperty(name = "android_goods_activity", value = "Android Activity")
    private String androidGoodsActivity;
    @ApiModelProperty(name = "android_push_key", value = "Android Key")
    private String androidPushKey;
    @ApiModelProperty(name = "ios_push_secret", value = "IOS secret")
    private String iosPushSecret;
    @ApiModelProperty(name = "ios_push_key", value = "IOS key")
    private String iosPushKey;

    public String getAndroidPushSecret() {
        return androidPushSecret;
    }

    public void setAndroidPushSecret(String androidPushSecret) {
        this.androidPushSecret = androidPushSecret;
    }

    public String getAndroidGoodsActivity() {
        return androidGoodsActivity;
    }

    public void setAndroidGoodsActivity(String androidGoodsActivity) {
        this.androidGoodsActivity = androidGoodsActivity;
    }

    public String getAndroidPushKey() {
        return androidPushKey;
    }

    public void setAndroidPushKey(String androidPushKey) {
        this.androidPushKey = androidPushKey;
    }

    public String getIosPushSecret() {
        return iosPushSecret;
    }

    public void setIosPushSecret(String iosPushSecret) {
        this.iosPushSecret = iosPushSecret;
    }

    public String getIosPushKey() {
        return iosPushKey;
    }

    public void setIosPushKey(String iosPushKey) {
        this.iosPushKey = iosPushKey;
    }

    @Override
    public String toString() {
        return "AppPushSetting{" +
                "androidPushSecret='" + androidPushSecret + '\'' +
                ", androidGoodsActivity='" + androidGoodsActivity + '\'' +
                ", androidPushKey='" + androidPushKey + '\'' +
                ", iosPushSecret='" + iosPushSecret + '\'' +
                ", iosPushKey='" + iosPushKey + '\'' +
                '}';
    }
}
