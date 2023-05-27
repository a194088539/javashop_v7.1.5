package com.enation.app.javashop.core.promotion.coupon.model.vo;

import java.util.List;

/**
 * 优惠券是否可用的验证结果
 *
 * @author fk
 * @version v2.0
 * @since v7.1.5
 * 2019-09-19 23:19:39
 */
public class CouponValidateResult {

    /**
     * skuid
     */
    private List<Integer> skuIdList;

    /**
     * 优惠券是否可用
     */
    private boolean isEnable;

    public List<Integer> getSkuIdList() {
        return skuIdList;
    }

    public void setSkuIdList(List<Integer> skuIdList) {
        this.skuIdList = skuIdList;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }
}
