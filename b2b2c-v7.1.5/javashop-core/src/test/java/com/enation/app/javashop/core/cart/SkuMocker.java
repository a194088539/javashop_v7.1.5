package com.enation.app.javashop.core.cart;

import com.enation.app.javashop.core.promotion.exchange.model.dos.ExchangeDO;
import com.enation.app.javashop.core.promotion.groupbuy.model.vo.GroupbuyGoodsVO;
import com.enation.app.javashop.core.promotion.halfprice.model.vo.HalfPriceVO;
import com.enation.app.javashop.core.promotion.minus.model.vo.MinusVO;
import com.enation.app.javashop.core.promotion.seckill.model.vo.SeckillGoodsVO;
import com.enation.app.javashop.core.trade.cart.model.vo.CartSkuVO;
import com.enation.app.javashop.framework.util.DateUtil;

/**
 * Created by kingapex on 2018/12/13.
 * 模拟sku
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/12/13
 */
public class SkuMocker {

    /**
     * 模拟一个sku:两个商品，单价100元，总计200元
     * @return
     */
    public static CartSkuVO mockSku() {

        CartSkuVO skuVO = new CartSkuVO();
        skuVO.setNum(2);
        skuVO.setOriginalPrice(100d);
        skuVO.setSubtotal(skuVO.getOriginalPrice() * skuVO.getNum());
        skuVO.setGoodsWeight(10D);
        return skuVO;

    }


    /**
     * 模拟一个带skuid的skuvo
     * @param skuid
     * @return
     */
    public static CartSkuVO mockSku(int skuid) {
        CartSkuVO skuVO = mockSku();
        skuVO.setSkuId(skuid);
        skuVO.setName("商品"+skuid);
        return  skuVO;
    }

    /**
     * 模拟一个MinusVO
     * @return
     */
    public static MinusVO createMinusVO(){

        MinusVO minusVO = new MinusVO();

        Long  startTime  =  DateUtil.getDateline("2018-01-01 12:00:00","yyyy-MM-dd HH:mm:ss");

        //5日过期
        Long  endTime  =  DateUtil.getDateline("2018-01-05 12:00:00","yyyy-MM-dd HH:mm:ss");
        minusVO.setStartTime(startTime);
        minusVO.setEndTime(endTime);
        //设定为单品减10元
        minusVO.setSingleReductionValue(10D);

        return minusVO;
    }


    /**
     * 模拟一个 ExchangeDO
     * @return
     */
    public static ExchangeDO mockExchangeDO(){
        ExchangeDO exchangeDO = new ExchangeDO();
        exchangeDO.setExchangeMoney(90D);
        exchangeDO.setExchangePoint(10);
        return exchangeDO;
    }

    /**
     * 创建一个默认的秒杀
     * @return
     */
    public static SeckillGoodsVO mockSeckill(){

        SeckillGoodsVO seckillGoodsVO = new SeckillGoodsVO();
        seckillGoodsVO.setSeckillPrice(90D);

        //秒杀开始时间
        Long  startTime  =  DateUtil.getDateline("2018-01-01 12:00:00","yyyy-MM-dd HH:mm:ss");
        seckillGoodsVO.setStartTime( startTime);

        //已售数量
        seckillGoodsVO.setSoldNum(1);
        //售空数量
        seckillGoodsVO.setSoldQuantity(5);

        return seckillGoodsVO;
    }


    /**
     * 模拟一个团购活动
     * @return
     */
    public static  GroupbuyGoodsVO mockGroupBuyVO(){
        GroupbuyGoodsVO groupbuyGoods = new GroupbuyGoodsVO();
        groupbuyGoods.setPrice(90D);

        Long  startTime  =  DateUtil.getDateline("2018-01-01 12:00:00","yyyy-MM-dd HH:mm:ss");

        //5日过期
        Long  endTime  =  DateUtil.getDateline("2018-01-05 12:00:00","yyyy-MM-dd HH:mm:ss");

        groupbuyGoods.setStartTime(startTime);
        groupbuyGoods.setEndTime(endTime);

        //已售数量为1
        groupbuyGoods.setBuyNum(1);

        //售空数量为5
        groupbuyGoods.setGoodsNum(5);


        //最多可以买3个
        groupbuyGoods.setLimitNum(3);
        return groupbuyGoods;
    }


    /**
     * 模拟一个第二件半件vo
     * @return
     */
    public static HalfPriceVO mockHalfPriceVO(){
        HalfPriceVO halfPriceVO = new HalfPriceVO();
        Long  startTime  =  DateUtil.getDateline("2018-01-01 12:00:00","yyyy-MM-dd HH:mm:ss");

        //5日过期
        Long  endTime  =  DateUtil.getDateline("2018-01-05 12:00:00","yyyy-MM-dd HH:mm:ss");

        halfPriceVO.setStartTime(startTime);
        halfPriceVO.setEndTime(endTime);

        return halfPriceVO;
    }





}
