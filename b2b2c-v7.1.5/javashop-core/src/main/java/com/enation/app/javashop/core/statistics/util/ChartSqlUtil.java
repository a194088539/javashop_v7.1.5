package com.enation.app.javashop.core.statistics.util;

import com.enation.app.javashop.core.base.SearchCriteria;
import com.enation.app.javashop.framework.util.StringUtil;

import java.util.List;

/**
 * chart sql工具
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-04-27 下午3:14
 */
public class ChartSqlUtil {


    /**
     * 价格参数sql 获取
     *
     * @param prices 价格参数
     */
    public static void appendPriceSql(Integer[] prices, StringBuffer stringBuffer, List<Object> params) {
        for (int i = 1; i < prices.length; i++) {
            stringBuffer.append(" when o.order_price >= ? AND o.order_price <=? then ?");
            params.add(prices[i - 1]);
            params.add(prices[i]);
            params.add(i);
        }
        stringBuffer.append(" else 0 end").toString();
    }

    /**
     * 价格参数sql group获取
     *
     * @param prices 价格参数
     * @return sql
     */
    public static String priceGroupSql(Integer[] prices) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(" group by case ");
        for (int i = 1; i < prices.length; i++) {
            stringBuffer.append(" when o.order_price >= ? AND  o.order_price <=? then ?");
        }
        return stringBuffer.append(" else 0 end").toString();
    }


    /**
     * 订单商品 分类参数sql获取
     *
     * @return
     */
    public static String categoryOrderGoodsSql() {
        return " AND oi.category_path like (?)";
    }


    /**
     * 订单商品 分类参数sql拼接
     *
     * @param searchCriteria 搜索参数
     * @param stringBuffer   sql
     * @param params         sql参数
     */
    public static void appendOrderGoodsCategorySql(SearchCriteria searchCriteria, StringBuffer stringBuffer, List<Object> params) {
        if (searchCriteria.getCategoryId() != 0) {
            stringBuffer.append(" AND oi.category_path like (?)");
            params.add(searchCriteria.getCategoryId());
        }
    }


    /**
     * 订单商家条件 sql拼接
     *
     * @param searchCriteria 搜索参数
     * @param stringBuffer   sql
     * @param params         sql参数
     */
    public static void appendOrderSellerSql(SearchCriteria searchCriteria, StringBuffer stringBuffer, List<Object> params) {
        if (searchCriteria.getSellerId() != 0) {
            stringBuffer.append(" AND o.seller_id = ?");
            params.add(searchCriteria.getSellerId());
        }
    }

    /**
     * 订单状态条件 sql拼接
     *
     * @param orderStatus  订单状态
     * @param stringBuffer sql
     * @param params       sql参数
     */
    public static void appendOrderStatusSql(String orderStatus, StringBuffer stringBuffer, List<Object> params) {
        if (!StringUtil.isEmpty(orderStatus)) {
            stringBuffer.append(" AND o.order_status = ? ");
            params.add(orderStatus);
        }
    }

    /**
     * 订单 商家参数sql获取
     *
     * @return
     */
    public static String orderSellerSql() {
        return " AND o.seller_id = ? ";
    }

    /**
     * like 参数
     *
     * @param like 参数
     * @return
     */
    public static String categoryLikeParams(Integer like) {
        return "%|" + like + "|%";
    }


}
