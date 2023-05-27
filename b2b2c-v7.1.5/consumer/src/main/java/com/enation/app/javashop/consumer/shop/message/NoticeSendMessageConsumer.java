package com.enation.app.javashop.consumer.shop.message;

import com.enation.app.javashop.consumer.core.event.*;
import com.enation.app.javashop.core.aftersale.model.enums.CreateChannelEnum;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceStatusEnum;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceTypeEnum;
import com.enation.app.javashop.core.aftersale.model.vo.ApplyAfterSaleVO;
import com.enation.app.javashop.core.aftersale.service.AfterSaleQueryManager;
import com.enation.app.javashop.core.base.message.*;
import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.client.member.MemberClient;
import com.enation.app.javashop.core.client.member.MemberNoticeLogClient;
import com.enation.app.javashop.core.client.member.ShopNoticeLogClient;
import com.enation.app.javashop.core.client.system.MessageTemplateClient;
import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.model.dos.MemberAsk;
import com.enation.app.javashop.core.member.model.vo.MemberLoginMsg;
import com.enation.app.javashop.core.shop.model.dos.ShopNoticeLogDO;
import com.enation.app.javashop.core.shop.model.enums.ShopNoticeTypeEnum;
import com.enation.app.javashop.core.system.enums.MessageCodeEnum;
import com.enation.app.javashop.core.system.enums.MessageOpenStatusEnum;
import com.enation.app.javashop.core.system.model.dos.MessageTemplateDO;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.dto.OrderDTO;
import com.enation.app.javashop.core.trade.order.model.enums.OrderStatusEnum;
import com.enation.app.javashop.core.trade.order.model.enums.OrderTypeEnum;
import com.enation.app.javashop.core.trade.order.model.vo.TradeVO;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.apache.commons.lang.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zjp
 * @version v7.0
 * @Description 消息模版发送站内信
 * @ClassName NoticeSendMessageConsumer
 * @since v7.0 上午11:43 2018/7/9
 */
@Component
public class NoticeSendMessageConsumer implements OrderStatusChangeEvent, GoodsChangeEvent, MemberLoginEvent, MemberRegisterEvent, TradeIntoDbEvent, GoodsCommentEvent, MemberAskSendMessageEvent, AfterSaleChangeEvent {

    @Autowired
    private ShopNoticeLogClient shopNoticeLogClient;

    @Autowired
    private MemberNoticeLogClient memberNoticeLogClient;

    @Autowired
    private MessageTemplateClient messageTemplateClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private MemberClient memberClient;

    @Autowired
    private AfterSaleQueryManager afterSaleQueryManager;

    private void sendShopNotice(ShopNoticeLogDO shopNoticeLogDO) {
        shopNoticeLogDO.setIsDelete(0);
        shopNoticeLogDO.setIsRead(0);
        shopNoticeLogDO.setSendTime(DateUtil.getDateline());
        shopNoticeLogClient.add(shopNoticeLogDO);
    }

    private void sendMemberNotice(String content, long sendTime, Integer memberId) {
        memberNoticeLogClient.add(content, sendTime, memberId, "");
    }

    @Override
    public void orderChange(OrderStatusChangeMsg orderMessage) {

        OrderDO orderDO = orderMessage.getOrderDO();

        ShopNoticeLogDO shopNoticeLogDO = new ShopNoticeLogDO();

        MessageTemplateDO messageTemplate = null;

        //订单支付提醒
        if (orderMessage.getNewStatus().name().equals(OrderStatusEnum.PAID_OFF.name())
                && !OrderTypeEnum.SUPPLY_AGAIN.name().equals(orderDO.getOrderType()) && !OrderTypeEnum.CHANGE.name().equals(orderDO.getOrderType())) {
            Map<String, Object> valuesMap = new HashMap<String, Object>(4);
            valuesMap.put("ordersSn", orderDO.getSn());
            valuesMap.put("paymentTime", DateUtil.toString(orderDO.getPaymentTime(), "yyyy-MM-dd"));

            // 店铺订单支付提醒
            messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.SHOPORDERSPAY);
            // 判断是否开启
            if (messageTemplate.getNoticeState().equals(MessageOpenStatusEnum.OPEN.value())) {
                shopNoticeLogDO.setShopId(orderDO.getSellerId());
                shopNoticeLogDO.setType(ShopNoticeTypeEnum.ORDER.value());
                shopNoticeLogDO.setNoticeContent(this.replaceContent(messageTemplate.getContent(), valuesMap));
                this.sendShopNotice(shopNoticeLogDO);
            }

            // 会员订单支付提醒
            messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.MEMBERORDERSPAY);
            if (messageTemplate != null) {
                // 判断是否开启
                if (messageTemplate.getNoticeState().equals(MessageOpenStatusEnum.OPEN.value())) {
                    sendMemberNotice(this.replaceContent(messageTemplate.getContent(), valuesMap), DateUtil.getDateline(), orderDO.getMemberId());
                }
            }
        }

        //订单收货提醒
        if (orderMessage.getNewStatus().name().equals(OrderStatusEnum.ROG.name())) {

            Map<String, Object> valuesMap = new HashMap<String, Object>(4);
            valuesMap.put("ordersSn", orderDO.getSn());
            valuesMap.put("finishTime", DateUtil.toString(DateUtil.getDateline(), "yyyy-MM-dd"));

            // 店铺订单收货提醒
            messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.SHOPORDERSRECEIVE);
            if (messageTemplate != null) {
                if (messageTemplate.getNoticeState().equals(MessageOpenStatusEnum.OPEN.value())) {
                    shopNoticeLogDO.setNoticeContent(this.replaceContent(messageTemplate.getContent(), valuesMap));
                    shopNoticeLogDO.setShopId(orderDO.getSellerId());
                    shopNoticeLogDO.setType(ShopNoticeTypeEnum.ORDER.value());
                    this.sendShopNotice(shopNoticeLogDO);
                }
            }
            //会员订单收货提醒
            messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.MEMBERORDERSRECEIVE);
            if (messageTemplate != null) {
                if (messageTemplate.getNoticeState().equals(MessageOpenStatusEnum.OPEN.value())) {
                    sendMemberNotice(this.replaceContent(messageTemplate.getContent(), valuesMap), DateUtil.getDateline(), orderDO.getMemberId());
                }
            }
        }

        //订单取消提醒
        if (orderMessage.getNewStatus().name().equals(OrderStatusEnum.CANCELLED.name())) {

            Map<String, Object> valuesMap = new HashMap<String, Object>(4);
            valuesMap.put("ordersSn", orderDO.getSn());
            valuesMap.put("cancelTime", DateUtil.toString(DateUtil.getDateline(), "yyyy-MM-dd"));

            // 发送会员消息
            messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.MEMBERORDERSCANCEL);
            if (messageTemplate != null) {

                // 判断是否开启
                if (messageTemplate.getNoticeState().equals(MessageOpenStatusEnum.OPEN.value())) {
                    sendMemberNotice(this.replaceContent(messageTemplate.getContent(), valuesMap), DateUtil.getDateline(), orderDO.getMemberId());
                }
            }

            // 发送店铺消息
            messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.SHOPORDERSCANCEL);
            if (messageTemplate != null) {
                // 判断是否开启
                if (messageTemplate.getNoticeState().equals(MessageOpenStatusEnum.OPEN.value())) {
                    shopNoticeLogDO.setShopId(orderDO.getSellerId());
                    shopNoticeLogDO.setNoticeContent(this.replaceContent(messageTemplate.getContent(), valuesMap));
                    shopNoticeLogDO.setType(ShopNoticeTypeEnum.ORDER.value());
                    this.sendShopNotice(shopNoticeLogDO);
                }
            }
        }

        //订单发货提醒
        if (orderMessage.getNewStatus().name().equals(OrderStatusEnum.SHIPPED.name())) {
            // 会员消息发送
            messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.MEMBERORDERSSEND);
            if (messageTemplate != null) {
                // 判断是否开启
                if (messageTemplate.getNoticeState().equals(MessageOpenStatusEnum.OPEN.value())) {
                    Map<String, Object> valuesMap = new HashMap<String, Object>(4);
                    valuesMap.put("ordersSn", orderDO.getSn());
                    valuesMap.put("shipSn", orderDO.getShipNo());
                    valuesMap.put("sendTime", DateUtil.toString(DateUtil.getDateline(), "yyyy-MM-dd"));
                    sendMemberNotice(this.replaceContent(messageTemplate.getContent(), valuesMap), DateUtil.getDateline(), orderDO.getMemberId());
                }
            }
        }
    }

    /**
     * 发送售后服务站内消息
     * @param afterSaleChangeMessage
     */
    @Override
    public void afterSaleChange(AfterSaleChangeMessage afterSaleChangeMessage) {
        //获取售后服务单详细信息
        String serviceSn = afterSaleChangeMessage.getServiceSn();
        ApplyAfterSaleVO applyAfterSaleVO = this.afterSaleQueryManager.detail(serviceSn);

        //获取售后服务单状态
        ServiceStatusEnum serviceStatus = afterSaleChangeMessage.getServiceStatus();
        //获取售后服务类型
        ServiceTypeEnum serviceType = afterSaleChangeMessage.getServiceType();

        boolean isShopMsg = true;
        MessageTemplateDO messageTemplate = null;
        Map<String, Object> valuesMap = new HashMap<String, Object>(3);

        //如果是用户申请售后服务
        if (ServiceStatusEnum.APPLY.equals(serviceStatus)) {

            isShopMsg = true;

            messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.SHOPAFTERSALE);

            valuesMap.put("orderSn", applyAfterSaleVO.getOrderSn());
            valuesMap.put("serviceSn", serviceSn);
            valuesMap.put("serviceType", ServiceTypeEnum.valueOf(serviceType.value()).description());

            //如果售后服务被商家审核通过
        } else if (ServiceStatusEnum.PASS.equals(serviceStatus)) {

            isShopMsg = false;

            if (ServiceTypeEnum.ORDER_CANCEL.equals(serviceType) || ServiceTypeEnum.SUPPLY_AGAIN_GOODS.equals(serviceType)) {
                messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.MEMBERASAUDIT);

                valuesMap.put("serviceSn", serviceSn);
                valuesMap.put("serviceType", ServiceTypeEnum.valueOf(serviceType.value()).description());
                valuesMap.put("auditStatus", ServiceStatusEnum.valueOf(serviceStatus.value()).description());
            } else {
                messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.MEMBERASAUDITRETURNCHANGE);

                valuesMap.put("serviceSn", serviceSn);
                valuesMap.put("serviceType", ServiceTypeEnum.valueOf(serviceType.value()).description());
                valuesMap.put("returnAddr", applyAfterSaleVO.getReturnAddr());
            }

            //如果售后服务被商家审核拒绝
        } else if (ServiceStatusEnum.REFUSE.equals(serviceStatus)) {
            isShopMsg = false;

            messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.MEMBERASAUDIT);

            valuesMap.put("serviceSn", serviceSn);
            valuesMap.put("serviceType", ServiceTypeEnum.valueOf(serviceType.value()).description());
            valuesMap.put("auditStatus", ServiceStatusEnum.valueOf(serviceStatus.value()).description());

            //如果是会员填写物流发货信息
        } else if (ServiceStatusEnum.FULL_COURIER.equals(serviceStatus)) {
            isShopMsg = true;

            messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.SHOPAFTERSALEGOODSSHIP);

            valuesMap.put("serviceSn", serviceSn);
            valuesMap.put("serviceType", ServiceTypeEnum.valueOf(serviceType.value()).description());
            valuesMap.put("courierNumber", applyAfterSaleVO.getExpressInfo().getCourierNumber());

            //如果售后服务单状态为待人工处理或退款中或退款失败
        } else if (ServiceStatusEnum.WAIT_FOR_MANUAL.equals(serviceStatus) || ServiceStatusEnum.REFUNDING.equals(serviceStatus) || ServiceStatusEnum.REFUNDFAIL.equals(serviceStatus)) {
            isShopMsg = false;

            messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.MEMBERREFUNDUPDATE);

            valuesMap.put("serviceSn", serviceSn);
            valuesMap.put("serviceType", ServiceTypeEnum.valueOf(serviceType.value()).description());

            //如果售后服务已完成
        } else if (ServiceStatusEnum.COMPLETED.equals(serviceStatus)) {
            isShopMsg = false;

            //判断售后服务单的创建渠道
            if (CreateChannelEnum.PINTUAN.value().equals(applyAfterSaleVO.getCreateChannel())) {
                messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.PINTUANORDERAUTOCANCEL);

                valuesMap.put("orderSn", applyAfterSaleVO.getOrderSn());
            } else {
                messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.MEMBERASCOMPLETED);

                valuesMap.put("serviceSn", serviceSn);
                valuesMap.put("serviceType", ServiceTypeEnum.valueOf(serviceType.value()).description());
            }

            //如果售后服务已关闭
        } else if (ServiceStatusEnum.CLOSED.equals(serviceStatus)) {
            isShopMsg = false;

            messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.MEMBERASCLOSED);

            valuesMap.put("serviceSn", serviceSn);
            valuesMap.put("serviceType", ServiceTypeEnum.valueOf(serviceType.value()).description());
        }

        //判断消息模板类型是店铺模板还是会员模板
        if (isShopMsg) {
            //如果消息模板不为空
            if (messageTemplate != null) {
                // 判断消息模板是否开启
                if (messageTemplate.getNoticeState().equals(MessageOpenStatusEnum.OPEN.value())) {
                    ShopNoticeLogDO shopNoticeLogDO = new ShopNoticeLogDO();
                    shopNoticeLogDO.setShopId(applyAfterSaleVO.getSellerId());
                    shopNoticeLogDO.setNoticeContent(this.replaceContent(messageTemplate.getContent(), valuesMap));
                    shopNoticeLogDO.setType(ShopNoticeTypeEnum.AFTERSALE.value());
                    this.sendShopNotice(shopNoticeLogDO);
                }
            }
        } else {
            //如果消息模板不为空
            if (messageTemplate != null) {
                // 判断消息模板是否开启
                if (messageTemplate.getNoticeState().equals(MessageOpenStatusEnum.OPEN.value())) {
                    sendMemberNotice(this.replaceContent(messageTemplate.getContent(), valuesMap), DateUtil.getDateline(), applyAfterSaleVO.getMemberId());
                }
            }
        }
    }

    @Override
    public void goodsChange(GoodsChangeMsg goodsChangeMsg) {
        ShopNoticeLogDO shopNoticeLogDO = new ShopNoticeLogDO();
        //商品审核失败提醒
        if (GoodsChangeMsg.GOODS_VERIFY_FAIL == goodsChangeMsg.getOperationType()) {
            //发送店铺消息
            for (Integer goodsId : goodsChangeMsg.getGoodsIds()) {

                CacheGoods goods = goodsClient.getFromCache(goodsId);

                // 记录店铺订单取消信息（商家中心查看）
                MessageTemplateDO messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.SHOPGOODSVERIFY);
                if (messageTemplate != null) {

                    // 判断是否开启
                    if (messageTemplate.getNoticeState().equals(MessageOpenStatusEnum.OPEN.value())) {

                        Map<String, Object> valuesMap = new HashMap<String, Object>(2);
                        valuesMap.put("name", goods.getGoodsName());
                        valuesMap.put("message", goodsChangeMsg.getMessage());
                        shopNoticeLogDO.setShopId(goods.getSellerId());
                        shopNoticeLogDO.setNoticeContent(this.replaceContent(messageTemplate.getContent(), valuesMap));
                        shopNoticeLogDO.setType(ShopNoticeTypeEnum.GOODS.value());
                        this.sendShopNotice(shopNoticeLogDO);
                    }
                }
            }
        }
        //商品下架消息提醒
        if (GoodsChangeMsg.UNDER_OPERATION == goodsChangeMsg.getOperationType() && !StringUtil.isEmpty(goodsChangeMsg.getMessage())) {
            //发送店铺消息
            for (Integer goodsId : goodsChangeMsg.getGoodsIds()) {

                CacheGoods goods = goodsClient.getFromCache(goodsId);
                // 记录店铺订单取消信息（商家中心查看）
                MessageTemplateDO messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.SHOPGOODSMARKETENABLE);
                if (messageTemplate != null) {

                    // 判断是否开启
                    if (messageTemplate.getNoticeState().equals(MessageOpenStatusEnum.OPEN.value())) {

                        Map<String, Object> valuesMap = new HashMap<String, Object>(2);
                        valuesMap.put("name", goods.getGoodsName());
                        valuesMap.put("reason", goodsChangeMsg.getMessage());
                        shopNoticeLogDO.setShopId(goods.getSellerId());
                        shopNoticeLogDO.setNoticeContent(this.replaceContent(messageTemplate.getContent(), valuesMap));
                        shopNoticeLogDO.setType(ShopNoticeTypeEnum.GOODS.value());
                        this.sendShopNotice(shopNoticeLogDO);
                    }
                }
            }
        }
    }

    @Override
    public void memberLogin(MemberLoginMsg memberLoginMsg) {
        Member member = memberClient.getModel(memberLoginMsg.getMemberId());
        MessageTemplateDO messageTemplate = null;
        // 记录会员登录成功信息（会员中心查看）
        messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.MEMBERLOGINSUCCESS);

        // 判断站内信是否开启
        if (messageTemplate != null) {
            // 判断短信是否开启
            if (messageTemplate.getNoticeState().equals(MessageOpenStatusEnum.OPEN.value())) {
                Map<String, Object> valuesMap = new HashMap<String, Object>(2);
                valuesMap.put("name", member.getUname());
                valuesMap.put("loginTime", DateUtil.toString(DateUtil.getDateline(), "yyyy-MM-dd"));

                //判断是会员登录还是商家登录 1 会员，2 商家
                if (memberLoginMsg.getMemberOrSeller().equals(1)) {
                    sendMemberNotice(this.replaceContent(messageTemplate.getContent(), valuesMap), DateUtil.getDateline(), member.getMemberId());
                } else {
                    ShopNoticeLogDO shopNoticeLogDO = new ShopNoticeLogDO();
                    shopNoticeLogDO.setShopId(member.getShopId());
                    shopNoticeLogDO.setNoticeContent(this.replaceContent(messageTemplate.getContent(), valuesMap));
                    shopNoticeLogDO.setType(ShopNoticeTypeEnum.OTHER.value());
                    this.sendShopNotice(shopNoticeLogDO);
                }

            }
        }
    }


    @Override
    public void memberRegister(MemberRegisterMsg memberRegisterMsg) {
        Member member = memberClient.getModel(memberRegisterMsg.getMember().getMemberId());
        //会员注册成功提醒
        MessageTemplateDO messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.MEMBERREGISTESUCCESS);
        if (messageTemplate != null) {
            // 判断是否开启
            if (messageTemplate.getNoticeState().equals(MessageOpenStatusEnum.OPEN.value())) {
                Map<String, Object> valuesMap = new HashMap<String, Object>(2);
                valuesMap.put("loginTime", DateUtil.toString(DateUtil.getDateline(), "yyyy-MM-dd"));
                valuesMap.put("name", member.getUname());
                sendMemberNotice(this.replaceContent(messageTemplate.getContent(), valuesMap), DateUtil.getDateline(), member.getMemberId());
            }
        }
    }

    @Override
    public void onTradeIntoDb(TradeVO tradeVO) {
        //店铺新订单创建提醒
        ShopNoticeLogDO shopNoticeLogDO = new ShopNoticeLogDO();
        List<OrderDTO> orderList = tradeVO.getOrderList();
        for (OrderDTO orderDTO : orderList) {
            MessageTemplateDO messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.SHOPORDERSNEW);
            // 判断是否开启
            if (messageTemplate.getNoticeState().equals(MessageOpenStatusEnum.OPEN.value())) {
                Map<String, Object> valuesMap = new HashMap<String, Object>(4);
                valuesMap.put("ordersSn", orderDTO.getSn());
                valuesMap.put("createTime", DateUtil.toString(orderDTO.getCreateTime(), "yyyy-MM-dd"));
                shopNoticeLogDO.setShopId(orderDTO.getSellerId());
                shopNoticeLogDO.setType(ShopNoticeTypeEnum.ORDER.value());
                shopNoticeLogDO.setNoticeContent(this.replaceContent(messageTemplate.getContent(), valuesMap));
                this.sendShopNotice(shopNoticeLogDO);
            }

            //会员新订单创建提醒
            MessageTemplateDO messageTemplateDO = messageTemplateClient.getModel(MessageCodeEnum.MEMBERORDERCREATE);
            // 判断是否开启
            if (messageTemplateDO != null) {
                if (MessageOpenStatusEnum.OPEN.value().equals(messageTemplateDO.getNoticeState())) {
                    Map<String, Object> valuesMap = new HashMap<String, Object>(4);
                    valuesMap.put("ordersSn", orderDTO.getSn());
                    valuesMap.put("createTime", DateUtil.toString(orderDTO.getCreateTime(), "yyyy-MM-dd"));
                    sendMemberNotice(this.replaceContent(messageTemplateDO.getContent(), valuesMap), DateUtil.getDateline(), orderDTO.getMemberId());
                }
            }
        }
    }

    @Override
    public void goodsComment(GoodsCommentMsg goodsCommentMsg) {
        if(goodsCommentMsg.getComment() == null || goodsCommentMsg.getComment().isEmpty()){
            return;
        }

        //如果不是系统自动对商品进行评价并且操作类型是添加操作才会发送消息
        if (!goodsCommentMsg.isAutoComment() && GoodsCommentMsg.ADD == goodsCommentMsg.getOperaType()) {
            //商品评价提醒
            ShopNoticeLogDO shopNoticeLogDO = new ShopNoticeLogDO();
            MessageTemplateDO messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.SHOPORDERSEVALUATE);
            // 判断是否开启
            if (messageTemplate.getNoticeState().equals(MessageOpenStatusEnum.OPEN.value())) {

                goodsCommentMsg.getComment().forEach(comment -> {
                    Map<String, Object> valuesMap = new HashMap<String, Object>(4);
                    valuesMap.put("sn", comment.getOrderSn());
                    valuesMap.put("member_name", comment.getMemberName());
                    shopNoticeLogDO.setShopId(comment.getSellerId());
                    shopNoticeLogDO.setType(ShopNoticeTypeEnum.ORDER.value());
                    shopNoticeLogDO.setNoticeContent(this.replaceContent(messageTemplate.getContent(), valuesMap));
                    this.sendShopNotice(shopNoticeLogDO);
                });
            }
        }
    }

    /**
     * 为店铺发送会员商品咨询站内信提醒
     * @param memberAskMessage
     */
    @Override
    public void goodsAsk(MemberAskMessage memberAskMessage) {
        List<MemberAsk> memberAskList = memberAskMessage.getMemberAsks();
        if (memberAskList == null || memberAskList.size() == 0) {
            return;
        }

        //会员商品咨询提醒
        ShopNoticeLogDO shopNoticeLogDO = new ShopNoticeLogDO();
        MessageTemplateDO messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.SHOPGOODSASK);

        if (messageTemplate != null) {
            // 判断是否开启站内信提醒
            if (messageTemplate.getNoticeState().equals(MessageOpenStatusEnum.OPEN.value())) {
                for (MemberAsk memberAsk : memberAskList) {
                    Map<String, Object> valuesMap = new HashMap<String, Object>(4);
                    valuesMap.put("askTime", DateUtil.toString(memberAsk.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                    valuesMap.put("memberName", memberAsk.getMemberName());
                    valuesMap.put("goodsName", memberAsk.getGoodsName());
                    shopNoticeLogDO.setShopId(memberAsk.getSellerId());
                    shopNoticeLogDO.setType(ShopNoticeTypeEnum.ORDER.value());
                    shopNoticeLogDO.setNoticeContent(this.replaceContent(messageTemplate.getContent(), valuesMap));
                    this.sendShopNotice(shopNoticeLogDO);
                }
            }
        }
    }

    /**
     * 替换短信中的内容
     *
     * @param content 短信
     * @param map     替换的文本内容
     * @return
     */
    private String replaceContent(String content, Map map) {
        StrSubstitutor strSubstitutor = new StrSubstitutor(map);
        return strSubstitutor.replace(content);
    }
}
