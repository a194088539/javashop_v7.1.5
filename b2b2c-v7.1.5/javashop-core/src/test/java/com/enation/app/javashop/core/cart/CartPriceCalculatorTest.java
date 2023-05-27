package com.enation.app.javashop.core.cart;

import com.enation.app.javashop.core.promotion.exchange.model.dos.ExchangeDO;
import com.enation.app.javashop.core.promotion.groupbuy.model.vo.GroupbuyGoodsVO;
import com.enation.app.javashop.core.promotion.halfprice.model.vo.HalfPriceVO;
import com.enation.app.javashop.core.promotion.minus.model.vo.MinusVO;
import com.enation.app.javashop.core.promotion.seckill.model.vo.SeckillGoodsVO;
import com.enation.app.javashop.core.trade.cart.service.CartPromotionManager;
import com.enation.app.javashop.core.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.javashop.core.promotion.tool.model.vo.PromotionVO;
import com.enation.app.javashop.core.trade.cart.model.enums.CartType;
import com.enation.app.javashop.core.trade.cart.model.vo.CartSkuVO;
import com.enation.app.javashop.core.trade.cart.model.vo.CartVO;
import com.enation.app.javashop.core.trade.cart.model.vo.SelectedPromotionVo;
import com.enation.app.javashop.core.trade.cart.service.cartbuilder.CartPriceCalculator;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.DateUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Created by kingapex on 2018/12/13.
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/12/13
 */
public class CartPriceCalculatorTest extends BaseTest {

    @Autowired
    private CartPriceCalculator cartPriceCalculator;


    /**
     * 需要模拟的购物车促销接口
     */
    @MockBean
    private CartPromotionManager cartPromotionManager;

    private SelectedPromotionVo selectedPromotionVo = new SelectedPromotionVo();
    private  List<CartVO> cartList  = new ArrayList();

    private void mock(){

        //模拟一个sku:两个商品，单价100元，总计200元
        CartSkuVO skuVO1 = SkuMocker.mockSku(1);

        CartSkuVO skuVO2 = SkuMocker.mockSku(2);

        CartSkuVO skuVO3 = SkuMocker.mockSku(3);


        CartSkuVO skuVO4 = SkuMocker.mockSku(4);

        CartSkuVO skuVO5 = SkuMocker.mockSku(5);


        /**
         * 单品立减
         */
        //设定为单品减10元
        MinusVO minusVO = SkuMocker.createMinusVO();
        PromotionVO promotionVO = new PromotionVO();
        promotionVO.setMinusVO(minusVO);
        promotionVO.setSkuId(1);
        promotionVO.setPromotionType(PromotionTypeEnum.MINUS.name());


        /**
         * 积分兑换
         */
        //模拟一个积分兑换：10积分+90元兑换一个商品
        PromotionVO promotionVO1 = new PromotionVO();
        ExchangeDO exchangeDO = SkuMocker.mockExchangeDO();

        promotionVO1.setExchange(exchangeDO);
        promotionVO1.setPromotionType(PromotionTypeEnum.EXCHANGE.name());
        promotionVO1.setSkuId(2);


        /**
         * 第二件半价
         */
        PromotionVO promotionVO2 = new PromotionVO();
        HalfPriceVO halfPriceVO = SkuMocker.mockHalfPriceVO();
        promotionVO2.setHalfPriceVO(halfPriceVO);
        promotionVO2.setPromotionType(PromotionTypeEnum.HALF_PRICE.name());
        promotionVO2.setSkuId(3);


        /**
         * 秒杀90元
         */
        PromotionVO promotionVO3 = new PromotionVO();
        SeckillGoodsVO seckillGoodsVO = SkuMocker.mockSeckill();
        promotionVO3.setSeckillGoodsVO(seckillGoodsVO);
        promotionVO3.setSkuId(4);
        promotionVO3.setPromotionType(PromotionTypeEnum.SECKILL.name());

        /**
         * 团购商品90元
         */
        PromotionVO promotionVO4 = new PromotionVO();
        GroupbuyGoodsVO groupbuyGoods =SkuMocker.mockGroupBuyVO();
        promotionVO4.setGroupbuyGoodsVO(groupbuyGoods);
        promotionVO4.setSkuId(5);
        promotionVO4.setPromotionType(PromotionTypeEnum.GROUPBUY.name());

        Integer sellerId1 = 1;
        String sellerName1 = "天猫旗舰店";
        Integer sellerId2 = 2;
        String sellerName2= "京东旗舰店";

        selectedPromotionVo.putPromotion(sellerId1,promotionVO);
        selectedPromotionVo.putPromotion(sellerId1,promotionVO1);
        selectedPromotionVo.putPromotion(sellerId2,promotionVO2);
        selectedPromotionVo.putPromotion(sellerId2,promotionVO3);
        selectedPromotionVo.putPromotion(sellerId2,promotionVO4);

        List<CartSkuVO> skuVOList1  = new ArrayList<>();
        skuVOList1.add(skuVO1);
        skuVOList1.add(skuVO2);

        List<CartSkuVO> skuVOList2  = new ArrayList<>();
        skuVOList2.add(skuVO3);
        skuVOList2.add(skuVO4);
        skuVOList2.add(skuVO5);

        CartVO cartVO1 = new CartVO(sellerId1,sellerName1, CartType.CART);
        cartVO1.setSkuList(skuVOList1);

        CartVO cartVO2 = new CartVO(sellerId2,sellerName2, CartType.CART);
        cartVO2.setSkuList(skuVOList2);
        cartList.add(cartVO1);
        cartList.add(cartVO2);


    }


    @Test
    public void test(){

        this.mock();
        //mock 已经选择的促销vo
        when(cartPromotionManager.getSelectedPromotion()).thenReturn(selectedPromotionVo);

        //设置为不过期
        DateUtil.mockDate= DateUtil.getDateline("2018-01-01 13:10:00","yyyy-MM-dd HH:mm:ss");

        cartPriceCalculator.countPrice(cartList,null);


    }


}
