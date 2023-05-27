package com.enation.app.javashop.core.client.goods.impl;

import com.enation.app.javashop.core.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.core.base.message.GoodsChangeMsg;
import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.client.trade.ExchangeGoodsClient;
import com.enation.app.javashop.core.goods.model.dos.BrandDO;
import com.enation.app.javashop.core.goods.model.dos.CategoryDO;
import com.enation.app.javashop.core.goods.model.dos.GoodsDO;
import com.enation.app.javashop.core.goods.model.dos.GoodsGalleryDO;
import com.enation.app.javashop.core.goods.model.dto.GoodsQueryParam;
import com.enation.app.javashop.core.goods.model.enums.GoodsType;
import com.enation.app.javashop.core.goods.model.vo.*;
import com.enation.app.javashop.core.goods.service.*;
import com.enation.app.javashop.core.promotion.exchange.model.dos.ExchangeDO;
import com.enation.app.javashop.core.trade.order.model.vo.OrderSkuVO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author fk
 * @version v2.0
 * @Description: 商品对外的接口实现
 * @date 2018/7/26 10:43
 * @since v7.0.0
 */
@Service
@ConditionalOnProperty(value = "javashop.product", havingValue = "stand")
public class GoodsClientDefaultImpl implements GoodsClient {

    @Autowired
    private GoodsManager goodsManager;
    @Autowired
    private GoodsQueryManager goodsQueryManager;
    @Autowired
    @Qualifier("goodsDaoSupport")
    private DaoSupport daoSupport;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private GoodsSkuManager goodsSkuManager;

    @Autowired
    private GoodsParamsManager goodsParamsManager;
    @Autowired
    private CategoryManager categoryManager;
    @Autowired
    private BrandManager brandManager;
    @Autowired
    private GoodsGalleryManager goodsGalleryManager;
    @Autowired
    private ExchangeGoodsClient exchangeGoodsClient;

    @Override
    public List<BackendGoodsVO> newGoods(Integer length) {

        return this.goodsManager.newGoods(length);
    }

    @Override
    public void underShopGoods(Integer sellerId) {

        this.goodsManager.underShopGoods(sellerId);
    }

    @Override
    public void updateGoodsGrade() {

        this.goodsManager.updateGoodsGrade();
    }

    @Override
    public CacheGoods getFromCache(Integer goodsId) {

        return this.goodsQueryManager.getFromCache(goodsId);
    }

    @Override
    public List<GoodsSelectLine> query(Integer[] goodsIds) {

        return this.goodsQueryManager.query(goodsIds, null);
    }

    @Override
    public List<GoodsDO> queryDo(Integer[] goodsIds) {

        return this.goodsQueryManager.queryDo(goodsIds);
    }

    @Override
    public void checkSellerGoodsCount(Integer[] goodsIds) {

        this.goodsQueryManager.checkSellerGoodsCount(goodsIds);
    }

    @Override
    public List<Map<String, Object>> getGoodsAndParams(Integer[] goodsIds) {

        return this.goodsQueryManager.getGoodsAndParams(goodsIds);
    }

    @Override
    public List<Map<String, Object>> getGoodsAndParams(Integer sellerId) {
        return this.goodsQueryManager.getGoodsAndParams(sellerId);
    }

    @Override
    public List<GoodsDO> listGoods(Integer sellerId) {

        return this.goodsQueryManager.listGoods(sellerId);
    }

    @Override
    public GoodsSkuVO getSkuFromCache(Integer skuId) {

        return this.goodsSkuManager.getSkuFromCache(skuId);
    }

    @Override
    public GoodsSkuVO getSku(Integer skuId) {
        return this.goodsSkuManager.getSku(skuId);
    }

    @Override
    public List<Map<String, Object>> getGoods(Integer[] goodsIds) {
        return goodsQueryManager.getGoods(goodsIds);
    }

    @Override
    public void updateCommentCount(Integer goodsId, Integer num) {
        String updateSql = "update es_goods set comment_num=comment_num + ? where goods_id=?";
        this.daoSupport.execute(updateSql, num, goodsId);
        // 发送商品消息变化消息
        GoodsChangeMsg goodsChangeMsg = new GoodsChangeMsg(new Integer[]{goodsId},
                GoodsChangeMsg.UPDATE_OPERATION);
        this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_CHANGE, AmqpExchange.GOODS_CHANGE + "_ROUTING", goodsChangeMsg);

    }

    @Override
    public void updateBuyCount(List<OrderSkuVO> list) {
        Set<Integer> set = new HashSet<>();
        for (OrderSkuVO sku : list) {
            String sql = "update es_goods set buy_count=buy_count+? where goods_id=?";
            this.daoSupport.execute(sql, sku.getNum(), sku.getGoodsId());
            set.add(sku.getGoodsId());
        }
        // 发送修改商品消息
        GoodsChangeMsg goodsChangeMsg = new GoodsChangeMsg(set.toArray(new Integer[set.size()]),
                GoodsChangeMsg.UPDATE_OPERATION);
        this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_CHANGE, AmqpExchange.GOODS_CHANGE + "_ROUTING", goodsChangeMsg);
    }

    @Override
    public Integer queryGoodsCount() {
        return this.goodsQueryManager.getGoodsCountByParam(null, null, null);
    }

    @Override
    public Integer queryGoodsCountByParam(Integer status, Integer sellerId) {
        return this.goodsQueryManager.getGoodsCountByParam(status, sellerId, 1);
    }


    @Override
    public List<Map> queryGoodsByRange(Integer pageNo, Integer pageSize) {

        StringBuffer sqlBuffer = new StringBuffer("select g.* from es_goods  g order by goods_id desc");
        List<Map> goodsList = this.daoSupport.queryForListPage(sqlBuffer.toString(), pageNo, pageSize);
        return goodsList;
    }

    @Override
    public GoodsSnapshotVO queryGoodsSnapShotInfo(Integer goodsId) {

        //商品
        GoodsDO goods = this.goodsQueryManager.getModel(goodsId);

        //判断是否为积分商品
        if (GoodsType.POINT.name().equals(goods.getGoodsType())) {
            //积分兑换信息
            ExchangeDO exchangeDO = this.exchangeGoodsClient.getModelByGoods(goodsId);
            goods.setPoint(exchangeDO.getExchangePoint());
        }


        //参数
        List<GoodsParamsGroupVO> paramList = goodsParamsManager.queryGoodsParams(goods.getCategoryId(), goodsId);
        //品牌
        BrandDO brand = brandManager.getModel(goods.getBrandId());
        //分类
        CategoryDO category = categoryManager.getModel(goods.getCategoryId());
        //相册
        List<GoodsGalleryDO> galleryList = goodsGalleryManager.list(goodsId);

        return new GoodsSnapshotVO(goods, paramList, brand, category, galleryList);
    }

    @Override
    public Page list(GoodsQueryParam goodsQueryParam) {

        return goodsQueryManager.list(goodsQueryParam);
    }

    @Override
    public CategoryDO getCategory(Integer id) {

        return categoryManager.getModel(id);
    }

    /**
     * 校验商品模版是否使用
     *
     * @param templateId
     * @return 商品名称
     */
    @Override
    public GoodsDO checkShipTemplate(Integer templateId) {
        return goodsManager.checkShipTemplate(templateId);
    }

    @Override
    public Integer getSellerGoodsCount(Integer sellerId) {

        return goodsQueryManager.getSellerGoodsCount(sellerId);
    }

    /**
     * 修改某店铺商品店铺名称
     *
     * @param sellerId   商家id
     * @param sellerName 商家名称
     */
    @Override
    public void changeSellerName(Integer sellerId, String sellerName) {
        this.goodsManager.changeSellerName(sellerId, sellerName);
    }

    @Override
    public void updateGoodsType(Integer sellerId, String type) {
        goodsManager.updateGoodsType(sellerId, type);
    }

    @Override
    public List<GoodsDO> listPointGoods(Integer shopId) {
        String sql = "select * from es_goods where goods_type = '" + GoodsType.POINT.name() + "' and seller_id = ? ";

        return this.daoSupport.queryForList(sql, GoodsDO.class, shopId);
    }

    @Override
    public List<GoodsSkuVO> listByGoodsId(Integer goodsId) {
        return goodsSkuManager.listByGoodsId(goodsId);
    }

    @Override
    public List<GoodsDO> getSellerGoods(Integer sellerId) {

        return goodsQueryManager.getSellerGoods(sellerId);
    }
}
