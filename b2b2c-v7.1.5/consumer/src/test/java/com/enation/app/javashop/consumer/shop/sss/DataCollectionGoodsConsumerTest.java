package com.enation.app.javashop.consumer.shop.sss;

import com.enation.app.javashop.core.statistics.model.dto.GoodsData;
import com.enation.app.javashop.core.statistics.service.GoodsDataManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 商品收藏更新单元测试
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-07-23 下午5:50
 */
public class DataCollectionGoodsConsumerTest extends BaseTest {

    @Autowired
    private DataCollectionGoodsConsumer dataCollectionGoodsConsumer;


    @Autowired
    @Qualifier("sssDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private GoodsDataManager goodsDataManager;

    @Test
    public void goodsCollectionChange() {

        this.daoSupport.execute("TRUNCATE TABLE es_sss_goods_data");
        this.daoSupport.execute("INSERT INTO `es_sss_goods_data` VALUES ('1', '9527', 'AMQP goods1', '31', '1231', '|0|1|2|', '123', null, '951.27', '1', '1');");

        GoodsData goodsData = new GoodsData();
        goodsData.setFavoriteNum(9994);
        goodsData.setGoodsId(9527);

        dataCollectionGoodsConsumer.goodsCollectionChange(goodsData);

        GoodsData afterGoods = goodsDataManager.get(goodsData.getGoodsId());
        Assert.assertEquals(9994L, afterGoods.getFavoriteNum(), 0);

    }


}
