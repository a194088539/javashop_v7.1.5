package com.enation.app.javashop.core.member.service;

/**
 * 会员安全业务
 *
 * @author zh
 * @version v7.0
 * @date 18/4/23 下午3:16
 * @since v7.0
 */
public interface MemberSecurityManager {

    /**
     * 发送绑定手机号码的验证码
     *
     * @param mobile
     */
    void sendBindSmsCode(String mobile);

    /**
     * 发送手机验证验证码
     *
     * @param mobile
     */
    void sendValidateSmsCode(String mobile);

    /**
     * 修改密码
     *
     * @param memberId 用户id
     * @param password 密码
     */
    void updatePassword(Integer memberId, String password);

    /**
     * 手机绑定
     *
     * @param mobile
     */
    void bindMobile(String mobile);

    /**
     * 手机更换绑定
     *
     * @param mobile
     */
    void changeBindMobile(String mobile);

    /**
     * 发送绑定电子邮箱验证码
     * @param email
     */
    void sendBindEmailCode(String email);

    /**
     * 邮箱绑定
     *
     * @param email
     */
    void bindEmail(String email);
}
