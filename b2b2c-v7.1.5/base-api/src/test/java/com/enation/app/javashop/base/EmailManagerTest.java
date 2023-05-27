package com.enation.app.javashop.base;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.enation.app.javashop.core.base.model.vo.EmailVO;
import com.enation.app.javashop.core.base.service.EmailManager;
import com.enation.app.javashop.framework.test.BaseTest;


/**
 * 
 * 短信发送消息测试
 * @author zh
 * @version v1.0
 * @since v7.0
 * 2018年3月27日 下午8:21:23
 */
public class EmailManagerTest extends BaseTest{

	@Autowired
	private EmailManager emailManager;
	/**
	 * 测试smtp发送邮件
	 */
	@Test
	public void smtpSendTest() {
		EmailVO emailVO = new EmailVO();
		emailVO.setContent("测试邮件");
		emailVO.setTitle("测试邮件");
		emailVO.setEmail("310487699@qq.com");
		emailManager.sendMQ(emailVO);
		assertTrue(true);
	}

}
