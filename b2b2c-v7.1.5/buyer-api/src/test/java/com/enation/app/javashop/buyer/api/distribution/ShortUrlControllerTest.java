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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 短链接单元测试
 *
 * @author Chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/23 上午8:35
 */
@Rollback
public class ShortUrlControllerTest extends BaseTest {


    private String prefix = "/distribution/su";

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
     * 访问短链接 把会员id加入session中，并跳转页面
     */
    @Test
    public void visit() throws Exception {

        mockMvc.perform(get(prefix + "/visit").header("Authorization", buyer1).param("su", "r6RRNz1"))
                .andExpect(status().is(200));

    }


    /**
     * 生成短链接， 必须登录
     */
    @Test
    public void getShortUrl() throws Exception {
        mockMvc.perform(post(prefix + "/get-short-url").param("goods_id", "1"))
                .andExpect(objectEquals(new ErrorMessage("1001", "用户未登录，请登录后重试")));
        mockMvc.perform(post(prefix + "/get-short-url").header("Authorization", buyer1))
                .andExpect(status().is(200));
    }


}
