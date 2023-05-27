package com.enation.app.javashop.core.goods.service;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.goods.model.dos.GoodsDO;
import com.enation.app.javashop.core.goods.model.dos.GoodsSkuDO;
import com.enation.app.javashop.core.goods.model.enums.QuantityType;
import com.enation.app.javashop.core.goods.model.vo.GoodsQuantityVO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品库存管理类测试，覆盖如下场景<br/>
 * <li>添加sku，初始化库存，商品及sku库存应该可以被正确的初始化<li/>
 * <li>扣减库存，商品及sku库存应该可以被正确的扣减<li/>
 * <li>增加库存，商品及sku库存应该可以被正确的增加<li/>
 * <li>缓冲区的测试：未达到阙值，数据库数据未被同步，达到缓冲区的值，数据库数据同步<li/>
 *
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2019-02-22
 */

public class GoodsQuantityManagerTest extends BaseTest {

    @Autowired
    private GoodsQuantityManager goodsQuantityManager;

    @Autowired
    private DaoSupport daoSupport;

    @Autowired
    public StringRedisTemplate stringRedisTemplate;


    int goodsId1 = 0;
    int goodsId2 = 0;
    List<GoodsSkuDO> skuList = new ArrayList<GoodsSkuDO>();
    List<GoodsQuantityVO> quantityList = new ArrayList();

    private ValueOperations cache() {
        return stringRedisTemplate.opsForValue();
    }

    @Test
    public void initTest() {

        this.add();

        //验证数据库及缓存全部成功
        baseAssert();

        //清除数据
        clean();

    }


    private void baseAssert() {
        skuList.forEach(goodsSku -> {

            String enableNum = (String) cache().get(CachePrefix.SKU_STOCK.getPrefix() + QuantityType.enable + "_" + goodsSku.getSkuId());
            String actualNum = (String) cache().get(CachePrefix.SKU_STOCK.getPrefix() + QuantityType.actual + "_" + goodsSku.getSkuId());
            String enableGoodsNum = (String) cache().get(CachePrefix.GOODS_STOCK.getPrefix() + QuantityType.enable + "_" + goodsSku.getGoodsId());
            String actualGoodsNum = (String) cache().get(CachePrefix.GOODS_STOCK.getPrefix() + QuantityType.actual + "_" + goodsSku.getGoodsId());


            Assert.assertEquals("" + 100, enableNum);
            Assert.assertEquals("" + 100, actualNum);
            Assert.assertEquals("" + 200, enableGoodsNum);
            Assert.assertEquals("" + 200, actualGoodsNum);

            //验证sku的库存
            String sql = "select * from es_goods_sku where sku_id=?";
            int skuId = goodsSku.getSkuId();
            Map<String, Integer> map = daoSupport.queryForMap(sql, skuId);
            Integer quantity = map.get("quantity");
            Integer enableQuantity = map.get("enable_quantity");

            Assert.assertEquals(100, quantity.intValue());
            Assert.assertEquals(100, enableQuantity.intValue());

            //验证goods的库存
            sql = "select quantity,enable_quantity from es_goods where goods_id=?";
            int goodId = goodsSku.getGoodsId();
            map = daoSupport.queryForMap(sql, goodId);
            quantity = map.get("quantity");
            enableQuantity = map.get("enable_quantity");

            Assert.assertEquals(200, quantity.intValue());
            Assert.assertEquals(200, enableQuantity.intValue());


        });
    }

    /**
     * 测试扣减库存
     */
    @Test
    public void testCut() {
        this.add();
        quantityList = new ArrayList<>();
        skuList.forEach(goodsSku -> {


            //1-1可用库存减1
            if (goodsSku.getSn().equals("1-1")) {
                GoodsQuantityVO enableQuantity = new GoodsQuantityVO();
                enableQuantity.setQuantity(-1);
                enableQuantity.setQuantityType(QuantityType.enable);
                enableQuantity.setGoodsId(goodsSku.getGoodsId());
                enableQuantity.setSkuId(goodsSku.getSkuId());
                quantityList.add(enableQuantity);
            }


            if (goodsSku.getSn().equals("1-2")) {
                GoodsQuantityVO enableQuantity = new GoodsQuantityVO();
                enableQuantity.setQuantity(-1);
                enableQuantity.setQuantityType(QuantityType.enable);
                enableQuantity.setGoodsId(goodsSku.getGoodsId());
                enableQuantity.setSkuId(goodsSku.getSkuId());
                quantityList.add(enableQuantity);
            }


            //1-2的可用库存减2，实际库存减1
            if (goodsSku.getSn().equals("2-1")) {
                GoodsQuantityVO enableQuantity = new GoodsQuantityVO();
                enableQuantity.setQuantity(-2);
                enableQuantity.setQuantityType(QuantityType.enable);
                enableQuantity.setGoodsId(goodsSku.getGoodsId());
                enableQuantity.setSkuId(goodsSku.getSkuId());
                quantityList.add(enableQuantity);

                GoodsQuantityVO actualQuantity = new GoodsQuantityVO();
                actualQuantity.setQuantity(-1);
                actualQuantity.setQuantityType(QuantityType.actual);
                actualQuantity.setGoodsId(goodsSku.getGoodsId());
                actualQuantity.setSkuId(goodsSku.getSkuId());
                quantityList.add(actualQuantity);

            }


        });
        goodsQuantityManager.updateSkuQuantity(quantityList);

        //没有达到同步数据库的条件，数据库都没有变化
        skuList.forEach(goodsSku -> {

            //验证sku的库存
            String sql = "select * from es_goods_sku where sku_id=?";
            int skuId = goodsSku.getSkuId();
            Map<String, Integer> map = daoSupport.queryForMap(sql, skuId);
            Integer quantity = map.get("quantity");
            Integer enableQuantity = map.get("enable_quantity");

            Assert.assertEquals(100, quantity.intValue());
            Assert.assertEquals(100, enableQuantity.intValue());

            //验证goods的库存
            sql = "select quantity,enable_quantity from es_goods where goods_id=?";
            int goodId = goodsSku.getGoodsId();
            map = daoSupport.queryForMap(sql, goodId);
            quantity = map.get("quantity");
            enableQuantity = map.get("enable_quantity");

            Assert.assertEquals(200, quantity.intValue());
            Assert.assertEquals(200, enableQuantity.intValue());


        });

        //同步数据库
        goodsQuantityManager.syncDataBase();
        //验证数据库及缓存全部成功
        skuList.forEach(goodsSku -> {

            String enableNum = (String) cache().get(CachePrefix.SKU_STOCK.getPrefix() + QuantityType.enable + "_" + goodsSku.getSkuId());
            String actualNum = (String) cache().get(CachePrefix.SKU_STOCK.getPrefix() + QuantityType.actual + "_" + goodsSku.getSkuId());
            String enableGoodsNum = (String) cache().get(CachePrefix.GOODS_STOCK.getPrefix() + QuantityType.enable + "_" + goodsSku.getGoodsId());
            String actualGoodsNum = (String) cache().get(CachePrefix.GOODS_STOCK.getPrefix() + QuantityType.actual + "_" + goodsSku.getGoodsId());


            if (goodsSku.getSn().equals("1-1")) {
                Assert.assertEquals("" + 99, enableNum);
                Assert.assertEquals("" + 100, actualNum);

                //商品可用库存减了2
                Assert.assertEquals("" + 198, enableGoodsNum);

                //商品实际库存没有动
                Assert.assertEquals("" + 200, actualGoodsNum);
            }

            if (goodsSku.getSn().equals("1-2")) {
                Assert.assertEquals("" + 99, enableNum);
                Assert.assertEquals("" + 100, actualNum);
            }

            if (goodsSku.getSn().equals("2-1")) {
                Assert.assertEquals("" + 98, enableNum);
                Assert.assertEquals("" + 99, actualNum);

                Assert.assertEquals("" + 198, enableGoodsNum);
                Assert.assertEquals("" + 199, actualGoodsNum);
            }


            //2-2库存无变化
            if (goodsSku.getSn().equals("2-2")) {
                Assert.assertEquals("" + 100, enableNum);
                Assert.assertEquals("" + 100, actualNum);

            }


            //验证数据库的库存
            String sql = "select quantity,enable_quantity from es_goods_sku where sku_id=?";
            int skuId = goodsSku.getSkuId();
            Map<String, Integer> map = daoSupport.queryForMap(sql, skuId);

            int quantity = map.get("quantity");
            int enableQuantity = map.get("enable_quantity");


            //验证goods的库存
            sql = "select quantity,enable_quantity from es_goods where goods_id=?";
            int goodId = goodsSku.getGoodsId();
            map = daoSupport.queryForMap(sql, goodId);
            int goodsQuantity = map.get("quantity");
            int goodsEnableQuantity = map.get("enable_quantity");


            if (goodsSku.getSn().equals("1-1")) {
                Assert.assertEquals(99, enableQuantity);
                Assert.assertEquals(100, quantity);

                //商品可用库存减了2
                Assert.assertEquals(198, goodsEnableQuantity);

                //商品实际库存没有动
                Assert.assertEquals(200, goodsQuantity);
            }

            if (goodsSku.getSn().equals("1-2")) {
                Assert.assertEquals(99, enableQuantity);
                Assert.assertEquals(100, quantity);
            }

            if (goodsSku.getSn().equals("2-1")) {
                Assert.assertEquals(98, enableQuantity);
                Assert.assertEquals(99, quantity);

                Assert.assertEquals(198, goodsEnableQuantity);
                Assert.assertEquals(199, goodsQuantity);
            }


            //2-2库存无变化
            if (goodsSku.getSn().equals("2-2")) {
                Assert.assertEquals(100, enableQuantity);
                Assert.assertEquals(100, quantity);

            }


        });


        //清除数据
        clean();

    }


    /**
     * 缓冲池测试
     */
    @Test
    public void updatePoolTest() {
        add();


        //更新100次
        for (int i = 0; i < 100; i++) {
            quantityList = new ArrayList<>();
            skuList.forEach(goodsSku -> {

                //1-1可用库存减1
                if (goodsSku.getSn().equals("1-1")) {
                    GoodsQuantityVO quantity = new GoodsQuantityVO();
                    quantity.setQuantity(-1);
                    quantity.setQuantityType(QuantityType.actual);
                    quantity.setGoodsId(goodsSku.getGoodsId());
                    quantity.setSkuId(goodsSku.getSkuId());
                    quantityList.add(quantity);
                }

            });

            goodsQuantityManager.updateSkuQuantity(quantityList);

        }


        skuList.forEach(goodsSku -> {

            String enableNum = (String) cache().get(CachePrefix.SKU_STOCK.getPrefix() + QuantityType.enable + "_" + goodsSku.getSkuId());
            String actualNum = (String) cache().get(CachePrefix.SKU_STOCK.getPrefix() + QuantityType.actual + "_" + goodsSku.getSkuId());
            String enableGoodsNum = (String) cache().get(CachePrefix.GOODS_STOCK.getPrefix() + QuantityType.enable + "_" + goodsSku.getGoodsId());
            String actualGoodsNum = (String) cache().get(CachePrefix.GOODS_STOCK.getPrefix() + QuantityType.actual + "_" + goodsSku.getGoodsId());

            if (goodsSku.getSn().equals("1-1") ) {
                Assert.assertEquals("" + 100, enableNum);
                Assert.assertEquals("" + 0, actualNum);
            } else {
                Assert.assertEquals("" + 100, enableNum);
                Assert.assertEquals("" + 100, actualNum);
            }
            if ( goodsSku.getSn().equals("1-1") || goodsSku.getSn().equals("1-2")){
                Assert.assertEquals("" + 200, enableGoodsNum);
                Assert.assertEquals("" + 100, actualGoodsNum);
            }


            //验证sku的库存
            String sql = "select * from es_goods_sku where sku_id=?";
            int skuId = goodsSku.getSkuId();
            Map<String, Integer> map = daoSupport.queryForMap(sql, skuId);
            Integer quantity = map.get("quantity");
            Integer enableQuantity = map.get("enable_quantity");

            if (goodsSku.getSn().equals("1-1")) {

                Assert.assertEquals(0, quantity.intValue());
                Assert.assertEquals(100, enableQuantity.intValue());
            } else {

                Assert.assertEquals(100, quantity.intValue());
                Assert.assertEquals(100, enableQuantity.intValue());
            }
            //验证goods的库存
            sql = "select quantity,enable_quantity from es_goods where goods_id=?";
            int goodId = goodsSku.getGoodsId();
            map = daoSupport.queryForMap(sql, goodId);
            quantity = map.get("quantity");
            enableQuantity = map.get("enable_quantity");


            if ( goodsSku.getSn().equals("1-1") || goodsSku.getSn().equals("1-2")){

                Assert.assertEquals(100, quantity.intValue());
                Assert.assertEquals(200, enableQuantity.intValue());
            }else {
                Assert.assertEquals(  200, enableQuantity.intValue());
                Assert.assertEquals( 200, quantity.intValue());
            }

        });


    clean();

}


    private void add() {
        GoodsDO goods1 = new GoodsDO();
        goods1.setGoodsName("测试商品1");
        goods1.setEnableQuantity(200);
        goods1.setQuantity(200);

        daoSupport.insert("es_goods", goods1);
        goodsId1 = daoSupport.getLastId("");

        GoodsDO goods2 = new GoodsDO();
        goods2.setGoodsName("测试商品2");
        goods2.setEnableQuantity(200);
        goods2.setQuantity(200);

        daoSupport.insert("es_goods", goods2);
        goodsId2 = daoSupport.getLastId("");


        GoodsSkuDO sku1 = new GoodsSkuDO();
        sku1.setGoodsName("测试商品1");
        sku1.setGoodsId(goodsId1);
        sku1.setEnableQuantity(100);
        sku1.setQuantity(100);
        sku1.setSn("1-1");


        GoodsSkuDO sku2 = new GoodsSkuDO();
        sku2.setGoodsName("测试商品1");
        sku2.setGoodsId(goodsId1);
        sku2.setEnableQuantity(100);
        sku2.setQuantity(100);
        sku2.setSn("1-2");


        GoodsSkuDO sku3 = new GoodsSkuDO();
        sku3.setGoodsName("测试商品2");
        sku3.setGoodsId(goodsId2);
        sku3.setEnableQuantity(100);
        sku3.setQuantity(100);
        sku3.setSn("2-1");


        GoodsSkuDO sku4 = new GoodsSkuDO();
        sku4.setGoodsName("测试商品2");
        sku4.setGoodsId(goodsId2);
        sku4.setEnableQuantity(100);
        sku4.setQuantity(100);
        sku4.setSn("2-2");

        skuList.add(sku1);
        skuList.add(sku2);
        skuList.add(sku3);
        skuList.add(sku4);


        //sku入库，同时形成库存更新列表
        skuList.forEach(goodsSku -> {

            stringRedisTemplate.delete(CachePrefix.SKU_STOCK.getPrefix() + QuantityType.enable + "_" + goodsSku.getSkuId());
            stringRedisTemplate.delete(CachePrefix.SKU_STOCK.getPrefix() + QuantityType.actual + "_" + goodsSku.getSkuId());
            stringRedisTemplate.delete(CachePrefix.GOODS_STOCK.getPrefix() + QuantityType.enable + "_" + goodsSku.getGoodsId());
            stringRedisTemplate.delete(CachePrefix.GOODS_STOCK.getPrefix() + QuantityType.actual + "_" + goodsSku.getGoodsId());
            goodsSku.setHashCode(0);
            daoSupport.insert(goodsSku);
            Integer skuId = daoSupport.getLastId("");
            goodsSku.setSkuId(skuId);
            System.out.println(goodsSku.getSn() + "->" + skuId);
            GoodsQuantityVO actualQuantity = new GoodsQuantityVO();
            actualQuantity.setQuantity(100);
            actualQuantity.setQuantityType(QuantityType.actual);
            actualQuantity.setGoodsId(goodsSku.getGoodsId());
            actualQuantity.setSkuId(goodsSku.getSkuId());

            GoodsQuantityVO enableQuantity = new GoodsQuantityVO();
            enableQuantity.setQuantity(100);
            enableQuantity.setQuantityType(QuantityType.enable);
            enableQuantity.setGoodsId(goodsSku.getGoodsId());
            enableQuantity.setSkuId(goodsSku.getSkuId());


            quantityList.add(actualQuantity);
            quantityList.add(enableQuantity);

        });


        goodsQuantityManager.updateSkuQuantity(quantityList);

        //立即同步数据库
        goodsQuantityManager.syncDataBase();


    }

    private void clean() {
        quantityList.forEach(goodsSku -> {
            stringRedisTemplate.delete(CachePrefix.SKU_STOCK.getPrefix() + QuantityType.enable + "_" + goodsSku.getSkuId());
            stringRedisTemplate.delete(CachePrefix.SKU_STOCK.getPrefix() + QuantityType.actual + "_" + goodsSku.getSkuId());
            stringRedisTemplate.delete(CachePrefix.GOODS_STOCK.getPrefix() + QuantityType.enable + "_" + goodsSku.getGoodsId());
            stringRedisTemplate.delete(CachePrefix.GOODS_STOCK.getPrefix() + QuantityType.actual + "_" + goodsSku.getGoodsId());
        });

    }
}
