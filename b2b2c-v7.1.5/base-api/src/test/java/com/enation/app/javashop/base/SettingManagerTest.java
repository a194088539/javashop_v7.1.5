package com.enation.app.javashop.base;

import static org.junit.Assert.assertEquals;


import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.enation.app.javashop.core.base.SettingGroup;
import com.enation.app.javashop.core.base.model.vo.SmsSendVO;
import com.enation.app.javashop.core.base.service.SettingManager;
import com.enation.app.javashop.framework.test.BaseTest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * 系统设置测试类
 * @author zh
 * @version v1.0
 * @since v7.0
 * 2018年3月28日 上午9:14:24
 */
@Transactional(value = "systemTransactionManager",rollbackFor = Exception.class)
public class SettingManagerTest extends BaseTest{
	@Autowired
	private SettingManager settingManager;

	@Autowired
	@Qualifier("systemDaoSupport")
	private DaoSupport systemDaoSupport;

	@Test
	public void settingSaveTest() {
		SmsSendVO smsSendVO = new SmsSendVO();
		smsSendVO.setContent("name");
		smsSendVO.setMobile("mobile");
		settingManager.save(SettingGroup.TEST,smsSendVO);
	}

	@Test
	public void getSettings() {
		this.settingSaveTest();
		//数据库查询出的对象
		String smsDBJson = settingManager.get(SettingGroup.TEST);
		SmsSendVO smsDB = JsonUtil.jsonToObject(smsDBJson,SmsSendVO.class);

		//存入之前的对象
		SmsSendVO smsNw = new SmsSendVO();
		smsNw.setContent("name");
		smsNw.setMobile("mobile");
		//比较是否相等
		assertEquals("equals",smsDB,smsNw);
	}

}
