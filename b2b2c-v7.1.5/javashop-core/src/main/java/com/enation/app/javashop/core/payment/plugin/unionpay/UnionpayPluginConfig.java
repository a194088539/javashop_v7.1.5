package com.enation.app.javashop.core.payment.plugin.unionpay;

import com.enation.app.javashop.core.payment.PaymentErrorCode;
import com.enation.app.javashop.core.payment.model.enums.UnionpayConfigItem;
import com.enation.app.javashop.core.payment.model.vo.PayBill;
import com.enation.app.javashop.core.payment.plugin.unionpay.sdk.SDKConfig;
import com.enation.app.javashop.core.payment.service.AbstractPaymentPlugin;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.util.StringUtil;

import java.util.Map;
import java.util.Properties;

/**
 * 中国银联
 * 
 * @author fk
 * @version v6.4
 * @since v6.4 2018年5月23日 下午3:37:06
 */
public class UnionpayPluginConfig extends AbstractPaymentPlugin {

	private static int is_load = 0;

	@Override
	protected String getPluginId() {

		return "unionpayPlugin";
	}


	/**
	 * 设置参数
	 */
	public void setConfig(PayBill bill) {

		Map<String, String> params = this.getConfig(bill.getClientType());
		UnionpayConfig.merId = params.get(UnionpayConfigItem.mer_id.name());
		// 是否是测试模式

		// 如果已经加载，并且是生产环境则不用再加载
		if (is_load == 1 && isTest == 1) {
			return;
		}

		// 签名证书路径
		String signCert = params.get(UnionpayConfigItem.sign_cert.name());

		// 签名证书密码
		String pwd = params.get(UnionpayConfigItem.pwd.name());

		// 验证签名证书目录
		String validateCert = params.get(UnionpayConfigItem.validate_cert.name());

		// 敏感信息加密证书路径
		String encryptCert = params.get(UnionpayConfigItem.encrypt_cert.name());

		if (StringUtil.isEmpty(signCert)) {
			throw new ServiceException(PaymentErrorCode.E505.code(),"支付方式参数不正确");
		}
		if (StringUtil.isEmpty(pwd)) {
			throw new ServiceException(PaymentErrorCode.E505.code(),"支付方式参数不正确");
		}
		if (StringUtil.isEmpty(validateCert)) {
			throw new ServiceException(PaymentErrorCode.E505.code(),"支付方式参数不正确");
		}

		Properties pro = new Properties();

		pro.setProperty("acpsdk.signCert.type", "PKCS12");
		pro.setProperty("acpsdk.signCert.path", signCert);
		pro.setProperty("acpsdk.signCert.pwd", pwd);
		pro.setProperty("acpsdk.validateCert.dir", validateCert);

		// 设置测试环境的提交地址
		if (isTest == 0) {
			pro.setProperty("acpsdk.frontTransUrl", "https://gateway.test.95516.com/gateway/api/frontTransReq.do");
			pro.setProperty("acpsdk.backTransUrl", "https://gateway.test.95516.com/gateway/api/backTransReq.do");
			pro.setProperty("acpsdk.singleQueryUrl", "https://gateway.test.95516.com/gateway/api/queryTrans.do");
			pro.setProperty("acpsdk.batchTransUrl", "https://gateway.test.95516.com/gateway/api/batchTrans.do");
			pro.setProperty("acpsdk.fileTransUrl", "https://filedownload.test.95516.com/");
			pro.setProperty("acpsdk.appTransUrl", "https://gateway.test.95516.com/gateway/api/appTransReq.do");
			pro.setProperty("acpsdk.cardTransUrl", "https://gateway.test.95516.com/gateway/api/cardTransReq.do");
		}

		// 设置生产环境的提交地址
		if (isTest == 1) {
			pro.setProperty("acpsdk.frontTransUrl", "https://gateway.95516.com/gateway/api/frontTransReq.do");
			pro.setProperty("acpsdk.backTransUrl", "https://gateway.95516.com/gateway/api/backTransReq.do");
			pro.setProperty("acpsdk.singleQueryUrl", "https://gateway.95516.com/gateway/api/queryTrans.do");
			pro.setProperty("acpsdk.batchTransUrl", "https://gateway.95516.com/gateway/api/batchTrans.do");
			pro.setProperty("acpsdk.fileTransUrl", "https://filedownload.95516.com/");
			pro.setProperty("acpsdk.appTransUrl", "https://gateway.95516.com/gateway/api/appTransReq.do");
			pro.setProperty("acpsdk.cardTransUrl", "https://gateway.95516.com/gateway/api/cardTransReq.do");

		}

		if (!StringUtil.isEmpty(encryptCert)) {
			pro.setProperty("acpsdk.encryptCert.path", encryptCert);
		}

		SDKConfig.getConfig().loadProperties(pro);

		is_load = 1;

	}
}
