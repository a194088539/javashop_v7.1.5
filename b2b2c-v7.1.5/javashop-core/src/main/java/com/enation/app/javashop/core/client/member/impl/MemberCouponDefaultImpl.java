package com.enation.app.javashop.core.client.member.impl;

import com.enation.app.javashop.core.client.member.MemberCouponClient;
import com.enation.app.javashop.core.member.model.dos.MemberCoupon;
import com.enation.app.javashop.core.member.service.MemberCouponManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会员优惠券默认实现
 *
 * @author zh
 * @version v7.0
 * @date 18/7/27 下午3:54
 * @since v7.0
 */
@Service
@ConditionalOnProperty(value = "javashop.product", havingValue = "stand")
public class MemberCouponDefaultImpl implements MemberCouponClient {

    @Autowired
    private MemberCouponManager memberCouponManager;

    @Override
    public List<MemberCoupon> listByCheckout(Integer[] sellerIds, Integer memberId) {
        return memberCouponManager.listByCheckout(sellerIds, memberId);
    }

    @Override
    public MemberCoupon getModel(Integer memberId, Integer mcId) {
        return memberCouponManager.getModel(memberId, mcId);
    }
    
}
