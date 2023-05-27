package com.enation.app.javashop.seller.api.orderbill;

import com.enation.app.javashop.core.client.member.ShopClient;
import com.enation.app.javashop.core.orderbill.service.BillManager;
import com.enation.app.javashop.core.shop.model.dto.ShopBankDTO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.DateUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author fk
 * @version v2.0
 * @Description: 结算单单元测试
 * @date 2018/4/28 16:21
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager",rollbackFor=Exception.class)
public class OrderBillControllerTest extends BaseTest{

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;
    @Autowired
    private BillManager billManager;
    @MockBean
    private ShopClient shopClient;

    @Before
    public void insertTestData() throws Exception {
        String sql = "delete from es_bill_item ";
        this.daoSupport.execute(sql);
        sql = "delete from es_bill";
        this.daoSupport.execute(sql);
        //构建结算项数据
        Long[] lastMouth = DateUtil.getLastMonth();
        sql = "INSERT INTO `es_bill_item` VALUES (0, '20171026000001', '249', '0', 'PAYMENT', '"+lastMouth[0]+"', null, '0', '3', '"+lastMouth[0]+"', null, '38', '下个路口见', '秋刀鱼', 'online', null)";
        this.daoSupport.execute(sql);
        sql = "INSERT INTO `es_bill_item` VALUES (0, '20171026000008', '1745', '0', 'PAYMENT', '"+lastMouth[0]+"', null, '0', '3', '"+lastMouth[0]+"', null, '38', '下个路口见', '秋刀鱼', 'online', null)";
        this.daoSupport.execute(sql);


    }


    /**
     * 创建-查询列表-查询单个详情-下一步操作
     * @throws Exception
     */
    @Test
    public void testQueryBills() throws Exception {
        List<ShopBankDTO> shopList = new ArrayList<>();
        ShopBankDTO bank = new ShopBankDTO();
        bank.setShopId(3);
        bank.setShopCommission(5d);
        shopList.add(bank);
        when (shopClient.listShopBankInfo()).thenReturn(shopList);

        //创建结算单
        Long[] lastMouth = DateUtil.getLastMonth();
        this.billManager.createBills(lastMouth[0],lastMouth[1]);

        // 查询结算单列表
        String json = mockMvc.perform(get("/seller/order/bills")
                .header("Authorization", seller1))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JSONObject page = JSONObject.fromObject(json);
        JSONArray list = JSONArray.fromObject(page.get("data"));
        JSONObject bill = JSONObject.fromObject(list.get(0));
        Integer billId = (Integer)bill.get("bill_id");

        //查询某个结算单,不是我的结算单
        ErrorMessage error  = new ErrorMessage("700","没有权限");
        mockMvc.perform(get("/seller/order/bills/"+billId)
                .header("Authorization", seller2))
                .andExpect(status().is(500))
                .andExpect(objectEquals( error ));

        mockMvc.perform(get("/seller/order/bills/"+billId)
                .header("Authorization", seller1))
                .andExpect(status().isOk());

        //下一步操作不是我的结算单
        error  = new ErrorMessage("700","没有权限");
        mockMvc.perform(put("/seller/order/bills/"+billId+"/next")
                .header("Authorization", seller2))
                .andExpect(status().is(500))
                .andExpect(objectEquals( error ));
        //对结算单进行下一步操作，即对账
        mockMvc.perform(put("/seller/order/bills/"+billId+"/next")
                .header("Authorization", seller1))
                .andExpect(status().isOk());
        //对账后不能再进行下一步操作
        error  = new ErrorMessage("700","已对账状态，您没有权限进项下一步操作");
        mockMvc.perform(put("/seller/order/bills/"+billId+"/next")
                .header("Authorization", seller1))
                .andExpect(status().is(500))
                .andExpect(objectEquals( error ));

        //查询某账单中订单或者退款单列表
        error  = new ErrorMessage("004","类型参数不正确");
        mockMvc.perform(get("/seller/order/bills/"+billId+"/lll")
                .header("Authorization", seller1))
                .andExpect(status().is(400))
                .andExpect(objectEquals(error));

        //正确查询某账单中订单或者退款单列表
        mockMvc.perform(get("/seller/order/bills/"+billId+"/PAYMENT")
                .header("Authorization", seller1))
                .andExpect(status().is(200));


    }

}
