package com.enation.app.javashop.base;

import static org.junit.Assert.assertTrue;

import com.enation.app.javashop.core.base.model.vo.SmsSendVO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.enation.app.javashop.core.base.service.SmsManager;
import com.enation.app.javashop.framework.test.BaseTest;


/**
 * 
 * 短信发送消息测试
 * @author zh
 * @version v1.0
 * @since v7.0
 * 2018年3月27日 下午8:21:23
 */
public class SmsManagerTest extends BaseTest{

	@Autowired
	private SmsManager smsManager;

	@Test
	public void smsSendTest() {
		String mobile = "18234124540";
		String content = "测试发送短信";
		SmsSendVO smsSendVO = new SmsSendVO();
		smsSendVO.setMobile(mobile);
		smsSendVO.setContent(content);
		smsManager.send(smsSendVO);
		assertTrue(true);
	}

}
