package com.enation.app.javashop.core.trade.cart.service.cartbuilder.impl;

import com.enation.app.javashop.core.trade.cart.model.vo.CartSkuVO;

/**
 * 购物车sku过滤器<br/>
 * 指定一定的条件，进行过滤购物车的sku。<br/>
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/12/19
 */
public interface CartSkuFilter {

    /**
     * 指定要过滤的条件
     * @param cartSkuVO 当前的sku，做要比对的对象
     * @return 返回true/false来决定是否过滤
     */
    boolean accept(CartSkuVO cartSkuVO);
}
