package com.enation.app.javashop.core.statistics.service;


import com.enation.app.javashop.core.base.SearchCriteria;
import com.enation.app.javashop.core.statistics.model.vo.SimpleChart;
import com.enation.app.javashop.framework.database.Page;

/**
 * 后台 行业分析
 *
 * @author chopper
 * @version v1.0
 * @since v7.0
 * 2018/4/16 下午1:53
 */
public interface IndustryStatisticManager {


    /**
     * 按分类统计下单量
     *
     * @param searchCriteria
     * @return
     */
    SimpleChart getOrderQuantity(SearchCriteria searchCriteria);

    /**
     * 按分类统计下单商品数量
     *
     * @param searchCriteria
     * @return
     */
    SimpleChart getGoodsNum(SearchCriteria searchCriteria);

    /**
     * 按分类统计下单金额
     *
     * @param searchCriteria
     * @return
     */
    SimpleChart getOrderMoney(SearchCriteria searchCriteria);

    /**
     * 概括总览
     *
     * @param searchCriteria
     * @return
     */
    Page getGeneralOverview(SearchCriteria searchCriteria);

}
