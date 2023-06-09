package com.enation.app.javashop.core.trade.cart.service.rulebuilder.impl;

import com.enation.app.javashop.core.promotion.exchange.model.dos.ExchangeDO;
import com.enation.app.javashop.core.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.javashop.core.promotion.tool.model.vo.PromotionVO;
import com.enation.app.javashop.core.trade.cart.model.enums.PromotionTarget;
import com.enation.app.javashop.core.trade.cart.model.vo.CartSkuVO;
import com.enation.app.javashop.core.trade.cart.model.vo.PromotionRule;
import com.enation.app.javashop.core.trade.cart.service.rulebuilder.SkuPromotionRuleBuilder;
import com.enation.app.javashop.framework.util.CurrencyUtil;
import org.springframework.stereotype.Component;

/**
 * 积分兑换插件
 * @author Snow create in 2018/5/25
 * @version v2.0
 * @since v7.0.0
 */
@Component
public class ExchangePluginNew  implements SkuPromotionRuleBuilder {



    @Override
    public PromotionRule build(CartSkuVO skuVO, PromotionVO promotionVO) {

        PromotionRule rule = new PromotionRule(PromotionTarget.SKU);

        ExchangeDO exchangeDO = promotionVO.getExchange();

        // 获取当前这个商品购买的数量
        int num = skuVO.getNum();
        if(exchangeDO != null) {

            //商品所需兑换的金额(单个商品优惠后价格)
            Double exchangeMoney = exchangeDO.getExchangeMoney();
            //商品所需兑换的积分
            int exchangePoint = exchangeDO.getExchangePoint();

            //此商品需要的积分(商品所需兑换积分*数量)
            int usePoint = CurrencyUtil.mul(exchangePoint, num).intValue();

            //此商品需要的金额(商品所需金额*数量)
            double useMoney = CurrencyUtil.mul(exchangeMoney, num);

            double oldTotal =  skuVO.getSubtotal();

            //计算相对于原价，一共减了多少钱
            Double reducedTotalPrice = CurrencyUtil.sub( oldTotal ,useMoney );
            Double reducedPrice = CurrencyUtil.sub( skuVO.getOriginalPrice() ,exchangeMoney );

            //记录给规则
            rule.setReducedTotalPrice(reducedTotalPrice);
            rule.setReducedPrice(reducedPrice);
            rule.setUsePoint(usePoint);
            rule.setTips("使用["+ usePoint +"]个积分和["+ useMoney +"]元兑换");
            rule.setTag("积分兑换");

        }

        return rule;

    }


    @Override
    public PromotionTypeEnum getPromotionType() {

        return PromotionTypeEnum.EXCHANGE;
    }

}
