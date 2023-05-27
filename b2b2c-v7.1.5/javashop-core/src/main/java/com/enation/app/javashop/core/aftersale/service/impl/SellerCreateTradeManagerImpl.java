package com.enation.app.javashop.core.aftersale.service.impl;

import com.enation.app.javashop.core.aftersale.AftersaleErrorCode;
import com.enation.app.javashop.core.aftersale.model.dos.AfterSaleGoodsDO;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceOperateTypeEnum;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceStatusEnum;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceTypeEnum;
import com.enation.app.javashop.core.aftersale.model.vo.ApplyAfterSaleVO;
import com.enation.app.javashop.core.aftersale.service.AfterSaleLogManager;
import com.enation.app.javashop.core.aftersale.service.AfterSaleManager;
import com.enation.app.javashop.core.aftersale.service.AfterSaleQueryManager;
import com.enation.app.javashop.core.aftersale.service.SellerCreateTradeManager;
import com.enation.app.javashop.core.base.message.AfterSaleChangeMessage;
import com.enation.app.javashop.core.base.message.OrderStatusChangeMsg;
import com.enation.app.javashop.core.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.client.goods.GoodsQuantityClient;
import com.enation.app.javashop.core.goods.GoodsErrorCode;
import com.enation.app.javashop.core.goods.model.enums.QuantityType;
import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.goods.model.vo.GoodsQuantityVO;
import com.enation.app.javashop.core.goods.model.vo.GoodsSkuVO;
import com.enation.app.javashop.core.support.FlowCheckOperate;
import com.enation.app.javashop.core.system.enums.ClientType;
import com.enation.app.javashop.core.trade.order.model.dos.*;
import com.enation.app.javashop.core.trade.order.model.enums.*;
import com.enation.app.javashop.core.trade.order.model.vo.OrderSkuVO;
import com.enation.app.javashop.core.trade.order.service.OrderLogManager;
import com.enation.app.javashop.core.trade.order.service.OrderOutStatusManager;
import com.enation.app.javashop.core.trade.order.service.OrderQueryManager;
import com.enation.app.javashop.core.trade.order.service.TradeSnCreator;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Seller;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 商家创建交易业务接口实现类
 * 用户申请换货、补发商品的售后服务时，商家审核通过后，需要生成新的交易订单
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-22
 */
@Service
public class SellerCreateTradeManagerImpl implements SellerCreateTradeManager {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private TradeSnCreator tradeSnCreator;

    @Autowired
    private OrderQueryManager orderQueryManager;

    @Autowired
    private OrderOutStatusManager orderOutStatusManager;

    @Autowired
    private OrderLogManager orderLogManager;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private AfterSaleLogManager afterSaleLogManager;

    @Autowired
    private AfterSaleManager afterSaleManager;

    @Autowired
    private AfterSaleQueryManager afterSaleQueryManager;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private GoodsQuantityClient goodsQuantityClient;

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public OrderDO systemCreateTrade(String serviceSn) {
        try {

            //获取售后服务单详细信息
            ApplyAfterSaleVO applyAfterSaleVO = this.afterSaleQueryManager.detail(serviceSn);

            //创建订单
            OrderDO orderDO = this.createTrade(applyAfterSaleVO, "系统");

            return orderDO;
        } catch (Exception e) {
            //修改售后服务单状态
            this.afterSaleManager.updateServiceStatus(serviceSn, ServiceStatusEnum.ERROR_EXCEPTION.value());
            //新增售后日志
            this.afterSaleLogManager.add(serviceSn, "系统自动生成新订单失败，原因：" + e.getMessage() + "，等待商家手动创建新订单。", "系统");
        }

        return null;
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public OrderDO sellerCreateTrade(String serviceSn) {
        //获取售后服务单详细信息
        ApplyAfterSaleVO applyAfterSaleVO = this.afterSaleQueryManager.detail(serviceSn);

        Seller seller = UserContext.getSeller();
        if (seller == null || seller.getSellerId().intValue() != applyAfterSaleVO.getSellerId().intValue()) {
            throw new ServiceException(AftersaleErrorCode.E614.name(), "售后服务单信息不存在");
        }

        //操作权限验证
        if (!FlowCheckOperate.checkOperate(applyAfterSaleVO.getServiceType(), applyAfterSaleVO.getServiceStatus(), ServiceOperateTypeEnum.CREATE_NEW_ORDER.value())) {
            throw new ServiceException(AftersaleErrorCode.E601.name(), "当前售后服务单状态不允许进行创建新订单操作");
        }

        //创建订单
        OrderDO orderDO = this.createTrade(applyAfterSaleVO, seller.getUsername());

        return orderDO;
    }

    /**
     * 创建订单公共方法
     * @param applyAfterSaleVO 售后服务单详细信息
     * @param operator 操作人
     * @return
     */
    private OrderDO createTrade(ApplyAfterSaleVO applyAfterSaleVO, String operator) {
        //获取原订单信息
        OrderDO orderDO = this.orderQueryManager.getOrder(applyAfterSaleVO.getOrderSn());
        if (orderDO == null) {
            throw new ServiceException(AftersaleErrorCode.E604.name(), "订单信息不存在");
        }

        //获取申请售后服务的商品信息(只有换货和补发商品会生成新订单，而这两种服务都是一对一的，所以这里取商品集合的第一个值即可)
        List<AfterSaleGoodsDO> goodsList = applyAfterSaleVO.getGoodsList();
        AfterSaleGoodsDO goodsDO = goodsList.get(0);

        //获取商品sku信息
        GoodsSkuVO skuVO = this.goodsClient.getSkuFromCache(goodsDO.getSkuId());

        //校验商品信息
        this.checkSku(skuVO, goodsDO.getReturnNum().intValue());

        //商品总价 = 商品单价 x 数量
        double goodsPrice = skuVO.getPrice() * goodsDO.getReturnNum();
        //商品总重 = 商品重量 x 数量
        double goodsWeight = skuVO.getWeight() * goodsDO.getReturnNum();

        //获取原订单中的订单item信息
        List<OrderSkuVO> skuList = JsonUtil.jsonToList(orderDO.getItemsJson(), OrderSkuVO.class);

        List<OrderSkuVO> newSkuList = new ArrayList<>();

        for (OrderSkuVO orderSkuVO : skuList) {
            if (orderSkuVO.getSkuId().intValue() == goodsDO.getSkuId().intValue()) {
                orderSkuVO.setNum(goodsDO.getReturnNum());
                orderSkuVO.setSnapshotId(null);
                orderSkuVO.setServiceStatus(OrderServiceStatusEnum.NOT_APPLY.value());
                orderSkuVO.setSingleList(new ArrayList<>());
                orderSkuVO.setGroupList(new ArrayList<>());
                orderSkuVO.setGoodsOperateAllowableVO(null);
                orderSkuVO.setPromotionTags(new ArrayList<>());
                orderSkuVO.setPurchaseNum(0);
                orderSkuVO.setActualPayTotal(0.00);
                newSkuList.add(orderSkuVO);
            }
        }

        if (newSkuList.size() == 0) {
            throw new ServiceException(AftersaleErrorCode.E604.name(), "自动生成订单出错：申请售后服务的商品与订单购买的商品不一致");
        }

        //生成交易单号
        String tradeSn = tradeSnCreator.generateTradeSn();
        //交易单信息入库
        this.tradeIntoDB(applyAfterSaleVO, orderDO, goodsPrice, tradeSn);

        //生成订单编号
        String orderSn = tradeSnCreator.generateOrderSn();
        //订单信息入库
        OrderDO order = this.orderIntoDB(applyAfterSaleVO, orderDO, goodsPrice, goodsWeight, newSkuList, tradeSn, orderSn);

        OrderStatusChangeMsg message = new OrderStatusChangeMsg();
        message.setOrderDO(order);
        message.setOldStatus(OrderStatusEnum.valueOf(order.getOrderStatus()));
        message.setNewStatus(OrderStatusEnum.valueOf(order.getOrderStatus()));
        //发送商家创建订单消息
        this.amqpTemplate.convertAndSend(AmqpExchange.AS_SELLER_CREATE_ORDER, "order-change-queue", message);

        //订单项信息入库
        this.orderItemIntoDB(newSkuList, tradeSn, orderSn, applyAfterSaleVO.getOrderSn(), goodsDO.getSkuId());

        //初始化订单出库状态
        this.orderOutStatusIntoDB(orderSn);

        //更新商品库存
        this.updateSkuQuantity(goodsDO);

        //创建订单日志
        this.orderLogIntoDB(applyAfterSaleVO, orderSn);

        //更新售后服务单的新订单号并将售后服务单状态恢复为审核通过
        this.afterSaleManager.setServiceNewOrderSn(applyAfterSaleVO.getSn(), orderSn);

        //创建售后日志
        this.afterSaleLogManager.add(applyAfterSaleVO.getSn(), "已成功生成新的订单，订单编号为" + orderDO.getSn() + "，可进入订单列表中查看。", operator);

        //如果用户申请的售后服务类型为补发商品，那么在商家审核通过并生成新的订单后，售后服务就结束了，因此要将售后服务单状态改为已完成
        if (ServiceTypeEnum.SUPPLY_AGAIN_GOODS.value().equals(applyAfterSaleVO.getServiceType())) {
            this.afterSaleManager.updateServiceStatus(applyAfterSaleVO.getSn(), ServiceStatusEnum.COMPLETED.value());

            //新增售后日志
            this.afterSaleLogManager.add(applyAfterSaleVO.getSn(), "当前售后服务已完成。", operator);

            //发送售后服务已完成的消息
            AfterSaleChangeMessage afterSaleChangeMessage = new AfterSaleChangeMessage(applyAfterSaleVO.getSn(), ServiceTypeEnum.SUPPLY_AGAIN_GOODS, ServiceStatusEnum.COMPLETED);
            amqpTemplate.convertAndSend(AmqpExchange.AS_STATUS_CHANGE, AmqpExchange.AS_STATUS_CHANGE + "_QUEUE", afterSaleChangeMessage);
        }

        return order;
    }

    /**
     * 校验申请售后的商品信息
     * @param skuVO 商品skuvo
     * @param returnNum 申请售后数量
     */
    private void checkSku(GoodsSkuVO skuVO, int returnNum) {
        if (skuVO == null) {
            throw new ServiceException(AftersaleErrorCode.E617.name(), "申请售后的商品信息不存在，无法生成新的订单");
        }

        //检测商品的上下架状态
        if (skuVO.getMarketEnable() != null && skuVO.getMarketEnable().intValue() != 1) {
            throw new ServiceException(AftersaleErrorCode.E617.name(), "商品已被商家下架，无法生成新的订单");
        }

        //检测商品的删除状态
        if (skuVO.getDisabled() != null && skuVO.getDisabled().intValue() != 1) {
            throw new ServiceException(AftersaleErrorCode.E617.name(), "商品已被商家删除，无法生成新的订单");
        }

        //检测商品的审核状态
        CacheGoods goodsVo = this.goodsClient.getFromCache(skuVO.getGoodsId());
        if (goodsVo.getIsAuth() == 0 || goodsVo.getIsAuth() == 3) {
            throw new ServiceException(AftersaleErrorCode.E617.name(), "商品还未通过平台审核，无法生成新的订单");
        }

        //判断当前售后申请的换货数量或者补发数量是否大于商品的可用库存
        if (returnNum > skuVO.getEnableQuantity().intValue()) {
            throw new ServiceException(GoodsErrorCode.E307.code(), "当前申请售后服务的商品库存不足，无法生成新的订单");
        }
    }

    /**
     * 交易单信息入库
     * @param applyAfterSaleVO 售后服务信息
     * @param orderDO 原订单信息
     * @param goodsPrice 商品价格
     * @param tradeSn 交易单号
     */
    private TradeDO tradeIntoDB(ApplyAfterSaleVO applyAfterSaleVO, OrderDO orderDO, double goodsPrice, String tradeSn) {
        //填充交易单信息
        TradeDO tradeDO = new TradeDO();
        tradeDO.setTradeSn(tradeSn);
        tradeDO.setMemberId(applyAfterSaleVO.getMemberId());
        tradeDO.setMemberName(applyAfterSaleVO.getMemberName());
        tradeDO.setPaymentMethodId(orderDO.getPaymentMethodId());
        tradeDO.setPaymentPluginId(orderDO.getPaymentPluginId());
        tradeDO.setPaymentMethodName(orderDO.getPaymentMethodName());
        tradeDO.setPaymentType(PaymentTypeEnum.ONLINE.value());
        tradeDO.setTotalPrice(0.00);
        tradeDO.setGoodsPrice(goodsPrice);
        tradeDO.setFreightPrice(0.00);
        tradeDO.setDiscountPrice(0.00);
        tradeDO.setConsigneeName(applyAfterSaleVO.getChangeInfo().getShipName());
        tradeDO.setConsigneeProvinceId(applyAfterSaleVO.getChangeInfo().getProvinceId());
        tradeDO.setConsigneeProvince(applyAfterSaleVO.getChangeInfo().getProvince());
        tradeDO.setConsigneeCityId(applyAfterSaleVO.getChangeInfo().getCityId());
        tradeDO.setConsigneeCity(applyAfterSaleVO.getChangeInfo().getCity());
        tradeDO.setConsigneeCountyId(applyAfterSaleVO.getChangeInfo().getCountyId());
        tradeDO.setConsigneeCounty(applyAfterSaleVO.getChangeInfo().getCounty());
        tradeDO.setConsigneeTownId(applyAfterSaleVO.getChangeInfo().getTownId());
        tradeDO.setConsigneeTown(applyAfterSaleVO.getChangeInfo().getTown());
        tradeDO.setConsigneeAddress(applyAfterSaleVO.getChangeInfo().getShipAddr());
        tradeDO.setConsigneeMobile(applyAfterSaleVO.getChangeInfo().getShipMobile());
        tradeDO.setCreateTime(DateUtil.getDateline());
        tradeDO.setTradeStatus(TradeStatusEnum.PAID_OFF.value());
        this.daoSupport.insert(tradeDO);
        return tradeDO;
    }

    /**
     * 订单信息入库
     * @param applyAfterSaleVO 售后服务单信息
     * @param orderDO 原订单信息
     * @param goodsPrice 商品价格
     * @param goodsWeight 商品重量
     * @param newSkuList 商品SKU信息集合
     * @param tradeSn 交易单号
     * @param orderSn 订单编号
     */
    private OrderDO orderIntoDB(ApplyAfterSaleVO applyAfterSaleVO, OrderDO orderDO, double goodsPrice, double goodsWeight, List<OrderSkuVO> newSkuList, String tradeSn, String orderSn) {
        //填充订单信息
        OrderDO order = new OrderDO();
        order.setSn(orderSn);
        order.setTradeSn(tradeSn);
        order.setSellerId(applyAfterSaleVO.getSellerId());
        order.setSellerName(applyAfterSaleVO.getSellerName());
        order.setMemberId(applyAfterSaleVO.getMemberId());
        order.setMemberName(applyAfterSaleVO.getMemberName());
        order.setOrderStatus(OrderStatusEnum.PAID_OFF.value());
        order.setPayStatus(PayStatusEnum.PAY_YES.value());
        order.setShipStatus(ShipStatusEnum.SHIP_NO.value());
        order.setShippingId(0);
        order.setCommentStatus(CommentStatusEnum.UNFINISHED.value());
        order.setPaymentPluginId(orderDO.getPaymentPluginId());
        order.setPaymentMethodName(orderDO.getPaymentMethodName());
        order.setPaymentType(PaymentTypeEnum.ONLINE.value());
        order.setPaymentTime(DateUtil.getDateline());
        order.setPayMoney(0.00);
        order.setShipName(applyAfterSaleVO.getChangeInfo().getShipName());
        order.setShipAddr(applyAfterSaleVO.getChangeInfo().getShipAddr());
        order.setShipMobile(applyAfterSaleVO.getChangeInfo().getShipMobile());
        order.setReceiveTime(orderDO.getReceiveTime());
        order.setShipProvinceId(applyAfterSaleVO.getChangeInfo().getProvinceId());
        order.setShipProvince(applyAfterSaleVO.getChangeInfo().getProvince());
        order.setShipCityId(applyAfterSaleVO.getChangeInfo().getCityId());
        order.setShipCity(applyAfterSaleVO.getChangeInfo().getCity());
        order.setShipCountyId(applyAfterSaleVO.getChangeInfo().getCountyId());
        order.setShipCounty(applyAfterSaleVO.getChangeInfo().getCounty());
        order.setShipTownId(applyAfterSaleVO.getChangeInfo().getTownId());
        order.setShipTown(applyAfterSaleVO.getChangeInfo().getTown());
        order.setOrderPrice(0.00);
        order.setGoodsPrice(goodsPrice);
        order.setShippingPrice(0.00);
        order.setDiscountPrice(0.00);
        order.setDisabled(0);
        order.setWeight(goodsWeight);
        order.setItemsJson(JsonUtil.objectToJson(newSkuList));
        order.setNeedPayMoney(0.00);
        order.setCreateTime(DateUtil.getDateline());
        order.setServiceStatus(OrderServiceStatusEnum.NOT_APPLY.value());
        order.setClientType(ClientType.PC.value());
        order.setNeedReceipt(0);
        order.setOrderType(ServiceTypeEnum.CHANGE_GOODS.value().equals(applyAfterSaleVO.getServiceType()) ? OrderTypeEnum.CHANGE.name() : OrderTypeEnum.SUPPLY_AGAIN.name());
        this.daoSupport.insert(order);
        order.setOrderId(this.daoSupport.getLastId("es_order"));
        return order;
    }

    /**
     * 订单项信息入库
     * @param newSkuList 商品SKU信息集合
     * @param tradeSn 交易单号
     * @param newOrderSn 新订单编号
     * @param oldOrderSn 原订单编号
     * @param skuId 申请售后的商品skuID
     */
    private void orderItemIntoDB(List<OrderSkuVO> newSkuList, String tradeSn, String newOrderSn, String oldOrderSn, Integer skuId) {
        //获取原订单项信息
        OrderItemsDO oldItem = this.getOrderItem(oldOrderSn, skuId);
        if (oldItem == null) {
            throw new ServiceException(AftersaleErrorCode.E604.name(), "自动生成订单出错：原订单信息不存在");
        }

        //订单项入库
        for (OrderSkuVO skuVO : newSkuList) {
            OrderItemsDO item = new OrderItemsDO();
            item.setGoodsId(skuVO.getGoodsId());
            item.setProductId(skuVO.getSkuId());
            item.setNum(skuVO.getNum());
            item.setOrderSn(newOrderSn);
            item.setTradeSn(tradeSn);
            item.setImage(skuVO.getGoodsImage());
            item.setName(skuVO.getName());
            item.setPrice(skuVO.getOriginalPrice());
            item.setCatId(skuVO.getCatId());
            item.setSpecJson(JsonUtil.objectToJson(skuVO.getSpecList()));
            item.setRefundPrice(oldItem.getRefundPrice());
            item.setCommentStatus(CommentStatusEnum.UNFINISHED.value());
            this.daoSupport.insert(item);
        }
    }

    /**
     * 初始化订单出库状态
     * @param orderSn 订单编号
     */
    private void orderOutStatusIntoDB(String orderSn) {
        //初始化订单出库状态
        for (String type : OrderOutTypeEnum.getAll()) {
            OrderOutStatus orderOutStatus = new OrderOutStatus();
            orderOutStatus.setOrderSn(orderSn);
            orderOutStatus.setOutType(type);
            orderOutStatus.setOutStatus(OrderOutStatusEnum.WAIT.name());
            this.orderOutStatusManager.add(orderOutStatus);
        }
    }

    /**
     * 更新商品库存
     * @param goodsDO 申请售后服务的商品信息
     */
    private void updateSkuQuantity(AfterSaleGoodsDO goodsDO) {
        //要更新的库存列表
        List<GoodsQuantityVO> stockList = new ArrayList<>();
        //可用库存
        GoodsQuantityVO enableQuantityVO = new GoodsQuantityVO();
        //设置要减去的库存
        enableQuantityVO.setQuantity(0 - goodsDO.getReturnNum());
        enableQuantityVO.setGoodsId(goodsDO.getGoodsId());
        enableQuantityVO.setQuantityType(QuantityType.enable);
        enableQuantityVO.setSkuId(goodsDO.getSkuId());

        stockList.add(enableQuantityVO);

        //更新库存
        this.goodsQuantityClient.updateSkuQuantity(stockList);

    }

    /**
     * 添加订单日志
     * @param applyAfterSaleVO 售后服务单信息
     * @param orderSn 订单编号
     */
    private void orderLogIntoDB(ApplyAfterSaleVO applyAfterSaleVO, String orderSn) {
        //记录订单日志
        OrderLogDO logDO = new OrderLogDO();
        logDO.setOrderSn(orderSn);
        if (ServiceTypeEnum.CHANGE_GOODS.value().equals(applyAfterSaleVO.getServiceType())) {
            logDO.setMessage("用户申请换货售后服务，商家审核通过生成新订单");
        } else {
            logDO.setMessage("用户申请补发商品售后服务，商家审核通过生成新订单");
        }

        logDO.setOpName("系统");
        logDO.setOpTime(DateUtil.getDateline());
        this.orderLogManager.add(logDO);
    }

    /**
     * 根据订单编号和商品skuID获取一条订单项信息
     * @param orderSn 订单编号
     * @param skuId 商品skuID
     * @return
     */
    private OrderItemsDO getOrderItem(String orderSn, Integer skuId) {
        String sql = "select * from es_order_items where order_sn = ? and product_id = ?";
        return this.daoSupport.queryForObject(sql, OrderItemsDO.class, orderSn, skuId);
    }

}
