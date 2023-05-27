package com.enation.app.javashop.core.trade.order.service;

/**
 *  交易订单号创建
 * @author Snow create in 2018/4/9
 * @version v2.0
 * @since v7.0.0
 */
public interface TradeSnCreator {

    /**
     * 生成交易编号  格式如：20171022000011
     * @return 交易编号
     */
    String generateTradeSn();


    /**
     * 生成订单编号  格式如：20171022000011
     * @return 订单编号
     */
    String generateOrderSn();

    /**
     * 生成付款流水号  格式如：20171022000011
     * @return 订单编号
     */
    String generatePayLogSn();

    /**
     * 零钱转账编号
     * @return
     */
    String generateSmallChangeLogSn();

    /**
     * 生成售后服务单号
     * @return
     */
    String generateAfterSaleServiceSn();

    /**
     * 清除
     */
    void cleanCache();

}
