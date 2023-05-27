package com.enation.app.javashop.buyer.api.trade;

import com.enation.app.javashop.core.client.member.MemberAddressClient;
import com.enation.app.javashop.core.member.model.dos.MemberAddress;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.exception.SystemErrorCodeV1;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.StringUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 结算参数测试
 *
 * @author Snow create in 2018/5/3
 * @version v2.0
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager", rollbackFor = Exception.class)
public class CheckoutParamControllerTest extends BaseTest {

    /**
     * 正确数据
     */
    private Map successData = new HashMap();

    /**
     * 发票—错误数据—参数验证
     */
    private List<MultiValueMap<String, String>> errorDataList = new ArrayList();

    /**
     * 错误数据—逻辑错误
     */
    private List<MultiValueMap<String, String>> errorDataTwoList = new ArrayList();


    @MockBean
    private MemberAddressClient memberAddressClient;



    private MemberAddress memberAddress;


    @Before
    public void testData() {
        memberAddress = new MemberAddress();
        memberAddress.setMemberId(1);
        memberAddress.setAddrId(1);
        memberAddress.setProvinceId(1);
        memberAddress.setProvince("中国");
        memberAddress.setCityId(22);
        memberAddress.setCity("北京");
        memberAddress.setCountyId(333);
        memberAddress.setCounty("通州");

        when(memberAddressClient.getModel(memberAddress.getAddrId())).thenReturn(memberAddress);

        //发票参数
        String[] names = new String[]{"receipt_title", "receipt_content", "tax_no", "type", "message"};
        String[] values1 = new String[]{"", "发票内容", "1231231", "1", "发票抬头必填"};
        String[] values2 = new String[]{"发票抬头2", "", "1231231", "1", "发票内容必填"};
        String[] values3 = new String[]{"发票抬头3", "发票内容", "", "1", "发票税号必填"};
        errorDataList = toMultiValueMaps(names, values1, values2, values3);

    }

    /**
     * 收货地址
     */
    @Test
    public void testAddress() throws Exception {

        mockMvc.perform(post("/trade/checkout-params/address-id/" + memberAddress.getAddrId())
                .header("Authorization", buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("address_id", memberAddress.getAddrId() + ""))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

    }

    /**
     * 测试设置—支付方式
     */
    @Test
    public void testPaymentType() throws Exception {

        mockMvc.perform(post("/trade/checkout-params/payment-type")
                .header("Authorization", buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("payment_type", "online"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

    }

//    /**
//     * 测试设置—发票
//     */
//    @Test
//    public void testReceipt() throws Exception {
//
//        ReceiptVO receiptVO = new ReceiptVO();
//        receiptVO.setReceiptTitle("发票抬头");
//        receiptVO.setReceiptContent("发票内容");
//        receiptVO.setTaxNo("12134545564");
//        receiptVO.setReceiptType("");
//        receiptVO.setType(1);
//
//        mockMvc.perform(post("/trade/checkout-params/receipt")
//                .header("Authorization", buyer1)
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .param("receipt_title", receiptVO.getReceiptTitle())
//                .param("receipt_content", receiptVO.getReceiptContent())
//                .param("tax_no", "123123123")
//                .param("type", "1"))
//                .andExpect(status().is(200))
//                .andReturn().getResponse().getContentAsString();
//
//        //场景2：参数为空的验证
//        for (MultiValueMap<String, String> params : errorDataList) {
//            String message = params.get("message").get(0);
//            ErrorMessage error = new ErrorMessage(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, message);
//
//            Map map = new HashMap<>();
//            for (String key : params.keySet()) {
//                map.put(key, params.get(key).get(0));
//            }
//
//            String title = StringUtil.toString(map.get("receipt_title"));
//            String content = StringUtil.toString(map.get("receipt_content"));
//            String dutyInvoice = StringUtil.toString(map.get("tax_no"));
//
//            mockMvc.perform(post("/trade/checkout-params/receipt")
//                    .header("Authorization", buyer1)
//                    .contentType(MediaType.APPLICATION_JSON_UTF8)
//                    .param("receipt_title", title)
//                    .param("receipt_content", content)
//                    .param("tax_no", dutyInvoice)
//                    .param("type", "1"))
//                    .andExpect(status().is(500))
//                    .andExpect(objectEquals(error));
//        }
//
//
//        //场景4，开个人发票不需要税号
//        ReceiptVO receiptVO2 = new ReceiptVO();
//        receiptVO2.setReceiptTitle("发票抬头");
//        receiptVO2.setReceiptContent("发票内容");
//        receiptVO2.setTaxNo("12134545564");
//        receiptVO2.setType(0);
//
//        mockMvc.perform(post("/trade/checkout-params/receipt")
//                .header("Authorization", buyer1)
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .param("type", "0")
//                .param("receipt_title", receiptVO2.getReceiptTitle())
//                .param("receipt_content", receiptVO2.getReceiptContent()))
//                .andExpect(status().is(200))
//                .andReturn().getResponse().getContentAsString();
//
//    }

    /**
     * 测试设置—送货时间
     */
    @Test
    public void testReceiveTime() throws Exception {

        mockMvc.perform(post("/trade/checkout-params/receive-time")
                .header("Authorization", buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("receive_time", "只工作日"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
    }


    /**
     * 测试设置—订单备注
     */
    @Test
    public void testRemark() throws Exception {

        mockMvc.perform(post("/trade/checkout-params/remark")
                .header("Authorization", buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("remark", "订单备注123123"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();


    }

    /**
     * 测试设置—读取结算参数
     */
    @Test
    public void testCheckoutParam() throws Exception {

        String resultJson = mockMvc.perform(get("/trade/checkout-params")
                .header("Authorization", buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();


    }

}
