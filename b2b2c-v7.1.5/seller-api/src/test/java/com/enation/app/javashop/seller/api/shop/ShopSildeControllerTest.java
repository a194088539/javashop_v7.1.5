package com.enation.app.javashop.seller.api.shop;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.enation.app.javashop.core.shop.model.dos.ShopSildeDO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;

/**
 * 店铺幻灯片测试类
 * @author zhangjiping
 * @version v7.0.0
 * @since v7.0.0
 * 2018年4月13日 上午10:43:20
 */
@Transactional(value = "memberTransactionManager",rollbackFor=Exception.class)
public class ShopSildeControllerTest extends BaseTest{
	@Autowired
	@Qualifier("memberDaoSupport")
	private DaoSupport daoSupport;

	/**定义卖家token sellerId：100 shopName:测试店铺 */
	String token = "";
	int id = 0;

	@Before
	public void insertData() {

		this.daoSupport.execute("delete from es_shop_silde");

		Map map = new  HashMap();
		map.put("shop_id", "100");
		map.put("silde_url", "www.xxx.com");
		map.put("img", "www.xxx.com");
		this.daoSupport.insert("es_shop_silde",map);
		id = this.daoSupport.getLastId("es_shop_silde");

		token = this.createSellerToken(100, 100, "woshiceshi", "测试店铺");
	}

	/**
	 * 获取幻灯片列表
	 * @throws Exception
	 */
	@Test
	public void testList() throws Exception {

		//正确性校验
		mockMvc.perform(get("/seller/shops/sildes")
				.header("Authorization", token))
				.andExpect(jsonPath("$.[0].silde_url").value("www.xxx.com"))
				.andExpect(jsonPath("$.[0].shop_id").value(100))
				.andExpect(jsonPath("$.[0].img").value("www.xxx.com"));

	}

	/**
	 * 编辑幻灯片
	 * @throws Exception
	 */
	@Test
	public void testEdit() throws Exception {
		String[] names = new String[] {"silde_url","img","silde_id"};
		String[] values3 = new String[] {"www.xxx.com","www.xxx.com",""+id};
		List<MultiValueMap<String, String>> list = toMultiValueMaps(names, values3) ;
		String values ="[\n" +
				"  {\n" +
				"    \"img\": \"www.xxx.com\",\n" +
				"    \"silde_id\": -1,\n" +
				"    \"silde_url\": \"www.xxx.com\"\n" +
				"  }\n" +
				"]";

		String values1 ="[\n" +
				"  {\n" +
				"    \"img\": \"www.xxx.com\",\n" +
				"    \"silde_id\":0 ,\n" +
				"    \"silde_url\": \"www.xxx.com\"\n" +
				"  }\n" +
				"]";

		String values2 ="[\n" +
				"  {\n" +
				"    \"img\": \"www.xxx.com\",\n" +
				"    \"silde_id\":" +id+
				" ,\n" +
				"    \"silde_url\": \"www.xxx.com\"\n" +
				"  }\n" +
				"]";

		//数据无效性校验
		ErrorMessage error  = new ErrorMessage("E208","存在无效幻灯片，无法进行编辑操作");
		mockMvc.perform(put("/seller/shops/sildes")
				.header("Authorization", token)
				.content(values)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(500))
				.andExpect(objectEquals(error));

		//正确性校验
		mockMvc.perform(put("/seller/shops/sildes")
				.header("Authorization", token)
				.content(values1)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200))
				.andReturn().getResponse().getContentAsString();
		mockMvc.perform(put("/seller/shops/sildes")
				.header("Authorization", token)
				.content(values2)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200))
				.andReturn().getResponse().getContentAsString();
		Map slide = this.daoSupport.queryForMap("select silde_url,img,silde_id from es_shop_silde where silde_id =  ? ",id);
		Map expected = this.formatMap(list.get(0));
		Map actual  = this.formatMap(slide);
		Assert.assertEquals(expected, actual);
	}

	/**
	 * 删除幻灯片
	 * @throws Exception
	 */
	@Test
	public void testDelete() throws Exception {

		//数据无效性校验
		ErrorMessage error  = new ErrorMessage("E208","不存在此幻灯片，无法删除");
		mockMvc.perform(delete("/seller/shops/sildes/-1")
				.header("Authorization", token))
				.andExpect(status().is(500))
				.andExpect(objectEquals(error));

		//正确性校验
		mockMvc.perform(delete("/seller/shops/sildes/"+id)
				.header("Authorization", token))
				.andExpect(status().is(200))
				.andReturn().getResponse().getContentAsString();
		int count = this.daoSupport.queryForInt("select count(*) from es_shop_silde where silde_id = ? ", id);
		Assert.assertEquals(0, count);
	}
}