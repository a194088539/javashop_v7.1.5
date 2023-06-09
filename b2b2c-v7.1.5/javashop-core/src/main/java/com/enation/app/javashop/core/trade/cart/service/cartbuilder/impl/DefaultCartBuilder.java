package com.enation.app.javashop.core.trade.cart.service.cartbuilder.impl;

import com.enation.app.javashop.core.trade.cart.model.enums.CartType;
import com.enation.app.javashop.core.trade.cart.model.enums.CheckedWay;
import com.enation.app.javashop.core.trade.cart.model.vo.CartVO;
import com.enation.app.javashop.core.trade.cart.model.vo.CartView;
import com.enation.app.javashop.core.trade.cart.model.vo.CouponVO;
import com.enation.app.javashop.core.trade.cart.model.vo.PriceDetailVO;
import com.enation.app.javashop.core.trade.cart.service.cartbuilder.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 促销信息构建器
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/12/10
 */
public class DefaultCartBuilder implements CartBuilder {

    protected final Log logger = LogFactory.getLog(this.getClass());


    /**
     * 购物车促销规则渲染器
     */
    private CartPromotionRuleRenderer cartPromotionRuleRenderer;

    /**
     * 购物车价格计算器
     */
    private CartPriceCalculator cartPriceCalculator;

    /**
     * 数据校验
     */
    private CheckDataRebderer checkDataRebderer;

    /**
     * 购物车sku渲染器
     */
    private CartSkuRenderer cartSkuRenderer;

    /**
     * 购物车优惠券渲染
     */
    private CartCouponRenderer cartCouponRenderer;


    /**
     * 运费价格计算器
     */
    private CartShipPriceCalculator cartShipPriceCalculator;


    private List<CartVO> cartList;
    private PriceDetailVO price;
    private CartType cartType;
    private List<CouponVO> couponList;
    /**
     * 使用的平台优惠券
     */
    private CouponVO siteCouponVO;


    public DefaultCartBuilder(CartType cartType, CartSkuRenderer cartSkuRenderer, CartPromotionRuleRenderer cartPromotionRuleRenderer, CartPriceCalculator cartPriceCalculator, CheckDataRebderer checkDataRebderer) {
        this.cartType = cartType;
        this.cartSkuRenderer = cartSkuRenderer;
        this.cartPromotionRuleRenderer = cartPromotionRuleRenderer;
        this.cartPriceCalculator = cartPriceCalculator;
        this.checkDataRebderer = checkDataRebderer;
        cartList = new ArrayList<>();
    }

    public DefaultCartBuilder(CartType cartType, CartSkuRenderer cartSkuRenderer, CartPromotionRuleRenderer cartPromotionRuleRenderer, CartPriceCalculator cartPriceCalculator, CartCouponRenderer cartCouponRenderer, CartShipPriceCalculator cartShipPriceCalculator, CheckDataRebderer checkDataRebderer) {
        this.cartType = cartType;
        this.cartSkuRenderer = cartSkuRenderer;
        this.cartPromotionRuleRenderer = cartPromotionRuleRenderer;
        this.cartPriceCalculator = cartPriceCalculator;
        this.cartCouponRenderer = cartCouponRenderer;
        this.cartShipPriceCalculator = cartShipPriceCalculator;
        this.checkDataRebderer = checkDataRebderer;
        cartList = new ArrayList<>();
    }

    /**
     * 渲染sku<br/>
     * 此步通过{@link com.enation.app.javashop.core.trade.cart.model.vo.CartSkuOriginVo}生产出一个全新的cartList
     *
     * @return
     */
    @Override
    public CartBuilder renderSku(CheckedWay way) {
        cartSkuRenderer.renderSku(this.cartList, cartType, way);
        return this;
    }

    /**
     * 带过滤器式的渲染sku<br/>
     * 可以过滤为指定条件{@link  CartSkuFilter}的商品<br/>
     *
     * @return
     * @see CartSkuFilter
     */
    @Override
    public CartBuilder renderSku(CartSkuFilter filter, CheckedWay way) {
        cartSkuRenderer.renderSku(this.cartList, filter, cartType, way);
        return this;
    }


    /**
     * 此步通过
     * {@link com.enation.app.javashop.core.trade.cart.model.vo.SelectedPromotionVo}
     * 生产出
     * {@link com.enation.app.javashop.core.trade.cart.model.vo.PromotionRule}
     *
     * @param includeCoupon
     * @return
     */
    @Override
    public CartBuilder renderPromotionRule(boolean includeCoupon, CheckedWay way) {
        siteCouponVO = cartPromotionRuleRenderer.render(cartList, includeCoupon, way);
        return this;
    }

    /**
     * 此步通过上一步的产出物
     * {@link com.enation.app.javashop.core.trade.cart.model.vo.PromotionRule}
     * 来计算出价格:
     * {@link PriceDetailVO}
     *
     * @return
     */
    @Override
    public CartBuilder countPrice() {
        this.price = cartPriceCalculator.countPrice(cartList,siteCouponVO);
        return this;
    }


    /**
     * 调用运费模板来算出运费，只接应用到购物车的价格中
     *
     * @return
     */
    @Override
    public CartBuilder countShipPrice() {
        cartShipPriceCalculator.countShipPrice(cartList);
        return this;
    }


    /**
     * 此步读取出会员的可用优惠券，加入到购物车的couponList中
     *
     * @return
     */
    @Override
    public CartBuilder renderCoupon() {
        List<CouponVO> couponList = cartCouponRenderer.render(cartList);
        this.couponList = couponList;
        return this;
    }

    @Override
    public CartView build() {
        return new CartView(cartList, price, couponList);
    }

    @Override
    public CartBuilder checkData() {
        checkDataRebderer.checkData(cartList);
        return this;
    }
}
