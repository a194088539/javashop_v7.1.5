package com.enation.app.javashop.consumer.shop.message;

import com.enation.app.javashop.consumer.core.event.AfterSaleChangeEvent;
import com.enation.app.javashop.consumer.core.event.OrderStatusChangeEvent;
import com.enation.app.javashop.consumer.core.event.PintuanSuccessEvent;
import com.enation.app.javashop.core.aftersale.model.dos.AfterSaleGoodsDO;
import com.enation.app.javashop.core.aftersale.model.enums.*;
import com.enation.app.javashop.core.aftersale.model.vo.ApplyAfterSaleVO;
import com.enation.app.javashop.core.aftersale.service.AfterSaleQueryManager;
import com.enation.app.javashop.core.base.message.AfterSaleChangeMessage;
import com.enation.app.javashop.core.base.message.OrderStatusChangeMsg;
import com.enation.app.javashop.core.client.member.ConnectClient;
import com.enation.app.javashop.core.system.enums.WechatMsgTemplateTypeEnum;
import com.enation.app.javashop.core.system.service.WechatMsgTemplateManager;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.enums.OrderStatusEnum;
import com.enation.app.javashop.core.trade.order.model.vo.OrderSkuVO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 微信发送模板消息
 *
 * @author fk
 * @version v7.1.4
 * @since v7.1.4
 * 2019年6月18日 下午3:15:01
 */
@Component
public class WechatMessageConsumer implements OrderStatusChangeEvent, PintuanSuccessEvent, AfterSaleChangeEvent {

    @Autowired
    private WechatMsgTemplateManager wechatMsgTemplateManager;
    @Autowired
    private ConnectClient connectClient;
    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;
    @Autowired
    private AfterSaleQueryManager afterSaleQueryManager;

    /**
     * 订单状态改变
     *
     * @param orderMessage
     */
    @Override
    public void orderChange(OrderStatusChangeMsg orderMessage) {

        OrderStatusEnum status = orderMessage.getNewStatus();
        OrderDO order = orderMessage.getOrderDO();
        //获取会员的openid
        String openId = connectClient.getMemberOpenid(order.getMemberId());
        WechatMsgTemplateTypeEnum noticeEnum = null;
        List<Object> keywords = new ArrayList<>();
        //商品信息
        String itemsJson = order.getItemsJson();
        List<OrderSkuVO> list = JsonUtil.jsonToList(itemsJson, OrderSkuVO.class);
        switch (status) {
            case PAID_OFF:
                //订单支付成功通知
                noticeEnum = WechatMsgTemplateTypeEnum.PAY_NOTICE;
                keywords.add(order.getMemberName());
                keywords.add(order.getSn());
                keywords.add(String.format("%.2f", order.getOrderPrice())+"元");
                //商品信息
                StringBuffer goodsInfo = new StringBuffer();
                for (OrderSkuVO sku : list) {
                    goodsInfo.append(sku.getName() + "*" + sku.getNum() + " ");
                }
                keywords.add(goodsInfo);
                break;
            case SHIPPED:
                //订单发货提醒
                noticeEnum = WechatMsgTemplateTypeEnum.SHIP_NOTICE;
                keywords.add(order.getSn());
                keywords.add(order.getLogiName());
                keywords.add(order.getShipNo());
                break;
            case ROG:
                //确认收货通知
                noticeEnum = WechatMsgTemplateTypeEnum.ROG_NOTICE;
                keywords.add(order.getSn());
                //商品信息
                StringBuffer goodsInfoRog = new StringBuffer();
                for (OrderSkuVO sku : list) {
                    goodsInfoRog.append(sku.getName() + "*" + sku.getNum() + " ");
                }
                keywords.add(goodsInfoRog);
                keywords.add(DateUtil.toString(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                keywords.add(DateUtil.toString(order.getShipTime(), "yyyy-MM-dd HH:mm:ss"));
                keywords.add(DateUtil.toString(order.getSigningTime(), "yyyy-MM-dd HH:mm:ss"));
                break;
            case CANCELLED:
                //订单取消通知
                noticeEnum = WechatMsgTemplateTypeEnum.CANCEL_NOTICE;
                keywords.add(order.getSn());
                //商品信息
                StringBuffer goodsInfoCancel = new StringBuffer();
                for (OrderSkuVO sku : list) {
                    goodsInfoCancel.append(sku.getName() + "*" + sku.getNum() + " ");
                }
                keywords.add(goodsInfoCancel);
                keywords.add(order.getCancelReason());
                break;
            default:
        }
        if (noticeEnum != null) {
            wechatMsgTemplateManager.send(openId, noticeEnum, keywords.toArray());
        }
    }

    /**
     * 售后退货退款通知
     * @param afterSaleChangeMessage
     */
    @Override
    public void afterSaleChange(AfterSaleChangeMessage afterSaleChangeMessage) {
        //获取售后服务单详细信息
        ApplyAfterSaleVO applyAfterSaleVO = this.afterSaleQueryManager.detail(afterSaleChangeMessage.getServiceSn());

        //获取售后服务单状态
        ServiceStatusEnum serviceStatus = afterSaleChangeMessage.getServiceStatus();

        if (ServiceStatusEnum.PASS.equals(serviceStatus)) {
            //获取会员的openid
            String openId = connectClient.getMemberOpenid(applyAfterSaleVO.getMemberId());
            List<Object> keywords = new ArrayList<>();

            keywords.add("审核通过");

            //商品信息
            List<AfterSaleGoodsDO> goodsList = applyAfterSaleVO.getGoodsList();
            StringBuffer goodsInfo = new StringBuffer();
            for (AfterSaleGoodsDO goods : goodsList) {
                goodsInfo.append(goods.getGoodsName() + "*" + goods.getReturnNum() + " ");
            }
            keywords.add(goodsInfo);
            //审核时间
            keywords.add(DateUtil.toString(new Date(), "yyyy-MM-dd HH:mm:ss"));
            //退款通知
            wechatMsgTemplateManager.send(openId, WechatMsgTemplateTypeEnum.REFUND_GOOD_NOTICE, keywords.toArray());
            return;
        }

        //退款通知
        if (ServiceStatusEnum.REFUNDING.equals(serviceStatus)) {

            //获取会员的openid
            String openId = connectClient.getMemberOpenid(applyAfterSaleVO.getMemberId());
            List<Object> keywords = new ArrayList<>();

            //商品信息
            List<AfterSaleGoodsDO> goodsList = applyAfterSaleVO.getGoodsList();
            StringBuffer goodsInfo = new StringBuffer();
            for (AfterSaleGoodsDO goods : goodsList) {
                goodsInfo.append(goods.getGoodsName() + "*" + goods.getReturnNum() + " ");
            }
            keywords.add(goodsInfo);
            //订单号
            keywords.add(applyAfterSaleVO.getOrderSn());
            //退款金额
            keywords.add(applyAfterSaleVO.getRefundInfo().getRefundPrice());
            //退款方式  原路退回或者线下支付
            String refundWay = RefundWayEnum.valueOf(applyAfterSaleVO.getRefundInfo().getRefundWay()).description();

            if (!StringUtil.isEmpty(applyAfterSaleVO.getRefundInfo().getAccountType())) {
                String text = AccountTypeEnum.valueOf(applyAfterSaleVO.getRefundInfo().getAccountType()).description();
                keywords.add(refundWay + "-" + text);
            } else {
                keywords.add("未知");
            }
            //退款通知
            wechatMsgTemplateManager.send(openId, WechatMsgTemplateTypeEnum.RETUND_NOTICE, keywords.toArray());
        }
    }

    /**
     * 发送拼团成功消息
     *
     * @param pintuanOrderId
     */
    @Override
    public void success(Integer pintuanOrderId) {
        //拼团主id查询相关的拼团订单，循环给每个人发送消息
        String sql = "select o.* from es_pintuan_child_order pc inner join es_order o on pc.order_sn = o.sn where pc.order_id = ? ";
        List<OrderDO> orders = this.daoSupport.queryForList(sql, OrderDO.class, pintuanOrderId);
        //查询订单
        for (OrderDO order : orders) {

            String openId = connectClient.getMemberOpenid(order.getMemberId());

            List<Object> keywords = new ArrayList<>();
            //商品信息
            String itemsJson = order.getItemsJson();
            List<OrderSkuVO> list = JsonUtil.jsonToList(itemsJson, OrderSkuVO.class);
            StringBuffer goodsInfo = new StringBuffer();
            for (OrderSkuVO sku : list) {
                goodsInfo.append(sku.getName() + "*" + sku.getNum() + " ");
            }
            keywords.add(goodsInfo);
            keywords.add(order.getSn());
            keywords.add(order.getOrderPrice());
            keywords.add(DateUtil.toString(order.getPaymentTime(), "yyyy-MM-dd HH:mm:ss"));

            wechatMsgTemplateManager.send(openId, WechatMsgTemplateTypeEnum.PINTUAN_NOTICE, keywords.toArray());
        }


    }

}
