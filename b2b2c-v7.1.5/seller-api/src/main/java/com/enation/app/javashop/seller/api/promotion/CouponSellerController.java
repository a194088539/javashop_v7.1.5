package com.enation.app.javashop.seller.api.promotion;

import com.enation.app.javashop.core.promotion.PromotionErrorCode;
import com.enation.app.javashop.core.promotion.coupon.model.dos.CouponDO;
import com.enation.app.javashop.core.promotion.coupon.model.dto.CouponParams;
import com.enation.app.javashop.core.promotion.coupon.service.CouponManager;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.NoPermissionException;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.exception.SystemErrorCodeV1;
import com.enation.app.javashop.framework.security.model.Seller;
import com.enation.app.javashop.framework.util.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 优惠券控制器
 *
 * @author Snow
 * @version v2.0
 * @since v7.0.0
 * 2018-04-17 23:19:39
 */
@RestController
@RequestMapping("/seller/promotion/coupons")
@Api(description = "优惠券相关API")
@Validated
public class CouponSellerController {

    @Autowired
    private CouponManager couponManager;


    @ApiOperation(value = "查询优惠券列表", response = CouponDO.class)
    @GetMapping
    public Page list(@ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize,
                     CouponParams params) {

        params.setPageNo(pageNo);
        params.setPageSize(pageSize);
        Seller seller = UserContext.getSeller();
        params.setSellerId(seller.getSellerId());

        return this.couponManager.list(params);
    }


    @ApiOperation(value = "添加优惠券", response = CouponDO.class)
    @PostMapping
    public CouponDO add(@Valid CouponDO couponDO) {

        Seller seller = UserContext.getSeller();

        couponDO.setSellerId(seller.getSellerId());
        couponDO.setSellerName(seller.getSellerName());

        this.couponManager.add(couponDO);

        return couponDO;
    }

    @PutMapping(value = "/{coupon_id}")
    @ApiOperation(value = "修改优惠券", response = CouponDO.class)
    @ApiImplicitParam(name = "coupon_id", value = "优惠券id", required = true, dataType = "int", paramType = "path")
    public CouponDO add(@ApiIgnore @PathVariable("coupon_id") Integer couponId,@Valid CouponDO couponDO) {

        couponDO.setCouponId(couponId);

        Seller seller = UserContext.getSeller();

        couponDO.setSellerId(seller.getSellerId());
        couponDO.setSellerName(seller.getSellerName());

        this.couponManager.edit(couponDO, couponId);

        return couponDO;
    }


    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除优惠券")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要删除的优惠券主键", required = true, dataType = "int", paramType = "path")
    })
    public String delete(@PathVariable Integer id) {

        this.couponManager.verifyAuth(id);
        this.couponManager.delete(id);

        return "";
    }


    @GetMapping(value = "/{id}")
    @ApiOperation(value = "查询一个优惠券")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要查询的优惠券主键", required = true, dataType = "int", paramType = "path")
    })
    public CouponDO get(@PathVariable Integer id) {

        CouponDO coupon = this.couponManager.getModel(id);
        if (coupon == null || !coupon.getSellerId().equals(UserContext.getSeller().getSellerId())) {
            throw new NoPermissionException("无权操作或者数据不存在");
        }

        return coupon;
    }

    @GetMapping(value = "/{status}/list")
    @ApiOperation(value = "根据状态获取优惠券数据集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "优惠券状态 0：全部，1：有效，2：失效", required = true, dataType = "int", paramType = "path", allowableValues = "0,1,2", example = "0：全部，1：有效，2：失效")
    })
    public List<CouponDO> getByStatus(@PathVariable Integer status) {

        return this.couponManager.getByStatus(status);
    }

}
