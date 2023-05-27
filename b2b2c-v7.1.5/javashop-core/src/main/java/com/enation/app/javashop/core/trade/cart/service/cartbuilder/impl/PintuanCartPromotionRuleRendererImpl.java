package com.enation.app.javashop.core.trade.cart.service.cartbuilder.impl;

import com.enation.app.javashop.core.promotion.fulldiscount.model.vo.FullDiscountVO;
import com.enation.app.javashop.core.promotion.tool.model.vo.PromotionVO;
import com.enation.app.javashop.core.trade.cart.model.enums.CheckedWay;
import com.enation.app.javashop.core.trade.cart.model.vo.*;
import com.enation.app.javashop.core.trade.cart.service.CartPromotionManager;
import com.enation.app.javashop.core.trade.cart.service.cartbuilder.CartPromotionRuleRenderer;
import com.enation.app.javashop.core.trade.cart.service.rulebuilder.CartCouponRuleBuilder;
import com.enation.app.javashop.core.trade.cart.service.rulebuilder.SkuPromotionRuleBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by kingapex on 2018/12/10.
 * 购物促销信息渲染实现
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/12/10
 */
@Service("pintuanCartPromotionRuleRendererImpl")
public class PintuanCartPromotionRuleRendererImpl implements CartPromotionRuleRenderer {


    protected final Log logger = LogFactory.getLog(this.getClass());


    @Autowired
    private CartPromotionManager cartPromotionManager;


    @Autowired
    private List<SkuPromotionRuleBuilder> skuPromotionRuleBuilderList;

    @Autowired
    private CartCouponRuleBuilder cartCouponRuleBuilder;



    @Override
    public CouponVO render(List<CartVO> cartList, boolean includeCoupon, CheckedWay way) {

        //渲染规则
        this.renderRule(cartList,includeCoupon, way);

        if (logger.isDebugEnabled()) {
            logger.debug("购物车处理完促销规则结果为：");
            logger.debug(cartList);
        }
        return null;
    }



    /**
     * 规则渲染
     *
     * @param cartList
     */
    private void renderRule(List<CartVO> cartList,boolean includeCoupon,CheckedWay way) {

        //获取正在进行的满优惠活动列表
        List<FullDiscountVO> fullDiscountVOList = cartPromotionManager.getFullDiscounPromotion(cartList);

        SelectedPromotionVo selectedPromotionVo = cartPromotionManager.getSelectedPromotion();

        //用户选择使用的优惠券
        Map<Integer, CouponVO>  couponMap = selectedPromotionVo.getCouponMap();

        //用户选择的单品活动
        Map<Integer, List<PromotionVO>> skuPromotionMap = selectedPromotionVo.getSinglePromotionMap();

        for (CartVO cart : cartList) {

            /**
             * 渲染优惠券
             */
            Integer sellerId = cart.getSellerId();

            if (includeCoupon) {
                CouponVO couponVO = couponMap.get(sellerId);
                //如果有使用的优惠券，则build rule
                if (couponVO != null) {
                    PromotionRule couponRule = cartCouponRuleBuilder.build(cart, couponVO);
                    cart.getRuleList().add(couponRule);
                }
            }



            /**
             * 渲染单品活动
             */
            //空过滤
            if (skuPromotionMap == null) {
                continue;
            }
            List<PromotionVO> skuPromotionList = skuPromotionMap.get(sellerId);

            if (skuPromotionList == null) {
                continue;
            }

            //循环处理购物车的促销规则
            for (CartSkuVO cartSku : cart.getSkuList()) {

                //跳过未选中的
                if (cartSku.getChecked() == 0) {
                    continue;
                }

                //计算促销规则，形成list，并压入统一的rule list 中
                PromotionRule skuRule = oneSku(cartSku, skuPromotionList);
                if (way.equals(CheckedWay.BUY_NOW) && skuRule.isInvalid()){
                    continue;
                }
                cartSku.setRule(skuRule);

            }


        }


    }



    /**
     * 根据促销类型找到相应的builder
     *
     * @param promotionType 促销类型
     * @return
     */
    private SkuPromotionRuleBuilder getSkuRuleBuilder(String promotionType) {

        if (skuPromotionRuleBuilderList == null) {
            return null;
        }
        for (SkuPromotionRuleBuilder builder : skuPromotionRuleBuilderList) {
            if (builder.getPromotionType().name().equals(promotionType)) {
                return builder;
            }
        }

        return null;
    }


    private PromotionRule oneSku(CartSkuVO cartSku, List<PromotionVO> skuPromotionList) {

        for (PromotionVO promotionVo : skuPromotionList) {

            if (promotionVo.getSkuId().intValue() == cartSku.getSkuId().intValue()) {
                //sku优惠规则构建器
                SkuPromotionRuleBuilder skuRuleBuilder = this.getSkuRuleBuilder(promotionVo.getPromotionType());

                if (skuRuleBuilder == null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(cartSku.getSkuId() + "的活动类型[" + promotionVo.getPromotionType() + "]没有找到builder");
                    }
                    continue;
                }

                //设置单品活动的选择中情况
                selectedPromotion(cartSku, promotionVo);

                //构建促销规则
                PromotionRule rule = skuRuleBuilder.build(cartSku, promotionVo);
                return rule;
            }

        }

        return null;
    }

    private void selectedPromotion(CartSkuVO cartSku, PromotionVO promotionVo) {

        List<CartPromotionVo> singleList = cartSku.getSingleList();
        for (CartPromotionVo cartPromotionVo : singleList) {
            if (cartPromotionVo.getPromotionType().equals( promotionVo.getPromotionType())) {
                cartPromotionVo.setIsCheck(1);
            }
        }

    }





}
