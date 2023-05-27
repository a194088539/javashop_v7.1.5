package com.enation.app.javashop.core.goods.service;

import com.enation.app.javashop.core.goods.model.dos.GoodsDO;
import com.enation.app.javashop.core.goods.model.vo.GoodsSkuVO;
import com.enation.app.javashop.core.goods.model.vo.SpecValueVO;
import com.enation.app.javashop.core.goods.service.impl.util.StockCacheKeyUtil;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.IntegerMapper;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * sku管理单元测试<br/>
 * 涵盖如下情况：<br/>
 * <h2>添加</h2>
 * <li>添加带规格sku，要验证：hashcode为生成的，库存正确数据库和缓存的）</li>
 * <li>添加不带规格的sku，，要验证：hashcode为-1，库存正确数据库和缓存的）</li>
 * <h2>修改</h2>
 *
 * <li>
 *     原来就是带规格，维度不变，减少一种规格组合，并修改其它组合的价格<br/>
 *     需验证：<br/>
 *     1、所有的hashcode不变，skuid不变，减少的不在了，修改的价格变了<br/>
 *     2、库存正确（数据库和缓存的）<br/>
 * </li>
 *
 * <li>>
 *     原来就是带规格，增加一个维度<br/>
 *     需验证：<br/>
 *     1、所有的skuid变化了，hashcode变化了
 *     2、库存正确（数据库和缓存的），要注意商品的库存增加了<br/>
 * </li>
 *
 * <li>>原来就是带规格，关闭规格
 *  需验证：<br/>
 *  1、所有的skuid变化了，hashcode为-1<br/>
 *  2、库存正确（数据库和缓存的），要注意是库存缓存中原skuid应该被删除<br/>
 *  </li>
 *
 * <li>
 *     原来是不带规格，开启规格<br/>
 *     需验证：<br/>
 *     1、skuid变化了，hashcode不为-1<br/>
 *     2、库存正确（数据库和缓存的），要注意是库存缓存中原skuid应该被删除<br/>
 * </li>
 *
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2019-02-15
 */
//@Rollback(false)
public class SkuManagerTest extends BaseTest {

    @Autowired
    private GoodsSkuManager goodsSkuManager;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private DaoSupport daoSupport;

    List<SpecValueVO> specList1 = new ArrayList<>();
    List<SpecValueVO> specList2 = new ArrayList<>();
    List<GoodsSkuVO> skuList = new ArrayList<>();
    GoodsDO goods;
    GoodsSkuVO sku1;
    GoodsSkuVO sku2;


    private void mock() {

        //模拟一个商品
        goods = this.getGoods();
        Integer goodsId = goods.getGoodsId();

        //定义规格组合
        String[] specs1 = new String[]{"红", "S"};
        String[] specs2 = new String[]{"红", "M"};

        //生成spec对象
        for (String s : specs1) {
            SpecValueVO spec = new SpecValueVO();
            spec.setSpecValue(s);
            spec.setSpecType(0);
            specList1.add(spec);
        }

        for (String s : specs2) {
            SpecValueVO spec = new SpecValueVO();
            spec.setSpecValue(s);
            spec.setSpecType(0);
            specList2.add(spec);
        }


        //生成sku对象
        sku1 = new GoodsSkuVO();
        sku1.setSn("1-1");
        sku1.setSpecList(specList1);
        sku1.setQuantity(10);
        sku1.setEnableQuantity(10);
        sku1.setPrice(100D);
        sku1.setGoodsId(goodsId);

        sku2 = new GoodsSkuVO();
        sku2.setSn("1-2");
        sku2.setSpecList(specList2);
        sku2.setQuantity(10);
        sku2.setEnableQuantity(10);
        sku2.setPrice(100D);
        sku2.setGoodsId(goodsId);

        //形成sku列表
        skuList.add(sku1);
        skuList.add(sku2);


    }


    /**
     * 添加带规格sku
     */
    @Test
    public void addHaveSpec() {

        mock();

        //添加
        goodsSkuManager.add(skuList, goods);

        //查询库的sku以做断言
        List<GoodsSkuVO> dbSkuList = goodsSkuManager.listByGoodsId(goods.getGoodsId());
        dbSkuList.forEach(skuVO -> {

            int skuId = skuVO.getSkuId();


            //断言skuid不为null
            Assert.assertEquals(true, skuVO.getSkuId() != null);

            //断言hashcode不为-1
            Assert.assertEquals(true, skuVO.getHashCode() != -1);

            assertSkuStock(skuId,10);
        });

        //验证goods的库存
        String sql = "select quantity,enable_quantity from es_goods where goods_id=?";
        int goodId = goods.getGoodsId();
        Map<String,Integer>  map = daoSupport.queryForMap(sql, goodId);
       Integer quantity = map.get("quantity");
        Integer enableQuantity = map.get("enable_quantity");

        Assert.assertEquals(20, quantity.intValue());
        Assert.assertEquals(20, enableQuantity.intValue());

        //验证商品的库存
        assertGoodsStock(goods.getGoodsId(),20);

    }


    /**
     * 添加不带规格的sku
     */
    @Test
    public void testAddNoSku() {
        GoodsDO goods = getGoods();
        goodsSkuManager.add(null, goods);
        List<GoodsSkuVO> dbSkuList = goodsSkuManager.listByGoodsId(goods.getGoodsId());

        //断言只有一个sku
        Assert.assertEquals(1, dbSkuList.size());

        GoodsSkuVO dbSku = dbSkuList.get(0);
        Assert.assertEquals(true, dbSku.getHashCode() == -1);

        //记录下skuid以证明没有变动
        Integer skuid = dbSku.getSkuId().intValue();


        //将商品价格修改为90,也就是sku的价格要变为50
        goods.setPrice(90D);
        goodsSkuManager.edit(null, goods);

        dbSkuList = goodsSkuManager.listByGoodsId(goods.getGoodsId());

        //断言只有一个sku
        Assert.assertEquals(1, dbSkuList.size());

        dbSku = dbSkuList.get(0);
        Assert.assertEquals(true, dbSku.getHashCode() == -1);

        //断言skuid没有变化
        Assert.assertEquals(skuid, dbSku.getSkuId());


        //验证库存
        int skuId = dbSku.getSkuId();

        //验证缓存中的库存数
        String enableNum = stringRedisTemplate.opsForValue().get(StockCacheKeyUtil.skuEnableKey(skuId) );
        String actualNum = stringRedisTemplate.opsForValue().get(StockCacheKeyUtil.skuActualKey(skuId) );
        Assert.assertEquals(""+20, enableNum);
        Assert.assertEquals(""+20, actualNum);



        //验证sku的数据库库存
        String sql = "select * from es_goods_sku where sku_id=?";
        Map<String, Integer> map = daoSupport.queryForMap(sql, skuId);

        Integer quantity = map.get("quantity");
        Integer enableQuantity = map.get("enable_quantity");

        Assert.assertEquals(20, quantity.intValue());
        Assert.assertEquals(20, enableQuantity.intValue());


        //验证商品的库存
        assertGoodsStock(goods.getGoodsId(),20);

    }


    /**
     * 测试编辑
     */
    @Test
    public void testEdit() {

        //添加有规格的
        this.addHaveSpec();

        //测试维度没有变化的，只是新增了一种组合
        String[] specs3 = new String[]{"白", "M"};
        List<SpecValueVO> specList3 = new ArrayList<>();
        for (String s : specs3) {
            SpecValueVO spec = new SpecValueVO();
            spec.setSpecValue(s);
            spec.setSpecType(0);
            specList3.add(spec);
        }

        GoodsSkuVO sku3 = new GoodsSkuVO();
        sku3.setSn("1-3");
        sku3.setSpecList(specList3);
        sku3.setQuantity(10);
        sku3.setEnableQuantity(10);
        sku3.setPrice(100D);
        sku3.setGoodsId(goods.getGoodsId());

        skuList.add(sku3);

        //备份skuid及hashcode 以便断言使用
        Integer skuid1 = sku1.getSkuId().intValue();
        Integer skuid2 = sku2.getSkuId().intValue();
        Integer hashcode1 = sku1.getHashCode().intValue();
        Integer hashcode2 = sku2.getHashCode().intValue();

        //编辑
        goodsSkuManager.edit(skuList, goods);


        //查询库的sku以做断言
        List<GoodsSkuVO> dbSkuList = goodsSkuManager.listByGoodsId(goods.getGoodsId());

        //断言新增了一个skuk
        Assert.assertEquals(3, dbSkuList.size());

        dbSkuList.forEach(skuVO -> {

            if (skuVO.getSn().equals("1-1")) {
                //断言skuid 没有变化
                Assert.assertEquals(skuid1, skuVO.getSkuId());

                //断言hashcode没有变化
                Assert.assertEquals(hashcode1, skuVO.getHashCode());
            }

            if (skuVO.getSn().equals("1-2")) {
                //断言skuid 没有变化
                Assert.assertEquals(skuid2, skuVO.getSkuId());

                //断言hashcode没有变化
                Assert.assertEquals(hashcode2, skuVO.getHashCode());
            }

            //所有sku的库存为10
            assertSkuStock(skuVO.getSkuId(), 10);

        });

        //商品的库存就该是30
        assertGoodsStock(goods.getGoodsId(),30);


        //测试维度变化，新增一个维度，那么skuid会改变
        //新增一个性别维度
        SpecValueVO spec = new SpecValueVO();
        spec.setSpecValue("男");
        spec.setSpecType(0);
        specList1.add(spec);

        //将sku1的维度变化
        skuList.forEach(skuVO -> {
            if (sku1.getSn().equals("1-1")) {
                sku1.setSpecList(specList1);
                sku1.setSkuId(null);

            }
        });

        Integer skuid3 = sku3.getSkuId().intValue();
        Integer hashcode3 = sku3.getHashCode().intValue();

        System.out.println("应该将"+ skuid1+"删除掉");
        goodsSkuManager.edit(skuList, goods);

        dbSkuList = goodsSkuManager.listByGoodsId(goods.getGoodsId());

        //断言sku的个数没有变
        Assert.assertEquals(3, dbSkuList.size());

        dbSkuList.forEach(skuVO -> {

            if (skuVO.getSn().equals("1-1")) {
                //断言skuid 有变化
                Assert.assertNotEquals(skuid1, skuVO.getSkuId());

                //断言hashcode有变化
                Assert.assertNotEquals(hashcode1, skuVO.getHashCode());


            }

            if (skuVO.getSn().equals("1-2")) {
                //断言skuid 没有变化
                Assert.assertEquals(skuid2, skuVO.getSkuId());
                //断言hashcode没有变化
                Assert.assertEquals(hashcode2, skuVO.getHashCode());
            }

            if (skuVO.getSn().equals("1-3")) {
                //断言skuid 没有变化
                Assert.assertEquals(skuid3, skuVO.getSkuId());
                //断言hashcode没有变化
                Assert.assertEquals(hashcode3, skuVO.getHashCode());
            }

            assertSkuStock(skuVO.getSkuId(), 10);
        });

        //商品库存数还是30
        assertGoodsStock(goods.getGoodsId(),30);

        //skuid1应该已经不存了
        assertNotExists(skuid1);

    }

    /**
     * 验证sku的库存在缓存中已经不存在了
     * @param skuid
     */
    private void assertNotExists(Integer skuid) {

        Assert.assertNull(stringRedisTemplate.opsForValue().get(StockCacheKeyUtil.skuActualKey(skuid)));
        Assert.assertNull(stringRedisTemplate.opsForValue().get(StockCacheKeyUtil.skuActualKey(skuid)));

    }

    /**
     * 测试由带规格变为无规格的
     */
    @Test
    public void testToNoSpec() {

        addHaveSpec();

        String sql = "select sku_id from es_goods_sku where goods_id=?";
        List<Integer> oldSkuIdList =daoSupport.queryForList(sql, new IntegerMapper(), goods.getGoodsId());

        goodsSkuManager.edit(null, goods);

        List<GoodsSkuVO> dbSkuList = goodsSkuManager.listByGoodsId(goods.getGoodsId());

        //断言只有一个sku
        Assert.assertEquals(1, dbSkuList.size());

        GoodsSkuVO dbSku = dbSkuList.get(0);
        Assert.assertEquals(true, dbSku.getHashCode() == -1);

        //验证原来的skuid 都不存在了
        oldSkuIdList.forEach(skuId->{
            assertNotExists(skuId);
        });

        //验证商品的库存是20
        assertGoodsStock(goods.getGoodsId(), 20);

    }

    @Test
    public void testToSpec() {
        testAddNoSku();
        mock();
        String sql = "select sku_id from es_goods_sku where goods_id=?";
        List<Integer> oldSkuIdList =daoSupport.queryForList(sql, new IntegerMapper(), goods.getGoodsId());

        //编辑
        goodsSkuManager.edit(skuList, goods);

        //查询库的sku以做断言
        List<GoodsSkuVO> dbSkuList = goodsSkuManager.listByGoodsId(goods.getGoodsId());

        //断言新有两个sku
        Assert.assertEquals(2, dbSkuList.size());


        dbSkuList.forEach(skuVO -> {
            //断言hashcode 为-1
            Assert.assertNotEquals(-1, skuVO.getHashCode().intValue());
            //验证sku的库存
            assertSkuStock(skuVO.getSkuId(), 10);
        });

        //验证原来的skuid 都不存在了
        oldSkuIdList.forEach(skuId->{
            assertNotExists(skuId);
        });

        //验证商品的库存是20
        assertGoodsStock(goods.getGoodsId(), 20);



    }

    private GoodsDO getGoods() {
        GoodsDO goods = new GoodsDO();
        goods.setThumbnail("/xx/xx.jpg");
        goods.setGoodsName("测试商品");
        goods.setSellerId(1);
        goods.setSellerName("amz");
        goods.setQuantity(20);
        goods.setEnableQuantity(20);
        goods.setPrice(100D);
        daoSupport.insert(goods);
        Integer goodsId  = daoSupport.getLastId("");
        goods.setGoodsId(goodsId);

        return goods;
    }



    /**
     * 验证sku的库存
     * @param skuId sku id
     * @param quantity 要验证的sku库存
     */
    private void assertSkuStock(Integer skuId,int quantity) {

        //验证缓存中的库存数
        String enableNum = stringRedisTemplate.opsForValue().get(StockCacheKeyUtil.skuEnableKey(skuId) );
        String actualNum = stringRedisTemplate.opsForValue().get(StockCacheKeyUtil.skuActualKey(skuId) );
        Assert.assertEquals(""+quantity, enableNum);
        Assert.assertEquals(""+quantity, actualNum);



        //验证sku的数据库库存
        String sql = "select * from es_goods_sku where sku_id=?";
        Map<String, Integer> map = daoSupport.queryForMap(sql, skuId);

        Integer actaulQuantity = map.get("quantity");
        Integer enableQuantity = map.get("enable_quantity");

        Assert.assertEquals(quantity, actaulQuantity.intValue());
        Assert.assertEquals(quantity, enableQuantity.intValue());
    }


    /**
     * 验证商品库存
     * @param goodsId 商品id
     * @param num 要验证的商品库存
     */
    private void assertGoodsStock(int goodsId ,int num) {

        //验证goods的库存
        String sql = "select quantity,enable_quantity from es_goods where goods_id=?";
        Map<String,Integer>  map = daoSupport.queryForMap(sql, goodsId);
        Integer quantity = map.get("quantity");
        Integer enableQuantity = map.get("enable_quantity");

        Assert.assertEquals(num, quantity.intValue());
        Assert.assertEquals(num, enableQuantity.intValue());

    }


}
