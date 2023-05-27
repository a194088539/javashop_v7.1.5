package com.enation.app.javashop.core.base.service.impl;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.base.SceneType;
import com.enation.app.javashop.core.base.SettingGroup;
import com.enation.app.javashop.core.base.model.dos.EmailDO;
import com.enation.app.javashop.core.base.model.vo.EmailVO;
import com.enation.app.javashop.core.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.core.base.service.EmailManager;
import com.enation.app.javashop.core.base.service.SettingManager;
import com.enation.app.javashop.core.client.system.MessageTemplateClient;
import com.enation.app.javashop.core.member.model.RandomCreate;
import com.enation.app.javashop.core.system.SystemErrorCode;
import com.enation.app.javashop.core.system.enums.MessageCodeEnum;
import com.enation.app.javashop.core.system.model.dos.MessageTemplateDO;
import com.enation.app.javashop.core.system.model.dos.SmtpDO;
import com.enation.app.javashop.core.system.model.vo.SiteSetting;
import com.enation.app.javashop.core.system.service.SmtpManager;
import com.enation.app.javashop.framework.JavashopConfig;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.logs.Debugger;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import com.sun.mail.util.MailSSLSocketFactory;
import org.apache.commons.lang.text.StrSubstitutor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 邮件发送实现
 *
 * @author zh
 * @version v7.0
 * @since v7.0
 * 2018年3月26日 下午3:23:04
 */
@Service
public class EmailManagerImpl implements EmailManager {
    @Autowired
    private SmtpManager smtpManager;
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private Debugger debugger;

    @Autowired
    @Qualifier("systemDaoSupport")
    private DaoSupport systemDaoSupport;

    @Autowired
    private MessageTemplateClient messageTemplateClient;

    @Autowired
    private SettingManager settingManager;

    @Autowired
    private Cache cache;

    @Autowired
    private JavashopConfig javashopConfig;

    /**
     * 通过java Transport发送邮件  支持ssl
     *
     * @param emailVO
     */
    @Override
    public void sendMailByTransport(SmtpDO smtp, EmailVO emailVO) {


        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", smtp.getHost());
            props.put("mail.smtp.socketFactory.port", String.valueOf(smtp.getPort()));
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.socketFactory", sf);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", String.valueOf(smtp.getPort()));
            final String username = smtp.getUsername().trim();
            final String password = smtp.getPassword().trim();
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username,password);
                        }
                    });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(smtp.getMailFrom()));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(emailVO.getEmail()));
            message.setSubject(emailVO.getTitle());
            message.setText(emailVO.getContent());

            Transport.send(message);



        } catch (Exception e) {
            debugger.log("邮件发送失败:", StringUtil.getStackTrace(e));
            e.printStackTrace();
            emailVO.setErrorNum(1);
            emailVO.setSuccess(0);
            throw new ServiceException(SystemErrorCode.E904.code(), "邮件发送失败！");
        }
        emailVO.setErrorNum(0);
        emailVO.setSuccess(1);
        //向库中插入
        this.add(emailVO);

    }

    /**
     * 通过javamail 发送邮件 暂不支持ssl
     *
     * @param emailVO
     */
    @Override
    public void sendMailByMailSender(SmtpDO smtp, EmailVO emailVO) {
        //否则使用javaMailSender
        JavaMailSender javaMailSender = new JavaMailSenderImpl();

        ((JavaMailSenderImpl) javaMailSender).setHost(smtp.getHost());
        ((JavaMailSenderImpl) javaMailSender).setUsername(smtp.getUsername());
        ((JavaMailSenderImpl) javaMailSender).setPassword(smtp.getPassword());
        ((JavaMailSenderImpl) javaMailSender).setPort(smtp.getPort());
        //设置发送者
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            //设置邮件标题
            helper.setSubject(emailVO.getTitle());
            //设置邮件内容
            helper.setText(emailVO.getContent());

            //设置邮件 收件人
            helper.setTo(emailVO.getEmail());

            helper.setFrom(smtp.getMailFrom());
            //发送邮件
            javaMailSender.send(message);
            debugger.log("邮件发送成功");

        } catch (Exception e) {
            debugger.log("邮件发送失败:", StringUtil.getStackTrace(e));

            e.printStackTrace();
            emailVO.setErrorNum(1);
            emailVO.setSuccess(0);
            throw new ServiceException(SystemErrorCode.E904.code(), "邮件发送失败！");
        }
        //向库中插入
        this.add(emailVO);
    }


    /**
     * 添加发邮件记录
     *
     * @param email 邮件信息
     * @return 邮件信息
     */
    private EmailDO add(EmailVO email) {
        EmailDO emailDO = new EmailDO();
        emailDO.setEmail(email.getEmail());
        emailDO.setTitle(email.getTitle());
        emailDO.setContent(email.getContent());
        emailDO.setType(email.getTitle());
        //默认假设成功
        emailDO.setSuccess(email.getSuccess());
        emailDO.setLastSend(DateUtil.getDateline());
        emailDO.setErrorNum(email.getErrorNum());
        this.systemDaoSupport.insert(emailDO);
        return emailDO;
    }

    @Override
    public void sendMQ(EmailVO emailVO) {
        this.amqpTemplate.convertAndSend(AmqpExchange.EMAIL_SEND_MESSAGE, "emailSendMessageMsg", emailVO);
    }


    @Override
    public void sendEmail(EmailVO emailVO) {
        //获取当钱的smtp服务器
        SmtpDO smtp = smtpManager.getCurrentSmtp();

        debugger.log("找到smtp服务器：", smtp.toString());
        //根据对ssl的支付 分别走不同的发送方法
        if (smtp.getOpenSsl() == 1 || "smtp.qq.com".equals(smtp.getHost())) {
            debugger.log("使用ssl");
            this.sendMailByTransport(smtp, emailVO);
        } else {
            debugger.log("不使用ssl");

            this.sendMailByMailSender(smtp, emailVO);
        }
    }

    @Override
    public void sendEmailMessage(String byName, String email, SceneType sceneType) {
        // 随机生成的动态码
        String dynamicCode = "";

        MessageTemplateDO template = messageTemplateClient.getModel(MessageCodeEnum.EMAILCODESEND);

        String siteSettingJson = settingManager.get(SettingGroup.SITE);

        SiteSetting siteSetting = JsonUtil.jsonToObject(siteSettingJson, SiteSetting.class);

        if (siteSetting.getTestMode().equals(1)) {
            dynamicCode = "1111";
        } else {
            dynamicCode = RandomCreate.getRandomCode();
        }

        String emailContent = template.getEmailContent();
        Map<String, Object> valuesMap = new HashMap<String, Object>(4);
        valuesMap.put("byName", byName);
        valuesMap.put("code", dynamicCode);
        valuesMap.put("siteName", siteSetting.getSiteName());
        StrSubstitutor strSubstitutor = new StrSubstitutor(valuesMap);
        String replace = strSubstitutor.replace(emailContent);

        EmailVO emailVO = new EmailVO();
        emailVO.setContent(replace);
        emailVO.setEmail(email);
        emailVO.setTitle(template.getEmailTitle());

        //发送邮箱验证码
        amqpTemplate.convertAndSend(AmqpExchange.EMAIL_SEND_MESSAGE, AmqpExchange.EMAIL_SEND_MESSAGE + "_QUEUE",
                emailVO);

        //缓存中记录验证码
        this.record(sceneType.name(), email, dynamicCode);
    }

    @Override
    public void record(String scene, String email, String code) {
        cache.put(CachePrefix.EMAIL_CODE.getPrefix() + scene + "_" + email, code, javashopConfig.getSmscodeTimout());
    }

    @Override
    public boolean valid(String scene, String email, String code) {
        //从传入参数组织key
        String valCode = CachePrefix.EMAIL_CODE.getPrefix() + scene + "_" + email;
        //redis中获取验证码
        Object obj = cache.get(valCode);
        if (obj != null && obj.equals(code)) {
            //验证码校验通过后清除缓存
            cache.remove(valCode);
            //将标识放入缓存中，在验证验证码正确后，下一步操作需要校验是否经过验证验证码(缓存中是否存在)
            cache.put(CachePrefix.EMAIL_VALIDATE.getPrefix() + "_" + scene + "_" + email, email, javashopConfig.getCaptchaTimout());
            return true;
        }
        return false;
    }
}
