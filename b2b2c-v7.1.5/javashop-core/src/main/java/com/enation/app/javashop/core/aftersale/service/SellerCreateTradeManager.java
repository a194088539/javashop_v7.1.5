package com.enation.app.javashop.core.aftersale.service;

import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;

/**
 * 商家创建交易业务接口
 * 用户申请换货、补发商品的售后服务时，商家审核通过后，需要生成新的交易订单
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-22
 */
public interface SellerCreateTradeManager {

    /**
     * 系统自动为售后服务创建新订单
     * @param serviceSn 售后服务单号
     * @return
     */
    OrderDO systemCreateTrade(String serviceSn);

    /**
     * 商家手动为售后服务创建新订单
     * @param serviceSn 售后服务单号
     * @return
     */
    OrderDO sellerCreateTrade(String serviceSn);

}
