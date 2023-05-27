package com.enation.app.javashop.manager.api.statistics;

import com.enation.app.javashop.core.client.statistics.SyncopateTableClient;
import com.enation.app.javashop.core.goods.model.vo.CategoryVO;
import com.enation.app.javashop.core.goods.service.CategoryManager;
import com.enation.app.javashop.core.statistics.model.vo.ChartSeries;
import com.enation.app.javashop.core.statistics.model.vo.SimpleChart;
import com.enation.app.javashop.core.statistics.util.ChartUtil;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * 行业统计测试类
 *
 * @author chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/4/3 下午3:16
 */
public class IndustryStatisticControllerTest extends BaseTest {

    //api 前缀
    private String prefix = "/admin/statistics/industry";

    @Autowired
    @Qualifier("sssDaoSupport")
    private DaoSupport daoSupport;

    @MockBean
    private CategoryManager categoryManager;

    @Autowired
    private SyncopateTableClient syncopateTableClient;
    @Before
    public void beforeSSS() {
        StatisticBeforeTest.before(daoSupport);
        syncopateTableClient.everyDay();



        List<CategoryVO> categoryVOS = new ArrayList<>();
        CategoryVO vo  = new CategoryVO();

        vo.setCategoryId(491);
        vo.setName("数码家电");
        vo.setParentId(0);
        vo.setCategoryPath("0|491|");
        vo.setGoodsCount(0);
        vo.setCategoryOrder(0);
        vo.setImage("");
        categoryVOS.add(vo);


        vo = new CategoryVO();
        vo.setParentId(0);
        vo.setCategoryId(1);
        vo.setName("食品饮料");
        vo.setCategoryPath("0|1|");
        vo.setGoodsCount(0);
        vo.setCategoryOrder(1);
        vo.setImage("");
        categoryVOS.add(vo);

        vo = new CategoryVO();
        vo.setCategoryId(4);
        vo.setName("进口食品");
        vo.setParentId(0);
        vo.setCategoryPath("0|4|");
        vo.setGoodsCount(0);
        vo.setCategoryOrder(2);
        vo.setImage("");
        categoryVOS.add(vo);

        vo = new CategoryVO();
        vo.setCategoryId(38);
        vo.setName("美容化妆");
        vo.setParentId(0);
        vo.setCategoryPath("0|38|");
        vo.setGoodsCount(0);
        vo.setCategoryOrder(3);
        vo.setImage("");
        categoryVOS.add(vo);

        vo = new CategoryVO();
        vo.setCategoryId(56);
        vo.setName("母婴玩具");
        vo.setParentId(0);
        vo.setCategoryPath("0|56|");
        vo.setGoodsCount(0);
        vo.setCategoryOrder(4);
        vo.setImage("");
        categoryVOS.add(vo);


        vo = new CategoryVO();
        vo.setCategoryId(79);
        vo.setName("厨房用品");
        vo.setParentId(0);
        vo.setCategoryPath("0|79|");
        vo.setGoodsCount(0);
        vo.setCategoryOrder(5);
        vo.setImage("");
        categoryVOS.add(vo);


        vo = new CategoryVO();
        vo.setCategoryId(35);
        vo.setName("钟表箱包");
        vo.setParentId(0);
        vo.setCategoryPath("0|35|");
        vo.setGoodsCount(0);
        vo.setCategoryOrder(20);
        vo.setImage("");
        categoryVOS.add(vo);

        vo = new CategoryVO();
        vo.setCategoryId(85);
        vo.setName("营养保健");
        vo.setParentId(0);
        vo.setCategoryPath("0|85|");
        vo.setGoodsCount(0);
        vo.setCategoryOrder(20);
        vo.setImage("");
        categoryVOS.add(vo);


        vo = new CategoryVO();
        vo.setCategoryId(86);
        vo.setName("服装鞋靴");
        vo.setParentId(0);
        vo.setCategoryPath("0|86|");
        vo.setGoodsCount(0);
        vo.setCategoryOrder(21);
        vo.setImage("");
        categoryVOS.add(vo);


        Mockito.when(categoryManager.listAllChildren(Mockito.anyInt())).thenReturn(categoryVOS);
    }

    /**
     * 按分类统计下单量
     *
     * @throws Exception 异常
     */
    @Test
    public void getOrderQuantity() throws Exception {

        /*
         * 按年统计 全平台 2018
         */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "0");
        ChartSeries chartSeries = new ChartSeries("行业下单统计", ChartUtil.structureArray("0",
                "4",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0"),
                ChartUtil.structureArray(
                        "数码家电", "食品饮料",
                        "进口食品",
                        "美容化妆",
                        "母婴玩具",
                        "厨房用品",
                        "钟表箱包",
                        "营养保健",
                        "服装鞋靴"));
        SimpleChart simpleChart = new SimpleChart(chartSeries, new String[0], ChartUtil.structureArray(
                "数码家电",
                "食品饮料",
                "进口食品",
                "美容化妆",
                "母婴玩具",
                "厨房用品",
                "钟表箱包",
                "营养保健",
                "服装鞋靴"));
        mockMvc.perform(get(prefix + "/order/quantity")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(simpleChart));

        /*
         * 按年统计 全平台 2017
         */
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2017");
        params.add("seller_id", "0");
        chartSeries = new ChartSeries("行业下单统计", ChartUtil.structureArray("0",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0"),
                ChartUtil.structureArray(
                        "数码家电", "食品饮料",
                        "进口食品",
                        "美容化妆",
                        "母婴玩具",
                        "厨房用品",
                        "钟表箱包",
                        "营养保健",
                        "服装鞋靴"));
        simpleChart = new SimpleChart(chartSeries, new String[0], ChartUtil.structureArray(
                "数码家电",
                "食品饮料",
                "进口食品",
                "美容化妆",
                "母婴玩具",
                "厨房用品",
                "钟表箱包",
                "营养保健",
                "服装鞋靴"));
        mockMvc.perform(get(prefix + "/order/quantity")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(simpleChart));
        /*
         * 按月统计 全平台 2018-04
         */
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "MONTH");
        params.add("year", "2018");
        params.add("month", "4");
        params.add("seller_id", "0");
        chartSeries = new ChartSeries("行业下单统计", ChartUtil.structureArray("0",
                "2",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0"),
                ChartUtil.structureArray(
                        "数码家电", "食品饮料",
                        "进口食品",
                        "美容化妆",
                        "母婴玩具",
                        "厨房用品",
                        "钟表箱包",
                        "营养保健",
                        "服装鞋靴"));
        simpleChart = new SimpleChart(chartSeries, new String[0], ChartUtil.structureArray(
                "数码家电",
                "食品饮料",
                "进口食品",
                "美容化妆",
                "母婴玩具",
                "厨房用品",
                "钟表箱包",
                "营养保健",
                "服装鞋靴"));
        mockMvc.perform(get(prefix + "/order/quantity")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(simpleChart));
        /*
         * 按月统计 全平台 2018-05
         */
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "MONTH");
        params.add("year", "2018");
        params.add("month", "5");
        params.add("seller_id", "0");
        chartSeries = new ChartSeries("行业下单统计", ChartUtil.structureArray("0",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0"),
                ChartUtil.structureArray(
                        "数码家电", "食品饮料",
                        "进口食品",
                        "美容化妆",
                        "母婴玩具",
                        "厨房用品",
                        "钟表箱包",
                        "营养保健",
                        "服装鞋靴"));
        simpleChart = new SimpleChart(chartSeries, new String[0], ChartUtil.structureArray(
                "数码家电",
                "食品饮料",
                "进口食品",
                "美容化妆",
                "母婴玩具",
                "厨房用品",
                "钟表箱包",
                "营养保健",
                "服装鞋靴"));
        mockMvc.perform(get(prefix + "/order/quantity")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(simpleChart));
        /*
         * 按年查询 店铺 1 2018
         */
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "1");
        chartSeries = new ChartSeries("行业下单统计", ChartUtil.structureArray("0",
                "3",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0"),
                ChartUtil.structureArray(
                        "数码家电", "食品饮料",
                        "进口食品",
                        "美容化妆",
                        "母婴玩具",
                        "厨房用品",
                        "钟表箱包",
                        "营养保健",
                        "服装鞋靴"));
        simpleChart = new SimpleChart(chartSeries, new String[0], ChartUtil.structureArray(
                "数码家电",
                "食品饮料",
                "进口食品",
                "美容化妆",
                "母婴玩具",
                "厨房用品",
                "钟表箱包",
                "营养保健",
                "服装鞋靴"));
        mockMvc.perform(get(prefix + "/order/quantity")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(simpleChart));
        /*
         * 按年查询 店铺 2 2018
         */
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "2");
        chartSeries = new ChartSeries("行业下单统计", ChartUtil.structureArray("0",
                "1",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0"),
                ChartUtil.structureArray(
                        "数码家电", "食品饮料",
                        "进口食品",
                        "美容化妆",
                        "母婴玩具",
                        "厨房用品",
                        "钟表箱包",
                        "营养保健",
                        "服装鞋靴"));
        simpleChart = new SimpleChart(chartSeries, new String[0], ChartUtil.structureArray(
                "数码家电",
                "食品饮料",
                "进口食品",
                "美容化妆",
                "母婴玩具",
                "厨房用品",
                "钟表箱包",
                "营养保健",
                "服装鞋靴"));
        mockMvc.perform(get(prefix + "/order/quantity")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(simpleChart));


    }

    /**
     * 按分类统计下单商品数量
     *
     * @throws Exception 异常
     */
    @Test
    public void getGoodsNum() throws Exception {

        /*
         * 按年统计 全平台 2018
         */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "0");
        ChartSeries chartSeries = new ChartSeries("行业下单商品数", ChartUtil.structureArray("0",
                "5",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0"),
                ChartUtil.structureArray(
                        "数码家电", "食品饮料",
                        "进口食品",
                        "美容化妆",
                        "母婴玩具",
                        "厨房用品",
                        "钟表箱包",
                        "营养保健",
                        "服装鞋靴"));
        SimpleChart simpleChart = new SimpleChart(chartSeries, new String[0], ChartUtil.structureArray(
                "数码家电",
                "食品饮料",
                "进口食品",
                "美容化妆",
                "母婴玩具",
                "厨房用品",
                "钟表箱包",
                "营养保健",
                "服装鞋靴"));
        mockMvc.perform(get(prefix + "/goods/num")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(simpleChart));

        /*
         * 按年统计 全平台 2017
         */
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2017");
        params.add("seller_id", "0");
        chartSeries = new ChartSeries("行业下单商品数", ChartUtil.structureArray("0",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0"),
                ChartUtil.structureArray(
                        "数码家电", "食品饮料",
                        "进口食品",
                        "美容化妆",
                        "母婴玩具",
                        "厨房用品",
                        "钟表箱包",
                        "营养保健",
                        "服装鞋靴"));
        simpleChart = new SimpleChart(chartSeries, new String[0], ChartUtil.structureArray(
                "数码家电",
                "食品饮料",
                "进口食品",
                "美容化妆",
                "母婴玩具",
                "厨房用品",
                "钟表箱包",
                "营养保健",
                "服装鞋靴"));
        mockMvc.perform(get(prefix + "/goods/num")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(simpleChart));
        /*
         * 按月统计 全平台 2018-04
         */
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "MONTH");
        params.add("year", "2018");
        params.add("month", "4");
        params.add("seller_id", "0");
        chartSeries = new ChartSeries("行业下单商品数", ChartUtil.structureArray("0",
                "3",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0"),
                ChartUtil.structureArray(
                        "数码家电", "食品饮料",
                        "进口食品",
                        "美容化妆",
                        "母婴玩具",
                        "厨房用品",
                        "钟表箱包",
                        "营养保健",
                        "服装鞋靴"));
        simpleChart = new SimpleChart(chartSeries, new String[0], ChartUtil.structureArray(
                "数码家电",
                "食品饮料",
                "进口食品",
                "美容化妆",
                "母婴玩具",
                "厨房用品",
                "钟表箱包",
                "营养保健",
                "服装鞋靴"));
        mockMvc.perform(get(prefix + "/goods/num")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(simpleChart));
        /*
         * 按月统计 全平台 2018-05
         */
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "MONTH");
        params.add("year", "2018");
        params.add("month", "5");
        params.add("seller_id", "0");
        chartSeries = new ChartSeries("行业下单商品数", ChartUtil.structureArray("0",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0"),
                ChartUtil.structureArray(
                        "数码家电", "食品饮料",
                        "进口食品",
                        "美容化妆",
                        "母婴玩具",
                        "厨房用品",
                        "钟表箱包",
                        "营养保健",
                        "服装鞋靴"));
        simpleChart = new SimpleChart(chartSeries, new String[0], ChartUtil.structureArray(
                "数码家电",
                "食品饮料",
                "进口食品",
                "美容化妆",
                "母婴玩具",
                "厨房用品",
                "钟表箱包",
                "营养保健",
                "服装鞋靴"));
        mockMvc.perform(get(prefix + "/goods/num")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(simpleChart));
        /*
         * 按年查询 店铺 1 2018
         */
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "1");
        chartSeries = new ChartSeries("行业下单商品数", ChartUtil.structureArray("0",
                "4",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0"),
                ChartUtil.structureArray(
                        "数码家电", "食品饮料",
                        "进口食品",
                        "美容化妆",
                        "母婴玩具",
                        "厨房用品",
                        "钟表箱包",
                        "营养保健",
                        "服装鞋靴"));
        simpleChart = new SimpleChart(chartSeries, new String[0], ChartUtil.structureArray(
                "数码家电",
                "食品饮料",
                "进口食品",
                "美容化妆",
                "母婴玩具",
                "厨房用品",
                "钟表箱包",
                "营养保健",
                "服装鞋靴"));
        mockMvc.perform(get(prefix + "/goods/num")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(simpleChart));
        /*
         * 按年查询 店铺 2 2018
         */
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "2");
        chartSeries = new ChartSeries("行业下单商品数", ChartUtil.structureArray("0",
                "1",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0"),
                ChartUtil.structureArray(
                        "数码家电", "食品饮料",
                        "进口食品",
                        "美容化妆",
                        "母婴玩具",
                        "厨房用品",
                        "钟表箱包",
                        "营养保健",
                        "服装鞋靴"));
        simpleChart = new SimpleChart(chartSeries, new String[0], ChartUtil.structureArray(
                "数码家电",
                "食品饮料",
                "进口食品",
                "美容化妆",
                "母婴玩具",
                "厨房用品",
                "钟表箱包",
                "营养保健",
                "服装鞋靴"));
        mockMvc.perform(get(prefix + "/goods/num")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(simpleChart));

    }

    /**
     * 按分类统计下单金额
     *
     * @throws Exception 异常
     */
    @Test
    public void getOrderMoney() throws Exception {
        /*
         * 按年统计 全平台 2018
         */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "0");
        ChartSeries chartSeries = new ChartSeries("行业下单金额", ChartUtil.structureArray("0",
                "544.00",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0"),
                ChartUtil.structureArray(
                        "数码家电", "食品饮料",
                        "进口食品",
                        "美容化妆",
                        "母婴玩具",
                        "厨房用品",
                        "钟表箱包",
                        "营养保健",
                        "服装鞋靴"));
        SimpleChart simpleChart = new SimpleChart(chartSeries, new String[0], ChartUtil.structureArray(
                "数码家电",
                "食品饮料",
                "进口食品",
                "美容化妆",
                "母婴玩具",
                "厨房用品",
                "钟表箱包",
                "营养保健",
                "服装鞋靴"));
        mockMvc.perform(get(prefix + "/order/money")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(simpleChart));

        /*
         * 按年统计 全平台 2017
         */
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2017");
        params.add("seller_id", "0");
        chartSeries = new ChartSeries("行业下单金额", ChartUtil.structureArray("0",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0"),
                ChartUtil.structureArray(
                        "数码家电", "食品饮料",
                        "进口食品",
                        "美容化妆",
                        "母婴玩具",
                        "厨房用品",
                        "钟表箱包",
                        "营养保健",
                        "服装鞋靴"));
        simpleChart = new SimpleChart(chartSeries, new String[0], ChartUtil.structureArray(
                "数码家电",
                "食品饮料",
                "进口食品",
                "美容化妆",
                "母婴玩具",
                "厨房用品",
                "钟表箱包",
                "营养保健",
                "服装鞋靴"));
        mockMvc.perform(get(prefix + "/order/money")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(simpleChart));
        /*
         * 按月统计 全平台 2018-04
         */
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "MONTH");
        params.add("year", "2018");
        params.add("month", "4");
        params.add("seller_id", "0");
        chartSeries = new ChartSeries("行业下单金额", ChartUtil.structureArray("0",
                "246.00",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0"),
                ChartUtil.structureArray(
                        "数码家电", "食品饮料",
                        "进口食品",
                        "美容化妆",
                        "母婴玩具",
                        "厨房用品",
                        "钟表箱包",
                        "营养保健",
                        "服装鞋靴"));
        simpleChart = new SimpleChart(chartSeries, new String[0], ChartUtil.structureArray(
                "数码家电",
                "食品饮料",
                "进口食品",
                "美容化妆",
                "母婴玩具",
                "厨房用品",
                "钟表箱包",
                "营养保健",
                "服装鞋靴"));
        mockMvc.perform(get(prefix + "/order/money")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(simpleChart));
        /*
         * 按月统计 全平台 2018-05
         */
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "MONTH");
        params.add("year", "2018");
        params.add("month", "5");
        params.add("seller_id", "0");
        chartSeries = new ChartSeries("行业下单金额", ChartUtil.structureArray("0",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0"),
                ChartUtil.structureArray(
                        "数码家电", "食品饮料",
                        "进口食品",
                        "美容化妆",
                        "母婴玩具",
                        "厨房用品",
                        "钟表箱包",
                        "营养保健",
                        "服装鞋靴"));
        simpleChart = new SimpleChart(chartSeries, new String[0], ChartUtil.structureArray(
                "数码家电",
                "食品饮料",
                "进口食品",
                "美容化妆",
                "母婴玩具",
                "厨房用品",
                "钟表箱包",
                "营养保健",
                "服装鞋靴"));
        mockMvc.perform(get(prefix + "/order/money")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(simpleChart));
        /*
         * 按年查询 店铺 1 2018
         */
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "1");
        chartSeries = new ChartSeries("行业下单金额", ChartUtil.structureArray("0",
                "421.00",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0"),
                ChartUtil.structureArray(
                        "数码家电", "食品饮料",
                        "进口食品",
                        "美容化妆",
                        "母婴玩具",
                        "厨房用品",
                        "钟表箱包",
                        "营养保健",
                        "服装鞋靴"));
        simpleChart = new SimpleChart(chartSeries, new String[0], ChartUtil.structureArray(
                "数码家电",
                "食品饮料",
                "进口食品",
                "美容化妆",
                "母婴玩具",
                "厨房用品",
                "钟表箱包",
                "营养保健",
                "服装鞋靴"));
        mockMvc.perform(get(prefix + "/order/money")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(simpleChart));
        /*
         * 按年查询 店铺 2 2018
         */
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "2");
        chartSeries = new ChartSeries("行业下单金额", ChartUtil.structureArray("0",
                "123.00",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0",
                "0"),
                ChartUtil.structureArray(
                        "数码家电", "食品饮料",
                        "进口食品",
                        "美容化妆",
                        "母婴玩具",
                        "厨房用品",
                        "钟表箱包",
                        "营养保健",
                        "服装鞋靴"));
        simpleChart = new SimpleChart(chartSeries, new String[0], ChartUtil.structureArray(
                "数码家电",
                "食品饮料",
                "进口食品",
                "美容化妆",
                "母婴玩具",
                "厨房用品",
                "钟表箱包",
                "营养保健",
                "服装鞋靴"));
        mockMvc.perform(get(prefix + "/order/money")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(simpleChart));
    }

    /**
     * 概括总览
     *
     * @throws Exception 异常
     */
    @Test
    public void getGeneralOverview() throws Exception {

        /*
         * 全部分类 全平台
         */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("seller_id", "0");
        params.add("category_id", "0");

        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "数码家电");
        map.put("category_id", 491);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "食品饮料");
        map.put("category_id", 1);
        map.put("goods_total_num", 3);
        map.put("avg_price", 140.333333);
        map.put("sold_goods_num", 3);
        map.put("sold_num", 3);
        map.put("sales_money", 544.0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "进口食品");
        map.put("category_id", 4);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "美容化妆");
        map.put("category_id", 38);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "母婴玩具");
        map.put("category_id", 56);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "厨房用品");
        map.put("category_id", 79);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "钟表箱包");
        map.put("category_id", 35);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "营养保健");
        map.put("category_id", 85);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "服装鞋靴");
        map.put("category_id", 86);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        Page page = new Page(1, (long) data.size(), 10, data);
        mockMvc.perform(get(prefix + "/overview")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));
        /*
         * 全部分类 店铺2
         */
        params = new LinkedMultiValueMap<>();
        params.add("seller_id", "2");
        params.add("category_id", "0");

        data = new ArrayList<>();
        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "数码家电");
        map.put("category_id", 491);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "食品饮料");
        map.put("category_id", 1);
        map.put("goods_total_num", 1);
        map.put("avg_price", 199.0);
        map.put("sold_goods_num", 1);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "进口食品");
        map.put("category_id", 4);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "美容化妆");
        map.put("category_id", 38);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "母婴玩具");
        map.put("category_id", 56);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "厨房用品");
        map.put("category_id", 79);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "钟表箱包");
        map.put("category_id", 35);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "营养保健");
        map.put("category_id", 85);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "服装鞋靴");
        map.put("category_id", 86);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        page = new Page(1, (long) data.size(), 10, data);
        mockMvc.perform(get(prefix + "/overview")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));
        /*
         * 全部分类 店铺1
         */
        params = new LinkedMultiValueMap<>();
        params.add("seller_id", "1");
        params.add("category_id", "0");

        data = new ArrayList<>();
        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "数码家电");
        map.put("category_id", 491);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "食品饮料");
        map.put("category_id", 1);
        map.put("goods_total_num", 2);
        map.put("avg_price", 111.0);
        map.put("sold_goods_num", 2);
        map.put("sold_num", 3);
        map.put("sales_money", 544.0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "进口食品");
        map.put("category_id", 4);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "美容化妆");
        map.put("category_id", 38);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "母婴玩具");
        map.put("category_id", 56);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "厨房用品");
        map.put("category_id", 79);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "钟表箱包");
        map.put("category_id", 35);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "营养保健");
        map.put("category_id", 85);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "服装鞋靴");
        map.put("category_id", 86);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        page = new Page(1, (long) data.size(), 10, data);
        mockMvc.perform(get(prefix + "/overview")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));
        /*
         * 分类1 店铺1
         */
        params = new LinkedMultiValueMap<>();
        params.add("seller_id", "1");
        params.add("category_id", "0");

        data = new ArrayList<>();
        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "数码家电");
        map.put("category_id", 491);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "食品饮料");
        map.put("category_id", 1);
        map.put("goods_total_num", 2);
        map.put("avg_price", 111.0);
        map.put("sold_goods_num", 2);
        map.put("sold_num", 3);
        map.put("sales_money", 544.0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "进口食品");
        map.put("category_id", 4);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "美容化妆");
        map.put("category_id", 38);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "母婴玩具");
        map.put("category_id", 56);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "厨房用品");
        map.put("category_id", 79);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "钟表箱包");
        map.put("category_id", 35);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "营养保健");
        map.put("category_id", 85);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        map = new LinkedCaseInsensitiveMap<>();
        map.put("nosales_goods_num", 0);
        map.put("category_name", "服装鞋靴");
        map.put("category_id", 86);
        map.put("goods_total_num", 0);
        map.put("avg_price", 0.0);
        map.put("sold_goods_num", 0);
        map.put("sold_num", 0);
        map.put("sales_money", 0);
        data.add(map);

        page = new Page(1, (long) data.size(), 10, data);
        mockMvc.perform(get(prefix + "/overview")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));


    }
}