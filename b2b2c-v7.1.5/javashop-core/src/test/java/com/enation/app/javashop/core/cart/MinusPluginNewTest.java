package com.enation.app.javashop.core.cart;

import com.enation.app.javashop.core.promotion.minus.model.vo.MinusVO;
import com.enation.app.javashop.core.promotion.tool.model.vo.PromotionVO;
import com.enation.app.javashop.core.trade.cart.model.enums.PromotionTarget;
import com.enation.app.javashop.core.trade.cart.model.vo.CartSkuVO;
import com.enation.app.javashop.core.trade.cart.model.vo.PromotionRule;
import com.enation.app.javashop.core.trade.cart.service.rulebuilder.impl.MinusPluginNew;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.DateUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by kingapex on 2018/12/13.
 * 单品立减 rule builder 测试
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/12/13
 */
public class MinusPluginNewTest extends BaseTest {

    @Autowired
    private MinusPluginNew minusPluginNew;



    @Test
    public void testNormal() {

        //设定当前时间没过期
        DateUtil.mockDate= DateUtil.getDateline("2018-01-01 12:10:00","yyyy-MM-dd HH:mm:ss");
        CartSkuVO skuVO = SkuMocker.mockSku();
        PromotionVO promotionVO = new PromotionVO();
        MinusVO minusVO = SkuMocker.createMinusVO();

        promotionVO.setMinusVO(minusVO);

        PromotionRule rule = minusPluginNew.build(skuVO, promotionVO);
        System.out.println(rule);

       //预期是减了20元
        PromotionRule expectedRule  = new PromotionRule(PromotionTarget.SKU);
        expectedRule.setReducedTotalPrice(20D);
        expectedRule.setReducedPrice(10D);
        expectedRule.setTips("单品立减[10.0]元");
        Assert.assertEquals(rule, expectedRule);


        //将数量改为买3件，优惠金额30元
        skuVO.setNum(3);
        rule = minusPluginNew.build(skuVO, promotionVO);
        expectedRule.setReducedTotalPrice(30D);
        expectedRule.setReducedPrice(10D);
        expectedRule.setTips("单品立减[10.0]元");
        Assert.assertEquals(rule, expectedRule);

        //将数量改为买1件，优惠金额应该是10
        skuVO.setNum(1);
        rule = minusPluginNew.build(skuVO, promotionVO);
        expectedRule.setReducedTotalPrice(10D);
        expectedRule.setReducedPrice(10D);
        expectedRule.setTips("单品立减[10.0]元");
        Assert.assertEquals(rule, expectedRule);


    }


    @Test
    public void testInvalid() {

        //把当前日期改为6日，即已过期
        DateUtil.mockDate= DateUtil.getDateline("2018-01-06 12:10:00","yyyy-MM-dd HH:mm:ss");
        CartSkuVO skuVO = SkuMocker.mockSku();
        PromotionVO promotionVO = new PromotionVO();
        MinusVO minusVO = SkuMocker.createMinusVO();


        promotionVO.setMinusVO(minusVO);

        PromotionRule rule = minusPluginNew.build(skuVO, promotionVO);
        System.out.println(rule);

        //预期是过期了
        PromotionRule expectedRule  = new PromotionRule(PromotionTarget.SKU);
        expectedRule.setInvalid(true);
        expectedRule.setInvalidReason("单品立减已过期,有效期为:[2018-01-01 12:00:00至2018-01-05 12:00:00]");
        Assert.assertEquals(rule, expectedRule);


    }
}
