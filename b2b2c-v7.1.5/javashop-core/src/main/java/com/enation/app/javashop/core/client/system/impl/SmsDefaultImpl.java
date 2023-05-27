package com.enation.app.javashop.core.client.system.impl;

import com.enation.app.javashop.core.base.SceneType;
import com.enation.app.javashop.core.base.model.vo.SmsSendVO;
import com.enation.app.javashop.core.base.service.SmsManager;
import com.enation.app.javashop.core.client.system.SmsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 短信实现
 *
 * @author zh
 * @version v7.0
 * @date 18/7/31 上午11:13
 * @since v7.0
 */
@Service
@ConditionalOnProperty(value="javashop.product", havingValue="stand")
public class SmsDefaultImpl implements SmsClient {

    @Autowired
    private SmsManager smsManager;


    @Override
    public boolean valid(String scene, String mobile, String code) {
        return smsManager.valid(scene, mobile, code);
    }

    @Override
    public String validMobile(String scene, String mobile) {
        return smsManager.validMobile(scene,mobile);
    }

    @Override
    public void sendSmsMessage(String byName, String mobile, SceneType sceneType) {
        this.smsManager.sendSmsMessage(byName, mobile, sceneType);
    }

    @Override
    public void send(SmsSendVO smsSendVO) {
        smsManager.send(smsSendVO);
    }
}
