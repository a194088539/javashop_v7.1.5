package com.enation.app.javashop.manager.api.statistics;

import com.enation.app.javashop.core.base.SearchCriteria;
import com.enation.app.javashop.core.statistics.model.vo.SimpleChart;
import com.enation.app.javashop.core.statistics.service.GoodsStatisticManager;
import com.enation.app.javashop.framework.database.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 商品统计控制器，后台
 *
 * @author chopper
 * @version v1.0
 * @since v7.0
 * 2018-03-23 上午5:19
 */

@RestController
@Api(description = "后台-》商品统计")
@RequestMapping("/admin/statistics/goods")
public class GoodsStatisticManagerController {

    @Autowired
    protected GoodsStatisticManager goodsStatisticsService;


    @ApiOperation("价格销量统计")
    @GetMapping(value = "/price/sales")
    @ApiImplicitParam(name = "prices", value = "价格区间", required = false, paramType = "int", dataType = "double", allowMultiple = true)
    public SimpleChart getPriceSales(SearchCriteria searchCriteria,@RequestParam(required = false) Integer[] prices) {
        return this.goodsStatisticsService.getPriceSales(searchCriteria, prices);
    }

    @ApiOperation("热卖商品按金额统计")
    @GetMapping(value = "/hot/money")
    public SimpleChart getHotSalesMoney(SearchCriteria searchCriteria) {
        return this.goodsStatisticsService.getHotSalesMoney(searchCriteria);
    }

    @ApiOperation("热卖商品按金额统计")
    @GetMapping(value = "/hot/money/page")
    public Page getHotSalesMoneyPage(SearchCriteria searchCriteria) {
        return this.goodsStatisticsService.getHotSalesMoneyPage(searchCriteria);
    }

    @ApiOperation("热卖商品按数量统计")
    @GetMapping(value = "/hot/num")
    public SimpleChart getHotSalesNum(SearchCriteria searchCriteria) {
        return this.goodsStatisticsService.getHotSalesNum(searchCriteria);
    }


    @ApiOperation("热卖商品按数量统计")
    @GetMapping(value = "/hot/num/page")
    public Page getHotSalesNumPage(SearchCriteria searchCriteria) {
        return this.goodsStatisticsService.getHotSalesNumPage(searchCriteria);
    }

    @ApiOperation("商品销售明细")
    @GetMapping(value = "/sale/details")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goods_name", value = "商品名字", required = false, paramType = "query", dataType = "String", allowMultiple = false),
            @ApiImplicitParam(name = "page_size", value = "页码大小", required = false, paramType = "query", dataType = "int", allowMultiple = false),
            @ApiImplicitParam(name = "page_no", value = "页码", required = false, paramType = "query", dataType = "int", allowMultiple = false),
    })
    public Page getSaleDetails(SearchCriteria searchCriteria, @ApiIgnore String goodsName, @ApiIgnore Integer pageSize, @ApiIgnore Integer pageNo) {
        return this.goodsStatisticsService.getSaleDetails(searchCriteria, goodsName, pageSize, pageNo);
    }

    @ApiOperation("商品收藏排行")
    @GetMapping(value = "/collect")
    public SimpleChart getGoodsCollect(SearchCriteria searchCriteria) {
        return this.goodsStatisticsService.getGoodsCollect(searchCriteria);
    }

    @ApiOperation("商品收藏排行")
    @GetMapping(value = "/collect/page")
    public Page getGoodsCollectPage(SearchCriteria searchCriteria) {
        return this.goodsStatisticsService.getGoodsCollectPage(searchCriteria);
    }

}
