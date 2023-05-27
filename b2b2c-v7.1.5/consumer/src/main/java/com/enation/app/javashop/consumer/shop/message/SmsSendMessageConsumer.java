package com.enation.app.javashop.consumer.shop.message;


import com.enation.app.javashop.consumer.core.event.*;
import com.enation.app.javashop.core.aftersale.model.enums.CreateChannelEnum;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceStatusEnum;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceTypeEnum;
import com.enation.app.javashop.core.aftersale.model.vo.ApplyAfterSaleVO;
import com.enation.app.javashop.core.aftersale.service.AfterSaleQueryManager;
import com.enation.app.javashop.core.base.SettingGroup;
import com.enation.app.javashop.core.base.message.*;
import com.enation.app.javashop.core.base.model.vo.SmsSendVO;
import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.client.member.MemberClient;
import com.enation.app.javashop.core.client.member.ShopClient;
import com.enation.app.javashop.core.client.system.MessageTemplateClient;
import com.enation.app.javashop.core.client.system.SettingClient;
import com.enation.app.javashop.core.client.system.SmsClient;
import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.model.dos.MemberAsk;
import com.enation.app.javashop.core.member.model.vo.MemberLoginMsg;
import com.enation.app.javashop.core.shop.model.vo.ShopVO;
import com.enation.app.javashop.core.system.enums.MessageCodeEnum;
import com.enation.app.javashop.core.system.enums.MessageOpenStatusEnum;
import com.enation.app.javashop.core.system.model.dos.MessageTemplateDO;
import com.enation.app.javashop.core.system.model.vo.SiteSetting;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.dto.OrderDTO;
import com.enation.app.javashop.core.trade.order.model.enums.OrderStatusEnum;
import com.enation.app.javashop.core.trade.order.model.enums.OrderTypeEnum;
import com.enation.app.javashop.core.trade.order.model.vo.TradeVO;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.apache.commons.lang.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 消息模版发送短信
 *
 * @author zjp
 * @version v7.0
 * @since v7.0
 * 2018年3月25日 下午3:15:01
 */
@Component
public class SmsSendMessageConsumer implements OrderStatusChangeEvent, GoodsChangeEvent, MemberLoginEvent, MemberRegisterEvent, TradeIntoDbEvent, GoodsCommentEvent, MemberAskSendMessageEvent, AfterSaleChangeEvent {

    @Autowired
    private MessageTemplateClient messageTemplateClient;

    @Autowired
    private SettingClient settingClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private ShopClient shopClient;

    @Autowired
    private MemberClient memberClient;

    @Autowired
    private SmsClient smsClient;

    @Autowired
    private AfterSaleQueryManager afterSaleQueryManager;

    private void sendSms(SmsSendVO smsSendVO) {
        smsClient.send(smsSendVO);
    }


    @Override
    public void orderChange(OrderStatusChangeMsg orderMessage) {
        OrderDO orderDO = orderMessage.getOrderDO();
        String siteSettingJson = settingClient.get(SettingGroup.SITE);
        //获取系统配置
        SiteSetting siteSetting = JsonUtil.jsonToObject(siteSettingJson, SiteSetting.class);
        MessageTemplateDO messageTemplate = null;
        //获取当前下单会员信息
        Member member = memberClient.getModel(orderDO.getMemberId());
        //获取当前店铺所有者的联系方式
        ShopVO shopVO = shopClient.getShop(orderDO.getSellerId());
        //订单支付提醒
        if (orderMessage.getNewStatus().name().equals(OrderStatusEnum.PAID_OFF.name())
                && !OrderTypeEnum.SUPPLY_AGAIN.name().equals(orderDO.getOrderType()) && !OrderTypeEnum.CHANGE.name().equals(orderDO.getOrderType())) {
            Map<String, Object> valuesMap = new HashMap<String, Object>(4);
            valuesMap.put("ordersSn", orderDO.getSn());
            valuesMap.put("paymentTime", DateUtil.toString(orderDO.getPaymentTime(), "yyyy-MM-dd"));
            valuesMap.put("siteName", siteSetting.getSiteName());

            // 店铺订单支付提醒
            messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.SHOPORDERSPAY);
            // 判断短信是否开启
            if (messageTemplate != null) {
                if (messageTemplate.getSmsState().equals(MessageOpenStatusEnum.OPEN.value())) {
                    // 发送短信
                    this.sendSms(this.getSmsMessage(shopVO.getLinkPhone(), this.replaceContent(messageTemplate.getSmsContent(), valuesMap)));
                }
            }
            // 会员订单支付提醒
            messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.MEMBERORDERSPAY);
            if (messageTemplate != null) {
                // 判断短信是否开启
                if (messageTemplate.getSmsState().equals(MessageOpenStatusEnum.OPEN.value())) {
                    this.sendSms(this.getSmsMessage(member.getMobile(), this.replaceContent(messageTemplate.getSmsContent(), valuesMap)));
                }
            }

        }

        //订单收货提醒
        if (orderMessage.getNewStatus().name().equals(OrderStatusEnum.ROG.name())) {
            Map<String, Object> valuesMap = new HashMap<String, Object>(4);
            valuesMap.put("ordersSn", orderDO.getSn());
            valuesMap.put("finishTime", DateUtil.toString(DateUtil.getDateline(), "yyyy-MM-dd"));
            valuesMap.put("siteName", siteSetting.getSiteName());
            // 店铺订单收货提醒
            messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.SHOPORDERSRECEIVE);
            if (messageTemplate != null) {
                // 判断短信是否开启
                if (messageTemplate.getSmsState().equals(MessageOpenStatusEnum.OPEN.value())) {
                    this.sendSms(this.getSmsMessage(shopVO.getLinkPhone(), this.replaceContent(messageTemplate.getSmsContent(), valuesMap)));
                }
            }
            //会员订单收货提醒
            messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.MEMBERORDERSRECEIVE);
            if (messageTemplate != null) {
                // 判断短信是否开启
                if (messageTemplate.getSmsState().equals(MessageOpenStatusEnum.OPEN.value())) {
                    this.sendSms(this.getSmsMessage(member.getMobile(), this.replaceContent(messageTemplate.getSmsContent(), valuesMap)));
                }
            }
        }

        //订单取消提醒
        if (orderMessage.getNewStatus().name().equals(OrderStatusEnum.CANCELLED.name())) {
            Map<String, Object> valuesMap = new HashMap<String, Object>(4);
            valuesMap.put("ordersSn", orderDO.getSn());
            valuesMap.put("cancelTime", DateUtil.toString(DateUtil.getDateline(), "yyyy-MM-dd"));
            valuesMap.put("siteName", siteSetting.getSiteName());
            // 发送会员消息
            messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.MEMBERORDERSCANCEL);
            if (messageTemplate != null) {
                // 判断短信是否开启
                if (messageTemplate.getSmsState().equals(MessageOpenStatusEnum.OPEN.value())) {
                    this.sendSms(this.getSmsMessage(member.getMobile(), this.replaceContent(messageTemplate.getSmsContent(), valuesMap)));
                }
            }

            // 发送店铺消息
            messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.SHOPORDERSCANCEL);
            if (messageTemplate != null) {
                // 判断短信是否开启
                if (messageTemplate.getSmsState().equals(MessageOpenStatusEnum.OPEN.value())) {
                    this.sendSms(this.getSmsMessage(shopVO.getLinkPhone(), this.replaceContent(messageTemplate.getSmsContent(), valuesMap)));
                }
            }
        }

        //订单发货提醒
        if (orderMessage.getNewStatus().name().equals(OrderStatusEnum.SHIPPED.name())) {
            // 会员消息发送
            messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.MEMBERORDERSSEND);
            if (messageTemplate != null) {
                // 判断短信是否开启
                if (messageTemplate.getSmsState().equals(MessageOpenStatusEnum.OPEN.value())) {
                    Map<String, Object> valuesMap = new HashMap<String, Object>(4);
                    valuesMap.put("ordersSn", orderDO.getSn());
                    valuesMap.put("shipSn", orderDO.getShipNo());
                    valuesMap.put("sendTime", DateUtil.toString(DateUtil.getDateline(), "yyyy-MM-dd"));
                    valuesMap.put("siteName", siteSetting.getSiteName());
                    this.sendSms(this.getSmsMessage(member.getMobile(), this.replaceContent(messageTemplate.getSmsContent(), valuesMap)));
                }
            }
        }
    }

    /**
     * 发送售后服务短信消息
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

        String siteSettingJson = settingClient.get(SettingGroup.SITE);
        SiteSetting siteSetting = JsonUtil.jsonToObject(siteSettingJson, SiteSetting.class);
        ShopVO shop = shopClient.getShop(applyAfterSaleVO.getSellerId());
        Member member = memberClient.getModel(applyAfterSaleVO.getMemberId());

        boolean isShopMsg = true;
        MessageTemplateDO messageTemplate = null;
        Map<String, Object> valuesMap = new HashMap<String, Object>(4);
        valuesMap.put("siteName", siteSetting.getSiteName());

        //如果是用户申请售后服务
        if (ServiceStatusEnum.APPLY.equals(serviceStatus)) {

            isShopMsg = true;

            messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.SHOPAFTERSALE);

            valuesMap.put("orderSn", applyAfterSaleVO.getOrderSn());
            valuesMap.put("serviceSn", serviceSn);
            valuesMap.put("serviceType", ServiceTypeEnum.valueOf(serviceType.value()).description());

            //如果售后服务审核通过
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

            //如果售后服务审核未通过
        } else if (ServiceStatusEnum.REFUSE.equals(serviceStatus)) {
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
            //如果消息模板不为空并且商家手机号存在
            if (messageTemplate != null && StringUtil.notEmpty(shop.getLinkPhone())) {
                // 判断消息模板是否开启
                if (messageTemplate.getSmsState().equals(MessageOpenStatusEnum.OPEN.value())) {
                    this.sendSms(this.getSmsMessage(shop.getLinkPhone(), this.replaceContent(messageTemplate.getSmsContent(), valuesMap)));
                }
            }
        } else {
            //如果消息模板不为空并且消息模板已开启
            if (messageTemplate != null && messageTemplate.getSmsState().equals(MessageOpenStatusEnum.OPEN.value())) {

                // 如果会员申请售后服务填写的接收取消结果手机号不为空
                if (StringUtil.notEmpty(applyAfterSaleVO.getMobile())) {
                    this.sendSms(this.getSmsMessage(applyAfterSaleVO.getMobile(), this.replaceContent(messageTemplate.getSmsContent(), valuesMap)));
                }
            }
        }
    }

    @Override
    public void goodsChange(GoodsChangeMsg goodsChangeMsg) {
        SmsSendVO smsSendVO = new SmsSendVO();
        //商品审核失败提醒
        String siteSettingJson = settingClient.get(SettingGroup.SITE);

        SiteSetting siteSetting = JsonUtil.jsonToObject(siteSettingJson, SiteSetting.class);
        if (GoodsChangeMsg.GOODS_VERIFY_FAIL == goodsChangeMsg.getOperationType()) {
            //发送店铺消息
            for (Integer goodsId : goodsChangeMsg.getGoodsIds()) {
                CacheGoods goods = goodsClient.getFromCache(goodsId);
                ShopVO shop = shopClient.getShop(goods.getSellerId());
                smsSendVO.setMobile(shop.getLinkPhone());
                // 记录店铺订单取消信息（商家中心查看）
                MessageTemplateDO messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.SHOPGOODSVERIFY);
                if (messageTemplate != null) {
                    // 判断短信是否开启
                    if (messageTemplate.getSmsState().equals(MessageOpenStatusEnum.OPEN.value())) {
                        Map<String, Object> valuesMap = new HashMap<String, Object>(4);
                        valuesMap.put("siteName", siteSetting.getSiteName());
                        valuesMap.put("name", goods.getGoodsName());
                        valuesMap.put("message", goodsChangeMsg.getMessage());
                        smsSendVO.setContent(this.replaceContent(messageTemplate.getSmsContent(), valuesMap));
                        this.sendSms(smsSendVO);
                    }
                }
            }
        }
        //商品下架消息提醒
        if (GoodsChangeMsg.UNDER_OPERATION == goodsChangeMsg.getOperationType() && !StringUtil.isEmpty(goodsChangeMsg.getMessage())) {
            //发送店铺消息
            for (Integer goodsId : goodsChangeMsg.getGoodsIds()) {
                CacheGoods goods = goodsClient.getFromCache(goodsId);
                ShopVO shop = shopClient.getShop(goods.getSellerId());
                smsSendVO.setMobile(shop.getLinkPhone());
                // 记录店铺订单取消信息（商家中心查看）
                MessageTemplateDO messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.SHOPGOODSMARKETENABLE);
                if (messageTemplate != null) {
                    // 判断短信是否开启
                    if (messageTemplate.getSmsState().equals(MessageOpenStatusEnum.OPEN.value())) {
                        Map<String, Object> valuesMap = new HashMap<String, Object>(4);
                        valuesMap.put("siteName", siteSetting.getSiteName());
                        valuesMap.put("name", goods.getGoodsName());
                        valuesMap.put("reason", goodsChangeMsg.getMessage());
                        smsSendVO.setContent(this.replaceContent(messageTemplate.getSmsContent(), valuesMap));
                        this.sendSms(smsSendVO);
                    }
                }
            }
        }
    }

    @Override
    public void memberLogin(MemberLoginMsg memberLoginMsg) {

        String siteSettingJson = settingClient.get(SettingGroup.SITE);

        SiteSetting siteSetting = JsonUtil.jsonToObject(siteSettingJson, SiteSetting.class);
        Member member = memberClient.getModel(memberLoginMsg.getMemberId());

        SmsSendVO smsSendVO = new SmsSendVO();
        smsSendVO.setMobile(member.getMobile());

        MessageTemplateDO messageTemplate = null;

        // 记录会员登录成功信息（会员中心查看）
        messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.MEMBERLOGINSUCCESS);
        // 判断站内信是否开启
        if (messageTemplate != null) {
            // 判断短信是否开启
            if (messageTemplate.getSmsState().equals(MessageOpenStatusEnum.OPEN.value())) {
                Map<String, Object> valuesMap = new HashMap<String, Object>(4);
                valuesMap.put("name", member.getUname());
                valuesMap.put("loginTime", DateUtil.toString(DateUtil.getDateline(), "yyyy-MM-dd"));
                valuesMap.put("siteName", siteSetting.getSiteName());
                smsSendVO.setContent(this.replaceContent(messageTemplate.getSmsContent(), valuesMap));
                this.sendSms(smsSendVO);
            }
        }
    }

    @Override
    public void memberRegister(MemberRegisterMsg memberRegisterMsg) {
        String siteSettingJson = settingClient.get(SettingGroup.SITE);

        SiteSetting siteSetting = JsonUtil.jsonToObject(siteSettingJson, SiteSetting.class);
        Member member = memberClient.getModel(memberRegisterMsg.getMember().getMemberId());
        SmsSendVO smsSendVO = new SmsSendVO();
        smsSendVO.setMobile(member.getMobile());

        //会员注册成功提醒
        MessageTemplateDO messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.MEMBERREGISTESUCCESS);
        if (messageTemplate != null) {
            // 判断短信是否开启
            if (messageTemplate.getSmsState().equals(MessageOpenStatusEnum.OPEN.value())) {

                Map<String, Object> valuesMap = new HashMap<String, Object>(4);
                valuesMap.put("name", member.getUname());
                valuesMap.put("siteName", siteSetting.getSiteName());
                valuesMap.put("loginTime", DateUtil.toString(DateUtil.getDateline(), "yyyy-MM-dd"));
                smsSendVO.setContent(this.replaceContent(messageTemplate.getSmsContent(), valuesMap));
                this.sendSms(smsSendVO);
            }
        }
    }

    @Override
    public void onTradeIntoDb(TradeVO tradeVO) {
        String siteSettingJson = settingClient.get(SettingGroup.SITE);
        SiteSetting siteSetting = JsonUtil.jsonToObject(siteSettingJson, SiteSetting.class);
        //店铺新订单创建提醒
        List<OrderDTO> orderList = tradeVO.getOrderList();
        SmsSendVO smsSendVO = new SmsSendVO();
        for (OrderDTO orderDTO : orderList) {
            MessageTemplateDO messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.SHOPORDERSNEW);
            // 判断是否开启
            if (messageTemplate.getSmsState().equals(MessageOpenStatusEnum.OPEN.value())) {
                ShopVO shop = shopClient.getShop(orderDTO.getSellerId());
                Map<String, Object> valuesMap = new HashMap<String, Object>(4);
                valuesMap.put("ordersSn", orderDTO.getSn());
                valuesMap.put("createTime", DateUtil.toString(orderDTO.getCreateTime(), "yyyy-MM-dd"));
                valuesMap.put("siteName", siteSetting.getSiteName());
                smsSendVO.setMobile(shop.getLinkPhone());
                smsSendVO.setContent(this.replaceContent(messageTemplate.getSmsContent(), valuesMap));
                this.sendSms(smsSendVO);
            }

            //会员新订单创建提醒
            Member member = memberClient.getModel(orderDTO.getMemberId());
            MessageTemplateDO messageTemplateDO = messageTemplateClient.getModel(MessageCodeEnum.MEMBERORDERCREATE);
            if (messageTemplateDO != null) {
                // 判断短信是否开启
                if (MessageOpenStatusEnum.OPEN.value().equals(messageTemplateDO.getSmsState())) {
                    Map<String, Object> valuesMap = new HashMap<String, Object>(4);
                    valuesMap.put("ordersSn", orderDTO.getSn());
                    valuesMap.put("createTime", DateUtil.toString(orderDTO.getCreateTime(), "yyyy-MM-dd"));
                    valuesMap.put("siteName", siteSetting.getSiteName());
                    this.sendSms(this.getSmsMessage(member.getMobile(), this.replaceContent(messageTemplateDO.getSmsContent(), valuesMap)));
                }
            }
        }
    }

    /**
     * 商品评论
     *
     * @param goodsCommentMsg 商品评论消息
     */
    @Override
    public void goodsComment(GoodsCommentMsg goodsCommentMsg) {
        if(goodsCommentMsg.getComment() == null || goodsCommentMsg.getComment().isEmpty()){
            return;
        }

        //如果不是系统自动对商品进行评价并且操作类型是添加操作才会发送消息
        if (!goodsCommentMsg.isAutoComment() && GoodsCommentMsg.ADD == goodsCommentMsg.getOperaType()) {
            String siteSettingJson = settingClient.get(SettingGroup.SITE);
            SiteSetting siteSetting = JsonUtil.jsonToObject(siteSettingJson, SiteSetting.class);
            //获取坪林的消息模板
            MessageTemplateDO messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.SHOPORDERSEVALUATE);
            if (messageTemplate != null) {
                if (messageTemplate.getSmsState().equals(MessageOpenStatusEnum.OPEN.value())) {
                    goodsCommentMsg.getComment().forEach(comment -> {
                        Map<String, Object> valuesMap = new HashMap<String, Object>(4);
                        valuesMap.put("siteName", siteSetting.getSiteName());
                        valuesMap.put("ordersSn", comment.getSellerId());
                        valuesMap.put("userName", comment.getMemberName());
                        valuesMap.put("evalTime", DateUtil.toString(comment.getCreateTime(), "yyyy-MM-dd"));
                        //获取当前店铺所有者的联系方式
                        ShopVO shopVO = shopClient.getShop(comment.getSellerId());
                        this.sendSms(this.getSmsMessage(shopVO.getLinkPhone(), this.replaceContent(messageTemplate.getSmsContent(), valuesMap)));

                    });
                }
            }
        }
    }

    /**
     * 为店铺发送会员商品咨询短信提醒
     * @param memberAskMessage
     */
    @Override
    public void goodsAsk(MemberAskMessage memberAskMessage) {
        List<MemberAsk> memberAskList = memberAskMessage.getMemberAsks();
        if (memberAskList == null || memberAskList.size() == 0) {
            return;
        }

        String siteSettingJson = settingClient.get(SettingGroup.SITE);
        SiteSetting siteSetting = JsonUtil.jsonToObject(siteSettingJson, SiteSetting.class);
        //获取模板信息
        MessageTemplateDO messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.SHOPGOODSASK);

        if (messageTemplate != null) {
            // 判断是否开启短信提醒
            if (messageTemplate.getSmsState().equals(MessageOpenStatusEnum.OPEN.value())) {
                for (MemberAsk memberAsk : memberAskList) {
                    //获取当前店铺所有者的联系方式
                    ShopVO shopVO = shopClient.getShop(memberAsk.getSellerId());
                    //判断店铺所有者的联系方式是否为空
                    if (StringUtil.notEmpty(shopVO.getLinkPhone())) {
                        Map<String, Object> valuesMap = new HashMap<String, Object>(4);
                        valuesMap.put("askTime", DateUtil.toString(memberAsk.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                        valuesMap.put("memberName", memberAsk.getMemberName());
                        valuesMap.put("goodsName", memberAsk.getGoodsName());
                        valuesMap.put("siteName", siteSetting.getSiteName());
                        this.sendSms(this.getSmsMessage(shopVO.getLinkPhone(), this.replaceContent(messageTemplate.getSmsContent(), valuesMap)));
                    }
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

    /**
     * 组织短信发送的相关信息
     *
     * @param mobile  手机号
     * @param content 内容
     * @return
     */
    private SmsSendVO getSmsMessage(String mobile, String content) {
        SmsSendVO smsSendVO = new SmsSendVO();
        smsSendVO.setMobile(mobile);
        smsSendVO.setContent(content);
        return smsSendVO;
    }


}
