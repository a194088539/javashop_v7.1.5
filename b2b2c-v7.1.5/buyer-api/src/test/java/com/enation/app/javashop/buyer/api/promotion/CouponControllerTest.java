package com.enation.app.javashop.buyer.api.promotion;

import com.enation.app.javashop.core.promotion.coupon.model.dos.CouponDO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 优惠券测试
 *
 * @author Snow create in 2018/7/13
 * @version v2.0
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager",rollbackFor = Exception.class)
public class CouponControllerTest extends BaseTest {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    private CouponDO couponDO;

    private int sellerId = 3;

    @Before
    public void testData(){

        couponDO = new CouponDO();
        couponDO.setTitle("优惠券1234324");
        couponDO.setSellerId(sellerId);
        couponDO.setCouponPrice(10.0);
        couponDO.setCouponThresholdPrice(100.0);
        couponDO.setStartTime(2534947200l);
        couponDO.setEndTime(264947200l);
        this.daoSupport.insert(couponDO);
        int couponId = this.daoSupport.getLastId("coupon_id");
        couponDO.setCouponId(couponId);
    }


    @Test
    public void testList() throws Exception {

        String resultJson = mockMvc.perform(get("/promotions/coupons")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("seller_id",sellerId+""))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        List<CouponDO> couponDOList = JsonUtil.jsonToList(resultJson,CouponDO.class);
        if(couponDOList == null && couponDOList.isEmpty()){
            throw new RuntimeException("读取商家优惠券列表出错");
        }
    }


    @After
    public void cleanData(){
        this.daoSupport.execute("delete from es_coupon where coupon_id = ?",couponDO.getCouponId());
    }

}
