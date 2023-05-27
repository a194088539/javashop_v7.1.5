package com.enation.app.javashop.manager.api.statistics;

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

/**
 * 平台后台，流量分析统计测试
 *
 * @author mengyuanming
 * @version 2.0
 * @since 7.0
 * 2018/6/12 9:09
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
     * 店铺流量统计测试
     *
     * @throws Exception 异常
     */
    @Test
    @Transactional(value = "sssTransactionManager")
    public void getShop() throws Exception {

        String[] data = new String[]{"0", "0", "0", "0", "0", "0", "40", "100", "0", "0", "0", "0"};

        String[] localName = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};

        ChartSeries series = new ChartSeries("访问量", data, localName);

        String[] xAxis = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};

        SimpleChart simpleChart = new SimpleChart(series, xAxis, new String[0]);

        // 日期类型为YEAR，其他为空
        mockMvc.perform(get("/admin/statistics/page_view/shop")
                .header("Authorization", superAdmin)
                .param("cycle_type", "YEAR"))
                .andExpect(objectEquals(simpleChart));

        // 日期类型为YEAR，年份为当前年份，月份为空
        mockMvc.perform(get("/admin/statistics/page_view/shop")
                .header("Authorization", superAdmin)
                .param("cycle_type", "YEAR")
                .param("year", year + ""))
                .andExpect(objectEquals(simpleChart));

        // 日期类型为YEAR，年份为当前年份，月份为当前月份
        mockMvc.perform(get("/admin/statistics/page_view/shop")
                .header("Authorization", superAdmin)
                .param("cycle_type", "YEAR")
                .param("year", year + "")
                .param("month", month + ""))
                .andExpect(objectEquals(simpleChart));

        data = new String[]{"0", "0", "0", "0", "0", "0", "20", "100", "0", "0", "0", "0"};

        series = new ChartSeries("访问量", data, localName);

        simpleChart = new SimpleChart(series, xAxis, new String[0]);

        // 日期类型为YEAR，店铺id为3，其他为空
        mockMvc.perform(get("/admin/statistics/page_view/shop")
                .header("Authorization", superAdmin)
                .param("cycle_type", "YEAR")
                .param("seller_id", "3")).andExpect(objectEquals(simpleChart));

        data = new String[]{"0", "0", "0", "0", "0", "0", "20", "0", "0", "0", "0", "0"};

        series = new ChartSeries("访问量", data, localName);

        simpleChart = new SimpleChart(series, xAxis, new String[0]);

        // 日期类型为YEAR，店铺id为4，其他为空
        mockMvc.perform(get("/admin/statistics/page_view/shop")
                .header("Authorization", superAdmin)
                .param("cycle_type", "YEAR")
                .param("seller_id", "4")).andExpect(objectEquals(simpleChart));


    }

    /**
     * 商品流量统计测试
     *
     * @throws Exception 异常
     */
    @Test
    @Transactional(value = "sssTransactionManager")
    public void getGoods() throws Exception {

        String[] xAxis = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};

        String[] data = new String[]{"400", "300", "150", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};

        String[] localName = new String[]{"华为手机", "华为", "苹果", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};

        ChartSeries series = new ChartSeries("访问量", data, localName);

        SimpleChart simpleChart = new SimpleChart(series, xAxis, new String[0]);

        // 无参数
        mockMvc.perform(get("/admin/statistics/page_view/goods")
                .header("Authorization", superAdmin)
                .param("cycle_type", "YEAR"))
                .andExpect(objectEquals(simpleChart));

        // 日期类型为YEAR，年份为当前年份，月份为空
        mockMvc.perform(get("/admin/statistics/page_view/goods")
                .header("Authorization", superAdmin)
                .param("cycle_type", "YEAR")
                .param("year", year + ""))
                .andExpect(objectEquals(simpleChart));

        // 日期类型为YEAR，年份为当前年份，月份为当前月份
        mockMvc.perform(get("/admin/statistics/page_view/goods")
                .header("Authorization", superAdmin)
                .param("cycle_type", "YEAR")
                .param("year", year + "")
                .param("month", month + ""))
                .andExpect(objectEquals(simpleChart));

        xAxis = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};

        data = new String[]{"300", "150", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};

        localName = new String[]{"华为", "苹果", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};

        series = new ChartSeries("访问量", data, localName);

        simpleChart = new SimpleChart(series, xAxis, new String[0]);

        // 日期类型为YEAR，店铺id为3，其他为空
        mockMvc.perform(get("/admin/statistics/page_view/goods")
                .header("Authorization", superAdmin)
                .param("cycle_type", "YEAR")
                .param("seller_id", "3"))
                .andExpect(objectEquals(simpleChart));

        xAxis = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};

        data = new String[]{"400", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};

        localName = new String[]{"华为手机", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};

        series = new ChartSeries("访问量", data, localName);

        simpleChart = new SimpleChart(series, xAxis, new String[0]);

        // 日期类型为YEAR，店铺id为3，其他为空
        mockMvc.perform(get("/admin/statistics/page_view/goods")
                .header("Authorization", superAdmin)
                .param("cycle_type", "YEAR")
                .param("seller_id", "4"))
                .andExpect(objectEquals(simpleChart));

    }

}

