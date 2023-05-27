package com.enation.app.javashop.manager.api.statistics;

import com.enation.app.javashop.core.client.statistics.SyncopateTableClient;
import com.enation.app.javashop.core.statistics.model.vo.ChartSeries;
import com.enation.app.javashop.core.statistics.model.vo.SimpleChart;
import com.enation.app.javashop.core.statistics.util.ChartUtil;
import com.enation.app.javashop.core.statistics.util.DataDisplayUtil;
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
 * 统计 会员分析 测试类
 *
 * @author chopper
 * @version v1.0
 * @since v7.0
 * 2018/4/3 下午4:46
 */
@Rollback(true)
public class MemberStatisticControllerTest extends BaseTest {


    //api 前缀
    private String prefix = "/admin/statistics/member";

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



    /**
     * 后台-》会员分析-》新增会员统计
     */
    @Test
    public void adminIncreaseMember() throws Exception {
        ChartSeries chartSeries = new ChartSeries("新增会员数量", ChartUtil.structureArray("0", "0", "3", "2", "0", "0", "0", "0", "0", "0", "0", "0"),
                new String[0]);
        SimpleChart simpleChart = new SimpleChart(chartSeries, ChartUtil.structureArray("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"), new String[0]);


        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "1");
        mockMvc.perform(get(prefix + "/increase/member")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(simpleChart));


    }


    /**
     * 后台-》会员分析-》新增会员统计page
     */
    @Test
    public void adminIncreaseMemberPage() throws Exception {

        /*
         * 按年查询 2018
         */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "0");

        List<Map<String, Object>> data = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("last_num", 0);
            if (i == 3) {
                map.put("num", 3);
            } else if (i == 4) {
                map.put("num", 2);
            } else {
                map.put("num", 0);
            }
            map.put("growth", 0);
            map.put("time", DataDisplayUtil.formatDate(i));
            data.add(map);
        }
        Page page = new Page(1, 12L, 12, data);

        mockMvc.perform(get(prefix + "/increase/member/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));


        /*
         * 按年查询 2017
         */
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2017");
        params.add("seller_id", "0");

        data = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("last_num", 0);
            map.put("num", 0);
            map.put("growth", 0);
            map.put("time", DataDisplayUtil.formatDate(i));
            data.add(map);
        }
        page = new Page(1, 12L, 12, data);
        mockMvc.perform(get(prefix + "/increase/member/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));


        /*
         * 按月查询 2018-04
         */
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "MONTH");
        params.add("year", "2018");
        params.add("month", "4");
        params.add("seller_id", "0");

        data = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            Map<String, Object> map = new HashMap<>();
            if (i == 22) {
                map.put("last_num", 2);
            } else {
                map.put("last_num", 0);
            }
            if (i == 12 || i == 22) {
                map.put("num", 1);
            } else {
                map.put("num", 0);
            }
            if(i == 22){
                map.put("growth", 1);
            }else {
                map.put("growth", 0);
            }


            if(i == 22){
                map.put("growth", "-50%");
            }else {
                map.put("growth", 0);
            }

            map.put("time", DataDisplayUtil.formatDate(i));
            data.add(map);
        }
        page = new Page(1, 30L, 30, data);
        mockMvc.perform(get(prefix + "/increase/member/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));


        /*
         * 按月查询 2018-05
         */
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "MONTH");
        params.add("year", "2018");
        params.add("month", "5");
        params.add("seller_id", "0");

        data = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("last_num", 0);
            map.put("num", 0);
            map.put("growth", 0);
            map.put("time", DataDisplayUtil.formatDate(i));
            data.add(map);
        }
        page = new Page(1, 31L, 31, data);
        mockMvc.perform(get(prefix + "/increase/member/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));

    }

    /**
     * 统计会员下单量
     *
     * @throws Exception 异常
     */
    @Test
    public void adminMemberOrderQuantityPage() throws Exception {

        /*
         * 按照年查询 所有店铺 日期：2018
         */
        List<Map<String,Object>> data = null;
        Page page = null;

        data = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();

        map = new HashMap<>();
        map.put("order_num",2);
        map.put("member_name","测试会员2");
        data.add(map);

        map = new HashMap<>();
        map.put("order_num",1);
        map.put("member_name","测试会员");
        data.add(map);

        page = new Page(1,(long)data.size(),10,data);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "0");
        mockMvc.perform(get(prefix + "/order/quantity/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));
        /*
         * 按照年查询所有店铺 日期：2017
         */

        page = new Page(1,0l,10,new ArrayList());
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2017");
        params.add("seller_id", "0");
        mockMvc.perform(get(prefix + "/order/quantity/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));
        /*
         * 按照年查询 店铺：2 ；日期： 2018
         */

        data = new ArrayList<>();
        map = new HashMap<>();
        map.put("order_num",1);
        map.put("member_name","测试会员2");
        data.add(map);
        page = new Page(1,(long)data.size(),10,data);
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "2");
        mockMvc.perform(get(prefix + "/order/quantity/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));
        /*
         * 按照月查询 店铺：2 ；日期： 2018-04
         */
        data = new ArrayList<>();
        map = new HashMap<>();
        map.put("order_num",1);
        map.put("member_name","测试会员2");
        data.add(map);
        page = new Page(1,(long)data.size(),10,data);
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "MONTH");
        params.add("year", "2018");
        params.add("month", "4");
        params.add("seller_id", "2");
        mockMvc.perform(get(prefix + "/order/quantity/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(
                objectEquals(page));

        /*
         * 按照月查询 店铺：1 ；日期： 2018-04
         */

        data = new ArrayList<>();
        page = new Page(1,(long)data.size(),10,data);
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "MONTH");
        params.add("year", "2018");
        params.add("month", "4");
        params.add("seller_id", "1");
        mockMvc.perform(get(prefix + "/order/quantity/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(
                objectEquals(page));


    }
    /**
     * 统计会员下单量
     *
     * @throws Exception 异常
     */
    @Test
    public void adminMemberOrderQuantity() throws Exception {
        ChartSeries chartSeries = new ChartSeries("会员下单量", ChartUtil.structureArray("1","1", null, null, null, null, null, null, null, null), ChartUtil.structureArray("测试会员","测试会员2", null, null, null, null, null, null, null, null));
        SimpleChart simpleChart = new SimpleChart(chartSeries, ChartUtil.structureArray("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"), new String[0]);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "1");
        mockMvc.perform(get(prefix + "/order/quantity")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(simpleChart));

    }

    /**
     * 统计下单商品数量
     * @throws Exception 异常
     */
    @Test
    public void adminMemberGoodsNumPage() throws Exception {
        /*
         * 按照年查询 所有店铺 日期：2018
         */
        List<Map<String,Object>> data = null;
        Page page = null;

        data = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();

        map = new HashMap<>();
        map.put("goods_num",3);
        map.put("member_name","测试会员2");
        data.add(map);

        map = new HashMap<>();
        map.put("goods_num",2);
        map.put("member_name","测试会员");
        data.add(map);

        page = new Page(1,(long)data.size(),10,data);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "0");
        mockMvc.perform(get(prefix + "/order/goods/num/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));
        /*
         * 按照年查询所有店铺 日期：2017
         */

        page = new Page(1,0L,10,new ArrayList());
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2017");
        params.add("seller_id", "0");
        mockMvc.perform(get(prefix + "/order/goods/num/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));
        /*
         * 按照年查询 店铺：2 ；日期： 2018
         */
        data = new ArrayList<>();
        map = new HashMap<>();
        map.put("goods_num",1);
        map.put("member_name","测试会员2");
        data.add(map);
        page = new Page(1,(long)data.size(),10,data);
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "2");
        mockMvc.perform(get(prefix + "/order/goods/num/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));
        /*
         * 按照月查询 店铺：2 ；日期： 2018-04
         */
        data = new ArrayList<>();
        map = new HashMap<>();
        map.put("goods_num",1);
        map.put("member_name","测试会员2");
        data.add(map);
        page = new Page(1,(long)data.size(),10,data);
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "MONTH");
        params.add("year", "2018");
        params.add("month", "4");
        params.add("seller_id", "2");
        mockMvc.perform(get(prefix + "/order/goods/num/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));

        /*
         * 按照月查询 店铺：1 ；日期： 2018-04
         */

        data = new ArrayList<>();
        page = new Page(1,(long)data.size(),10,data);
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "MONTH");
        params.add("year", "2018");
        params.add("month", "4");
        params.add("seller_id", "1");
        mockMvc.perform(get(prefix + "/order/goods/num/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));


    }
    /**
     * 统计下单商品数量
     * @throws Exception 异常
     */
    @Test
    public void adminMemberGoodsNum() throws Exception {

        ChartSeries chartSeries = new ChartSeries("会员下单商品数", ChartUtil.structureArray("3","2", null, null, null, null, null, null, null, null), ChartUtil.structureArray("测试会员2","测试会员", null, null, null, null, null, null, null, null));
        SimpleChart simpleChart = new SimpleChart(chartSeries, ChartUtil.structureArray("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"), new String[0]);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "0");
        mockMvc.perform(get(prefix + "/order/goods/num")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(simpleChart));

    }

    /**
     * 统计下单总金额
     *
     * @throws Exception 异常
     */
    @Test
    public void adminMemberMoneyPage() throws Exception {

        /*
         * 按照年查询 所有店铺 日期：2018
         */
        List<Map<String,Object>> data = null;
        Page page = null;

        data = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();

        map = new HashMap<>();
        map.put("total_money",369.0);
        map.put("member_name","测试会员2");
        data.add(map);

        map = new HashMap<>();
        map.put("total_money",298.0);
        map.put("member_name","测试会员");
        data.add(map);

        page = new Page(1,(long)data.size(),10,data);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "0");
        mockMvc.perform(get(prefix + "/order/money/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));
        /*
         * 按照年查询所有店铺 日期：2017
         */

        page = new Page(1,0L,10,new ArrayList());
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2017");
        params.add("seller_id", "0");
        mockMvc.perform(get(prefix + "/order/money/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));
        /*
         * 按照年查询 店铺：2 ；日期： 2018
         */

        data = new ArrayList<>();
        map = new HashMap<>();
        map.put("total_money",123.0);
        map.put("member_name","测试会员2");
        data.add(map);
        page = new Page(1,(long)data.size(),10,data);
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "2");
        mockMvc.perform(get(prefix + "/order/money/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));
        /*
         * 按照月查询 店铺：2 ；日期： 2018-04
         */
        data = new ArrayList<>();
        map = new HashMap<>();
        map.put("total_money",123.0);
        map.put("member_name","测试会员2");
        data.add(map);
        page = new Page(1,(long)data.size(),10,data);
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "MONTH");
        params.add("year", "2018");
        params.add("month", "4");
        params.add("seller_id", "2");
        mockMvc.perform(get(prefix + "/order/money/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));

        /*
         * 按照月查询 店铺：1 ；日期： 2018-04
         */

        data = new ArrayList<>();
        page = new Page(1,(long)data.size(),10,data);
        params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "MONTH");
        params.add("year", "2018");
        params.add("month", "4");
        params.add("seller_id", "1");
        mockMvc.perform(get(prefix + "/order/money/page")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(page));

    }
    /**
     * 统计下单总金额
     *
     * @throws Exception 异常
     */
    @Test
    public void adminMemberMoney() throws Exception {

        ChartSeries chartSeries = new ChartSeries("会员下单金额", ChartUtil.structureArray("369.00","298.00", null, null, null, null, null, null, null, null), ChartUtil.structureArray("测试会员2","测试会员", null, null, null, null, null, null, null, null));
        SimpleChart simpleChart = new SimpleChart(chartSeries, ChartUtil.structureArray("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"),new String[0]);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cycle_type", "YEAR");
        params.add("year", "2018");
        params.add("seller_id", "0");
        mockMvc.perform(get(prefix + "/order/money")
                .header("Authorization", superAdmin)
                .params(params)).andExpect(objectEquals(simpleChart));

    }

}
