package com.enation.app.javashop.consumer.shop.message;

import com.enation.app.javashop.consumer.core.event.*;
import com.enation.app.javashop.core.aftersale.model.enums.CreateChannelEnum;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceStatusEnum;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceTypeEnum;
import com.enation.app.javashop.core.aftersale.model.vo.ApplyAfterSaleVO;
import com.enation.app.javashop.core.aftersale.service.AfterSaleQueryManager;
import com.enation.app.javashop.core.base.SettingGroup;
import com.enation.app.javashop.core.base.message.*;
import com.enation.app.javashop.core.base.model.vo.EmailVO;
import com.enation.app.javashop.core.base.model.vo.SmsSendVO;
import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.client.member.MemberClient;
import com.enation.app.javashop.core.client.member.ShopClient;
import com.enation.app.javashop.core.client.system.EmailClient;
import com.enation.app.javashop.core.client.system.MessageTemplateClient;
import com.enation.app.javashop.core.client.system.SettingClient;
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
 * 消息模版发送邮件
 *
 * @author zh
 * @version v2.0
 * @since v7.0
 * 2018年3月26日 下午4:45:27
 */
@Component
public class EmailSendMessageConsumer implements OrderStatusChangeEvent, GoodsChangeEvent, MemberLoginEvent, MemberRegisterEvent, TradeIntoDbEvent, GoodsCommentEvent, MemberAskSendMessageEvent, AfterSaleChangeEvent {


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
    private EmailClient emailClient;

    @Autowired
    private AfterSaleQueryManager afterSaleQueryManager;

    public void send(EmailVO emailVO, MessageTemplateDO messageTemplate, Map<String, Object> valuesMap) {
        emailVO.setTitle(messageTemplate.getEmailTitle());
        emailVO.setContent(this.replaceContent(messageTemplate.getEmailContent(), valuesMap));
        emailClient.sendEmail(emailVO);
    }

    @Override
    public void orderChange(OrderStatusChangeMsg orderMessage) {
        OrderDO orderDO = orderMessage.getOrderDO();
        ShopVO shop = shopClient.getShop(orderDO.getSellerId());
        Member member = memberClient.getModel(orderDO.getMemberId());
        String siteSettingJson = settingClient.get(SettingGroup.SITE);

        SiteSetting siteSetting = JsonUtil.jsonToObject(siteSettingJson, SiteSetting.class);

        EmailVO emailVO = new EmailVO();

        MessageTemplateDO messageTemplate = null;

        //订单支付提醒
        if (orderMessage.getNewStatus().name().equals(OrderStatusEnum.PAID_OFF.name())
                && !OrderTypeEnum.SUPPLY_AGAIN.name().equals(orderDO.getOrderType()) && !OrderTypeEnum.CHANGE.name().equals(orderDO.getOrderType())) {
            Map<String, Object> valuesMap = new HashMap<String, Object>(4);
            valuesMap.put("ordersSn", orderDO.getSn());
            valuesMap.put("paymentTime", DateUtil.toString(orderDO.getPaymentTime(), "yyyy-MM-dd"));
            valuesMap.put("siteName", siteSetting.getSiteName());
            // 店铺订单支付提醒
            messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.SHOPORDERSPAY);
            // 判断是否开启
            if (shop.getCompanyEmail() != null && messageTemplate != null) {
                if (messageTemplate.getEmailState().equals(MessageOpenStatusEnum.OPEN.value())) {
                    emailVO.setEmail(shop.getCompanyEmail());
                    this.send(emailVO, messageTemplate, valuesMap);
                }
            }
            // 会员订单支付提醒
            messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.MEMBERORDERSPAY);
            if (member.getEmail() != null && messageTemplate != null) {
                // 判断是否开启
                if (messageTemplate.getEmailState().equals(MessageOpenStatusEnum.OPEN.value())) {
                    emailVO.setEmail(member.getEmail());
                    this.send(emailVO, messageTemplate, valuesMap);
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
            if (shop.getCompanyEmail() != null && messageTemplate != null) {
                // 判断是否开启
                if (messageTemplate.getEmailState().equals(MessageOpenStatusEnum.OPEN.value())) {
                    emailVO.setEmail(shop.getCompanyEmail());
                    this.send(emailVO, messageTemplate, valuesMap);
                }

            }
            //会员订单收货提醒
            messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.MEMBERORDERSRECEIVE);
            if (member.getEmail() != null && messageTemplate != null) {
                // 判断是否开启
                if (messageTemplate.getEmailState().equals(MessageOpenStatusEnum.OPEN.value())) {
                    emailVO.setEmail(member.getEmail());
                    this.send(emailVO, messageTemplate, valuesMap);
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
            if (member.getEmail() != null && messageTemplate != null) {
                // 判断是否开启
                if (messageTemplate.getEmailState().equals(MessageOpenStatusEnum.OPEN.value())) {
                    emailVO.setEmail(member.getEmail());
                    this.send(emailVO, messageTemplate, valuesMap);
                }
            }
            // 发送店铺消息
            messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.SHOPORDERSCANCEL);
            if (shop.getCompanyEmail() != null && messageTemplate != null) {
                // 判断是否开启
                if (messageTemplate.getEmailState().equals(MessageOpenStatusEnum.OPEN.value())) {
                    emailVO.setEmail(shop.getCompanyEmail());
                    this.send(emailVO, messageTemplate, valuesMap);
                }
            }
        }

        //订单发货提醒
        if (orderMessage.getNewStatus().name().equals(OrderStatusEnum.SHIPPED.name())) {
            // 会员消息发送
            messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.MEMBERORDERSSEND);
            if (member.getEmail() != null && messageTemplate != null) {
                // 判断是否开启
                if (messageTemplate.getEmailState().equals(MessageOpenStatusEnum.OPEN.value())) {
                    Map<String, Object> valuesMap = new HashMap<String, Object>(4);
                    valuesMap.put("ordersSn", orderDO.getSn());
                    valuesMap.put("shipSn", orderDO.getShipNo());
                    valuesMap.put("sendTime", DateUtil.toString(DateUtil.getDateline(), "yyyy-MM-dd"));
                    valuesMap.put("siteName", siteSetting.getSiteName());
                    emailVO.setEmail(member.getEmail());
                    this.send(emailVO, messageTemplate, valuesMap);
                }
            }
        }
    }

    /**
     * 发送售后服务电子邮件消息提醒
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

        EmailVO emailVO = new EmailVO();
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
            //如果消息模板不为空并且商家邮箱地址存在
            if (messageTemplate != null && StringUtil.notEmpty(shop.getCompanyEmail())) {
                // 判断消息模板是否开启
                if (messageTemplate.getEmailState().equals(MessageOpenStatusEnum.OPEN.value())) {
                    emailVO.setEmail(shop.getCompanyEmail());
                    this.send(emailVO, messageTemplate, valuesMap);
                }
            }
        } else {
            //如果消息模板不为空并且会员邮箱地址存在
            if (messageTemplate != null && StringUtil.notEmpty(member.getEmail())) {
                // 判断消息模板是否开启
                if (messageTemplate.getEmailState().equals(MessageOpenStatusEnum.OPEN.value())) {
                    emailVO.setEmail(shop.getCompanyEmail());
                    this.send(emailVO, messageTemplate, valuesMap);
                }
            }
        }

    }

    @Override
    public void goodsChange(GoodsChangeMsg goodsChangeMsg) {
        EmailVO emailVO = new EmailVO();
        //商品审核失败提醒
        String siteSettingJson = settingClient.get(SettingGroup.SITE);
        SiteSetting siteSetting = JsonUtil.jsonToObject(siteSettingJson, SiteSetting.class);
        if (GoodsChangeMsg.GOODS_VERIFY_FAIL == goodsChangeMsg.getOperationType()) {
            //发送店铺消息
            for (Integer goodsId : goodsChangeMsg.getGoodsIds()) {
                CacheGoods goods = goodsClient.getFromCache(goodsId);
                ShopVO shop = shopClient.getShop(goods.getSellerId());
                // 记录店铺订单取消信息（商家中心查看）
                MessageTemplateDO messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.SHOPGOODSVERIFY);
                if (shop.getCompanyEmail() != null && messageTemplate != null) {
                    // 判断是否开启
                    if (messageTemplate.getEmailState().equals(MessageOpenStatusEnum.OPEN.value())) {
                        Map<String, Object> valuesMap = new HashMap<String, Object>(4);
                        valuesMap.put("siteName", siteSetting.getSiteName());
                        valuesMap.put("name", goods.getGoodsName());
                        valuesMap.put("message", goodsChangeMsg.getMessage());
                        emailVO.setEmail(shop.getCompanyEmail());
                        this.send(emailVO, messageTemplate, valuesMap);
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
                // 记录店铺订单取消信息（商家中心查看）
                MessageTemplateDO messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.SHOPGOODSMARKETENABLE);
                if (shop.getCompanyEmail() != null && messageTemplate != null) {
                    // 判断是否开启
                    if (messageTemplate.getEmailState().equals(MessageOpenStatusEnum.OPEN.value())) {
                        Map<String, Object> valuesMap = new HashMap<String, Object>(4);
                        valuesMap.put("siteName", siteSetting.getSiteName());
                        valuesMap.put("name", goods.getGoodsName());
                        valuesMap.put("reason", goodsChangeMsg.getMessage());
                        emailVO.setEmail(shop.getCompanyEmail());
                        this.send(emailVO, messageTemplate, valuesMap);
                    }
                }
            }
        }
    }

    @Override
    public void memberLogin(MemberLoginMsg memberLoginMsg) {
        //如果是会员登录
        if (memberLoginMsg.getMemberOrSeller().equals(1)) {
            EmailVO emailVO = new EmailVO();
            String siteSettingJson = settingClient.get(SettingGroup.SITE);

            SiteSetting siteSetting = JsonUtil.jsonToObject(siteSettingJson, SiteSetting.class);
            Member member = memberClient.getModel(memberLoginMsg.getMemberId());

            SmsSendVO smsSendVO = new SmsSendVO();
            smsSendVO.setMobile(member.getMobile());

            MessageTemplateDO messageTemplate = null;

            // 记录会员登录成功信息（会员中心查看）
            messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.MEMBERLOGINSUCCESS);
            // 判断站内信是否开启
            if (member.getEmail() != null && messageTemplate != null) {
                // 判断是否开启
                if (messageTemplate.getEmailState().equals(MessageOpenStatusEnum.OPEN.value())) {
                    Map<String, Object> valuesMap = new HashMap<String, Object>(4);
                    valuesMap.put("name", member.getUname());
                    valuesMap.put("loginTime", DateUtil.toString(DateUtil.getDateline(), "yyyy-MM-dd"));
                    valuesMap.put("siteName", siteSetting.getSiteName());
                    emailVO.setEmail(member.getEmail());
                    this.send(emailVO, messageTemplate, valuesMap);
                }
            }
        }
    }

    @Override
    public void memberRegister(MemberRegisterMsg memberRegisterMsg) {
        String siteSettingJson = settingClient.get(SettingGroup.SITE);
        SiteSetting siteSetting = JsonUtil.jsonToObject(siteSettingJson, SiteSetting.class);
        Member member = memberClient.getModel(memberRegisterMsg.getMember().getMemberId());
        EmailVO emailVO = new EmailVO();
        //会员注册成功提醒
        MessageTemplateDO messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.MEMBERREGISTESUCCESS);
        if (member.getEmail() != null && messageTemplate != null) {
            // 判断是否开启
            if (messageTemplate.getEmailState().equals(MessageOpenStatusEnum.OPEN.value())) {
                Map<String, Object> valuesMap = new HashMap<String, Object>(4);
                valuesMap.put("name", member.getUname());
                valuesMap.put("siteName", siteSetting.getSiteName());
                valuesMap.put("loginTime", DateUtil.toString(DateUtil.getDateline(), "yyyy-MM-dd"));
                emailVO.setEmail(member.getEmail());
                this.send(emailVO, messageTemplate, valuesMap);
            }
        }
    }

    @Override
    public void onTradeIntoDb(TradeVO tradeVO) {
        String siteSettingJson = settingClient.get(SettingGroup.SITE);
        SiteSetting siteSetting = JsonUtil.jsonToObject(siteSettingJson, SiteSetting.class);

        List<OrderDTO> orderList = tradeVO.getOrderList();
        EmailVO emailVO = new EmailVO();
        for (OrderDTO orderDTO : orderList) {
            //店铺新订单创建提醒
            ShopVO shop = shopClient.getShop(orderDTO.getSellerId());
            MessageTemplateDO messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.SHOPORDERSNEW);
            // 判断是否开启
            if (shop.getCompanyEmail() != null && messageTemplate != null) {
                if (messageTemplate.getEmailState().equals(MessageOpenStatusEnum.OPEN.value())) {
                    Map<String, Object> valuesMap = new HashMap<String, Object>(4);
                    valuesMap.put("ordersSn", orderDTO.getSn());
                    valuesMap.put("createTime", DateUtil.toString(orderDTO.getCreateTime(), "yyyy-MM-dd"));
                    valuesMap.put("siteName", siteSetting.getSiteName());
                    emailVO.setEmail(shop.getCompanyEmail());
                    this.send(emailVO, messageTemplate, valuesMap);
                }
            }

            //会员新订单创建提醒
            Member member = memberClient.getModel(orderDTO.getMemberId());
            MessageTemplateDO messageTemplateDO = messageTemplateClient.getModel(MessageCodeEnum.MEMBERORDERCREATE);
            // 判断是否开启
            if (StringUtil.notEmpty(member.getEmail()) && messageTemplateDO != null) {
                if (MessageOpenStatusEnum.OPEN.value().equals(messageTemplateDO.getEmailState())) {
                    Map<String, Object> valuesMap = new HashMap<String, Object>(4);
                    valuesMap.put("ordersSn", orderDTO.getSn());
                    valuesMap.put("createTime", DateUtil.toString(orderDTO.getCreateTime(), "yyyy-MM-dd"));
                    valuesMap.put("siteName", siteSetting.getSiteName());
                    emailVO.setEmail(member.getEmail());
                    this.send(emailVO, messageTemplateDO, valuesMap);
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
            String siteSettingJson = settingClient.get(SettingGroup.SITE);
            SiteSetting siteSetting = JsonUtil.jsonToObject(siteSettingJson, SiteSetting.class);
            EmailVO emailVO = new EmailVO();
            //获取模板信息
            MessageTemplateDO messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.SHOPORDERSEVALUATE);

            goodsCommentMsg.getComment().forEach(comment ->{
                // 判断是否开启
                ShopVO shop = shopClient.getShop(comment.getSellerId());
                if (shop.getCompanyEmail() != null && messageTemplate != null) {
                    if (messageTemplate.getEmailState().equals(MessageOpenStatusEnum.OPEN.value())) {
                        Map<String, Object> valuesMap = new HashMap<String, Object>(4);
                        valuesMap.put("ordersSn", comment.getOrderSn());
                        valuesMap.put("evalTime", DateUtil.toString(comment.getCreateTime(), "yyyy-MM-dd"));
                        valuesMap.put("userName", comment.getMemberName());
                        valuesMap.put("siteName", siteSetting.getSiteName());
                        emailVO.setEmail(shop.getCompanyEmail());
                        this.send(emailVO, messageTemplate, valuesMap);
                    }
                }
            });
        }
    }

    /**
     * 为店铺发送会员商品咨询邮件提醒
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
        EmailVO emailVO = new EmailVO();
        //获取模板信息
        MessageTemplateDO messageTemplate = messageTemplateClient.getModel(MessageCodeEnum.SHOPGOODSASK);

        if (messageTemplate != null) {
            // 判断是否开启邮件提醒
            if (messageTemplate.getEmailState().equals(MessageOpenStatusEnum.OPEN.value())) {
                for (MemberAsk memberAsk : memberAskList) {
                    // 判断店铺是否绑定了电子邮箱
                    ShopVO shop = shopClient.getShop(memberAsk.getSellerId());
                    if (StringUtil.notEmpty(shop.getCompanyEmail())) {
                        Map<String, Object> valuesMap = new HashMap<String, Object>(4);
                        valuesMap.put("askTime", DateUtil.toString(memberAsk.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                        valuesMap.put("memberName", memberAsk.getMemberName());
                        valuesMap.put("goodsName", memberAsk.getGoodsName());
                        valuesMap.put("siteName", siteSetting.getSiteName());
                        emailVO.setEmail(shop.getCompanyEmail());
                        this.send(emailVO, messageTemplate, valuesMap);
                    }
                }
            }
        }
    }

    /**
     * 替换中的内容
     *
     * @param content
     * @param map     替换的文本内容
     * @return
     */
    private String replaceContent(String content, Map map) {
        StrSubstitutor strSubstitutor = new StrSubstitutor(map);
        return strSubstitutor.replace(content);
    }


}
