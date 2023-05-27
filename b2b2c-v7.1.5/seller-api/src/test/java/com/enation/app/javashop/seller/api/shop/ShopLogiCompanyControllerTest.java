package com.enation.app.javashop.seller.api.shop;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import com.enation.app.javashop.core.client.system.LogiCompanyClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.javashop.core.system.model.dos.LogisticsCompanyDO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.Mockito.when;

/**
 * 
 * 店铺物流公司测试类
 * @author zhangjiping
 * @version v7.0.0
 * @since v7.0.0
 * 2018年4月13日 上午9:27:26
 */
@Transactional(value = "memberTransactionManager",rollbackFor=Exception.class)
public class ShopLogiCompanyControllerTest extends BaseTest{
	
	@Autowired
	@Qualifier("memberDaoSupport")
	private DaoSupport daoSupport;

	@MockBean
	private LogiCompanyClient logiCompanyClient;
	
	//定义卖家token sellerId：100 shopName:测试店铺
	String token = "";
	
	@Before
	public void insertData() {
		
		this.daoSupport.execute("delete from es_shop_logi_rel");
		
		Map map = new  HashMap();
		
		map.put("shop_id", "100");
		map.put("logi_id", 100);
		this.daoSupport.insert("es_shop_logi_rel",map);

		token = this.createSellerToken(100, 100, "woshiceshi", "测试店铺");
	}


	/**
	 * 开启物流公司
	 * @throws Exception
	 */
	@Test
	public void testAdd() throws Exception {

		//数据无效性校验
		ErrorMessage error  = new ErrorMessage("E214","物流公司不存在");
		mockMvc.perform(post("/seller/shops/logi-companies/-1")
				.header("Authorization", token))
        	 	.andExpect(status().is(500))
        	 	.andExpect(objectEquals(error));
		LogisticsCompanyDO logisticsCompanyDO = new LogisticsCompanyDO();
		logisticsCompanyDO.setCode("ceshi2");
		logisticsCompanyDO.setName("测试物流公司2");
		logisticsCompanyDO.setKdcode("CSO2");
		when(logiCompanyClient.getModel(101)).thenReturn(logisticsCompanyDO);
		//正确性校验
		mockMvc.perform(post("/seller/shops/logi-companies/101")
							.header("Authorization", token))
			        	 	.andExpect(status().is(200));

		//重复性校验
		error  = new ErrorMessage("E215","物流公司已开启");
		when(logiCompanyClient.getModel(100)).thenReturn(logisticsCompanyDO);
		mockMvc.perform(post("/seller/shops/logi-companies/100")
				.header("Authorization", token))
        	 	.andExpect(status().is(500))
        	 	.andExpect(objectEquals(error));
	}

	/**
	 * 关闭物流公司
	 * @throws Exception
	 */
	@Test
	public void testDelete() throws Exception {
		
		//数据无效性校验
		ErrorMessage error  = new ErrorMessage("E216","物流公司已关闭");
		mockMvc.perform(delete("/seller/shops/logi-companies/-1")
				.header("Authorization", token))
        	 	.andExpect(status().is(500))
        	 	.andExpect(objectEquals(error));
		
		//正确性校验
		mockMvc.perform(delete("/seller/shops/logi-companies/100")
				.header("Authorization", token))
        	 	.andExpect(status().is(200))
        	 	.andReturn().getResponse().getContentAsString();
		int count = this.daoSupport.queryForInt("select count(*) from es_shop_logi_rel where shop_id =  ? and logi_id = ? ",100,100);
		Assert.assertEquals(0, count);
	}
}
