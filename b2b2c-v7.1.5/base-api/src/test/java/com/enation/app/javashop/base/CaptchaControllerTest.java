package com.enation.app.javashop.base;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.enation.app.javashop.core.base.service.CaptchaManager;
import com.enation.app.javashop.framework.test.BaseTest;

/**
 * 图片验证码测试
 * @author zh
 * @version v1.0
 * @since v7.0
 * 2018年3月28日 下午6:23:42
 */
public class CaptchaControllerTest extends BaseTest{
	@Autowired
	private CaptchaManager captchaManager;

	@Test
	public void captchaGenerate() throws Exception {
		String uuid = "19940303";
		String scene = "login";
		mockMvc.perform(MockMvcRequestBuilders.get("/captchas/"+uuid+"/"+scene+""))
		.andExpect(status().isOk())
		.andReturn().getResponse().getContentAsString(); 
		boolean b = captchaManager.valid(uuid, "1111", scene);
		Assert.assertTrue(b);
	}
}

