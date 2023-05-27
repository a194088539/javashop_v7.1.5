package com.enation.app.javashop.buyer.api.distribution;


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
 * 分销商控制器单元测试
 *
 * @author Chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/23 上午8:34
 */
@Rollback
public class DistributionControllerTest extends BaseTest {


    private String prefix = "/distribution";

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
     * 获取下级分销商
     */
    @Test
    public void getLowerDistributorList() throws Exception {
        mockMvc.perform(get(prefix + "/lower-list").header("Authorization", buyer1))
                .andExpect(status().is(200));
    }

    /**
     * 获取推荐我的人
     */
    @Test
    public void recommendMe() throws Exception {
        mockMvc.perform(get(prefix + "/recommend-me").header("Authorization", buyer1))
                .andExpect(status().is(200));

    }


}
