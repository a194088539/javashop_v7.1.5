package com.enation.app.javashop.buyer.api.trade;

import com.enation.app.javashop.core.trade.cart.model.enums.CheckedWay;
import com.enation.app.javashop.core.trade.cart.service.CartPromotionManager;
import com.enation.app.javashop.core.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.javashop.core.trade.cart.model.enums.CartType;
import com.enation.app.javashop.core.trade.cart.model.vo.CartVO;
import com.enation.app.javashop.core.trade.cart.model.vo.CartView;
import com.enation.app.javashop.core.trade.cart.service.cartbuilder.CartBuilder;
import com.enation.app.javashop.core.trade.cart.service.cartbuilder.CartPriceCalculator;
import com.enation.app.javashop.core.trade.cart.service.cartbuilder.CartSkuRenderer;
import com.enation.app.javashop.core.trade.cart.service.cartbuilder.CheckDataRebderer;
import com.enation.app.javashop.core.trade.cart.service.cartbuilder.impl.CartSkuFilter;
import com.enation.app.javashop.core.trade.cart.service.cartbuilder.impl.DefaultCartBuilder;
import com.enation.app.javashop.core.trade.cart.util.CartUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;

/**
 * 购物车价格计算接口
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-12-01 下午8:26
 */
@Api(description = "购物车价格计算API")
@RestController
@RequestMapping("/trade/promotion")
@Validated
public class TradePromotionController {


    @Autowired
    private CartPromotionManager promotionManager;


    /**
     * 购物车价格计算器
     */
    @Autowired
    private CartPriceCalculator cartPriceCalculator;
    /**
     * 数据校验
     */
    @Autowired
    private CheckDataRebderer checkDataRebderer;

    /**
     * 购物车sku数据渲染器
     */
    @Autowired
    private CartSkuRenderer cartSkuRenderer;

    @ApiOperation(value = "选择要参与的促销活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "seller_id", value = "卖家id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "sku_id", value = "产品id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "activity_id", value = "活动id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "promotion_type", value = "活动类型", required = true, dataType = "String", paramType = "query"),})
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public void setPromotion(@ApiIgnore Integer sellerId, @ApiIgnore Integer skuId, @ApiIgnore Integer activityId, @ApiIgnore String promotionType) {
        promotionManager.usePromotion(sellerId, skuId, activityId, PromotionTypeEnum.valueOf(promotionType));
    }


    @ApiOperation(value = "取消参与促销")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "seller_id", value = "卖家id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "sku_id", value = "产品id", required = true, dataType = "int", paramType = "query")
    })
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
    public void promotionCancel(Integer sellerId, Integer skuId) {
        promotionManager.delete(new Integer[]{skuId});
    }


    @ApiOperation(value = "设置优惠券", notes = "使用优惠券的时候分为三种情况：前2种情况couponId 不为0,不为空。第3种情况couponId为0," +
            "1、使用优惠券:在刚进入订单结算页，为使用任何优惠券之前。" +
            "2、切换优惠券:在1、情况之后，当用户切换优惠券的时候。" +
            "3、取消已使用的优惠券:用户不想使用优惠券的时候。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "seller_id", value = "店铺ID", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "mc_id", value = "优惠券ID", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "way", value = "结算方式，BUY_NOW：立即购买，CART：购物车", required = true, dataType = "String")
    })
    @PostMapping(value = "/{seller_id}/seller/{mc_id}/coupon")
    public void setCoupon(@NotNull(message = "店铺id不能为空") @PathVariable("seller_id") Integer sellerId,
                          @NotNull(message = "优惠券id不能为空") @PathVariable("mc_id") Integer mcId,
                          @NotNull(message = "结算方式不能为空") @RequestParam String way) {

        CartBuilder cartBuilder = new DefaultCartBuilder(CartType.CART, cartSkuRenderer, null, cartPriceCalculator, checkDataRebderer);

        //查询要结算的某卖家的购物信息
        CartView cartView = cartBuilder.renderSku(CheckedWay.valueOf(way)).countPrice().build();

        //设置优惠券  goodsPrice
        promotionManager.useCoupon(sellerId,mcId, cartView.getCartList());
    }


}
