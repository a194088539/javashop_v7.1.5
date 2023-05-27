package com.enation.app.javashop.manager.api.statistics;

import com.enation.app.javashop.core.base.SearchCriteria;
import com.enation.app.javashop.core.statistics.model.vo.SimpleChart;
import com.enation.app.javashop.core.statistics.service.PageViewStatisticManager;
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
 * 平台后台 流量分析
 *
 * @author mengyuanming
 * @version 2.0
 * @since 7.0
 * 2018年3月19日上午8:35:47
 */

@Api(description = "平台统计 流量分析")
@RestController
@RequestMapping("/admin/statistics/page_view")
public class PageViewStatisticManagerController {

    @Autowired
    private PageViewStatisticManager pageViewStatisticManager;

    @ApiOperation(value = "获取店铺访问量数据", response = SimpleChart.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cycle_type", value = "日期类型", dataType = "String", paramType = "query", required = true, allowableValues = "YEAR,MONTH"),
            @ApiImplicitParam(name = "year", value = "年份，空则查询当前年份", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "month", value = "月份，空则查询当前月份", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "seller_id", value = "店铺id，为空则返回所有店铺数据", dataType = "int", paramType = "query")})
    @GetMapping("/shop")
    public SimpleChart getShop(@Valid @ApiIgnore SearchCriteria searchCriteria) {
        return this.pageViewStatisticManager.countShop(searchCriteria);
    }

    @ApiOperation(value = "获取商品访问量数据，只取前30", response = SimpleChart.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cycle_type", value = "日期类型", dataType = "String", paramType = "query", required = true, allowableValues = "YEAR,MONTH"),
            @ApiImplicitParam(name = "year", value = "年份，空则查询当前年份", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "month", value = "月份，空则查询当前月份", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "seller_id", value = "店铺id，为空则返回所有店铺数据", dataType = "int", paramType = "query")})
    @GetMapping("/goods")
    public SimpleChart getGoods(@Valid @ApiIgnore SearchCriteria searchCriteria) {
        return this.pageViewStatisticManager.countGoods(searchCriteria);
    }

}
