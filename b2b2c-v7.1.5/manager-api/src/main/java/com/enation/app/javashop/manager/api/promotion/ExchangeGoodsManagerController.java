package com.enation.app.javashop.manager.api.promotion;

import com.enation.app.javashop.core.promotion.exchange.model.dos.ExchangeCat;
import com.enation.app.javashop.core.promotion.exchange.model.dto.ExchangeQueryParam;
import com.enation.app.javashop.core.promotion.exchange.service.ExchangeGoodsManager;
import com.enation.app.javashop.framework.database.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 积分商品控制器
 * @author Snow create in 2018/5/29
 * @version v2.0
 * @since v7.0.0
 */
@RestController
@RequestMapping("/admin/promotion/exchange-goods")
@Api(description = "积分商品相关API")
@Validated
public class ExchangeGoodsManagerController {

    @Autowired
    private ExchangeGoodsManager exchangeGoodsManager;


    @ApiOperation(value	= "查询积分商品列表", response = ExchangeCat.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name	= "goods_name", value = "商品名称", dataType = "String",	paramType =	"query"),
            @ApiImplicitParam(name	= "goods_sn", value = "商品编号", dataType = "String",	paramType =	"query"),
            @ApiImplicitParam(name	= "seller_name", value = "店铺名称", dataType = "String",	paramType =	"query"),
            @ApiImplicitParam(name	= "cat_id", value = "积分分类ID", dataType = "int",	paramType =	"query"),
            @ApiImplicitParam(name	= "page_no", value = "取消原因", dataType = "int",	paramType =	"query"),
            @ApiImplicitParam(name	= "page_size", value = "取消原因", dataType = "int",	paramType =	"query")

    })
    @GetMapping
    public Page list(@ApiIgnore  String goodsName,@ApiIgnore String goodsSn,@ApiIgnore String sellerName,
                     @ApiIgnore Integer catId,@ApiIgnore Integer pageNo,@ApiIgnore Integer pageSize)	{

        ExchangeQueryParam param = new ExchangeQueryParam();
        param.setName(goodsName);
        param.setSn(goodsSn);
        param.setSellerName(sellerName);
        param.setCatId(catId);
        param.setPageNo(pageNo);
        param.setPageSize(pageSize);

        return	this.exchangeGoodsManager.list(param);
    }

}
