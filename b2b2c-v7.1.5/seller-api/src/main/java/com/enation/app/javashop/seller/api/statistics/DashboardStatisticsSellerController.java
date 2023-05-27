package com.enation.app.javashop.seller.api.statistics;

import com.enation.app.javashop.core.statistics.model.vo.ShopDashboardVO;
import com.enation.app.javashop.core.statistics.service.DashboardStatisticManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商家中心，首页数据
 *
 * @author mengyuanming
 * @version 2.0
 * @since 7.0
 * 2018/6/25 15:13
 */
@RestController
@RequestMapping("/seller/statistics/dashboard")
@Api(description = "商家中心，首页数据")
public class DashboardStatisticsSellerController {

    @Autowired
    private DashboardStatisticManager dashboardStatisticManager;

    @ApiOperation(value = "首页数据",response = ShopDashboardVO.class)
    @GetMapping("/shop")
    public ShopDashboardVO shop() {
        return this.dashboardStatisticManager.getShopData();
    }

}
