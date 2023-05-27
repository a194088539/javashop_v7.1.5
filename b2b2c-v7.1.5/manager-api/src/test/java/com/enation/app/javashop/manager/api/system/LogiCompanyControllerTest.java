package com.enation.app.javashop.manager.api.system;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.enation.app.javashop.core.system.model.dos.LogisticsCompanyDO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;

/**
 * 物流公司测试用例
 *
 * @author zhangjiping
 * @version v7.0.0
 * @since v7.0.0
 * 2018年4月19日 下午3:23:49
 */
@Transactional(value = "systemTransactionManager", rollbackFor = Exception.class)
public class LogiCompanyControllerTest extends BaseTest {

    @Autowired
    @Qualifier("systemDaoSupport")
    private DaoSupport daoSupport;

    List<MultiValueMap<String, String>> list = null;
    String[] names = new String[]{"name", "code", "kdcode", "is_waybill", "message"};
    String[] names1 = new String[]{"name", "code", "kdcode", "is_waybill"};

    LogisticsCompanyDO logiCompany = null;
    LogisticsCompanyDO logiCompany1 = null;

    @Before
    public void insterData() {

        this.daoSupport.execute("delete from es_logi_company");
        this.daoSupport.insert("es_logi_company", new LogisticsCompanyDO("测试物流公司1", "ceshi1", "CSO1", 0));
        int lastId = this.daoSupport.getLastId("es_logi_company");
        logiCompany = this.daoSupport.queryForObject("select * from es_logi_company where id = ? ", LogisticsCompanyDO.class, lastId);

        this.daoSupport.insert("es_logi_company", new LogisticsCompanyDO("测试物流公司3", "ceshi3", "CSO3", 0));
        lastId = this.daoSupport.getLastId("es_logi_company");
        logiCompany1 = this.daoSupport.queryForObject("select * from es_logi_company where id = ? ", LogisticsCompanyDO.class, lastId);

        String[] values1 = new String[]{"", "ceshi2", "CSO2", "0", "物流公司名称必填"};
        String[] values2 = new String[]{"测试物流公司2", "", "CSO2", "0", "物流公司code必填"};
        String[] values3 = new String[]{"测试物流公司2", "ceshi2", "", "0", "快递鸟物流公司code必填"};
        String[] values4 = new String[]{"测试物流公司2", "ceshi2", "CSO2", "", "是否支持电子面单必填"};
        list = toMultiValueMaps(names, values1, values2, values3, values4);
    }

    /**
     * 获取物流公司列表
     *
     * @throws Exception
     */
    @Test
    public void testList() throws Exception {

        //正确性校验
        mockMvc.perform(get("/admin/systems/logi-companies")
                .header("Authorization", superAdmin)
                .param("page_no", "1")
                .param("page_size", "10"))
                .andExpect(jsonPath("$.data.[0].name").value("测试物流公司1"))
                .andExpect(jsonPath("$.data.[0].kdcode").value("CSO1"));
    }

    /**
     * 添加物流公司
     *
     * @throws Exception
     */
    @Test
    public void testAdd() throws Exception {
        String[] values = new String[]{"测试物流公司2", "ceshi2", "CSO2", "0"};
        String[] values1 = new String[]{"测试物流公司1", "ceshi2", "CSO2", "0"};
        String[] values2 = new String[]{"测试物流公司2", "ceshi1", "CSO2", "0"};
        String[] values3 = new String[]{"测试物流公司2", "ceshi2", "CSO1", "0"};
        List<MultiValueMap<String, String>> logi = toMultiValueMaps(names1, values, values1, values2, values3);

        //参数性校验
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(post("/admin/systems/logi-companies")
                    .header("Authorization", superAdmin)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }

        //物流公司名称重复校验
        ErrorMessage error = new ErrorMessage("E211", "物流公司名称重复");
        mockMvc.perform(post("/admin/systems/logi-companies")
                .header("Authorization", superAdmin)
                .params(logi.get(1)))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        //物流公司代码重复校验
        error = new ErrorMessage("E213", "物流公司代码重复");
        mockMvc.perform(post("/admin/systems/logi-companies")
                .header("Authorization", superAdmin)
                .params(logi.get(2)))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        //快递鸟公司代码重复校验
        error = new ErrorMessage("E212", "快递鸟公司代码重复");
        mockMvc.perform(post("/admin/systems/logi-companies")
                .header("Authorization", superAdmin)
                .params(logi.get(3)))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        //正确性校验
        String contentAsString = mockMvc.perform(post("/admin/systems/logi-companies")
                .header("Authorization", superAdmin)
                .params(logi.get(0)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        LogisticsCompanyDO jsonToObject = JsonUtil.jsonToObject(contentAsString, LogisticsCompanyDO.class);
        Map logiCompany = this.daoSupport.queryForMap("select name,code,kdcode,is_waybill from es_logi_company where id = ? ", jsonToObject.getId());
        Map expected = this.formatMap(logi.get(0));
        Map actual = this.formatMap(logiCompany);
        Assert.assertEquals(expected, actual);

    }

    /**
     * 编辑物流公司
     *
     * @throws Exception
     */
    @Test
    public void testEdit() throws Exception {

        String[] values = new String[]{"测试物流公司2", "ceshi2", "CSO2", "0"};
        String[] values1 = new String[]{"测试物流公司3", "ceshi2", "CSO2", "0"};
        String[] values2 = new String[]{"测试物流公司2", "ceshi3", "CSO2", "0"};
        String[] values3 = new String[]{"测试物流公司2", "ceshi2", "CSO3", "0"};
        List<MultiValueMap<String, String>> logi = toMultiValueMaps(names1, values, values1, values2, values3);

        //参数性校验
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(put("/admin/systems/logi-companies/" + logiCompany.getId())
                    .header("Authorization", superAdmin)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }

        //物流公司有效性校验
        ErrorMessage error = new ErrorMessage("E214", "物流公司不存在");
        mockMvc.perform(put("/admin/systems/logi-companies/-1")
                .header("Authorization", superAdmin)
                .params(logi.get(1)))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        //物流公司名称重复校验
        error = new ErrorMessage("E211", "物流公司名称重复");
        mockMvc.perform(put("/admin/systems/logi-companies/" + logiCompany.getId())
                .header("Authorization", superAdmin)
                .params(logi.get(1)))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        //物流公司代码重复校验
        error = new ErrorMessage("E213", "物流公司代码重复");
        mockMvc.perform(put("/admin/systems/logi-companies/" + logiCompany.getId())
                .header("Authorization", superAdmin)
                .params(logi.get(2)))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        //快递鸟公司代码重复校验
        error = new ErrorMessage("E212", "快递鸟公司代码重复");
        mockMvc.perform(put("/admin/systems/logi-companies/" + logiCompany.getId())
                .header("Authorization", superAdmin)
                .params(logi.get(3)))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        //正确性校验
        String json = mockMvc.perform(put("/admin/systems/logi-companies/" + logiCompany.getId())
                .header("Authorization", superAdmin)
                .params(logi.get(0)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        Integer id = logiCompany.getId();
        Map logiCompany = this.daoSupport.queryForMap("select name,code,kdcode,is_waybill  from es_logi_company where id = ? ", id);
        Map expected = this.formatMap(logi.get(0));
        Map actual = this.formatMap(logiCompany);
        Assert.assertEquals(expected, actual);
    }

    /**
     * 删除物流公司
     *
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception {

        //物流公司存在性校验
        ErrorMessage error = new ErrorMessage("E214", "物流公司不存在");
        mockMvc.perform(delete("/admin/systems/logi-companies")
                .header("Authorization", superAdmin)
                .param("id", "-1"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        //正确性校验
        mockMvc.perform(delete("/admin/systems/logi-companies")
                .header("Authorization", superAdmin)
                .param("id", logiCompany.getId().toString()));
        int queryForInt = this.daoSupport.queryForInt("select count(*) from es_logi_company where id = ? ", logiCompany.getId());
        Assert.assertEquals(0, queryForInt);
    }

    /**
     * 查询物流公司
     *
     * @throws Exception
     */
    @Test
    public void testGet() throws Exception {
        //正确性校验
        String json = mockMvc.perform(get("/admin/systems/logi-companies/" + logiCompany.getId())
                .header("Authorization", superAdmin))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        LogisticsCompanyDO lCompanyDO = JsonUtil.jsonToObject(json, LogisticsCompanyDO.class);
        Assert.assertEquals(logiCompany, lCompanyDO);
    }
}
