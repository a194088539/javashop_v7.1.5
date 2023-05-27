package com.enation.app.javashop.core.client.member.impl;

import com.enation.app.javashop.core.client.member.ShopClient;
import com.enation.app.javashop.core.shop.model.dto.ShopBankDTO;
import com.enation.app.javashop.core.shop.model.vo.ShopVO;
import com.enation.app.javashop.core.shop.service.ShopManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zjp
 * @version v7.0
 * @Description
 * @ClassName ShopClientDefaultImpl
 * @since v7.0 上午10:02 2018/7/13
 */
@Service
@ConditionalOnProperty(value="javashop.product", havingValue="stand")
public class ShopClientDefaultImpl implements ShopClient{

    @Autowired
    private ShopManager shopManager;

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport daoSupport;

    @Override
    public ShopVO getShop(Integer shopId) {
        return shopManager.getShop(shopId);
    }

    @Override
    public List<ShopBankDTO> listShopBankInfo() {
        return shopManager.listShopBankInfo();
    }

    @Override
    public void addCollectNum(Integer shopId) {
        shopManager.addcollectNum(shopId);
    }

    @Override
    public void reduceCollectNum(Integer shopId) {
        shopManager.reduceCollectNum(shopId);
    }

    @Override
    public void calculateShopScore() {
        shopManager.calculateShopScore();
    }

    @Override
    public void updateShopGoodsNum(Integer sellerId, Integer sellerGoodsCount) {
        String sql = "update es_shop_detail set goods_num  = ? where shop_id = ? ";
        this.daoSupport.execute(sql,sellerGoodsCount,sellerId);
    }


}
