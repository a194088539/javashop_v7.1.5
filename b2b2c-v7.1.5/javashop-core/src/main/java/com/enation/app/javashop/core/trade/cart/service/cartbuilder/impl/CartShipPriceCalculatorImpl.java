package com.enation.app.javashop.core.trade.cart.service.cartbuilder.impl;

import com.enation.app.javashop.core.trade.cart.model.vo.CartVO;
import com.enation.app.javashop.core.trade.cart.service.cartbuilder.CartShipPriceCalculator;
import com.enation.app.javashop.core.trade.order.service.ShippingManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/12/19
 */
@Service
public class CartShipPriceCalculatorImpl implements CartShipPriceCalculator {
    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private ShippingManager shippingManager;

    @Override
    public void countShipPrice(List<CartVO> cartList) {
        shippingManager.setShippingPrice(cartList);

        if (logger.isDebugEnabled()) {
            logger.debug("购物车处理运费结果为：");
            logger.debug(cartList);
        }
    }

}
