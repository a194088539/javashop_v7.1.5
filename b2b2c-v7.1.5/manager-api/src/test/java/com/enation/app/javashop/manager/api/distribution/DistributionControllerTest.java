package com.enation.app.javashop.manager.api.distribution;

import com.enation.app.javashop.core.distribution.data.DistributionBeforeTest;
import com.enation.app.javashop.core.distribution.data.DistributionBeforeTest;
import com.enation.app.javashop.core.distribution.exception.DistributionErrorCode;
import com.enation.app.javashop.core.distribution.service.DistributionManager;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.service.MemberManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 后台分销商管理 单元测试
 *
 * @author Chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/25 上午10:55
 */
@Rollback
public class DistributionControllerTest extends BaseTest {

    private String prefix = "/admin/distribution/member";

    /**
     * 测试参数集合
     */
    private List<MultiValueMap<String, String>> list = null;
    @Autowired
    @Qualifier("distributionDaoSupport")
    private DaoSupport daoSupport;

    @MockBean
    private MemberManager memberManager;

    @Before
    public void beforeDistribution() {

        DistributionBeforeTest.before(daoSupport);

        Member member = new Member();
        member.setUname("test1");
        member.setMemberId(1);
        when (memberManager.getModel(Mockito.anyInt())).thenReturn(member);

    }


    @Autowired
    private DistributionManager distributionManager;


    /**
     * 集合
     */
    @Test
    public void page() throws Exception {
        mockMvc.perform(get(prefix).header("Authorization", superAdmin).param("member_name", "l")).andExpect(status().is(200));
        mockMvc.perform(get(prefix).header("Authorization", superAdmin)).andExpect(status().is(200));
    }


    /**
     * 修改模版
     */
    @Test
    public void changeTpl() throws Exception {
        //参数不足
        mockMvc.perform(put(prefix + "/tpl").header("Authorization", superAdmin).param("tpl_id", "1")).andExpect(objectEquals(new ErrorMessage(DistributionErrorCode.E1011.code(), DistributionErrorCode.E1011.des())));
        mockMvc.perform(put(prefix + "/tpl").header("Authorization", superAdmin).param("member_id", "1")).andExpect(objectEquals(new ErrorMessage(DistributionErrorCode.E1011.code(), DistributionErrorCode.E1011.des())));
        mockMvc.perform(put(prefix + "/tpl").header("Authorization", superAdmin)).andExpect(objectEquals(new ErrorMessage(DistributionErrorCode.E1011.code(), DistributionErrorCode.E1011.des())));

        //参数无效
        mockMvc.perform(put(prefix + "/tpl").header("Authorization", superAdmin).param("member_id", "999").param("tpl_id", "1")).andExpect(objectEquals(new ErrorMessage(DistributionErrorCode.E1000.code(), DistributionErrorCode.E1000.des())));
        mockMvc.perform(put(prefix + "/tpl").header("Authorization", superAdmin).param("member_id", "1").param("tpl_id", "999")).andExpect(objectEquals(new ErrorMessage(DistributionErrorCode.E1000.code(), DistributionErrorCode.E1000.des())));

        //正常请求
        mockMvc.perform(put(prefix + "/tpl").header("Authorization", superAdmin).param("member_id", "1").param("tpl_id", "1")).andExpect(status().is(200));

    }


}
