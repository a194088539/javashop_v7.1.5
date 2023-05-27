package com.enation.app.javashop.core.goods.service.impl;

import com.enation.app.javashop.core.goods.service.StaticsPageGoodsManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * StaticsPageGoodsManager
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-07-17 下午3:21
 */
@Service
public class StaticsPageGoodsManagerImpl implements StaticsPageGoodsManager {

    @Autowired
    @Qualifier("goodsDaoSupport")
    private DaoSupport daoSupport;

    /**
     * 商品总数
     *
     * @return
     */
    @Override
    public Integer count() {
        return daoSupport.queryForInt("select count(0) from es_goods");
    }

    /**
     * 商品数据获取
     *
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List goodsList(Integer page, Integer pageSize) {
        return this.daoSupport.queryForListPage("select goods_id,goods_name from es_goods order by goods_id desc ", page, pageSize);
    }
}
