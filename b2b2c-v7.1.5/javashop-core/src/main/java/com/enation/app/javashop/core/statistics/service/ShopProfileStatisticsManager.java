package com.enation.app.javashop.core.statistics.service;

import com.enation.app.javashop.core.statistics.model.vo.ShopProfileVO;
import com.enation.app.javashop.core.statistics.model.vo.SimpleChart;

/**
 * 商家中心，店铺概况
 *
 * @author mengyuanming
 * @version 2.0
 * @since 7.0
 * 2018/5/11 19:58
 */
public interface ShopProfileStatisticsManager {

    /**
     * 店铺近30天概况
     *
     * @return ShopProfileVO 店铺概况数据
     */
    ShopProfileVO data();

    /**
     * 店铺近30天销售额
     *
     * @return SimpleChart 简单图表数据
     */
    SimpleChart chart();

}
