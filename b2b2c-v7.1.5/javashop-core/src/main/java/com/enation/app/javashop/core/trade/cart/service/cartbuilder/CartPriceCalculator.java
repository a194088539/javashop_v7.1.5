package com.enation.app.javashop.core.trade.cart.service.cartbuilder;

import com.enation.app.javashop.core.trade.cart.model.vo.CartVO;
import com.enation.app.javashop.core.trade.cart.model.vo.CouponVO;
import com.enation.app.javashop.core.trade.cart.model.vo.PriceDetailVO;

import java.util.List;

/**
 * 购物车价格计算器<br/>
 * 对购物车中的{@link com.enation.app.javashop.core.trade.cart.model.vo.PromotionRule}进行计算
 * 形成{@link PriceDetailVO}
 * 文档请参考：<br>
 * <a href="http://doc.javamall.com.cn/current/achitecture/jia-gou/ding-dan/cart-and-checkout.html" >购物车架构</a>
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/12/10
 */
public interface CartPriceCalculator {



    /**
     * 计算购物车价格
     * @param cartList
     * @param siteCouponVO 站点优惠券
     */
    PriceDetailVO countPrice(List<CartVO> cartList, CouponVO siteCouponVO);


}
