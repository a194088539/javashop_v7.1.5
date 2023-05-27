package com.enation.app.javashop.core.client.member;

import com.enation.app.javashop.core.shop.model.dos.ShopCatDO;

import java.util.List;

/**
 * @version v7.0
 * @Description: 店铺分组Client默认实现
 * @Author: zjp
 * @Date: 2018/7/25 16:46
 */
public interface ShopCatClient {
    /**
     * 获取当前分组所有的子(包括当前分组)
     * @param catPath
     * @return 子类集合
     */
    List getChildren(String catPath);

    /**
     * 获取店铺分组
     * @param id 店铺分组主键
     * @return ShopCatDO  店铺分组
     */
    ShopCatDO getModel(Integer id);
}
