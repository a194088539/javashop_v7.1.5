package com.enation.app.javashop.core.trade.snapshot.service.impl;

import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.goods.model.dos.BrandDO;
import com.enation.app.javashop.core.goods.model.dos.CategoryDO;
import com.enation.app.javashop.core.goods.model.dos.GoodsDO;
import com.enation.app.javashop.core.goods.model.dos.GoodsGalleryDO;
import com.enation.app.javashop.core.goods.model.enums.Permission;
import com.enation.app.javashop.core.goods.model.vo.GoodsParamsGroupVO;
import com.enation.app.javashop.core.goods.model.vo.GoodsSnapshotVO;
import com.enation.app.javashop.core.goods.model.vo.SpecValueVO;
import com.enation.app.javashop.core.promotion.coupon.model.dos.CouponDO;
import com.enation.app.javashop.core.promotion.coupon.service.CouponManager;
import com.enation.app.javashop.core.promotion.tool.model.vo.PromotionVO;
import com.enation.app.javashop.core.promotion.tool.service.PromotionGoodsManager;
import com.enation.app.javashop.core.trade.TradeErrorCode;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.dos.OrderItemsDO;
import com.enation.app.javashop.core.trade.order.model.vo.OrderSkuVO;
import com.enation.app.javashop.core.trade.order.service.OrderOperateManager;
import com.enation.app.javashop.core.trade.snapshot.model.SnapshotVO;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.framework.security.model.Seller;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.core.trade.snapshot.model.GoodsSnapshot;
import com.enation.app.javashop.core.trade.snapshot.service.GoodsSnapshotManager;

import java.util.List;

/**
 * 交易快照业务类
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-08-01 14:55:26
 */
@Service
public class GoodsSnapshotManagerImpl implements GoodsSnapshotManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private OrderOperateManager orderOperateManager;

    @Autowired
    private PromotionGoodsManager promotionGoodsManager;
    @Autowired
    private CouponManager couponManager;


    @Override
    public Page list(int page, int pageSize) {

        String sql = "select * from es_goods_snapshot  ";
        Page webPage = this.daoSupport.queryForPage(sql, page, pageSize, GoodsSnapshot.class);

        return webPage;
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public GoodsSnapshot add(GoodsSnapshot goodsSnapshot) {
        this.daoSupport.insert(goodsSnapshot);

        int id = this.daoSupport.getLastId("");

        goodsSnapshot.setSnapshotId(id);

        return goodsSnapshot;
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public GoodsSnapshot edit(GoodsSnapshot goodsSnapshot, Integer id) {
        this.daoSupport.update(goodsSnapshot, id);
        return goodsSnapshot;
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer id) {
        this.daoSupport.delete(GoodsSnapshot.class, id);
    }

    @Override
    public GoodsSnapshot getModel(Integer id) {
        return this.daoSupport.queryForObject(GoodsSnapshot.class, id);
    }

    @Override
    public void add(OrderDO orderDO) {

        //查看订单中的商品
        List<OrderSkuVO> skuList = JsonUtil.jsonToList(orderDO.getItemsJson(), OrderSkuVO.class);
        if (skuList != null) {
            for (OrderSkuVO sku : skuList) {

                GoodsSnapshotVO snapshotGoods = goodsClient.queryGoodsSnapShotInfo(sku.getGoodsId());

                //商品的促销信息
                List<PromotionVO> promotionVOList = this.promotionGoodsManager.getPromotion(sku.getGoodsId());


                GoodsDO goods = snapshotGoods.getGoods();

                //商品的优惠券信息
                List<CouponDO> couponDOList = this.couponManager.getList(goods.getSellerId());

                CategoryDO category = snapshotGoods.getCategoryDO();
                List<GoodsGalleryDO> galleryList = snapshotGoods.getGalleryList();
                List<GoodsParamsGroupVO> paramList = snapshotGoods.getParamList();
                BrandDO brand = snapshotGoods.getBrandDO();

                GoodsSnapshot snapshot = new GoodsSnapshot();
                snapshot.setGoodsId(sku.getGoodsId());
                snapshot.setName(goods.getGoodsName());
                snapshot.setSn(goods.getSn());
                snapshot.setCategoryName(category.getName());
                snapshot.setBrandName(brand == null ? "" : brand.getName());
                snapshot.setGoodsType(goods.getGoodsType());
                snapshot.setHaveSpec(goods.getHaveSpec() == null ? 0 : goods.getHaveSpec());
                snapshot.setWeight(goods.getWeight());
                snapshot.setIntro(goods.getIntro());
                snapshot.setPrice(sku.getOriginalPrice());
                snapshot.setCost(goods.getCost());
                snapshot.setMktprice(goods.getMktprice());
                snapshot.setParamsJson(JsonUtil.objectToJson(paramList));
                snapshot.setImgJson(JsonUtil.objectToJson(galleryList));
                snapshot.setPoint(goods.getPoint());
                snapshot.setSellerId(goods.getSellerId());
                snapshot.setCreateTime(DateUtil.getDateline());
                snapshot.setPromotionJson(JsonUtil.objectToJson(promotionVOList));
                snapshot.setCouponJson(JsonUtil.objectToJson(couponDOList));
                snapshot.setMemberId(orderDO.getMemberId());
                //添加快照
                this.add(snapshot);
                Integer snapshotId = snapshot.getSnapshotId();
                sku.setSnapshotId(snapshotId);
                //更新订单项的快照id
                String sql = "update es_order_items set snapshot_id = ? where order_sn = ? and product_id = ?";
                this.daoSupport.execute(sql, snapshotId, orderDO.getSn(), sku.getSkuId());
            }

            if (logger.isDebugEnabled()) {
                logger.debug("生成商品快照信息");
            }
            //更新订单
            orderOperateManager.updateItemJson(JsonUtil.objectToJson(skuList), orderDO.getSn());
        }
    }

    @Override
    public SnapshotVO get(Integer id, String owner) {

        GoodsSnapshot model = this.getModel(id);
        if (!Permission.SELLER.name().equals(owner) && !Permission.BUYER.name().equals(owner)) {
            if(logger.isDebugEnabled()){
                logger.debug("传参错误");
            }
            throw new ServiceException(TradeErrorCode.E453.code(), "无权查看，请联系管理员");
        }
        //查看是卖家查看还是买家查看
        if (Permission.SELLER.name().equals(owner)) {
            Seller seller = UserContext.getSeller();
            if (seller == null || !seller.getSellerId().equals(model.getSellerId())) {
                if(logger.isDebugEnabled()){
                    logger.debug("seller == null？"+(seller == null)+",equals?"+seller.getSellerId()+"=="+model.getSellerId());
                }
                throw new ServiceException(TradeErrorCode.E453.code(), "无权查看");
            }
        }
        if (Permission.BUYER.name().equals(owner)) {
            Buyer buyer = UserContext.getBuyer();
            if (buyer == null || !buyer.getUid().equals(model.getMemberId())) {
                if(logger.isDebugEnabled()){
                    logger.debug("buyer == null？"+(buyer == null)+",equals?"+buyer.getUid()+"=="+model.getMemberId());
                }
                throw new ServiceException(TradeErrorCode.E453.code(), "无权查看");
            }
        }
        SnapshotVO snapshotVO = new SnapshotVO();
        BeanUtils.copyProperties(model, snapshotVO);

        if (model.getHaveSpec() == 1) {
            //有规格
            String sql = "select * from es_order_items where snapshot_id = ?";
            OrderItemsDO items = this.daoSupport.queryForObject(sql, OrderItemsDO.class, id);
            List<SpecValueVO> specs = JsonUtil.jsonToList(items.getSpecJson(), SpecValueVO.class);
            snapshotVO.setSpecList(specs);
        }

        return snapshotVO;
    }
}
