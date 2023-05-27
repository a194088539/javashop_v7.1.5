package com.enation.app.javashop.core.client.member.impl;

import com.enation.app.javashop.core.client.member.ConnectClient;
import com.enation.app.javashop.core.member.model.dos.ConnectDO;
import com.enation.app.javashop.core.member.service.ConnectManager;
import com.enation.app.javashop.core.payment.plugin.weixin.signaturer.WechatSignaturer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 第三方连接client
 *
 * @author zh
 * @version v7.0
 * @date 18/7/27 下午3:51
 * @since v7.0
 */
@Service
@ConditionalOnProperty(value="javashop.product", havingValue="stand")
public class ConnectClientDefaultImpl implements ConnectClient {

    @Autowired
    private ConnectManager connectManager;

    @Autowired
    private WechatSignaturer wechatSignaturer;

    @Override
    public ConnectDO getConnect(Integer memberId, String unionType) {

        return connectManager.getConnect(memberId, unionType);
    }

    @Override
    public String getMemberOpenid(Integer memberId) {

        return wechatSignaturer.getMemberOpenid(memberId);
    }
}
