package com.enation.app.javashop.buyer.api.trade;

import com.enation.app.javashop.core.trade.cart.model.enums.CheckedWay;
import com.enation.app.javashop.core.trade.order.model.vo.TradeVO;
import com.enation.app.javashop.core.trade.order.service.TradeManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 交易控制器
 *
 * @author Snow create in 2018/5/8
 * @version v2.0
 * @since v7.0.0
 */
@Api(description = "交易接口模块")
@RestController
@RequestMapping("/trade")
@Validated
public class TradeBuyerController {

    @Autowired
    @Qualifier("tradeManagerImpl")
    private TradeManager tradeManager;


    @ApiOperation(value = "创建交易")
    @PostMapping(value = "/create")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "client", value = "客户端类型", required = false, dataType = "String", paramType = "query",allowableValues = "PC,WAP,NATIVE,REACT,MINI"),
            @ApiImplicitParam(name = "way", value = "检查获取方式，购物车还是立即购买", required = true, dataType = "String", paramType = "query",allowableValues = "BUY_NOW,CART"),
    })
    public TradeVO create(String client, String way) {
        return this.tradeManager.createTrade(client, CheckedWay.valueOf(way));
    }



}
