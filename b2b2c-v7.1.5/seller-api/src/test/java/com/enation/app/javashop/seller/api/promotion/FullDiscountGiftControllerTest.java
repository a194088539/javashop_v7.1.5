package com.enation.app.javashop.seller.api.promotion;

import com.enation.app.javashop.core.promotion.fulldiscount.model.dos.FullDiscountGiftDO;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.exception.SystemErrorCodeV1;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 赠品测试脚本
 * @author Snow create in 2018/4/24
 * @version v2.0
 * @since v7.0.0
 */
public class FullDiscountGiftControllerTest extends BaseTest {

    /** 正确数据  */
    private FullDiscountGiftDO giftDO;

    /** 错误数据—参数验证  */
    private List<MultiValueMap<String, String>> errorDataList = new ArrayList();


    @Before
    public void testData() {

        giftDO = new FullDiscountGiftDO();
        giftDO.setGiftName("赠品名称");
        giftDO.setGiftPrice(10.0);
        giftDO.setActualStore(100);
        giftDO.setGiftImg("img_path");

        String[] names = new String[]{"gift_name","gift_price","actual_store","gift_img","message"};
        //必填项验证
        String[] values1 = new String[]{"","10","100","img_path","请填写赠品名称"};
        String[] values2 = new String[]{"赠品名称","","100","img_path","请填写赠品金额"};
        String[] values3 = new String[]{"赠品名称","10","","img_path","请填写库存"};
        String[] values4 = new String[]{"赠品名称","10","100","","请上传赠品图片"};
        errorDataList = toMultiValueMaps(names,values1,values2,values3,values4);
    }


    @Test
    @Transactional(value = "tradeTransactionManager",rollbackFor = Exception.class)
    public void testAdd() throws Exception {

        //场景1: 正确添加
        String resultJson = mockMvc.perform(post("/seller/promotion/full-discount-gifts")
                .header("Authorization", seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("gift_name",giftDO.getGiftName()).param("gift_price",giftDO.getGiftPrice()+"")
                .param("gift_img",giftDO.getGiftImg()).param("actual_store",giftDO.getActualStore()+""))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        FullDiscountGiftDO giftDO = JsonUtil.jsonToObject(resultJson, FullDiscountGiftDO.class);

        //验证是否正确添加
        mockMvc.perform(get("/seller/promotion/full-discount-gifts/" + giftDO.getGiftId())
                .header("Authorization", seller1))
                .andExpect(status().is(200))
                .andExpect(jsonPath("gift_name").value("赠品名称"));

        //场景2：参数为空的验证
        for (MultiValueMap<String,String> params  : errorDataList){
            String message =  params.get("message").get(0);
            ErrorMessage error  = new ErrorMessage(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER,message);

            mockMvc.perform(post("/seller/promotion/full-discount-gifts")
                    .header("Authorization",seller1)
                    .param("gift_name",params.get("gift_name").get(0)).param("gift_price",params.get("gift_price").get(0))
                    .param("gift_img",params.get("gift_img").get(0)).param("actual_store",params.get("actual_store").get(0)))
                    .andExpect(status().is(400))
                    .andExpect(  objectEquals( error ));
        }
    }


    @Test
    @Transactional(value = "tradeTransactionManager",rollbackFor = Exception.class)
    public void testEdit() throws Exception {

        FullDiscountGiftDO giftDO2 = this.add();
        giftDO2.setGiftName("修改过的赠品名称");

        //越权操作
        ErrorMessage error  = new ErrorMessage(SystemErrorCodeV1.NO_PERMISSION,"无权操作");
        mockMvc.perform(put("/seller/promotion/full-discount-gifts/"+giftDO2.getGiftId())
                .header("Authorization",seller2)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("gift_name",giftDO2.getGiftName()).param("gift_price",giftDO2.getGiftPrice()+"")
                .param("gift_img",giftDO2.getGiftImg()).param("actual_store",giftDO2.getActualStore()+""))
                .andExpect(status().is(401))
                .andExpect(  objectEquals( error ));


        //场景1：正确修改
        String resultJson = mockMvc.perform(put("/seller/promotion/full-discount-gifts/"+giftDO2.getGiftId())
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("gift_name",giftDO2.getGiftName()).param("gift_price",giftDO2.getGiftPrice()+"")
                .param("gift_img",giftDO2.getGiftImg()).param("actual_store",giftDO2.getActualStore()+""))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        FullDiscountGiftDO resultGiftDO = JsonUtil.jsonToObject(resultJson,FullDiscountGiftDO.class);
        //验证是否正确修改
        mockMvc.perform(get("/seller/promotion/full-discount-gifts/"+resultGiftDO.getGiftId())
                .header("Authorization",seller1))
                .andExpect(status().is(200))
                .andExpect(jsonPath("gift_name").value(giftDO2.getGiftName()));

    }



    @Test
    @Transactional(value = "tradeTransactionManager",rollbackFor = Exception.class)
    public void testDelete() throws Exception {

        FullDiscountGiftDO giftDO2 = this.add();

        //越权操作
        ErrorMessage error  = new ErrorMessage(SystemErrorCodeV1.NO_PERMISSION,"无权操作");
        mockMvc.perform(delete("/seller/promotion/full-discount-gifts/"+giftDO2.getGiftId())
                .header("Authorization",seller2)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(401))
                .andExpect(  objectEquals( error ));


        //正确删除
        mockMvc.perform(delete("/seller/promotion/full-discount-gifts/"+giftDO2.getGiftId())
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

    }



    @Test
    @Transactional(value = "tradeTransactionManager",rollbackFor = Exception.class)
    public void testGetOne() throws Exception {
        FullDiscountGiftDO giftDO2 = this.add();

        //场景一
        String resultJson = mockMvc.perform(get("/seller/promotion/full-discount-gifts/"+giftDO2.getGiftId())
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        FullDiscountGiftDO resultGiftDO = JsonUtil.jsonToObject(resultJson,FullDiscountGiftDO.class);
        Assert.assertEquals(giftDO2.getGiftName(),resultGiftDO.getGiftName());

        //场景2
        ErrorMessage error  = new ErrorMessage(SystemErrorCodeV1.NO_PERMISSION,"无权操作");
        mockMvc.perform(get("/seller/promotion/full-discount-gifts/"+giftDO2.getGiftId())
                .header("Authorization",seller2)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(401))
                .andExpect(  objectEquals( error ));
    }


    @Test
    @Transactional(value = "tradeTransactionManager",rollbackFor = Exception.class)
    public void testGetPage() throws Exception {

        //场景一
        mockMvc.perform(get("/seller/promotion/full-discount-gifts")
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("page_no","1").param("page_size","10"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

    }


    /**
     * 公用添加的方法
     * @return
     */
    private FullDiscountGiftDO add() throws Exception {

        String resultJson = mockMvc.perform(post("/seller/promotion/full-discount-gifts")
                .header("Authorization", seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("gift_name",giftDO.getGiftName()).param("gift_price",giftDO.getGiftPrice()+"")
                .param("gift_img",giftDO.getGiftImg()).param("actual_store",giftDO.getActualStore()+""))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        FullDiscountGiftDO giftDO = JsonUtil.jsonToObject(resultJson, FullDiscountGiftDO.class);

        return giftDO;
    }


}
