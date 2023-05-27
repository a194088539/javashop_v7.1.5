package com.enation.app.javashop.core.cart;

import com.enation.app.javashop.core.promotion.halfprice.model.vo.HalfPriceVO;
import com.enation.app.javashop.core.promotion.tool.model.vo.PromotionVO;
import com.enation.app.javashop.core.trade.cart.model.enums.PromotionTarget;
import com.enation.app.javashop.core.trade.cart.model.vo.CartSkuVO;
import com.enation.app.javashop.core.trade.cart.model.vo.PromotionRule;
import com.enation.app.javashop.core.trade.cart.service.rulebuilder.impl.HalfPricePluginNew;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.DateUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by kingapex on 2018/12/13.
 * 第二件半件rule builder 测试
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/12/13
 */
public class HalfPricePluginNewTest extends BaseTest {

    @Autowired
    private HalfPricePluginNew halfPricePluginNew;



    @Test
    public void testNormal() {

        //设定当前时间没过期
        DateUtil.mockDate= DateUtil.getDateline("2018-01-02 12:10:00","yyyy-MM-dd HH:mm:ss");
        CartSkuVO skuVO = SkuMocker.mockSku();
        PromotionVO promotionVO = new PromotionVO();
        HalfPriceVO halfPriceVO = SkuMocker.mockHalfPriceVO();
        promotionVO.setHalfPriceVO(halfPriceVO);

        PromotionRule rule = halfPricePluginNew.build(skuVO, promotionVO);
        System.out.println(rule);

       //预期是减了20元
        PromotionRule expectedRule  = new PromotionRule(PromotionTarget.SKU);
        expectedRule.setReducedTotalPrice(50D);
        expectedRule.setTips("第二件半件，减[50.0]元");
        Assert.assertEquals(expectedRule,rule );


        //将数量改为买3件，优惠金额应该还是一样的
        skuVO.setNum(3);
        rule = halfPricePluginNew.build(skuVO, promotionVO);
        Assert.assertEquals(expectedRule,rule );

        //将数量改为买1件，优惠金额应该是0
        skuVO.setNum(1);
        rule = halfPricePluginNew.build(skuVO, promotionVO);
        Assert.assertEquals(rule,  new PromotionRule(PromotionTarget.SKU));


    }



    @Test
    public void testInvalid() {

        //设定当前时间没过期
        DateUtil.mockDate= DateUtil.getDateline("2018-01-06 12:10:00","yyyy-MM-dd HH:mm:ss");
        CartSkuVO skuVO = SkuMocker.mockSku();
        PromotionVO promotionVO = new PromotionVO();
        HalfPriceVO halfPriceVO = SkuMocker.mockHalfPriceVO();
        promotionVO.setHalfPriceVO(halfPriceVO);


        PromotionRule rule = halfPricePluginNew.build(skuVO, promotionVO);
        System.out.println(rule);

        //预期是过期了
        PromotionRule expectedRule  = new PromotionRule(PromotionTarget.SKU);
        expectedRule.setInvalid(true);
        expectedRule.setInvalidReason("第二件半件已过期,有效期为:[2018-01-01 12:00:00至2018-01-05 12:00:00]");

        Assert.assertEquals(expectedRule,rule );


    }


}
