package com.enation.app.javashop.manager.api.trade;

import com.enation.app.javashop.core.trade.order.model.dos.PayLog;
import com.enation.app.javashop.core.trade.order.model.enums.PayStatusEnum;
import com.enation.app.javashop.core.trade.order.service.PayLogManager;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 付款单测试用例
 * @author Snow create in 2018/7/18
 * @version v2.0
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager",rollbackFor = Exception.class)
public class PayLogControllerTest extends BaseTest {

    @Autowired
    private PayLogManager payLogManager;

    @Before
    public void testData(){
        PayLog payLog = new PayLog();
        payLog.setOrderSn("123465798");
        payLog.setPayWay("alipay");
        payLog.setPayTime(223456798l);
        payLog.setPayMemberName("xulipengceshi");
        payLog.setPayStatus(PayStatusEnum.PAY_YES.name());
        this.payLogManager.add(payLog);
    }


    @Test
    public void test() throws Exception {

        String resultJson = mockMvc.perform(get("/admin/trade/orders/pay-log")
                .header("Authorization",superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("order_sn","123").param("pay_way","alipay")
                .param("start_time","1212").param("end_time","323456798")
                .param("member_name","xulipengceshi")
                .param("page_no","1").param("page_Size","10"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        Page webPage = JsonUtil.jsonToObject(resultJson, Page.class);
        List<Map> payLogList = webPage.getData();
        Map map = payLogList.get(0);
        Assert.assertEquals(map.get("pay_status"), PayStatusEnum.PAY_YES.name());


    }



    @Test
    public void testList() throws Exception {

        String resultJson = mockMvc.perform(get("/admin/trade/orders/pay-log/list")
                .header("Authorization",superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("order_sn","123").param("pay_way","alipay")
                .param("start_time","1212").param("end_time","323456798")
                .param("member_name","xulipengceshi"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);

    }

}
