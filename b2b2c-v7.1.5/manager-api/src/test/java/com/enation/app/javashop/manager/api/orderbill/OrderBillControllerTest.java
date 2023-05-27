package com.enation.app.javashop.manager.api.orderbill;

import com.enation.app.javashop.core.client.member.ShopClient;
import com.enation.app.javashop.core.orderbill.model.enums.BillStatusEnum;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author fk
 * @version v1.0
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

        List<ShopBankDTO> shopList = new ArrayList<>();
        ShopBankDTO bank = new ShopBankDTO();
        bank.setShopId(3);
        bank.setShopCommission(5d);
        shopList.add(bank);
        when (shopClient.listShopBankInfo()).thenReturn(shopList);

        //创建结算单
        this.billManager.createBills(lastMouth[0],lastMouth[1]);

    }

    /**
     * 创建-查询列表-查询单个详情-下一步操作
     * @throws Exception
     */
    @Test
    public void testQueryBillStatistics() throws Exception {

        // 查询结算周期列表
        String json = mockMvc.perform(get("/admin/order/bills/statistics")
                .header("Authorization",superAdmin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].price").value(1994.00))
                .andReturn().getResponse().getContentAsString();

        //查询某个周期的结算单列表，不存在的周期单号,无数据返回
        mockMvc.perform(get("/admin/order/bills")
                .header("Authorization",superAdmin)
                .param("sn","-1"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.data").isEmpty());

        //查询某个周期的结算单列表， 正确查询
        JSONArray list = JSONArray.fromObject(JSONObject.fromObject(json).get("data"));
        JSONObject bill = JSONObject.fromObject(list.get(0));
        String sn = (String)bill.get("sn");
        json = mockMvc.perform(get("/admin/order/bills")
                .header("Authorization",superAdmin)
                .param("sn",sn))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //查询某个结算单详情,不存在的结算单
        ErrorMessage error  = new ErrorMessage("700","没有权限");
        mockMvc.perform(get("/admin/order/bills/-1")
                .header("Authorization",superAdmin))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //查询某个结算单详情,正确查询
        JSONArray list1 = JSONArray.fromObject(JSONObject.fromObject(json).get("data"));
        JSONObject bill1 = JSONObject.fromObject(list1.get(0));
        Integer billId = (Integer)bill1.get("bill_id");
        mockMvc.perform(get("/admin/order/bills/"+billId)
                .header("Authorization",superAdmin))
                .andExpect(status().isOk());

        //下一步，不存在的结算单
        error  = new ErrorMessage("700","没有权限");
        mockMvc.perform(put("/admin/order/bills/-1/next")
                .header("Authorization",superAdmin))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        //下一步，状态不正确
        error  = new ErrorMessage("700","已出账状态，您没有权限进项下一步操作");
        mockMvc.perform(put("/admin/order/bills/"+billId+"/next")
                .header("Authorization",superAdmin))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        //手动修改状态
        String sql = "update es_bill set status  = ? where bill_id = ? ";
        this.daoSupport.execute(sql, BillStatusEnum.RECON.name(),billId);

        //正确下一步
        mockMvc.perform(put("/admin/order/bills/"+billId+"/next")
                .header("Authorization",superAdmin))
                .andExpect(status().isOk());

        //查询某账单中订单或者退款单列表
        error  = new ErrorMessage("004","类型参数不正确");
        mockMvc.perform(get("/admin/order/bills/"+billId+"/lll")
                .header("Authorization",superAdmin))
                .andExpect(status().is(400))
                .andExpect(objectEquals(error));

        //正确查询某账单中订单或者退款单列表
        mockMvc.perform(get("/admin/order/bills/"+billId+"/PAYMENT")
                .header("Authorization",superAdmin))
                .andExpect(status().is(200));

    }

}
