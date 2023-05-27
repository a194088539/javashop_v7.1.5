package com.enation.app.javashop.core.client.member;

import com.enation.app.javashop.core.member.model.dos.MemberCoupon;

import java.util.List;

/**
 * 会员优惠券client
 *
 * @author zh
 * @version v7.0
 * @date 18/7/27 上午11:48
 * @since v7.0
 */

public interface MemberCouponClient {
    /**
     * 查询优惠券列表
     *
     * @param sellerIds 商家ids
     * @param memberId  会员id
     * @return 优惠券列表
     */
    List listByCheckout(Integer[] sellerIds, Integer memberId);


    /**
     * 获取会员优惠券信息
     *
     * @param memberId 会员id
     * @param mcId     优惠券id
     * @return 优惠券对象
     */
    MemberCoupon getModel(Integer memberId, Integer mcId);


}
