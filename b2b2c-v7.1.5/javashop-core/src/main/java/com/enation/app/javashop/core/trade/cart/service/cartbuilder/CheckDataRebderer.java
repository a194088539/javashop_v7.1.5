package com.enation.app.javashop.core.trade.cart.service.cartbuilder;

import com.enation.app.javashop.core.trade.cart.model.vo.CartVO;

import java.util.List;

/**
 * 数据正确性校验器
 *
 * @author zh
 * @version v7.0
 * @date 18/12/27 上午10:05
 * @since v7.0
 */
public interface CheckDataRebderer {
    /**
     * 数据正确性校验
     *
     * @param cartList 购物车数据
     */
    void checkData(List<CartVO> cartList);
}
