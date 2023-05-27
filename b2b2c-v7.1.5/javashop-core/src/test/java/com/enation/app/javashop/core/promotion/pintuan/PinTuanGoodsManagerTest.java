package com.enation.app.javashop.core.promotion.pintuan;

import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.goods.model.dos.CategoryDO;
import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.goods.model.vo.GoodsSkuVO;
import com.enation.app.javashop.core.promotion.pintuan.model.PinTuanGoodsVO;
import com.enation.app.javashop.core.promotion.pintuan.model.Pintuan;
import com.enation.app.javashop.core.promotion.pintuan.model.PintuanGoodsDO;
import com.enation.app.javashop.core.promotion.pintuan.service.PintuanGoodsManager;
import com.enation.app.javashop.core.promotion.pintuan.service.PintuanManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.DateUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.when;

/**
 * Created by kingapex on 2019-01-22.
 * 拼团商品单元测试
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2019-01-22
 */
public class PinTuanGoodsManagerTest extends BaseTest {


    @Autowired
    private PintuanManager pintuanManager;

    @Autowired
    private PintuanGoodsManager pintuanGoodsManager;

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport tradeDaoSupport;

    @MockBean
    private GoodsClient goodsClient;
    Integer goodsId=1;
    int categoryId =1;

    public void before() {
        tradeDaoSupport.execute(" TRUNCATE TABLE es_pintuan ");
        tradeDaoSupport.execute(" TRUNCATE TABLE es_pintuan_goods ");
    }

    /**
     * 测试添加拼团功能
     */
    @Test
    public void testAdd() {
        before();
        Pintuan pintuan = innerAdd();
        Pintuan pintuanDB =  pintuanManager.getModel(pintuan.getPromotionId());
        Assert.assertEquals(pintuanDB, pintuan);

    }

    @Test
    public void testAddGoods() {
        before();
        mockGoods();
        Pintuan pintuan = innerAdd();
        Pintuan pintuanDB =  pintuanManager.getModel(pintuan.getPromotionId());
        Integer pintuanId  = pintuanDB.getPromotionId();

        List<PintuanGoodsDO>  goodsList  = new ArrayList<>();

        PintuanGoodsDO pintuanGoodsDO1 = new PintuanGoodsDO();
        pintuanGoodsDO1.setPintuanId(pintuanId);
        pintuanGoodsDO1.setGoodsName("商品1");
        pintuanGoodsDO1.setLockedQuantity(0);
        pintuanGoodsDO1.setSoldQuantity(10);
        pintuanGoodsDO1.setOriginPrice(100D);
        pintuanGoodsDO1.setSalesPrice(80D);
        pintuanGoodsDO1.setSkuId(1);
        pintuanGoodsDO1.setGoodsId(goodsId);
        pintuanGoodsDO1.setSn("11-2");
        goodsList.add(pintuanGoodsDO1);


        PintuanGoodsDO pintuanGoodsDO2 = new PintuanGoodsDO();
        pintuanGoodsDO2.setPintuanId(pintuanId);
        pintuanGoodsDO2.setGoodsName("商品2");
        pintuanGoodsDO2.setLockedQuantity(0);
        pintuanGoodsDO2.setSoldQuantity(10);
        pintuanGoodsDO2.setOriginPrice(100D);
        pintuanGoodsDO2.setSalesPrice(80D);
        pintuanGoodsDO2.setSkuId(2);
        pintuanGoodsDO2.setGoodsId(goodsId);
        pintuanGoodsDO2.setSn("11-1");
        goodsList.add(pintuanGoodsDO2);

        pintuanGoodsManager.save(pintuanDB.getPromotionId(),goodsList);

    }


    @Test
    public void testGet() {

        PinTuanGoodsVO pinTuanGoodsVO = pintuanGoodsManager.getDetail(1,null);
        System.out.println(pinTuanGoodsVO);

    }


    @Test
    public void testSkus() {

        mockGoods();

       List<GoodsSkuVO> ptSkuList =  pintuanGoodsManager.skus(goodsId,null);
        System.out.println(ptSkuList);

    }


    private void mockGoods() {
        CacheGoods cacheGoods = new CacheGoods();
        GoodsSkuVO skuVO1 = new GoodsSkuVO();
        skuVO1.setSkuId(1);
        GoodsSkuVO skuVO2 = new GoodsSkuVO();
        skuVO2.setSkuId(2);
        GoodsSkuVO skuVO3 = new GoodsSkuVO();
        skuVO3.setSkuId(3);

        List<GoodsSkuVO> skuList = new ArrayList<>();
        skuList.add(skuVO1);
        skuList.add(skuVO2);
        skuList.add(skuVO3);

        cacheGoods.setSkuList(skuList);
        cacheGoods.setCategoryId(categoryId);
        cacheGoods.setGoodsName("测试商品");

        when(goodsClient.getFromCache(goodsId)).thenReturn(cacheGoods);


        CategoryDO categoryDO = new CategoryDO();
        categoryDO.setCategoryId(1);
        categoryDO.setCategoryPath("1,1");
        categoryDO.setName("测试分类");
        when(goodsClient.getCategory(categoryId )).thenReturn(categoryDO);
    }

    private Pintuan innerAdd() {
        Pintuan pintuan = new Pintuan();
        pintuan.setPromotionName("拼团名称");
        pintuan.setPromotionTitle("拼团标题");
        pintuan.setEnableMocker(1);
        pintuan.setLimitNum(1);
        pintuan.setRequiredNum(3);

        Long  startTime  = 	DateUtil.getDateline("2019-01-22 15:00","yyyy-MM-dd HH:mm");
        Long  endTime  = 	DateUtil.getDateline("2019-02-25 15:00","yyyy-MM-dd HH:mm");

        pintuan.setStartTime(startTime);
        pintuan.setEndTime(endTime);

        pintuanManager.add(pintuan);

        return pintuan;
    }
}
