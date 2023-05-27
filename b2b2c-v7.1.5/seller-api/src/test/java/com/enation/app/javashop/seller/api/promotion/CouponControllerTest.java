package com.enation.app.javashop.seller.api.promotion;

import com.enation.app.javashop.core.promotion.PromotionErrorCode;
import com.enation.app.javashop.core.promotion.coupon.model.dos.CouponDO;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.exception.SystemErrorCodeV1;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 优惠券测试脚本
 * @author Snow create in 2018/4/23
 * @version v2.0
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager",rollbackFor = Exception.class)
public class CouponControllerTest extends BaseTest {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private Cache cache;

    /** 正确数据  */
    private CouponDO couponDO;

    /** 错误数据—参数验证  */
    private List<MultiValueMap<String, String>> errorDataList = new ArrayList();

    /** 错误数据—逻辑错误  */
    private List<MultiValueMap<String, String>> errorDataTwoList = new ArrayList();


    @Before
    public void testData() {

        couponDO = new CouponDO();
        couponDO.setTitle("满100减10元");
        couponDO.setCouponPrice(10.0);
        couponDO.setCouponThresholdPrice(100.0);
        couponDO.setStartTime(2534947200l);
        couponDO.setEndTime(2850566400l);
        couponDO.setCreateNum(100);
        couponDO.setLimitNum(1);

        String[] names = new String[]{"title","coupon_price","coupon_threshold_price","start_time","end_time","create_num","limit_num","message"};

        //必填项验证
        String[] values1 = new String[]{"","10","100","2534947200","2850566400","100","1","请填写优惠券名称"};
        String[] values2 = new String[]{"满100减10元","","100","2534947200","2850566400","100","1","请填写优惠券面额"};
        String[] values3 = new String[]{"满100减10元","10","","2534947200","2850566400","100","1","请填写优惠券门槛价格"};
        String[] values4 = new String[]{"满100减10元","10","100","","2850566400","100","1","请填写起始时间"};
        String[] values5 = new String[]{"满100减10元","10","100","2534947200","","100","1","请填写截止时间"};
        String[] values6 = new String[]{"满100减10元","10","100","2534947200","2850566400","","1","请填写发行量"};
        String[] values7 = new String[]{"满100减10元","10","100","2534947200","2850566400","100","","请填写每人限领数量"};
        errorDataList = toMultiValueMaps(names,values1,values2,values3,values4,values5,values6,values7);

        String[] values8 = new String[]{"满100减10元","10","100","1514736000","2850566400","100","1","活动起始时间必须大于当前时间"};
        String[] values9 = new String[]{"满100减10元","10","100","2950566400","2850566400","100","1","活动起始时间不能大于活动结束时间"};
        errorDataTwoList =toMultiValueMaps(names,values8,values9);

    }

    @Test
    public void testAdd() throws Exception {

        String resultJson = mockMvc.perform(post("/seller/promotion/coupons")
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("title",couponDO.getTitle()).param("coupon_price",couponDO.getCouponPrice()+"")
                .param("coupon_threshold_price",couponDO.getCouponThresholdPrice()+"").param("start_time",couponDO.getStartTime()+"")
                .param("end_time",couponDO.getEndTime()+"").param("create_num",couponDO.getCreateNum()+"")
                .param("limit_num",couponDO.getLimitNum()+""))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        CouponDO couponDO = JsonUtil.jsonToObject(resultJson,CouponDO.class);

        //验证是否正确添加
        mockMvc.perform(get("/seller/promotion/coupons/"+couponDO.getCouponId())
                .header("Authorization",seller1))
                .andExpect(status().is(200))
                .andExpect(jsonPath("title").value("满100减10元"));



        //场景2：参数为空的验证
        for (MultiValueMap<String,String> params  : errorDataList){
            String message =  params.get("message").get(0);
            ErrorMessage error  = new ErrorMessage(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER,message);

            mockMvc.perform(post("/seller/promotion/coupons")
                    .header("Authorization",seller1)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .param("title",params.get("title").get(0)).param("coupon_price",params.get("coupon_price").get(0))
                    .param("coupon_threshold_price",params.get("coupon_threshold_price").get(0)).param("start_time",params.get("start_time").get(0))
                    .param("end_time",params.get("end_time").get(0)).param("create_num",params.get("create_num").get(0))
                    .param("limit_num",params.get("limit_num").get(0)))
                    .andExpect(status().is(400))
                    .andExpect(  objectEquals( error ));
        }


        //场景3：参数异常验证
        for (MultiValueMap<String,String> params  : errorDataTwoList){
            String message =  params.get("message").get(0);
            ErrorMessage error  = new ErrorMessage("004",message);

            mockMvc.perform(post("/seller/promotion/coupons")
                    .header("Authorization",seller1)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .param("title",params.get("title").get(0)).param("coupon_price",params.get("coupon_price").get(0))
                    .param("coupon_threshold_price",params.get("coupon_threshold_price").get(0)).param("start_time",params.get("start_time").get(0))
                    .param("end_time",params.get("end_time").get(0)).param("create_num",params.get("create_num").get(0))
                    .param("limit_num",params.get("limit_num").get(0)))
                    .andExpect(status().is(500))
                    .andExpect(  objectEquals( error ));
        }

    }

    @Test
    public void testEdit() throws Exception {
        CouponDO couponDO2 = this.add();
        couponDO2.setTitle("修改过的优惠券标题");

        //越权操作
        ErrorMessage error  = new ErrorMessage(SystemErrorCodeV1.NO_PERMISSION,"无权操作或者数据不存在");
        mockMvc.perform(put("/seller/promotion/coupons/"+couponDO2.getCouponId())
                .header("Authorization",seller2)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("title",couponDO2.getTitle()).param("coupon_price",couponDO2.getCouponPrice()+"")
                .param("coupon_threshold_price",couponDO2.getCouponThresholdPrice()+"").param("start_time",couponDO2.getStartTime()+"")
                .param("end_time",couponDO2.getEndTime()+"").param("create_num",couponDO2.getCreateNum()+"")
                .param("limit_num",couponDO2.getLimitNum()+""))
                .andExpect(status().is(401))
                .andExpect(  objectEquals( error ));

        //场景1：正确修改
        String resultJson = mockMvc.perform(put("/seller/promotion/coupons/"+couponDO2.getCouponId())
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("title",couponDO2.getTitle()).param("coupon_price",couponDO2.getCouponPrice()+"")
                .param("coupon_threshold_price",couponDO2.getCouponThresholdPrice()+"").param("start_time",couponDO2.getStartTime()+"")
                .param("end_time",couponDO2.getEndTime()+"").param("create_num",couponDO2.getCreateNum()+"")
                .param("limit_num",couponDO2.getLimitNum()+""))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        CouponDO resultCouponDO = JsonUtil.jsonToObject(resultJson,CouponDO.class);
        //验证是否正确添加
        mockMvc.perform(get("/seller/promotion/coupons/"+resultCouponDO.getCouponId())
                .header("Authorization",seller1))
                .andExpect(status().is(200))
                .andExpect(jsonPath("title").value(couponDO2.getTitle()));


        //场景4：活动已经开始
        //手工修改活动的开始时间为 2018年1月1号
        long oldTime = 1514736000l;
        this.daoSupport.execute("update es_coupon set start_time=? where coupon_id=? and seller_id=?",oldTime,couponDO2.getCouponId(),couponDO2.getSellerId());

        ErrorMessage error2  = new ErrorMessage(PromotionErrorCode.E400.code(),"活动已经开始，不能进行编辑删除操作");
        mockMvc.perform(put("/seller/promotion/coupons/"+couponDO2.getCouponId())
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("title",couponDO2.getTitle()).param("coupon_price",couponDO2.getCouponPrice()+"")
                .param("coupon_threshold_price",couponDO2.getCouponThresholdPrice()+"").param("start_time",couponDO2.getStartTime()+"")
                .param("end_time",couponDO2.getEndTime()+"").param("create_num",couponDO2.getCreateNum()+"")
                .param("limit_num",couponDO2.getLimitNum()+""))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error2 ));

    }

    @Test
    public void testDelete() throws Exception {
        CouponDO couponDO = this.add();

        //越权操作
        ErrorMessage error  = new ErrorMessage(SystemErrorCodeV1.NO_PERMISSION,"无权操作或者数据不存在");
        mockMvc.perform(delete("/seller/promotion/coupons/"+couponDO.getCouponId())
                .header("Authorization",seller2)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(401))
                .andExpect(  objectEquals( error ));


        //活动已经开始
        //手工修改活动的开始时间为 2018年1月1号
        long oldTime = 1514736000l;
        this.daoSupport.execute("update es_coupon set start_time=? where coupon_id=? and seller_id=?",oldTime,couponDO.getCouponId(),couponDO.getSellerId());

        ErrorMessage error2  = new ErrorMessage(PromotionErrorCode.E400.code(),"活动已经开始，不能进行编辑删除操作");
        mockMvc.perform(delete("/seller/promotion/coupons/"+couponDO.getCouponId())
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error2 ));

        //正确删除
        long futureTime = 2534947222l;
        this.daoSupport.execute("update es_coupon set start_time=? where coupon_id=? and seller_id=?",futureTime,couponDO.getCouponId(),couponDO.getSellerId());

        mockMvc.perform(delete("/seller/promotion/coupons/"+couponDO.getCouponId())
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
    }


    @Test
    public void testGetOne() throws Exception {
        CouponDO couponDO = this.add();

        //场景一
        String resultJson = mockMvc.perform(get("/seller/promotion/coupons/"+couponDO.getCouponId())
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        CouponDO resultCouponDO = JsonUtil.jsonToObject(resultJson,CouponDO.class);
        Assert.assertEquals(resultCouponDO.getTitle(),resultCouponDO.getTitle());

        //场景2
        ErrorMessage error  = new ErrorMessage(SystemErrorCodeV1.NO_PERMISSION,"无权操作或者数据不存在");
        mockMvc.perform(get("/seller/promotion/coupons/"+couponDO.getCouponId())
                .header("Authorization",seller2)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(401))
                .andExpect(  objectEquals( error ));
    }


    @Test
    public void testGetPage() throws Exception {
        this.add();

        //场景一
        String resultJson = mockMvc.perform(get("/seller/promotion/coupons/")
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("page_no","1").param("page_size","10")
                .param("start_time","2544947200").param("end_time","2634947200"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

    }

    /**
     * 公用添加方法
     * @return
     * @throws Exception
     */
    private CouponDO add() throws Exception {
        //场景1: 正确添加
        String resultJson = mockMvc.perform(post("/seller/promotion/coupons")
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("title",couponDO.getTitle()).param("coupon_price",couponDO.getCouponPrice()+"")
                .param("coupon_threshold_price",couponDO.getCouponThresholdPrice()+"").param("start_time",couponDO.getStartTime()+"")
                .param("end_time",couponDO.getEndTime()+"").param("create_num",couponDO.getCreateNum()+"")
                .param("limit_num",couponDO.getLimitNum()+""))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        CouponDO couponDO = JsonUtil.jsonToObject(resultJson,CouponDO.class);

        return couponDO;
    }

}