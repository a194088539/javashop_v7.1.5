package com.enation.app.javashop.core.trade.cart.service.cartbuilder;

import com.enation.app.javashop.core.trade.cart.model.enums.CheckedWay;
import com.enation.app.javashop.core.trade.cart.model.vo.CartVO;
import com.enation.app.javashop.core.trade.cart.model.vo.CouponVO;

import java.util.List;

/**
 * 购物车促销信息渲染器<br/>
 * 使用{@link com.enation.app.javashop.core.trade.cart.model.vo.SelectedPromotionVo}物料生产出
 * {@link com.enation.app.javashop.core.trade.cart.model.vo.PromotionRule}
 * 文档请参考：<br>
 * <a href="http://doc.javamall.com.cn/current/achitecture/jia-gou/ding-dan/cart-and-checkout.html" >购物车架构</a>
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/12/10
 */
public interface CartPromotionRuleRenderer {


    /**
     * 对购物车进行渲染促销数据
     * @param cartList
     * @param includeCoupon 是否包含优惠券
     * @param way
     * @return
     */
    CouponVO render(List<CartVO> cartList, boolean includeCoupon, CheckedWay way);

}
