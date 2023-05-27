package com.enation.app.javashop.core.client.system.impl;

import com.enation.app.javashop.core.base.model.vo.EmailVO;
import com.enation.app.javashop.core.base.service.EmailManager;
import com.enation.app.javashop.core.client.system.EmailClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @author fk
 * @version v2.0
 * @Description:
 * @date 2018/8/13 16:26
 * @since v7.0.0
 */
@Service
@ConditionalOnProperty(value="javashop.product", havingValue="stand")
public class EmailClientDefaultImpl implements EmailClient {

    @Autowired
    private EmailManager emailManager;

    @Override
    public void sendEmail(EmailVO emailVO) {
        emailManager.sendEmail(emailVO);
    }

    @Override
    public boolean valid(String scene, String email, String code) {
        return emailManager.valid(scene, email, code);
    }
}
