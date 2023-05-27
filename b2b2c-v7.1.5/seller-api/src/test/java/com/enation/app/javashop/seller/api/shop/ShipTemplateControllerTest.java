package com.enation.app.javashop.seller.api.shop;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.enation.app.javashop.core.shop.model.dos.ShipTemplateDO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;

/**
 * 运费模版测试类
 * @author zhangjiping
 * @version v7.0.0
 * @since v7.0.0
 * 2018年4月13日 上午9:26:01
 */
@Transactional(value = "memberTransactionManager",rollbackFor=Exception.class)
public class ShipTemplateControllerTest extends BaseTest{
	@Autowired
	@Qualifier("memberDaoSupport")
	private DaoSupport daoSupport;
	
	/**定义卖家token sellerId：100 shopName:测试店铺 */
	String token = "";
	int id = 0;

	String[] names = new String[] {"name","first_company","first_price","continued_company","continued_price","area_json","type","area_id","message"};
	String[] names1 = new String[] {"name","first_company","first_price","continued_company","continued_price","area_json","type","area_id"};

	@Before
	public void insertData() {
		
		this.daoSupport.execute("delete from es_ship_template");
		
		Map map = new  HashMap();
		map.put("seller_id", "100");
		map.put("name", "测试运费模版");
		map.put("first_company", 1);
		map.put("first_price", 1.0);
		map.put("continued_company", 1);
		map.put("continued_price", 1.0);
		map.put("area", "{河北,石家庄市,辛集市}");
		map.put("type", 1);
		map.put("area_id", "{5,142,143}");
		this.daoSupport.insert("es_ship_template",map);
		id = this.daoSupport.getLastId("es_ship_template");

		token = this.createSellerToken(100, 100, "woshiceshi", "测试店铺");
	}

	/**
	 * 获取运费模版列表
	 * @throws Exception
	 */
	@Test
	public void testList() throws Exception {
		//正确性校验
		mockMvc.perform(get("/seller/shops/ship-templates")
				.header("Authorization", token))
				.andExpect(status().is(200))
				.andExpect(jsonPath("$.[0].seller_id").value(100))
				.andExpect(jsonPath("$.[0].name").value("测试运费模版"))
				.andExpect(jsonPath("$.[0].template_id").value(id));
	}

	/**
	 * 添加运费模版
	 * @throws Exception
	 */
	@Test
	public void testAdd() throws Exception {

		String[] values1 = new String[] {"","2","2.0","2","2.0","河北,石家庄市,辛集市","1","{5,142,143}","模版名称必填"};
		String[] values2 = new String[] {"测试运费模版1","","2.0","2","2.0","河北,石家庄市,辛集市","1","{5,142,143}","首重/首件必填"};
		String[] values3 = new String[] {"测试运费模版1","2","","2","2.0","河北,石家庄市,辛集市","1","{5,142,143}","运费必填"};
		String[] values4 = new String[] {"测试运费模版1","2","2.0","","2.0","河北,石家庄市,辛集市","1","{5,142,143}","续重/需件必填"};
		String[] values5 = new String[] {"测试运费模版1","2","2.0","2","","河北,石家庄市,辛集市","1","{5,142,143}","续费必填"};
		String[] values6 = new String[] {"测试运费模版1","2","2.0","2","2.0","","1","{5,142,143}","地区必填"};
		String[] values7 = new String[] {"测试运费模版1","2","2.0","2","2.0","河北,石家庄市,辛集市","","{5,142,143}","模版类型必填"};
		List<MultiValueMap<String, String>> list = toMultiValueMaps(names, values1, values2, values3, values4, values5, values6, values7) ;
		
		String[] values = new String[] {"测试运费模版1","2","2.0","2","2.0","[{'local_name':'北苑','id':4139,'level':3}]","1","{5,142,143}"};
		List<MultiValueMap<String, String>> ship = toMultiValueMaps(names1, values);
		//参数性校验
		for (MultiValueMap<String, String> params : list) {
			 String message =  params.get("message").get(0);
	         ErrorMessage error  = new ErrorMessage("004",message);
	         mockMvc.perform(post("/seller/shops/ship-templates")
	        		 .header("Authorization", token)
	        		 .params(params))
	         		 .andExpect(status().is(400))
	         		 .andExpect(objectEquals( error ));
		}
		
		//正确性校验
		String json = mockMvc.perform(post("/seller/shops/ship-templates")
							.header("Authorization", token)
			       		 	.params(ship.get(0)))
			        	 	.andExpect(status().is(200))
			        	 	.andReturn().getResponse().getContentAsString();
		ShipTemplateDO shipTemplateDO = JsonUtil.jsonToObject(json, ShipTemplateDO.class);
		
		Map shipTemplate = this.daoSupport.queryForMap("select name,first_company,first_price,continued_company,continued_price,area,type,area_id from es_ship_template where id =  ? ",shipTemplateDO.getId());
		Map expected = new HashMap();
		expected.put("continued_company","2.0");
		expected.put("area","北苑");
		expected.put("continued_price","2.00");
		expected.put("first_price","2.00");
		expected.put("name","测试运费模版1");
		expected.put("type","1");
		expected.put("area_id","4139");
		expected.put("first_company","2.0");
		Map actual  = this.formatMap(shipTemplate);
		Assert.assertEquals(expected, actual);
	}

	/**
	 * 编辑运费模版
	 * @throws Exception
	 */
	@Test
	public void testEdit() throws Exception {
		String[] values1 = new String[] {"","2","2.0","2","2.0","[{'local_name':'北苑','region_id':4139,'p_regions_id':72,'level':3}]","1","{5,142,143}","模版名称必填"};
		String[] values2 = new String[] {"测试运费模版1","","2.0","2","2.0","[{'local_name':'北苑','region_id':4139,'p_regions_id':72,'level':3}]","1","{5,142,143}","首重/首件必填"};
		String[] values3 = new String[] {"测试运费模版1","2","","2","2.0","[{'local_name':'北苑','region_id':4139,'p_regions_id':72,'level':3}]","1","{5,142,143}","运费必填"};
		String[] values4 = new String[] {"测试运费模版1","2","2.0","","2.0","[{'local_name':'北苑','region_id':4139,'p_regions_id':72,'level':3}]","1","{5,142,143}","续重/需件必填"};
		String[] values5 = new String[] {"测试运费模版1","2","2.0","2","","[{'local_name':'北苑','region_id':4139,'p_regions_id':72,'level':3}]","1","{5,142,143}","续费必填"};
		String[] values6 = new String[] {"测试运费模版1","2","2.0","2","2.0","","1","{5,142,143}","地区必填"};
		String[] values7 = new String[] {"测试运费模版1","2","2.0","2","2.0","[{'local_name':'北苑','region_id':4139,'p_regions_id':72,'level':3}]","","{5,142,143}","模版类型必填"};
		List<MultiValueMap<String, String>> list = toMultiValueMaps(names, values1, values2, values3, values4, values5, values6, values7) ;
		
		String[] values = new String[] {"测试运费模版1","2","2.0","2","2.0","[{'local_name':'北苑','id':4139,'level':3}]","1","{5,142,143}"};
		List<MultiValueMap<String, String>> ship = toMultiValueMaps(names1, values);
		//参数性校验
		for (MultiValueMap<String, String> params : list) {
			 String message =  params.get("message").get(0);
	         ErrorMessage error  = new ErrorMessage("004",message);
	         mockMvc.perform(put("/seller/shops/ship-templates/"+id)
	        		 .header("Authorization", token)
	        		 .params(params))
	         		 .andExpect(status().is(400))
	         		 .andExpect(objectEquals( error ));
		}
		
		//正确性校验
		mockMvc.perform(put("/seller/shops/ship-templates/"+id)
							.header("Authorization", token)
			       		 	.params(ship.get(0)))
			        	 	.andExpect(status().is(200));

		Map shipTemplate = this.daoSupport.queryForMap("select name,first_company,first_price,continued_company,continued_price,area,type,area_id from es_ship_template where template_id =  ? ",id);
		Map expected = new HashMap();
		expected.put("continued_company","2.0");
		expected.put("area","北苑");
		expected.put("continued_price","2.00");
		expected.put("first_price","2.00");
		expected.put("name","测试运费模版1");
		expected.put("type","1");
		expected.put("area_id","4139");
		expected.put("first_company","2.0");
		Map actual  = this.formatMap(shipTemplate);
		Assert.assertEquals(expected, actual);
	}

	/**
	 * 删除运费模版
	 * @throws Exception
	 */
	@Test
	public void testDelete() throws Exception {
		//数据无效性校验
		ErrorMessage error  = new ErrorMessage("E210","运费模版不存在，不能进行此操作");
		mockMvc.perform(delete("/seller/shops/ship-templates/-1")
				.header("Authorization", token))
        	 	.andExpect(status().is(500))
        	 	.andExpect(objectEquals(error));
		
		//正确性校验
		mockMvc.perform(delete("/seller/shops/ship-templates/"+id)
				.header("Authorization", token))
        	 	.andExpect(status().is(200))
        	 	.andReturn().getResponse().getContentAsString();
		int count = this.daoSupport.queryForInt("select count(*) from es_ship_template where template_id = ? ", id);
		Assert.assertEquals(0, count);
	}

	/**
	 * 获取运费模版
	 * @throws Exception
	 */
	@Test
	public void testGet() throws Exception {
		//数据无效性校验
		ErrorMessage error  = new ErrorMessage("E210","运费模版不存在，不能进行此操作");
		mockMvc.perform(get("/seller/shops/ship-templates/-1")
				.header("Authorization", token))
        	 	.andExpect(status().is(500))
        	 	.andExpect(objectEquals(error));
		
		String json = mockMvc.perform(get("/seller/shops/ship-templates/"+id)
				.header("Authorization", token))
        	 	.andExpect(status().is(200))
        	 	.andReturn().getResponse().getContentAsString();
		ShipTemplateDO shipTemplateDO = JsonUtil.jsonToObject(json, ShipTemplateDO.class);
		
		ShipTemplateDO shipTemplate = this.daoSupport.queryForObject("Select * from es_ship_template where template_id = ? ",ShipTemplateDO.class, id);
		
		Assert.assertEquals(shipTemplateDO, shipTemplate);
		
	}
}
