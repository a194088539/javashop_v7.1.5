package com.enation.app.javashop.manager.api.distribution;

import com.enation.app.javashop.core.distribution.data.DistributionBeforeTest;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 分销商总结算单单元测试
 *
 * @author Chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/24 下午2:47
 */
@Rollback
public class BillTotalControllerTest extends BaseTest {


    private String prefix = "/admin/distribution/bill/total";

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


    @Test
    public void page() throws Exception {
        mockMvc.perform(get(prefix).header("Authorization", superAdmin)).andExpect(status().is(200));
    }


}
