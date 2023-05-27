package com.enation.app.javashop.core.cart;

import com.enation.app.javashop.core.promotion.groupbuy.model.vo.GroupbuyGoodsVO;
import com.enation.app.javashop.core.promotion.tool.model.vo.PromotionVO;
import com.enation.app.javashop.core.trade.cart.model.enums.PromotionTarget;
import com.enation.app.javashop.core.trade.cart.model.vo.CartSkuVO;
import com.enation.app.javashop.core.trade.cart.model.vo.PromotionRule;
import com.enation.app.javashop.core.trade.cart.service.rulebuilder.impl.GroupBuyGoodsPluginNew;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.DateUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by kingapex on 2018/12/13.
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/12/13
 */
public class GroupBuyGoodsPluginNewTest extends BaseTest {


    @Autowired
    private GroupBuyGoodsPluginNew groupBuyGoodsPluginNew;


    @Test
    public void testNormal() {

        //设定当前时间没过期
        DateUtil.mockDate= DateUtil.getDateline("2018-01-01 12:10:00","yyyy-MM-dd HH:mm:ss");

        CartSkuVO skuVO = SkuMocker.mockSku();
        PromotionVO promotionVO = new PromotionVO();
        GroupbuyGoodsVO groupbuyGoods = SkuMocker.mockGroupBuyVO();

        promotionVO.setGroupbuyGoodsVO(groupbuyGoods);

        PromotionRule rule = groupBuyGoodsPluginNew.build(skuVO, promotionVO);
        System.out.println(rule);

        //预期是减了20元
        PromotionRule expectedRule  = new PromotionRule(PromotionTarget.SKU);
        expectedRule.setReducedTotalPrice(20D);
        expectedRule.setReducedPrice(10D);
        expectedRule.setTips("团购价[90.0]");
        Assert.assertEquals(expectedRule,rule );


    }


    @Test
    public void testInvalid() {

        CartSkuVO skuVO = SkuMocker.mockSku();
        PromotionVO promotionVO = new PromotionVO();
        GroupbuyGoodsVO groupbuyGoods =SkuMocker.mockGroupBuyVO();
        promotionVO.setGroupbuyGoodsVO(groupbuyGoods);
        //把当前日期改为2日，即已过期
        DateUtil.mockDate= DateUtil.getDateline("2018-01-06 12:10:00","yyyy-MM-dd HH:mm:ss");
        PromotionRule rule = groupBuyGoodsPluginNew.build(skuVO, promotionVO);
        System.out.println(rule);

        //预期是秒杀已过期
        PromotionRule  expectedRule  = new PromotionRule(PromotionTarget.SKU);
        expectedRule.setInvalid(true);
        expectedRule.setInvalidReason("团购已过期,有效期为:[2018-01-01 12:00:00至2018-01-05 12:00:00]");
        Assert.assertEquals(expectedRule,rule );


        //恢复时间为没过期
        DateUtil.mockDate= DateUtil.getDateline("2018-01-01 12:10:00","yyyy-MM-dd HH:mm:ss");

        //设置为已经卖了4上，再买2个，已经超出了售空数量
        groupbuyGoods.setBuyNum(4);
        skuVO.setNum(2);
        rule = groupBuyGoodsPluginNew.build(skuVO, promotionVO);
        System.out.println(rule);
        //预期库存[1]已不足购买数量[2]
        expectedRule  = new PromotionRule(PromotionTarget.SKU);
        expectedRule.setInvalid(true);
        expectedRule.setInvalidReason("团库存[1]已不足购买数量[2]");
        Assert.assertEquals(expectedRule,rule );


        //设置为已经卖了0上，再买4个，已经超出了单笔限制数量
        groupbuyGoods.setBuyNum(0);
        skuVO.setNum(4);
        rule = groupBuyGoodsPluginNew.build(skuVO, promotionVO);
        System.out.println(rule);

        //预期库团库存[4]已起出限购数[3]
        expectedRule  = new PromotionRule(PromotionTarget.SKU);
        expectedRule.setInvalid(true);
        expectedRule.setInvalidReason("团库存[4]已起出限购数[3]");
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
        promotionVO.setGroupbuyGoodsVO(null);

        //调用builder形成rule
        PromotionRule rule = groupBuyGoodsPluginNew.build(skuVO,promotionVO);

        Assert.assertEquals(rule, new PromotionRule(PromotionTarget.SKU));


    }

}
