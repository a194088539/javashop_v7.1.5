package com.enation.app.javashop.seller.api.statistics;

import com.enation.app.javashop.core.statistics.model.vo.ChartSeries;
import com.enation.app.javashop.core.statistics.model.vo.ShopProfileVO;
import com.enation.app.javashop.core.statistics.model.vo.SimpleChart;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.DateUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * 商家中心，店铺概况统计测试用例
 *
 * @author mengyuanming
 * @version 2.0
 * @since 7.0
 * 2018/6/21 11:12
 */
public class ShopProfileStatisticsControllerTest extends BaseTest {

    @Autowired
    @Qualifier("sssDaoSupport")
    private DaoSupport daoSupport;

    // 获得今天23:59:59的时间戳
    private Calendar cal = Calendar.getInstance();
    private int year = cal.get(Calendar.YEAR);
    private int month = cal.get(Calendar.MONTH) + 1;
    private int day = cal.get(Calendar.DATE);
    private long currentTime = DateUtil.toDate(year + "-" + month + "-" + day + " 23:59:59", "yyyy-MM-dd HH:mm:ss")
            .getTime() / 1000;

    // 获取1天前的时间戳，作为下单时间
    private long orderTime = currentTime - 864000;

    private String format = "M-d";

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

    // 获取时间数据，例如6-15，便于与返回值进行对比
    private String time = simpleDateFormat.format(new Date(Long.valueOf(orderTime * 1000)));

    /**
     * 测试前置
     */
    @Before
    @Transactional(value = "sssTransactionManager")
    public void preparationSSS() {

        String cleanSql = "DELETE FROM `es_sss_order_data` WHERE 1=1 ;";

        this.daoSupport.execute(cleanSql);

        cleanSql = "DELETE FROM `es_sss_goods_data` where 1=1 ;";
        this.daoSupport.execute(cleanSql);

        daoSupport.execute("delete from es_sss_shop_data where 1=1");

        // 插入测试数据
        String insertData = "INSERT INTO `es_sss_order_data` VALUES" +
                " ('1', '201801', '1', '测试用户1', '3', '测试店铺1', 'COMPLETE', 'PAY_YES', '497.00', '2', '1', '1', '" + orderTime + "')," +
                " ('2', '201802', '1', '测试用户1', '3', '测试店铺1', 'COMPLETE', 'PAY_YES', '369.00', '2', '1', '1', '" + orderTime + "')," +
                " ('3', '201803', '2', '测试用户2', '4', '测试店铺2', 'COMPLETE', 'PAY_YES', '123.00', '1', '1', '1', '" + orderTime + "');";

        this.daoSupport.execute(insertData);

        insertData = "INSERT INTO `es_sss_goods_data` VALUES" +
                " ('1', '1', '测试商品1', '1', '1', '|0|1|3|', '3', '1', '99.00', '99', '1')," +
                " ('2', '2', '测试商品2', '2', '1', '|0|1|3|', '3', '2', '199.00', '199', '1')," +
                " ('3', '3', '测试商品3', '2', '2', '|0|1|2|', '3', '2', '123.00', '1221', '1')," +
                " ('4', '4', '测试商品4', '2', '2', '|0|1|2|', '4', '2', '123.00', '55', '1');";
        this.daoSupport.execute(insertData);

        //构造店铺数据
        daoSupport.execute("insert into es_sss_shop_data values(1,3,'测试店铺',123,1)");
        daoSupport.execute("insert into es_sss_shop_data values(2,4,'测试店铺2',123,1)");

    }

    /**
     * 店铺概况，概况数据测试
     *
     * @throws Exception 异常
     */
    @Test
    @Transactional("sssTransactionManager")
    public void getLast30dayStatus() throws Exception {

        ShopProfileVO shopProfileVO = new ShopProfileVO();
        shopProfileVO.setOrderMoney("866.00");
        shopProfileVO.setOrderMember("1");
        shopProfileVO.setOrderNum("2");
        shopProfileVO.setOrderGoods("0");
        shopProfileVO.setAverageMemberMoney("866.0");
        shopProfileVO.setAverageGoodsMoney("0.0");
        shopProfileVO.setGoodsCollect("1519");
        shopProfileVO.setShopCollect("123");
        shopProfileVO.setTotalGoods("3");
        String orderFastigium = time;
        shopProfileVO.setOrderFastigium(orderFastigium);

        // 店铺1测试
        mockMvc.perform(get("/seller/statistics/shop_profile/data").header("Authorization", seller1))
                .andExpect(objectEquals(shopProfileVO));

        shopProfileVO.setOrderMoney("123.00");
        shopProfileVO.setOrderNum("1");
        shopProfileVO.setAverageMemberMoney("123.0");
        shopProfileVO.setGoodsCollect("55");
        shopProfileVO.setTotalGoods("1");

        // 店铺2测试
        mockMvc.perform(get("/seller/statistics/shop_profile/data").header("Authorization", seller2))
                .andExpect(objectEquals(shopProfileVO));

    }

    /**
     * 店铺概况，图表数据测试
     *
     * @throws Exception 异常
     */
    @Test
    @Transactional("sssTransactionManager")
    public void getLast30dayLineChart() throws Exception {

        String[] xAxis = new String[30];
        int limitDays = 30;
        for (int i = 0; i < limitDays; i++) {
            Map<String, Object> map = DateUtil.getYearMonthAndDay(i);
            String month = map.get("month").toString();
            String day = map.get("day").toString();
            xAxis[i] = month + "-" + day;
        }

        String[] data = new String[]{"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "866.00", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};

        ChartSeries series = new ChartSeries("下单金额", data, xAxis);
        SimpleChart simpleChart = new SimpleChart(series, xAxis, new String[0]);

        mockMvc.perform(get("/seller/statistics/shop_profile/chart").header("Authorization", seller1))
                .andExpect(objectEquals(simpleChart));

        data = new String[]{"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "123.00", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};

        series = new ChartSeries("下单金额", data, xAxis);
        simpleChart = new SimpleChart(series, xAxis, new String[0]);

        mockMvc.perform(get("/seller/statistics/shop_profile/chart").header("Authorization", seller2))
                .andExpect(objectEquals(simpleChart));

    }

}
