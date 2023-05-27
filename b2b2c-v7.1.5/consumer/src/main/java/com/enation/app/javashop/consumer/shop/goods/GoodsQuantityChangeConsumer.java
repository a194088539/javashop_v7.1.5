package com.enation.app.javashop.consumer.shop.goods;

import com.enation.app.javashop.consumer.core.event.OrderStatusChangeEvent;
import com.enation.app.javashop.core.base.message.OrderStatusChangeMsg;
import com.enation.app.javashop.core.client.goods.GoodsQuantityClient;
import com.enation.app.javashop.core.goods.model.enums.QuantityType;
import com.enation.app.javashop.core.goods.model.vo.GoodsQuantityVO;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.enums.OrderStatusEnum;
import com.enation.app.javashop.core.trade.order.model.enums.PayStatusEnum;
import com.enation.app.javashop.core.trade.order.model.vo.OrderSkuVO;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品库存增加/扣减
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018年6月22日 上午10:18:20
 */
@Service
public class GoodsQuantityChangeConsumer implements OrderStatusChangeEvent{

    @Autowired
    private GoodsQuantityClient goodsQuantityClient;


    /**
     * 订单变化处理
     *
     * @param orderMessage
     */
    @Override
    public void orderChange(OrderStatusChangeMsg orderMessage) {
        //发货
        if (orderMessage.getNewStatus().name().equals(OrderStatusEnum.SHIPPED.name())) {
            //获取订单信息
            OrderDO order = orderMessage.getOrderDO();
            String itemsJson = order.getItemsJson();
            //订单中的sku集合
            List<OrderSkuVO> list = JsonUtil.jsonToList(itemsJson, OrderSkuVO.class);
            List<GoodsQuantityVO> quantityVOList = new ArrayList<>();
            for (OrderSkuVO sku : list) {
                GoodsQuantityVO goodsQuantity = new GoodsQuantityVO();

                goodsQuantity.setGoodsId(sku.getGoodsId());

                //设置为要减掉的库存
                goodsQuantity.setQuantity(0 - sku.getNum());
                //发货要减少实际的库存
                goodsQuantity.setQuantityType(QuantityType.actual);

                goodsQuantity.setSkuId(sku.getSkuId());

                quantityVOList.add(goodsQuantity);
            }
            //扣减库存
            goodsQuantityClient.updateSkuQuantity(quantityVOList);

        }

        //付款前 订单取消
        if (orderMessage.getNewStatus().name().equals(OrderStatusEnum.CANCELLED.name()) && orderMessage.getOrderDO().getPayStatus().equals(PayStatusEnum.PAY_NO.name())) {

            List<GoodsQuantityVO> quantityVOList = new ArrayList<>();

            OrderDO order = orderMessage.getOrderDO();
            String itemsJson = order.getItemsJson();
            List<OrderSkuVO> list = JsonUtil.jsonToList(itemsJson, OrderSkuVO.class);

            for (OrderSkuVO sku : list) {

                GoodsQuantityVO goodsQuantity = new GoodsQuantityVO();
                goodsQuantity.setGoodsId(sku.getGoodsId());

                //取消订单要恢复下单时占用的可用库存
                goodsQuantity.setQuantity(sku.getNum());
                goodsQuantity.setQuantityType(QuantityType.enable);
                goodsQuantity.setSkuId(sku.getSkuId());
                quantityVOList.add(goodsQuantity);

            }

            goodsQuantityClient.updateSkuQuantity(quantityVOList);

        }

    }
}
