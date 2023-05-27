package com.enation.app.javashop.core.statistics.service.impl;

import com.enation.app.javashop.core.statistics.StatisticsErrorCode;
import com.enation.app.javashop.core.statistics.StatisticsException;
import com.enation.app.javashop.core.statistics.model.vo.ChartSeries;
import com.enation.app.javashop.core.statistics.model.vo.ShopProfileVO;
import com.enation.app.javashop.core.statistics.model.vo.SimpleChart;
import com.enation.app.javashop.core.statistics.service.ShopProfileStatisticsManager;
import com.enation.app.javashop.core.trade.order.model.enums.OrderStatusEnum;
import com.enation.app.javashop.core.trade.order.model.enums.PayStatusEnum;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.util.CurrencyUtil;
import com.enation.app.javashop.framework.util.DateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * 店铺概况管理实现类
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018/3/28 上午9:50
 */
@Service
public class ShopProfileStatisticsManagerImpl implements ShopProfileStatisticsManager {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    @Qualifier("sssDaoSupport")
    private DaoSupport daoSupport;

    /**
     * 获取店铺概况
     *
     * @return ShopProfileVO 店铺概况展示VO
     */
    @Override
    public ShopProfileVO data() {
        Integer sellerId = UserContext.getSeller().getSellerId();
        try {

            // 近30天起始时间
            long startTime = startOfSomeDay(30);
            long endTime = DateUtil.endOfTodDay();

            List<Object> paramList = new ArrayList();

            paramList.add(startTime);
            paramList.add(endTime);
            paramList.add(OrderStatusEnum.COMPLETE.value());
            paramList.add(PayStatusEnum.PAY_YES.value());
            paramList.add(sellerId);

            // 获取下单金额，下单会员数，下单量，下单商品数
            String sql = "SELECT SUM(o.order_price) AS order_money ,COUNT(DISTINCT o.buyer_id) AS order_member" +
                    ", COUNT(o.sn) AS order_num,SUM(o.goods_num) AS order_good FROM es_sss_order_data o" +
                    " WHERE o.create_time >= ? AND o.create_time <= ? AND o.order_status = ? AND o.pay_status= ? AND o.seller_id = ?";

            Map<String, Object> map = this.daoSupport.queryForMap(sql, paramList.toArray());

            ShopProfileVO shopProfileVO = new ShopProfileVO();
            // 下单金额
            String orderMoney = null == map.get("order_money") ? "0.0" : map.get("order_money").toString();
            shopProfileVO.setOrderMoney(orderMoney);
            // 下单会员数
            String orderMember = null == map.get("order_member") ? "0" : map.get("order_member").toString();
            shopProfileVO.setOrderMember(orderMember);
            // 下单量
            String orderNum = null == map.get("order_num") ? "0" : map.get("order_num").toString();
            shopProfileVO.setOrderNum(orderNum);
            // 下单商品数
            String orderGoods = null == map.get("order_good") ? "0" : map.get("order_good").toString();
            shopProfileVO.setOrderGoods(orderGoods);
            // 平均客单价
            Double averageMemberMoney = 0.0;
            if (!"0".equals(orderMember)) {
                double orderMoneyNum = new Double(orderMoney);
                double orderMemberNum = new Double(orderMember);
                averageMemberMoney = CurrencyUtil.div(orderMoneyNum, orderMemberNum, 2);
            }
            shopProfileVO.setAverageMemberMoney(averageMemberMoney.toString());
            // 商品平均价格
            Double averageGoodsMoney = 0.0;
            if (!"0".equals(orderGoods)) {
                double orderMoneyNum = new Double(orderMoney);
                double orderGoodsNum = new Double(orderGoods);
                averageGoodsMoney = CurrencyUtil.div(orderMoneyNum, orderGoodsNum, 2);
            }
            shopProfileVO.setAverageGoodsMoney(averageGoodsMoney.toString());

            sql = "SELECT COUNT(DISTINCT g.goods_id) AS total_goods, SUM(g.favorite_num) AS goods_collect " +
                    " FROM es_sss_goods_data g WHERE g.seller_id = ? AND g.market_enable = 1";

            map = this.daoSupport.queryForMap(sql, sellerId);

            // 店铺商品总数
            String totalGoods = null == map.get("total_goods") ? "0" : map.get("total_goods").toString();
            shopProfileVO.setTotalGoods(totalGoods);
            // 商品收藏总数
            String goodsCollect = null == map.get("goods_collect") ? "0" : map.get("goods_collect").toString();
            shopProfileVO.setGoodsCollect(goodsCollect);

            sql = "SELECT IFNULL(s.favorite_num,'0') AS shop_collect FROM es_sss_shop_data s WHERE s.seller_id = ? ";

            List<Map<String, Object>> list = this.daoSupport.queryForList(sql, sellerId);

            // 店铺收藏数
            String shopCollect = "0";
            if (null != list && list.size() > 0) {
                map = list.get(0);
                shopCollect = map.get("shop_collect").toString();
            }

            shopProfileVO.setShopCollect(shopCollect);

            StringBuilder conditionSql = this.getConditionSql();


            ZonedDateTime nowDate = getNowZoneDateTime();

            long startDate = nowDate.plusDays(-29).toEpochSecond();
            long endDate = nowDate.plusHours(23).plusMinutes(59).plusSeconds(59).toEpochSecond();

            sql = "select count(1) FROM es_sss_order_data o WHERE o.order_status = ? AND o.pay_status = ? AND o.seller_id = ? AND o.create_time BETWEEN ? and ?  ";

            int count = this.daoSupport.queryForInt(sql, OrderStatusEnum.COMPLETE.value(), PayStatusEnum.PAY_YES.value(), sellerId, startDate, endDate);

            if (count > 0) {
                sql = "SELECT s.num,s.time FROM (SELECT COUNT(o.sn) AS num,(CASE " + conditionSql.toString() + ") AS time " +
                        " FROM es_sss_order_data o WHERE o.order_status = ? AND o.pay_status = ? AND o.seller_id = ? AND o.create_time BETWEEN ? and ?  GROUP BY time) s order by s.num DESC LIMIT 1";

                map = this.daoSupport.queryForMap(sql, OrderStatusEnum.COMPLETE.value(), PayStatusEnum.PAY_YES.value(), sellerId, startDate, endDate);

            }
            String orderFastigium = "暂无";
            String time = null == map.get("time") ? "0" : map.get("time").toString();

            if (null != time && !"0".equals(time)) {
                orderFastigium = time;
            }

            shopProfileVO.setOrderFastigium(orderFastigium);

            return shopProfileVO;

        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new StatisticsException(StatisticsErrorCode.E810.code(), "业务异常");
        }
    }

    /**
     * 店铺概况，获取近30天下单金额
     *
     * @return SimpleChart 简单图表数据
     */
    @Override
    public SimpleChart chart() {
        Integer sellerId = UserContext.getSeller().getSellerId();

        //查询map集合
        try {

            List<Object> paramList = new ArrayList();
            paramList.add(OrderStatusEnum.COMPLETE.value());
            paramList.add(PayStatusEnum.PAY_YES.value());
            paramList.add(sellerId);
            // 数据名称，与x轴刻度名相同
            String[] localName = new String[30];

            int limitDays = 30;
            for (int i = 0; i < limitDays; i++) {
                Map<String, Object> map = DateUtil.getYearMonthAndDay(i);
                String month = map.get("month").toString();
                String day = map.get("day").toString();
                localName[i] = month + "-" + day;
            }

            StringBuilder conditionSql = this.getConditionSql();

            String sql = "SELECT SUM(o.order_price) AS money, CASE " + conditionSql.toString() + " AS time " +
                    " FROM es_sss_order_data o WHERE o.order_status = ? AND o.pay_status = ? AND o.seller_id = ? GROUP BY time";
            List<Map<String, Object>> list = this.daoSupport.queryForList(sql, paramList.toArray());

            String[] data = new String[30];

            // 循环xAxis，如果与time相同，则将money放入data数组，无数据的数组元素填入0
            for (int i = 0; i < limitDays; i++) {
                for (Map map : list) {
                    if (localName[i].equals(map.get("time").toString())) {
                        data[i] = map.get("money").toString();
                    }
                }
                if (null == data[i]) {
                    data[i] = 0 + "";
                }
            }

            ChartSeries chartSeries = new ChartSeries("下单金额", data, localName);

            return new SimpleChart(chartSeries, localName, new String[0]);
        } catch (Exception e) {
            logger.error(e);
            throw new StatisticsException(StatisticsErrorCode.E810.code(), "业务异常");
        }
    }

    private StringBuilder getConditionSql() {

        // 时间分组，同时获取数据名称
        StringBuilder conditionSql = new StringBuilder();
        // 取近30天数据
        int limitDays = 30;
        for (int i = 0; i < limitDays; i++) {
            Map<String, Object> map = DateUtil.getYearMonthAndDay(i);
            String year = map.get("year").toString();
            String month = map.get("month").toString();
            String day = map.get("day").toString();
            String dayDate = year + "-" + month + "-" + day;
            long start = DateUtil.getDateline(dayDate + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
            long end = DateUtil.getDateline(dayDate + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
            conditionSql.append(" when create_time >= ").append(start).append(" and   create_time <=").append(end).append(" then '").append(month).append("-").append(day).append("'");
        }
        conditionSql.append(" else '0' end");

        return conditionSql;
    }

    /**
     * 某天的开始时间
     *
     * @param dayUntilNow 距今多少天以前
     * @return 时间戳
     */
    private static long startOfSomeDay(int dayUntilNow) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, -dayUntilNow);
        Date date = calendar.getTime();
        return date.getTime() / 1000;
    }

    /**
     * 获取当前时区的时间
     *
     * @return
     */
    public static ZonedDateTime getNowZoneDateTime() {
        Instant now = Instant.now();
        ZoneId zoneId = ZoneId.of("Asia/Chongqing");
        ZonedDateTime zdt = ZonedDateTime.ofInstant(now, zoneId);
        return zdt.toLocalDate().atStartOfDay(zoneId);
    }
}
