package com.enation.app.javashop.seller.api.statistics;

import com.enation.app.javashop.core.client.member.MemberAskClient;
import com.enation.app.javashop.core.statistics.model.vo.ShopDashboardVO;
import com.enation.app.javashop.core.trade.order.model.vo.OrderStatusNumVO;
import com.enation.app.javashop.core.trade.order.service.OrderQueryManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * 商家中心，首页数据测试
 *
 * @author mengyuanming
 * @version 2.0
 * @since 7.0
 * 2018/6/25 15:12
 */
public class DashboardStatisticsControllerTest extends BaseTest {

    @Autowired
    @Qualifier("sssDaoSupport")
    private DaoSupport daoSupport;

    @MockBean
    private MemberAskClient memberAskClient;

    @MockBean
    private OrderQueryManager orderQueryManager;

    @Before
    @Transactional(value = "sssTransactionManager", rollbackFor = Exception.class)
    public void mockData() {

        // 模拟数据
        Mockito.when(memberAskClient.getNoReplyCount(3)).thenReturn(90);

        Mockito.when(orderQueryManager.getOrderStatusNum(null, 3)).thenReturn(this.orderData());

        String cleanTable = "DELETE FROM `es_sss_goods_data` where 1=1 ;";
        this.daoSupport.execute(cleanTable);

        String insertData = "INSERT INTO `es_sss_goods_data` VALUES" +
                " ('1', '1', '测试商品1', '1', '1', '|0|1|3|', '3', '1', '99.00', '99', '1')," +
                " ('2', '2', '测试商品2', '2', '1', '|0|1|3|', '3', '2', '199.00', '199', '1')," +
                " ('3', '3', '测试商品3', '2', '2', '|0|1|2|', '3', '2', '123.00', '1221', '1')," +
                " ('4', '4', '测试商品4', '2', '2', '|0|1|2|', '4', '2', '123.00', '55', '1');";
        this.daoSupport.execute(insertData);

    }

    /**
     * 店铺首页数据测试
     *
     * @throws Exception 异常
     */
    @Test
    @Transactional(value = "sssTransactionManager", rollbackFor = Exception.class)
    public void getDashboardData() throws Exception {

        ShopDashboardVO shopDashboardVO = new ShopDashboardVO();

        shopDashboardVO.setMarketGoods("3");
        shopDashboardVO.setPendingGoods("0");
        shopDashboardVO.setPendingMemberAsk("90");
        shopDashboardVO.setAllOrdersNum("10");
        shopDashboardVO.setWaitPayOrderNum("70");
        shopDashboardVO.setWaitShipOrderNum("60");
        shopDashboardVO.setWaitDeliveryOrderNum("0");
        shopDashboardVO.setAftersaleOrderNum("80");

        // 模拟一次访问，因无参数，所以只有一次测试
        mockMvc.perform(get("/seller/statistics/dashboard/shop").header("Authorization", seller1))
                .andExpect(objectEquals(shopDashboardVO));

    }

    /**
     * 模拟订单相关数据
     *
     * @return 订单相关数据
     */
    private OrderStatusNumVO orderData() {
        OrderStatusNumVO orderStatusNumVO = new OrderStatusNumVO();

        orderStatusNumVO.setAllNum(10);
        orderStatusNumVO.setWaitCommentNum(20);
        orderStatusNumVO.setCompleteNum(30);
        orderStatusNumVO.setCancelNum(40);
        orderStatusNumVO.setWaitRogNum(null);
        orderStatusNumVO.setWaitShipNum(60);
        orderStatusNumVO.setWaitPayNum(70);
        orderStatusNumVO.setRefundNum(80);

        return orderStatusNumVO;
    }

}
