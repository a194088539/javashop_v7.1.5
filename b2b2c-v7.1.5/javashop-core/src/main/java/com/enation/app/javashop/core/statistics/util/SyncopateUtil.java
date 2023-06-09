package com.enation.app.javashop.core.statistics.util;

import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.util.StringUtil;

import java.time.LocalDate;
import java.util.Calendar;

/**
 * 年份查询sql工具
 *
 * @author chopper
 * @version v1.0
 * @since v7.0
 * 2018-05-02 下午8:23
 */
public class SyncopateUtil {

    /**
     * 需要替换年份的 表名称
     */
    private static String[] table = {
            "es_sss_order_data",
            "es_sss_order_goods_data",
            "es_sss_refund_data",
            "es_sss_shop_pv",
            "es_sss_goods_pv"
    };


    /**
     * sql 处理
     *
     * @param year 搜索年份
     * @param sql  sql
     * @return
     */
    public static String handleSql(Integer year, String sql) {

        if (StringUtil.isEmpty(sql) || year == null) {
            return "";
        }
        sql = sql.toLowerCase();
        //sql处理
        sql = replaceTable(sql, year);
        return sql;
    }


    /**
     * 替换表。
     *
     * @param sql  查询语句
     * @param year 年
     * @return
     */
    private static String replaceTable(String sql, Integer year) {
        for (int i = 0; i < table.length; i++) {
            sql = sql.replaceAll(table[i], table[i] + "_" + year);
        }
        return sql;
    }


    /**
     * 创建对应年份的表
     *
     * @param year
     * @param daoSupport
     */
    public static void createTable(Integer year, DaoSupport daoSupport) {
        for (String tb : table) {
            daoSupport.execute("create table " + tb + "_" + year + " select *from " + tb + " where 1=0");
        }
    }

    /**
     * 切分表
     *
     * @param year
     * @param daoSupport
     */
    public static void syncopateTable(Integer year, DaoSupport daoSupport) {
        Long[] time = getYearTime(year);
        drop(year, daoSupport);
        for (String tb : table) {
            if ("es_sss_shop_pv".equals(tb) || "es_sss_goods_pv".equals(tb)) {
                daoSupport.execute("create table " + tb + "_" + year + " like " + tb);
                daoSupport.execute("insert into " + tb + "_" + year + " select * from " + tb + " where vs_year = ?", year);
            } else {
                daoSupport.execute("create table " + tb + "_" + year + " like " + tb);
                daoSupport.execute("insert into " + tb + "_" + year + " select * from " + tb + " where create_time >=? and create_time <?", time[0], time[1]);
            }
        }
    }

    /**
     * 数据全局初始化
     *
     * @param daoSupport
     */
    public static void init(DaoSupport daoSupport) {
        for (int i = 2015; i < LocalDate.now().getYear(); i++) {
            drop(i, daoSupport);
            Long[] year = getYearTime(i);
//          如果有数据，进行初始化
            int count = daoSupport.queryForInt("select count(0) from es_sss_order_data where create_time > ? and  create_time <  ?", year[0], year[1]);
            if (count > 0) {
                SyncopateUtil.syncopateTable(i, daoSupport);
            }
        }
    }

    /**
     * 创建当前
     *
     * @param daoSupport
     */
    public static void createCurrentTable(DaoSupport daoSupport) {
        Integer year = LocalDate.now().getYear();
        syncopateTable(year, daoSupport);
    }

    /**
     * 删除表
     *
     * @param year
     * @param daoSupport
     */
    private static void drop(Integer year, DaoSupport daoSupport) {

        for (String tb : table) {
            daoSupport.execute("DROP TABLE IF EXISTS " + tb + "_" + year);
        }

    }

    /**
     * 获取某年开始结束时间
     *
     * @return
     */
    private static Long[] getYearTime(Integer year) {
        Calendar firstCal = Calendar.getInstance();
        firstCal.set(Calendar.YEAR, year - 1);
        firstCal.set(Calendar.MONTH, Calendar.DECEMBER);
        firstCal.set(Calendar.DATE, 31);

        Calendar lastCal = Calendar.getInstance();
        lastCal.set(Calendar.YEAR, year);
        lastCal.set(Calendar.MONTH, Calendar.DECEMBER);
        lastCal.set(Calendar.DATE, 31);

        Long[] yearTime = new Long[2];
        yearTime[0] = firstCal.getTime().getTime() / 1000;
        yearTime[1] = lastCal.getTime().getTime() / 1000;

        return yearTime;
    }

}
