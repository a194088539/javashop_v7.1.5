package com.enation.app.javashop.core.client.member.impl;

import com.enation.app.javashop.core.client.member.MemberAddressClient;
import com.enation.app.javashop.core.member.model.dos.MemberAddress;
import com.enation.app.javashop.core.member.service.MemberAddressManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 会员地址默认实现
 *
 * @author zh
 * @version v7.0
 * @date 18/7/27 下午3:54
 * @since v7.0
 */
@Service
@ConditionalOnProperty(value="javashop.product", havingValue="stand")
public class MemberAddressDefaultImpl implements MemberAddressClient {

    @Autowired
    private MemberAddressManager memberAddressManager;

    @Override
    public MemberAddress getModel(Integer id) {
        return memberAddressManager.getModel(id);
    }

    @Override
    public MemberAddress getDefaultAddress(Integer memberId) {
        return memberAddressManager.getDefaultAddress(memberId);
    }
}
