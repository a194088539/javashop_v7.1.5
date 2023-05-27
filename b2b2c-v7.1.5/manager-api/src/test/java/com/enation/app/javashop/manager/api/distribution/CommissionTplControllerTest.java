package com.enation.app.javashop.manager.api.distribution;

import com.enation.app.javashop.core.distribution.data.DistributionBeforeTest;
import com.enation.app.javashop.core.distribution.model.dos.CommissionTpl;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * BackendCommissionTplControllerTest
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-06-11 下午4:21
 */
@Rollback
public class CommissionTplControllerTest extends BaseTest {


    private String prefix = "/admin/distribution/commission-tpl";

    /**
     * 测试参数集合
     */
    private List<MultiValueMap<String, String>> list = null;
    @Autowired
    @Qualifier("distributionDaoSupport")
    private DaoSupport daoSupport;


    @Before
    public void beforeDistribution() {
        DistributionBeforeTest.before(daoSupport);
    }


    /**
     * 添加模版
     */
    @Test
    public void save() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap();
        params.add("tpl_name", "测试模版");
        params.add("tpl_describe", "模版描述");
        params.add("switch_model", "MANUAL");
        params.add("profit", "999.99");
        params.add("num", "999");
        params.add("grade1", "1");
        params.add("grade2", "2");
        params.add("is_default", "1");
        CommissionTpl commissionTpl = new CommissionTpl();
        commissionTpl.setTplName("测试模版");
        commissionTpl.setTplDescribe("模版描述");
        commissionTpl.setGrade1(1D);
        commissionTpl.setGrade2(2D);
        commissionTpl.setIsDefault(1);
        mockMvc.perform(post(prefix).params(params).header("Authorization", superAdmin)).andExpect(
                objectEquals(commissionTpl));


        String[] names = new String[]{"tpl_name", "tpl_describe", "switch_model", "profit", "num", "grade1", "grade2", "is_default", "message"};
        String[] values1 = new String[]{"测试模版", "模版描述", "", "999.99", "999", "1", "2", "1", "切换模式不能为空"};
        String[] values2 = new String[]{"测试模版", "模版描述", "MANUAL", "999.99", "", "1", "2", "1", "下线人数 不能为空"};
        String[] values3 = new String[]{"测试模版", "模版描述", "MANUAL", "999.99", "999", "", "2", "1", "相差1级返利金额 不能为空"};
        String[] values4 = new String[]{"测试模版", "模版描述", "MANUAL", "999.99", "999", "1", "", "1", "相差2级返利金额 不能为空"};
        String[] values5 = new String[]{"测试模版", "模版描述", "MANUAL", "", "999", "1", "2", "1", "利润要求不能为空"};
        String[] values6 = new String[]{"", "模版描述", "MANUAL", "999.99", "999", "1", "2", "1", "模版名称不能为空"};
        String[] values7 = new String[]{"测试模版", "模版描述", "MANUAL", "999.99", "999", "1", "2", "", "默认参数不能为空"};

        list = toMultiValueMaps(names, values1, values2, values3, values4, values5, values6, values7);
        for (MultiValueMap<String, String> param : list) {
            String message = param.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(post(prefix)
                    .params(param).header("Authorization", superAdmin))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
    }

    /**
     * 修改模版
     */
    @Test
    public void saveEdit() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap();
        params.add("tpl_name", "测试模版");
        params.add("tpl_describe", "模版描述");
        params.add("switch_model", "MANUAL");
        params.add("profit", "999.99");
        params.add("num", "999");
        params.add("grade1", "1");
        params.add("grade2", "2");
        params.add("is_default", "1");
        CommissionTpl commissionTpl = new CommissionTpl();
        commissionTpl.setTplName("测试模版");
        commissionTpl.setTplDescribe("模版描述");
        commissionTpl.setGrade1(1D);
        commissionTpl.setGrade2(2D);
        commissionTpl.setIsDefault(1);
        mockMvc.perform(put(prefix + "/1").params(params).header("Authorization", superAdmin)).andExpect(
                objectEquals(commissionTpl));


        String[] names = new String[]{"tpl_name", "tpl_describe", "switch_model", "profit", "num", "grade1", "grade2", "is_default", "message"};
        String[] values1 = new String[]{"测试模版", "模版描述", "", "999.99", "999", "1", "2", "1", "切换模式不能为空"};
        String[] values2 = new String[]{"测试模版", "模版描述", "MANUAL", "999.99", "", "1", "2", "1", "下线人数 不能为空"};
        String[] values3 = new String[]{"测试模版", "模版描述", "MANUAL", "999.99", "999", "", "2", "1", "相差1级返利金额 不能为空"};
        String[] values4 = new String[]{"测试模版", "模版描述", "MANUAL", "999.99", "999", "1", "", "1", "相差2级返利金额 不能为空"};
        String[] values5 = new String[]{"测试模版", "模版描述", "MANUAL", "", "999", "1", "2", "1", "利润要求不能为空"};
        String[] values6 = new String[]{"", "模版描述", "MANUAL", "999.99", "999", "1", "2", "1", "模版名称不能为空"};
        String[] values7 = new String[]{"测试模版", "模版描述", "MANUAL", "999.99", "999", "1", "2", "", "默认参数不能为空"};

        list = toMultiValueMaps(names, values1, values2, values3, values4, values5, values6, values7);
        for (MultiValueMap<String, String> param : list) {
            String message = param.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(put(prefix + "/1")
                    .params(param).header("Authorization", superAdmin))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }


    }


    /**
     * 模板列表
     */
    @Test
    public void listJson() throws Exception {
        mockMvc.perform(get(prefix).header("Authorization", superAdmin)).andExpect(status().is(200));
        mockMvc.perform(get(prefix).param("page_size", "2").param("page_no", "2").header("Authorization", superAdmin)).andExpect(status().is(200));
    }


    /**
     * 获取模板
     */
    @Test
    public void getModel() throws Exception {
        CommissionTpl commissionTpl = new CommissionTpl();
        commissionTpl.setTplName("模版1");
        commissionTpl.setTplDescribe("");
        commissionTpl.setGrade1(1D);
        commissionTpl.setGrade2(5D);
        commissionTpl.setIsDefault(1);
        mockMvc.perform(get(prefix + "/1").header("Authorization", superAdmin)).andExpect(objectEquals(commissionTpl));
    }


    /**
     * 删除模板
     */
    @Test
    public void deleteModel() throws Exception {
        mockMvc.perform(delete(prefix + "/2").header("Authorization", superAdmin)).andExpect(status().is(200));

        //删除已经存在的模版

        ErrorMessage errorMessage= new ErrorMessage("1010","默认模版不允许修改为否!");
        mockMvc.perform(delete(prefix + "/1").header("Authorization", superAdmin)).andExpect(objectEquals(errorMessage));
    }


}
