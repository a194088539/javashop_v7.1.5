package com.enation.app.javashop.core.trade.cart.service;


import com.enation.app.javashop.core.trade.cart.model.enums.CheckedWay;
import com.enation.app.javashop.core.trade.cart.model.vo.CartView;

/**
 *
 * 购物车只读操作业务接口<br>
 * 包含对购物车读取操作
 * @author Snow
 * @since v7.0.0
 * @version v2.0
 * 2018年03月19日21:55:53
 */
public interface CartReadManager {


    /**
     * 读取购物车数据，并计算优惠和价格
     * @param way 获取方式
     * @return
     */
    CartView getCartListAndCountPrice(CheckedWay way);



    /**
     * 由缓存中取出已勾选的购物列表<br>
     * @param way 获取方式
     * @return
     */
    CartView  getCheckedItems(CheckedWay way);


}
