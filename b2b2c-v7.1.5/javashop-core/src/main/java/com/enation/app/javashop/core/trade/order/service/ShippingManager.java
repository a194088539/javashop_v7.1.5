package com.enation.app.javashop.core.trade.order.service;

import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.trade.cart.model.vo.CartVO;

import java.util.List;
import java.util.Map;

/**
 * 运费计算业务层接口
 *
 * @author Snow create in 2018/4/8
 * @version v2.0
 * @since v7.0.0
 */
public interface ShippingManager {

    /**
     * 获取各个商家的运费
     *
     * @param cartList 购物车
     * @param areaId   地区
     * @return
     */
    Map<Integer, Double> getShippingPrice(List<CartVO> cartList, Integer areaId);

    /**
     * 设置运费
     *
     * @param cartList 购物车集合
     */
    void setShippingPrice(List<CartVO> cartList);

    /**
     * 检测是否有不能配送的区域
     *
     * @param cartList 购物车
     * @param areaId   地区
     * @return
     */
    List<CacheGoods> checkArea(List<CartVO> cartList, Integer areaId);


}
