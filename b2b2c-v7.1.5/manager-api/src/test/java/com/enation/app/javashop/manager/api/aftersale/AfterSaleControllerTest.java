package com.enation.app.javashop.manager.api.aftersale;

import com.enation.app.javashop.core.aftersale.model.dos.RefundDO;
import com.enation.app.javashop.core.aftersale.model.enums.RefundStatusEnum;
import com.enation.app.javashop.core.aftersale.model.enums.RefundWayEnum;
import com.enation.app.javashop.core.trade.order.model.enums.PaymentTypeEnum;
import com.enation.app.javashop.core.client.trade.OrderClient;
import com.enation.app.javashop.core.trade.sdk.model.OrderDetailDTO;
import com.enation.app.javashop.core.trade.sdk.model.OrderSkuDTO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author zjp
 * @version v7.0
 * @Description 售后相关测试用例
 * @ClassName AfterSaleControllerTest
 * @since v7.0 下午7:00 2018/5/29
 */
@Transactional(value = "tradeTransactionManager",rollbackFor=Exception.class)
public class AfterSaleControllerTest extends BaseTest{
    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @MockBean
    private OrderClient orderClient;

    List<MultiValueMap<String, String>> list1= null;
    List<MultiValueMap<String, String>> list2= null;

    @Before
    public void insertTestData(){
//        Map map =new HashMap(16);
//        map.put("member_id","1");
//        map.put("sn","1234567");
////        map.put("refund_status", RefundStatusEnum.WAIT_FOR_MANUAL.value());
//        map.put("refuse_type", RefuseTypeEnum.RETURN_MONEY.value());
//        map.put("payment_type", PaymentTypeEnum.ONLINE.value());
//        map.put("seller_id",3);
//        map.put("refund_way", RefundWayEnum.OFFLINE.name());
//        map.put("refund_price","100.0");
//        map.put("order_sn","111111");
//        map.put("trade_sn","222222");
//        map.put("refund_type", RefundTypeEnum.AFTER_SALE);
//        this.daoSupport.insert("es_refund",map);
//
//        map.put("sn","1234561");
////        map.put("refund_status", RefundStatusEnum.PASS.value());
//        map.put("refuse_type", RefuseTypeEnum.RETURN_GOODS.value());
//        this.daoSupport.insert("es_refund",map);
//
//        map.clear();
//        map.put("refund_sn","1234567");
//        map.put("sku_id","99");
//        map.put("price","100");
//        map.put("return_num","1");
//        this.daoSupport.insert("es_refund_goods",map);
//
//        map.put("refund_sn","1234561");
//        this.daoSupport.insert("es_refund_goods",map);
//
//        String[] names1 = new String[] {"sn","refund_price","message"};
//        String[] values1 = new String[] {"","100","退款单号必填"};
//        String[] values3 = new String[] {"1234567","","退款金额必填"};
//        list1 = toMultiValueMaps(names1,values1,values3);
//
//        String[] names2 = new String[] {"sn","refundPrice"};
//        String[] values4 = new String[] {"1234567","80"};
//        String[] values5 = new String[] {"1234567","120"};
//        list2 = toMultiValueMaps(names2,values4,values5);
    }

    /**
     * 管理员退款测试
     * @throws Exception
     */
    @Test
    public void testApproval() throws Exception {
        ErrorMessage error = null;
        //参数性校验
        for (MultiValueMap<String, String> params : list1) {
            String message =  params.get("message").get(0);
            error  = new ErrorMessage("004",message);
            mockMvc.perform(post("/admin/after-sales/refunds/1111111")
                    .header("Authorization",superAdmin)
                    .params(params)
                    .header("Authorization", seller1))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals( error ));
        }

        //退款单无效性校验
        error  = new ErrorMessage("E603","退款单不存在");
        mockMvc.perform(post("/admin/after-sales/refunds/-1")
                .header("Authorization",superAdmin)
                .params(list2.get(0)))
                .andExpect(status().is(500))
                .andExpect(objectEquals( error ));

        OrderDetailDTO orderDetail = new OrderDetailDTO();
        orderDetail.setPaymentPluginId("alipayDirectPlugin");
        List<OrderSkuDTO> list = new ArrayList<>();
        OrderSkuDTO orderSku = new OrderSkuDTO();
        orderSku.setSkuId(99);
        orderSku.setSubtotal(100.0);
        list.add(orderSku);
        orderDetail.setOrderSkuList(list);
        orderDetail.setNeedPayMoney(100.0);
        when(orderClient.getModel("111111")).thenReturn(orderDetail);

        //权限校验
        error  = new ErrorMessage("E601","操作不允许");
        mockMvc.perform(post("/admin/after-sales/refunds/1234561")
                .header("Authorization",superAdmin)
                .params(list2.get(0)))
                .andExpect(status().is(500))
                .andExpect(objectEquals( error ));

        error  = new ErrorMessage("E600","退款金额不能大于支付金额");
        mockMvc.perform(post("/admin/after-sales/refunds/1234567")
                .header("Authorization",superAdmin)
                .params(list2.get(1)))
                .andExpect(status().is(500))
                .andExpect(objectEquals( error ));

        mockMvc.perform(post("/admin/after-sales/refunds/1234567")
                .header("Authorization",superAdmin)
                .params(list2.get(0)))
                .andExpect(status().is(200));
        String sql = "select * from es_refund where sn = ?";
        RefundDO refundDO = this.daoSupport.queryForObject(sql, RefundDO.class,"1234567");
        Assert.assertEquals(RefundStatusEnum.COMPLETED.name(),refundDO.getRefundStatus());

    }
}
