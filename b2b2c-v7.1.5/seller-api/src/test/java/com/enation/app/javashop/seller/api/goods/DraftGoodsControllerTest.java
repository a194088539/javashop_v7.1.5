package com.enation.app.javashop.seller.api.goods;

import com.enation.app.javashop.core.goods.model.dos.DraftGoodsDO;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author fk
 * @version v2.0
 * @Description: 草稿商品单元测试
 * @date 2018/4/1015:33
 * @since v7.0.0
 */
public class DraftGoodsControllerTest extends BaseTest{


    @Before
    public void insertTestData() throws Exception {

    }

    /**
     * 添加草稿商品单元测试
     *
     * @throws Exception
     */
    @Test
    public void testAddDraft() throws Exception {

        //什么都没有填写，仍然可以添加成功
        mockMvc.perform(post("/seller/goods/draft-goods")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
                .header("Authorization", seller1))
                .andExpect(status().is(200));
    }

    /**
     * 修改草稿商品单元测试
     * @throws Exception
     */
    @Test
    public void testEdit()  throws Exception {

        //添加一个草稿商品
       String json =  mockMvc.perform(post("/seller/goods/draft-goods")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
                .header("Authorization", seller1))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        DraftGoodsDO draftGoods = JsonUtil.jsonToObject(json, DraftGoodsDO.class);
        //修改时什么也不传也能修改
        mockMvc.perform(put("/seller/goods/draft-goods/"+draftGoods.getDraftGoodsId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
                .header("Authorization", seller1))
                .andExpect(status().is(200));
        //不能修改不属于我的草稿商品
        ErrorMessage error = new ErrorMessage("308", "无权操作");
        mockMvc.perform(put("/seller/goods/draft-goods/"+draftGoods.getDraftGoodsId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
                .header("Authorization", seller2))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
    }


}
