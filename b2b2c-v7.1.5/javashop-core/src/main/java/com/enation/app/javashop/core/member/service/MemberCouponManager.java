package com.enation.app.javashop.core.member.service;

import com.enation.app.javashop.core.member.model.dos.MemberCoupon;
import com.enation.app.javashop.core.member.model.dto.MemberCouponQueryParam;
import com.enation.app.javashop.core.member.model.vo.MemberCouponNumVO;
import com.enation.app.javashop.framework.database.Page;

import java.util.List;

/**
 * 会员优惠券
 *
 * @author Snow create in 2018/5/24
 * @version v2.0
 * @since v7.0.0
 */
public interface MemberCouponManager {

    /**
     * 领取优惠券
     *  @param memberId 会员id
     * @param memberName
     * @param couponId 优惠券id
     */
    void receiveBonus(Integer memberId, String memberName, Integer couponId);


    /**
     * 查询我的所有优惠券
     *
     * @param param
     * @return
     */
    Page list(MemberCouponQueryParam param);


    /**
     * 读取我的优惠券
     *
     * @param memberId
     * @param mcId
     * @return
     */
    MemberCoupon getModel(Integer memberId, Integer mcId);

    /**
     * 修改优惠券的使用信息
     *
     * @param mcId    优惠券id
     * @param orderSn 使用优惠券的订单编号
     */
    void usedCoupon(Integer mcId, String orderSn);


    /**
     * 检测已领取数量
     *
     * @param couponId
     */
    void checkLimitNum(Integer couponId);

    /**
     * 结算页—查询会员优惠券
     *
     * @param sellerIds 商家id
     * @param memberId  会员id
     * @return
     */
    List<MemberCoupon> listByCheckout(Integer[] sellerIds, Integer memberId);

    /**
     * 优惠券各个状态数量
     *
     * @return
     */
    MemberCouponNumVO statusNum();


    /**
     * 查询某优惠券的领取列表
     *
     * @param couponId
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page queryByCouponId(Integer couponId, Integer pageNo, Integer pageSize);

    /**
     * 取消某个会员优惠券
     * @param memberCouponId
     */
    void cancel(Integer memberCouponId);

}
