package com.enation.app.javashop.core.cart;

import com.enation.app.javashop.core.promotion.seckill.model.vo.SeckillGoodsVO;
import com.enation.app.javashop.core.promotion.tool.model.vo.PromotionVO;
import com.enation.app.javashop.core.trade.cart.model.enums.PromotionTarget;
import com.enation.app.javashop.core.trade.cart.model.vo.CartSkuVO;
import com.enation.app.javashop.core.trade.cart.model.vo.PromotionRule;
import com.enation.app.javashop.core.trade.cart.service.rulebuilder.impl.SeckillPluginNew;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.DateUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by kingapex on 2018/12/13.
 * 秒杀 rule builder 测试
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/12/13
 */
public class SeckillPluginNewTest extends BaseTest {

    @Autowired
    private SeckillPluginNew  seckillPluginNew;



    @Test
    public void testNormal() {
        //设定当前时间没过期
        DateUtil.mockDate= DateUtil.getDateline("2018-01-01 12:10:00","yyyy-MM-dd HH:mm:ss");
        CartSkuVO skuVO = SkuMocker.mockSku();
        PromotionVO promotionVO = new PromotionVO();

        SeckillGoodsVO seckillGoodsVO = SkuMocker.mockSeckill();
        promotionVO.setSeckillGoodsVO(seckillGoodsVO);

        PromotionRule rule = seckillPluginNew.build(skuVO, promotionVO);
        System.out.println(rule);

        //预期是减了20元，单价减
        PromotionRule expectedRule  = new PromotionRule(PromotionTarget.SKU);
        expectedRule.setReducedTotalPrice(20D);
        expectedRule.setReducedPrice(10D);
        expectedRule.setTips("秒杀价[90.0]元");
        expectedRule.setTag("秒杀");
        Assert.assertEquals(expectedRule,rule );

    }


    @Test
    public void testInvalid() {

        DateUtil.mockDate= DateUtil.getDateline("2018-01-01 12:10:00","yyyy-MM-dd HH:mm:ss");
        CartSkuVO skuVO = SkuMocker.mockSku();

        //已售1个，售空5个，买5个，应该失效
        skuVO.setNum(5);
        PromotionVO promotionVO = new PromotionVO();

        SeckillGoodsVO seckillGoodsVO = SkuMocker.mockSeckill();
        promotionVO.setSeckillGoodsVO(seckillGoodsVO);

        PromotionRule rule = seckillPluginNew.build(skuVO, promotionVO);
        PromotionRule expectedRule  = new PromotionRule(PromotionTarget.SKU);
        expectedRule.setInvalid(true);
        expectedRule.setInvalidReason("秒杀库存[4]已不足购买数量[5]");
        Assert.assertEquals(expectedRule,rule );
        System.out.println(rule);


        //把当前日期改为2日，即已过期
        DateUtil.mockDate= DateUtil.getDateline("2018-01-02 12:10:00","yyyy-MM-dd HH:mm:ss");
        rule = seckillPluginNew.build(skuVO, promotionVO);
        System.out.println(rule);

        //预期是秒杀已过期
        expectedRule  = new PromotionRule(PromotionTarget.SKU);
        expectedRule.setInvalid(true);
        expectedRule.setInvalidReason("秒杀已过期,有效期为:[2018-01-01 12:00:00至2018-01-01 23:59:59]");

        Assert.assertEquals(expectedRule,rule );

    }




    /**
     * 测试团购数据为空的情况
     */
    @Test
    public void testNull() {
        //模拟一个sku:两个商品，单价100元，总计200元
        CartSkuVO skuVO = SkuMocker.mockSku();

        PromotionVO promotionVO = new PromotionVO();

        //设置团购活动为null
        promotionVO.setSeckillGoodsVO(null);

        //调用builder形成rule
        PromotionRule rule = seckillPluginNew.build(skuVO,promotionVO);

        Assert.assertEquals(rule, new PromotionRule(PromotionTarget.SKU));


    }

}
