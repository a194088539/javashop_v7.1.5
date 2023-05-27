package com.enation.app.javashop.seller.api.statistics;

import com.enation.app.javashop.core.client.statistics.SyncopateTableClient;
import com.enation.app.javashop.core.statistics.model.vo.ChartSeries;
import com.enation.app.javashop.core.statistics.model.vo.SimpleChart;
import com.enation.app.javashop.core.statistics.util.DateUtil;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * 商家中心，流量统计测试类
 *
 * @author mengyuanming
 * @version 2.0
 * @since 7.0
 * 2018/6/4 10:55
 */
public class PageViewStatisticControllerTest extends BaseTest {

    @Autowired
    @Qualifier("sssDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private SyncopateTableClient syncopateTableClient;

    // 获得今天23:59:59的时间戳
    private Calendar cal = Calendar.getInstance();
    private int year = cal.get(Calendar.YEAR);
    private int month = cal.get(Calendar.MONTH) + 1;
    private int day = cal.get(Calendar.DAY_OF_MONTH);

    /**
     * 测试前置操作
     */
    @Before
    @Transactional(value = "sssTransactionManager")
    public void preparation() {

        // 因为TRUNCATE TABLE回滚困难，所以使用delete
        String cleanTable = "truncate table `es_sss_shop_pv` ";
        // 清空表中数据
        this.daoSupport.execute(cleanTable);
        cleanTable = "truncate table `es_sss_goods_pv`";
        this.daoSupport.execute(cleanTable);

        // 插入测试数据
        String insertData = "INSERT INTO `es_sss_shop_pv` VALUES" +
                " ('1', '3', '" + (year - 1) + "',  '" + (month - 1) + "','18','10' )," +
                " ('2', '3', '" + year + "',  '" + (month - 1) + "','19','20' )," +
                " ('3', '4', '" + year + "',  '" + (month - 1) + "','18','20' )," +
                " ('4', '3', '" + year + "',  '" + month + "','1','100' );";

        this.daoSupport.execute(insertData);

        insertData = "INSERT INTO `es_sss_goods_pv` VALUES" +
                " ('1', '3', '1', '苹果', '" + year + "', '" + (month - 1) + "'," + day + ", '150')," +
                " ('2', '3', '2', '华为', '" + year + "', '" + month + "'," + day + ", '300')," +
                " ('3', '4', '3', '华为手机', '" + year + "', '" + month + "'," + day + ", '400');";
        this.daoSupport.execute(insertData);


        //分表
        syncopateTableClient.everyDay();

    }

    /**
     * 店铺流量查询测试
     *
     * @throws Exception 异常
     */
    @Test
    @Transactional(value = "sssTransactionManager")
    public void countShop() throws Exception {

        String[] xAxis = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};

        String[] data = new String[]{"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};

        String[] localName = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};

        ChartSeries series = new ChartSeries("访问量", data, localName);

        SimpleChart simpleChart = new SimpleChart(series, xAxis, new String[0]);

        // 日期类型为MONTH，year为1980，month为5
        mockMvc.perform(get("/seller/statistics/page_view/shop").header("Authorization", seller1)
                .param("cycle_type", "MONTH").param("year", "1980")
                .param("month", "5")).andExpect(objectEquals(simpleChart));


    }

    /**
     * 商品流量查询测试
     *
     * @throws Exception 异常
     */
    @Test
    @Transactional(value = "sssTransactionManager")
    public void countGoods() throws Exception {

        String[] xAxis = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};

        String[] data = new String[]{"300", "150", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};

        String[] localName = new String[]{"华为", "苹果", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};

        ChartSeries series = new ChartSeries("访问量", data, localName);

        SimpleChart simpleChart = new SimpleChart(series, xAxis, new String[0]);

        // 日期类型为YEAR，其他为空
        mockMvc.perform(get("/seller/statistics/page_view/goods").header("Authorization", seller1)
                .param("cycle_type", "YEAR")).andExpect(objectEquals(simpleChart));

        // 日期类型为YEAR，年份为空，月份为当前月份
        mockMvc.perform(get("/seller/statistics/page_view/goods").header("Authorization", seller1)
                .param("cycle_type", "YEAR").param("month", month + ""))
                .andExpect(objectEquals(simpleChart));

        xAxis = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};

        data = new String[]{"300", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};

        localName = new String[]{"华为", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};

        series = new ChartSeries("访问量", data, localName);

        simpleChart = new SimpleChart(series, xAxis, new String[0]);

        // 日期类型为MONTH，其他为空
        mockMvc.perform(get("/seller/statistics/page_view/goods").header("Authorization", seller1)
                .param("cycle_type", "MONTH")).andExpect(objectEquals(simpleChart));


    }

}
