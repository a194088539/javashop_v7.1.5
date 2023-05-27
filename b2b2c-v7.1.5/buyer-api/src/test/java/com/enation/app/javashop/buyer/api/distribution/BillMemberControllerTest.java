package com.enation.app.javashop.buyer.api.distribution;

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
 * 分销总结算单元测试
 *
 * @author Chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/23 上午8:32
 */
@Rollback
public class BillMemberControllerTest extends BaseTest {


    private String prefix = "/distribution/bill";

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
     * 根据结算单获取订单信息
     */
    @Test
    public void orderList() throws Exception {
        mockMvc.perform(get(prefix + "/order-list"))
                .andExpect(objectEquals(new ErrorMessage("001", "无权访问")));

        mockMvc.perform(get(prefix + "/order-list").header("Authorization", buyer1).param("bill_id", "2").header("Authorization", buyer1))
                .andExpect(status().is(200));
        mockMvc.perform(get(prefix + "/order-list").header("Authorization", buyer1).param("bill_id", "2").param("member_id", "2").header("Authorization", buyer1))
                .andExpect(status().is(200));
    }

    /**
     * 根据结算单获取退款订单信息
     */
    @Test
    public void sellbackOrderList() throws Exception {
        mockMvc.perform(get(prefix + "/sellback-order-list").header("Authorization", buyer1))
                .andExpect(status().is(400));
        mockMvc.perform(get(prefix + "/sellback-order-list").param("bill_id", "2").header("Authorization", buyer1))
                .andExpect(status().is(200));
        mockMvc.perform(get(prefix + "/sellback-order-list").param("bill_id", "2").param("member_id", "2").header("Authorization", buyer1))
                .andExpect(status().is(200));

    }


    /**
     * 历史业绩
     */
    @Test
    public void historyBill() throws Exception {
        mockMvc.perform(get(prefix + "/history").header("Authorization", buyer1))
                .andExpect(status().is(200));
        mockMvc.perform(get(prefix + "/history").param("member_id", "2").header("Authorization", buyer1))
                .andExpect(status().is(200));

    }


    /**
     * 获取某会员当前月份结算单
     */
    @Test
    public void billMemberVO() throws Exception {
        mockMvc.perform(get(prefix + "/member").param("member_id", "2").header("Authorization", buyer1)).andExpect(status().is(200));
        mockMvc.perform(get(prefix + "/member").header("Authorization", buyer1)).andExpect(status().is(200));
    }


}
