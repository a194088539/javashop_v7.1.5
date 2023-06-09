package com.enation.app.javashop.core.payment.service;

import com.enation.app.javashop.core.payment.model.dos.PaymentBillDO;
import com.enation.app.javashop.core.payment.model.enums.TradeType;
import com.enation.app.javashop.framework.database.Page;

/**
 * 支付帐单业务层
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018-04-16 17:28:07
 */
public interface PaymentBillManager	{

	/**
	 * 查询支付帐单列表
	 * @param page 页码
	 * @param pageSize 每页数量
	 * @return Page
	 */
	Page list(int page, int pageSize);
	/**
	 * 添加支付帐单
	 * @param paymentBill 支付帐单
	 * @return PaymentBill 支付帐单
	 */
	PaymentBillDO add(PaymentBillDO paymentBill);

	/**
	 * 支付成功调用
	 * @param billSn 支付账单号
	 * @param returnTradeNo 第三方平台回传单号（第三方平台的支付单号）
	 * @param tradeType 交易类型
	 * @param payPrice 支付金额
	 */
    void paySuccess(String billSn, String returnTradeNo, TradeType tradeType, double payPrice);


	/**
	 * 使用单号和交易类型查询对应的支付流水，最后一条
	 * @param sn
	 * @param tradeType
	 * @return
	 */
	PaymentBillDO getBillBySnAndTradeType(String sn, String tradeType);

	/**
	 * 使用第三方单号查询流水
	 * @param returnTradeNo
	 * @return
	 */
	PaymentBillDO getBillByReturnTradeNo(String returnTradeNo);

}