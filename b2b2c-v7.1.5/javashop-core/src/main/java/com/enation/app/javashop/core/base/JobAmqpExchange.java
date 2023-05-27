package com.enation.app.javashop.core.base;

/**
 * 定时任务AMQP消息定义
 *
 * @author kingapex
 * @version 1.0
 * @since 6.4
 * 2017-08-17 18：00
 */
public class JobAmqpExchange {


    /**
     * 每小时执行
     */
    public final static String EVERY_HOUR_EXECUTE = "EVERY_HOUR_EXECUTE";

    /**
     * 每日执行
     */
    public final static String EVERY_DAY_EXECUTE = "EVERY_DAY_EXECUTE";

    /**
     * 每月执行
     */
    public final static String EVERY_MONTH_EXECUTE = "EVERY_MONTH_EXECUTE";

    /**
     * 每年执行
     */
    public final static String EVERY_YEAR_EXECUTE = "EVERY_YEAR_EXECUTE";




}
