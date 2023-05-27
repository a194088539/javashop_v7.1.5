package com.enation.app.javashop.core.passport.service;


import com.enation.app.javashop.core.member.model.dos.Member;

/**
 * 账号管理
 *
 * @author zh
 * @version v7.0
 * @since v7.0.0
 * 2018-04-8 11:33:56
 */
public interface PassportManager {
    /**
     * 发送注册短信验证码
     *
     * @param mobile 手机号码
     */
    void sendRegisterSmsCode(String mobile);

    /**
     * 发送找回密码短信验证码
     *
     * @param mobile
     */
    void sendFindPasswordCode(String mobile);

    /**
     * 发送登录短信验证码
     *
     * @param mobile
     */
    void sendLoginSmsCode(String mobile);

    /**
     * 发送短信验证码
     *
     * @param mobile
     */
    void sendSmsCode(String mobile);

    /**
     * 生成token
     *
     * @param member 会员
     * @param time   时间
     * @return
     */
    String createToken(Member member, int time);


    /**
     * 通过refreshToken 获取 accessToken
     *
     * @param refreshToken
     * @returna ccessToken
     */
    String exchangeToken(String refreshToken);

    /**
     * 清除标记缓存
     *
     * @param mobile 手机号码
     * @param scene  业务场景
     */
    void clearSign(String mobile, String scene);


}