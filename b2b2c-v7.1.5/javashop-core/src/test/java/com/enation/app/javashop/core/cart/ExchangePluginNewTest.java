package com.enation.app.javashop.core.cart;

import com.enation.app.javashop.core.promotion.exchange.model.dos.ExchangeDO;
import com.enation.app.javashop.core.promotion.tool.model.vo.PromotionVO;
import com.enation.app.javashop.core.trade.cart.model.enums.PromotionTarget;
import com.enation.app.javashop.core.trade.cart.model.vo.CartSkuVO;
import com.enation.app.javashop.core.trade.cart.model.vo.PromotionRule;
import com.enation.app.javashop.core.trade.cart.service.rulebuilder.impl.ExchangePluginNew;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by kingapex on 2018/12/13.
 * 积分兑换插件
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/12/13
 */
public class ExchangePluginNewTest extends BaseTest {

    @Autowired
    private ExchangePluginNew exchangePluginNew;


    /**
     * 测试正常的兑换
     */
    @Test
    public  void testNormal(){

        //模拟一个sku:两个商品，单价100元，总计200元
        CartSkuVO skuVO = SkuMocker.mockSku();

        //模拟一个积分兑换：10积分+90元兑换一个商品
        PromotionVO promotionVO = new PromotionVO();
        ExchangeDO exchangeDO = SkuMocker.mockExchangeDO();
        promotionVO.setExchange(exchangeDO);

        //调用builder形成rule
        PromotionRule rule = exchangePluginNew.build(skuVO,promotionVO);

        //预期是减了20元，用了20个积分
        PromotionRule expectedRule  = new PromotionRule(PromotionTarget.SKU);
        expectedRule.setReducedTotalPrice(20D);
        expectedRule.setUsePoint(20);
        expectedRule.setTips("使用[20]个积分和[180.0]元兑换");
        Assert.assertEquals(expectedRule,rule );

//        System.out.println(rule);

    }


    /**
     * 测试积分兑换数据为空的情况
     */
    @Test
    public void testNull() {
        //模拟一个sku:两个商品，单价100元，总计200元
        CartSkuVO skuVO = SkuMocker.mockSku();

        PromotionVO promotionVO = new PromotionVO();

        //设置积分兑换活动为null
        promotionVO.setExchange(null);

        //调用builder形成rule
        PromotionRule rule = exchangePluginNew.build(skuVO,promotionVO);


        Assert.assertEquals(rule, new PromotionRule(PromotionTarget.SKU));


    }
}
