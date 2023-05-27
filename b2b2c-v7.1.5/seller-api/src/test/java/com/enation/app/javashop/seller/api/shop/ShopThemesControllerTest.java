package com.enation.app.javashop.seller.api.shop;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;


/**
 * 
 * 店铺模版测试用例
 * @author zhangjiping
 * @version v7.0.0
 * @since v7.0.0
 * 2018年4月19日 下午5:42:42
 */
@Transactional(value = "memberTransactionManager",rollbackFor=Exception.class)
public class ShopThemesControllerTest extends BaseTest{
	
	@Autowired
	@Qualifier("memberDaoSupport")
	private DaoSupport daoSupport;
	
	/**定义卖家token sellerId：100 shopName:测试店铺 */
	String token = "";
	int id = 0;
	
	@Before
	public void insertData() {
		
		this.daoSupport.execute("delete from es_shop_themes");
		Map map = new  HashMap();
		map.put("name", "测试店铺模版");
		map.put("image_path", "/shop/themes");
		map.put("is_default", 1);
		map.put("type", "PC");
		map.put("mark", "mark");
		this.daoSupport.insert("es_shop_themes",map);
		id = this.daoSupport.getLastId("es_shop_themes");
		
		this.daoSupport.execute("delete from es_shop_detail");
		map.clear();
		map.put("shop_id", 100);
		map.put("shop_themeid", id);
		this.daoSupport.insert("es_shop_detail",map);

		token = this.createSellerToken(100, 100, "woshiceshi", "测试店铺");

	}

	/**
	 * 更换店铺模版
	 * @throws Exception
	 */
	@Test
	public void testChangeShopThemes() throws Exception {
		
		//无效性校验
		ErrorMessage error  = new ErrorMessage("E202","模版不存在");
		mockMvc.perform(put("/seller/shops/themes/-1")
				.header("Authorization", token))
				.andExpect(status().is(500))
			 	.andExpect(objectEquals(error));
		
		//正确性校验
		mockMvc.perform(put("/seller/shops/themes/"+id)
				.header("Authorization", token))
				.andExpect(status().is(200));
		
		int shopThemeid = this.daoSupport.queryForInt("select shop_themeid from es_shop_detail where shop_id = ? ", 100);
		
		Assert.assertEquals(id, shopThemeid);
		
	}

	/**
	 * 获取店铺模版列表
	 * @throws Exception
	 */
	@Test
	public void testList() throws Exception {
		
		//参数性校验
		ErrorMessage error  = new ErrorMessage("004","模版类型必填");
		mockMvc.perform(get("/seller/shops/themes")
				.header("Authorization", token))
				.andExpect(status().is(400))
			 	.andExpect(objectEquals(error));
		
		error  = new ErrorMessage("E201","模版类型不匹配");
		mockMvc.perform(get("/seller/shops/themes")
				.header("Authorization", token)
				.param("type", "PCC"))
				.andExpect(status().is(500))
			 	.andExpect(objectEquals(error));

		//正确性校验
		mockMvc.perform(get("/seller/shops/themes")
				.header("Authorization", token)
				.param("type", "PC"))
				.andExpect(jsonPath("$.[0].id").value(id))
				.andExpect(jsonPath("$.[0].type").value("PC"));
		
	}

}
