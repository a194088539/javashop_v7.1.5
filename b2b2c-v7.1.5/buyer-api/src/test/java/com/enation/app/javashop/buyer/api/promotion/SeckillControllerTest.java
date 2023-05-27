package com.enation.app.javashop.buyer.api.promotion;

import com.enation.app.javashop.core.promotion.seckill.service.SeckillRangeManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 限时抢购商品测试
 *
 * @author Snow create in 2018/7/30
 * @version v2.0
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager",rollbackFor = Exception.class)
public class SeckillControllerTest extends BaseTest {


    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private SeckillRangeManager seckillRangeManager;

    @Before
    public void testData(){

        List<Integer> list = new ArrayList<>();
        list.add(2);
        list.add(5);
        list.add(15);
        list.add(18);
        list.add(21);
        list.add(23);

        this.seckillRangeManager.addList(list,1);

    }


    @Test
    public void test() throws Exception {

        String resultJson = mockMvc.perform(get("/promotions/seckill/time-line")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

    }

}
