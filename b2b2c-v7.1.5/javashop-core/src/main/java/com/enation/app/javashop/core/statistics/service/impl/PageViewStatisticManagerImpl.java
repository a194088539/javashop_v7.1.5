package com.enation.app.javashop.core.statistics.service.impl;

import com.enation.app.javashop.core.base.SearchCriteria;
import com.enation.app.javashop.core.statistics.StatisticsErrorCode;
import com.enation.app.javashop.core.statistics.StatisticsException;
import com.enation.app.javashop.core.statistics.model.enums.QueryDateType;
import com.enation.app.javashop.core.statistics.model.vo.ChartSeries;
import com.enation.app.javashop.core.statistics.model.vo.SimpleChart;
import com.enation.app.javashop.core.statistics.service.PageViewStatisticManager;
import com.enation.app.javashop.core.statistics.util.ChartUtil;
import com.enation.app.javashop.core.statistics.util.DataDisplayUtil;
import com.enation.app.javashop.core.statistics.util.StatisticsUtil;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.util.SqlUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 平台后台 流量分析
 *
 * @author mengyuanming
 * @version 2.0
 * @since 7.0 2018年3月19日上午9:35:06
 */
@Service
public class PageViewStatisticManagerImpl implements PageViewStatisticManager {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    @Qualifier("sssDaoSupport")
    private DaoSupport daoSupport;

    /**
     * 统计店铺访问量
     *
     * @param searchCriteria 店铺流量参数
     * @return SimpleChart 简单图表数据
     */
    @Override
    public SimpleChart countShop(SearchCriteria searchCriteria) {

        searchCriteria = new SearchCriteria(searchCriteria);
        try {

            // 获取参数，便于使用，年份和月份有默认值
            String type = searchCriteria.getCycleType();
            searchCriteria = new SearchCriteria(searchCriteria);
            Integer year = searchCriteria.getYear();
            Integer month = searchCriteria.getMonth();
            Integer sellerId = searchCriteria.getSellerId();

            // 获取结果大小
            Integer resultSize = DataDisplayUtil.getResultSize(type, year, month);

            String sql;

            // 如果周期为年，则查询每月流量，如果周期为月，则查询每天流量
            if (QueryDateType.YEAR.value().equals(type)) {
                sql = "select sum(vs_num) as num,vs_month from es_sss_shop_pv ";
            } else {
                sql = "select sum(vs_num) as num,vs_day from es_sss_shop_pv ";
            }

            List<Integer> paramList = new ArrayList<>();
            // 查询条件集合，和参数集合
            List<String> sqlList = new ArrayList<>();
            if (type.equals(QueryDateType.YEAR.value())) {
                sqlList.add(" vs_year = ? ");
                paramList.add(year);
            } else if (type.equals(QueryDateType.MONTH.value())) {
                sqlList.add(" vs_year = ? ");
                sqlList.add(" vs_month = ? ");
                paramList.add(year);
                paramList.add(month);
            }

            // 如果店铺id不为空，则添加查询条件
            if (null != sellerId && sellerId != 0) {
                sqlList.add(" seller_id = ? ");
                paramList.add(sellerId);
            }
            // 如果按年查询，则按月分组，如果按月查询，则按天分组
            if (QueryDateType.YEAR.value().equals(type)) {
                sql += SqlUtil.sqlSplicing(sqlList) + " group by vs_month ";
            } else {
                sql += SqlUtil.sqlSplicing(sqlList) + " group by vs_day ";
            }
            List<Map<String, Object>> list = StatisticsUtil.getDataList(this.daoSupport, searchCriteria.getYear(), sql, paramList.toArray());

            // 获取x轴数据各区段数据
            String[] data = new String[resultSize];

            String[] localName = new String[resultSize];

            // 长度为日期的循环
            for (int i = 0; i < resultSize; i++) {
                // 遍历list
                for (Map map : list) {
                    // 如果按年查询，获取vs_month字段，如果按月查询，获取vs_day字段
                    if (QueryDateType.YEAR.value().equals(type)) {
                        // 判断月份是否与下标相等，是插入数据库数据，否则插入0
                        if ((i + 1 + "").equals(map.get("vs_month").toString())) {
                            data[i] = map.get("num").toString();
                        }
                    } else {
                        // 判断日期是否与下标相等，是插入数据库数据，否则插入0
                        if ((i + 1 + "").equals(map.get("vs_day").toString())) {
                            data[i] = map.get("num").toString();
                        }
                    }
                }
                // 如果未找到合适数据，则填入0
                if (null == data[i]) {
                    data[i] = "0";
                }
                localName[i] = i + 1 + "";
            }


            ChartSeries series = new ChartSeries("访问量", data, localName);

            return new SimpleChart(series, ChartUtil.structureXAxis(type, year, month), new String[0]);

        } catch (Exception e) {
            logger.error(e);
            throw new StatisticsException(StatisticsErrorCode.E810.code(), "业务处理异常");
        }

    }

    /**
     * 统计商品访问量
     *
     * @param searchCriteria 时间参数
     * @return SimpleChart 简单图表数据
     */
    @Override
    public SimpleChart countGoods(SearchCriteria searchCriteria) {

        searchCriteria = new SearchCriteria(searchCriteria);
        // 获取参数，便于使用
        String type = searchCriteria.getCycleType();
        Integer sellerId = searchCriteria.getSellerId();

        String sql = "select sum(vs_num) as num, goods_name from es_sss_goods_pv ";

        // 查询条件集合，和参数集合
        List<String> sqlList = new ArrayList<>();
        List<Integer> paramList = new ArrayList<>();

        if (type.equals(QueryDateType.YEAR.value())) {
            sqlList.add(" vs_year = ? ");
            paramList.add(searchCriteria.getYear());
        } else if (type.equals(QueryDateType.MONTH.value())) {
            sqlList.add(" vs_month = ? ");
            paramList.add(searchCriteria.getMonth());
        }
        // 如果店铺id不为空，则添加查询条件
        if (null != sellerId && sellerId != 0) {
            sqlList.add(" seller_id = ? ");
            paramList.add(sellerId);
        }

        // 按商品名分组，按流量排序，取前30
        sql += SqlUtil.sqlSplicing(sqlList) + " group by goods_id,goods_name order by num desc limit 30  ";

        List<Map<String, Object>> list = StatisticsUtil.getDataList(this.daoSupport, searchCriteria.getYear(), sql, paramList.toArray());

        int dataLength = 30;

        // 获取x轴数据，包括商品名和访问量
        String[] data = new String[dataLength];
        String[] goodsName = new String[dataLength];
        // 获取x轴刻度
        String[] xAxis = new String[dataLength];
        // 赋值
        for (int i = 0; i < dataLength; i++) {
            if (null != list && i < list.size()) {
                Map map = list.get(i);
                data[i] = map.get("num").toString();
                goodsName[i] = map.get("goods_name").toString();
            } else {
                data[i] = "无";
                goodsName[i] = "无";
            }
            xAxis[i] = i + 1 + "";
        }

        ChartSeries series = new ChartSeries("访问量", data, goodsName);

        return new SimpleChart(series, xAxis, new String[0]);

    }
}
