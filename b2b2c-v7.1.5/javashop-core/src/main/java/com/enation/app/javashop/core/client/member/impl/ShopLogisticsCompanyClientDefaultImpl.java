package com.enation.app.javashop.core.client.member.impl;

import com.enation.app.javashop.core.client.member.ShopLogisticsCompanyClient;
import com.enation.app.javashop.core.shop.service.ShopLogisticsCompanyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 店铺物流client实现
 *
 * @author fk
 * @version v7.0
 * @date 19/7/27 下午3:51
 * @since v7.0
 */
@Service
@ConditionalOnProperty(value="javashop.product", havingValue="stand")
public class ShopLogisticsCompanyClientDefaultImpl implements ShopLogisticsCompanyClient {


    @Autowired
    private ShopLogisticsCompanyManager shopLogisticsCompanyManager;

    @Override
    public List queryListByLogisticsId(Integer logisticsId) {

        return shopLogisticsCompanyManager.queryListByLogisticsId(logisticsId);
    }

    @Override
    public void deleteByLogisticsId(Integer logisticsId) {

        shopLogisticsCompanyManager.deleteByLogisticsId(logisticsId);
    }
}
