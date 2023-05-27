package com.enation.app.javashop.core.statistics.service;

import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.statistics.model.dos.GoodsPageView;
import com.enation.app.javashop.core.statistics.model.dos.ShopPageView;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * 流量单元测试
 *
 * @author liushuai
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/8/7 上午8:21
 */

public class DisplayTimesManagerTest extends BaseTest {


    /**
     * 商品访问
     */
    private final String GOODS = "GOODS_VIEW";
    /**
     * 店铺访问
     */
    private final String SHOP = "SHOP_VIEW";

    /**
     * 访问记录
     */
    private final String HISTORY = "VIEW_HISTORY";

    @Autowired
    @Qualifier("sssDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private DisplayTimesManager displayTimesManager;

    @Autowired
    private Cache cache;

    @MockBean
    private GoodsClient goodsClient;




    @Before
    public void before() {
        this.daoSupport.execute("TRUNCATE TABLE es_sss_shop_pv");
        this.daoSupport.execute("TRUNCATE TABLE es_sss_goods_pv");
        cache.remove(HISTORY);
        cache.remove(GOODS);
        cache.remove(SHOP);
        initData();

        CacheGoods cacheGoods = new CacheGoods();
        cacheGoods.setGoodsName("test_goods");
        cacheGoods.setSellerId(1);
        when(goodsClient.getFromCache(anyInt())).thenReturn(cacheGoods);
    }

    private void initData() {

        //正常访问
        displayTimesManager.view("www.baidu.com/shop/1", "AAAABBBBCCCCDDDDEEEEFFFGGGG");
        displayTimesManager.view("www.baidu.com/goods/1", "AAAABBBBCCCCDDDDEEEEFFFGGGG");
        //重复访问
        displayTimesManager.view("www.baidu.com/shop/1", "AAAABBBBCCCCDDDDEEEEFFFGGGG");
        displayTimesManager.view("www.baidu.com/goods/1", "AAAABBBBCCCCDDDDEEEEFFFGGGG");
        //不同用户访问
        displayTimesManager.view("www.baidu.com/shop/1", "AAAABBBBCCCCDDDDEEEEFFFGGGG1");
        displayTimesManager.view("www.baidu.com/goods/1", "AAAABBBBCCCCDDDDEEEEFFFGGG1");
        //访问其他商品或店铺
        displayTimesManager.view("www.baidu.com/shop/2", "AAAABBBBCCCCDDDDEEEEFFFGGGG");
        displayTimesManager.view("www.baidu.com/goods/2", "AAAABBBBCCCCDDDDEEEEFFFGGGG");
        //无效访问
        displayTimesManager.view("www.baidu.com/shopasdf/1", "AAAABBBBCCCCDDDDEEEEFFFGGGG");
        displayTimesManager.view("www.baidu.com/goodsaf/1", "AAAABBBBCCCCDDDDEEEEFFFGGGG");

    }

    /**
     * 访问某地址
     */
    @Test
    public void view() {
        initData();
        List<GoodsPageView> pageViewList = (List<GoodsPageView>) this.cache.get(GOODS);

        List<ShopPageView> shopPageViews = (List<ShopPageView>) this.cache.get(SHOP);

        Assert.assertEquals(pageViewList.size(), 3);
        Assert.assertEquals(shopPageViews.size(), 3);
    }


    /**
     * 立即整理现有的数据
     */
    @Test
    public void countNow() {
        displayTimesManager.countNow();
        List<ShopPageView> shopPageViews = daoSupport.queryForList("select * from es_sss_shop_pv", ShopPageView.class);
        List<GoodsPageView> goodsPageViews = daoSupport.queryForList("select * from es_sss_goods_pv", GoodsPageView.class);
        Assert.assertEquals(goodsPageViews.size(), 2);
        Assert.assertEquals(shopPageViews.size(), 2);
    }

    /**
     * 将统计好的店铺数据 写入数据库
     */
    @Test
    public void countShop() {

        displayTimesManager.countShop((List<ShopPageView>) cache.get(SHOP));
        List<ShopPageView> shopPageViews = daoSupport.queryForList("select * from es_sss_shop_pv", ShopPageView.class);
        Assert.assertEquals(shopPageViews.size(), 2);
    }


    /**
     * 将统计好的商品数据 写入数据库
     */
    @Test
    public void countGoods() {

        displayTimesManager.countGoods((List<GoodsPageView>) cache.get(GOODS));
        List<GoodsPageView> goodsPageViews = daoSupport.queryForList("select * from es_sss_goods_pv", GoodsPageView.class);
        Assert.assertEquals(goodsPageViews.size(), 2);
    }


}
