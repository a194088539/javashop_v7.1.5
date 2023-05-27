package com.enation.app.javashop.core.trade.cart.util;

import com.enation.app.javashop.core.goods.model.enums.GoodsType;
import com.enation.app.javashop.core.member.model.dos.MemberCoupon;
import com.enation.app.javashop.core.member.model.vo.MemberCouponVO;
import com.enation.app.javashop.core.promotion.coupon.model.enums.CouponUseScope;
import com.enation.app.javashop.core.promotion.coupon.model.vo.CouponValidateResult;
import com.enation.app.javashop.core.promotion.coupon.service.CouponCalculator;
import com.enation.app.javashop.core.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.javashop.core.promotion.tool.model.vo.PromotionVO;
import com.enation.app.javashop.core.trade.cart.model.vo.CartSkuVO;
import com.enation.app.javashop.core.trade.cart.model.vo.CartVO;
import com.enation.app.javashop.core.trade.cart.model.vo.CouponVO;
import com.enation.app.javashop.core.trade.cart.model.vo.SelectedPromotionVo;
import com.enation.app.javashop.framework.util.CurrencyUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author liuyulei
 * @version 1.0
 * @Description: 检测优惠券是否可用
 * @date 2019/5/7 20:38
 * @since v7.0
 */
public class CouponValidateUtil {


    /**
     * 检测选择的促销活动是否为积分兑换  如果为积分兑换则不能使用优惠券
     *
     * @param selectedPromotionVo
     */
    public static Boolean validateCoupon(SelectedPromotionVo selectedPromotionVo, Integer sellerId, List<CartSkuVO> skuList) {
        Map<Integer, List<PromotionVO>> singlePromotionMap = selectedPromotionVo.getSinglePromotionMap();

        List<PromotionVO> promotions = singlePromotionMap.get(sellerId);
        AtomicReference<Boolean> result = new AtomicReference<>(new Boolean(false));
        if (promotions != null && !promotions.isEmpty()) {

            promotions.forEach(promotionVO -> {
                for (CartSkuVO cartSkuVO : skuList) {
                    if (promotionVO.getSkuId().equals(cartSkuVO.getSkuId())) {
                        //此时存在积分商品
                        result.set(PromotionTypeEnum.EXCHANGE.name().equals(promotionVO.getPromotionType()));
                        break;
                    }
                }
            });

        }

        return result.get();
    }


    /**
     * 监测购物车中是否包含
     *
     * @param skuList
     * @return
     */
    public static Boolean validateExchange(List<CartSkuVO> skuList) {
        for (CartSkuVO cartSkuVO : skuList) {
            //此时存在积分商品
            if (GoodsType.POINT.name().equals(cartSkuVO.getGoodsType()) && cartSkuVO.getChecked().intValue() == 1) {
                return true;
            }
        }

        return false;
    }

    /**
     * 查看该优惠券是否可用
     *
     * @param coupon
     * @param cartList
     * @return
     */
    public static CouponValidateResult isEnable(MemberCoupon coupon, List<CartVO> cartList) {

        CouponValidateResult result = new CouponValidateResult();

        if (coupon.getSellerId().equals(0)) {
            //平台优惠券全品类优惠券
            if (CouponUseScope.ALL.name().equals(coupon.getUseScope())) {
                CouponValidateResult enableRes = isAllEnable(coupon, cartList);
                if (enableRes.isEnable()) {
                    return enableRes;
                }
            }

            //某分类优惠券
            if (CouponUseScope.CATEGORY.name().equals(coupon.getUseScope())) {
                //判断购物车中的商品的分类 符合 这个优惠券 的促销范围，且金额满足否？
                CouponValidateResult enableRes = isCatEnable(coupon, cartList);
                if (enableRes.isEnable()) {
                    return enableRes;
                }
            }
            //部分商品优惠券
            if (CouponUseScope.SOME_GOODS.name().equals(coupon.getUseScope())) {
                CouponValidateResult enableRes = isGoodsEnable(coupon, cartList);
                if (enableRes.isEnable()) {
                    return enableRes;
                }
            }
        } else {
            //店铺优惠券
            CouponValidateResult enableRes = isAllEnable(coupon, cartList);
            if (enableRes.isEnable()) {
                result.setEnable(true);
                return result;
            }
        }

        result.setEnable(false);
        return result;
    }


    /**
     * 分类优惠券，查看购物车商品是否可用
     *
     * @param coupon
     * @param cartList
     * @return
     */
    private static CouponValidateResult isCatEnable(MemberCoupon coupon, List<CartVO> cartList) {

        CouponValidateResult result = new CouponValidateResult();
        List<Integer> skuIdList = new ArrayList<>();

        Double totalPrice = cartForeach(coupon, cartList, new CouponCalculator() {
            @Override
            public Double calculate(MemberCoupon coupon, CartSkuVO sku) {
                if (coupon.getScopeId().indexOf(","+sku.getCatId()+",") > -1) {
                    skuIdList.add(sku.getSkuId());
                    return CurrencyUtil.mul(sku.getOriginalPrice(), sku.getNum());
                }
                return 0D;
            }
        });

        result.setEnable(totalPrice >= coupon.getCouponThresholdPrice());
        result.setSkuIdList(skuIdList);

        return result;

    }

    /**
     * 部分商品优惠券，查看购物车商品是否可用
     *
     * @param coupon
     * @param cartList
     * @return
     */
    private static CouponValidateResult isGoodsEnable(MemberCoupon coupon, List<CartVO> cartList) {

        CouponValidateResult result = new CouponValidateResult();
        List<Integer> skuIdList = new ArrayList<>();

        Double totalPrice = cartForeach(coupon, cartList, new CouponCalculator() {
            @Override
            public Double calculate(MemberCoupon coupon, CartSkuVO sku) {
                if (coupon.getScopeId().indexOf(","+sku.getGoodsId()+",") > -1) {
                    skuIdList.add(sku.getSkuId());
                    return CurrencyUtil.mul(sku.getOriginalPrice(), sku.getNum());
                }
                return 0D;
            }
        });

        result.setEnable(totalPrice >= coupon.getCouponThresholdPrice());
        result.setSkuIdList(skuIdList);

        return result;
    }

    /**
     * 全品优惠券，查看购物车商品是否可用
     *
     * @param coupon
     * @param cartList
     * @return
     */
    private static CouponValidateResult isAllEnable(MemberCoupon coupon, List<CartVO> cartList) {

        CouponValidateResult result = new CouponValidateResult();
        List<Integer> skuIdList = new ArrayList<>();
        Double totalPrice = 0D;
        for (CartVO cartVO : cartList) {
            if (!coupon.getSellerId().equals(0) && coupon.getSellerId().equals(cartVO.getSellerId())) {
                totalPrice = cartVO.getPrice().getOriginalPrice();
                break;
            }
            totalPrice += cartVO.getPrice().getOriginalPrice();

            for (CartSkuVO skuVO : cartVO.getSkuList()) {
                skuIdList.add(skuVO.getSkuId());
            }

        }

        result.setEnable(totalPrice >= coupon.getCouponThresholdPrice());
        result.setSkuIdList(skuIdList);
        return result;

    }

    /**
     * 购物车商品循环
     *
     * @param coupon
     * @param cartList
     * @param calculator
     * @return
     */
    private static Double cartForeach(MemberCoupon coupon, List<CartVO> cartList, CouponCalculator calculator) {
        Double total = 0D;
        for (CartVO cartVO : cartList) {
            List<CartSkuVO> skuVOList = cartVO.getSkuList();
            for (CartSkuVO cartSkuVO : skuVOList) {
                total += calculator.calculate(coupon, cartSkuVO);
            }
        }

        return total;
    }


}
