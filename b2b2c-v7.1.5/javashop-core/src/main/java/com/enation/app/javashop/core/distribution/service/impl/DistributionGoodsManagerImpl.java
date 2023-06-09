package com.enation.app.javashop.core.distribution.service.impl;

import com.enation.app.javashop.core.distribution.model.dos.DistributionGoods;
import com.enation.app.javashop.core.distribution.service.DistributionGoodsManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * DistributionGoodsManagerImpl
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-06-14 上午12:39
 */
@Service
public class DistributionGoodsManagerImpl implements DistributionGoodsManager {


    @Autowired
    @Qualifier("distributionDaoSupport")
    private DaoSupport daoSupport;

    /**
     * 修改分销商品提现设置
     *
     * @param distributionGoods
     * @return
     */
    @Override
    @Transactional(value = "distributionTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public DistributionGoods edit(DistributionGoods distributionGoods) {
        DistributionGoods old = this.daoSupport.queryForObject("select * from es_distribution_goods where goods_id = ?", distributionGoods.getClass(), distributionGoods.getGoodsId());
        if (null == old) {
            daoSupport.insert("es_distribution_goods", distributionGoods);
            return distributionGoods;
        } else {
            Map<String, Object> map = new HashMap<>(16);
            map.put("id", old.getId());
            daoSupport.update("es_distribution_goods", distributionGoods, map);
            return distributionGoods;
        }
    }

    /**
     * 删除
     *
     * @param goodsId
     */
    @Override
    public void delete(Integer goodsId) {
        this.daoSupport.execute("delete from es_distribution_goods where goods_id = ?", goodsId);
    }

    /**
     * 获取
     *
     * @param goodsId
     */
    @Override
    public DistributionGoods getModel(Integer goodsId) {
        return this.daoSupport.queryForObject("select * from es_distribution_goods where goods_id = ?", DistributionGoods.class, goodsId);
    }
}
