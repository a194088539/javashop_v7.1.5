package com.enation.app.javashop.buyer.api.shop;

import com.enation.app.javashop.core.shop.model.enums.ShopStatusEnum;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
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
import com.enation.app.javashop.framework.test.BaseTest;
/**
 *
 * 店铺相关单元测试
 * @author zhangjiping
 * @version v7.0.0
 * @since v7.0.0
 * 2018年4月9日 下午3:01:22
 */
@Transactional(value = "memberTransactionManager",rollbackFor=Exception.class)
public class ShopControllerTest extends BaseTest{

	@Autowired
	@Qualifier("memberDaoSupport")
	private DaoSupport daoSupport ;

	//定义买家token name：woshiceshi1 id：101
	String buyerTocken = "";
	//定义拥有店铺买家的token name:woshiceshi id：100
	String sellerTocken = "";

	List<MultiValueMap<String, String>> step1 = null ;
	List<MultiValueMap<String, String>> step2 = null ;
	List<MultiValueMap<String, String>> step3 = null ;
	List<MultiValueMap<String, String>> step4 = null ;

	List<MultiValueMap<String, String>> list1 = null ;
	List<MultiValueMap<String, String>> list2 = null ;
	List<MultiValueMap<String, String>> list3 = null ;
	List<MultiValueMap<String, String>> list4 = null ;
	int shopId = 0;

	@Before
	public void insertTestData() {

		this.daoSupport.execute("delete from es_shop");
		this.daoSupport.execute("delete from es_shop_detail");

		Map map = new HashMap(4);
		map.put("member_id",100);
		map.put("member_name","测试会员");
		map.put("shop_name","测试店铺");
		map.put("shop_disable",ShopStatusEnum.OPEN.toString() );
		this.daoSupport.insert("es_shop",map);
		shopId = this.daoSupport.getLastId("es_shop");

		sellerTocken = this.createBuyerToken(100,"woshiceshi");
		buyerTocken = this.createBuyerToken(101,"woshiceshi1");

		//申请开店第一步测试数据
		String[] names1 = new String[] {"company_name","company_address","company_phone","company_email","employee_num","reg_money","link_name","link_phone","message"};
		String[] values1 = new String[] {"","测试地址","12345678910","123456@qq.com","1000","1000","测试联系人","12345678910","公司名称必填"};
		String[] values2 = new String[] {"测试公司","","12345678910","123456@qq.com","1000","1000","测试联系人","12345678910","公司地址必填"};
		String[] values3 = new String[] {"测试公司","测试地址","","123456@qq.com","1000","1000","测试联系人","12345678910","公司电话必填"};
		String[] values4 = new String[] {"测试公司","测试地址","12345678910","","1000","1000","测试联系人","12345678910","电子邮箱必填"};
		String[] values5 = new String[] {"测试公司","测试地址","12345678910","123456@qq.com","","1000","测试联系人","12345678910","员工总数必填"};
		String[] values6 = new String[] {"测试公司","测试地址","12345678910","123456@qq.com","-1000","1000","测试联系人","12345678910","员工总数必须大于零"};
		String[] values7 = new String[] {"测试公司","测试地址","12345678910","123456@qq.com","1000","","测试联系人","12345678910","注册资金必填"};
		String[] values8 = new String[] {"测试公司","测试地址","12345678910","123456@qq.com","1000","-1000","测试联系人","12345678910","注册资金必须大于零"};
		String[] values9 = new String[] {"测试公司","测试地址","12345678910","123456@qq.com","1000","1000","","12345678910","联系人姓名必填"};
		String[] values10 = new String[] {"测试公司","测试地址","12345678910","123456@qq.com","1000","1000","测试联系人","","联系人电话必填"};
		step1 = toMultiValueMaps(names1, values1,values2,values3,values4,values5,values6,values7,values8,values9,values10);

		String[] name1 = new String[] {"company_name","company_address","company_phone","company_email","employee_num","reg_money","link_name","link_phone"};
		String[] value1 = new String[] {"测试公司","测试地址","12345678910","123456@qq.com","1000","1000.00","测试联系人","12345678910"};
		list1 = toMultiValueMaps(name1, value1);

		//申请开店第二步测试数据
		String[] names2 = new String[] {"legal_name","legal_id","legal_img","license_num","license_region","license_add","establish_date","licence_start","licence_end","scope","licence_img","organization_code","code_img","taxes_img","message"};
		String[] values11 = new String[] {"","000000000000000000","http://www.javamall.com.cn/images/logonew.jpg","532501100006302","210","朗威大厦","1427854939","1427854939","1534242083","电商","http://www.javamall.com.cn/images/logonew.jpg","123456789","http://www.javamall.com.cn/images/logonew.jpg","http://www.javamall.com.cn/images/logonew.jpg","法人姓名必填"};
		String[] values13 = new String[] {"我是法人","000000000","http://www.javamall.com.cn/images/logonew.jpg","532501100006302","2862","朗威大厦","1427854939","1427854939","1534242083","电商","http://www.javamall.com.cn/images/logonew.jpg","123456789","http://www.javamall.com.cn/images/logonew.jpg","http://www.javamall.com.cn/images/logonew.jpg","身份证长度不符"};
		String[] values14 = new String[] {"我是法人","000000000000000000","","532501100006302","2862","朗威大厦","1427854939","1427854939","1534242083","电商","http://www.javamall.com.cn/images/logonew.jpg","123456789","http://www.javamall.com.cn/images/logonew.jpg","http://www.javamall.com.cn/images/logonew.jpg","法人身份证照片必填"};
		String[] values15 = new String[] {"我是法人","000000000000000000","http://www.javamall.com.cn/images/logonew.jpg","","2862","朗威大厦","1427854939","1427854939","1534242083","电商","http://www.javamall.com.cn/images/logonew.jpg","123456789","http://www.javamall.com.cn/images/logonew.jpg","http://www.javamall.com.cn/images/logonew.jpg","营业执照号必填"};
		String[] values22 = new String[] {"我是法人","000000000000000000","http://www.javamall.com.cn/images/logonew.jpg","532501100006302","2862","","1427854939","1427854939","1534242083","电商","http://www.javamall.com.cn/images/logonew.jpg","123456789","http://www.javamall.com.cn/images/logonew.jpg","http://www.javamall.com.cn/images/logonew.jpg","营业执照详细地址必填"};
		String[] values23 = new String[] {"我是法人","000000000000000000","http://www.javamall.com.cn/images/logonew.jpg","532501100006302","2862","朗威大厦","","1427854939","1534242083","电商","http://www.javamall.com.cn/images/logonew.jpg","123456789","http://www.javamall.com.cn/images/logonew.jpg","http://www.javamall.com.cn/images/logonew.jpg","成立日期必填"};
		String[] values24 = new String[] {"我是法人","000000000000000000","http://www.javamall.com.cn/images/logonew.jpg","532501100006302","2862","朗威大厦","1427854939","","1534242083","电商","http://www.javamall.com.cn/images/logonew.jpg","123456789","http://www.javamall.com.cn/images/logonew.jpg","http://www.javamall.com.cn/images/logonew.jpg","营业执照有效期开始时间必填"};
		String[] values25 = new String[] {"我是法人","000000000000000000","http://www.javamall.com.cn/images/logonew.jpg","532501100006302","2862","朗威大厦","1427854939","1427854939","","电商","http://www.javamall.com.cn/images/logonew.jpg","123456789","http://www.javamall.com.cn/images/logonew.jpg","http://www.javamall.com.cn/images/logonew.jpg","营业执照有效期结束必填"};
		String[] values26 = new String[] {"我是法人","000000000000000000","http://www.javamall.com.cn/images/logonew.jpg","532501100006302","2862","朗威大厦","1427854939","1427854939","1534242083","","http://www.javamall.com.cn/images/logonew.jpg","123456789","http://www.javamall.com.cn/images/logonew.jpg","http://www.javamall.com.cn/images/logonew.jpg","法定经营范围必填"};
		String[] values27 = new String[] {"我是法人","000000000000000000","http://www.javamall.com.cn/images/logonew.jpg","532501100006302","2862","朗威大厦","1427854939","1427854939","1534242083","电商","","123456789","http://www.javamall.com.cn/images/logonew.jpg","http://www.javamall.com.cn/images/logonew.jpg","营业执照电子版必填"};
		step2 = toMultiValueMaps(names2, values11,values13,values14,values15,values22,values23,values24,values25,values26,values27);

		String[] name2 = new String[] {"legal_name","legal_id","legal_img","license_num","license_region","license_add","establish_date","licence_start","licence_end","scope","licence_img","organization_code","code_img","taxes_img"};
		String[] value2 = new String[] {"我是法人","000000000000000000","http://www.javamall.com.cn/images/logonew.jpg","532501100006302","2862","朗威大厦","1427854939","1427854939","1534242083","电商","http://www.javamall.com.cn/images/logonew.jpg","123456789","http://www.javamall.com.cn/images/logonew.jpg","http://www.javamall.com.cn/images/logonew.jpg"};
		String[] value3 = new String[] {"我是法人","000000000000000000","http://www.javamall.com.cn/images/logonew.jpg","532501100006302","2862","朗威大厦","1427854939","1427854939","1427854930","电商","http://www.javamall.com.cn/images/logonew.jpg","123456789","http://www.javamall.com.cn/images/logonew.jpg","http://www.javamall.com.cn/images/logonew.jpg"};
		list2 = toMultiValueMaps(name2, value2,value3);

		//申请开店第三步测试数据
		String[] names3 = new String[] {"bank_account_name","bank_number","bank_name","bank_region","message"};
		String[] values31 = new String[] {"","123456","海淀北京银行","2862","银行开户名必填"};
		String[] values32 = new String[] {"北京银行","","海淀北京银行","2862","银行开户账号必填"};
		String[] values33 = new String[] {"北京银行","123456","","2862","开户银行支行名称必填"};
		step3 = toMultiValueMaps(names3, values31,values32,values33);

		String[] name3 = new String[] {"bank_account_name","bank_number","bank_name","bank_region"};
		String[] value4 = new String[] {"北京银行","123456","海淀北京银行","2862"};
		list3 = toMultiValueMaps(name3, value4);

		//申请开店第四步测试数据
		String[] names4 = new String[] {"shop_name","goods_management_category","shop_region","message"};
		String[] values40 = new String[] {"","1,4,35,38,56,79,85,86","2862","店铺名称必填"};
		String[] values41 = new String[] {"测试店铺1","","county_id_2862","店铺经营类目必填"};
		step4 = toMultiValueMaps(names4, values40,values41);

		String[] name4 = new String[] {"shop_name","goods_management_category","shop_region"};
		String[] value5 = new String[] {"测试店铺1","1,4,35,38,56,79,85,86","2862"};
		String[] value6 = new String[] {"测试店铺","1,4,35,38,56,79,85,86","2862"};
		list4 = toMultiValueMaps(name4, value5,value6);
	}

	/**
	 * 初始化店铺信息
	 * @throws Exception
	 */
	@Test
	public void testInit() throws Exception {
		mockMvc.perform(post("/shops/apply")
				.header("Authorization", buyerTocken))
				.andExpect(status().is(200));
	}

	/**
	 * 申请开店第一步
	 * @throws Exception
	 */
	@Test
	public void testStep1() throws Exception {

		ErrorMessage error = null;
		//参数性校验
		for (MultiValueMap<String, String> params : step1) {
			String message =  params.get("message").get(0);
			error  = new ErrorMessage("004",message);
			mockMvc.perform(put("/shops/apply/step1")
					.params(params)
					.header("Authorization", buyerTocken))
					.andExpect(status().is(400))
					.andExpect(objectEquals( error ));
		}

		//已拥有店铺会员执行此操作
		error  = new ErrorMessage("E204","店铺在申请中，不允许此操作");
		mockMvc.perform(put("/shops/apply/step1")
				.params(list1.get(0))
				.header("Authorization", sellerTocken))
				.andExpect(status().is(500))
				.andExpect(objectEquals( error ));

		//未初始化店铺执行此操作
		error  = new ErrorMessage("E200","您尚未拥有店铺，不能进行此操作");
		mockMvc.perform(put("/shops/apply/step1")
				.params(list1.get(0))
				.header("Authorization", buyerTocken))
				.andExpect(status().is(500))
				.andExpect(objectEquals( error ));

		//初始化店铺
		testInit();

		mockMvc.perform(put("/shops/apply/step1")
				.params(list1.get(0))
				.header("Authorization", buyerTocken))
				.andExpect(status().is(200));
		Map step1 = this.daoSupport.queryForMap("select company_email,reg_money,employee_num,company_name,link_phone,company_phone,company_address,link_name from es_shop_detail sd left join es_shop s on s.shop_id = sd.shop_id where  s.member_id= ? ", 101);
		Map expected = this.formatMap(list1.get(0));
		Map actual  = this.formatMap(step1);
		Assert.assertEquals(expected, actual);
	}


	/**
	 * 申请开店第二步
	 * @throws Exception
	 */
	@Test
	public void testStep2() throws Exception {


		ErrorMessage error = null;

		//数据性校验
		for (MultiValueMap<String, String> params : step2) {
			String message =  params.get("message").get(0);
			error  = new ErrorMessage("004",message);
			mockMvc.perform(put("/shops/apply/step2").params(params)
					.header("Authorization", buyerTocken))
					.andExpect(status().is(400))
					.andExpect(objectEquals( error ));
		}

		//已拥有店铺会员执行此操作
		error  = new ErrorMessage("E204","店铺在申请中，不允许此操作");
		mockMvc.perform(put("/shops/apply/step2")
				.params(list2.get(0))
				.header("Authorization", sellerTocken))
				.andExpect(status().is(500))
				.andExpect(objectEquals( error ));

		//未初始化店铺执行此操作
		error  = new ErrorMessage("E200","您尚未拥有店铺，不能进行此操作");
		mockMvc.perform(put("/shops/apply/step2")
				.params(list2.get(0))
				.header("Authorization", buyerTocken))
				.andExpect(status().is(500))
				.andExpect(objectEquals( error ));

		//初始化店铺
		testInit();

		//未完成第一步操作执行此步校验
		error  = new ErrorMessage("E224","完成上一步才可进行此步操作");
		mockMvc.perform(put("/shops/apply/step2")
				.params(list2.get(0))
				.header("Authorization", buyerTocken))
				.andExpect(status().is(500))
				.andExpect(objectEquals( error ));

		//完成第一步操作
		mockMvc.perform(put("/shops/apply/step1")
				.params(list1.get(0))
				.header("Authorization", buyerTocken))
				.andExpect(status().is(200));

		//营业执照开始时间不能大于结束时间校验
		error  = new ErrorMessage("E217","营业执照开始时间不能大于结束时间");
		mockMvc.perform(put("/shops/apply/step2")
				.params(list2.get(1))
				.header("Authorization", buyerTocken))
				.andExpect(status().is(500))
				.andExpect(objectEquals( error ));


		//正确性校验
		mockMvc.perform(put("/shops/apply/step2")
				.params(list2.get(0))
				.header("Authorization", buyerTocken))
				.andExpect(status().is(200));
		Map step2 = this.daoSupport.queryForMap("select legal_name,legal_id,legal_img,license_num,license_add,establish_date,licence_start,licence_end,scope,licence_img,organization_code,code_img,taxes_img from es_shop_detail sd left join es_shop s on s.shop_id = sd.shop_id where  s.member_id= ? ", 101);

		Map expected = this.formatMap(list2.get(0));
		expected.remove("license_region");
		Map actual  = this.formatMap(step2);
		Assert.assertEquals(expected, actual);

	}

	/**
	 * 申请开店第三步
	 * @throws Exception
	 */
	@Test
	public void testStep3() throws Exception {

		ErrorMessage error = null;

		//参数性校验
		for (MultiValueMap<String, String> params : step3) {
			String message =  params.get("message").get(0);
			error  = new ErrorMessage("004",message);
			mockMvc.perform(put("/shops/apply/step3").params(params)
					.header("Authorization", buyerTocken))
					.andExpect(status().is(400))
					.andExpect(objectEquals( error ));
		}

		//已拥有店铺会员执行此操作
		error  = new ErrorMessage("E204","店铺在申请中，不允许此操作");
		mockMvc.perform(put("/shops/apply/step3")
				.params(list3.get(0))
				.header("Authorization", sellerTocken))
				.andExpect(status().is(500))
				.andExpect(objectEquals( error ));

		//未初始化店铺执行此操作
		error  = new ErrorMessage("E200","您尚未拥有店铺，不能进行此操作");
		mockMvc.perform(put("/shops/apply/step3")
				.params(list3.get(0))
				.header("Authorization", buyerTocken))
				.andExpect(status().is(500))
				.andExpect(objectEquals( error ));

		//初始化店铺
		testInit();

		//完成第一步操作
		mockMvc.perform(put("/shops/apply/step1")
				.params(list1.get(0))
				.header("Authorization", buyerTocken))
				.andExpect(status().is(200));

		//未完成第二步操作执行此步校验
		error  = new ErrorMessage("E224","完成上一步才可进行此步操作");
		mockMvc.perform(put("/shops/apply/step3")
				.params(list3.get(0))
				.header("Authorization", buyerTocken))
				.andExpect(status().is(500))
				.andExpect(objectEquals( error ));

		//完成第二步操作
		mockMvc.perform(put("/shops/apply/step2")
				.params(list2.get(0))
				.header("Authorization", buyerTocken))
				.andExpect(status().is(200));

		//正确性校验
		mockMvc.perform(put("/shops/apply/step3")
				.params(list3.get(0))
				.header("Authorization", buyerTocken))
				.andExpect(status().is(200));
		Map step3 = this.daoSupport.queryForMap("select bank_account_name,bank_number,bank_name from es_shop_detail sd left join es_shop s on s.shop_id = sd.shop_id where  s.member_id= ? ", 101);

		Map expected = this.formatMap(list3.get(0));
		expected.remove("bank_region");
		Map actual  = this.formatMap(step3);
		Assert.assertEquals(expected, actual);

	}

	/**
	 * 申请开店第四步
	 * @throws Exception
	 */
	@Test
	public void testStep4() throws Exception {

		ErrorMessage error = null;

		//数据性校验
		for (MultiValueMap<String, String> params : step4) {
			String message =  params.get("message").get(0);
			error  = new ErrorMessage("004",message);
			mockMvc.perform(put("/shops/apply/step4").params(params)
					.header("Authorization", buyerTocken))
					.andExpect(status().is(400))
					.andExpect(objectEquals( error ));
		}

		//已拥有店铺会员执行此操作
		error  = new ErrorMessage("E204","店铺在申请中，不允许此操作");
		mockMvc.perform(put("/shops/apply/step4")
				.params(list4.get(0))
				.header("Authorization", sellerTocken))
				.andExpect(status().is(500))
				.andExpect(objectEquals( error ));

		//未初始化店铺执行此操作
		error  = new ErrorMessage("E200","您尚未拥有店铺，不能进行此操作");
		mockMvc.perform(put("/shops/apply/step4")
				.params(list4.get(0))
				.header("Authorization", buyerTocken))
				.andExpect(status().is(500))
				.andExpect(objectEquals( error ));

		//初始化店铺
		testInit();

		//完成第一步操作
		mockMvc.perform(put("/shops/apply/step1")
				.params(list1.get(0))
				.header("Authorization", buyerTocken))
				.andExpect(status().is(200));

		//完成第二步操作
		mockMvc.perform(put("/shops/apply/step2")
				.params(list2.get(0))
				.header("Authorization", buyerTocken))
				.andExpect(status().is(200));

		//未完成第三步操作执行此步校验
		error  = new ErrorMessage("E224","完成上一步才可进行此步操作");
		mockMvc.perform(put("/shops/apply/step4")
				.params(list4.get(0))
				.header("Authorization", buyerTocken))
				.andExpect(status().is(500))
				.andExpect(objectEquals( error ));

		//完成第三步操作
		mockMvc.perform(put("/shops/apply/step3")
				.params(list3.get(0))
				.header("Authorization", buyerTocken))
				.andExpect(status().is(200));

		//店铺名称重复性校验
		error  = new ErrorMessage("E203","店铺名称重复");
		mockMvc.perform(put("/shops/apply/step4")
				.params(list4.get(1))
				.header("Authorization", buyerTocken))
				.andExpect(status().is(500))
				.andExpect(objectEquals( error ));

		//正确性校验
		mockMvc.perform(put("/shops/apply/step4")
				.params(list4.get(0))
				.header("Authorization", buyerTocken))
				.andExpect(status().is(200));
		Map step4 = this.daoSupport.queryForMap("select shop_name,goods_management_category from es_shop_detail sd left join es_shop s on s.shop_id = sd.shop_id where  s.member_id= ? ", 101);

		Map expected = this.formatMap(list4.get(0));
		expected.remove("shop_region");
		Map actual  = this.formatMap(step4);
		Assert.assertEquals(expected, actual);

	}

	/**
	 * 店铺名称重复校验
	 * @throws Exception
	 */
	@Test
	public void testCheckShopName() throws Exception {
		//参数校验
		ErrorMessage error = new ErrorMessage("004","店铺名称必填");
		mockMvc.perform(get("/shops/apply/check-name")
				.header("Authorization", buyerTocken))
				.andExpect(status().is(400))
				.andExpect(objectEquals(error));

		//正确性校验
		String content = mockMvc.perform(get("/shops/apply/check-name")
				.param("shop_name", "测试店铺")
				.header("Authorization", buyerTocken))
				.andExpect(status().is(200))
				.andReturn().getResponse().getContentAsString();
		Assert.assertEquals("true", content);

		//正确性校验
		content = mockMvc.perform(get("/shops/apply/check-name")
				.param("shop_name", "我是店铺")
				.header("Authorization", buyerTocken))
				.andExpect(status().is(200))
				.andReturn().getResponse().getContentAsString();
		Assert.assertEquals("false", content);
	}

	/**
	 * 身份证重复校验
	 * @throws Exception
	 */
	@Test
	public void testCheckIdCard() throws Exception {
		//参数校验
		ErrorMessage error = new ErrorMessage("004","身份证号必填");
		mockMvc.perform(get("/shops/apply/id-card")
				.header("Authorization", buyerTocken))
				.andExpect(status().is(400))
				.andExpect(objectEquals(error));

		//正确性校验
		String content = mockMvc.perform(get("/shops/apply/id-card")
				.param("id_card", "101010101010101010")
				.header("Authorization", buyerTocken))
				.andExpect(status().is(200))
				.andReturn().getResponse().getContentAsString();
		Assert.assertEquals("false", content);
	}

}