package com.enation.app.javashop.core.shop.model.vo;

import java.io.Serializable;

/**
 * 店铺名称改变消息
 *
 * @author zh
 * @version v7.0
 * @date 18/12/6 下午4:21
 * @since v7.0
 */

public class ShopChangeMsg implements Serializable {


    private static final long serialVersionUID = -109352767337144162L;

    public ShopChangeMsg() {

    }

    public ShopChangeMsg(ShopVO originalShop, ShopVO newShop) {
        this.originalShop = originalShop;
        this.newShop = newShop;
    }


    /**
     * 原店铺信息
     */
    private ShopVO originalShop;

    /**
     * 现在店铺信息
     */
    private ShopVO newShop;


    public ShopVO getOriginalShop() {
        return originalShop;
    }

    public void setOriginalShop(ShopVO originalShop) {
        this.originalShop = originalShop;
    }

    public ShopVO getNewShop() {
        return newShop;
    }

    public void setNewShop(ShopVO newShop) {
        this.newShop = newShop;
    }

    @Override
    public String toString() {
        return "ShopStatusChangeMsg{" +
                "originalShop=" + originalShop +
                ", newShop=" + newShop +
                '}';
    }

}
