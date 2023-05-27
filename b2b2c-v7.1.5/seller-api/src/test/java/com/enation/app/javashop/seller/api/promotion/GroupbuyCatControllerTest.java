package com.enation.app.javashop.seller.api.promotion;

import com.enation.app.javashop.core.promotion.groupbuy.model.dos.GroupbuyCatDO;
import com.enation.app.javashop.core.promotion.groupbuy.service.GroupbuyCatManager;
import com.enation.app.javashop.core.promotion.minus.model.vo.MinusVO;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 团购分类测试
 *
 * @author Snow create in 2018/7/12
 * @version v2.0
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager",rollbackFor = Exception.class)
public class GroupbuyCatControllerTest extends BaseTest {

    @Autowired
    private GroupbuyCatManager groupbuyCatManager;

    @Before
    public void testData() throws Exception {

        GroupbuyCatDO catDO = new GroupbuyCatDO();
        catDO.setCatName("测试分类");
        this.groupbuyCatManager.add(catDO);

    }

    @Test
    public void testList() throws Exception {

        String resultJson = mockMvc.perform(get("/seller/promotion/group-buy-cats")
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("parent_id","0"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        List list = JsonUtil.jsonToList(resultJson,GroupbuyCatDO.class);
        if(list == null ||list.isEmpty() ){
            throw new RuntimeException("团购分类测试用例错误");
        }

    }


}
