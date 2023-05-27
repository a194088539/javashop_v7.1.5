package com.enation.app.javashop.buyer.api.distribution;

import com.enation.app.javashop.core.distribution.data.DistributionBeforeTest;
import com.enation.app.javashop.core.distribution.exception.DistributionErrorCode;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import io.swagger.annotations.ApiOperation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 提现api
 *
 * @author Chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/24 上午7:09
 */
@Rollback
public class WithdrawControllerTest extends BaseTest {


    private String prefix = "/distribution/withdraw";

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


    @ApiOperation("保存 提现参数")
    @Test
    public void saveWithdrawWay() throws Exception {

        mockMvc.perform(put(prefix + "/params"))
                .andExpect(objectEquals(new ErrorMessage("001", "无权访问")));
        mockMvc.perform(put(prefix + "/params").header("Authorization", buyer1))
                .andExpect(status().is(200));

    }


    /**
     * 获取 提现参数
     */
    @Test
    public void getWithdrawWay() throws Exception {
        mockMvc.perform(get(prefix + "/params"))
                .andExpect(objectEquals(new ErrorMessage("001", "无权访问")));
        mockMvc.perform(get(prefix + "/params").header("Authorization", buyer1))
                .andExpect(status().is(200));
    }

    @ApiOperation("提现申请")
    @Test
    public void applyWithdraw() throws Exception {
        mockMvc.perform(post(prefix + "/apply-withdraw"))
                .andExpect(objectEquals(new ErrorMessage("001", "无权访问")));
        mockMvc.perform(post(prefix + "/apply-withdraw").header("Authorization", buyer1))
                .andExpect(objectEquals(new ErrorMessage("1011", "参数不足!")));
        mockMvc.perform(post(prefix + "/apply-withdraw").header("Authorization", buyer1).param("apply_money", "1"))
                .andExpect(status().is(200));

    }

    @ApiOperation("提现记录")
    @Test
    public void applyWithdrawPage() throws Exception {
        mockMvc.perform(get(prefix + "/apply-history"))
                .andExpect(objectEquals(new ErrorMessage("001", "无权访问")));

        mockMvc.perform(get(prefix + "/apply-history").header("Authorization", buyer1))
                .andExpect(status().is(200));
    }


    @ApiOperation("可提现金额")
    @Test
    public void canRebate() throws Exception {
        mockMvc.perform(get(prefix + "/can-rebate"))
                .andExpect(objectEquals(new ErrorMessage("001", "无权访问")));

        mockMvc.perform(get(prefix + "/can-rebate").header("Authorization", buyer1))
                .andExpect(status().is(200));

    }


}
