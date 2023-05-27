package com.enation.app.javashop.manager.api.statistics;

import com.enation.app.javashop.core.base.SearchCriteria;
import com.enation.app.javashop.core.statistics.model.vo.SimpleChart;
import com.enation.app.javashop.core.statistics.service.IndustryStatisticManager;
import com.enation.app.javashop.framework.database.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 行业分析
 *
 * @author chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/8 下午6:47
 */

@RestController
@Api(description = "统计 行业分析")
@RequestMapping("/admin/statistics/industry")
public class IndustryStatisticManagerController {

    @Autowired
    private IndustryStatisticManager industryAnalysisManager;

    @ApiOperation("按分类统计下单量")
    @GetMapping(value = "/order/quantity")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cycle_type", value = "日期类型", dataType = "String", paramType = "query", required = true, allowableValues = "YEAR,MONTH"),
            @ApiImplicitParam(name = "year", value = "年份", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "month", value = "月份", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "seller_id",value = "店铺id",dataType = "int",paramType = "query")
    })
    public SimpleChart getOrderQuantity(@ApiIgnore SearchCriteria searchCriteria) {
        return this.industryAnalysisManager.getOrderQuantity(searchCriteria);
    }

    @ApiOperation("按分类统计下单商品数量")
    @GetMapping(value = "/goods/num")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cycle_type", value = "日期类型", dataType = "String", paramType = "query", required = true, allowableValues = "YEAR,MONTH"),
            @ApiImplicitParam(name = "year", value = "年份", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "month", value = "月份", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "seller_id",value = "店铺id",dataType = "int",paramType = "query")
    })
    public SimpleChart getGoodsNum(@ApiIgnore SearchCriteria searchCriteria) {
        return this.industryAnalysisManager.getGoodsNum(searchCriteria);
    }

    @ApiOperation("按分类统计下单金额")
    @GetMapping(value = "/order/money")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cycle_type", value = "日期类型", dataType = "String", paramType = "query", required = true, allowableValues = "YEAR,MONTH"),
            @ApiImplicitParam(name = "year", value = "年份", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "month", value = "月份", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "seller_id",value = "店铺id",dataType = "int",paramType = "query")
    })
    public SimpleChart getOrderMoney(@ApiIgnore SearchCriteria searchCriteria) {
        return this.industryAnalysisManager.getOrderMoney(searchCriteria);
    }

    @ApiOperation("概括总览")
    @GetMapping(value = "/overview")
    public Page getGeneralOverview(SearchCriteria searchCriteria) {
        return this.industryAnalysisManager.getGeneralOverview(searchCriteria);
    }
}
