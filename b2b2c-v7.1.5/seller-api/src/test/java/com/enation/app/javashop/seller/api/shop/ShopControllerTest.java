package com.enation.app.javashop.seller.api.shop;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.enation.app.javashop.core.shop.model.enums.ShopStatusEnum;
import com.enation.app.javashop.core.shop.model.vo.ShopVO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;


/**
 *
 * 店铺相关测试用例
 * @author zhangjiping
 * @version v7.0.0
 * @since v7.0.0
 * 2018年4月9日 下午2:52:17
 */
@Transactional(value = "memberTransactionManager",rollbackFor=Exception.class)
public class ShopControllerTest extends BaseTest{

	@Autowired
	@Qualifier("memberDaoSupport")
	private DaoSupport daoSupport;

	List<MultiValueMap<String, String>> list = null ;
	//定义卖家token sellerId：100 shopName:测试店铺
	String token = "";
	int shopId = 0;
	String[] names = new String[] {"shop_region","link_phone","message"};
	String[] names1 = new String[] {"shop_region","link_phone"};

	@Before
	public void prepareData() {

		this.daoSupport.execute("delete from es_shop");
		Map map = new HashMap();
		map.put("member_id",100);
		map.put("member_name","测试会员");
		map.put("shop_id",100);
		map.put("shop_name","测试店铺");
		map.put("shop_disable",ShopStatusEnum.OPEN.toString() );
		this.daoSupport.insert("es_shop",map);
		shopId = this.daoSupport.getLastId("es_shop");

		this.daoSupport.execute("delete from es_shop_detail");
		map.clear();
		map.put("shop_id", 100);
		map.put("shop_level", 100);
		this.daoSupport.insert("es_shop_detail",map);
		int es_shop_detail = this.daoSupport.getLastId("es_shop_detail");

		token = this.createSellerToken(100, 100, "woshiceshi", "测试店铺");
	}

	/**
	 * 设置店铺信息
	 * @throws Exception
	 */
	@Test
	public void testEdit() throws Exception {
		String[] values7 = new String[] {"2862","","联系人电话必填"};
		list = toMultiValueMaps(names, values7);

		String[] values8 = new String[] {"2862","12345678910"};
		List<MultiValueMap<String, String>> shop = toMultiValueMaps(names1, values8);

		//数据性校验
		for (MultiValueMap<String, String> params : list) {
			String message =  params.get("message").get(0);
			ErrorMessage error  = new ErrorMessage("004",message);
			mockMvc.perform(put("/seller/shops")
					.params(params)
					.header("Authorization", token))
					.andExpect(status().is(400))
					.andExpect(objectEquals( error ));
		}

		//正确性校验
		mockMvc.perform(put("/seller/shops")
				.params(shop.get(0))
				.header("Authorization", token))
				.andExpect(status().is(200));
		Map map = this.daoSupport.queryForMap("select link_phone from es_shop_detail  where shop_id = ? ", 100);
		Map expected = this.formatMap(shop.get(0));
		expected.remove("shop_region");
		Map actual  = this.formatMap(map);
		Assert.assertEquals(expected, actual);
	}

	/**
	 * 编辑店铺logo
	 * @throws Exception
	 */
	@Test
	public void testEditShopLogo() throws Exception {

		//参数性校验
		ErrorMessage error = new ErrorMessage("004","店铺logo必填");
		mockMvc.perform(put("/seller/shops/logos")
				.header("Authorization", token))
				.andExpect(status().is(400))
				.andExpect(objectEquals(error));

		//正确性校验
		mockMvc.perform(put("/seller/shops/logos")
				.header("Authorization", token)
				.param("logo", "http://www.javamall.com.cn/images/logonew.jpg"))
				.andExpect(status().is(200));
		System.out.println(shopId);
		String shopLogo = this.daoSupport.queryForString("select shop_logo from es_shop_detail where shop_id = ? and shop_level = ? ", 100, 100);
		Assert.assertEquals("http://www.javamall.com.cn/images/logonew.jpg", shopLogo);
	}

	/**
	 * 修改预警货品数
	 * @throws Exception
	 */
	@Test
	public void testEditWarningCount() throws Exception {
		//参数校验
		ErrorMessage error = new ErrorMessage("004","预警货品数必填");
		mockMvc.perform(put("/seller/shops/warning-counts")
				.header("Authorization", token))
				.andExpect(status().is(400))
				.andExpect(objectEquals(error));

		mockMvc.perform(put("/seller/shops/warning-counts")
				.header("Authorization", token)
				.param("warning_count","10"))
				.andExpect(status().is(200));

		int  count= this.daoSupport.queryForInt("select goods_warning_count from es_shop_detail where shop_id = ? and shop_level = ? ", 100, 100);
		Assert.assertEquals(10, count);
	}

}