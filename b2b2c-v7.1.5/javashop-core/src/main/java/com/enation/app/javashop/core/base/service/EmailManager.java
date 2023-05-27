package com.enation.app.javashop.core.base.service;

import com.enation.app.javashop.core.base.SceneType;
import com.enation.app.javashop.core.base.model.vo.EmailVO;
import com.enation.app.javashop.core.system.model.dos.SmtpDO;

/**
 * 发送邮件接口
 *
 * @author zh
 * @version v2.0
 * @since v7.0
 * 2018年3月26日 下午3:11:00
 */
public interface EmailManager {

    /**
     * 邮件发送到amqp，供具体业务使用
     *
     * @param emailVO 邮件发送vo
     */
    void sendMQ(EmailVO emailVO);

    /**
     * 邮件发送实现，供消费者调用
     *
     * @param emailVO
     */
    void sendEmail(EmailVO emailVO);

    /**
     * 通过java Transport发送邮件  支持ssl
     *
     * @param smtp    smtp设置
     * @param emailVO 邮件内容
     */
    void sendMailByTransport(SmtpDO smtp, EmailVO emailVO);

    /**
     * 通过java Transport发送邮件  不支持ssl
     *
     * @param smtp    smtp设置
     * @param emailVO 邮件内容
     */
    void sendMailByMailSender(SmtpDO smtp, EmailVO emailVO);

    /**
     * 发送(发送邮箱验证码)消息
     *
     * @param byName    操作，替换内容
     * @param email     电子邮箱
     * @param sceneType 操作类型
     */
    void sendEmailMessage(String byName, String email, SceneType sceneType);

    /**
     * 在缓存中记录验证码
     *
     * @param scene  业务场景
     * @param email  电子邮箱
     * @param code   邮箱验证码
     */
    void record(String scene, String email, String code);

    /**
     * 验证电子邮箱验证码
     *
     * @param scene  业务场景
     * @param email  电子邮箱
     * @param code   电子邮箱验证码
     * @return 是否通过校验 true通过，false不通过
     */
    boolean valid(String scene, String email, String code);

}
