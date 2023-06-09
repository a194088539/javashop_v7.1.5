package com.enation.app.javashop.core.statistics.util;


import com.enation.app.javashop.core.base.SearchCriteria;
import com.enation.app.javashop.core.statistics.model.enums.QueryDateType;

/**
 * 统计日期工具
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018/4/28 下午5:09
 */
public class DataDisplayUtil {

    private final static String DATA_SEPARATOR = "-";

    /**
     * 获取月份的天数
     *
     * @param searchCriteria 参数类
     * @return 天数
     */
    public static int getResultSize(SearchCriteria searchCriteria) {

        if (searchCriteria.getCycleType().equals(QueryDateType.YEAR.value())) {
            return 12;
        }

        return getMonthDayNum(searchCriteria.getMonth(), searchCriteria.getYear());

    }


    /**
     * 获取月份的天数
     *
     * @param cycleType 日期类型
     * @param year      年份
     * @param month     月份
     * @return 天数
     */
    public static int getResultSize(String cycleType, Integer year, Integer month) {

        if (cycleType.equals(QueryDateType.YEAR.value())) {
            return 12;
        }

        return getMonthDayNum(month, year);

    }

    /**
     * 获取月份的天数
     *
     * @param month 月份
     * @param year  年
     * @return 天数
     */
    public static int getMonthDayNum(Integer month, Integer year) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                boolean flag = year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
                if (flag) {
                    return 29;
                } else {
                    return 28;
                }
            default:
                return 0;
        }
    }

    /**
     * 根据vo获取当前开始和结束条件时间戳
     *
     * @param searchCriteria 时间参数
     * @return 时间戳
     */
    public static long[] getStartTimeAndEndTime(SearchCriteria searchCriteria) {
        long[] timestamp = new long[2];
        String startTime;
        String endTime;
        if (searchCriteria.getCycleType().equals(QueryDateType.YEAR.name())) {
            startTime = new StringBuffer().append(searchCriteria.getYear()).append(DataDisplayUtil.DATA_SEPARATOR).append("01").append(DataDisplayUtil.DATA_SEPARATOR).append("01 ").append("00:00:00").toString();
            endTime = new StringBuffer().append(searchCriteria.getYear()).append(DataDisplayUtil.DATA_SEPARATOR).append("12").append(DataDisplayUtil.DATA_SEPARATOR).append("31 ").append("23:59:59").toString();
        } else {
            startTime = new StringBuffer().append(searchCriteria.getYear()).append(DataDisplayUtil.DATA_SEPARATOR).append(searchCriteria.getMonth()).append(DataDisplayUtil.DATA_SEPARATOR).append("01").append(" 00:00:00").toString();
            endTime = new StringBuffer().append(searchCriteria.getYear()).append(DataDisplayUtil.DATA_SEPARATOR).append(searchCriteria.getMonth() + 1).append(DataDisplayUtil.DATA_SEPARATOR).append("01").append(" 00:00:00").toString();
        }
        timestamp[0] = DateUtil.getDateline(startTime, "yyyy-MM-dd HH:mm:ss");
        timestamp[1] = DateUtil.getDateline(endTime, "yyyy-MM-dd HH:mm:ss");
        return timestamp;
    }

    /**
     * 根据vo获取上一周期开始和结束条件时间戳
     *
     * @param searchCriteria 时间参数
     * @return 时间戳
     */
    public static long[] getLastStartTimeAndEndTime(SearchCriteria searchCriteria) {
        long[] lastTimestamp = new long[2];
        String startTime;
        String endTime;
        if (searchCriteria.getCycleType().equals(QueryDateType.YEAR.name())) {
            startTime = new StringBuffer().append(searchCriteria.getYear() - 1).append(DataDisplayUtil.DATA_SEPARATOR).append("01").append(DataDisplayUtil.DATA_SEPARATOR).append("01 ").append("00:00:00").toString();
            endTime = new StringBuffer().append(searchCriteria.getYear() - 1).append(DataDisplayUtil.DATA_SEPARATOR).append("12").append(DataDisplayUtil.DATA_SEPARATOR).append("31 ").append("23:59:59").toString();
        } else {
            startTime = new StringBuffer().append(searchCriteria.getYear()).append(DataDisplayUtil.DATA_SEPARATOR).append(searchCriteria.getMonth() - 1).append(DataDisplayUtil.DATA_SEPARATOR).append("01").append(" 00:00:00").toString();
            endTime = new StringBuffer().append(searchCriteria.getYear()).append(DataDisplayUtil.DATA_SEPARATOR).append(searchCriteria.getMonth()).append(DataDisplayUtil.DATA_SEPARATOR).append("01").append(" 00:00:00").toString();
        }
        lastTimestamp[0] = DateUtil.getDateline(startTime, "yyyy-MM-dd HH:mm:ss");
        lastTimestamp[1] = DateUtil.getDateline(endTime, "yyyy-MM-dd HH:mm:ss");
        return lastTimestamp;

    }

    /**
     * 格式日期
     *
     * @param date 日期
     * @return 格式化日期
     */
    public static String formatDate(Integer date) {
        // 个位数前加0
        int isSingle = 10;
        if (date < isSingle) {
            return "0" + date;
        }
        return date + "";
    }
}
