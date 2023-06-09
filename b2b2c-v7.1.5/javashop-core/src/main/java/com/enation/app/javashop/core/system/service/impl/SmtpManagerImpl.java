package com.enation.app.javashop.core.system.service.impl;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.base.model.vo.EmailVO;
import com.enation.app.javashop.core.base.service.EmailManager;
import com.enation.app.javashop.core.system.model.dos.SmtpDO;
import com.enation.app.javashop.core.system.service.SmtpManager;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.ResourceNotFoundException;
import com.enation.app.javashop.framework.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**
 * 邮件业务类
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-25 16:16:53
 */
@Service
public class SmtpManagerImpl implements SmtpManager {


    @Autowired
    @Qualifier("systemDaoSupport")
    private DaoSupport systemDaoSupport;
    @Autowired
    private EmailManager emailManager;

    @Autowired
    private Cache cache;

    @Override
    public Page list(int page, int pageSize) {
        String sql = "select * from es_smtp";
        Page webPage = this.systemDaoSupport.queryForPage(sql, page, pageSize, SmtpDO.class);
        return webPage;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public SmtpDO edit(SmtpDO smtp, Integer id) {
        SmtpDO smtpDO = this.getModel(id);
        if (smtpDO != null) {
            cache.remove(CachePrefix.SMTP.getPrefix());
            this.systemDaoSupport.update(smtp, id);
            return smtp;
        }
        throw new ResourceNotFoundException("该smtp未找到！");
    }

    @Override
    public SmtpDO getModel(Integer id) {
        SmtpDO smtp = this.systemDaoSupport.queryForObject(SmtpDO.class, id);
        if (smtp == null) {
            throw new ResourceNotFoundException("该smtp未找到！");
        }
        return smtp;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public SmtpDO add(SmtpDO smtp) {
        if (smtp != null && smtp.getOpenSsl() > 1) {
            smtp.setOpenSsl(1);
        }
        this.systemDaoSupport.insert(smtp);
        Integer id = systemDaoSupport.getLastId("es_smtp");
        smtp.setId(id);
        return smtp;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer id) {
        SmtpDO smtp = this.getModel(id);
        if (smtp == null) {
            throw new ResourceNotFoundException("该smtp未找到！");

        }
        cache.remove(CachePrefix.SMTP.getPrefix());
        this.systemDaoSupport.delete(SmtpDO.class, id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void send(String send, SmtpDO smtp) {
        EmailVO emailVO = new EmailVO();
        emailVO.setTitle("测试邮件");
        emailVO.setEmail(send);
        emailVO.setType("测试邮件");
        emailVO.setContent("测试邮件发送");

        if (smtp.getOpenSsl() == 1 || "smtp.qq.com".equals(smtp.getHost())) {
            emailManager.sendMailByTransport(smtp, emailVO);
        } else {
            emailManager.sendMailByMailSender(smtp, emailVO);
        }
    }

    @Override
    public SmtpDO getCurrentSmtp() {
        List<SmtpDO> smtpList = (List<SmtpDO>) cache.get(CachePrefix.SMTP.getPrefix());
        if (smtpList == null || smtpList.size() < 0) {
            String sql = "select * from es_smtp";
            smtpList = this.systemDaoSupport.queryForList(sql, SmtpDO.class);
            cache.put(CachePrefix.SMTP.getPrefix(), smtpList);
        }
        SmtpDO currentSmtp = null;
        for (SmtpDO smtp : smtpList) {
            if (checkCount(smtp)) {
                currentSmtp = smtp;
                break;
            }
        }
        if (currentSmtp == null) {
            throw new ResourceNotFoundException("未找到可用smtp，都已达到最大发信数 ");
        }
        return currentSmtp;

    }

    /**
     * 检测smtp服务器是否可以用
     *
     * @param smtp
     * @return 检查是否通过
     */
    private boolean checkCount(SmtpDO smtp) {
        //最后一次发送时间
        long lastSendTime = smtp.getLastSendTime();
        //已经不是今天
        if (!DateUtil.toString(new Date(lastSendTime * 1000), "yyyy-MM-dd").equals(DateUtil.toString(new Date(), "yyyy-MM-dd"))) {
            smtp.setSendCount(0);
        }
        return smtp.getSendCount() < smtp.getMaxCount();
    }
}
