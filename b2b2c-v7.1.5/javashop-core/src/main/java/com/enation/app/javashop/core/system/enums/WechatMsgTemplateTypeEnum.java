package com.enation.app.javashop.core.system.enums;

/**
 * @author fk
 * @version v7.1.4
 * @Description 微信服务消息枚举类型
 * @ClassName WechatMsgTemplateTypeEnum
 * @since v7.1
 */
public enum WechatMsgTemplateTypeEnum {

    /**
     *订单支付成功通知
     */
    PAY_NOTICE("OPENTM207498902","订单支付成功通知","您的订单已支付成功。","如有问题，请致电400-88888888"),
    /**
     *订单发货提醒
     */
    SHIP_NOTICE("OPENTM200565259","订单发货提醒","亲，宝贝已经启程，好想快点来到你的身边","如有问题，请致电400-88888888"),
    /**
     *确认收货通知
     */
    ROG_NOTICE("OPENTM202314085","确认收货通知","亲，你在我们商城买的宝贝已经确认收货","感谢您的支持与厚爱"),
    /**
     *订单取消通知
     */
    CANCEL_NOTICE("OPENTM411066758","订单取消通知","您的订单已取消","如有问题，请致电400-88888888"),
    /**
     *退货审核通知
     */
    REFUND_GOOD_NOTICE("OPENTM409316217","退货审核通知","您好，你的退货申请已审核通过，请您尽快寄回商品。","如有问题，请致电400-88888888"),
    /**
     *退款通知
     */
    RETUND_NOTICE("OPENTM405731445","退款通知","退款成功，详情如下。","请注意查收"),
    /**
     *拼团成功通知
     */
    PINTUAN_NOTICE("OPENTM417762951","拼团成功通知","恭喜您，拼团成功","感谢您选择我们");

    private String sn;
    private String tmpName;
    private String first;
    private String remark;


    WechatMsgTemplateTypeEnum(String sn, String tmpName,String first,String remark) {
        this.sn = sn;
        this.tmpName = tmpName;
        this.first = first;
        this.remark = remark;
    }

    public String getSn() {
        return this.sn;
    }

    public String getTmpName() {
        return this.tmpName;
    }

    public String getFirst() {
        return this.first;
    }

    public String getRemark() {
        return this.remark;
    }

    public String value() {
        return this.name();
    }



}
