package com.enation.app.javashop.core.base.message;

import com.enation.app.javashop.core.base.SceneType;

import java.io.Serializable;

/**
 * @author zjp
 * @version v7.0
 * @Description 会员发送信息
 * @ClassName MemberSendMsg
 * @since v7.0 下午8:47 2018/7/9
 */
public class MemberSendMsg implements Serializable {

    private static final long serialVersionUID = -2026510292893377321L;
    /**
     * 手机号
     */
    private String mobile;

    /**
     * 验证码
     */
    private String code;

    /**
     * 验证码类型
     */
    private SceneType sceneType;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SceneType getSceneType() {
        return sceneType;
    }

    public void setSceneType(SceneType sceneType) {
        this.sceneType = sceneType;
    }

    @Override
    public String toString() {
        return "MemberSendMsg{" +
                "mobile='" + mobile + '\'' +
                ", code='" + code + '\'' +
                ", sceneType=" + sceneType +
                '}';
    }
}
