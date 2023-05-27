package com.enation.app.javashop.core.trade.cart.service.cartbuilder.impl;

import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.goods.model.vo.GoodsSkuVO;
import com.enation.app.javashop.core.trade.cart.model.vo.CartSkuVO;
import com.enation.app.javashop.core.trade.cart.model.vo.CartVO;
import com.enation.app.javashop.core.trade.cart.service.cartbuilder.CheckDataRebderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据正确性校验实现
 *
 * @author zh
 * @version v7.0
 * @date 18/12/27 上午10:05
 * @since v7.0
 */

@Service
public class CheckDataRendererImpl implements CheckDataRebderer {


    @Autowired
    private GoodsClient goodsClient;


    @Override
    public void checkData(List<CartVO> cartList) {
        for (CartVO cartVO : cartList) {
            for (CartSkuVO cartSkuVO : cartVO.getSkuList()) {
                GoodsSkuVO goodsSkuVO = goodsClient.getSkuFromCache(cartSkuVO.getSkuId());
                if (goodsSkuVO == null || goodsSkuVO.getLastModify() > cartSkuVO.getLastModify()) {
                    //设置购物车未选中
                    cartSkuVO.setChecked(0);
                    //设置购物车此sku商品已失效
                    cartSkuVO.setInvalid(1);
                    //设置失效消息
                    cartSkuVO.setErrorMessage("商品信息发生变化,已失效");
                    continue;
                }
                //商品库存不足
                if (goodsSkuVO.getEnableQuantity() <= 0) {
                    //设置购物车未选中
                    cartSkuVO.setChecked(0);
                    //设置购物车此sku商品已失效
                    cartSkuVO.setInvalid(1);
                    //设置失效消息
                    cartSkuVO.setErrorMessage("商品库存不足");
                }

            }
        }
    }


}
