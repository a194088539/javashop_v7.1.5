package com.enation.app.javashop.core.aftersale.service.impl;

import com.enation.app.javashop.core.aftersale.model.dos.AfterSaleGoodsDO;
import com.enation.app.javashop.core.aftersale.service.AfterSaleGoodsManager;
import com.enation.app.javashop.core.trade.order.model.dos.OrderItemsDO;
import com.enation.app.javashop.framework.database.DaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 售后服务商品业务接口实现
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-12-03
 */
@Service
public class AfterSaleGoodsManagerImpl implements AfterSaleGoodsManager {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Override
    public void add(AfterSaleGoodsDO afterSaleGoodsDO) {
        this.daoSupport.insert(afterSaleGoodsDO);
    }

    @Override
    public AfterSaleGoodsDO fillGoods(String serviceSn, Integer returnNum, OrderItemsDO itemsDO) {
        //获取售后商品相关信息
        AfterSaleGoodsDO afterSaleGoodsDO = new AfterSaleGoodsDO();
        //填充售后商品信息
        afterSaleGoodsDO.setServiceSn(serviceSn);
        afterSaleGoodsDO.setGoodsId(itemsDO.getGoodsId());
        afterSaleGoodsDO.setSkuId(itemsDO.getProductId());
        afterSaleGoodsDO.setShipNum(itemsDO.getNum());
        afterSaleGoodsDO.setPrice(itemsDO.getPrice());
        afterSaleGoodsDO.setReturnNum(returnNum);
        afterSaleGoodsDO.setGoodsName(itemsDO.getName());
        afterSaleGoodsDO.setGoodsImage(itemsDO.getImage());
        afterSaleGoodsDO.setSpecJson(itemsDO.getSpecJson());
        //售后商品信息入库
        this.add(afterSaleGoodsDO);

        return afterSaleGoodsDO;
    }

    @Override
    public List<AfterSaleGoodsDO> listGoods(String serviceSn) {
        String sql = "select * from es_as_goods where service_sn = ?";
        return this.daoSupport.queryForList(sql, AfterSaleGoodsDO.class, serviceSn);
    }

    @Override
    public void updateStorageNum(String serviceSn, Integer skuId, Integer num) {
        String sql = "update es_as_goods set storage_num = ? where service_sn = ? and sku_id = ?";
        this.daoSupport.execute(sql, num, serviceSn, skuId);
    }
}
