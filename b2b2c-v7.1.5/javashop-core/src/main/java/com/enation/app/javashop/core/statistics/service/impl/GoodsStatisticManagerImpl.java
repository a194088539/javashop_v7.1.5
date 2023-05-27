package com.enation.app.javashop.core.statistics.service.impl;

import com.enation.app.javashop.core.base.SearchCriteria;
import com.enation.app.javashop.core.statistics.StatisticsErrorCode;
import com.enation.app.javashop.core.statistics.StatisticsException;
import com.enation.app.javashop.core.statistics.model.vo.ChartSeries;
import com.enation.app.javashop.core.statistics.model.vo.SimpleChart;
import com.enation.app.javashop.core.statistics.service.GoodsStatisticManager;
import com.enation.app.javashop.core.statistics.util.ChartSqlUtil;
import com.enation.app.javashop.core.statistics.util.DataDisplayUtil;
import com.enation.app.javashop.core.statistics.util.StatisticsUtil;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.util.SqlUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品统计
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-03-23 上午4:17
 */
@Service
public class GoodsStatisticManagerImpl implements GoodsStatisticManager {


    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    @Qualifier("sssDaoSupport")
    private DaoSupport daoSupport;



    /**
     * 价格销量统计
     *
     * @param searchCriteria 搜索参数
     * @param prices         价格区间
     * @return chart
     */
    @Override
    public SimpleChart getPriceSales(SearchCriteria searchCriteria, Integer[] prices) {

        searchCriteria = new SearchCriteria(searchCriteria);

        //价格参数判定
        int notAvailable = 2;
        if (prices == null || prices.length < notAvailable) {
            prices = SearchCriteria.defaultPrice();
        }

        /*
         * 获取 开始/结束 时间
         */
        long[] timestamp = DataDisplayUtil.getStartTimeAndEndTime(searchCriteria);

        /*
         * 参数
         */
        List<Object> params = new ArrayList<>();

        /*
         * 查询sql
         */
        StringBuffer sql = new StringBuffer();
        sql.append("select count(0) as t_num , case ");
        ChartSqlUtil.appendPriceSql(prices, sql, params);
        sql.append(" as price_interval  from es_sss_order_goods_data oi left join es_sss_order_data o on oi.order_sn=o.sn "
                + " where o.create_time >= ? and  o.create_time <=? ");
        params.add(timestamp[0]);
        params.add(timestamp[1]);

        /*
         * 分类参数判定
         */
        if (searchCriteria.getCategoryId() != 0) {
            sql.append(ChartSqlUtil.categoryOrderGoodsSql());
            params.add(ChartSqlUtil.categoryLikeParams(searchCriteria.getCategoryId()));
        }

        /*
         * 商家判定sql
         */
        if (searchCriteria.getSellerId() != 0) {
            sql.append(ChartSqlUtil.orderSellerSql());
            params.add(searchCriteria.getSellerId());
        }

        /*
         * group 条件
         */
        sql.append(ChartSqlUtil.priceGroupSql(prices));
        for (int i = 1; i < prices.length; i++) {
            params.add(prices[i - 1]);
            params.add(prices[i]);
            params.add(i);
        }
        try {
            List<Map<String, Object>> queryList = StatisticsUtil.getDataList(this.daoSupport, searchCriteria.getYear(), sql.toString(), params.toArray());
            ArrayList<Map<String, Object>> dataList = new ArrayList<>();

            for (int i = 0; i < prices.length - 1; i++) {
                Map<String, Object> m = new HashMap<>(16);
                m.put("t_num", 0);
                m.put("price_interval", i + 1);
                m.put("prices", prices[i]);
                dataList.add(m);

                for (Map map : queryList) {
                    if ((int) map.get("price_interval") == (i + 1)) {
                        m.put("t_num", map.get("t_num"));
                    }
                }

            }

            String[] xAxis = new String[dataList.size()], localName = new String[dataList.size()], data = new String[dataList.size()];

            int index = 0;
            if (dataList.size() > 0) {
                for (Map map : dataList) {
                    xAxis[index] = prices[index] + "~" + prices[index + 1];
                    localName[index] = xAxis[index];
                    data[index] = map.get("t_num").toString();

                    index++;
                }
            }

            ChartSeries chartSeries = new ChartSeries("下单金额", data, localName);

            return new SimpleChart(chartSeries, xAxis, new String[0]);
        }catch (BadSqlGrammarException e) {

            //某个年份的统计表不存在，则返回空数据
            if(e.getMessage().endsWith("doesn't exist")){

                ChartSeries chartSeries = new ChartSeries("下单金额", new String[]{"0","0","0","0"},
                        new String[]{"0~100","100~1000","1000~10000","10000~100000"});
                return new SimpleChart(chartSeries, new String[]{"0~100","100~1000","1000~10000","10000~100000"}, new String[0]);
            }else{
                e.printStackTrace();
                logger.error(e);
                throw new StatisticsException(StatisticsErrorCode.E810.code(), StatisticsErrorCode.E810.des());
            }

        }


    }

    /**
     * 热卖商品按金额统计
     *
     * @param searchCriteria 搜索参数
     * @return 热卖商品按金额统计Chart
     */
    @Override
    public SimpleChart getHotSalesMoney(SearchCriteria searchCriteria) {

        searchCriteria = new SearchCriteria(searchCriteria);

        int dataLength = 50;

        try {
            List<Map<String, Object>> dataList = this.getHotSalesMoneyPage(searchCriteria).getData();
            String[] xAxis = new String[dataLength], localName = new String[dataLength], data = new String[dataLength];

            for (int i = 0; i < dataLength; i++) {
                if (null != dataList && i < dataList.size()) {
                    Map map = dataList.get(i);
                    data[i] = map.get("price").toString();
                    localName[i] = map.get("goods_name").toString();
                } else {
                    data[i] = "0";
                    localName[i] = "无";
                }
                xAxis[i] = i + 1 + "";
            }

            ChartSeries chartSeries = new ChartSeries("下单金额", data, localName);

            return new SimpleChart(chartSeries, xAxis, new String[0]);
        } catch (BadSqlGrammarException e) {

            //某个年份的统计表不存在，则返回空数据
            if(e.getMessage().endsWith("doesn't exist")){
                String[] xAxis = new String[dataLength];
                String[] data = new String[dataLength];
                String[] localName = new String[dataLength];
                for(int i = 0; i < dataLength; i++){
                    xAxis[i] = i+1+"";
                    data[i] = null;
                    localName[i] = null;
                }

                ChartSeries chartSeries = new ChartSeries("下单金额", data, localName);

                return new SimpleChart(chartSeries, xAxis, new String[0]);
            }else{
                e.printStackTrace();
                logger.error(e);
                throw new StatisticsException(StatisticsErrorCode.E810.code(), StatisticsErrorCode.E810.des());
            }

        }catch (Exception e) {
            logger.error(e);
            throw new StatisticsException(StatisticsErrorCode.E810.code(), StatisticsErrorCode.E810.des());
        }

    }

    /**
     * 热卖商品按金额统计
     *
     * @param searchCriteria 搜索参数
     * @return 热卖商品按金额统计page
     */
    @Override
    public Page getHotSalesMoneyPage(SearchCriteria searchCriteria) {

        searchCriteria = new SearchCriteria(searchCriteria);

        long[] timestamp = DataDisplayUtil.getStartTimeAndEndTime(searchCriteria);
        /*
         * 参数
         */
        List<Object> list = new ArrayList<>();

        /*
         * 查询sql
         */
        StringBuffer sql = new StringBuffer();


        sql.append("SELECT sum(oi.price) as price,oi.`goods_name` FROM es_sss_order_goods_data oi,es_sss_order_data o WHERE oi.`order_sn` = o.`sn` AND o.`create_time` >= ? AND o.`create_time` <= ? ");
        list.add(timestamp[0]);
        list.add(timestamp[1]);

        /*
         * 分类参数判定
         */
        if (searchCriteria.getCategoryId() != 0) {
            sql.append(ChartSqlUtil.categoryOrderGoodsSql());
            list.add(ChartSqlUtil.categoryLikeParams(searchCriteria.getCategoryId()));
        }

        /*
         * 商家判定sql
         */
        if (searchCriteria.getSellerId() != 0) {
            sql.append(ChartSqlUtil.orderSellerSql());
            list.add(searchCriteria.getSellerId());
        }

        sql.append("  GROUP BY oi.goods_id,price,oi.goods_name order by price desc,goods_name asc");

        return StatisticsUtil.getDataPage(this.daoSupport, searchCriteria.getYear(), sql.toString(), 1, 50, list.toArray());


    }

    /**
     * 热卖商品按数量统计
     *
     * @param searchCriteria 搜索参数
     * @return SimpleChart   热卖商品按数量统计chart
     */
    @Override
    public SimpleChart getHotSalesNum(SearchCriteria searchCriteria) {
        searchCriteria = new SearchCriteria(searchCriteria);


        try {

            List<Map<String, Object>> dataList = this.getHotSalesNumPage(searchCriteria).getData();

            int dataLength = 50;
            String[] xAxis = new String[dataLength], localName = new String[dataLength], data = new String[dataLength];

            for (int i = 0; i < dataLength; i++) {
                if (null != dataList && i < dataList.size()) {
                    Map map = dataList.get(i);
                    data[i] = map.get("order_num").toString();
                    localName[i] = map.get("goods_name").toString();
                } else {
                    data[i] = "0";
                    localName[i] = "无";
                }
                xAxis[i] = i + 1 + "";
            }

            ChartSeries chartSeries = new ChartSeries("下单数量", data, localName);

            return new SimpleChart(chartSeries, xAxis, new String[0]);
        } catch (Exception e) {
            logger.error(e);
            throw new StatisticsException(StatisticsErrorCode.E810.code(), StatisticsErrorCode.E810.des());
        }
    }

    /**
     * 热卖商品按数量统计
     *
     * @param searchCriteria 搜索参数
     * @return 按热卖商品数量统计 page
     */
    @Override
    public Page getHotSalesNumPage(SearchCriteria searchCriteria) {
        searchCriteria = new SearchCriteria(searchCriteria);


        long[] timestamp = DataDisplayUtil.getStartTimeAndEndTime(searchCriteria);
        /*
         * 参数
         */
        List<Object> list = new ArrayList<>();

        /*
         * 查询sql
         */
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT count(oi.order_sn) AS order_num,oi.`goods_name` FROM es_sss_order_goods_data oi,es_sss_order_data o WHERE oi.`order_sn` = o.`sn` AND o.`create_time` >= ? AND o.`create_time` <= ? ");

        list.add(timestamp[0]);
        list.add(timestamp[1]);

        /*
         * 商家判定sql
         */
        if (searchCriteria.getSellerId() != 0) {
            sql.append(ChartSqlUtil.orderSellerSql());
            list.add(searchCriteria.getSellerId());
        }
        /*
         * 分类参数判定
         */
        if (searchCriteria.getCategoryId() != 0) {
            sql.append(ChartSqlUtil.categoryOrderGoodsSql());
            list.add(ChartSqlUtil.categoryLikeParams(searchCriteria.getCategoryId()));
        }
        sql.append(" GROUP BY oi.`goods_name` order by order_num desc,goods_name asc");

        return StatisticsUtil.getDataPage(this.daoSupport, searchCriteria.getYear(), sql.toString(), 1, 50, list.toArray());

    }

    /**
     * 商品销售明细
     *
     * @param searchCriteria 搜索参数
     * @param goodsName      商品名称
     * @return 销售page
     */
    @Override
    public Page getSaleDetails(SearchCriteria searchCriteria, String goodsName, Integer pageSize, Integer pageNo) {
        searchCriteria = new SearchCriteria(searchCriteria);

        long[] timestamp = DataDisplayUtil.getStartTimeAndEndTime(searchCriteria);
        /*
         * 参数
         */
        List<Object> list = new ArrayList<>();

        /*
         * 查询sql
         */
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT oi.`goods_name`,COUNT(oi.`order_sn`) AS order_num,sum(oi.goods_num) as num, sum(oi.`price`) as price FROM es_sss_order_goods_data oi LEFT JOIN es_sss_order_data o  ON o.`sn` = oi.`order_sn` WHERE  o.`create_time` >= ? AND o.`create_time` <= ? ");

        list.add(timestamp[0]);
        list.add(timestamp[1]);

        //商品条件
        if (!StringUtil.isEmpty(goodsName)) {
            sql.append(" AND oi.`goods_name` like(?) ");
            list.add('%' + goodsName + '%');
        }

        /*
         * 商家判定sql
         */
        if (searchCriteria.getSellerId() != 0) {
            sql.append(ChartSqlUtil.orderSellerSql());
            list.add(searchCriteria.getSellerId());
        }
        /*
         * 分类参数判定
         */
        if (searchCriteria.getCategoryId() != 0) {
            sql.append(ChartSqlUtil.categoryOrderGoodsSql());
            list.add(ChartSqlUtil.categoryLikeParams(searchCriteria.getCategoryId()));
        }

        sql.append(" GROUP BY oi.goods_id,oi.goods_name ORDER BY order_num,oi.goods_name");
        return StatisticsUtil.getDataPage(this.daoSupport, searchCriteria.getYear(), sql.toString(), pageNo, pageSize, list.toArray());
    }

    /**
     * 商品收藏排行
     *
     * @param searchCriteria 搜索参数
     * @return 排行page
     */
    @Override
    public Page getGoodsCollectPage(SearchCriteria searchCriteria) {

        searchCriteria = new SearchCriteria(searchCriteria);
        /*
         * 参数
         */
        List<Object> list = new ArrayList<>();

        /*
         * 查询sql
         */
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT goods_name,price,g.favorite_num,seller_name from es_sss_goods_data g inner join es_sss_shop_data s on s.seller_id = g.seller_id ");

        List<String> where = new ArrayList<>();
        if (searchCriteria.getSellerId() != 0) {
            where.add(" s.seller_id = ?");
            list.add(searchCriteria.getSellerId());
        }
        //分类
        if (searchCriteria.getCategoryId() != 0) {
            where.add(" g.category_id = ?");
            list.add(searchCriteria.getCategoryId());
        }
        //按年
        searchCriteria.getYear();
        searchCriteria.getMonth();

        String condition = SqlUtil.sqlSplicing(where);
        sql.append(condition);

        sql.append(" ORDER BY favorite_num desc");
        return StatisticsUtil.getDataPage(this.daoSupport, searchCriteria.getYear(), sql.toString(), 1, 50, list.toArray());
    }

    /**
     * 商品收藏排行
     *
     * @param searchCriteria 搜索参数
     * @return SimpleChart   商品收藏chart
     */
    @Override
    public SimpleChart getGoodsCollect(SearchCriteria searchCriteria) {

        searchCriteria = new SearchCriteria(searchCriteria);
        try {
            Page page = this.getGoodsCollectPage(searchCriteria);
            List<Map<String, Object>> dataList = page.getData();

            int dataLength = 50;
            String[] xAxis = new String[dataLength], localName = new String[dataLength], data = new String[dataLength];

            for (int i = 0; i < dataLength; i++) {
                if (null != dataList && i < dataList.size()) {
                    Map map = dataList.get(i);
                    data[i] = map.get("favorite_num").toString();
                    localName[i] = map.get("goods_name").toString();
                } else {
                    data[i] = "0";
                    localName[i] = "无";
                }
                xAxis[i] = i + 1 + "";
            }

            ChartSeries chartSeries = new ChartSeries("商品收藏TOP50", data, localName);

            return new SimpleChart(chartSeries, xAxis, new String[0]);
        } catch (Exception e) {
            logger.error(e);
            throw new StatisticsException(StatisticsErrorCode.E810.code(), StatisticsErrorCode.E810.des());
        }
    }

}
