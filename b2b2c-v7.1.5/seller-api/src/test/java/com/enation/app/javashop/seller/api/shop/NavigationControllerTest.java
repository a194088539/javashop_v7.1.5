package com.enation.app.javashop.seller.api.shop;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.enation.app.javashop.core.shop.model.dos.NavigationDO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;

/**
 * 
 * 店铺导航相关测试
 * @author zhangjiping
 * @version v7.0.0
 * @since v7.0.0
 * 2018年4月12日 下午5:27:28
 */
 
@Transactional(value = "memberTransactionManager",rollbackFor=Exception.class)
public class NavigationControllerTest extends BaseTest{
	
	@Autowired
	@Qualifier("memberDaoSupport")
	private DaoSupport daoSupport;
	
	//定义卖家token sellerId：100 shopName:测试店铺 
	String token = "";

	int id = 0;

	String[] names = new String[] {"name","disable","sort","nav_url","target","message"};
	String[] names1 = new String[] {"name","disable","sort","nav_url","target"};
	@Before
	public void insertData() {
		
		this.daoSupport.execute("delete from es_navigation");
		
		Map map = new  HashMap();
		map.put("name", "测试手机");
		map.put("disable", 0);
		map.put("sort", 1);
		map.put("nav_url", "1");
		map.put("target", 1);
		map.put("shop_id", 100);
		this.daoSupport.insert("es_navigation",map);
		id = this.daoSupport.getLastId("es_navigation");

		token = this.createSellerToken(100, 100, "woshiceshi", "测试店铺");

	}

	/**
	 * 获取店铺导航列表
	 * @throws Exception
	 */
	@Test
	public void testList() throws Exception {
		
		//正确性校验
		mockMvc.perform(get("/seller/shops/navigations")
				.header("Authorization", token)
				.param("page_no", "1")
				.param("page_size", "10"))
				.andExpect(jsonPath("$.data.[0].name").value("测试手机"))
				.andExpect(jsonPath("$.data.[0].shop_id").value(100))
				.andExpect(jsonPath("$.data.[0].id").value(id));
		
	}

	/**
	 * 添加店铺导航
	 * @throws Exception
	 */
	@Test
	public void testAdd() throws Exception {
		

		String[] values = new String[] {"","1","1","www.xxx.com","1","导航栏名称必填"};
		String[] values1 = new String[] {"测试导航栏","","1","www.xxx.com","1","是否显示必填"};
		String[] values2= new String[] {"测试导航栏","1","","www.xxx.com","1","排序必填"};
		String[] values3 = new String[] {"测试导航栏","1","1","","1","URL必填"};
		String[] values4 = new String[] {"测试导航栏","1","1","www.xxx.com","","是否新窗口打开必填"};
		
		List<MultiValueMap<String, String>> list = toMultiValueMaps(names, values, values1, values2, values3, values4) ;

		String[] values5 = new String[] {"测试导航栏","1","1","www.xxx.com","1"};
		List<MultiValueMap<String, String>> navigation = toMultiValueMaps(names1, values5);
		//参数性校验
		for (MultiValueMap<String, String> params : list) {
			 String message =  params.get("message").get(0);
	         ErrorMessage error  = new ErrorMessage("004",message);
	         mockMvc.perform(post("/seller/shops/navigations")
	        		 .header("Authorization", token)
	        		 .params(params))
	         		 .andExpect(status().is(400))
	         		 .andExpect(objectEquals( error ));
		}
		
		//正确性校验
		String json = mockMvc.perform(post("/seller/shops/navigations")
							.header("Authorization", token)
			       		 	.params(navigation.get(0)))
			        	 	.andExpect(status().is(200))
			        	 	.andReturn().getResponse().getContentAsString();
		NavigationDO navigationDO = JsonUtil.jsonToObject(json, NavigationDO.class);
		Map nav = this.daoSupport.queryForMap("select name,disable,sort,nav_url,target from es_navigation where id =  ? ",navigationDO.getId());
		Map expected = this.formatMap(navigation.get(0));
		Map actual  = this.formatMap(nav);
		Assert.assertEquals(expected, actual);
	}

	/**
	 * 编辑店铺导航
	 * @throws Exception
	 */
	@Test
	public void testEdit() throws Exception {
		
		String[] values = new String[] {"","1","1","www.xxx.com","1","导航栏名称必填"};
		String[] values1 = new String[] {"测试导航栏1","","1","www.xxx.com","1","是否显示必填"};
		String[] values2= new String[] {"测试导航栏1","1","","www.xxx.com","1","排序必填"};
		String[] values3 = new String[] {"测试导航栏1","1","1","","1","URL必填"};
		String[] values4 = new String[] {"测试导航栏1","1","1","www.xxx.com","","是否新窗口打开必填"};
		
		List<MultiValueMap<String, String>> list = toMultiValueMaps(names, values, values1, values2, values3, values4) ;
		
		String[] values5 = new String[] {"测试导航栏","1","1","www.xxx.com","1",};
		List<MultiValueMap<String, String>> navigation = toMultiValueMaps(names1, values5);
		ErrorMessage error = null;
		
		//参数性校验
		for (MultiValueMap<String, String> params : list) {
			 String message =  params.get("message").get(0);
	         error  = new ErrorMessage("004",message);
	         mockMvc.perform(put("/seller/shops/navigations/"+id)
	        		 .header("Authorization", token)
	        		 .params(params))
	         		 .andExpect(status().is(400))
	         		 .andExpect(objectEquals( error ));
		}
		
		error  = new ErrorMessage("E209","导航不存在，不能进行编辑操作");
		mockMvc.perform(put("/seller/shops/navigations/-1")
       		 .header("Authorization", token)
       		 .params(navigation.get(0)))
        		 .andExpect(status().is(500))
        		 .andExpect(objectEquals( error ));
		
		//正确性校验
		mockMvc.perform(put("/seller/shops/navigations/"+id)
							.header("Authorization", token)
			       		 	.params(navigation.get(0)))
			        	 	.andExpect(status().is(200));
		Map nav = this.daoSupport.queryForMap("select name,disable,sort,nav_url,target from es_navigation where id =  ? ",id);
		Map expected = this.formatMap(navigation.get(0));
		Map actual  = this.formatMap(nav);
		Assert.assertEquals(expected, actual);
	}

	/**
	 * 删除店铺导航
	 * @throws Exception
	 */
	@Test
	public void testDelete() throws Exception {
		
		//数据无效性校验
		ErrorMessage error  = new ErrorMessage("E209","导航不存在，不能进行删除操作");
		mockMvc.perform(delete("/seller/shops/navigations/-1")
				.header("Authorization", token))
        	 	.andExpect(status().is(500))
        	 	.andExpect(objectEquals(error));
		
		//正确性校验
		mockMvc.perform(delete("/seller/shops/navigations/"+id)
				.header("Authorization", token))
        	 	.andExpect(status().is(200))
        	 	.andReturn().getResponse().getContentAsString();
		int count = this.daoSupport.queryForInt("select count(*) from es_navigation where id = ? ", id);
		Assert.assertEquals(0, count);
	}
}
