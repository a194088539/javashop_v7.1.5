package com.enation.app.javashop.core.promotion.tool.service.impl;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.promotion.PromotionErrorCode;
import com.enation.app.javashop.core.promotion.coupon.model.dos.CouponDO;
import com.enation.app.javashop.core.promotion.coupon.service.CouponManager;
import com.enation.app.javashop.core.promotion.exchange.model.dos.ExchangeDO;
import com.enation.app.javashop.core.promotion.exchange.service.ExchangeGoodsManager;
import com.enation.app.javashop.core.promotion.fulldiscount.model.dos.FullDiscountGiftDO;
import com.enation.app.javashop.core.promotion.fulldiscount.model.vo.FullDiscountVO;
import com.enation.app.javashop.core.promotion.fulldiscount.service.FullDiscountGiftManager;
import com.enation.app.javashop.core.promotion.fulldiscount.service.FullDiscountManager;
import com.enation.app.javashop.core.promotion.groupbuy.model.dos.GroupbuyGoodsDO;
import com.enation.app.javashop.core.promotion.groupbuy.model.vo.GroupbuyGoodsVO;
import com.enation.app.javashop.core.promotion.groupbuy.service.GroupbuyGoodsManager;
import com.enation.app.javashop.core.promotion.halfprice.model.vo.HalfPriceVO;
import com.enation.app.javashop.core.promotion.halfprice.service.HalfPriceManager;
import com.enation.app.javashop.core.promotion.minus.model.vo.MinusVO;
import com.enation.app.javashop.core.promotion.minus.service.MinusManager;
import com.enation.app.javashop.core.promotion.model.PromotionAbnormalGoods;
import com.enation.app.javashop.core.promotion.seckill.model.vo.SeckillGoodsVO;
import com.enation.app.javashop.core.promotion.seckill.service.SeckillManager;
import com.enation.app.javashop.core.promotion.tool.model.dos.PromotionGoodsDO;
import com.enation.app.javashop.core.promotion.tool.model.dto.PromotionDetailDTO;
import com.enation.app.javashop.core.promotion.tool.model.dto.PromotionGoodsDTO;
import com.enation.app.javashop.core.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.javashop.core.promotion.tool.model.vo.PromotionVO;
import com.enation.app.javashop.core.promotion.tool.service.PromotionGoodsManager;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.SqlUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动商品对照实现类
 *
 * @author Snow create in 2018/3/21
 * @version v2.0
 * @since v7.0.0
 */
@Service
public class PromotionGoodsManagerImpl implements PromotionGoodsManager {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private Cache cache;

    @Autowired
    private ExchangeGoodsManager exchangeGoodsManager;

    @Autowired
    private GroupbuyGoodsManager groupbuyGoodsManager;

    @Autowired
    private FullDiscountManager fullDiscountManager;

    @Autowired
    private MinusManager minusManager;

    @Autowired
    private HalfPriceManager halfPriceManager;

    @Autowired
    private SeckillManager seckillManager;

    @Autowired
    private FullDiscountGiftManager fullDiscountGiftManager;

    @Autowired
    private CouponManager couponManager;

    @Autowired
    private GoodsClient goodsClient;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {RuntimeException.class, Exception.class})
    public void add(List<PromotionGoodsDTO> list, PromotionDetailDTO detailDTO) {

        if (list.isEmpty()) {
            throw new ServiceException(PromotionErrorCode.E400.code(), "没有可用的商品参与此活动");
        }

        /**
         * 因为 Spring jdbcTemplate 没有提供批量插入的方法,
         * 使用 insert into table_name (xxx1,xxx2) VALUES (1,2),(3,3) 拼接参数，会有sql注入的问题。
         * 所以经过和架构师的沟通，此操作不是高频操作，可以一个一个插入数据库。
         */
        for (PromotionGoodsDTO goodsDTO : list) {

            PromotionGoodsDO goodsDO = new PromotionGoodsDO();
            goodsDO.setGoodsId(goodsDTO.getGoodsId());
            goodsDO.setStartTime(detailDTO.getStartTime());
            goodsDO.setEndTime(detailDTO.getEndTime());
            goodsDO.setActivityId(detailDTO.getActivityId());
            goodsDO.setPromotionType(detailDTO.getPromotionType());
            goodsDO.setTitle(detailDTO.getTitle());
            goodsDO.setSellerId(goodsDTO.getSellerId());

            String promotionType = goodsDO.getPromotionType();
            //因为限时抢购和团购活动为 平台发布,不是商家
            if (goodsDO.getSellerId() == null && !PromotionTypeEnum.SECKILL.name().equals(promotionType)
                    && !PromotionTypeEnum.GROUPBUY.name().equals(promotionType)) {
                goodsDO.setSellerId(UserContext.getSeller().getSellerId());
            }
            this.daoSupport.insert(goodsDO);

        }

    }

    @Override
    public void edit(List<PromotionGoodsDTO> list, PromotionDetailDTO detailDTO) {
        this.delete(detailDTO.getActivityId(), detailDTO.getPromotionType());
        this.add(list, detailDTO);
    }


    @Override
    public void delete(Integer activityId, String promotionType) {
        String sql = "DELETE FROM es_promotion_goods WHERE activity_id=? and promotion_type= ? ";
        this.daoSupport.execute(sql, activityId, promotionType);
    }

    @Override
    public void delete(Integer goodsId, Integer activityId, String promotionType) {
        String sql = "DELETE FROM es_promotion_goods WHERE activity_id=? and promotion_type= ? and goods_id = ?";
        this.daoSupport.execute(sql, activityId, promotionType, goodsId);
    }

    @Override
    public void delete(Integer goodsId) {
        String sql = "DELETE FROM es_promotion_goods WHERE goods_id = ? and end_time >= ? and promotion_type != ?";
        this.daoSupport.execute(sql,  goodsId,DateUtil.getDateline(),PromotionTypeEnum.EXCHANGE.name());
    }

    @Override
    public List<PromotionVO> getPromotion(Integer goodsId) {

        long currTime = DateUtil.getDateline();

        String currDate = DateUtil.toString(currTime, "yyyyMMdd");
        //使用关键字+当天日期+goods_id为关键字
        String promotionKey = CachePrefix.PROMOTION_KEY + currDate + goodsId;
        //从缓存中读取商品活动信息
        List<PromotionVO> promotionVOList = (List<PromotionVO>) cache.get(promotionKey);

        if (promotionVOList == null || promotionVOList.isEmpty()) {
            promotionVOList = new ArrayList<>();


            //读取此商品参加的活动
            String sql = "select distinct goods_id, start_time, end_time, activity_id, promotion_type,title,num,price " +
                    "from es_promotion_goods where goods_id=? and start_time<=? and end_time>=?";
            List<PromotionGoodsDO> resultList = this.daoSupport.queryForList(sql, PromotionGoodsDO.class, goodsId, currTime, currTime);

            //查询全部商品参与的活动，条件为：
            // 商品id && 大于等于开始时间 && 小于等于结束时间 && 不是团购活动 && 不是限时抢购活动 && 商家id
            Integer totalGoodsId = -1;
            sql = "select distinct goods_id, start_time, end_time, activity_id, promotion_type,title,num,price " +
                    " from es_promotion_goods where goods_id=? and start_time<=? and end_time>=? " +
                    " and promotion_type != '" + PromotionTypeEnum.GROUPBUY.name() + "' " +
                    " and promotion_type != '" + PromotionTypeEnum.SECKILL.name() + "' and seller_id=?";

            CacheGoods cacheGoods = this.goodsClient.getFromCache(goodsId);
            List<PromotionGoodsDO> promotionGoodsDOList = this.daoSupport.queryForList(sql, PromotionGoodsDO.class, totalGoodsId, currTime, currTime, cacheGoods.getSellerId());
            resultList.addAll(promotionGoodsDOList);

            for (PromotionGoodsDO promotionGoodsDO : resultList) {
                PromotionVO promotionVO = new PromotionVO();
                BeanUtils.copyProperties(promotionGoodsDO, promotionVO);

                //积分换购
                if (promotionGoodsDO.getPromotionType().equals(PromotionTypeEnum.EXCHANGE.name())) {
                    ExchangeDO exchange = exchangeGoodsManager.getModel(promotionGoodsDO.getActivityId());
                    promotionVO.setExchange(exchange);
                }

                //团购
                if (promotionGoodsDO.getPromotionType().equals(PromotionTypeEnum.GROUPBUY.name())) {
                    GroupbuyGoodsDO groupbuyGoodsDO = groupbuyGoodsManager.getModel(promotionGoodsDO.getActivityId(), promotionGoodsDO.getGoodsId());

                    GroupbuyGoodsVO groupbuyGoodsVO = new GroupbuyGoodsVO();
                    BeanUtils.copyProperties(groupbuyGoodsDO, groupbuyGoodsVO);

                    promotionVO.setGroupbuyGoodsVO(groupbuyGoodsVO);
                }

                //满优惠活动
                if (promotionGoodsDO.getPromotionType().equals(PromotionTypeEnum.FULL_DISCOUNT.name())) {
                    FullDiscountVO fullDiscountVO = this.fullDiscountManager.getModel(promotionGoodsDO.getActivityId());

                    //赠品
                    if (fullDiscountVO.getIsSendGift() != null && fullDiscountVO.getIsSendGift() == 1 && fullDiscountVO.getGiftId() != null) {
                        FullDiscountGiftDO giftDO = fullDiscountGiftManager.getModel(fullDiscountVO.getGiftId());
                        fullDiscountVO.setFullDiscountGiftDO(giftDO);
                    }

                    //判断是否赠品优惠券
                    if (fullDiscountVO.getIsSendBonus() != null && fullDiscountVO.getIsSendBonus() == 1 && fullDiscountVO.getBonusId() != null) {
                        CouponDO couponDO = this.couponManager.getModel(fullDiscountVO.getBonusId());
                        fullDiscountVO.setCouponDO(couponDO);
                    }

                    //判断是否赠品优惠券
                    if (fullDiscountVO.getIsSendPoint() != null && fullDiscountVO.getIsSendPoint() == 1 && fullDiscountVO.getPointValue() != null) {
                        fullDiscountVO.setPoint(fullDiscountVO.getPointValue());
                    }


                    promotionVO.setFullDiscountVO(fullDiscountVO);
                }

                //单品立减活动
                if (promotionGoodsDO.getPromotionType().equals(PromotionTypeEnum.MINUS.name())) {
                    MinusVO minusVO = this.minusManager.getFromDB(promotionGoodsDO.getActivityId());
                    promotionVO.setMinusVO(minusVO);
                }

                //第二件半价活动
                if (promotionGoodsDO.getPromotionType().equals(PromotionTypeEnum.HALF_PRICE.name())) {
                    HalfPriceVO halfPriceVO = this.halfPriceManager.getFromDB(promotionGoodsDO.getActivityId());
                    promotionVO.setHalfPriceVO(halfPriceVO);
                }
                //限时抢购活动
                if (promotionGoodsDO.getPromotionType().equals(PromotionTypeEnum.SECKILL.name())) {
                    SeckillGoodsVO seckillGoodsVO = this.seckillManager.getSeckillGoods(promotionGoodsDO.getGoodsId());
                    promotionVO.setSeckillGoodsVO(seckillGoodsVO);
                }

                promotionVOList.add(promotionVO);
            }

        } else {


            for (PromotionVO promotionVO : promotionVOList) {

                //当前时间大于活动的开始时间 && 当前时间小于活动的结束时间
                if (currTime > promotionVO.getStartTime() && currTime < promotionVO.getEndTime()) {

                    //初始化限时抢购数据，防止返回的时间戳错误
                    promotionVO.setSeckillGoodsVO(null);

                    //限时抢购活动需要重新计算 距离结束的时间戳 返回给前端
                    if (promotionVO.getPromotionType().equals(PromotionTypeEnum.SECKILL.name())) {
                        SeckillGoodsVO seckillGoodsVO = this.seckillManager.getSeckillGoods(promotionVO.getGoodsId());
                        promotionVO.setSeckillGoodsVO(seckillGoodsVO);
                    }
                    promotionVOList.add(promotionVO);
                }
            }

        }

        return promotionVOList;
    }


    @Override
    public List<PromotionGoodsDO> getPromotionGoods(Integer activityId, String promotionType) {
        String sql = "select * from es_promotion_goods where activity_id=? and promotion_type=?";
        List<PromotionGoodsDO> goodsDOList = this.daoSupport.queryForList(sql, PromotionGoodsDO.class, activityId, promotionType);
        return goodsDOList;
    }


    @Override
    public void cleanCache(Integer goodsId) {
        long currTime = DateUtil.getDateline();
        String currDate = DateUtil.toString(currTime, "yyyyMMdd");
        //使用关键字+当天日期+goods_id为关键字
        String promotionKey = CachePrefix.PROMOTION_KEY + currDate + goodsId;
        this.cache.remove(promotionKey);

    }

    /**
     * 重新写入缓存
     *
     * @param goodsId
     */
    @Override
    public void reputCache(Integer goodsId) {
        this.cleanCache(goodsId);
        this.getPromotion(goodsId);
    }

    /**
     * 查询指定时间范围，是否有参与其他活动
     *
     * @param goodsIds  商品id集合
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    @Override
    public List<PromotionAbnormalGoods> checkPromotion(Integer[] goodsIds, Long startTime, Long endTime) {


        //何为冲突
        //（1）
        //                       校验时间
        //                  --》【              】《--
        // ******************************************************************       时间轴
        //                已经存在 活动
        //       --》【              】《--
        // ******************************************************************       时间轴

        //（2）
        //                       校验时间
        //                  --》【              】《--
        // ******************************************************************       时间轴
        //                                       已经存在 活动
        //                              --》【              】《--
        // ******************************************************************       时间轴

        //（3）
        //                       校验时间
        //                  --》【              】《--
        // ******************************************************************       时间轴
        //                       已经存在 活动
        //        --》【                               】《--
        // ******************************************************************       时间轴

        //（4）
        //                       校验时间
        //                  --》【              】《--
        // ******************************************************************       时间轴
        //                       已经存在 活动
        //                      --》【     】《--
        // ******************************************************************       时间轴

        List term = new ArrayList();
        for (Integer goodsId :
                goodsIds) {
            term.add(goodsId);
        }

        term.add(startTime);
        term.add(startTime);
        term.add(endTime);
        term.add(endTime);
        term.add(startTime);
        term.add(endTime);
        term.add(endTime);
        term.add(startTime);

        //查询时间轴以外的促销活动商品
        List<PromotionGoodsDO> promotionGoodsDOS = this.daoSupport.queryForList("select * from es_promotion_goods where goods_id in (" + SqlUtil.getInSql(goodsIds)
                        + ") and ( (start_time < ? and end_time > ?) or (start_time < ? and end_time > ?) or(start_time < ? and end_time > ?) or(start_time < ? and end_time > ?))",
                PromotionGoodsDO.class, term.toArray()
        );

        List<PromotionAbnormalGoods> promotionAbnormalGoods = new ArrayList<>();
        promotionGoodsDOS.forEach(goods -> {
            PromotionAbnormalGoods paGoods = new PromotionAbnormalGoods();
            paGoods.setEndTime(goods.getEndTime());
            paGoods.setStartTime(goods.getStartTime());
            paGoods.setGoodsId(goods.getGoodsId());
            paGoods.setGoodsName(goodsClient.getFromCache(paGoods.getGoodsId()).getGoodsName());
            paGoods.setPromotionType(goods.getPromotionType());
            promotionAbnormalGoods.add(paGoods);
        });
        return promotionAbnormalGoods;

    }


    /**
     * 活动商品缓存
     *
     * @param startTime
     * @param goodsId
     * @return
     */
    private String getPromotionGoodsKey(long startTime, int goodsId) {
        String key = CachePrefix.PROMOTION_KEY.getPrefix() + DateUtil.toString(startTime, "yyyyMMdd") + goodsId;
        return key;
    }

    public static void main(String[] args) {

        long line = DateUtil.getDateline("2018-12-25 22:05:00", "yyyy-MM-dd HH:mm:ss");
        String str = DateUtil.toString(1514120400L, "yyyy-MM-dd HH:mm:ss");
//        System.out.println(str);
        System.out.println(line);
//        System.out.println(DateUtil.getDateline());
//        Integer[] goodsIds = new Integer[2];
//
//        goodsIds[0]=1;
//        goodsIds[1]=2;
//        String goodsIdStr = StringUtils.join(goodsIds,",");
//
//        System.out.println(goodsIdStr);


    }

}
