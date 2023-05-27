package com.enation.app.javashop.buyer.api.goods;

import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author fk
 * @version v2.0
 * @Description: 标签商品单元测试
 * @date 2018/4/1110:23
 * @since v7.0.0
 */
public class TagsControllerTest extends BaseTest{

    List<MultiValueMap<String, String>> list = null;

    @Before
    public void insertTestData(){
        String[] names = new String[]{"seller_id","mark","num","message"};
        String[] values1 = new String[]{"","hot","5","店铺不能为空"};
        String[] values2 = new String[]{"1","111","5","标签关键字不正确"};
        list = toMultiValueMaps(names,values2,values1);
    }

    @Test
    public void testAdd() throws Exception {

        // 必填验证
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            String mark = params.get("mark").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(get("/goods/tags/" + mark + "/goods")
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }

        //没有数量api正常
        mockMvc.perform(get("/goods/tags/hot/goods")
                .param("seller_id","1"))
                .andExpect(status().isOk());


    }

}
