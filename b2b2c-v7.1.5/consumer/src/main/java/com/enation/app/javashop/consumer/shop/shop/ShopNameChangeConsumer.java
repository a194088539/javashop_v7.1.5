package com.enation.app.javashop.consumer.shop.shop;

import com.enation.app.javashop.consumer.core.event.ShopChangeEvent;
import com.enation.app.javashop.core.aftersale.service.AfterSaleManager;
import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.client.goods.GoodsIndexClient;
import com.enation.app.javashop.core.promotion.coupon.service.CouponManager;
import com.enation.app.javashop.core.shop.model.vo.ShopChangeMsg;
import com.enation.app.javashop.core.shop.model.vo.ShopVO;
import com.enation.app.javashop.core.trade.order.service.OrderOperateManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 店铺信息同步
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-12-10 下午4:05
 */
@Component
public class ShopNameChangeConsumer implements ShopChangeEvent {
    @Autowired
    private GoodsIndexClient goodsIndexClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private CouponManager couponManager;
    @Autowired
    private AfterSaleManager afterSaleManager;
    @Autowired
    private OrderOperateManager orderOperateManager;

    protected final Log logger = LogFactory.getLog(this.getClass());


    /**
     * 店铺名称改变消息
     *
     * @param shopChangeMsg 店铺名称改变消息
     */
    @Override
    public void shopChange(ShopChangeMsg shopChangeMsg) {
        try {
            //原店铺数据
            ShopVO originalShop = shopChangeMsg.getOriginalShop();
            //更新后店铺数据
            ShopVO shop = shopChangeMsg.getNewShop();
            //如果店铺名称发生变化
            if (!originalShop.getShopName().equals(shop.getShopName())) {
                //修改商品的店铺名称
                goodsClient.changeSellerName(shop.getShopId(), shop.getShopName());
                //查询此店家的商品集合，循环更新索引
                List<Map<String, Object>> goods = goodsClient.getGoodsAndParams(shop.getShopId());
                if (goods.size() > 0) {
                    for (int i = 0; i < goods.size(); i++) {
                        goodsIndexClient.updateIndex(goods.get(i));
                    }
                }
                //修改优惠券中的店铺名
                couponManager.editCouponShopName(shop.getShopId(), shop.getShopName());
                //修改售后服务单的店铺名称
                afterSaleManager.editAfterSaleShopName(shop.getShopId(), shop.getShopName());
                //修改订单的店铺名称
                orderOperateManager.editOrderShopName(shop.getShopId(), shop.getShopName());

            }
        } catch (Exception e) {
            logger.error("处理店铺名称改变出错" + e.getMessage());
            e.printStackTrace();
        }
    }
}