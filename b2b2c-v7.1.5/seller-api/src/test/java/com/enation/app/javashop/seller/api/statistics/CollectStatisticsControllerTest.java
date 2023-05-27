package com.enation.app.javashop.seller.api.statistics;

import com.enation.app.javashop.core.statistics.model.vo.ChartSeries;
import com.enation.app.javashop.core.statistics.model.vo.SimpleChart;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.test.BaseTest;
import org.apache.commons.collections.map.LinkedMap;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * 商家中心，商品收藏量统计测试
 *
 * @author mengyuanming
 * @version 2.0
 * @since 7.0
 * 2018/5/17 10:58
 */
public class CollectStatisticsControllerTest extends BaseTest {

    @Autowired
    @Qualifier("sssDaoSupport")
    private DaoSupport daoSupport;

    /**
     * 测试前置条件
     */
    @Before
    @Transactional(value = "sssTransactionManager")
    public void preparation() {

        // 因为TRUNCATE TABLE回滚困难，所以使用delete
        String cleanTable = "DELETE FROM `es_sss_goods_data` WHERE 1=1 ";
        // 清空表中数据
        this.daoSupport.execute(cleanTable);
        // 插入测试数据
        String insertData = "INSERT INTO `es_sss_goods_data`" +
                " VALUES ('1', '1', '测试商品1', '1', '1', '|0|1|3|', '3', '1', '99.00', '99', '1')," +
                " ('2', '2', '测试商品2', '2', '2', '|0|1|2|', '4', '2', '199.00', '199', '1')," +
                " ('3', '3', '测试商品3', '2', '2', '|0|1|2|', '3', '2', '123.00', '1221', '1');";
        this.daoSupport.execute(insertData);

    }

    /**
     * 商品收藏图表数据测试
     *
     * @throws Exception 异常
     */
    @Test
    @Transactional(value = "sssTransactionManager")
    public void getChart() throws Exception {

        // 构建预期数据
        String[] data = {"1221", "99"};
        String[] localName = {"测试商品3", "测试商品1"};
        String[] xAxis = {"1", "2"};

        ChartSeries chartSeries = new ChartSeries("收藏数", data, localName);

        SimpleChart simpleChart = new SimpleChart(chartSeries, xAxis, new String[0]);

        // 模拟获取商品收藏图表数据，并与预期数据进行对比
        mockMvc.perform(get("/seller/statistics/collect/chart").header("Authorization", seller1))
                .andExpect(objectEquals(simpleChart));

    }

    /**
     * 商品收藏表格数据测试
     *
     * @throws Exception 异常
     */
    @Test
    @Transactional(value = "sssTransactionManager")
    public void getPage() throws Exception {

        // 添加预期数据
        ArrayList<Map> list = new ArrayList<>();
        LinkedMap map = new LinkedMap();
        map.put("goods_name", "测试商品3");
        map.put("price", 123.00);
        map.put("favorite_num", 1221);
        list.add(map);
        LinkedMap map2 = new LinkedMap();
        map2.put("goods_name", "测试商品1");
        map2.put("price", 99.00);
        map2.put("favorite_num", 99);
        list.add(map2);
        Page page = new Page(1, 2L, 10, list);

        // 模拟请求，参数为page_no，page_size，对比返回值与预期值
        mockMvc.perform(get("/seller/statistics/collect/page").header("Authorization", seller1)
                .param("page_no", "1").param("page_size", "10")).andExpect(objectEquals(page));

    }

}
