package com.enation.app.javashop.core.trade.order.service.impl;

import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.client.member.MemberClient;
import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.goods.model.vo.GoodsSkuVO;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.model.dos.MemberAddress;
import com.enation.app.javashop.core.promotion.coupon.model.enums.CouponUseScope;
import com.enation.app.javashop.core.promotion.coupon.model.vo.GoodsCouponPrice;
import com.enation.app.javashop.core.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.javashop.core.trade.TradeErrorCode;
import com.enation.app.javashop.core.trade.cart.model.vo.*;
import com.enation.app.javashop.core.trade.order.model.dto.OrderDTO;
import com.enation.app.javashop.core.trade.order.model.vo.CheckoutParamVO;
import com.enation.app.javashop.core.trade.order.model.vo.ConsigneeVO;
import com.enation.app.javashop.core.trade.order.model.vo.TradeVO;
import com.enation.app.javashop.core.trade.order.service.ShippingManager;
import com.enation.app.javashop.core.trade.order.service.TradeCreator;
import com.enation.app.javashop.core.trade.order.service.TradeSnCreator;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.framework.util.CurrencyUtil;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kingapex on 2019-01-24.
 * 默认交易创建器
 *
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2019-01-24
 */
@SuppressWarnings("Duplicates")
public class DefaultTradeCreator implements TradeCreator {

    protected CheckoutParamVO param;
    protected CartView cartView;
    protected MemberAddress memberAddress;
    protected ShippingManager shippingManager;
    protected GoodsClient goodsClient;
    protected MemberClient memberClient;
    protected TradeSnCreator tradeSnCreator;

    protected final Log logger = LogFactory.getLog(this.getClass());

    private final String sellerKey = "seller_";
    private final String couponPriceKey = "total_coupon_price_";
    private final String isSite = "is_site";


    public DefaultTradeCreator() {
    }

    /**
     * 通过构造器设置构建交易所需要的原料
     *
     * @param param         结算参数
     * @param cartView      购物车视图
     * @param memberAddress 收货地址
     */
    public DefaultTradeCreator(CheckoutParamVO param, CartView cartView, MemberAddress memberAddress) {

        this.param = param;
        this.cartView = cartView;
        this.memberAddress = memberAddress;
    }

    public DefaultTradeCreator setShippingManager(ShippingManager shippingManager) {
        this.shippingManager = shippingManager;
        return this;
    }

    public DefaultTradeCreator setGoodsClient(GoodsClient goodsClient) {
        this.goodsClient = goodsClient;
        return this;
    }

    public DefaultTradeCreator setMemberClient(MemberClient memberClient) {
        this.memberClient = memberClient;
        return this;
    }

    public DefaultTradeCreator setTradeSnCreator(TradeSnCreator tradeSnCreator) {
        this.tradeSnCreator = tradeSnCreator;
        return this;
    }

    @Override
    public TradeVO createTrade() {

        Assert.notNull(tradeSnCreator, "tradeSnCreator为空，请先调用setTradeSnCreator设置正确的交易号生成器");


        Assert.notNull(param.getAddressId(), "必须选择收货地址");
        Assert.notNull(param.getPaymentType(), "必须选择支付方式");

        Buyer buyer = UserContext.getBuyer();
        //获取购物车信息
        List<CartVO> cartList = cartView.getCartList();

        String tradeNo = tradeSnCreator.generateTradeSn();
        TradeVO tradeVO = new TradeVO();

        //收货人
        ConsigneeVO consignee = new ConsigneeVO(memberAddress);
        tradeVO.setConsignee(consignee);

        //效果编号
        tradeVO.setTradeSn(tradeNo);

        //支付类型
        tradeVO.setPaymentType(param.getPaymentType().value());

        //会员信息
        tradeVO.setMemberId(buyer.getUid());
        tradeVO.setMemberName(buyer.getUsername());
        List<OrderDTO> orderList = new ArrayList<OrderDTO>();

        //订单创建时间
        long createTime = DateUtil.getDateline();

        //所有商家使用优惠券的商品集合 key:sellerId,value:使用优惠券的商品集合
        Map sellerUseMap = this.getSellerCouponMap(cartView.getCouponList(), cartList);

        //生成订单
        for (CartVO cart : cartList) {

            //生成订单编号
            String orderSn = tradeSnCreator.generateOrderSn();

            //购物信息
            OrderDTO order = new OrderDTO(cart);

            //创建时间
            order.setCreateTime(createTime);

            //购买的会员信息
            order.setMemberId(buyer.getUid());
            order.setMemberName(buyer.getUsername());
            order.setTradeSn(tradeNo);
            order.setSn(orderSn);
            order.setConsignee(consignee);

            //配送方式 这个参数暂时无效
            order.setShippingId(0);

            //支付类型
            order.setPaymentType(param.getPaymentType().value());
            //发票
            order.setNeedReceipt(0);
            if (param.getReceipt() != null && !StringUtil.isEmpty(param.getReceipt().getReceiptType())) {
                order.setNeedReceipt(1);
            }
            order.setReceiptHistory(param.getReceipt());
            //收货时间
            order.setReceiveTime(param.getReceiveTime());

            //订单备注
            order.setRemark(param.getRemark());

            //订单来源
            order.setClientType(param.getClientType());

            if (logger.isDebugEnabled()) {
                logger.debug("订单[" + order.getSn() + "]的price:");
                logger.debug(order.getPrice());
            }

            order.setGoodsNum(cart.getSkuList().size());

            //计算该商家可以分享的优惠券的总金额
            Double couponTotalPrice = (Double) sellerUseMap.get(couponPriceKey + order.getSellerId());
            List<GoodsCouponPrice> couponGoodsList = (List<GoodsCouponPrice>) sellerUseMap.get(sellerKey + order.getSellerId());
            order.setGoodsCouponPrices(couponGoodsList);

            order.getPrice().setCouponPrice(couponTotalPrice);
            //订单价格
            order.getPrice().reCountDiscountPrice();

            double orderTotalPrice = order.getPrice().getTotalPrice();
            Boolean site = (Boolean)sellerUseMap.get(isSite);
            if (site) {
                orderTotalPrice = CurrencyUtil.sub(order.getPrice().getTotalPrice(), couponTotalPrice);
            }

            order.setNeedPayMoney(orderTotalPrice);

            order.setOrderPrice(orderTotalPrice);

            orderList.add(order);

        }


        //读取结算价格
        PriceDetailVO paymentDetail = cartView.getTotalPrice();
        paymentDetail.reCountDiscountPrice();
        if (logger.isDebugEnabled()) {
            logger.debug("生成TradeVO时price为");
            logger.debug(paymentDetail);
        }
        //交易价格
        tradeVO.setPriceDetail(paymentDetail);

        tradeVO.setOrderList(orderList);

        return tradeVO;
    }

    /**
     * 将计算商品中可以使用优惠券的组装成map
     *
     * @param couponList
     * @param cartList
     * @return
     */
    private Map getSellerCouponMap(List<CouponVO> couponList, List<CartVO> cartList) {

        //seller_1:List<GoodsCouponPrice>,total_coupon_price_1:Double
        Map sellerUseMap = new HashMap<>();
        sellerUseMap.put(isSite,false);
        if (couponList != null && couponList.size() > 0) {
            for (CouponVO couponVO : cartView.getCouponList()) {
                if (couponVO.getSelected() == 1) {

                    Map map = getEnableGoodsPrice(couponVO, cartList);

                    //1 获取能使用该优惠券的商品的总金额
                    Double goodsTotalPrice = (Double) map.get("totalPrice");
                    //可以使用该优惠券的商品
                    List<GoodsCouponPrice> goodsList = (List<GoodsCouponPrice>) map.get("goods");
                    Double caculateCouponPrice = 0D;
                    int i = 0;
                    for (GoodsCouponPrice goodsCouponPrice : goodsList) {
                        Double couponTotalPrice = (Double) sellerUseMap.get(couponPriceKey + goodsCouponPrice.getSellerId());
                        List<GoodsCouponPrice> goodsCouponPriceList = (List<GoodsCouponPrice>) sellerUseMap.get(sellerKey + goodsCouponPrice.getSellerId());
                        if (goodsCouponPriceList == null || couponTotalPrice == null) {
                            goodsCouponPriceList = new ArrayList<>();
                            couponTotalPrice = 0D;
                        }
                        Double couponPrice;
                        if (i == goodsList.size() - 1) {
                            //最后一个可以使用该优惠券的商品，用优惠券金额-使用的金额
                            couponPrice = CurrencyUtil.sub(couponVO.getAmount(), caculateCouponPrice);
                        } else {
                            //计算该商品拆单后能使用的优惠券金额
                            couponPrice = CurrencyUtil.mul(CurrencyUtil.div(goodsCouponPrice.getGoodsOriginPrice(), goodsTotalPrice, 4), couponVO.getAmount());
                            caculateCouponPrice = CurrencyUtil.add(caculateCouponPrice, couponPrice);
                        }
                        goodsCouponPrice.setCouponPrice(couponPrice);
                        goodsCouponPrice.setCouponId(couponVO.getCouponId());
                        goodsCouponPrice.setMemberCouponId(couponVO.getMemberCouponId());

                        goodsCouponPriceList.add(goodsCouponPrice);
                        //塞进map中
                        sellerUseMap.put(sellerKey + goodsCouponPrice.getSellerId(), goodsCouponPriceList);
                        sellerUseMap.put(couponPriceKey + goodsCouponPrice.getSellerId(), CurrencyUtil.add(couponTotalPrice, couponPrice));
                        i++;
                    }
                    //是否是平台优惠券
                    if(couponVO.getSellerId().equals(0)){
                        sellerUseMap.put(isSite,true);
                    }
                }
            }
        }

        return sellerUseMap;
    }

    /**
     * 获取可以使用该优惠券的商品信息和总价格
     *
     * @param couponVO
     * @param cartList
     * @return
     */
    private Map getEnableGoodsPrice(CouponVO couponVO, List<CartVO> cartList) {

        Map resMap = new HashMap();
        //可以使用该优惠券的商品的总金额
        Double totalPrice = 0d;
        List<GoodsCouponPrice> list = new ArrayList<>();
        for (CartVO orderDTO : cartList) {
            //商家优惠券
            if (!couponVO.getSellerId().equals(0) && orderDTO.getSellerId().equals(couponVO.getSellerId())) {
                totalPrice = orderDTO.getPrice().getOriginalPrice();
                for (CartSkuVO skuVO : orderDTO.getSkuList()) {
                    GoodsCouponPrice goodsCouponPrice = new GoodsCouponPrice(skuVO);
                    list.add(goodsCouponPrice);
                }
                break;
            }
            //平台优惠券
            if (couponVO.getSellerId().equals(0)) {
                List<Integer> skuIdList = couponVO.getEnableSkuList();
                List<CartSkuVO> skuList = orderDTO.getSkuList();
                for (CartSkuVO skuVO : skuList) {
                    //查看商品是否在优惠券可使用的skuid中
                    if (skuIdList.indexOf(skuVO.getSkuId()) > -1 || CouponUseScope.ALL.name().equals(couponVO.getUseScope())) {
                        totalPrice += CurrencyUtil.mul(skuVO.getOriginalPrice(), skuVO.getNum());
                        GoodsCouponPrice goodsCouponPrice = new GoodsCouponPrice(skuVO);
                        list.add(goodsCouponPrice);
                    }
                }
            }

        }

        resMap.put("totalPrice", totalPrice);
        resMap.put("goods", list);

        return resMap;

    }


    @SuppressWarnings("Duplicates")
    @Override
    public TradeCreator checkShipRange() {

        Assert.notNull(shippingManager, "shippingManager为空，请先调用setShippingManager设置正确的交配送管理业务类");

        if (memberAddress == null) {
            throw new ServiceException(TradeErrorCode.E452.code(), "请填写收货地址");
        }

        //已选中结算的商品
        List<CartVO> cartList = cartView.getCartList();

        Integer areaId = memberAddress.getCountyId();

        //2、筛选不在配送区域的商品
        List<CacheGoods> list = this.shippingManager.checkArea(cartList, areaId);

        //验证后存在商品问题的集合
        List<Map> goodsErrorList = new ArrayList();

        if (list.size() > 0) {
            for (CacheGoods goods : list) {
                Map errorMap = new HashMap(16);
                errorMap.put("name", goods.getGoodsName());
                errorMap.put("image", goods.getThumbnail());
                goodsErrorList.add(errorMap);
            }
            throw new ServiceException(TradeErrorCode.E461.code(), "商品不在配送区域", goodsErrorList);
        }


        return this;
    }

    @Override
    public TradeCreator checkGoods() {

        Assert.notNull(goodsClient, "goodsClient为空，请先调用setGoodsClient设置正确的商品业务Client");


        //已选中结算的商品
        List<CartVO> cartList = cartView.getCartList();

        //1、检测购物车是否为空
        if (cartList == null || cartList.isEmpty()) {
            throw new ServiceException(TradeErrorCode.E452.code(), "购物车为空");
        }
        //验证后存在商品问题的集合
        List<Map> goodsErrorList = new ArrayList();

        boolean flag = true;
        //遍历购物车集合
        for (CartVO cartVO : cartList) {

            List<CartSkuVO> skuList = cartVO.getSkuList();

            for (CartSkuVO cartSkuVO : skuList) {
                Map errorMap = new HashMap(16);
                errorMap.put("name", cartSkuVO.getName());
                errorMap.put("image", cartSkuVO.getGoodsImage());

                Integer skuId = cartSkuVO.getSkuId();
                GoodsSkuVO skuVO = this.goodsClient.getSkuFromCache(skuId);

                //检测商品是否存在
                if (skuVO == null) {
                    goodsErrorList.add(errorMap);
                    continue;
                }

                //检测商品的上下架状态
                if (skuVO.getMarketEnable() != null && skuVO.getMarketEnable().intValue() != 1) {
                    goodsErrorList.add(errorMap);
                    continue;
                }

                //检测商品的删除状态
                if (skuVO.getDisabled() != null && skuVO.getDisabled().intValue() != 1) {
                    goodsErrorList.add(errorMap);
                    continue;
                }

                Integer goodsId = skuVO.getGoodsId();
                CacheGoods goodsVo = this.goodsClient.getFromCache(goodsId);
                if (goodsVo.getIsAuth() == 0 || goodsVo.getIsAuth() == 3) {
                    goodsErrorList.add(errorMap);
                    continue;
                }

                //读取此产品的可用库存数量
                int enableQuantity = skuVO.getEnableQuantity();
                //此产品将要购买的数量
                int num = cartSkuVO.getNum();

                //如果将要购买的产品数量大于redis中的数量，则此产品不能下单
                if (num > enableQuantity) {
                    flag = false;
                    goodsErrorList.add(errorMap);
                    continue;
                }
            }
        }

        if (!goodsErrorList.isEmpty()) {
            throw new ServiceException(TradeErrorCode.E452.code(), "抱歉，您以下商品所在地区无货", JsonUtil.objectToJson(goodsErrorList));
        }

        return this;
    }


    @Override
    public TradeCreator checkPromotion() {
        Assert.notNull(memberClient, "memberClient为空，请先调用setMemberClient设置正确的会员业务Client");

        List<CartVO> cartList = cartView.getCartList();

        for (CartVO cartVO : cartList) {

            List<CartSkuVO> skuList = cartVO.getSkuList();

            for (CartSkuVO cartSkuVO : skuList) {
                innerCheckPromotion(cartSkuVO);

            }
        }


        //读取订单的总交易价格信息
        PriceDetailVO detailVO = cartView.getTotalPrice();

        //此交易需要扣除用户的积分
        Long point = detailVO.getExchangePoint();

        if (point > 0) {
            Buyer buyer = UserContext.getBuyer();
            Member member = this.memberClient.getModel(buyer.getUid());
            Long consumPoint = member.getConsumPoint();

            //如果用户可使用的消费积分小于 交易需要扣除的积分时，则不能下单
            if (consumPoint < point) {
                //update by liuyulei 2019-07-17  修改bug,立即购买不经过购物车，错误信息返回:"您可使用的消费积分不足"
                throw new ServiceException(TradeErrorCode.E452.code(), "您可使用的消费积分不足");
            }
        }

        return this;
    }


    private void innerCheckPromotion(CartSkuVO cartSkuVO) {

        Map errorMap = new HashMap(16);
        errorMap.put("name", cartSkuVO.getName());
        errorMap.put("image", cartSkuVO.getGoodsImage());

        //验证后存在促销活动问题的集合
        List<Map> promotionErrorList = new ArrayList();
        boolean flag = true;
        //此商品参与的单品活动
        List<CartPromotionVo> singlePromotionList = cartSkuVO.getSingleList();
        if (!singlePromotionList.isEmpty()) {
            for (CartPromotionVo promotionGoodsVO : singlePromotionList) {

                // 默认参与的活动 && 非不参与活动的状态
                if (promotionGoodsVO.getIsCheck().intValue() == 1 && !promotionGoodsVO.getPromotionType().equals(PromotionTypeEnum.NO.name())) {
                    //当前活动的失效时间
                    long entTime = promotionGoodsVO.getEndTime();

                    //当前时间
                    long currTime = DateUtil.getDateline();

                    //如果当前时间大于失效时间，则此活动已经失效了，不能下单
                    if (currTime > entTime) {
                        flag = false;
                        promotionErrorList.add(errorMap);
                        continue;
                    }
                }

            }
        }

        //此商品参与的组合活动
        List<CartPromotionVo> groupPromotionList = cartSkuVO.getGroupList();
        if (!groupPromotionList.isEmpty()) {
            for (CartPromotionVo cartPromotionGoodsVo : groupPromotionList) {
                //当前活动的失效时间
                long entTime = cartPromotionGoodsVo.getEndTime();

                //当前时间
                long currTime = DateUtil.getDateline();

                //如果当前时间大于失效时间，则此活动已经失效了，不能下单
                if (currTime > entTime) {
                    flag = false;

                    promotionErrorList.add(errorMap);
                    continue;
                }
            }
        }
    }


}
