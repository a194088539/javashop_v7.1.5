package com.enation.app.javashop.manager.api.distribution;

import com.enation.app.javashop.core.distribution.data.DistributionBeforeTest;
import com.enation.app.javashop.core.distribution.exception.DistributionErrorCode;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 分销订单订单单元测试
 *
 * @author Chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/28 上午11:12
 */
public class DistributionOrderControllerTest extends BaseTest {


    private String prefix = "/admin/distribution/order";

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
     * 结算单 分销订单查询
     */
    @Test
    public void billOrder() throws Exception {
        mockMvc.perform(get(prefix).param("bill_id", "1").header("Authorization", superAdmin)).andExpect(objectEquals(new ErrorMessage(DistributionErrorCode.E1011.code(), DistributionErrorCode.E1011.des())));
        mockMvc.perform(get(prefix).param("member_id", "1").header("Authorization", superAdmin)).andExpect(objectEquals(new ErrorMessage(DistributionErrorCode.E1011.code(), DistributionErrorCode.E1011.des())));
        mockMvc.perform(get(prefix).param("bill_id", "1").header("Authorization", superAdmin).param("member_id", "1")).andExpect(status().is(200));
    }

    /**
     * 结算单 分销退货订单查询
     */
    @Test
    public void billSellbackOrder() throws Exception {
        mockMvc.perform(get(prefix + "/sellback").header("Authorization", superAdmin).param("bill_id", "1")).andExpect(objectEquals(new ErrorMessage(DistributionErrorCode.E1011.code(), DistributionErrorCode.E1011.des())));
        mockMvc.perform(get(prefix + "/sellback").header("Authorization", superAdmin).param("member_id", "1")).andExpect(objectEquals(new ErrorMessage(DistributionErrorCode.E1011.code(), DistributionErrorCode.E1011.des())));
        mockMvc.perform(get(prefix + "/sellback").header("Authorization", superAdmin).param("bill_id", "1").param("member_id", "1")).andExpect(status().is(200));
    }
}
