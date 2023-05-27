package com.enation.app.javashop.seller.api.statistics;

import com.enation.app.javashop.core.base.SearchCriteria;
import com.enation.app.javashop.core.statistics.model.vo.SimpleChart;
import com.enation.app.javashop.core.statistics.service.PageViewStatisticManager;
import com.enation.app.javashop.framework.context.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

/**
 * 商家中心 流量分析
 *
 * @author mengyuanming
 * @version 2.0
 * @since 7.0
 * 2018年3月19日上午8:35:47
 */

@Api(description = "商家统计 流量分析")
@RestController
@RequestMapping("/seller/statistics/page_view")
public class PageViewStatisticSellerController {

    @Autowired
    private PageViewStatisticManager pageViewStatisticManager;

    @ApiOperation(value = "获取店铺访问量数据", response = SimpleChart.class)
    @GetMapping("/shop")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cycle_type", value = "周期类型", dataType = "String", paramType = "query",required = true,allowableValues = "YEAR,MONTH"),
            @ApiImplicitParam(name = "year", value = "年份，默认当前年份", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "month", value = "月份，默认当前月份", dataType = "int", paramType = "query")})
    public SimpleChart getShop(@Valid @ApiIgnore SearchCriteria searchCriteria) {

        // 获取商家id
        searchCriteria.setSellerId(UserContext.getSeller().getSellerId());
        return this.pageViewStatisticManager.countShop(searchCriteria);
    }

    @ApiOperation(value = "获取商品访问量数据，只取前30", response = SimpleChart.class)
    @GetMapping("/goods")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cycle_type", value = "周期类型", dataType = "String", paramType = "query",required = true,allowableValues = "YEAR,MONTH"),
            @ApiImplicitParam(name = "year", value = "年份，默认当前年份", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "month", value = "月份，默认当前月份", dataType = "int", paramType = "query")})
    public SimpleChart getGoods(@Valid @ApiIgnore SearchCriteria searchCriteria) {

        // 获取商家id
        searchCriteria.setSellerId(UserContext.getSeller().getSellerId());

        return this.pageViewStatisticManager.countGoods(searchCriteria);
    }

}
