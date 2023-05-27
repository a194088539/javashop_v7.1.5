package com.enation.app.javashop.core.distribution.service.impl;

import com.enation.app.javashop.core.base.SearchCriteria;
import com.enation.app.javashop.core.distribution.model.vo.SellerPushVO;
import com.enation.app.javashop.core.distribution.service.DistributionStatisticManager;
import com.enation.app.javashop.core.statistics.model.enums.QueryDateType;
import com.enation.app.javashop.core.statistics.model.vo.ChartSeries;
import com.enation.app.javashop.core.statistics.model.vo.SimpleChart;
import com.enation.app.javashop.core.statistics.util.DataDisplayUtil;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.util.CurrencyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * DistributionStatisticManagerImpl
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-06-13 上午8:37
 */
@Service
public class DistributionStatisticManagerImpl implements DistributionStatisticManager {

    @Autowired
    @Qualifier("distributionDaoSupport")
    private DaoSupport daoSupport;

    @Override
    public SimpleChart getOrderMoney(String circle, Integer memberId, Integer year, Integer month) {


        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setMonth(month);
        searchCriteria.setYear(year);
        searchCriteria.setCycleType(circle);

        searchCriteria = new SearchCriteria(searchCriteria);

        long[] timesTramp = DataDisplayUtil.getStartTimeAndEndTime(searchCriteria);


        String sql = "select SUM(order_price) order_price,FROM_UNIXTIME(create_time, ?) date from es_distribution_order where create_time > ? and create_time < ? AND (member_id_lv1 = ?||member_id_lv2 = ?)";

        Integer resultSize = DataDisplayUtil.getResultSize(searchCriteria);

        String circleWhere = "";
        if (Objects.equals(searchCriteria.getCycleType(), QueryDateType.YEAR.name())) {
            circleWhere = "%m";
        } else {
            circleWhere = "%d";
        }
        List<Map<String, Object>> list = this.daoSupport.queryForList(sql.toString(), circleWhere, timesTramp[0], timesTramp[1], memberId, memberId);
        String[] xAxis = new String[resultSize],
                data = new String[resultSize];

        for (int i = 0; i < resultSize; i++) {

            data[i] = 0 + "";
            for (Map<String, Object> map : list) {
                try {
                    if (Integer.parseInt(map.get("date").toString()) == (i + 1)) {
                        data[i] = map.get("order_price").toString();
                    }
                } catch (NullPointerException e) {
                }
            }
            xAxis[i] = i + 1 + "";
        }

        ChartSeries chartSeries = new ChartSeries("订单金额统计", data, new String[0]);

        SimpleChart simpleChart = new SimpleChart(chartSeries, xAxis, new String[0]);

        return simpleChart;
    }

    @Override
    public SimpleChart getPushMoney(String circle, Integer memberId, Integer year, Integer month) {


        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setMonth(month);
        searchCriteria.setYear(year);
        searchCriteria.setCycleType(circle);

        searchCriteria = new SearchCriteria(searchCriteria);


        long[] timesTramp = DataDisplayUtil.getStartTimeAndEndTime(searchCriteria);
        String sql = "select SUM(grade1_rebate) grade_rebate,FROM_UNIXTIME(create_time, ?) date from es_distribution_order where create_time > ? and create_time < ? AND (member_id_lv1 = ?)";
        String sql2 = "select SUM(grade2_rebate) grade_rebate,FROM_UNIXTIME(create_time, ?) date from es_distribution_order where create_time > ? and create_time < ? AND (member_id_lv2 = ?)";

        Integer resultSize = DataDisplayUtil.getResultSize(searchCriteria);

        String circleWhere = "";
        if (Objects.equals(searchCriteria.getCycleType(), QueryDateType.YEAR.name())) {
            circleWhere = "%m";
        } else {
            circleWhere = "%d";
        }
        List<Map<String, Object>> list = this.daoSupport.queryForList(sql.toString(), circleWhere, timesTramp[0], timesTramp[1], memberId);
        List<Map<String, Object>> list2 = this.daoSupport.queryForList(sql2.toString(), circleWhere, timesTramp[0], timesTramp[1], memberId);

        List<Map<String, Object>> result = new ArrayList<>();

        for (int i = 0; i < resultSize; i++) {
            double finalRebate = 0;
            for (Map<String, Object> map : list) {
                try {
                    if (Integer.parseInt(map.get("date").toString()) == (i + 1)) {
                        finalRebate = CurrencyUtil.add(finalRebate, Double.parseDouble(map.get("grade_rebate").toString()));
                    }
                } catch (NullPointerException e) {
                }
            }
            for (Map<String, Object> map : list2) {
                try {
                    if (Integer.parseInt(map.get("date").toString()) == (i + 1)) {
                        finalRebate = CurrencyUtil.add(finalRebate, Double.parseDouble(map.get("grade_rebate").toString()));
                    }
                } catch (NullPointerException e) {
                }
            }
            Map<String, Object> map = new HashMap<>(16);
            map.put("date", i + 1);
            map.put("grade_rebate", finalRebate);
            result.add(map);
        }

        String[] xAxis = new String[resultSize],
                data = new String[resultSize];

        for (int i = 0; i < resultSize; i++) {

            data[i] = 0 + "";
            for (Map<String, Object> map : result) {
                if (Integer.parseInt(map.get("date").toString()) == (i + 1)) {
                    data[i] = map.get("grade_rebate").toString();
                }
            }
            xAxis[i] = i + 1 + "";
        }

        ChartSeries chartSeries = new ChartSeries("订单提成统计", data, new String[0]);

        SimpleChart simpleChart = new SimpleChart(chartSeries, xAxis, new String[0]);
        return simpleChart;
    }

    @Override
    public SimpleChart getOrderCount(String circle, Integer memberId, Integer year, Integer month) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setMonth(month);
        searchCriteria.setYear(year);
        searchCriteria.setCycleType(circle);

        searchCriteria = new SearchCriteria(searchCriteria);


        long[] timesTramp = DataDisplayUtil.getStartTimeAndEndTime(searchCriteria);
        String sql = "select count(0) count,FROM_UNIXTIME(create_time, ?) date from es_distribution_order where create_time > ? and create_time < ? AND (member_id_lv1 = ?||member_id_lv2 = ?)";

        Integer resultSize = DataDisplayUtil.getResultSize(searchCriteria);

        String circleWhere = "";
        if (Objects.equals(searchCriteria.getCycleType(), QueryDateType.YEAR.name())) {
            circleWhere = "%m";
        } else {
            circleWhere = "%d";
        }
        List<Map<String, Object>> list = this.daoSupport.queryForList(sql.toString(), circleWhere, timesTramp[0], timesTramp[1], memberId, memberId);

        String[] xAxis = new String[resultSize],
                data = new String[resultSize];

        for (int i = 0; i < resultSize; i++) {

            data[i] = 0 + "";
            for (Map<String, Object> map : list) {
                try {
                    if (Integer.parseInt(map.get("date").toString()) == (i + 1)) {
                        data[i] = map.get("count").toString();
                    }
                } catch (NullPointerException e) {
                }
            }
            xAxis[i] = i + 1 + "";
        }

        ChartSeries chartSeries = new ChartSeries("订单数量统计", data, new String[0]);

        SimpleChart simpleChart = new SimpleChart(chartSeries, xAxis, new String[0]);

        return simpleChart;
    }


    @Override
    public Page getShopPush(String circle, Integer year, Integer month, Integer pageSize, Integer pageNo) {

        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setMonth(month);
        searchCriteria.setYear(year);
        searchCriteria.setCycleType(circle);

        searchCriteria = new SearchCriteria(searchCriteria);
        long[] timesTramp = DataDisplayUtil.getStartTimeAndEndTime(searchCriteria);


        return this.daoSupport.queryForPage("select sum(grade1_rebate)+sum(grade2_rebate) push_money,seller_name,seller_id from es_distribution_order where create_time > ? and create_time < ? group by seller_id ", pageNo, pageSize, SellerPushVO.class, timesTramp[0], timesTramp[1]);


    }
}
