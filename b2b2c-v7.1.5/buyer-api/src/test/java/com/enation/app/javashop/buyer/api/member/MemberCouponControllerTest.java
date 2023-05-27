package com.enation.app.javashop.buyer.api.member;

import com.enation.app.javashop.core.member.MemberErrorCode;
import com.enation.app.javashop.core.member.model.dos.MemberCoupon;
import com.enation.app.javashop.core.member.service.MemberCouponManager;
import com.enation.app.javashop.core.promotion.coupon.model.dos.CouponDO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 会员优惠券测试
 * @author Snow create in 2018/7/3
 * @version v2.0
 * @since v7.0.0
 */
@Transactional(value = "memberTransactionManager", rollbackFor = Exception.class)
public class MemberCouponControllerTest extends BaseTest {

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport memberDaoSupport;

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    private CouponDO couponDO;


    @Before
    public void testDate() {

        couponDO = new CouponDO();
        couponDO.setTitle("优惠券1234324");
        couponDO.setSellerId(3);
        couponDO.setCouponPrice(10.0);
        couponDO.setCouponThresholdPrice(100.0);
        couponDO.setStartTime(2534947200l);
        couponDO.setEndTime(264947200l);
        couponDO.setLimitNum(1);
        this.daoSupport.insert(couponDO);
        int couponId = this.daoSupport.getLastId("coupon_id");
        couponDO.setCouponId(couponId);

        MemberCoupon coupon = new MemberCoupon();
        coupon.setTitle("未使用优惠券名称");
        coupon.setCouponPrice(10.0);
        coupon.setCouponThresholdPrice(100.0);
        coupon.setUsedStatus(0);
        coupon.setMemberId(1);
        coupon.setSellerId(3);
        this.memberDaoSupport.insert(coupon);
        int id = this.memberDaoSupport.getLastId("es_member_coupon");
        coupon.setMcId(id);


        MemberCoupon coupon2 = new MemberCoupon();
        coupon2.setTitle("已使用优惠券");
        coupon2.setCouponPrice(10.0);
        coupon2.setCouponThresholdPrice(100.0);
        coupon2.setUsedStatus(1);
        coupon2.setMemberId(1);
        coupon2.setSellerId(3);
        this.memberDaoSupport.insert(coupon2);
        int id2 = this.memberDaoSupport.getLastId("es_member_coupon");
        coupon2.setMcId(id2);


        MemberCoupon coupon3 = new MemberCoupon();
        coupon3.setTitle("已过期优惠券");
        coupon3.setCouponPrice(10.0);
        coupon3.setCouponThresholdPrice(100.0);
        coupon3.setUsedStatus(0);
        coupon3.setMemberId(1);
        coupon3.setSellerId(3);
        coupon3.setEndTime(1514521600L);
        this.memberDaoSupport.insert(coupon3);
        int id3 = this.memberDaoSupport.getLastId("es_member_coupon");
        coupon3.setMcId(id3);

    }


    @Test
    public void testPage() throws Exception {
        //场景一
        String resultJson = mockMvc.perform(get("/members/coupon")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("page_no","1").param("page_size","10"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        Page page = JsonUtil.jsonToObject(resultJson, Page.class);

        List<Map> memberCouponList = page.getData();
        for(Map map:memberCouponList){
            if("未使用优惠券名称".equals(map.get("title")) ||
                    "已使用优惠券".equals(map.get("title")) ||
                    "已过期优惠券".equals(map.get("title"))){
            }else{
                throw new RuntimeException("出现错误");
            }
        }

    }


    @Test
    public void testOne() throws Exception {
        //场景一  限领优惠券
        mockMvc.perform(post("/members/coupon/"+couponDO.getCouponId()+"/receive")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        ErrorMessage error  = new ErrorMessage(MemberErrorCode.E203.code(),"优惠券限领"+couponDO.getLimitNum()+"个");

        //场景二   领取优惠券
        mockMvc.perform(post("/members/coupon/"+couponDO.getCouponId()+"/receive")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error ));

    }

}
