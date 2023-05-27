package com.enation.app.javashop.core.client.member.impl;

import com.enation.app.javashop.core.client.member.ShopCatClient;
import com.enation.app.javashop.core.shop.model.dos.ShopCatDO;
import com.enation.app.javashop.core.shop.service.ShopCatManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version v7.0
 * @Description:
 * @Author: zjp
 * @Date: 2018/7/25 16:47
 */
@Service
@ConditionalOnProperty(value="javashop.product", havingValue="stand")
public class ShopCatClientDefaultImpl implements ShopCatClient {
    @Autowired
    private ShopCatManager shopCatManager;

    @Override
    public List getChildren(String catPath) {
        return shopCatManager.getChildren(catPath);
    }


    @Override
    public ShopCatDO getModel(Integer id) {
        return shopCatManager.getModel(id);
    }
}
