package com.enation.app.javashop.buyer.api.promotion;

import com.enation.app.javashop.core.promotion.coupon.model.dos.CouponDO;
import com.enation.app.javashop.core.promotion.coupon.service.CouponManager;
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

import java.util.List;

/**
 * 优惠券相关API
 * @author Snow create in 2018/7/13
 * @version v2.0
 * @since v7.0.0
 */
@RestController
@RequestMapping("/promotions/coupons")
@Api(description = "优惠券相关API")
@Validated
public class CouponBuyerController {

    @Autowired
    private CouponManager couponManager;

    @ApiOperation(value = "查询商家优惠券列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "seller_id", value = "商家ID", dataType = "int", paramType = "query")
    })
    @GetMapping()
    public List<CouponDO> getList(@ApiIgnore Integer sellerId){

        List<CouponDO>  couponDOList = this.couponManager.getList(sellerId);
        return couponDOList;
    }

    @ApiOperation(value = "查询某商品的优惠券列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goods_id", value = "商品ID", dataType = "int", paramType = "query")
    })
    @GetMapping("/goods-use")
    public List<CouponDO> getListByGoods(@ApiIgnore Integer goodsId){

        List<CouponDO>  couponDOList = this.couponManager.getListByGoods(goodsId);

        return couponDOList;
    }


    @ApiOperation(value = "查询所有优惠券")
    @ApiImplicitParams({
            @ApiImplicitParam(name	= "page_no", value = "页码", dataType = "int",	paramType =	"query"),
            @ApiImplicitParam(name	= "page_size", value = "条数", dataType = "int",	paramType =	"query"),
            @ApiImplicitParam(name = "seller_id", value = "商家ID", dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/all")
    public Page<CouponDO> getPage(@ApiIgnore Integer pageNo,@ApiIgnore Integer pageSize,@ApiIgnore Integer sellerId){
        Page<CouponDO> page = this.couponManager.all(pageNo,pageSize,sellerId);
        return page;
    }


}
