package com.enation.app.javashop.manager.api.shop;

import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.service.MemberManager;
import com.enation.app.javashop.core.payment.service.PaymentMethodManager;
import com.enation.app.javashop.core.shop.model.dos.ShopDO;
import com.enation.app.javashop.core.shop.model.enums.ShopStatusEnum;
import com.enation.app.javashop.core.shop.model.vo.ShopVO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;

import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.when;

/**
 * 店铺相关单元测试
 *
 * @author zhangjiping
 * @version v7.0.0
 * @since v7.0.0
 * 2018年4月9日 下午3:01:22
 */
@Transactional(value = "memberTransactionManager", rollbackFor = Exception.class)
public class ShopControllerTest extends BaseTest {

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport daoSupport;

    @MockBean
    private MemberManager memberManager;

    List<MultiValueMap<String, String>> list1 = null;
    List<MultiValueMap<String, String>> list2 = null;
    int shopId = 0;
    int shopId1 = 0;
    String[] name = new String[]{"company_name", "company_address", "company_phone", "company_email", "employee_num", "reg_money", "link_name", "link_phone"
            , "legal_name", "legal_id", "legal_img", "license_num", "license_region", "license_add", "establish_date", "licence_start", "licence_end", "scope", "licence_img", "organization_code", "code_img", "taxes_img"
            , "bank_account_name", "bank_number", "bank_name", "bank_region"
            , "shop_name", "goods_management_category", "shop_region"
            , "member_id", "message"};
    String[] name1 = new String[]{"company_name", "company_address", "company_phone", "company_email", "employee_num", "reg_money", "link_name", "link_phone"
            , "legal_name", "legal_id", "legal_img", "license_num", "license_region", "license_add", "establish_date", "licence_start", "licence_end", "scope", "licence_img", "organization_code", "code_img", "taxes_img"
            , "bank_account_name", "bank_number", "bank_name", "bank_region"
            , "shop_name", "goods_management_category", "shop_region"
            , "member_id"};

    @Before
    public void insertTestData() {

        this.daoSupport.execute("delete from es_shop_detail");
        this.daoSupport.execute("delete from es_shop");

        Map map = new HashMap();
        map.put("member_id", 100);
        map.put("member_name", "测试会员");
        map.put("shop_name", "测试店铺");
        map.put("shop_disable", ShopStatusEnum.OPEN.name());
        this.daoSupport.insert("es_shop", map);
        shopId = this.daoSupport.getLastId("es_shop");

        map.clear();
        map.put("shop_id", shopId);
        this.daoSupport.insert("es_shop_detail", map);

        map.clear();
        map.put("member_id", 110);
        map.put("member_name", "测试会员1");
        map.put("shop_name", "测试店铺1");
        map.put("shop_disable", ShopStatusEnum.APPLY.name());
        this.daoSupport.insert("es_shop", map);
        shopId1 = this.daoSupport.getLastId("es_shop");

        map.clear();
        map.put("shop_id", shopId1);
        this.daoSupport.insert("es_shop_detail", map);

    }

    /**
     * 获取店铺列表
     *
     * @throws Exception
     */
    @Test
    public void testList() throws Exception {

        mockMvc.perform(get("/admin/shops")
                .header("Authorization", superAdmin)
                .param("shop_name", "测试店铺")
                .param("page_no", "1")
                .param("page_size", "10"))
                .andExpect(jsonPath("$.data.[0].shop_name").value("测试店铺"))
                .andExpect(jsonPath("$.data.[0].shop_id").value(shopId));
    }

    /**
     * 禁用店铺
     *
     * @throws Exception
     */
    @Test
    public void testDisShop() throws Exception {

        //无效店铺校验
        ErrorMessage error = new ErrorMessage("E206", "不存在此店铺");
        mockMvc.perform(put("/admin/shops/disable/-1")
                .header("Authorization", superAdmin))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        //正确性校验
        mockMvc.perform(put("/admin/shops/disable/" + shopId)
                .header("Authorization", superAdmin))
                .andExpect(status().is(200));
        String shopDisable = this.daoSupport.queryForString("select shop_disable from es_shop where shop_id = ? ", shopId);
        Assert.assertEquals(ShopStatusEnum.CLOSED.name(), shopDisable);
    }

    /**
     * 开启店铺
     *
     * @throws Exception
     */
    @Test
    public void testUseShop() throws Exception {

        //无效店铺校验
        ErrorMessage error = new ErrorMessage("E206", "不存在此店铺");
        mockMvc.perform(put("/admin/shops/enable/-1")
                .header("Authorization", superAdmin))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        //正确性校验
        mockMvc.perform(put("/admin/shops/enable/" + shopId)
                .header("Authorization", superAdmin))
                .andExpect(status().is(200));
        String shopDisable = this.daoSupport.queryForString("select shop_disable from es_shop where shop_id = ? ", shopId);
        Assert.assertEquals(ShopStatusEnum.OPEN.name(), shopDisable);
    }


    /**
     * 编辑审核店铺
     *
     * @throws Exception
     */
    @Test
    public void testEdit() throws Exception {

        String[] values1 = new String[]{"", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "18", "公司名称必填"};
        String[] values2 = new String[]{"测试公司", "", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "18", "公司地址必填"};
        String[] values3 = new String[]{"测试公司", "测试地址", "", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "18", "公司电话必填"};
        String[] values4 = new String[]{"测试公司", "测试地址", "12345678910", "", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "18", "电子邮箱必填"};
        String[] values5 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "18", "员工总数必填"};
        String[] values6 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "-1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "18", "员工总数必须大于零"};
        String[] values7 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "18", "注册资金必填"};
        String[] values8 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "-1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "18", "注册资金必须大于零"};
        String[] values9 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "18", "联系人姓名必填"};
        String[] values10 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "18", "联系人电话必填"};
        String[] values11 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "18", "法人姓名必填"};
        String[] values13 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "18", "身份证长度不符"};
        String[] values14 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "18", "法人身份证照片必填"};
        String[] values15 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "18", "营业执照号必填"};
        String[] values22 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "18", "营业执照详细地址必填"};
        String[] values23 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "18", "成立日期必填"};
        String[] values24 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "18", "营业执照有效期开始时间必填"};
        String[] values25 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "18", "营业执照有效期结束时间必填"};
        String[] values26 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "18", "法定经营范围必填"};
        String[] values27 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "18", "营业执照电子版必填"};
        String[] values31 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "18", "银行开户名必填"};
        String[] values32 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "18", "银行开户账号必填"};
        String[] values33 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "18", "开户银行支行名称必填"};
        String[] values40 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "", "1,4,35,38,56,79,85,86", "2862"
                , "18", "店铺名称必填"};
        String[] values41 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "", "2862"
                , "18", "店铺经营类目必填"};
        String[] values48 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "", "会员Id不能为空"};
        list1 = toMultiValueMaps(name, values1, values2, values3, values4, values5, values6, values7, values8, values9, values10
                , values11, values13, values14, values15, values22, values23, values24, values25, values26, values27
                , values31, values32, values33
                , values40, values41
                , values48);


        String[] values = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000.00", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试的店铺", "1,4,35,38,56,79,85,86", "2862"
                , "100"};

        List<MultiValueMap<String, String>> shops = toMultiValueMaps(name1, values);
        ErrorMessage error = null;
        //参数性校验
        for (MultiValueMap<String, String> params : list1) {
            String message = params.get("message").get(0);
            error = new ErrorMessage("004", message);
            mockMvc.perform(put("/admin/shops/" + shopId)
                    .header("Authorization", superAdmin)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        this.daoSupport.execute("delete from es_shop_themes");
        //店铺模版不存在校验
        error = new ErrorMessage("E202", "店铺模版不存在,请设置店铺模版");
        mockMvc.perform(put("/admin/shops/" + shopId)
                .header("Authorization", superAdmin)
                .params(shops.get(0))
                .param("pass", "1"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        Map map = new HashMap();
        map.put("name", "测试店铺模版");
        map.put("image_path", "/admin/shop/themes");
        map.put("is_default", 1);
        map.put("type", "PC");
        this.daoSupport.insert("es_shop_themes", map);
        map.put("type", "WAP");
        this.daoSupport.insert("es_shop_themes", map);
        Member member = new Member();
        member.setUname("测试");
        member.setMemberId(1);
        when(memberManager.getModel(100)).thenReturn(member);
        //正确性校验,管理员审核通过
        mockMvc.perform(put("/admin/shops/" + shopId1)
                .header("Authorization", superAdmin)
                .params(shops.get(0))
                .param("pass", "1"));
        Map shop = this.daoSupport.queryForMap("select company_name,company_address,company_phone,company_email,employee_num,reg_money,link_name,link_phone" +
                " ,legal_name,legal_id,legal_img,license_num,license_add,establish_date,licence_start,licence_end,scope,licence_img,organization_code,code_img,taxes_img" +
                " ,bank_account_name,bank_number,bank_name " +
                " ,shop_name,goods_management_category" +
                " ,member_id from es_shop s left join es_shop_detail d on  s.shop_id = d.shop_id where s.shop_id = ?", shopId1);
        Map expected = this.formatMap(shops.get(0));
        Map actual = this.formatMap(shop);
        expected.remove("license_region");
        expected.remove("bank_region");
        expected.remove("shop_region");
        Assert.assertEquals(expected, actual);
        String shop_disable = this.daoSupport.queryForString("select shop_disable from es_shop where shop_id = ? ", shopId1);
        Assert.assertEquals(ShopStatusEnum.OPEN.name(), "OPEN");
        //正确性校验,管理员审核拒绝
        mockMvc.perform(put("/admin/shops/" + shopId)
                .header("Authorization", superAdmin)
                .params(shops.get(0))
                .param("pass", "0"));
        shop_disable = this.daoSupport.queryForString("select shop_disable from es_shop where shop_id = ? ", shopId1);
        Assert.assertEquals(ShopStatusEnum.REFUSED.name(), "REFUSED");

    }

    /**
     * 后台添加店铺
     *
     * @throws Exception
     */
    @Test
    public void testSave() throws Exception {

        String[] values51 = new String[]{"", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "48", "公司名称必填"};
        String[] values52 = new String[]{"测试公司", "", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "48", "公司地址必填"};
        String[] values53 = new String[]{"测试公司", "测试地址", "", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "48", "公司电话必填"};
        String[] values54 = new String[]{"测试公司", "测试地址", "12345678910", "", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "48", "电子邮箱必填"};
        String[] values55 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "48", "员工总数必填"};
        String[] values56 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "-1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "48", "员工总数必须大于零"};
        String[] values57 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "48", "注册资金必填"};
        String[] values58 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "-1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "48", "注册资金必须大于零"};
        String[] values59 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "48", "联系人姓名必填"};
        String[] values60 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "48", "联系人电话必填"};
        String[] values61 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "48", "法人姓名必填"};
        String[] values63 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "48", "身份证长度不符"};
        String[] values64 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "48", "法人身份证照片必填"};
        String[] values65 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "48", "营业执照号必填"};
        String[] values72 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "48", "营业执照详细地址必填"};
        String[] values73 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "48", "成立日期必填"};
        String[] values74 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "48", "营业执照有效期开始时间必填"};
        String[] values75 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "48", "营业执照有效期结束时间必填"};
        String[] values76 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "48", "法定经营范围必填"};
        String[] values77 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "48", "营业执照电子版必填"};
        String[] values81 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "48", "银行开户名必填"};
        String[] values82 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "48", "银行开户账号必填"};
        String[] values83 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "48", "开户银行支行名称必填"};
        String[] values90 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "", "1,4,35,38,56,79,85,86", "2862"
                , "48", "店铺名称必填"};
        String[] values91 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "", "2862"
                , "48", "店铺经营类目必填"};

        String[] values98 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "", "会员Id不能为空"};
        list2 = toMultiValueMaps(name, values51, values52, values53, values54, values55, values56, values57, values58, values59, values60
                , values61, values63, values64, values65, values72, values73, values74, values75, values76, values77
                , values81, values82, values83
                , values90, values91
                , values98);


        String[] values = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000.00", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "店铺测试1", "1,4,35,38,56,79,85,86", "2862"
                , "101"};
        String[] values1 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "新店铺测试", "1,4,35,38,56,79,85,86", "2862"
                , "100"};
        String[] values2 = new String[]{"测试公司", "测试地址", "12345678910", "123456@qq.com", "1000", "1000", "测试联系人", "12345678910",
                "我是法人", "000000000000000000", "http://www.javamall.com.cn/images/logonew.jpg", "532501100006302", "2862", "朗威大厦", "1427854939", "1427854939", "1534242083", "电商", "http://www.javamall.com.cn/images/logonew.jpg", "123456789", "http://www.javamall.com.cn/images/logonew.jpg", "http://www.javamall.com.cn/images/logonew.jpg",
                "北京银行", "123456", "海淀北京银行", "2862",
                "测试店铺", "1,4,35,38,56,79,85,86", "2862"
                , "101"};
        List<MultiValueMap<String, String>> shops = toMultiValueMaps(name1, values, values1, values2);

        ErrorMessage error = null;
        //数据性校验
        for (MultiValueMap<String, String> params : list2) {
            String message = params.get("message").get(0);
            error = new ErrorMessage("004", message);
            mockMvc.perform(post("/admin/shops")
                    .header("Authorization", superAdmin)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }

        //已存在店铺校验
        error = new ErrorMessage("E207", "会员已存在店铺，不可重复添加");
        mockMvc.perform(post("/admin/shops")
                .header("Authorization", superAdmin)
                .params(shops.get(1)))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        this.daoSupport.execute("delete from es_shop_themes");

        //店铺模版不存在校验
        error = new ErrorMessage("E202", "店铺模版不存在,请设置店铺模版");
        mockMvc.perform(post("/admin/shops")
                .header("Authorization", superAdmin)
                .params(shops.get(2)))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));


        Map map = new HashMap();
        map.put("name", "测试店铺模版");
        map.put("image_path", "/shop/themes");
        map.put("is_default", 1);
        map.put("type", "PC");
        this.daoSupport.insert("es_shop_themes", map);
        map.put("type", "WAP");
        this.daoSupport.insert("es_shop_themes", map);

        //店铺重复校验
        error = new ErrorMessage("E203", "店铺名称重复");
        mockMvc.perform(post("/admin/shops")
                .header("Authorization", superAdmin)
                .params(shops.get(2)))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        Member member = new Member();
        member.setMemberId(101);
        when(memberManager.getModel(101)).thenReturn(member);
        //正确性校验
        mockMvc.perform(post("/admin/shops")
                .header("Authorization", superAdmin)
                .params(shops.get(0)))
                .andExpect(status().is(200));

    }

}