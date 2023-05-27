package com.enation.app.javashop.core.client.trade;

import com.enation.app.javashop.core.promotion.coupon.model.dos.CouponDO;

/**
 * @author zh
 * @version v2.0
 * @Description: 优惠券单对外接口
 * @date 2018/7/26 11:21
 * @since v7.0.0
 */
public interface CouponClient {
    /**
     * 修改优惠券的店铺名称
     *
     * @param shopId   店铺id
     * @param shopName 店铺名称
     */
    void editCouponShopName(Integer shopId, String shopName);

    /**
     * 获取优惠券
     *
     * @param id 优惠券主键
     * @return Coupon  优惠券
     */
    CouponDO getModel(Integer id);


    /**
     * 增加被领取数量
     *
     * @param couponId
     */
    void addReceivedNum(Integer couponId);


}
