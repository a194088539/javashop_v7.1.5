package com.enation.app.javashop.core.client.trade.impl;

import com.enation.app.javashop.core.client.trade.CouponClient;
import com.enation.app.javashop.core.promotion.coupon.model.dos.CouponDO;
import com.enation.app.javashop.core.promotion.coupon.service.CouponManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 优惠券client实现
 *
 * @author zh
 * @version v7.0
 * @date 18/12/6 下午4:28
 * @since v7.0
 */
@Service
public class CouponClientImpl implements CouponClient {


    @Autowired
    private CouponManager couponManager;

    @Override
    public void editCouponShopName(Integer shopId, String shopName) {
        couponManager.editCouponShopName(shopId, shopName);
    }

    @Override
    public CouponDO getModel(Integer id) {
        return couponManager.getModel(id);
    }

    @Override
    public void addReceivedNum(Integer couponId) {
        couponManager.addReceivedNum(couponId);
    }
}
