package com.enation.app.javashop.core.trade.order.service.impl;

import com.enation.app.javashop.core.aftersale.model.dos.AfterSaleServiceDO;
import com.enation.app.javashop.core.aftersale.service.AfterSaleQueryManager;
import com.enation.app.javashop.core.base.DomainHelper;
import com.enation.app.javashop.core.base.SettingGroup;
import com.enation.app.javashop.core.client.system.SettingClient;
import com.enation.app.javashop.core.payment.model.dos.PaymentMethodDO;
import com.enation.app.javashop.core.payment.service.PaymentMethodManager;
import com.enation.app.javashop.core.promotion.coupon.model.vo.GoodsCouponPrice;
import com.enation.app.javashop.core.promotion.fulldiscount.model.dos.FullDiscountGiftDO;
import com.enation.app.javashop.core.system.model.vo.SiteSetting;
import com.enation.app.javashop.core.trade.TradeErrorCode;
import com.enation.app.javashop.core.trade.cart.model.vo.CouponVO;
import com.enation.app.javashop.core.trade.complain.model.enums.ComplainSkuStatusEnum;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.dos.OrderItemsDO;
import com.enation.app.javashop.core.trade.order.model.dos.OrderMetaDO;
import com.enation.app.javashop.core.trade.order.model.dto.OrderDetailQueryParam;
import com.enation.app.javashop.core.trade.order.model.dto.OrderQueryParam;
import com.enation.app.javashop.core.trade.order.model.enums.*;
import com.enation.app.javashop.core.trade.order.model.vo.*;
import com.enation.app.javashop.core.trade.order.service.OrderMetaManager;
import com.enation.app.javashop.core.trade.order.service.OrderQueryManager;
import com.enation.app.javashop.core.trade.sdk.model.OrderDetailDTO;
import com.enation.app.javashop.core.trade.sdk.model.OrderSkuDTO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单查询业务实现类
 *
 * @author Snow create in 2018/5/14
 * @version v2.0
 * @since v7.0.0
 */
@Service
public class OrderQueryManagerImpl implements OrderQueryManager {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private OrderMetaManager orderMetaManager;

    @Autowired
    private SettingClient settingClient;

    @Autowired
    private PaymentMethodManager paymentMethodManager;

    @Autowired
    private DomainHelper domainHelper;

    @Autowired
    private AfterSaleQueryManager afterSaleQueryManager;


    @Override
    public Page list(OrderQueryParam paramDTO) {

        StringBuffer sql = new StringBuffer("select * from es_order o where disabled=0 ");
        List<Object> term = new ArrayList<>();

        if (paramDTO.getKeywords() != null) {
            sql.append(" and (sn like ? or items_json like ? )");
            term.add("%" + paramDTO.getKeywords() + "%");
            term.add("%" + paramDTO.getKeywords() + "%");
        }

        // 按卖家查询
        Integer sellerId = paramDTO.getSellerId();
        if (sellerId != null) {
            sql.append(" and o.seller_id=?");
            term.add(sellerId);
        }

        // 按买家查询
        Integer memberId = paramDTO.getMemberId();
        if (memberId != null) {
            sql.append(" and o.member_id=?");
            term.add(memberId);
        }

        // 按订单编号查询
        if (StringUtil.notEmpty(paramDTO.getOrderSn())) {
            sql.append(" and o.sn like ?");
            term.add("%" + paramDTO.getOrderSn() + "%");
        }

        // 按交易编号查询
        if (StringUtil.notEmpty(paramDTO.getTradeSn())) {
            sql.append(" and o.trade_sn like ?");
            term.add("%" + paramDTO.getTradeSn() + "%");
        }

        // 按时间查询
        Long startTime = paramDTO.getStartTime();
        Long endTime = paramDTO.getEndTime();
        if (startTime != null) {

            String startDay = DateUtil.toString(startTime, "yyyy-MM-dd");
            sql.append(" and o.create_time >= ?");
            term.add(DateUtil.getDateline(startDay + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
        }

        if (endTime != null) {
            String endDay = DateUtil.toString(endTime, "yyyy-MM-dd");
            sql.append(" and o.create_time <= ?");
            term.add(DateUtil.getDateline(endDay + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
        }

        // 按购买人用户名
        String memberName = paramDTO.getBuyerName();
        if (StringUtil.notEmpty(memberName)) {
            sql.append(" and o.member_name like ?");
            term.add("%" + memberName + "%");
        }

        // 按标签查询
        String tag = paramDTO.getTag();
        if (!StringUtil.isEmpty(tag)) {
            OrderTagEnum tagEnum = OrderTagEnum.valueOf(tag);
            switch (tagEnum) {
                case ALL:
                    break;
                //待付款
                case WAIT_PAY:
                    // 非货到付款的，未付款状态的可以结算 OR 货到付款的要发货或收货后才能结算
                    sql.append(" and ( ( ( payment_type!='cod' and  order_status='" + OrderStatusEnum.CONFIRM + "') ");
                    sql.append(" or ( payment_type='cod' and   order_status='" + OrderStatusEnum.ROG + "'  ) ) ");
                    sql.append(" or order_status = '" + OrderStatusEnum.NEW + "' )");
                    break;

                //待发货
                case WAIT_SHIP:
                    // 普通订单：
                    //      非货到付款的，要已结算才能发货 OR 货到付款的，已确认就可以发货
                    // 拼团订单：
                    //      已经成团的
                    sql.append(" and (");
                    sql.append(" ( payment_type!='cod' and (order_type='" + OrderTypeEnum.NORMAL + "' or order_type='"+OrderTypeEnum.CHANGE+"' or order_type='"+OrderTypeEnum.SUPPLY_AGAIN+"')  and  order_status='" + OrderStatusEnum.PAID_OFF + "')  ");
                    sql.append(" or ( payment_type='cod' and order_type='" + OrderTypeEnum.NORMAL + "'  and  order_status='" + OrderStatusEnum.CONFIRM + "') ");
                    sql.append(" or ( order_type='" + OrderTypeEnum.PINTUAN + "'  and  order_status='" + OrderStatusEnum.FORMED + "') ");
                    sql.append(")");
                    break;

                //待收货
                case WAIT_ROG:
                    sql.append(" and o.order_status='" + OrderStatusEnum.SHIPPED + "'");
                    break;

                //待评论
                case WAIT_COMMENT:
                    sql.append(" and o.ship_status='" + ShipStatusEnum.SHIP_ROG + "' and o.comment_status='" + CommentStatusEnum.UNFINISHED + "' ");
                    break;

                //待追评
                case WAIT_CHASE:
                    sql.append(" and o.ship_status='" + ShipStatusEnum.SHIP_ROG + "' and o.comment_status='" + CommentStatusEnum.WAIT_CHASE + "' ");
                    break;

                //已取消
                case CANCELLED:
                    sql.append(" and o.order_status='" + OrderStatusEnum.CANCELLED + "'");
                    break;

                case COMPLETE:
                    sql.append(" and o.order_status='" + OrderStatusEnum.COMPLETE + "'");
                    break;
                default:
                    break;
            }
        }
        //订单状态
        if (!StringUtil.isEmpty(paramDTO.getOrderStatus())) {
            sql.append(" and o.order_status = ?");
            term.add(paramDTO.getOrderStatus());
        }

        if (StringUtil.notEmpty(paramDTO.getBuyerName())) {
            sql.append(" and o.items_json like ?");
            term.add("%" + paramDTO.getGoodsName() + "%");
        }
        if (StringUtil.notEmpty(paramDTO.getShipName())) {
            sql.append(" and o.ship_name like ?");
            term.add("%" + paramDTO.getShipName() + "%");
        }
        // 按商品名称查询
        if (StringUtil.notEmpty(paramDTO.getGoodsName())) {
            sql.append(" and o.items_json like ?");
            term.add("%" + paramDTO.getGoodsName() + "%");
        }

        //付款方式
        if (!StringUtil.isEmpty(paramDTO.getPaymentType())) {
            sql.append(" and o.payment_type = ?");
            term.add(paramDTO.getPaymentType());
        }

        //订单来源
        if (!StringUtil.isEmpty(paramDTO.getClientType())) {
            sql.append(" and o.client_type = ?");
            term.add(paramDTO.getClientType());
        }

        sql.append(" order by o.order_id desc");

        // 先按PO进行查询
        Page<OrderDO> page = daoSupport.queryForPage(sql.toString(), paramDTO.getPageNo(), paramDTO.getPageSize(),
                OrderDO.class, term.toArray());


        //订单自动取消天数
        int cancelLeftDay = getCancelLeftDay();

        // 转为VO
        List<OrderDO> orderList = page.getData();
        List<OrderLineVO> lineList = new ArrayList();
        for (OrderDO orderDO : orderList) {
            OrderLineVO line = new OrderLineVO(orderDO);

            //如果未付款并且是在线支付则显示取消时间
            if (!PayStatusEnum.PAY_YES.value().equals(orderDO.getPayStatus())
                    && PaymentTypeEnum.ONLINE.value().equals(orderDO.getPaymentType())
                    && !OrderStatusEnum.CANCELLED.value().equals(orderDO.getOrderStatus())) {
                //计算自动取消剩余时间
                Long leftTime = getCancelLeftTime(line.getCreateTime(), cancelLeftDay);
                line.setCancelLeftTime(leftTime);


            } else {
                line.setCancelLeftTime(0L);
            }

            //默认订单是不支持原路退款操作的
            line.setIsRetrace(false);

            //如果订单已付款并且是在线支付的，那么需要获取订单的支付方式判断是否支持原路退款操作
            if (PayStatusEnum.PAY_YES.value().equals(orderDO.getPayStatus())
                    && PaymentTypeEnum.ONLINE.value().equals(orderDO.getPaymentType())) {
                //获取订单的支付方式
                PaymentMethodDO paymentMethodDO = this.paymentMethodManager.getByPluginId(orderDO.getPaymentPluginId());
                if (paymentMethodDO != null && paymentMethodDO.getIsRetrace() == 1) {
                    line.setIsRetrace(true);
                }
            }

            if (OrderTypeEnum.PINTUAN.name().equals(line.getOrderType())) {

                //如果订单
                int waitNums = convertOwesNums(orderDO);
                line.setWaitingGroupNums(waitNums);
                if (waitNums == 0 && PayStatusEnum.PAY_YES.value().equals(line.getPayStatus())) {
                    line.setPingTuanStatus("已成团");
                } else if (OrderStatusEnum.CANCELLED.value().equals(line.getOrderStatus())) {
                    line.setPingTuanStatus("未成团");
                } else {
                    line.setPingTuanStatus("待成团");
                }
            }

            lineList.add(line);
        }


        // 生成新的Page
        long totalCount = page.getDataTotal();
        Page<OrderLineVO> linePage = new Page(paramDTO.getPageNo(), totalCount, paramDTO.getPageSize(), lineList);

        return linePage;
    }

    @Override
    public List<OrderLineVO> export(OrderQueryParam paramDTO) {
        StringBuffer sql = new StringBuffer("select * from es_order o where disabled=0 ");
        List<Object> term = new ArrayList<>();

        // 按订单编号查询
        if (StringUtil.notEmpty(paramDTO.getOrderSn())) {
            sql.append(" and o.sn like ?");
            term.add("%" + paramDTO.getOrderSn() + "%");
        }

        // 按收货人查询
        if (StringUtil.notEmpty(paramDTO.getShipName())) {
            sql.append(" and o.ship_name like ?");
            term.add("%" + paramDTO.getShipName() + "%");
        }

        // 按商品名称查询
        if (StringUtil.notEmpty(paramDTO.getGoodsName())) {
            sql.append(" and o.items_json like ?");
            term.add("%" + paramDTO.getGoodsName() + "%");
        }

        // 按购买人用户名
        String memberName = paramDTO.getBuyerName();
        if (StringUtil.notEmpty(memberName)) {
            sql.append(" and o.member_name like ?");
            term.add("%" + memberName + "%");
        }

        // 订单状态
        if (!StringUtil.isEmpty(paramDTO.getOrderStatus())) {
            sql.append(" and o.order_status = ?");
            term.add(paramDTO.getOrderStatus());
        }

        // 按下单时间查询
        Long startTime = paramDTO.getStartTime();
        Long endTime = paramDTO.getEndTime();
        if (startTime != null && startTime != 0) {
            String startDay = DateUtil.toString(startTime, "yyyy-MM-dd");
            sql.append(" and o.create_time >= ?");
            term.add(DateUtil.getDateline(startDay + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
        }

        if (endTime != null && endTime != 0) {
            String endDay = DateUtil.toString(endTime, "yyyy-MM-dd");
            sql.append(" and o.create_time <= ?");
            term.add(DateUtil.getDateline(endDay + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
        }

        // 按卖家ID查询
        Integer sellerId = paramDTO.getSellerId();
        if (sellerId != null) {
            sql.append(" and o.seller_id=?");
            term.add(sellerId);
        }

        // 按付款方式查询
        if (!StringUtil.isEmpty(paramDTO.getPaymentType())) {
            sql.append(" and o.payment_type = ?");
            term.add(paramDTO.getPaymentType());
        }

        // 按订单来源查询
        if (!StringUtil.isEmpty(paramDTO.getClientType())) {
            sql.append(" and o.client_type = ?");
            term.add(paramDTO.getClientType());
        }

        sql.append(" order by o.order_id desc");

        // 先按PO进行查询
        List<OrderDO> orderDOList = this.daoSupport.queryForList(sql.toString(), OrderDO.class, term.toArray());

        // 转为VO
        List<OrderLineVO> lineList = new ArrayList();
        for (OrderDO orderDO : orderDOList) {
            OrderLineVO line = new OrderLineVO(orderDO);
            lineList.add(line);
        }

        return lineList;
    }

    /**
     * 读取订单自动取消天数
     *
     * @return
     */
    private int getCancelLeftDay() {
        String settingVOJson = this.settingClient.get(SettingGroup.TRADE);
        OrderSettingVO settingVO = JsonUtil.jsonToObject(settingVOJson, OrderSettingVO.class);
        int day = settingVO.getCancelOrderDay();
        return day;
    }


    private Long getCancelLeftTime(Long createTime, int cancelLeftDay) {

        Long cancelTime = createTime + cancelLeftDay * 24 * 60 * 60;
        Long now = DateUtil.getDateline();
        Long leftTime = cancelTime - now;
        if (leftTime < 0) {
            leftTime = 0L;
        }
        return leftTime;

    }


    /**
     * 转换拼团的 待成团人数
     *
     * @param orderDO 订单do
     * @return 待成团人数
     */
    private int convertOwesNums(OrderDO orderDO) {
        //取出个性化数据
        String orderData = orderDO.getOrderData();
        Gson gson = new GsonBuilder().create();
        if (!StringUtil.isEmpty(orderData)) {

            //将个性化数据转为map
            Map map = gson.fromJson(orderData, HashMap.class);

            //转换拼团个性化数据
            String json = (String) map.get(OrderDataKey.pintuan.name());
            if (!StringUtil.isEmpty(json)) {
                Map pintuanMap = gson.fromJson(json, HashMap.class);
                Double nums = (Double) pintuanMap.get("owesPersonNums");
                return nums.intValue();
            }
        }

        return 0;


    }


    @Override
    public OrderDetailVO getModel(String orderSn, OrderDetailQueryParam queryParam) {

        List param = new ArrayList();

        StringBuffer sql = new StringBuffer();
        sql.append("select * from es_order where sn = ? ");
        param.add(orderSn);

        if (queryParam != null && queryParam.getSellerId() != null) {
            sql.append(" and seller_id =?");
            param.add(queryParam.getSellerId());

        } else if (queryParam != null && queryParam.getBuyerId() != null) {
            sql.append(" and member_id=? ");
            param.add(queryParam.getBuyerId());
        }

        OrderDO orderDO = this.daoSupport.queryForObject(sql.toString(), OrderDO.class, param.toArray());
        if (orderDO == null) {
            throw new ServiceException(TradeErrorCode.E453.code(), "订单不存在");
        }

        OrderDetailVO detailVO = new OrderDetailVO();
        BeanUtils.copyProperties(orderDO, detailVO);
        //初始化sku信息
        List<OrderSkuVO> skuList = JsonUtil.jsonToList(orderDO.getItemsJson(), OrderSkuVO.class);

        //订单商品原价
        double goodsOriginalPrice = 0.00;

        for (OrderSkuVO skuVO : skuList) {
            //设置商品的可操作状态
            skuVO.setGoodsOperateAllowableVO(new GoodsOperateAllowable(ShipStatusEnum.valueOf(orderDO.getShipStatus()),
                    OrderServiceStatusEnum.valueOf(skuVO.getServiceStatus()), PayStatusEnum.valueOf(orderDO.getPayStatus()),
                    skuVO.getComplainStatus() == null ? ComplainSkuStatusEnum.EXPIRED : ComplainSkuStatusEnum.valueOf(skuVO.getComplainStatus())));

            //计算订单商品原价总和
            goodsOriginalPrice = CurrencyUtil.add(goodsOriginalPrice, CurrencyUtil.mul(skuVO.getOriginalPrice(), skuVO.getNum()));
        }

        detailVO.setOrderSkuList(skuList);

        // 初始化订单允许状态
        OrderOperateAllowable operateAllowableVO = new OrderOperateAllowable(detailVO);

        detailVO.setOrderOperateAllowableVO(operateAllowableVO);


        List<OrderMetaDO> metalList = this.orderMetaManager.list(orderSn);

        Double couponPrice = 0D;
        for (OrderMetaDO metaDO : metalList) {

            //订单的赠品信息
            if (OrderMetaKeyEnum.GIFT.name().equals(metaDO.getMetaKey())) {
                String giftJson = metaDO.getMetaValue();
                if (!StringUtil.isEmpty(giftJson)) {
                    List<FullDiscountGiftDO> giftList = JsonUtil.jsonToList(giftJson, FullDiscountGiftDO.class);
                    detailVO.setGiftList(giftList);
                }
            }

            //使用的积分
            if (OrderMetaKeyEnum.POINT.name().equals(metaDO.getMetaKey())) {
                String pointStr = metaDO.getMetaValue();
                int point = 0;
                if (!StringUtil.isEmpty(pointStr)) {
                    point = Integer.valueOf(pointStr);
                }

                detailVO.setUsePoint(point);

            }


            //赠送的积分
            if (OrderMetaKeyEnum.GIFT_POINT.name().equals(metaDO.getMetaKey())) {
                String giftPointStr = metaDO.getMetaValue();
                int giftPoint = 0;
                if (!StringUtil.isEmpty(giftPointStr)) {
                    giftPoint = Integer.valueOf(giftPoint);
                }

                detailVO.setGiftPoint(giftPoint);

            }


            //满减金额
            if (OrderMetaKeyEnum.FULL_MINUS.name().equals(metaDO.getMetaKey())) {
                Double fullMinus = 0D;
                if (!StringUtil.isEmpty(metaDO.getMetaValue())) {
                    fullMinus = Double.valueOf(metaDO.getMetaValue());
                }
                detailVO.setFullMinus(fullMinus);
            }


            if (OrderMetaKeyEnum.COUPON.name().equals(metaDO.getMetaKey())) {

                String couponJson = metaDO.getMetaValue();
                if (!StringUtil.isEmpty(couponJson)) {
                    List<CouponVO> couponList = JsonUtil.jsonToList(couponJson, CouponVO.class);
                    if (couponList != null && !couponList.isEmpty()) {
                        CouponVO couponVO = couponList.get(0);
                        detailVO.setGiftCoupon(couponVO);
                    }

                }

            }

            //优惠券抵扣金额
            if (OrderMetaKeyEnum.COUPON_PRICE.name().equals(metaDO.getMetaKey())) {
                String couponPriceStr = metaDO.getMetaValue();

                if (!StringUtil.isEmpty(couponPriceStr) && !"null".equals(couponPriceStr)
                        && !"0.0".equals(couponPriceStr)) {
                    List<GoodsCouponPrice> couponList = JsonUtil.jsonToList(couponPriceStr, GoodsCouponPrice.class);
                    for (GoodsCouponPrice goodsCouponPrice : couponList) {
                        couponPrice += goodsCouponPrice.getCouponPrice();
                    }
                }
                //设置优惠券抵扣金额
                detailVO.setCouponPrice(couponPrice);

            }


        }


        //设置订单的返现金额
        Double cashBack = CurrencyUtil.sub(detailVO.getDiscountPrice(), couponPrice);
        detailVO.setCashBack(cashBack);

        //当商品总价(优惠后的商品单价*数量+总优惠金额)超过商品原价总价
        if (detailVO.getGoodsPrice().doubleValue() > goodsOriginalPrice) {
            detailVO.setGoodsPrice(CurrencyUtil.sub(CurrencyUtil.sub(goodsOriginalPrice, cashBack), couponPrice));
        }
        if (OrderTypeEnum.PINTUAN.name().equals(detailVO.getOrderType())) {

            //如果订单
            int waitNums = convertOwesNums(orderDO);
            if (waitNums == 0 && PayStatusEnum.PAY_YES.value().equals(detailVO.getPayStatus())) {
                detailVO.setPingTuanStatus("已成团");
            } else if (OrderStatusEnum.CANCELLED.value().equals(detailVO.getOrderStatus())) {
                detailVO.setPingTuanStatus("未成团");
            } else {
                detailVO.setPingTuanStatus("待成团");
            }
        }

        //默认订单是不支持原路退款操作的
        detailVO.setIsRetrace(false);

        //如果订单已付款并且是在线支付的，那么需要获取订单的支付方式判断是否支持原路退款操作
        if (PayStatusEnum.PAY_YES.value().equals(orderDO.getPayStatus())
                && PaymentTypeEnum.ONLINE.value().equals(orderDO.getPaymentType())) {
            //获取订单的支付方式
            PaymentMethodDO paymentMethodDO = this.paymentMethodManager.getByPluginId(orderDO.getPaymentPluginId());
            if (paymentMethodDO != null && paymentMethodDO.getIsRetrace() == 1) {
                detailVO.setIsRetrace(true);
            }
        }

        return detailVO;
    }

    /**
     * 读取一个订单详细
     *
     * @param orderId
     * @return
     */
    @Override
    public OrderDO getModel(Integer orderId) {
        return this.daoSupport.queryForObject(OrderDO.class, orderId);
    }

    @Override
    public OrderDO getOrder(String orderSn) {
        return this.daoSupport.queryForObject("select * from es_order where sn = ?", OrderDO.class, orderSn);
    }

    @Override
    public OrderStatusNumVO getOrderStatusNum(Integer memberId, Integer sellerId) {

        StringBuffer sql = new StringBuffer("select order_type,order_status,pay_status,ship_status,payment_type,comment_status,count(order_id) as count from es_order o ");

        List<Object> term = new ArrayList<>();

        List<String> sqlSplice = new ArrayList<>();
        //按商家查询
        if (sellerId != null) {
            sqlSplice.add("o.seller_id = ? ");
            term.add(sellerId);
        }
        // 按买家查询
        if (memberId != null) {
            sqlSplice.add("o.member_id = ? ");
            term.add(memberId);
        }
        String sqlSplicing = SqlSplicingUtil.sqlSplicing(sqlSplice);
        if (!StringUtil.isEmpty(sqlSplicing)) {
            sql.append(sqlSplicing);
        }
        sql.append(" GROUP BY order_status,pay_status,ship_status,comment_status,payment_type,order_type");

        List<Map<String, Object>> list = this.daoSupport.queryForList(sql.toString(), term.toArray());

        // 所有订单数
        StringBuilder allNumSql = new StringBuilder("select count(order_id) as count from es_order o ");
        if (!StringUtil.isEmpty(sqlSplicing)) {
            allNumSql.append(sqlSplicing);
        }

        OrderStatusNumVO numVO = new OrderStatusNumVO();
        numVO.setWaitShipNum(0);
        numVO.setWaitPayNum(0);
        numVO.setWaitRogNum(0);
        numVO.setCompleteNum(0);
        numVO.setCancelNum(0);
        numVO.setWaitCommentNum(0);
        numVO.setAllNum(this.daoSupport.queryForInt(allNumSql.toString(), term.toArray()));

        // 支付状态未支付，订单状态已确认，为待付款订单
        for (Map map : list) {
            boolean flag = (OrderStatusEnum.CONFIRM.value().equals(map.get("order_status").toString()) && !"COD".equals(map.get("payment_type").toString()))
                    || (OrderStatusEnum.ROG.value().equals(map.get("order_status").toString()) && "COD".equals(map.get("payment_type").toString()));
            if (flag) {
                numVO.setWaitPayNum(numVO.getWaitPayNum() + (null == map.get("count") ? 0 : Integer.parseInt(map.get("count").toString())));
            }

            // 物流状态为未发货，订单状态为已收款，为待发货订单
            flag = (OrderStatusEnum.CONFIRM.value().equals(map.get("order_status").toString()) && "COD".equals(map.get("payment_type").toString()) && OrderTypeEnum.NORMAL.name().equals(map.get("order_type").toString()))
                    || (OrderStatusEnum.PAID_OFF.value().equals(map.get("order_status").toString()) && !"COD".equals(map.get("payment_type").toString()) && OrderTypeEnum.NORMAL.name().equals(map.get("order_type").toString()))
                    || (OrderTypeEnum.PINTUAN.name().equals(map.get("order_type").toString()) && OrderStatusEnum.FORMED.value().equals(map.get("order_status").toString()));
            if (flag) {
                numVO.setWaitShipNum(numVO.getWaitShipNum() + (null == map.get("count") ? 0 : Integer.parseInt(map.get("count").toString())));
            }

            // 订单状态为已发货，为待收货订单
            if (OrderStatusEnum.SHIPPED.value().equals(map.get("order_status").toString())) {
                numVO.setWaitRogNum(numVO.getWaitRogNum() + (null == map.get("count") ? 0 : Integer.parseInt(map.get("count").toString())));
            }

            // 订单状态为已取消，为已取消订单
            if (OrderStatusEnum.CANCELLED.value().equals(map.get("order_status").toString())) {
                numVO.setCancelNum(numVO.getCancelNum() + (null == map.get("count") ? 0 : Integer.parseInt(map.get("count").toString())));
            }

            // 订单状态为已完成，为已完成订单
            if (OrderStatusEnum.COMPLETE.value().equals(map.get("order_status").toString())) {
                numVO.setCompleteNum(numVO.getCompleteNum() + (null == map.get("count") ? 0 : Integer.parseInt(map.get("count").toString())));
            }

            // 评论状态为未评论，订单状态为已收货，为待评论订单
            if (CommentStatusEnum.UNFINISHED.value().equals(map.get("comment_status").toString()) && OrderStatusEnum.ROG.value().equals(map.get("order_status").toString())) {
                numVO.setWaitCommentNum(numVO.getWaitCommentNum() + (null == map.get("count") ? 0 : Integer.parseInt(map.get("count").toString())));
            }
        }

        // 申请售后，但未完成售后的订单
        numVO.setRefundNum(this.afterSaleQueryManager.getAfterSaleCount(memberId, sellerId));

        return numVO;

    }

    @Override
    public List<OrderFlowNode> getOrderFlow(String orderSn) {

        OrderDetailVO orderDetailVO = this.getModel(orderSn, null);
        String orderStatus = orderDetailVO.getOrderStatus();
        String serviceStatus = orderDetailVO.getServiceStatus();
        String paymentType = orderDetailVO.getPaymentType();

        //如果订单售后状态是已申请取消订单
        if (serviceStatus.equals(OrderServiceStatusEnum.APPLY.value())) {
            AfterSaleServiceDO serviceDO = this.afterSaleQueryManager.getCancelService(orderSn);
            List<OrderFlowNode> resultFlow = getFlow(OrderFlow.getCancelOrderFlow(), serviceDO.getServiceStatus());
            return resultFlow;
        }

        //如果订单状态是已取消
        if (orderStatus.equals(OrderStatusEnum.CANCELLED.name())) {
            return OrderFlow.getCancelFlow();
        }

        //如果订单状态是出库失败
        if (orderStatus.equals(OrderStatusEnum.INTODB_ERROR.name())) {
            return OrderFlow.getIntodbErrorFlow();
        }

        List<OrderFlowNode> resultFlow = getFlow(OrderFlow.getFlow(orderDetailVO.getOrderType(), paymentType), orderStatus);
        return resultFlow;
    }


    @Override
    public Integer getOrderNumByMemberId(Integer memberId) {
        String sql = "select count(0) from es_order where member_id=?";
        Integer num = this.daoSupport.queryForInt(sql, memberId);
        return num;
    }


    @Override
    public Integer getOrderCommentNumByMemberId(Integer memberId, String commentStatus) {
        StringBuffer sql = new StringBuffer("select count(0) from es_order where member_id=? ");

        sql.append(" and ship_status='" + ShipStatusEnum.SHIP_ROG + "' and comment_status = ?  ");

        Integer num = this.daoSupport.queryForInt(sql.toString(), memberId, commentStatus);
        return num;
    }


    @Override
    public List<OrderDetailDTO> getOrderByTradeSn(String tradeSn) {
        List<OrderDetailVO> orderDetailVOList = this.getOrderByTradeSn(tradeSn, null);

        List<OrderDetailDTO> orderDetailDTOList = new ArrayList<>();
        for (OrderDetailVO orderDetailVO : orderDetailVOList) {
            OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
            BeanUtils.copyProperties(orderDetailVO, orderDetailDTO);
            orderDetailDTOList.add(orderDetailDTO);
        }
        return orderDetailDTOList;
    }

    @Override
    public List<OrderDetailVO> getOrderByTradeSn(String tradeSn, Integer memberId) {
        String sql = "select * from es_order where trade_sn = ?";
        List<OrderDetailVO> orderDetailVOList = this.daoSupport.queryForList(sql, OrderDetailVO.class, tradeSn);

        if (orderDetailVOList == null) {
            return new ArrayList<>();
        }
        return orderDetailVOList;
    }

    @Override
    public List<OrderItemsDO> orderItems(String orderSn) {
        return daoSupport.queryForList("select * from es_order_items where order_sn = ?", OrderItemsDO.class, orderSn);
    }

    @Override
    public OrderDetailDTO getModel(String orderSn) {
        OrderDetailVO orderDetailVO = this.getModel(orderSn, null);
        OrderDetailDTO detailDTO = new OrderDetailDTO();
        BeanUtils.copyProperties(orderDetailVO, detailDTO);
        detailDTO.setOrderSkuList(new ArrayList<>());

        for (OrderSkuVO skuVO : orderDetailVO.getOrderSkuList()) {
            OrderSkuDTO skuDTO = new OrderSkuDTO();
            BeanUtil.copyProperties(skuVO, skuDTO);
            detailDTO.getOrderSkuList().add(skuDTO);
        }

        String json = this.orderMetaManager.getMetaValue(detailDTO.getSn(), OrderMetaKeyEnum.GIFT);
        List<FullDiscountGiftDO> giftList = JsonUtil.jsonToList(json, FullDiscountGiftDO.class);
        detailDTO.setGiftList(giftList);

        return detailDTO;
    }

    @Override
    public double getOrderRefundPrice(String orderSn) {
        double refundPrice = 0.00;
        List<OrderItemsDO> itemsDOList = this.orderItems(orderSn);
        if (itemsDOList != null && itemsDOList.size() != 0) {
            for (OrderItemsDO itemsDO : itemsDOList) {
                refundPrice = CurrencyUtil.add(refundPrice, itemsDO.getRefundPrice());
            }
        }
        return refundPrice;
    }

    @Override
    public List<OrderDO> listOrderByGoods(Integer goodsId, Integer memberId, Integer month) {
        StringBuffer sql = new StringBuffer("select o.* from es_order o left join es_order_items oi on o.sn = oi.order_sn " +
                "where o.disabled = ? and (o.order_status = ? or o.order_status = ?) and o.create_time >= ? and oi.goods_id = ? and o.member_id != ? " +
                "group by o.member_id order by o.create_time desc");
        List<Object> term = new ArrayList<>();
        term.add(0);
        term.add(OrderStatusEnum.ROG.value());
        term.add(OrderStatusEnum.COMPLETE.value());
        term.add(DateUtil.getBeforeMonthDateline(month));
        term.add(goodsId);
        term.add(memberId);
        return this.daoSupport.queryForList(sql.toString(), OrderDO.class, term.toArray());
    }

    @Override
    public InvoiceVO getInvoice(Integer orderId) {
        //获取订单信息
        OrderDO orderDO = this.getModel(orderId);
        //获取sku信息
        List<OrderSkuVO> skuList = JsonUtil.jsonToList(orderDO.getItemsJson(), OrderSkuVO.class);
        //获取站点信息
        String siteJson = settingClient.get(SettingGroup.SITE);
        SiteSetting siteSetting = JsonUtil.jsonToObject(siteJson, SiteSetting.class);
        //组织返回数据
        InvoiceVO invoiceVO = new InvoiceVO();
        invoiceVO.setOrderSkuList(skuList);
        invoiceVO.setLogo(siteSetting.getLogo());
        invoiceVO.setSiteName(siteSetting.getSiteName());
        invoiceVO.setSiteAddress(domainHelper.getBuyerDomain());
        invoiceVO.setAddress(orderDO.getShipAddr());
        invoiceVO.setConsignee(orderDO.getShipName());
        invoiceVO.setRegion(orderDO.getShipProvince() + orderDO.getShipCity() + orderDO.getShipCounty() + orderDO.getShipTown());
        invoiceVO.setOrderCreateTime(orderDO.getCreateTime());
        invoiceVO.setCreateTime(DateUtil.getDateline());
        invoiceVO.setMemberName(orderDO.getMemberName());
        invoiceVO.setSn(orderDO.getSn());
        invoiceVO.setPhone(orderDO.getShipMobile());
        return invoiceVO;
    }

    /**
     * 获取订单相关流程公共方法
     * @param resultFlow 流程结构集合
     * @param status 状态
     * @return
     */
    private List<OrderFlowNode> getFlow(List<OrderFlowNode> resultFlow, String status) {
        boolean isEnd = false;
        for (OrderFlowNode flow : resultFlow) {

            flow.setShowStatus(1);
            if (isEnd) {
                flow.setShowStatus(0);
            }

            if (flow.getOrderStatus().equals(status)) {
                isEnd = true;
            }

        }
        return resultFlow;
    }

}