package com.enation.app.javashop.manager.api.statistics;

import com.enation.app.javashop.core.client.statistics.SyncopateTableClient;
import com.enation.app.javashop.core.statistics.model.vo.ChartSeries;
import com.enation.app.javashop.core.statistics.model.vo.SimpleChart;
import com.enation.app.javashop.core.statistics.util.ChartUtil;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * 前台商品统计测试类
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-04-03 上午8:07
 */
@Rollback(true)
public class GoodsStatisticControllerTest extends BaseTest {

    @Autowired
    @Qualifier("sssDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private SyncopateTableClient syncopateTableClient;
    @Before
    public void beforeSSS() {
        StatisticBeforeTest.before(daoSupport);
        syncopateTableClient.everyDay();
    }



    //api 前缀
    private String prefix = "/admin/statistics/goods";

    /**
     * 价格销量统计
     */
    @Test
    public void getPriceSales() throws Exception {
        /*
         * 按年统计全部分类 全平台 价格区间默认 2018
         */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "0");
        params.add("category_id", "0");

        params.add("prices", "100");
        params.add("prices", "1000");
        params.add("prices", "10000");
        params.add("prices", "100000");


        ChartSeries chartSeries = new ChartSeries("下单金额", ChartUtil.structureArray("4", "0", "0"),
                ChartUtil.structureArray(
                        "100~1000",
                        "1000~10000",
                        "10000~100000"));
        SimpleChart simpleChart = new SimpleChart(chartSeries, ChartUtil.structureArray("100~1000",
                "1000~10000",
                "10000~100000"), new String[0]);


        mockMvc.perform(get(prefix + "/price/sales")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(simpleChart));

        /*
         * 按年统计全部分类 全平台 价格区间默认 2017
         */
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2017");
        params.add("seller_id", "0");
        params.add("category_id", "0");

        params.add("prices", "100");
        params.add("prices", "1000");
        params.add("prices", "10000");
        params.add("prices", "100000");


        chartSeries = new ChartSeries("下单金额", ChartUtil.structureArray("0", "0", "0"),
                ChartUtil.structureArray(
                        "100~1000",
                        "1000~10000",
                        "10000~100000"));
        simpleChart = new SimpleChart(chartSeries, ChartUtil.structureArray("100~1000",
                "1000~10000",
                "10000~100000"), new String[0]);


        mockMvc.perform(get(prefix + "/price/sales")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(
                objectEquals(simpleChart));

        /*
         * 按年统计全部分类 店铺2 价格区间默认 2018
         */
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "2");
        params.add("category_id", "0");

        params.add("prices", "100");
        params.add("prices", "1000");
        params.add("prices", "10000");
        params.add("prices", "100000");


        chartSeries = new ChartSeries("下单金额", ChartUtil.structureArray("1", "0", "0"),
                ChartUtil.structureArray(
                        "100~1000",
                        "1000~10000",
                        "10000~100000"));
        simpleChart = new SimpleChart(chartSeries, ChartUtil.structureArray("100~1000",
                "1000~10000",
                "10000~100000"), new String[0]);


        mockMvc.perform(get(prefix + "/price/sales")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(
                objectEquals(simpleChart));

        /*
         * 按年统计全部分类 店铺3 价格区间默认 2018
         */
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "3");
        params.add("category_id", "0");

        params.add("prices", "100");
        params.add("prices", "1000");
        params.add("prices", "10000");
        params.add("prices", "100000");


        chartSeries = new ChartSeries("下单金额", ChartUtil.structureArray("0", "0", "0"),
                ChartUtil.structureArray(
                        "100~1000",
                        "1000~10000",
                        "10000~100000"));
        simpleChart = new SimpleChart(chartSeries, ChartUtil.structureArray("100~1000",
                "1000~10000",
                "10000~100000"), new String[0]);


        mockMvc.perform(get(prefix + "/price/sales")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(simpleChart));

        /*
         * 按月统计全部分类 全部店铺 价格区间默认 2018-04
         */
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "MONTH");
        params.add("year", "2018");
        params.add("month", "4");
        params.add("seller_id", "0");
        params.add("category_id", "0");

        params.add("prices", "100");
        params.add("prices", "1000");
        params.add("prices", "10000");
        params.add("prices", "100000");


        chartSeries = new ChartSeries("下单金额", ChartUtil.structureArray("1", "0", "0"),
                ChartUtil.structureArray(
                        "100~1000",
                        "1000~10000",
                        "10000~100000"));
        simpleChart = new SimpleChart(chartSeries, ChartUtil.structureArray("100~1000",
                "1000~10000",
                "10000~100000"), new String[0]);


        mockMvc.perform(get(prefix + "/price/sales")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(
                objectEquals(simpleChart));
        /*
         * 按年统计 分类1 全部店铺 价格区间默认 2018
         */
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "0");
        params.add("category_id", "0");

        params.add("prices", "100");
        params.add("prices", "1000");
        params.add("prices", "10000");
        params.add("prices", "100000");


        chartSeries = new ChartSeries("下单金额", ChartUtil.structureArray("4", "0", "0"),
                ChartUtil.structureArray(
                        "100~1000",
                        "1000~10000",
                        "10000~100000"));
        simpleChart = new SimpleChart(chartSeries, ChartUtil.structureArray("100~1000",
                "1000~10000",
                "10000~100000"), new String[0]);


        mockMvc.perform(get(prefix + "/price/sales")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(
                objectEquals(simpleChart));
        /*
         * 按年统计 分类4 全部店铺 价格区间默认 2018
         */

        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "0");
        params.add("category_id", "4");

        params.add("prices", "100");
        params.add("prices", "1000");
        params.add("prices", "10000");
        params.add("prices", "100000");


        chartSeries = new ChartSeries("下单金额", ChartUtil.structureArray("0", "0", "0"),
                ChartUtil.structureArray(
                        "100~1000",
                        "1000~10000",
                        "10000~100000"));
        simpleChart = new SimpleChart(chartSeries, ChartUtil.structureArray("100~1000",
                "1000~10000",
                "10000~100000"), new String[0]);


        mockMvc.perform(get(prefix + "/price/sales")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(
                objectEquals(simpleChart));


        /*
         * 按年统计 全部分类 全部店铺 价格区间参数：1/200/2000/20000
         */

        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "0");
        params.add("category_id", "0");

        params.add("prices", "1");
        params.add("prices", "200");
        params.add("prices", "2000");
        params.add("prices", "20000");


        chartSeries = new ChartSeries("下单金额", ChartUtil.structureArray("1", "3", "0"),
                ChartUtil.structureArray(
                        "1~200",
                        "200~2000",
                        "2000~20000"));
        simpleChart = new SimpleChart(chartSeries, ChartUtil.structureArray("1~200",
                "200~2000",
                "2000~20000"), new String[0]);


        mockMvc.perform(get(prefix + "/price/sales")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(
                objectEquals(simpleChart));


    }


    /**
     * 热卖商品按金额统计
     */
    @Test
    public void getHotSalesMoneyPage() throws Exception {
        /*
         * 按年统计 全平台 全分类 2018
         */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "0");
        params.add("category_id", "0");

        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("price", 199.0);
        map.put("goods_name", "测试商品2");
        data.add(map);
        map = new HashMap<>();
        map.put("price", 123.0);
        map.put("goods_name", "测试商品3");
        data.add(map);
        map = new HashMap<>();
        map.put("price", 123.0);
        map.put("goods_name", "测试商品4");
        data.add(map);
        map = new HashMap<>();
        map.put("price", 99.0);
        map.put("goods_name", "测试商品1");
        data.add(map);
        Page page = new Page(1, 4L, 4, data);
        mockMvc.perform(get(prefix + "/hot/money/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));

        /*
         * 按月统计 全平台 全分类 2018-04
         */
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "MONTH");
        params.add("year", "2018");
        params.add("month", "4");
        params.add("seller_id", "0");
        params.add("category_id", "0");

        data = new ArrayList<>();
        map = new HashMap<>();
        map.put("price", 123.0);
        map.put("goods_name", "测试商品4");
        data.add(map);
        page = new Page(1, 1L, 1, data);
        mockMvc.perform(get(prefix + "/hot/money/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(
                objectEquals(page));


        /*
         * 按月统计 全平台 全分类 2018-05
         */
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "MONTH");
        params.add("year", "2018");
        params.add("month", "5");
        params.add("seller_id", "0");
        params.add("category_id", "0");

        data = new ArrayList<>();
        page = new Page(1, 0L, 0, data);
        mockMvc.perform(get(prefix + "/hot/money/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(
                objectEquals(page));

        /*
         * 按年统计 店铺1 全分类 2018
         */
        data = new ArrayList<>();
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "1");
        params.add("category_id", "0");

        map = new HashMap<>();
        map.put("price", 199.0);
        map.put("goods_name", "测试商品2");
        data.add(map);
        map = new HashMap<>();
        map.put("price", 123.0);
        map.put("goods_name", "测试商品3");
        data.add(map);
        map = new HashMap<>();
        map.put("price", 99.0);
        map.put("goods_name", "测试商品1");
        data.add(map);
        page = new Page(1, 3L, 3, data);
        mockMvc.perform(get(prefix + "/hot/money/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));

        /*
         * 按年统计 店铺2 全分类 2018
         */
        data = new ArrayList<>();
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "2");
        params.add("category_id", "0");

        map = new HashMap<>();
        map.put("price", 123.0);
        map.put("goods_name", "测试商品4");
        data.add(map);
        page = new Page(1, 1L, 1, data);
        mockMvc.perform(get(prefix + "/hot/money/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));


        /*
         * 按年统计 店铺2 分类1 2018
         */
        data = new ArrayList<>();
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "2");
        params.add("category_id", "1");

        map = new HashMap<>();
        map.put("price", 123.0);
        map.put("goods_name", "测试商品4");
        data.add(map);
        page = new Page(1, 1L, 1, data);
        mockMvc.perform(get(prefix + "/hot/money/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));


        /*
         * 按年统计 店铺2 分类3 2018
         */
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "2");
        params.add("category_id", "3");

        data = new ArrayList<>();
        page = new Page(1, 0L, 0, data);
        mockMvc.perform(get(prefix + "/hot/money/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));
    }

    /**
     * 热卖商品按金额统计
     */
    @Test
    public void getHotSalesMoney() throws Exception {
        //构建一个预期的对象
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "0");
        params.add("category_id", "0");
        ChartSeries chartSeries = new ChartSeries("下单金额", ChartUtil.structureArray("199.00", "123.00", "123.00", "99.00", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null), ChartUtil.structureArray("测试商品2", "测试商品3", "测试商品4", "测试商品1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
        SimpleChart simpleChart = new SimpleChart(chartSeries, ChartUtil.structureArray("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50"), new String[0]);
        mockMvc.perform(get(prefix + "/hot/money")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(simpleChart));
    }

    /**
     * 热卖商品按数量统计
     */
    @Test
    public void getHotSalesNumPage() throws Exception {
        /*
         * 按年统计 全平台 全分类 2018
         */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "0");
        params.add("category_id", "0");
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("order_num", 1);
        map.put("goods_name", "测试商品1");
        data.add(map);
        map = new HashMap<>();
        map.put("order_num", 1);
        map.put("goods_name", "测试商品2");
        data.add(map);
        map = new HashMap<>();
        map.put("order_num", 1);
        map.put("goods_name", "测试商品3");
        data.add(map);
        map = new HashMap<>();
        map.put("order_num", 1);
        map.put("goods_name", "测试商品4");
        data.add(map);
        Page page = new Page(1, 4L, 4, data);
        mockMvc.perform(get(prefix + "/hot/num/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));

        /*
         * 按月统计 全平台 全分类 2018-04
         */
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "MONTH");
        params.add("year", "2018");
        params.add("month", "4");
        params.add("seller_id", "0");
        params.add("category_id", "0");
        data = new ArrayList<>();
        map = new HashMap<>();
        map.put("order_num", 1);
        map.put("goods_name", "测试商品4");
        data.add(map);
        page = new Page(1, 1L, 1, data);
        mockMvc.perform(get(prefix + "/hot/num/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));

        /*
         * 按月统计 全平台 全分类 2018-05
         */
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "MONTH");
        params.add("year", "2018");
        params.add("month", "5");
        params.add("seller_id", "0");
        params.add("category_id", "0");
        data = new ArrayList<>();
        page = new Page(1, 0L, 0, data);
        mockMvc.perform(get(prefix + "/hot/num/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));
        /*
         * 按年统计 店铺1 全分类 2018
         */

        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "1");
        params.add("category_id", "0");
        data = new ArrayList<>();
        map = new HashMap<>();
        map.put("order_num", 1);
        map.put("goods_name", "测试商品1");
        data.add(map);
        map = new HashMap<>();
        map.put("order_num", 1);
        map.put("goods_name", "测试商品2");
        data.add(map);
        map = new HashMap<>();
        map.put("order_num", 1);
        map.put("goods_name", "测试商品3");
        data.add(map);
        page = new Page(1, 3L, 3, data);
        mockMvc.perform(get(prefix + "/hot/num/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));
        /*
         * 按年统计 店铺2 全分类 2018
         */
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "2");
        params.add("category_id", "0");
        data = new ArrayList<>();
        map = new HashMap<>();
        map.put("order_num", 1);
        map.put("goods_name", "测试商品4");
        data.add(map);
        page = new Page(1, 1L, 1, data);
        mockMvc.perform(get(prefix + "/hot/num/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));
        /*
         * 按年统计 店铺2 分类1 2018
         */
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "2");
        params.add("category_id", "0");
        data = new ArrayList<>();
        map = new HashMap<>();
        map.put("order_num", 1);
        map.put("goods_name", "测试商品4");
        data.add(map);
        page = new Page(1, 1L, 1, data);
        mockMvc.perform(get(prefix + "/hot/num/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));
        /*
         * 按年统计 店铺2 分类3 2018
         */

        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "2");
        params.add("category_id", "3");
        data = new ArrayList<>();
        page = new Page(1, 0L, 0, data);
        mockMvc.perform(get(prefix + "/hot/num/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));
    }

    /**
     * 热卖商品按数量统计
     */
    @Test
    public void getHotSalesNum() throws Exception {
        //构建一个预期的对象
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "0");
        params.add("category_id", "0");
        ChartSeries chartSeries = new ChartSeries("下单数量", ChartUtil.structureArray("1", "1", "1", "1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null), ChartUtil.structureArray("测试商品1",
                "测试商品2", "测试商品3", "测试商品4", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
        SimpleChart simpleChart = new SimpleChart(chartSeries, ChartUtil.structureArray("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50"), new String[0]);
        mockMvc.perform(get(prefix + "/hot/num")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(simpleChart));
    }

    /**
     * 商品销售明细
     */
    @Test
    public void getSaleDetails() throws Exception {

        /*
         * 按年统计 全平台 全分类 2018
         */
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("goods_name", "测试商品1");
        map.put("price", 99.0);
        map.put("num", 1);
        map.put("order_num", 1);
        result.add(map);
        map = new HashMap<>();
        map.put("goods_name", "测试商品2");
        map.put("price", 199.0);
        map.put("num", 1);
        map.put("order_num", 1);
        result.add(map);
        map = new HashMap<>();
        map.put("goods_name", "测试商品3");
        map.put("price", 123.0);
        map.put("num", 2);
        map.put("order_num", 1);
        result.add(map);
        map = new HashMap<>();
        map.put("goods_name", "测试商品4");
        map.put("price", 123.0);
        map.put("num", 1);
        map.put("order_num", 1);
        result.add(map);
        Page page = new Page(1, 4L, 10, result);

        //构建一个预期的对象
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "0");
        params.add("category_id", "0");
        params.add("page_size", "10");
        params.add("page_no", "1");

        mockMvc.perform(get(prefix + "/sale/details")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));
        /*
         * 按月统计 全平台 全分类 2018-05
         */
        result = new ArrayList<>();
        page = new Page(1, 0L, 10, result);
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "MONTH");
        params.add("year", "2018");
        params.add("month", "5");
        params.add("seller_id", "0");
        params.add("category_id", "0");
        params.add("page_size", "10");
        params.add("page_no", "1");
        mockMvc.perform(get(prefix + "/sale/details")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));
        /*
         * 按月统计 全平台 全分类 2018-04
         */
        result = new ArrayList<>();
        map = new HashMap<>();
        map.put("goods_name", "测试商品4");
        map.put("price", 123.0);
        map.put("num", 1);
        map.put("order_num", 1);
        result.add(map);
        page = new Page(1, 1L, 10, result);

        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "MONTH");
        params.add("year", "2018");
        params.add("month", "4");
        params.add("seller_id", "0");
        params.add("category_id", "0");
        params.add("page_size", "10");
        params.add("page_no", "1");
        mockMvc.perform(get(prefix + "/sale/details")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));
        /*
         * 按年统计 店铺1 全分类 2018
         */
        result = new ArrayList<>();
        map = new HashMap<>();
        map.put("goods_name", "测试商品1");
        map.put("price", 99.0);
        map.put("num", 1);
        map.put("order_num", 1);
        result.add(map);
        map = new HashMap<>();
        map.put("goods_name", "测试商品2");
        map.put("price", 199.0);
        map.put("num", 1);
        map.put("order_num", 1);
        result.add(map);
        map = new HashMap<>();
        map.put("goods_name", "测试商品3");
        map.put("price", 123.0);
        map.put("num", 2);
        map.put("order_num", 1);
        result.add(map);
        page = new Page(1, 3L, 10, result);
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "1");
        params.add("category_id", "0");
        params.add("page_size", "10");
        params.add("page_no", "1");
        mockMvc.perform(get(prefix + "/sale/details")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));
        /*
         * 按年统计 店铺2 全分类 2018
         */
        result = new ArrayList<>();
        map = new HashMap<>();
        map.put("goods_name", "测试商品4");
        map.put("price", 123.0);
        map.put("num", 1);
        map.put("order_num", 1);
        result.add(map);
        page = new Page(1, 1L, 10, result);

        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "2");
        params.add("category_id", "0");
        params.add("page_size", "10");
        params.add("page_no", "1");
        mockMvc.perform(get(prefix + "/sale/details")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));
        /*
         * 按年统计 店铺2 分类1 2018
         */
        result = new ArrayList<>();
        map = new HashMap<>();
        map.put("goods_name", "测试商品4");
        map.put("price", 123.0);
        map.put("num", 1);
        map.put("order_num", 1);
        result.add(map);
        page = new Page(1, 1L, 10, result);

        params = new LinkedMultiValueMap();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "2");
        params.add("category_id", "1");
        params.add("page_size", "10");
        params.add("page_no", "1");
        mockMvc.perform(get(prefix + "/sale/details")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));
        /*
         * 按年统计 店铺2 分类3 2018
         */
        result = new ArrayList<>();
        page = new Page(1, 0L, 10, result);
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "2");
        params.add("category_id", "3");
        params.add("page_size", "10");
        params.add("page_no", "1");
        mockMvc.perform(get(prefix + "/sale/details")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));

        /*
         * 按年统计 全平台 全分类 2018  商品名称 测试商品1
         */
        result = new ArrayList<>();
        map = new HashMap<>();
        map.put("goods_name", "测试商品1");
        map.put("price", 99.0);
        map.put("num", 1);
        map.put("order_num", 1);
        result.add(map);
        page = new Page(1, 1L, 10, result);

        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "0");
        params.add("category_id", "0");
        params.add("goods_name", "测试商品1");
        params.add("page_size", "10");
        params.add("page_no", "1");
        mockMvc.perform(get(prefix + "/sale/details")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));

    }

    /**
     * 商品收藏统计
     */
    @Test
    public void getGoodsCollect() throws Exception {
        //构建一个预期的对象
        MultiValueMap<String, String> params = new LinkedMultiValueMap();
        params.add("seller_id", "0");

        ChartSeries chartSeries = new ChartSeries("商品收藏TOP50", ChartUtil.structureArray("99", "199", "1221", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null), ChartUtil.structureArray("测试商品1", "测试商品2",
                "测试商品3", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
        SimpleChart simpleChart = new SimpleChart(chartSeries, ChartUtil.structureArray("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50"), new String[0]);


        mockMvc.perform(get(prefix + "/collect")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(simpleChart));
    }


    /**
     * 商品收藏统计
     */
    @Test
    public void getGoodsCollectPage() throws Exception {
        /*
         * 全平台
         */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("seller_id", "0");
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("goods_name", "测试商品3");
        map.put("price", 123.0);
        map.put("favorite_num", 1221);
        map.put("seller_name", "测试店铺");
        result.add(map);
        map = new HashMap<>();
        map.put("goods_name", "测试商品2");
        map.put("price", 199.0);
        map.put("favorite_num", 199);
        map.put("seller_name", "测试店铺2");
        result.add(map);
        map = new HashMap<>();
        map.put("goods_name", "测试商品1");
        map.put("price", 99.0);
        map.put("favorite_num", 99);
        map.put("seller_name", "测试店铺");
        result.add(map);
        Page page = new Page(1, 3L, 50, result);

        /*
         * 店铺2
         */
        params = new LinkedMultiValueMap<>();
        params.add("seller_id", "2");
        result = new ArrayList<>();
        map = new HashMap<>();
        map.put("goods_name", "测试商品2");
        map.put("price", 199.0);
        map.put("favorite_num", 199);
        map.put("seller_name", "测试店铺2");
        result.add(map);
        page = new Page(1, 1L, 50, result);

        mockMvc.perform(get(prefix + "/collect/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));
    }


}
