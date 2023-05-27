package com.enation.app.javashop.seller.api.aftersale;

import com.enation.app.javashop.core.aftersale.model.dos.RefundDO;
import com.enation.app.javashop.core.aftersale.model.enums.RefundStatusEnum;
import com.enation.app.javashop.core.aftersale.model.enums.RefundWayEnum;
import com.enation.app.javashop.core.client.trade.OrderClient;
import com.enation.app.javashop.core.payment.service.RefundManager;
import com.enation.app.javashop.core.trade.order.model.enums.PaymentTypeEnum;
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
 * @since v7.0 下午7:29 2018/5/23
 */
@Transactional(value = "tradeTransactionManager",rollbackFor=Exception.class)
public class AfterSaleControllerTest extends BaseTest{

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @MockBean
    private RefundManager refundManager;

    @MockBean
    private OrderClient orderClient;


    List<MultiValueMap<String, String>> list1= null;
    List<MultiValueMap<String, String>> list2= null;
    RefundDO refundDO = new RefundDO();

    @Before
    public void insertTestData(){

//        Map map =new HashMap(16);
//        map.put("member_id","1");
//        map.put("sn","1234567");
//        map.put("refund_status", RefundStatusEnum.APPLY.value());
//        map.put("refuse_type",RefuseTypeEnum.RETURN_MONEY.value());
//        map.put("payment_type", PaymentTypeEnum.ONLINE.value());
//        map.put("seller_id",3);
//        map.put("refund_way", RefundWayEnum.ORIGINAL.name());
//        map.put("refund_price","100.0");
//        map.put("order_sn","111111");
//        map.put("trade_sn","222222");
//        map.put("pay_order_no","222222");
//        map.put("refund_type", RefundTypeEnum.AFTER_SALE);
//        this.daoSupport.insert("es_refund",map);
//
//        map.put("refuse_type",RefuseTypeEnum.RETURN_GOODS.value());
//        map.put("sn","1234561");
//        map.put("refund_status", RefundStatusEnum.PASS.value());
//        this.daoSupport.insert("es_refund",map);
//
//        map.put("refuse_type",RefuseTypeEnum.RETURN_GOODS.value());
//        map.put("sn","1234562");
//        map.put("refund_status", RefundStatusEnum.APPLY.value());
//        this.daoSupport.insert("es_refund",map);
//
//        map.put("refuse_type",RefuseTypeEnum.RETURN_MONEY.value());
//        map.put("sn","1234563");
//        map.put("refund_way", RefundWayEnum.OFFLINE.name());
//        this.daoSupport.insert("es_refund",map);
//
//        map.put("sn","1234564");
//        map.put("refund_way", RefundWayEnum.ORIGINAL.name());
//        this.daoSupport.insert("es_refund",map);
//
//        map.put("sn","1234565");
//        map.put("refuse_type",RefuseTypeEnum.RETURN_GOODS.value());
//        map.put("refund_status", RefundStatusEnum.PASS.value());
//        this.daoSupport.insert("es_refund",map);
//
//        map.put("sn","1234566");
//        map.put("refund_way", RefundWayEnum.OFFLINE.name());
//        this.daoSupport.insert("es_refund",map);
//
//        map.put("sn","1234568");
//        map.put("refund_way", RefundWayEnum.ORIGINAL.name());
//        this.daoSupport.insert("es_refund",map);
//
//        map.put("sn","1234569");
//        map.put("refuse_type",RefuseTypeEnum.RETURN_GOODS.value());
//        map.put("refund_status",RefundStatusEnum.WAIT_FOR_MANUAL.value());
//        map.put("payment_type", PaymentTypeEnum.ONLINE.value());
//        this.daoSupport.insert("es_refund",map);
//
//        map.put("sn","12345671");
//        map.put("refund_status",RefundStatusEnum.APPLY.value());
//        map.put("payment_type", PaymentTypeEnum.COD.value());
//        this.daoSupport.insert("es_refund",map);
//
//        map.put("sn","12345672");
//        map.put("refund_status",RefundStatusEnum.WAIT_FOR_MANUAL.value());
//        map.put("payment_type", PaymentTypeEnum.COD.value());
//        this.daoSupport.insert("es_refund",map);
//
//
//
//        map.clear();
//        map.put("return_num","1");
//        map.put("refund_sn","1234567");
//        map.put("sku_id","99");
//        map.put("price","100");
//        this.daoSupport.insert("es_refund_goods",map);
//
//        map.put("refund_sn","1234561");
//        this.daoSupport.insert("es_refund_goods",map);
//
//        map.put("refund_sn","1234562");
//        this.daoSupport.insert("es_refund_goods",map);
//
//        map.put("refund_sn","1234563");
//        this.daoSupport.insert("es_refund_goods",map);
//
//        map.put("refund_sn","1234564");
//        this.daoSupport.insert("es_refund_goods",map);
//
//        map.put("refund_sn","1234565");
//        map.put("goods_id","9");
//
//        this.daoSupport.insert("es_refund_goods",map);
//
//        map.put("refund_sn","1234566");
//        this.daoSupport.insert("es_refund_goods",map);
//
//        map.put("refund_sn","1234568");
//        this.daoSupport.insert("es_refund_goods",map);
//
//        String[] names1 = new String[] {"sn","agree","refund_price","message"};
//        String[] values1 = new String[] {"","1","100","退款单号必填"};
//        String[] values2 = new String[] {"123456","","100","是否同意必填"};
//        String[] values3 = new String[] {"123456","1","","退款金额必填"};
//        list1 = toMultiValueMaps(names1,values1,values2,values3);
//
//        String[] names2 = new String[] {"sn","agree","refund_price"};
//        String[] values4 = new String[] {"123456","1","80"};
//        String[] values5 = new String[] {"123456","1","120"};
//        list2 = toMultiValueMaps(names2,values4,values5);
//
//        refundDO.setMemberId(1);
//        refundDO.setSn("1234567");
//        refundDO.setRefundStatus(RefundStatusEnum.APPLY.value());
////        refundDO.setRefuseType(RefuseTypeEnum.RETURN_MONEY.value());
//        refundDO.setPaymentType(PaymentTypeEnum.ONLINE.value());
//        refundDO.setSellerId(1);
//        refundDO.setRefundWay(RefundWayEnum.ORIGINAL.name());
    }

    /**
     * 卖家审核售后申请
     * @throws Exception
     */
    @Test
    public void testAudit() throws Exception {
        ErrorMessage error = null;
        //参数性校验
        for (MultiValueMap<String, String> params : list1) {
            String message =  params.get("message").get(0);
            error  = new ErrorMessage("004",message);
            mockMvc.perform(post("/seller/after-sales/audits/1111111")
                    .params(params)
                    .header("Authorization", seller1))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals( error ));
        }

        //退款单无效性校验
        error  = new ErrorMessage("E603","退款单不存在");
        mockMvc.perform(post("/seller/after-sales/audits/-1")
                .params(list2.get(0))
                .header("Authorization", seller1))
                .andExpect(status().is(500))
                .andExpect(objectEquals( error ));
        mockMvc.perform(post("/seller/after-sales/audits/1234561")
                .params(list2.get(0))
                .header("Authorization", seller2))
                .andExpect(status().is(500))
                .andExpect(objectEquals( error ));

        //审核操作权限校验
        error  = new ErrorMessage("E601","操作不允许");
        mockMvc.perform(post("/seller/after-sales/audits/1234561")
                .params(list2.get(0))
                .header("Authorization", seller1))
                .andExpect(status().is(500))
                .andExpect(objectEquals( error ));

        //退款金额校验
        OrderDetailDTO orderDetail = new OrderDetailDTO();
        orderDetail.setPaymentPluginId("alipayDirectPlugin");
        List<OrderSkuDTO> list = new ArrayList<>();
        OrderSkuDTO orderSku = new OrderSkuDTO();
        orderSku.setSubtotal(100.0);
        orderSku.setSkuId(99);
        orderSku.setPurchasePrice(100.0);
        orderSku.setNum(1);
        list.add(orderSku);
        orderDetail.setOrderSkuList(list);
        orderDetail.setNeedPayMoney(100.0);
        orderDetail.setPayOrderNo("222222");
        when(orderClient.getModel("111111")).thenReturn(orderDetail);
        error  = new ErrorMessage("E600","退款金额不能大于支付金额");
        mockMvc.perform(post("/seller/after-sales/audits/1234567")
                .params(list2.get(1))
                .header("Authorization", seller1))
                .andExpect(status().is(500))
                .andExpect(objectEquals( error ));

        //退款且支持原路退回正确性校验
        Map map = new HashMap();
        map.put("result","true");
        when(refundManager.originRefund("222222","1234567",80.0)).thenReturn(map);
        mockMvc.perform(post("/seller/after-sales/audits/1234567")
                .params(list2.get(0))
                .header("Authorization", seller1))
                .andExpect(status().is(200));
        String sql = "select * from es_refund where sn = ?";
        RefundDO refundDO = this.daoSupport.queryForObject(sql, RefundDO.class,"1234567");
        Assert.assertEquals("80.0",refundDO.getRefundPrice().toString());
        Assert.assertEquals(RefundStatusEnum.REFUNDING.name(),refundDO.getRefundStatus());

        //退款且不支持原路退回正确性校验
        mockMvc.perform(post("/seller/after-sales/audits/1234563")
                .params(list2.get(0))
                .header("Authorization", seller1))
                .andExpect(status().is(200));
        refundDO = this.daoSupport.queryForObject(sql, RefundDO.class,"1234563");
        Assert.assertEquals("80.0",refundDO.getRefundPrice().toString());
//        Assert.assertEquals(RefundStatusEnum.WAIT_FOR_MANUAL.name(),refundDO.getRefundStatus());

        //退货申请审核校验
        mockMvc.perform(post("/seller/after-sales/audits/1234562")
                .params(list2.get(0))
                .header("Authorization", seller1))
                .andExpect(status().is(200));
        refundDO = this.daoSupport.queryForObject(sql, RefundDO.class,"1234562");
        Assert.assertEquals("80.0",refundDO.getRefundPrice().toString());
//        Assert.assertEquals(RefundStatusEnum.PASS.name(),refundDO.getRefundStatus());

        //原路退回退款失败校验
        Map hashMap = new HashMap();
        hashMap.put("result","false");
        hashMap.put("fail_reason","");
        when(refundManager.originRefund("222222","1234564",80.0)).thenReturn(hashMap);
        mockMvc.perform(post("/seller/after-sales/audits/1234564")
                .params(list2.get(0))
                .header("Authorization", seller1))
                .andExpect(status().is(200));
        sql = "select * from es_refund where sn = ?";
        refundDO = this.daoSupport.queryForObject(sql, RefundDO.class,"1234564");
        Assert.assertEquals(RefundStatusEnum.REFUNDFAIL.name(),refundDO.getRefundStatus());

    }

    /**
     * 卖家货品入库
     * @throws Exception
     */
    @Test
    public void testStockIn() throws Exception {
        ErrorMessage error = null;
        //退款单无效性校验
        error  = new ErrorMessage("E603","退款单不存在");
        mockMvc.perform(post("/seller/after-sales/stock-ins/-1")
                .header("Authorization", seller1))
                .andExpect(status().is(500))
                .andExpect(objectEquals( error ));
        mockMvc.perform(post("/seller/after-sales/stock-ins/1234561")
                .header("Authorization", seller2))
                .andExpect(status().is(500))
                .andExpect(objectEquals( error ));

        //操作权限校验
        error  = new ErrorMessage("E601","操作不允许");
        mockMvc.perform(post("/seller/after-sales/stock-ins/1234567")
                .header("Authorization", seller1))
                .andExpect(status().is(500))
                .andExpect(objectEquals( error ));

        //入库失败校验
        error  = new ErrorMessage("E606","商品入库失败，请刷新后重新操作");



        String sql = "select * from es_refund where sn = ?";
        Map hashMap = new HashMap();
        hashMap.put("result","true");
        when(refundManager.originRefund("222222","1234565",100.0)).thenReturn(hashMap);
        mockMvc.perform(post("/seller/after-sales/stock-ins/1234565")
                .header("Authorization", seller1))
                .andExpect(status().is(200));
        RefundDO refundDO = this.daoSupport.queryForObject(sql, RefundDO.class,"1234565");
        Assert.assertEquals(RefundStatusEnum.REFUNDING.name(),refundDO.getRefundStatus());

        //不支持原路退回正确性校验
        mockMvc.perform(post("/seller/after-sales/stock-ins/1234566")
                .header("Authorization", seller1))
                .andExpect(status().is(200));
        refundDO = this.daoSupport.queryForObject(sql, RefundDO.class,"1234566");
//        Assert.assertEquals(RefundStatusEnum.WAIT_FOR_MANUAL.name(),refundDO.getRefundStatus());

        //原路退回失败校验
        hashMap.put("result","false");
        hashMap.put("fail_reason","");
        when(refundManager.originRefund("222222","1234568",100.0)).thenReturn(hashMap);
        mockMvc.perform(post("/seller/after-sales/stock-ins/1234568")
                .header("Authorization", seller1))
                .andExpect(status().is(200));
        refundDO = this.daoSupport.queryForObject(sql, RefundDO.class,"1234568");
        Assert.assertEquals(RefundStatusEnum.REFUNDFAIL.name(),refundDO.getRefundStatus());
    }

    /**
     * 卖家退款
     * @throws Exception
     */
    @Test
    public void testSellerRefund() throws Exception {
        ErrorMessage error = null;
        //退款单无效性校验
        error  = new ErrorMessage("E603","退款单不存在");
        mockMvc.perform(post("/seller/after-sales/refunds/-1")
                .header("Authorization", seller1))
                .andExpect(status().is(500))
                .andExpect(objectEquals( error ));
        mockMvc.perform(post("/seller/after-sales/refunds/1234569")
                .header("Authorization", seller2))
                .andExpect(status().is(500))
                .andExpect(objectEquals( error ));

        //权限校验
        error  = new ErrorMessage("E601","操作不允许");
        mockMvc.perform(post("/seller/after-sales/refunds/1234569")
                .header("Authorization", seller1))
                .andExpect(status().is(500))
                .andExpect(objectEquals( error ));
        mockMvc.perform(post("/seller/after-sales/refunds/12345671")
                .header("Authorization", seller1))
                .andExpect(status().is(500))
                .andExpect(objectEquals( error ));

        mockMvc.perform(post("/seller/after-sales/refunds/12345672")
                .header("Authorization", seller1))
                .andExpect(status().is(200));
        String sql = "select * from es_refund where sn = ?";
        refundDO = this.daoSupport.queryForObject(sql, RefundDO.class,"12345672");
        Assert.assertEquals(RefundStatusEnum.COMPLETED.name(),refundDO.getRefundStatus());

    }
}
