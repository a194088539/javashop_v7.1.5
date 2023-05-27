package com.enation.app.javashop.seller.api.statistics;

import com.enation.app.javashop.core.client.system.RegionsClient;
import com.enation.app.javashop.core.statistics.model.vo.ChartSeries;
import com.enation.app.javashop.core.statistics.model.vo.MultipleChart;
import com.enation.app.javashop.core.statistics.model.vo.SimpleChart;
import com.enation.app.javashop.core.system.model.dos.Regions;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.apache.commons.collections.map.LinkedMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 商家中心，运营报告测试类
 *
 * @author mengyuanming
 * @version 2.0
 * @since 7.0
 * 2018/6/11 22:01
 */
public class ReportsStatisticControllerTest extends BaseTest {

    @Autowired
    @Qualifier("sssDaoSupport")
    private DaoSupport daoSupport;

    @MockBean
    private RegionsClient regionsClient;

    // 获得今天23:59:59的时间戳
    private Calendar cal = Calendar.getInstance();
    private int year = cal.get(Calendar.YEAR);
    private int month = cal.get(Calendar.MONTH) + 1;
    private int day = cal.get(Calendar.DATE);
    private long currentTime = DateUtil.toDate(year + "-" + month + "-" + day + " 23:59:59", "yyyy-MM-dd HH:mm:ss")
            .getTime() / 1000;

    // 获取1天前的时间戳，作为下单时间
    private long orderTime = currentTime - 86040;

    @Before
    public void setUp() {
        // 模拟地区的返回值
        Mockito.when(regionsClient.getRegionsChildren(0)).thenReturn(regions);
    }

    @Before
    @Transactional(value = "sssTransactionManager", rollbackFor = Exception.class)
    public void preparationSSS() {

        String cleanSql = "DELETE FROM `es_sss_order_data` WHERE 1=1 ;";

        this.daoSupport.execute(cleanSql);

        // 插入测试数据
        String insertData = "INSERT INTO `es_sss_order_data` VALUES" +
                " ('1', '201801', '2', '测试用户2', '4', '测试店铺2', 'COMPLETE', 'PAY_YES', '246.00', '2', '1', '1', '" + (orderTime - 2678400) + "')," +
                " ('2', '201802', '1', '测试用户1', '3', '测试店铺1', 'COMPLETE', 'PAY_YES', '497.00', '2', '1', '1', '" + orderTime + "')," +
                " ('3', '201803', '1', '测试用户1', '3', '测试店铺1', 'COMPLETE', 'PAY_YES', '369.00', '2', '1', '1', '" + orderTime + "')," +
                " ('4', '201804', '2', '测试用户2', '4', '测试店铺2', 'COMPLETE', 'PAY_YES', '123.00', '1', '1', '1', '" + orderTime + "');";


        this.daoSupport.execute(insertData);

    }

    private List<Regions> regions = this.mockRegionsData();

    /**
     * 销售统计，下单金额图表数据
     *
     * @throws Exception 异常
     */
    @Test
    @Transactional(value = "sssTransactionManager", rollbackFor = Exception.class)
    public void getSalesMoney() throws Exception {

        // 日期类型为YEAR，年份为当前年份，月份为空
        String json1 = mockMvc.perform(get("/seller/statistics/reports/sales_money").header("Authorization", seller1)
                .param("cycle_type", "YEAR").param("year", year + ""))
                .andReturn().getResponse().getContentAsString();

        // 日期类型为YEAR，年份为当前年份，月份为当前月份
        String json2 = mockMvc.perform(get("/seller/statistics/reports/sales_money").header("Authorization", seller1)
                .param("cycle_type", "YEAR").param("year", year + "")
                .param("month", month + "")).andReturn().getResponse().getContentAsString();

        // 按年查询有没有月份参数返回的数据都相同
        Assert.assertEquals(json1, json2);

        // 验证数据正确性，获取本月与上月天数时，哪个月天数长，用哪个月的天数作为x轴刻度
        String json3 = mockMvc.perform(get("/seller/statistics/reports/sales_money").header("Authorization", seller1)
                .param("cycle_type", "MONTH").param("year", year + "")
                .param("month", "6")).andReturn().getResponse().getContentAsString();

        String json4 = mockMvc.perform(get("/seller/statistics/reports/sales_money").header("Authorization", seller1)
                .param("cycle_type", "MONTH").param("year", year + "")
                .param("month", "5")).andReturn().getResponse().getContentAsString();

        MultipleChart multipleChart3 = JsonUtil.jsonToObject(json3, MultipleChart.class);
        MultipleChart multipleChart4 = JsonUtil.jsonToObject(json4, MultipleChart.class);

        assert multipleChart3 != null;
        Integer length3 = null == multipleChart3.getxAxis() ? 0 : multipleChart3.getxAxis().length;
        assert multipleChart4 != null;
        Integer length4 = null == multipleChart4.getxAxis() ? 0 : multipleChart4.getxAxis().length;

        Assert.assertEquals(length3, length4);

    }

    /**
     * 销售统计，下单量图表数据
     *
     * @throws Exception 异常
     */
    @Test
    @Transactional(value = "sssTransactionManager", rollbackFor = Exception.class)
    public void getSalesNum() throws Exception {

        // 按年查询，月份为空
        String json1 = mockMvc.perform(get("/seller/statistics/reports/sales_num").header("Authorization", seller1)
                .param("cycle_type", "YEAR").param("year", year + ""))
                .andReturn().getResponse().getContentAsString();

        // 按年查询，月份为当前月份，数据应跟上一个相同
        String json2 = mockMvc.perform(get("/seller/statistics/reports/sales_num").header("Authorization", seller1)
                .param("cycle_type", "YEAR").param("year", year + "")
                .param("month", month + "")).andReturn().getResponse().getContentAsString();

        Assert.assertEquals(json1, json2);

        // 验证数据正确性，获取本月与上月天数时，哪个月天数长，用哪个月的天数作为x轴刻度
        String json3 = mockMvc.perform(get("/seller/statistics/reports/sales_num").header("Authorization", seller1)
                .param("cycle_type", "MONTH").param("year", year + "")
                .param("month", "6")).andReturn().getResponse().getContentAsString();

        String json4 = mockMvc.perform(get("/seller/statistics/reports/sales_num").header("Authorization", seller1)
                .param("cycle_type", "MONTH").param("year", year + "")
                .param("month", "5")).andReturn().getResponse().getContentAsString();

        MultipleChart multipleChart1 = JsonUtil.jsonToObject(json3, MultipleChart.class);
        MultipleChart multipleChart2 = JsonUtil.jsonToObject(json4, MultipleChart.class);

        assert multipleChart1 != null;
        Integer length1 = null == multipleChart1.getxAxis() ? 0 : multipleChart1.getxAxis().length;
        assert multipleChart2 != null;
        Integer length2 = null == multipleChart2.getxAxis() ? 0 : multipleChart1.getxAxis().length;

        Assert.assertEquals(length1, length2);

    }

    /**
     * 销售统计，表格数据
     *
     * @throws Exception 异常
     */
    @Test
    @Transactional(value = "sssTransactionManager", rollbackFor = Exception.class)
    public void salesPage() throws Exception {

        LinkedMap map1 = new LinkedMap();
        map1.put("sn", "201801");
        map1.put("buyer_name", "测试用户1");
        map1.put("create_time", Integer.parseInt(orderTime + ""));
        map1.put("order_price", 497.00);
        map1.put("order_status", "已完成");

        LinkedMap map2 = new LinkedMap();
        map2.put("sn", "201802");
        map2.put("buyer_name", "测试用户1");
        map2.put("create_time", Integer.parseInt(orderTime + ""));
        map2.put("order_price", 369.00);
        map2.put("order_status", "已完成");

        List list = new ArrayList();
        list.add(map1);
        list.add(map2);
        Page page = new Page(1, 2L, 10, list);

        // 按年查询时，月份参数无效，以及按年查询是否有效
        mockMvc.perform(get("/seller/statistics/reports/sales_page").header("Authorization", seller1)
                .param("cycle_type", "YEAR").param("year", year + ""))
                .andExpect(objectEquals(page));

        mockMvc.perform(get("/seller/statistics/reports/sales_page").header("Authorization", seller1)
                .param("cycle_type", "YEAR").param("year", year + "")
                .param("month", month + "")).andExpect(objectEquals(page));

        list.remove(map2);
        page = new Page(1, 2L, 1, list);

        // 验证分页查询
        mockMvc.perform(get("/seller/statistics/reports/sales_page").header("Authorization", seller1)
                .param("cycle_type", "YEAR").param("year", year + "")
                .param("page_size", "1")).andExpect(objectEquals(page));

        LinkedMap map3 = new LinkedMap();

        map3.put("sn", "201803");
        map3.put("buyer_name", "测试用户2");
        map3.put("create_time", Integer.parseInt(orderTime + ""));
        map3.put("order_price", 123.00);
        map3.put("order_status", "已完成");
        list.removeAll(list);
        list.add(map3);

        page = new Page(1, 1L, 10, list);

        // 按月查询是否有效
        mockMvc.perform(get("/seller/statistics/reports/sales_page").header("Authorization", seller2)
                .param("cycle_type", "MONTH").param("year", year + "")
                .param("month", month + "")).andExpect(objectEquals(page));

    }

    /**
     * 销量分析，数据小结
     *
     * @throws Exception 异常
     */
    @Test
    @Transactional(value = "sssTransactionManager", rollbackFor = Exception.class)
    public void getSalesSummary() throws Exception {

        Map map = new HashMap(16);

        map.put("order_num", 0);
        map.put("order_price", null);

        // 验证是否有空指针
        mockMvc.perform(get("/seller/statistics/reports/sales_summary").header("Authorization", seller1)
                .param("cycle_type", "YEAR").param("year", 2015 + ""))
                .andExpect(status().is(200)).andExpect(objectEquals(map));

    }

    /**
     * 区域分析，地图数据
     *
     * @throws Exception 异常
     */
    @Test
    @Transactional(value = "sssTransactionManager", rollbackFor = Exception.class)
    public void regionsMemberMap() throws Exception {

        String expectData = "[{\"name\":\"北京\",\"value\":\"1\"},{\"name\":\"上海\",\"value\":0},{\"name\":\"天津\",\"value\":0},{\"name\":\"重庆\",\"value\":0},{\"name\":\"河北\",\"value\":0},{\"name\":\"山西\",\"value\":0},{\"name\":\"河南\",\"value\":0},{\"name\":\"辽宁\",\"value\":0},{\"name\":\"吉林\",\"value\":0},{\"name\":\"黑龙江\",\"value\":0},{\"name\":\"内蒙古\",\"value\":0},{\"name\":\"江苏\",\"value\":0},{\"name\":\"山东\",\"value\":0},{\"name\":\"安徽\",\"value\":0},{\"name\":\"浙江\",\"value\":0},{\"name\":\"福建\",\"value\":0},{\"name\":\"湖北\",\"value\":0},{\"name\":\"湖南\",\"value\":0},{\"name\":\"广东\",\"value\":0},{\"name\":\"广西\",\"value\":0},{\"name\":\"江西\",\"value\":0},{\"name\":\"四川\",\"value\":0},{\"name\":\"海南\",\"value\":0},{\"name\":\"贵州\",\"value\":0},{\"name\":\"云南\",\"value\":0},{\"name\":\"西藏\",\"value\":0},{\"name\":\"陕西\",\"value\":0},{\"name\":\"甘肃\",\"value\":0},{\"name\":\"青海\",\"value\":0},{\"name\":\"宁夏\",\"value\":0},{\"name\":\"新疆\",\"value\":0},{\"name\":\"台湾\",\"value\":0},{\"name\":\"港澳\",\"value\":0}]";

        // 按年查询，无月份参数
        String actualData = mockMvc.perform(get("/seller/statistics/reports/regions/data").header("Authorization", seller1)
                .param("cycle_type", "YEAR").param("year", year + "")
                .param("type", "ORDER_MEMBER_NUM")).andReturn().getResponse().getContentAsString();

        Assert.assertEquals(expectData, actualData);

        // 按年查询，有月份参数
        actualData = mockMvc.perform(get("/seller/statistics/reports/regions/data").header("Authorization", seller1)
                .param("cycle_type", "YEAR").param("year", year + "")
                .param("month", month + "").param("type", "ORDER_MEMBER_NUM"))
                .andReturn().getResponse().getContentAsString();

        Assert.assertEquals(expectData,actualData);

        // 按月查询，当前月份
        actualData = mockMvc.perform(get("/seller/statistics/reports/regions/data").header("Authorization", seller1)
                .param("cycle_type", "MONTH").param("year", year + "")
                .param("month", month + "").param("type", "ORDER_MEMBER_NUM")).andReturn().getResponse().getContentAsString();

        Assert.assertEquals(expectData,actualData);

        // 按月查询，当前月份，换店铺
        mockMvc.perform(get("/seller/statistics/reports/regions/data").header("Authorization", seller2)
                .param("cycle_type", "MONTH").param("year", year + "")
                .param("month", month + "").param("type", "ORDER_MEMBER_NUM")).andReturn().getResponse().getContentAsString();

    }

    /**
     * 购买分析，客单价分布测试
     *
     * @throws Exception 异常
     */
    @Test
    @Transactional(value = "sssTransactionManager", rollbackFor = Exception.class)
    public void orderDistribution() throws Exception {

        String[] xAxis = new String[]{"0~500", "500~1000", "1000~1500", "1500~2000"};
        String[] data = new String[]{"2", "0", "0", "0"};

        ChartSeries series = new ChartSeries("下单量", data, xAxis);
        SimpleChart simpleChart = new SimpleChart(series, xAxis, new String[0]);

        // 价格区间默认值
        mockMvc.perform(get("/seller/statistics/reports/purchase/ranges").header("Authorization", seller1)
                .param("cycle_type", "YEAR").param("year", year + ""))
                .andExpect(objectEquals(simpleChart));

        // 价格区间传入一个有效值，两个空值
        mockMvc.perform(get("/seller/statistics/reports/purchase/ranges").header("Authorization", seller1)
                .param("cycle_type", "YEAR").param("year", year + "")
                .param("ranges", "").param("ranges", "100").param("ranges", ""))
                .andExpect(jsonPath("message").value("应至少上传两个数字，才可构成价格区间"));

        xAxis = new String[]{"100~200"};
        data = new String[]{"0"};
        series = new ChartSeries("下单量", data, xAxis);
        simpleChart = new SimpleChart(series, xAxis, new String[0]);

        // 价格区间传入两个有效值，一个空值
        mockMvc.perform(get("/seller/statistics/reports/purchase/ranges").header("Authorization", seller1)
                .param("cycle_type", "YEAR").param("year", year + "")
                .param("ranges", "").param("ranges", "100").param("ranges", "200"))
                .andExpect(objectEquals(simpleChart));

        xAxis = new String[]{"100~200", "200~201"};
        data = new String[]{"0", "0"};
        series = new ChartSeries("下单量", data, xAxis);
        simpleChart = new SimpleChart(series, xAxis, new String[0]);

        // 价格区间三个有效值
        mockMvc.perform(get("/seller/statistics/reports/purchase/ranges").header("Authorization", seller1)
                .param("cycle_type", "YEAR").param("year", year + "")
                .param("ranges", "100").param("ranges", "200")
                .param("ranges", "201")).andExpect(objectEquals(simpleChart));

        xAxis = new String[]{"100~200", "200~201"};
        data = new String[]{"1", "0"};
        series = new ChartSeries("下单量", data, xAxis);
        simpleChart = new SimpleChart(series, xAxis, new String[0]);

        // 换个店铺，按月查询
        mockMvc.perform(get("/seller/statistics/reports/purchase/ranges").header("Authorization", seller2)
                .param("cycle_type", "MONTH").param("year", year + "")
                .param("month", month + "").param("ranges", "100")
                .param("ranges", "200").param("ranges", "201"))
                .andExpect(objectEquals(simpleChart));

    }

    /**
     * 购买分析，购买时段分布测试
     *
     * @throws Exception 异常
     */
    @Test
    @Transactional(value = "sssTransactionManager", rollbackFor = Exception.class)
    public void purchasePeriod() throws Exception {

        // 按年查询，有无月份参数都应返回同样的数据
        String json1 = mockMvc.perform(get("/seller/statistics/reports/purchase/period").header("Authorization", seller1)
                .param("cycle_type", "YEAR").param("year", year + ""))
                .andReturn().getResponse().getContentAsString();

        String json2 = mockMvc.perform(get("/seller/statistics/reports/purchase/period").header("Authorization", seller1)
                .param("cycle_type", "YEAR").param("year", year + "").param("month", month + ""))
                .andReturn().getResponse().getContentAsString();

        Assert.assertEquals(json1, json2);

        String[] xAxis = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
        String[] data = new String[]{"2", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
        ChartSeries series = new ChartSeries("下单量", data, xAxis);
        SimpleChart simpleChart = new SimpleChart(series, xAxis, new String[0]);

        // 按月查询，当前月份
        mockMvc.perform(get("/seller/statistics/reports/purchase/period").header("Authorization", seller1)
                .param("cycle_type", "MONTH").param("year", year + "").param("month", month + ""))
                .andExpect(objectEquals(simpleChart));

        data = new String[]{"1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
        series = new ChartSeries("下单量", data, xAxis);
        simpleChart = new SimpleChart(series, xAxis, new String[0]);

        // 按月查询，换店铺
        mockMvc.perform(get("/seller/statistics/reports/purchase/period").header("Authorization", seller2)
                .param("cycle_type", "MONTH").param("year", year + "").param("month", month + ""))
                .andExpect(objectEquals(simpleChart));

    }

    /**
     * 模拟的地区数据
     *
     * @return 地区DO集合
     */
    private List<Regions> mockRegionsData() {

        String json = "[{\"id\":1,\"parent_id\":0,\"region_path\":\",1,\",\"region_grade\":1,\"local_name\":\"北京\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":2,\"parent_id\":0,\"region_path\":\",2,\",\"region_grade\":1,\"local_name\":\"上海\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":3,\"parent_id\":0,\"region_path\":\",3,\",\"region_grade\":1,\"local_name\":\"天津\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":4,\"parent_id\":0,\"region_path\":\",4,\",\"region_grade\":1,\"local_name\":\"重庆\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":5,\"parent_id\":0,\"region_path\":\",5,\",\"region_grade\":1,\"local_name\":\"河北\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":6,\"parent_id\":0,\"region_path\":\",6,\",\"region_grade\":1,\"local_name\":\"山西\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":7,\"parent_id\":0,\"region_path\":\",7,\",\"region_grade\":1,\"local_name\":\"河南\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":8,\"parent_id\":0,\"region_path\":\",8,\",\"region_grade\":1,\"local_name\":\"辽宁\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":9,\"parent_id\":0,\"region_path\":\",9,\",\"region_grade\":1,\"local_name\":\"吉林\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":10,\"parent_id\":0,\"region_path\":\",10,\",\"region_grade\":1,\"local_name\":\"黑龙江\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":11,\"parent_id\":0,\"region_path\":\",11,\",\"region_grade\":1,\"local_name\":\"内蒙古\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":12,\"parent_id\":0,\"region_path\":\",12,\",\"region_grade\":1,\"local_name\":\"江苏\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":13,\"parent_id\":0,\"region_path\":\",13,\",\"region_grade\":1,\"local_name\":\"山东\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":14,\"parent_id\":0,\"region_path\":\",14,\",\"region_grade\":1,\"local_name\":\"安徽\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":15,\"parent_id\":0,\"region_path\":\",15,\",\"region_grade\":1,\"local_name\":\"浙江\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":16,\"parent_id\":0,\"region_path\":\",16,\",\"region_grade\":1,\"local_name\":\"福建\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":17,\"parent_id\":0,\"region_path\":\",17,\",\"region_grade\":1,\"local_name\":\"湖北\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":18,\"parent_id\":0,\"region_path\":\",18,\",\"region_grade\":1,\"local_name\":\"湖南\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":19,\"parent_id\":0,\"region_path\":\",19,\",\"region_grade\":1,\"local_name\":\"广东\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":20,\"parent_id\":0,\"region_path\":\",20,\",\"region_grade\":1,\"local_name\":\"广西\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":21,\"parent_id\":0,\"region_path\":\",21,\",\"region_grade\":1,\"local_name\":\"江西\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":22,\"parent_id\":0,\"region_path\":\",22,\",\"region_grade\":1,\"local_name\":\"四川\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":23,\"parent_id\":0,\"region_path\":\",23,\",\"region_grade\":1,\"local_name\":\"海南\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":24,\"parent_id\":0,\"region_path\":\",24,\",\"region_grade\":1,\"local_name\":\"贵州\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":25,\"parent_id\":0,\"region_path\":\",25,\",\"region_grade\":1,\"local_name\":\"云南\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":26,\"parent_id\":0,\"region_path\":\",26,\",\"region_grade\":1,\"local_name\":\"西藏\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":27,\"parent_id\":0,\"region_path\":\",27,\",\"region_grade\":1,\"local_name\":\"陕西\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":28,\"parent_id\":0,\"region_path\":\",28,\",\"region_grade\":1,\"local_name\":\"甘肃\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":29,\"parent_id\":0,\"region_path\":\",29,\",\"region_grade\":1,\"local_name\":\"青海\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":30,\"parent_id\":0,\"region_path\":\",30,\",\"region_grade\":1,\"local_name\":\"宁夏\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":31,\"parent_id\":0,\"region_path\":\",31,\",\"region_grade\":1,\"local_name\":\"新疆\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":32,\"parent_id\":0,\"region_path\":\",32,\",\"region_grade\":1,\"local_name\":\"台湾\",\"zipcode\":null,\"cod\":1}," +
                "{\"id\":52993,\"parent_id\":0,\"region_path\":\",52993,\",\"region_grade\":1,\"local_name\":\"港澳\",\"zipcode\":null,\"cod\":1}]";

        return JsonUtil.jsonToList(json, Regions.class);

    }
}
