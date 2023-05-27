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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 统计单元测试
 *
 * @author Chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/24 下午3:15
 */
@Rollback
public class DistributionStatisticControllerTest extends BaseTest {

    String prefix = "/admin/distribution/statistic";

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
     * 订单金额统计
     *
     * @throws Exception
     */
    @Test
    public void order() throws Exception {
        mockMvc.perform(get(prefix + "/order").header("Authorization", superAdmin)).andExpect(objectEquals(new ErrorMessage("1011", "参数不足!")));
        mockMvc.perform(get(prefix + "/order").header("Authorization", superAdmin).param("member_id", "1")).andExpect(status().is(200));
    }

    /**
     * 订单数量统计
     *
     * @throws Exception
     */
    @Test
    public void count() throws Exception {
        mockMvc.perform(get(prefix + "/count").header("Authorization", superAdmin)).andExpect(objectEquals(new ErrorMessage("1011", "参数不足!")));
        mockMvc.perform(get(prefix + "/count").header("Authorization", superAdmin).param("member_id", "1")).andExpect(status().is(200));
    }


    @Test
    /**
     * 订单返现统计
     */
    public void push() throws Exception {
        mockMvc.perform(get(prefix + "/push").header("Authorization", superAdmin)).andExpect(objectEquals(new ErrorMessage("1011", "参数不足!")));
        mockMvc.perform(get(prefix + "/push").header("Authorization", superAdmin).param("member_id", "1")).andExpect(status().is(200));

    }


    /**
     * 店铺返现统计
     */
    @Test
    public void pushShop() throws Exception {
        mockMvc.perform(get(prefix + "/push/seller").header("Authorization", superAdmin).param("member_id", "1")).andExpect(status().is(200));
    }


}
