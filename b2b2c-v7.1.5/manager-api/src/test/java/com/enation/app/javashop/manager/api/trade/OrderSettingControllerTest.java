package com.enation.app.javashop.manager.api.trade;

import com.enation.app.javashop.core.trade.order.model.vo.OrderSettingVO;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 订单设置测试类
 *
 * @author Snow create in 2018/7/16
 * @version v2.0
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager",rollbackFor = Exception.class)
public class OrderSettingControllerTest extends BaseTest {


    private OrderSettingVO settingVO;

    @Before
    public void testData(){
        settingVO = new OrderSettingVO();
        settingVO.setServiceExpiredDay(1);
        settingVO.setRogOrderDay(2);
        settingVO.setCompleteOrderPay(3);
        settingVO.setCommentOrderDay(4);
        settingVO.setCompleteOrderDay(5);
        settingVO.setCancelOrderDay(1);
    }

    @Test
    public void testAdd() throws Exception {


        MultiValueMap<String, String> siteSettingMap = objectToMap(settingVO);

        String resultJson = mockMvc.perform(post("/admin/trade/orders/setting")
                .header("Authorization",superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .params(siteSettingMap))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);

    }

}
