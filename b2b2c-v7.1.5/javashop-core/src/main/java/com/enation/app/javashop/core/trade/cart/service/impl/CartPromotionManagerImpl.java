package com.enation.app.javashop.core.trade.cart.service.impl;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.client.member.MemberCouponClient;
import com.enation.app.javashop.core.goods.model.vo.GoodsSkuVO;
import com.enation.app.javashop.core.member.model.dos.MemberCoupon;
import com.enation.app.javashop.core.promotion.coupon.model.vo.CouponValidateResult;
import com.enation.app.javashop.core.promotion.exchange.model.dos.ExchangeDO;
import com.enation.app.javashop.core.promotion.exchange.service.ExchangeGoodsManager;
import com.enation.app.javashop.core.promotion.fulldiscount.model.vo.FullDiscountVO;
import com.enation.app.javashop.core.promotion.groupbuy.model.dos.GroupbuyActiveDO;
import com.enation.app.javashop.core.promotion.groupbuy.model.dos.GroupbuyGoodsDO;
import com.enation.app.javashop.core.promotion.groupbuy.model.vo.GroupbuyGoodsVO;
import com.enation.app.javashop.core.promotion.groupbuy.service.GroupbuyActiveManager;
import com.enation.app.javashop.core.promotion.groupbuy.service.GroupbuyGoodsManager;
import com.enation.app.javashop.core.promotion.halfprice.model.vo.HalfPriceVO;
import com.enation.app.javashop.core.promotion.halfprice.service.HalfPriceManager;
import com.enation.app.javashop.core.promotion.minus.model.vo.MinusVO;
import com.enation.app.javashop.core.promotion.minus.service.MinusManager;
import com.enation.app.javashop.core.promotion.seckill.model.vo.SeckillGoodsVO;
import com.enation.app.javashop.core.promotion.seckill.service.SeckillManager;
import com.enation.app.javashop.core.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.javashop.core.promotion.tool.model.vo.FullDiscountWithGoodsId;
import com.enation.app.javashop.core.promotion.tool.model.vo.PromotionVO;
import com.enation.app.javashop.core.statistics.util.DateUtil;
import com.enation.app.javashop.core.trade.TradeErrorCode;
import com.enation.app.javashop.core.trade.cart.model.vo.*;
import com.enation.app.javashop.core.trade.cart.service.CartPromotionManager;
import com.enation.app.javashop.core.trade.cart.util.CartUtil;
import com.enation.app.javashop.core.trade.cart.util.CouponValidateUtil;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Buyer;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;

/**
 * 购物车促销信息处理实现类
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/12/1
 */
@Service
public class CartPromotionManagerImpl implements CartPromotionManager {

    protected final Log logger = LogFactory.getLog(this.getClass());


    @Autowired
    private Cache cache;


    @Autowired
    private ExchangeGoodsManager exchangeGoodsManager;

    @Autowired
    private GroupbuyGoodsManager groupbuyGoodsManager;

    @Autowired
    private GroupbuyActiveManager groupbuyActiveManager;

    @Autowired
    private MinusManager minusManager;

    @Autowired
    private HalfPriceManager halfPriceManager;

    @Autowired
    private SeckillManager seckillManager;


    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private MemberCouponClient memberCouponClient;


    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport tradeDaoSupport;


    private String getOriginKey() {
        String cacheKey = "";
        //如果会员登录了，则要以会员id为key
        Buyer buyer = UserContext.getBuyer();
        if (buyer != null) {
            cacheKey = CachePrefix.CART_PROMOTION_PREFIX.getPrefix() + buyer.getUid();
        }
        return cacheKey;
    }

    /**
     * 由缓存中读取出用户选择的促销信息
     *
     * @return 用户选择的促销信息
     */
    @Override
    public SelectedPromotionVo getSelectedPromotion() {
        String cacheKey = this.getOriginKey();
        SelectedPromotionVo selectedPromotionVo = (SelectedPromotionVo) cache.get(cacheKey);
        if (selectedPromotionVo == null) {
            selectedPromotionVo = new SelectedPromotionVo();
            cache.put(cacheKey, selectedPromotionVo);
        }

        return selectedPromotionVo;
    }

    @Override
    public List<FullDiscountVO> getFullDiscounPromotion(List<CartVO> cartList) {

        StringBuffer goodsIdStr = new StringBuffer("-1");

        for (CartVO cartVO : cartList) {
            List<CartSkuVO> skuList = cartVO.getSkuList();
            for (CartSkuVO skuVO : skuList) {
                //如果商品失效，
                if (skuVO.getInvalid() == 1) {
                    continue;
                }
                goodsIdStr.append(",");
                goodsIdStr.append(skuVO.getGoodsId());
            }
        }

        long now = DateUtil.getDateline();

        //查询所有正在进行的满减活动
        String sql = "select fd.*,pg.goods_id from es_full_discount fd left join es_promotion_goods pg on fd.fd_id = pg.activity_id  " +
                " where  fd.start_time <? and fd.end_time>? and  pg.goods_id in  (" + goodsIdStr + ") order by fd.fd_id asc";


        List<FullDiscountWithGoodsId> list = tradeDaoSupport.queryForList(sql, FullDiscountWithGoodsId.class, now, now);

        List<FullDiscountVO> fullDiscountVOList = new ArrayList<>();

        //上一个活动id，在变化时说明要生成新的vo
        Integer preFdId = null;
        FullDiscountVO fullDiscountVO = null;
        for (FullDiscountWithGoodsId fullDiscountWithGoodsId : list) {
            Integer fdid = fullDiscountWithGoodsId.getFdId();

            //需要生成新vo
            if (!fdid.equals(preFdId)) {
                fullDiscountVO = new FullDiscountVO();
                BeanUtils.copyProperties(fullDiscountWithGoodsId, fullDiscountVO);
                fullDiscountVOList.add(fullDiscountVO);
                preFdId = fdid;

            }

            fullDiscountVO.getGoodsIdList().add(fullDiscountWithGoodsId.getGoodsId());

        }

        return fullDiscountVOList;

    }


    @Override
    public void usePromotion(Integer sellerId, Integer skuId, Integer activityId, PromotionTypeEnum promotionType) {
        Assert.notNull(promotionType, "未知的促销类型");

        try {

            SelectedPromotionVo selectedPromotionVo = this.getSelectedPromotion();

            PromotionVO promotionVO = new PromotionVO();
            promotionVO.setSkuId(skuId);
            promotionVO.setPromotionType(promotionType.name());

            if (PromotionTypeEnum.EXCHANGE.equals(promotionType)) {
                ExchangeDO exchangeDO = exchangeGoodsManager.getModel(activityId);
                promotionVO.setExchange(exchangeDO);
                promotionVO.setActivityId(exchangeDO.getExchangeId());
            }

            if (PromotionTypeEnum.GROUPBUY.equals(promotionType)) {
                GoodsSkuVO skuVO = goodsClient.getSkuFromCache(skuId);
                GroupbuyActiveDO activeDO = groupbuyActiveManager.getModel(activityId);
                GroupbuyGoodsDO groupbuyGoodsDO = groupbuyGoodsManager.getModel(activityId, skuVO.getGoodsId());
                GroupbuyGoodsVO groupbuyGoodsVO = new GroupbuyGoodsVO();
                BeanUtils.copyProperties(groupbuyGoodsDO, groupbuyGoodsVO);

                groupbuyGoodsVO.setStartTime(activeDO.getStartTime());
                groupbuyGoodsVO.setEndTime(activeDO.getEndTime());
                promotionVO.setGroupbuyGoodsVO(groupbuyGoodsVO);
                promotionVO.setActivityId(groupbuyGoodsVO.getActId());
            }

            //单品立减活动
            if (PromotionTypeEnum.MINUS.equals(promotionType)) {
                MinusVO minusVO = this.minusManager.getFromDB(activityId);
                promotionVO.setMinusVO(minusVO);
                promotionVO.setActivityId(minusVO.getMinusId());
            }

            //第二件半价活动
            if (PromotionTypeEnum.HALF_PRICE.equals(promotionType)) {
                HalfPriceVO halfPriceVO = this.halfPriceManager.getFromDB(activityId);
                promotionVO.setHalfPriceVO(halfPriceVO);
                promotionVO.setActivityId(halfPriceVO.getHpId());
            }

            //限时抢购活动
            if (PromotionTypeEnum.SECKILL.equals(promotionType)) {
                GoodsSkuVO goodsSkuVO = goodsClient.getSkuFromCache(skuId);
                SeckillGoodsVO seckillGoodsVO = this.seckillManager.getSeckillGoods(goodsSkuVO.getGoodsId());
                promotionVO.setSeckillGoodsVO(seckillGoodsVO);
            }
            selectedPromotionVo.putPromotion(sellerId, promotionVO);
            String cacheKey = this.getOriginKey();
            cache.put(cacheKey, selectedPromotionVo);
            if (logger.isDebugEnabled()) {
                logger.debug("使用促销：" + promotionVO);
                logger.debug("促销信息为:" + selectedPromotionVo);
            }
        } catch (Exception e) {
            logger.error("使用促销出错", e);
            throw new ServiceException(TradeErrorCode.E462.code(), "使用促销出错");
        }


    }

    @Override
    public void useCoupon(Integer sellerId, Integer mcId, List<CartVO> cartList) {

        Buyer buyer = UserContext.getBuyer();
        MemberCoupon memberCoupon = this.memberCouponClient.getModel(buyer.getUid(), mcId);

        //如果优惠券Id为0并且优惠券为空则取消优惠券使用
        if (memberCoupon == null && mcId.equals(0)) {
            this.deleteCoupon(sellerId);
            return;
        }
        //如果优惠券为空则抛出异常
        if (memberCoupon == null) {
            throw new ServiceException(TradeErrorCode.E455.code(), "当前优惠券不存在");
        }
        //查询选中的促销
        SelectedPromotionVo selectedPromotionVo = getSelectedPromotion();
        //查看购物车中是否包含积分商品
        for (CartVO cartVO : cartList) {
            if (CouponValidateUtil.validateCoupon(selectedPromotionVo, sellerId, cartVO.getSkuList())) {
                throw new ServiceException(TradeErrorCode.E455.code(), "您选择的商品包含积分兑换的商品不能使用优惠券！");
            }
        }

        //判断是平台优惠券，还是商家优惠券
        if (memberCoupon.getSellerId().equals(0)) {
            //平台优惠券
            CouponValidateResult isUseRes = CouponValidateUtil.isEnable(memberCoupon, cartList);
            if (isUseRes.isEnable()) {
                //平台优惠券和店铺优惠券不能同时使用，所以选中了平台优惠券，先将优惠券清空，再设置选中的优惠券
                this.cleanCoupon();
            }
        } else {
            //平台优惠券和店铺优惠券不能同时使用
            this.deleteCoupon(0);
            //商家优惠券
            CartVO cart = CartUtil.findCart(sellerId, cartList);
            double goodsPrice = cart.getPrice().getGoodsPrice();

            //校验优惠券的限额
            if (goodsPrice < memberCoupon.getCouponThresholdPrice()) {
                throw new ServiceException(TradeErrorCode.E455.code(), "未达到优惠券使用最低限额");
            }
        }

        CouponVO couponVO = new CouponVO(memberCoupon);

        SelectedPromotionVo selectedPromotion = getSelectedPromotion();

        selectedPromotion.putCooupon(sellerId, couponVO);
        if (logger.isDebugEnabled()) {
            logger.debug("使用优惠券：" + couponVO);
            logger.debug("促销信息为:" + selectedPromotionVo);
        }
        String cacheKey = this.getOriginKey();
        cache.put(cacheKey, selectedPromotion);
    }


    @Override
    public void deleteCoupon(Integer sellerId) {
        SelectedPromotionVo selectedPromotionVo = getSelectedPromotion();
        selectedPromotionVo.getCouponMap().remove(sellerId);
        String cacheKey = this.getOriginKey();
        cache.put(cacheKey, selectedPromotionVo);
    }

    @Override
    public void cleanCoupon() {
        SelectedPromotionVo selectedPromotionVo = getSelectedPromotion();
        selectedPromotionVo.getCouponMap().clear();
        String cacheKey = this.getOriginKey();
        cache.put(cacheKey, selectedPromotionVo);
    }


    /**
     * 删除一组sku的促销，
     *
     * @param skuids
     */
    @Override
    public void delete(Integer[] skuids) {
        SelectedPromotionVo selectedPromotionVo = this.getSelectedPromotion();
        Map<Integer, List<PromotionVO>> promotionMap = selectedPromotionVo.getSinglePromotionMap();

        //用来记录要删除的店铺
        List<Integer> needRemoveSellerIds = new ArrayList<>();

        Iterator<Integer> sellerIdIter = promotionMap.keySet().iterator();

        while (sellerIdIter.hasNext()) {
            Integer sellerId = sellerIdIter.next();
            List<PromotionVO> skuPromotionVoList = promotionMap.get(sellerId);

            if (skuPromotionVoList == null) {
                continue;
            }

            List<PromotionVO> newList = deleteBySkus(skuids, skuPromotionVoList);

            //如果新list是空的，表明这个店铺已经没有促销活动了
            if (newList.isEmpty()) {
                needRemoveSellerIds.add(sellerId);
            } else {
                //将清理后的
                promotionMap.put(sellerId, newList);
            }

        }

        //经过上述的处理，list中已经有了要删除的店铺id
        for (Integer sellerid : needRemoveSellerIds) {
            promotionMap.remove(sellerid);
        }

        //重新压入缓存
        String cacheKey = this.getOriginKey();
        cache.put(cacheKey, selectedPromotionVo);

    }

    @Override
    public boolean checkPromotionInvalid(Integer skuId) {
        boolean invalid = false;
        SelectedPromotionVo selectedPromotionVo = this.getSelectedPromotion();
        Map<Integer, List<PromotionVO>> promotionMap = selectedPromotionVo.getSinglePromotionMap();
        Map<Integer, List<PromotionVO>> newPromotionMap = new HashMap<>(promotionMap);
        for (Map.Entry<Integer, List<PromotionVO>> entry : promotionMap.entrySet()) {
            Integer key = entry.getKey();
            List<PromotionVO> newList = new ArrayList<>();
            for (PromotionVO promotionVO : entry.getValue()) {
                //遍历所有优惠活动验证是否超过其有效时间
                if (promotionVO.getSkuId().equals(skuId)) {
                    if (promotionVO.getHalfPriceVO() != null && !com.enation.app.javashop.framework.util.DateUtil.inRangeOf(promotionVO.getHalfPriceVO().getStartTime(), promotionVO.getHalfPriceVO().getEndTime())
                            || promotionVO.getGroupbuyGoodsVO() != null && !com.enation.app.javashop.framework.util.DateUtil.inRangeOf(promotionVO.getGroupbuyGoodsVO().getStartTime(), promotionVO.getGroupbuyGoodsVO().getEndTime())
                            || promotionVO.getMinusVO() != null && !com.enation.app.javashop.framework.util.DateUtil.inRangeOf(promotionVO.getMinusVO().getStartTime(), promotionVO.getMinusVO().getEndTime())) {
                        promotionVO = null;
                        invalid = true;
                    } else if (promotionVO.getSeckillGoodsVO() != null) {
                        SeckillGoodsVO seckillGoodsVO = promotionVO.getSeckillGoodsVO();
                        String secKillEndTimeStr = com.enation.app.javashop.framework.util.DateUtil.toString(seckillGoodsVO.getStartTime(), "yyyy-MM-dd") + " 23:59:59";
                        long secKillEndTime = com.enation.app.javashop.framework.util.DateUtil.getDateline(secKillEndTimeStr, "yyyy-MM-dd HH:mm:ss");
                        if (!com.enation.app.javashop.framework.util.DateUtil.inRangeOf(seckillGoodsVO.getStartTime(), secKillEndTime)) {
                            promotionVO = null;
                            invalid = true;
                        }
                    }
                }
                if (promotionVO != null) {
                    newList.add(promotionVO);
                }

            }
            newPromotionMap.remove(key);
            if (newList.size() > 0) {
                newPromotionMap.put(key, newList);
            }

        }
        selectedPromotionVo.setSinglePromotionMap(newPromotionMap);
        String cacheKey = this.getOriginKey();
        cache.put(cacheKey, selectedPromotionVo);
        return invalid;
    }

    @Override
    public void clean() {
        String cacheKey = this.getOriginKey();
        cache.remove(cacheKey);
    }


    /**
     * 从促销活动列表中删除一批sku的活动
     *
     * @param skuids             skuid数组
     * @param skuPromotionVoList 要清理的活动列表
     * @return 清理后的活动列表
     */
    private List<PromotionVO> deleteBySkus(Integer[] skuids, List<PromotionVO> skuPromotionVoList) {
        List<PromotionVO> newList = new ArrayList<>();
        for (PromotionVO promotionVO : skuPromotionVoList) {
            //如果skuid数组中不包含，则不压入新list中
            if (!ArrayUtils.contains(skuids, promotionVO.getSkuId())) {
                newList.add(promotionVO);
            }
        }
        return newList;
    }


}
