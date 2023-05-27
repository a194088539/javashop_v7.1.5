package com.enation.app.javashop.consumer.shop.aftersale;

import com.enation.app.javashop.consumer.core.event.AfterSaleChangeEvent;
import com.enation.app.javashop.core.aftersale.model.dto.PutInWarehouseDTO;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceStatusEnum;
import com.enation.app.javashop.core.base.message.AfterSaleChangeMessage;
import com.enation.app.javashop.core.client.goods.GoodsQuantityClient;
import com.enation.app.javashop.core.goods.model.enums.QuantityType;
import com.enation.app.javashop.core.goods.model.vo.GoodsQuantityVO;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.enums.ShipStatusEnum;
import com.enation.app.javashop.core.trade.order.service.OrderQueryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 修改商品库存
 * 商家入库售后服务商品时，需要恢复对应商品的库存
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-12-04
 */
@Component
public class UpdateGoodsQuantityConsumer  implements AfterSaleChangeEvent {

    @Autowired
    private OrderQueryManager orderQueryManager;

    @Autowired
    private GoodsQuantityClient goodsQuantityClient;

    @Override
    public void afterSaleChange(AfterSaleChangeMessage afterSaleChangeMessage) {

        //如果售后服务状态为退货入库
        if (ServiceStatusEnum.STOCK_IN.equals(afterSaleChangeMessage.getServiceStatus())) {

            //获取申请售后的订单信息
            OrderDO orderDO = this.orderQueryManager.getOrder(afterSaleChangeMessage.getOrderSn());

            //获取商品入库信息
            List<PutInWarehouseDTO> storageList = afterSaleChangeMessage.getStorageList();

            //要更新的库存列表
            List<GoodsQuantityVO> stockList = new ArrayList<>();

            for (PutInWarehouseDTO warehouseDTO : storageList) {
                //可用库存
                GoodsQuantityVO enable = this.initGoodsQuantity(warehouseDTO.getStorageNum(), warehouseDTO.getGoodsId(), QuantityType.enable, warehouseDTO.getSkuId());
                stockList.add(enable);

                //如果申请售后的订单发货状态为已发货或已收货，那么入库时还需要回复商品的实际库存
                if (!ShipStatusEnum.SHIP_NO.value().equals(orderDO.getShipStatus())) {
                    //实际库存
                    GoodsQuantityVO actual = this.initGoodsQuantity(warehouseDTO.getStorageNum(), warehouseDTO.getGoodsId(), QuantityType.actual, warehouseDTO.getSkuId());
                    stockList.add(actual);
                }
            }

            //如果要更新的库存列表集合不为空并且长度不为0
            if (stockList != null && stockList.size() != 0) {
                //更新商品库存
                this.goodsQuantityClient.updateSkuQuantity(stockList);
            }
        }
    }

    /**
     * 构建库存VO
     * @param storageNum 库存数量
     * @param goodsId 商品id
     * @param quantityType 库存类型 可用库存和实际库存
     * @param skuId 商品skuID
     */
    private GoodsQuantityVO initGoodsQuantity(Integer storageNum, Integer goodsId, QuantityType quantityType, Integer skuId) {
        GoodsQuantityVO goodsQuantityVO = new GoodsQuantityVO();
        goodsQuantityVO.setQuantity(storageNum);
        goodsQuantityVO.setGoodsId(goodsId);
        goodsQuantityVO.setQuantityType(quantityType);
        goodsQuantityVO.setSkuId(skuId);
        return goodsQuantityVO;
    }
}
