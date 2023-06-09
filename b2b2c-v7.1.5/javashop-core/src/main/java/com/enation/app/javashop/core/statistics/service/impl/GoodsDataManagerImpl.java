package com.enation.app.javashop.core.statistics.service.impl;

import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.client.member.MemberCollectionGoodsClient;
import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.statistics.model.dto.GoodsData;
import com.enation.app.javashop.core.statistics.service.GoodsDataManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.util.SqlUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品数据收集
 *
 * @author mengyuanming
 * @version 2.0
 * @since 7.0
 * 2018/6/4 9:54
 */
@Service
public class GoodsDataManagerImpl implements GoodsDataManager {


    protected final Log logger = LogFactory.getLog(this.getClass());


    @Autowired
    @Qualifier("sssDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private MemberCollectionGoodsClient memberCollectionGoodsClient;

    @Override
    public void addGoods(Integer[] goodsIds) {
        for (Integer goodsId : goodsIds) {
            CacheGoods cacheGoods = goodsClient.getFromCache(goodsId);
            GoodsData gd = new GoodsData(cacheGoods);
            gd.setFavoriteNum(0);
            try {
                gd.setCategoryPath(goodsClient.getCategory(gd.getCategoryId()).getCategoryPath());
            } catch (Exception e) {
                this.logger.error("错误的商品分类id：" + gd.getCategoryId());
                gd.setCategoryPath("");
            }
            this.daoSupport.insert("es_sss_goods_data", gd);
        }
    }

    /**
     * 似有方法，新增商品
     *
     * @param cacheGoods
     */
    private void saveGoods(CacheGoods cacheGoods) {
        GoodsData gd = new GoodsData(cacheGoods);
        gd.setFavoriteNum(memberCollectionGoodsClient.getGoodsCollectCount(gd.getGoodsId()));
        gd.setCategoryPath(goodsClient.getCategory(gd.getCategoryId()).getCategoryPath());
        this.daoSupport.insert("es_sss_goods_data", gd);
    }

    @Override
    public void updateGoods(Integer[] goodsIds) {

        for (Integer goodsId : goodsIds) {
            CacheGoods cacheGoods = goodsClient.getFromCache(goodsId);
            GoodsData gd = this.daoSupport.queryForObject("select * from es_sss_goods_data where goods_id = ?",
                    GoodsData.class, cacheGoods.getGoodsId());
            if (gd == null) {
                this.saveGoods(cacheGoods);
            } else {
                gd = new GoodsData(cacheGoods, gd);
                gd.setFavoriteNum(memberCollectionGoodsClient.getGoodsCollectCount(gd.getGoodsId()));
                gd.setCategoryPath(goodsClient.getCategory(gd.getCategoryId()).getCategoryPath());
                Map<String, String> where = new HashMap(16);
                where.put("id", gd.getId() + "");
                this.daoSupport.update("es_sss_goods_data", gd, where);
            }
        }

    }

    @Override
    public void deleteGoods(Integer[] goodsIds) {
        List<Object> term = new ArrayList<>();

        String str = SqlUtil.getInSql(goodsIds,term);

        String sql = "delete from es_sss_goods_data where goods_id in (" + str + ")";

        daoSupport.execute(sql, term.toArray());
    }


    /**
     * 商品收藏数量修改
     *
     * @param goodsData
     */
    @Override
    public void updateCollection(GoodsData goodsData) {
        this.daoSupport.execute("update es_sss_goods_data set favorite_num = ? where goods_id = ?", goodsData.getFavoriteNum(), goodsData.getGoodsId());
    }

    /**
     * 获取商品
     *
     * @param goodsId
     * @return
     */
    @Override
    public GoodsData get(Integer goodsId) {

        return this.daoSupport.queryForObject("select * from es_sss_goods_data where goods_id = ?", GoodsData.class, goodsId);
    }

    /**
     * 下架所有商品
     *
     * @param sellerId
     */
    @Override
    public void underAllGoods(Integer sellerId) {
        this.daoSupport.execute("update es_sss_goods_data set market_enable = 0 where seller_id = ?", sellerId);

    }
}
