package com.enation.app.javashop.seller.api.statistics;

import com.enation.app.javashop.core.client.statistics.SyncopateTableClient;
import com.enation.app.javashop.core.statistics.model.vo.ChartSeries;
import com.enation.app.javashop.core.statistics.model.vo.SimpleChart;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.DateUtil;
import org.apache.commons.collections.map.LinkedMap;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 商家中心，商品分析测试
 *
 * @author mengyuanming
 * @version 2.0
 * @since 7.0
 * 2018/5/22 10:23
 */
public class GoodsStatisticsControllerTest extends BaseTest {

    @Autowired
    @Qualifier("sssDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private SyncopateTableClient syncopateTableClient;

    public void main(){
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        System.out.print(month);
    }
    // 获得今天23:59:59的时间戳
    private Calendar cal = Calendar.getInstance();
    private int year = cal.get(Calendar.YEAR);
    private int month = cal.get(Calendar.MONTH) + 1;
    private int day = cal.get(Calendar.DATE);
    private long currentTime = DateUtil.toDate(year + "-" + month + "-" + day + " 23:59:59", "yyyy-MM-dd HH:mm:ss")
            .getTime() / 1000;

    // 获取1天前的时间戳，作为下单时间
    private long orderTime = currentTime - 86040;

    /**
     * 测试前置
     */
    @Before
    @Transactional(value = "sssTransactionManager")
    public void preparation() {

        // 因为TRUNCATE TABLE回滚困难，所以使用delete
        String cleanTable = "DELETE FROM `es_sss_order_data` where 1=1 ;";
        // 清空表中数据
        this.daoSupport.execute(cleanTable);

        cleanTable = "DELETE FROM `es_sss_order_goods_data` where 1=1 ;";
        this.daoSupport.execute(cleanTable);

        cleanTable = "DELETE FROM `es_sss_goods_data` where 1=1 ;";
        this.daoSupport.execute(cleanTable);

        // 插入测试数据
        String insertData = "INSERT INTO `es_sss_order_data` VALUES" +
                " ('1', '201801', '1', '测试用户1', '3', '测试店铺1', 'COMPLETE', 'PAY_YES', '497.00', '2', '1', '1', '" + orderTime + "')," +
                " ('2', '201802', '1', '测试用户1', '3', '测试店铺1', 'COMPLETE', 'PAY_YES', '369.00', '2', '1', '1', '" + orderTime + "')," +
                " ('3', '201803', '2', '测试用户2', '4', '测试店铺2', 'COMPLETE', 'PAY_YES', '123.00', '1', '2', '1', '" + orderTime + "');";

        this.daoSupport.execute(insertData);

        insertData = "INSERT INTO `es_sss_order_goods_data` VALUES" +
                " ('1', '201801', '1', '测试商品1', '1', '99.00', '99.00', '|0|1|3|', '1', '" + orderTime + "', '1')," +
                " ('2', '201801', '2', '测试商品2', '2', '199.00', '398.00', '|0|1|3|', '2', '" + orderTime + "', '1')," +
                " ('3', '201802', '3', '测试商品3', '3', '123.00', '369.00', '|0|1|2|', '3', '" + orderTime + "', '1')," +
                " ('4', '201803', '4', '测试商品4', '1', '123.00', '123.00', '|0|1|2|', '2', '" + orderTime + "', '1');";
        this.daoSupport.execute(insertData);

        insertData = "INSERT INTO `es_sss_goods_data` VALUES" +
                " ('1', '1', '测试商品1', '1', '1', '|0|1|3|', '3', '1', '99.00', '99', '1')," +
                " ('2', '2', '测试商品2', '2', '1', '|0|1|3|', '3', '2', '199.00', '199', '1')," +
                " ('3', '3', '测试商品3', '2', '2', '|0|1|2|', '3', '2', '123.00', '1221', '1')," +
                " ('4', '4', '测试商品4', '2', '2', '|0|1|2|', '4', '2', '123.00', '55', '1');";
        this.daoSupport.execute(insertData);
        syncopateTableClient.everyDay();
    }


    /**
     * 商品详情单元测试
     *
     * @throws Exception 异常
     */
    @Test
    @Transactional(value = "sssTransactionManager")
    public void getGoodsSalesDetail() throws Exception {


        // 所有参数为空
        mockMvc.perform(get("/seller/statistics/goods/goods_detail").header("Authorization", seller1))
                .andExpect(jsonPath("message").value("商品分类id不可为空"));

        // 填充测试数据
        Page page = new Page();

        LinkedMap map = new LinkedMap();
        map.put("goods_name", "测试商品1");
        map.put("numbers", 1);
        map.put("price", 99.00d);
        map.put("total_price", 99.00d);

        List<LinkedMap> list = new ArrayList<>();
        list.add(map);

        LinkedMap map2 = new LinkedMap();
        map2.put("goods_name", "测试商品2");
        map2.put("numbers", 2);
        map2.put("price", 199.00d);
        map2.put("total_price", 398.00d);

        list.add(map2);

        page.setData(list);
        page.setPageNo(1);
        page.setPageSize(10);
        page.setDataTotal(2L);

        // 商品分类id为1，page_no为1，page_size为10，goods_name为“测试”
        mockMvc.perform(get("/seller/statistics/goods/goods_detail").header("Authorization", seller1)
                .param("category_id", "1").param("page_no", "1")
                .param("page_size", "10").param("goods_name", "测试"))
                .andExpect(objectEquals(page));

        // 更新测试数据
        LinkedMap map3 = new LinkedMap();
        map3.put("goods_name", "测试商品3");
        map3.put("numbers", 3);
        map3.put("price", 123.00d);
        map3.put("total_price", 369.00d);
        list.add(map3);

        page.setData(list);
        page.setPageNo(1);
        page.setPageSize(10);
        page.setDataTotal(3L);

        // 商品分类id为0
        mockMvc.perform(get("/seller/statistics/goods/goods_detail").header("Authorization", seller1)
                .param("category_id", "0")).andExpect(objectEquals(page));


        // 商品分类id为0，且商品名包含“测试”
        mockMvc.perform(get("/seller/statistics/goods/goods_detail").header("Authorization", seller1)
                .param("category_id", "0").param("goods_name", "测试"))
                .andExpect(objectEquals(page));

        // 更新测试数据
        list.clear();
        LinkedMap map4 = new LinkedMap();
        map4.put("goods_name", "测试商品4");
        map4.put("numbers", 1);
        map4.put("price", 123.00d);
        map4.put("total_price", 123.00d);
        list.add(map4);
        page.setData(list);
        page.setPageNo(1);
        page.setPageSize(10);
        page.setDataTotal(1L);

        // 商品分类id为0，店铺id为4
        mockMvc.perform(get("/seller/statistics/goods/goods_detail").header("Authorization", seller2)
                .param("category_id", "0")).andExpect(objectEquals(page));

    }

    /**
     * 价格区间单元测试
     *
     * @throws Exception 异常
     */
    @Test
    @Transactional(value = "sssTransactionManager")
    public void getGoodsPriceSales() throws Exception {

        // 构造期望数据
        String[] localName = {"0~500", "500~1000", "1000~1500", "1500~2000"};
        String[] data = {"0", "0", "0", "0"};

        ChartSeries series = new ChartSeries("价格销量分析", data, localName);

        SimpleChart simpleChart = new SimpleChart(series, localName, new String[0]);

        // 价格区间有null值
        mockMvc.perform(get("/seller/statistics/goods/price_sales").header("Authorization", seller1).param("cycle_type","YEAR")
                .param("year", "2018").param("sections","100").param("sections",""))
                .andExpect(jsonPath("message").value("应至少上传两个数字，才可构成价格区间"));

        // 日期类型为空
        mockMvc.perform(get("/seller/statistics/goods/price_sales").header("Authorization", seller1)
                .param("year", "2018"))
                .andExpect(jsonPath("message").value("日期类型及年份不可为空"));

        // 年份为空
        mockMvc.perform(get("/seller/statistics/goods/price_sales").header("Authorization", seller1)
                .param("cycle_type", "YEAR"))
                .andExpect(jsonPath("message").value("日期类型及年份不可为空"));

        // 按月查询时，月份为空
        mockMvc.perform(get("/seller/statistics/goods/price_sales").header("Authorization", seller1)
                .param("cycle_type", "MONTH").param("year", "2018"))
                .andExpect(jsonPath("message").value("按月查询时，月份不可为空"));

        // 价格区间为空
        mockMvc.perform(get("/seller/statistics/goods/price_sales").header("Authorization", seller1)
                .param("cycle_type", "YEAR").param("year", "1980"))
                .andExpect(objectEquals(simpleChart));

        // 价格空间只传一个值
        mockMvc.perform(get("/seller/statistics/goods/price_sales").header("Authorization", seller1)
                .param("cycle_type", "YEAR").param("sections", "100")
                .param("year", "2018"))
                .andExpect(jsonPath("message").value("应至少上传两个数字，才可构成价格区间"));

        localName = new String[]{"0~100", "100~1000", "1000~10000", "10000~100000"};
        data = new String[]{"1", "2", "0", "0"};

        series = new ChartSeries("价格销量分析", data, localName);

        simpleChart = new SimpleChart(series, localName, new String[0]);

        // 传满所有值
        mockMvc.perform(get("/seller/statistics/goods/price_sales").header("Authorization", seller1)
                .param("cycle_type", "MONTH").param("year", year + "")
                .param("month", month + "").param("sections", "0")
                .param("sections", "100").param("sections", "1000")
                .param("sections", "10000").param("sections", "100000")
                .param("category_id", "1")).andExpect(objectEquals(simpleChart));

    }

    /**
     * 下单金额排行前三十商品，分页数据
     *
     * @throws Exception 异常
     */
    @Test
    @Transactional(value = "sssTransactionManager")
    public void getGoodsOrderPriceTopPage() throws Exception {

        // 无参数
        mockMvc.perform(get("/seller/statistics/goods/order_price_page").header("Authorization", seller1))
                .andExpect(jsonPath("message").value("日期类型及年份不可为空"));

        // 按月查询时，月份为空
        mockMvc.perform(get("/seller/statistics/goods/order_price_page").header("Authorization", seller1)
                .param("cycle_type", "MONTH").param("year", "2018"))
                .andExpect(jsonPath("message").value("按月查询时，月份不可为空"));

        ArrayList<LinkedMap> list = new ArrayList<>();

        LinkedMap map = new LinkedMap();
        map.put("goods_name", "测试商品2");
        map.put("goods_id", 2);
        map.put("price", 199.00d);
        map.put("sum", 398.00d);
        list.add(map);

        LinkedMap map1 = new LinkedMap();
        map1.put("goods_name", "测试商品3");
        map1.put("goods_id", 3);
        map1.put("price", 123.00d);
        map1.put("sum", 369.00d);
        list.add(map1);

        LinkedMap map3 = new LinkedMap();
        map3.put("goods_name", "测试商品1");
        map3.put("goods_id", 1);
        map3.put("price", 99.00d);
        map3.put("sum", 99.00d);
        list.add(map3);

        Page page = new Page(1, 3L, 30, list);

        // 按年查询
        mockMvc.perform(get("/seller/statistics/goods/order_price_page").header("Authorization", seller1)
                .param("cycle_type", "YEAR").param("year", year + ""))
                .andExpect(objectEquals(page));

        // 按月查询
        mockMvc.perform(get("/seller/statistics/goods/order_price_page").header("Authorization", seller1)
                .param("cycle_type", "MONTH").param("year", year + "")
                .param("month", month + "")).andExpect(objectEquals(page));


    }

    /**
     * 下单量排行前三十商品，分页数据
     *
     * @throws Exception 异常
     */
    @Test
    @Transactional(value = "sssTransactionManager")
    public void getGoodsNumTopPage() throws Exception {

        // 无参数
        mockMvc.perform(get("/seller/statistics/goods/order_num_page").header("Authorization", seller1))
                .andExpect(jsonPath("message").value("日期类型及年份不可为空"));

        // 按月查询时，月份为空
        mockMvc.perform(get("/seller/statistics/goods/order_num_page").header("Authorization", seller1)
                .param("cycle_type", "MONTH").param("year", "2018"))
                .andExpect(jsonPath("message").value("按月查询时，月份不可为空"));

        ArrayList<LinkedMap> list = new ArrayList<>();

        LinkedMap map = new LinkedMap();
        map.put("goods_name", "测试商品3");
        map.put("goods_id", 3);
        map.put("all_num", 3);
        list.add(map);

        LinkedMap map1 = new LinkedMap();
        map1.put("goods_name", "测试商品2");
        map1.put("goods_id", 2);
        map1.put("all_num", 2);
        list.add(map1);

        LinkedMap map3 = new LinkedMap();
        map3.put("goods_name", "测试商品1");
        map3.put("goods_id", 1);
        map3.put("all_num", 1);
        list.add(map3);

        Page page = new Page(1, 3L, 30, list);

        // 按年查询
        mockMvc.perform(get("/seller/statistics/goods/order_num_page").header("Authorization", seller1)
                .param("cycle_type", "YEAR").param("year", year + ""))
                .andExpect(objectEquals(page));


        // 按月查询
        mockMvc.perform(get("/seller/statistics/goods/order_num_page").header("Authorization", seller1)
                .param("cycle_type", "MONTH").param("year", year + "")
                .param("month", month + "")).andExpect(objectEquals(page));

    }

    /**
     * 下单金额前30的商品，图表数据
     *
     * @throws Exception 异常
     */
    @Test
    @Transactional(value = "sssTransactionManager")
    public void getGoodsOrderPriceTopChart() throws Exception {

        String[] xAxis = new String[0];

        String[] data = new String[0];

        String[] localName = new String[0];

        ChartSeries series = new ChartSeries("总金额", data, localName);

        SimpleChart simpleChart = new SimpleChart(series, xAxis, new String[0]);

        // 无数据查询
        mockMvc.perform((get("/seller/statistics/goods/order_price").header("Authorization", seller1))
                .param("cycle_type", "YEAR").param("year", "1980"))
                .andExpect(status().is(200)).andExpect(objectEquals(simpleChart));

        xAxis = new String[]{"1", "2", "3"};

        data = new String[]{"398.00", "369.00", "99.00"};

        localName = new String[]{"测试商品2", "测试商品3", "测试商品1"};

        series = new ChartSeries("总金额", data, localName);

        simpleChart = new SimpleChart(series, xAxis, new String[0]);

        // 按年查询
        mockMvc.perform(get("/seller/statistics/goods/order_price").header("Authorization", seller1)
                .param("cycle_type", "YEAR").param("year", year + ""))
                .andExpect(objectEquals(simpleChart));

        // 按月查询
        mockMvc.perform(get("/seller/statistics/goods/order_price").header("Authorization", seller1)
                .param("cycle_type", "MONTH").param("year", year + "")
                .param("month", month + "")).andExpect(objectEquals(simpleChart));

    }

    /**
     * 下单量前30的商品，图表数据
     *
     * @throws Exception 异常
     */
    @Test
    @Transactional(value = "sssTransactionManager")
    public void getGoodsNumTopChart() throws Exception {

        String[] xAxis = new String[0];

        String[] data = new String[0];

        String[] localName = new String[0];

        ChartSeries series = new ChartSeries("下单量", data, localName);

        SimpleChart simpleChart = new SimpleChart(series, xAxis, new String[0]);

        // 无数据校验
        mockMvc.perform(get("/seller/statistics/goods/order_num").header("Authorization", seller1)
                .param("cycle_type", "YEAR").param("year", "1980"))
                .andExpect(objectEquals(simpleChart));

        xAxis = new String[]{"1", "2", "3"};

        data = new String[]{"3", "2", "1"};

        localName = new String[]{"测试商品3", "测试商品2", "测试商品1"};

        series = new ChartSeries("下单量", data, localName);

        simpleChart = new SimpleChart(series, xAxis, new String[0]);

        // 按年查询
        mockMvc.perform(get("/seller/statistics/goods/order_num").header("Authorization", seller1)
                .param("cycle_type", "YEAR").param("year", year + ""))
                .andExpect(objectEquals(simpleChart));

        // 按月查询
        mockMvc.perform(get("/seller/statistics/goods/order_num").header("Authorization", seller1)
                .param("cycle_type", "MONTH").param("year", year + "")
                .param("month", month + "")).andExpect(objectEquals(simpleChart));

    }


}
