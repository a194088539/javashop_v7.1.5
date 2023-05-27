package com.enation.app.javashop.core.goods.service.impl;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.client.member.ShipTemplateClient;
import com.enation.app.javashop.core.client.member.ShopCatClient;
import com.enation.app.javashop.core.client.member.ShopClient;
import com.enation.app.javashop.core.client.trade.ExchangeGoodsClient;
import com.enation.app.javashop.core.client.trade.PromotionGoodsClient;
import com.enation.app.javashop.core.goods.GoodsErrorCode;
import com.enation.app.javashop.core.goods.model.dos.CategoryDO;
import com.enation.app.javashop.core.goods.model.dos.GoodsDO;
import com.enation.app.javashop.core.goods.model.dos.GoodsGalleryDO;
import com.enation.app.javashop.core.goods.model.dto.GoodsQueryParam;
import com.enation.app.javashop.core.goods.model.enums.GoodsType;
import com.enation.app.javashop.core.goods.model.vo.*;
import com.enation.app.javashop.core.goods.service.*;
import com.enation.app.javashop.core.goods.service.impl.util.SearchUtil;
import com.enation.app.javashop.core.goods.service.impl.util.StockCacheKeyUtil;
import com.enation.app.javashop.core.promotion.exchange.model.dos.ExchangeDO;
import com.enation.app.javashop.core.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.javashop.core.promotion.tool.model.vo.PromotionVO;
import com.enation.app.javashop.core.promotion.tool.service.PromotionGoodsManager;
import com.enation.app.javashop.core.shop.model.vo.ShipTemplateChildBuyerVO;
import com.enation.app.javashop.core.shop.model.vo.ShipTemplateVO;
import com.enation.app.javashop.core.shop.model.vo.ShopVO;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Seller;
import com.enation.app.javashop.framework.util.SqlUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品业务类
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018-03-21 11:23:10
 */
@Service
public class GoodsQueryManagerImpl implements GoodsQueryManager {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    @Qualifier("goodsDaoSupport")
    private DaoSupport daoSupport;
    @Autowired
    private GoodsGalleryManager goodsGalleryManager;
    @Autowired
    private GoodsSkuManager goodsSkuManager;
    @Autowired
    private ShopClient shopClient;
    @Autowired
    private Cache cache;
    @Autowired
    private ShopCatClient shopCatClient;
    @Autowired
    private CategoryManager categoryManager;
    @Autowired
    private ExchangeGoodsClient exchangeGoodsClient;
    @Autowired
    private ShipTemplateClient shipTemplateClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private GoodsQuantityManager goodsQuantityManager;

    @Autowired
    private PromotionGoodsClient promotionGoodsClient;

    @Override
    public Page list(GoodsQueryParam goodsQueryParam) {

        StringBuffer sqlBuffer = new StringBuffer(
                "select g.goods_id,g.goods_name,g.sn,g.brand_id,g.thumbnail,g.seller_name,g.enable_quantity,g.quantity,g.price,g.create_time,g.market_enable,g.is_auth,g.under_message,g.priority "
                        + "from es_goods g   ");

        List<Object> term = new ArrayList<>();

        //基础查询
        SearchUtil.baseQuery(goodsQueryParam, term, sqlBuffer);
        //分类查询
        SearchUtil.categoryQuery(goodsQueryParam, term, sqlBuffer, daoSupport);

        // 店铺分组
        SearchUtil.shopCatQuery(goodsQueryParam, term, sqlBuffer, shopCatClient);


        sqlBuffer.append(" order by g.priority desc,g.create_time desc");
        Page page = this.daoSupport.queryForPage(sqlBuffer.toString(), goodsQueryParam.getPageNo(),
                goodsQueryParam.getPageSize(), term.toArray());

        return page;
    }

    @Override
    public Page goodsPriorityList(GoodsQueryParam goodsQueryParam) {
        StringBuffer sqlBuffer = new StringBuffer(
                "select g.goods_id,g.goods_name,g.thumbnail,g.market_enable,g.is_auth,g.priority from es_goods g   ");

        List<Object> term = new ArrayList<>();

        //基础查询
        SearchUtil.baseQuery(goodsQueryParam, term, sqlBuffer);
        //分类查询
        SearchUtil.categoryQuery(goodsQueryParam, term, sqlBuffer, daoSupport);

        // 店铺分组
        SearchUtil.shopCatQuery(goodsQueryParam, term, sqlBuffer, shopCatClient);


        sqlBuffer.append(" order by g.priority desc");
        Page<GoodsPriorityVO> page = this.daoSupport.queryForPage(sqlBuffer.toString(), goodsQueryParam.getPageNo(),
                goodsQueryParam.getPageSize(), GoodsPriorityVO.class, term.toArray());

        page.getData().forEach(priorityVO -> {
            switch (priorityVO.getPriority()) {
                case 3:
                    priorityVO.setPriorityText("高");
                    break;
                case 2:
                    priorityVO.setPriorityText("中");
                    break;
                case 1:
                    priorityVO.setPriorityText("低");
                    break;
                default:
                    priorityVO.setPriorityText("低");
                    break;
            }
        });

        return page;
    }

    @Override
    public GoodsDO getModel(Integer goodsId) {

        return this.daoSupport.queryForObject(GoodsDO.class, goodsId);
    }

    @Override
    public GoodsVO sellerQueryGoods(Integer goodsId) {

        Seller seller = UserContext.getSeller();

        GoodsDO goods = this.getModel(goodsId);

        if (goods == null || !goods.getSellerId().equals(seller.getSellerId())) {
            throw new ServiceException(GoodsErrorCode.E301.code(), "没有操作权限");
        }
        List<GoodsGalleryDO> galleryList = goodsGalleryManager.list(goodsId);
        GoodsVO goodsVO = new GoodsVO();

        BeanUtils.copyProperties(goods, goodsVO);

        goodsVO.setGoodsGalleryList(galleryList);

        //商品分类赋值
        Integer categoryId = goods.getCategoryId();
        CategoryDO category = categoryManager.getModel(categoryId);
        String sql = "select name,category_id from es_category " +
                "where category_id in (" + category.getCategoryPath().replace("|", ",") + "-1) " +
                "order by category_id asc";
        List<Map> list = this.daoSupport.queryForList(sql);
        String categoryName = "";
        Integer[] categoryIds = new Integer[3];
        int i = 0;
        if (StringUtil.isNotEmpty(list)) {
            for (Map map : list) {
                if ("".equals(categoryName)) {
                    categoryName = " " + map.get("name").toString();
                } else {
                    categoryName += ">" + map.get("name").toString() + " ";
                }
                categoryIds[i] = StringUtil.toInt(map.get("category_id"), false);
                i++;
            }
        }
        goodsVO.setCategoryIds(categoryIds);
        goodsVO.setCategoryName(categoryName);

        //查询积分商品信息
        if (goodsVO.getGoodsType().equals(GoodsType.POINT.name())) {
            ExchangeDO exchangeDO = exchangeGoodsClient.getModelByGoods(goodsId);

            goodsVO.setExchange(exchangeDO);
        }

        //设置商品参与的促销活动提示
        String promotionTips = this.convertTips(goodsId);
        goodsVO.setPromotionTip(promotionTips);


        return goodsVO;
    }


    @Override
    public CacheGoods getFromCache(Integer goodsId) {
        CacheGoods goods = (CacheGoods) cache.get(CachePrefix.GOODS.getPrefix() + goodsId);
        if (logger.isDebugEnabled()) {
            logger.debug("由缓存中读出商品：");
            logger.debug(goods);
        }
        if (goods == null) {

            GoodsDO goodsDB = this.getModel(goodsId);
            if (goodsDB == null) {
                throw new ServiceException(GoodsErrorCode.E301.code(), "该商品已被彻底删除");
            }

            // GoodsVo的对象返回,GoodsVo中的skuList是要必须填充好的
            List<GoodsSkuVO> skuList = goodsSkuManager.listByGoodsId(goodsId);


            goods = new CacheGoods();
            BeanUtils.copyProperties(goodsDB, goods);
            goods.setSkuList(skuList);

            //填充库存数据
            fillStock(goods);
            cache.put(CachePrefix.GOODS.getPrefix() + goodsId, goods);

            if (logger.isDebugEnabled()) {
                logger.debug("由缓存中读出商品为空,由数据库中返回商品：");
                logger.debug(goods);
            }
            return goods;
        } else {
            //填充库存数据
            fillStock(goods);
        }

        return goods;
    }


    /**
     * 为商品填充库存信息<br/>
     * 库存的信息存储在单独的缓存key中<br/>
     * 由缓存中读取出所有sku的库存，并分别为goods.skuList中的sku设置库存，以保证库存的时时正确性<br/>
     * 同时还会将所有的sku库存累加设置为商品的库存
     *
     * @param goods
     */
    private void fillStock(CacheGoods goods) {

        List<GoodsSkuVO> skuList = goods.getSkuList();

        //由缓存中获取sku的可用库存和实际库存
        //此操作为批量操作，因为是高频操作，要尽量减少和redis的交互次数
        List keys = createKeys(skuList);

        //将商品的可用库存和实际库存一起读
        keys.add(StockCacheKeyUtil.goodsEnableKey(goods.getGoodsId()));
        keys.add(StockCacheKeyUtil.goodsActualKey(goods.getGoodsId()));

        List<String> quantityList = stringRedisTemplate.opsForValue().multiGet(keys);

        int enableTotal = 0;
        int actualTotal = 0;

        int i = 0;
        for (GoodsSkuVO skuVO : skuList) {

            //第一个是可用库存
            Integer enable = StringUtil.toInt(quantityList.get(i), null);

            i++;
            //第二个是真实库存
            Integer actual = StringUtil.toInt(quantityList.get(i), null);

            //缓存被击穿，由数据库中读取
            if (enable == null || actual == null) {
                Map<String, Integer> map = goodsQuantityManager.fillCacheFromDB(skuVO.getSkuId());
                enable = map.get("enable_quantity");
                actual = map.get("quantity");

                //重置缓存中的库存
                stringRedisTemplate.opsForValue().set(StockCacheKeyUtil.skuEnableKey(skuVO.getSkuId()), "" + enable);
                stringRedisTemplate.opsForValue().set(StockCacheKeyUtil.skuActualKey(skuVO.getSkuId()), "" + actual);
            }


            skuVO.setEnableQuantity(enable);
            skuVO.setQuantity(actual);

            if (enable == null) {
                enable = 0;
            }

            if (actual == null) {
                actual = 0;
            }
            //累计商品的库存
            enableTotal += enable;
            actualTotal += actual;

            i++;
        }


        //设置商品的库存
        goods.setEnableQuantity(enableTotal);
        goods.setQuantity(actualTotal);


        //读取缓存中商品的库存，看是否被击穿了
        //第一个是可用库存
        Integer goodsEnable = StringUtil.toInt(quantityList.get(i), null);

        i++;
        //第二个是真实库存
        Integer goodsActual = StringUtil.toInt(quantityList.get(i), null);

        //商品的库存被击穿了
        if (goodsEnable == null || goodsActual == null) {
            //重置缓存中的库存
            stringRedisTemplate.opsForValue().set(StockCacheKeyUtil.goodsEnableKey(goods.getGoodsId()), "" + enableTotal);
            stringRedisTemplate.opsForValue().set(StockCacheKeyUtil.goodsActualKey(goods.getGoodsId()), "" + actualTotal);
        }


    }

    /**
     * 生成批量获取sku库存的keys
     *
     * @param goodsList
     * @return
     */
    private List createKeys(List<GoodsSkuVO> goodsList) {
        List keys = new ArrayList();
        for (GoodsSkuVO goodsSkuVO : goodsList) {

            keys.add(StockCacheKeyUtil.skuEnableKey(goodsSkuVO.getSkuId()));
            keys.add(StockCacheKeyUtil.skuActualKey(goodsSkuVO.getSkuId()));
        }
        return keys;
    }

    @Override
    public Page warningGoodsList(int pageNo, int pageSize, String keyword) {

        Seller seller = UserContext.getSeller();

        StringBuffer sqlBuffer = new StringBuffer("select g.* from es_goods g where g.goods_id in(" +
                " select s.goods_id from es_goods_sku s WHERE s.enable_quantity <= ? and s.seller_id = ?) and g.market_enable = 1 AND g.disabled = 1");

        List<Object> term = new ArrayList<>();
        ShopVO shop = shopClient.getShop(seller.getSellerId());
        if (shop == null) {
            throw new ServiceException(GoodsErrorCode.E301.code(), "店铺不存在");
        }
        term.add(shop.getGoodsWarningCount() == null ? 5 : shop.getGoodsWarningCount());
        term.add(seller.getSellerId());

        if (!StringUtil.isEmpty(keyword)) {
            sqlBuffer.append(" and (g.goods_name like ? or g.sn like ?)");
            term.add("%" + keyword + "%");
            term.add("%" + keyword + "%");
        }

        return this.daoSupport.queryForPage(sqlBuffer.toString(), pageNo, pageSize, term.toArray());
    }

    @Override
    public List<GoodsSelectLine> query(Integer[] goodsIds, Integer sellerId) {

        List<Object> term = new ArrayList<>();
        String str = SqlUtil.getInSql(goodsIds, term);
        if (goodsIds == null || goodsIds.length == 0) {
            return new ArrayList<>();
        }
        String sql = "select * from es_goods where goods_id in (" + str + ")";
        if (sellerId != null) {
            sql += " and seller_id = ? ";
            term.add(sellerId);
        }

        return this.daoSupport.queryForList(sql, GoodsSelectLine.class, term.toArray());
    }

    @Override
    public List<GoodsDO> queryDo(Integer[] goodsIds) {

        List<Object> term = new ArrayList<>();
        String str = SqlUtil.getInSql(goodsIds, term);
        if (goodsIds == null || goodsIds.length == 0) {
            return new ArrayList<>();
        }
        String sql = "select * from es_goods where goods_id in (" + str + ")";

        return this.daoSupport.queryForList(sql, GoodsDO.class, term.toArray());
    }

    @Override
    public Double getGoodsGrade(Integer goodsId) {

        Double grade = (Double) cache.get(CachePrefix.GOODS_GRADE.getPrefix() + goodsId);
        if (grade == null) {
            String sql = "select grade from es_goods where goods_id = ?";
            grade = this.daoSupport.queryForDouble(sql, goodsId);
            cache.put(CachePrefix.GOODS_GRADE.getPrefix() + goodsId, grade);
        }
        return grade;
    }

    @Override
    public void checkSellerGoodsCount(Integer[] goodsIds) {
        Seller seller = UserContext.getSeller();
        List<Object> term = new ArrayList<>();
        String idStr = SqlUtil.getInSql(goodsIds, term);

        String sql = "select count(1) from es_goods where goods_id in (" + idStr + ") and seller_id = ?";
        term.add(seller.getSellerId());
        Integer count = this.daoSupport.queryForInt(sql, term.toArray());
        if (count != goodsIds.length) {
            throw new ServiceException(GoodsErrorCode.E301.code(), "存在不属于您的商品，不能操作");
        }
    }

    @Override
    public List<Map<String, Object>> getGoodsAndParams(Integer[] goodsIds) {

        if (goodsIds == null) {
            return null;
        }

        StringBuffer sqlBuffer = new StringBuffer("select g.* from es_goods g ");

        List<Object> term = new ArrayList<>();

        String str = SqlUtil.getInSql(goodsIds, term);
        sqlBuffer.append(" where goods_id in (" + str + ")  order by goods_id desc");

        List<Map<String, Object>> list = this.daoSupport.queryForList(sqlBuffer.toString(), term.toArray());
        //查询该商品关联的可检索的参数集合
        this.getIndexGoodsList(list);
        return list;
    }

    @Override
    public List<Map<String, Object>> getGoodsAndParams(Integer sellerId) {
        String sql = "select * from es_goods where seller_id = ? and market_enable = 1";
        List<Map<String, Object>> list = this.daoSupport.queryForList(sql, sellerId);
        //查询该商品关联的可检索的参数集合
        this.getIndexGoodsList(list);
        return list;
    }

    /**
     * 查询该商品关联的可检索的参数集合
     *
     * @param list 原始商品数据
     * @return
     */
    private List<Map<String, Object>> getIndexGoodsList(List<Map<String, Object>> list) {
        if (list != null) {
            for (Map<String, Object> map : list) {
                // 查询该商品关联的可检索的参数集合
                String sql = "select gp.* from es_goods_params gp inner join es_parameters p on gp.param_id=p.param_id "
                        + "where goods_id = ? and is_index = 1";
                List listParams = this.daoSupport.queryForList(sql,
                        map.get("goods_id"));
                map.put("params", listParams);
            }
        }
        return list;
    }

    @Override
    public List<GoodsDO> listGoods(Integer sellerId) {
        String sql = "select * from es_goods where seller_id = ? and market_enable = 1 and disabled = 1 and is_auth = 1 order by buy_count desc limit 5 ";
        return this.daoSupport.queryForList(sql, GoodsDO.class, sellerId);
    }

    /**
     * 查询购买量
     *
     * @param goodsIds
     * @return
     */
    @Override
    public List<BuyCountVO> queryBuyCount(Integer[] goodsIds) {
        List<Object> term = new ArrayList<>();
        String str = SqlUtil.getInSql(goodsIds, term);
        return this.daoSupport.queryForList("select buy_count,goods_id from es_goods where goods_id in (" + str + ")", BuyCountVO.class, term.toArray());
    }

    /**
     * 获取商品分类路径
     *
     * @param categoryId
     * @return
     */
    @Override
    public String queryCategoryPath(Integer categoryId) {
        CategoryDO category = categoryManager.getModel(categoryId);
        String sql = "select name from es_category " +
                "where category_id in (" + category.getCategoryPath().replace("|", ",") + "-1) " +
                "order by category_id asc";
        List<Map> list = this.daoSupport.queryForList(sql);
        String categoryName = "";
        if (StringUtil.isNotEmpty(list)) {
            for (Map map : list) {
                if ("".equals(categoryName)) {
                    categoryName = " " + map.get("name").toString();
                } else {
                    categoryName += ">" + map.get("name").toString() + " ";
                }
            }
        }
        return categoryName;
    }

    @Override
    public Integer getSellerGoodsCount(Integer sellerId) {

        String sql = "select count(*) from es_goods where seller_id=? and market_enable=1 and is_auth =1 and disabled=1 ";
        return this.daoSupport.queryForInt(sql, sellerId);
    }

    @Override
    public Integer checkArea(Integer goodsId, Integer areaId) {

        CacheGoods goods = this.getFromCache(goodsId);
        //卖家承担运费
        if (goods.getGoodsTransfeeCharge() == 1) {
            //有货
            return 1;
        }


        ShipTemplateVO temp = this.shipTemplateClient.get(goods.getTemplateId());

        for (ShipTemplateChildBuyerVO child : temp.getItems()) {
            if (child.getAreaId() != null) {
                if (child.getAreaId().indexOf("," + areaId + ",") >= 0) {
                    //有货
                    return 1;
                }
            }
        }

        //无货
        return 0;
    }

    @Override
    public Integer getGoodsCountByParam(Integer status, Integer sellerId, Integer disabled) {
        StringBuffer sql = new StringBuffer("select count(0) from es_goods");
        List paramList = new ArrayList();
        List<String> sqlList = new ArrayList<>();
        //商品状态查询
        if (status != null) {
            sqlList.add(" market_enable = ?");
            paramList.add(status.toString());
        }
        //商家id查询
        if (sellerId != null) {
            sqlList.add(" seller_id = ?");
            paramList.add(sellerId.toString());
        }

        //商品删除状态查询
        if (disabled != null) {
            sqlList.add(" disabled = ?");
            paramList.add(disabled.toString());
        }

        sql.append(SqlUtil.sqlSplicing(sqlList));


        Integer num = this.daoSupport.queryForInt(sql.toString(), paramList.toArray());
        return num;
    }

    @Override
    public List<Map<String, Object>> getGoods(Integer[] goodsIds) {
        List<Integer> term = new ArrayList<>();
        String idStr = StringUtil.getIdStr(goodsIds, term);
        String sql = "select goods_id,goods_name,price as original_price from es_goods where goods_id in (" + idStr + ")";
        List<Map<String, Object>> result = this.daoSupport.queryForList(sql, term.toArray());
        return result;
    }


    @Override
    public List<GoodsDO> getSellerGoods(Integer sellerId) {
        String sql = "select * from es_goods where seller_id = ? and disabled != -1";
        return daoSupport.queryForList(sql, GoodsDO.class, sellerId);
    }

    /**
     * 获取商品参与的促销活动
     *
     * @param goodsId
     * @return
     */
    private String convertTips(Integer goodsId) {
        List<PromotionVO> promotionVOS = promotionGoodsClient.getPromotion(goodsId);
        if (promotionVOS == null || promotionVOS.isEmpty()) {
            return "";
        }
        List<String> tips = new ArrayList<>();
        promotionVOS.forEach(promotionVO -> {
            tips.add(PromotionTypeEnum.valueOf(promotionVO.getPromotionType()).getPromotionName());
        });
        StringBuffer result = new StringBuffer("此商品参与的[");
        result.append(StringUtil.listToString(tips, ","));
        result.append("]促销活动,修改上架后将会被取消");
        return result.toString();
    }
}
