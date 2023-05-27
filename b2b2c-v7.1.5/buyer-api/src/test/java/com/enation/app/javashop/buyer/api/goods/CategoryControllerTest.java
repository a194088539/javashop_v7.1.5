package com.enation.app.javashop.buyer.api.goods;

import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author fk
 * @version v2.0
 * @Description: 分类单元测试
 * @date 2018/4/1110:12
 * @since v7.0.0
 */
public class CategoryControllerTest extends BaseTest{


    @Test
    public void testCategory() throws Exception{

        mockMvc.perform(get("/goods/categories/0/children"))
                .andExpect(status().isOk());
    }

    /**
     * (这里用一句话描述这个类的作用)
     *
     * @author zh
     * @version v7.0
     * @since v7.0
     */
    public static class MemberAddressControllerTest {
    }
}
