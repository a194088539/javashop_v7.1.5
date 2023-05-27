package com.enation.app.javashop.manager.api.distribution;

import com.enation.app.javashop.core.distribution.data.DistributionBeforeTest;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 分销提现控制器单元测试
 *
 * @author Chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/24 下午5:11
 */
@Rollback
public class WithdrawControllerTest extends BaseTest {

    String prefix = "/admin/distribution/withdraw";

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
     * 提现申请审核列表
     *
     * @return
     */
    @Test
    public void pageApply() throws Exception {
        mockMvc.perform(get(prefix + "/apply").header("Authorization", superAdmin).param("page_size", "2").param("page_no", "2")).andExpect(status().is(200));
        mockMvc.perform(get(prefix + "/apply").header("Authorization", superAdmin).param("end_time", "2").param("start_time", "2")).andExpect(status().is(200));
        mockMvc.perform(get(prefix + "/apply").header("Authorization", superAdmin).param("uname", "2")).andExpect(status().is(200));
        mockMvc.perform(get(prefix + "/apply").header("Authorization", superAdmin)).andExpect(status().is(200));
    }

    /**
     * 审核提现申请
     */
    @Test
    public void auditing() throws Exception {
        mockMvc.perform(post(prefix + "/auditing").header("Authorization", superAdmin).param("apply_id", "3").param("remark", "remark").param("audit_result", "VIA_AUDITING")).andExpect(status().is(200));
        //重复操作
        mockMvc.perform(post(prefix + "/auditing").header("Authorization", superAdmin).param("apply_id", "3").param("remark", "remark").param("audit_result", "FAIL_AUDITING")).andExpect(objectEquals(new ErrorMessage("1002", "提现申请不可以重复操作。")));
        //错误的参数
        mockMvc.perform(post(prefix + "/auditing").header("Authorization", superAdmin).param("apply_id", "2").param("remark", "remark").param("audit_result", "FAIL_AUDITING1")).andExpect(objectEquals(new ErrorMessage("1000", "分销业务异常，请稍后重试。")));
        //错误的参数
        mockMvc.perform(post(prefix + "/auditing").header("Authorization", superAdmin).param("apply_id", "9999").param("remark", "remark").param("audit_result", "FAIL_AUDITING")).andExpect(objectEquals(new ErrorMessage("1004", "错误的提现申请。")));

    }

    /**
     * 设为已转账
     */
    @Test
    public void accountPaid() throws Exception {
        mockMvc.perform(post(prefix + "/account/end").header("Authorization", superAdmin).param("apply_id", "5").param("remark", "remark")).andExpect(status().is(200));
        //重复操作
        mockMvc.perform(post(prefix + "/account/end").header("Authorization", superAdmin).param("apply_id", "5").param("remark", "remark")).andExpect(objectEquals(new ErrorMessage("1002", "提现申请不可以重复操作。")));
        //错误的参数
        mockMvc.perform(post(prefix + "/account/end").header("Authorization", superAdmin).param("apply_id", "9999").param("remark", "remark")).andExpect(objectEquals(new ErrorMessage("1004", "错误的提现申请。")));

    }
}
