package com.enation.app.javashop.buyer.api.shop;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enation.app.javashop.core.base.context.Region;
import com.enation.app.javashop.core.base.context.RegionFormat;
import com.enation.app.javashop.core.shop.model.dto.ShopBasicInfoDTO;
import com.enation.app.javashop.core.shop.model.dto.ShopReceiptDTO;
import com.enation.app.javashop.core.shop.model.enums.ShopStatusEnum;
import com.enation.app.javashop.core.shop.model.vo.*;
import com.enation.app.javashop.framework.database.Page;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.enation.app.javashop.core.shop.model.vo.operator.Init;
import com.enation.app.javashop.core.shop.service.ShopManager;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.security.model.Buyer;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 店铺相关API
 *
 * @author zhangjiping
 * @version v7.0.0
 * @since v7.0.0
 * 2018年3月30日 上午10:52:48
 */
@Api(description = "店铺相关API")
@RestController
@RequestMapping("/shops")
@Validated
public class ShopBuyerController {
    @Autowired
    private ShopManager shopManager;

    @PostMapping("/apply")
    @ApiOperation(value = "会员初始化店铺信息")
    public Init save() {
        Init init = new Init();
        Buyer buyer = UserContext.getBuyer();
        ShopVO shopVO = shopManager.getShopByMemberId(buyer.getUid());
        if (shopVO == null) {
            init.setMemberId(buyer.getUid());
            init.setOperator("初始化店铺信息");
            shopManager.saveInit();
            return init;
        }
        //被拒绝的再次申请修改状态
        if (shopVO.getShopDisable().equals(ShopStatusEnum.REFUSED.value())) {
            shopManager.editStatus(ShopStatusEnum.APPLYING, shopVO.getShopId());
        }

        init.setShopStatus(shopVO.getShopDisable());
        return init;
    }

    @ApiOperation(value = "会员申请店铺第一步", response = ApplyStep1VO.class)
    @PutMapping("/apply/step1")
    public ApplyStep1VO step1(@Valid ApplyStep1VO applyStep1) {
        shopManager.step1(applyStep1);
        return applyStep1;
    }

    @ApiOperation(value = "会员申请店铺第二步", response = ApplyStep2VO.class)
    @PutMapping("/apply/step2")
    public ApplyStep2VO step2(@Valid ApplyStep2VO applyStep2, @RegionFormat @RequestParam("license_region") Region licenseRegion) {
        applyStep2.setLicenseProvince(licenseRegion.getProvince());
        applyStep2.setLicenseCity(licenseRegion.getCity());
        applyStep2.setLicenseCounty(licenseRegion.getCounty());
        applyStep2.setLicenseTown(licenseRegion.getTown());
        applyStep2.setLicenseProvinceId(licenseRegion.getProvinceId());
        applyStep2.setLicenseCityId(licenseRegion.getCityId());
        applyStep2.setLicenseCountyId(licenseRegion.getCountyId());
        applyStep2.setLicenseTownId(licenseRegion.getTownId());
        shopManager.step2(applyStep2);
        return applyStep2;
    }

    @ApiOperation(value = "会员申请店铺第三步", response = ApplyStep3VO.class)
    @PutMapping("/apply/step3")
    public ApplyStep3VO step3(@Valid ApplyStep3VO applyStep3, @RegionFormat @RequestParam("bank_region") Region bankRegion) {
        applyStep3.setBankProvince(bankRegion.getProvince());
        applyStep3.setBankCity(bankRegion.getCity());
        applyStep3.setBankCounty(bankRegion.getCounty());
        applyStep3.setBankTown(bankRegion.getTown());
        applyStep3.setBankProvinceId(bankRegion.getProvinceId());
        applyStep3.setBankCityId(bankRegion.getCityId());
        applyStep3.setBankCountyId(bankRegion.getCountyId());
        applyStep3.setBankTownId(bankRegion.getTownId());
        shopManager.step3(applyStep3);
        return applyStep3;
    }

    @ApiOperation(value = "会员申请店铺第四步", response = ApplyStep4VO.class)
    @PutMapping("/apply/step4")
    public ApplyStep4VO step4(@Valid ApplyStep4VO applyStep4, @RegionFormat @RequestParam("shop_region") Region shopRegion) {
        applyStep4.setShopProvince(shopRegion.getProvince());
        applyStep4.setShopCity(shopRegion.getCity());
        applyStep4.setShopCounty(shopRegion.getCounty());
        applyStep4.setShopTown(shopRegion.getTown());
        applyStep4.setShopProvinceId(shopRegion.getProvinceId());
        applyStep4.setShopCityId(shopRegion.getCityId());
        applyStep4.setShopCountyId(shopRegion.getCountyId());
        applyStep4.setShopTownId(shopRegion.getTownId());
        shopManager.step4(applyStep4);
        return applyStep4;
    }

    @ApiOperation(value = "校验店铺名称是否重复")
    @GetMapping(value = "/apply/check-name")
    @ApiImplicitParam(name = "shop_name", value = "店铺名称", required = true, dataType = "String", paramType = "query")
    public Boolean checkShopName(@ApiIgnore @NotEmpty(message = "店铺名称必填") String shopName) {
        return shopManager.checkShopName(shopName, null);
    }

    @ApiOperation(value = "检测身份证是否重复")
    @GetMapping(value = "/apply/id-card")
    @ApiImplicitParam(name = "id_card", value = "身份证号", required = true, dataType = "String", paramType = "query")
    public Boolean checkIdCard(@ApiIgnore @NotEmpty(message = "身份证号必填") String idCard) {
        return shopManager.checkIdNumber(idCard);
    }

    @ApiOperation(value = "获取店铺信息(未登录状态)", response = ShopBasicInfoDTO.class)
    @ApiImplicitParam(name = "shop_id", value = "店铺id", required = true, dataType = "int", paramType = "path")
    @GetMapping("/{shop_id}")
    public ShopBasicInfoDTO get(@PathVariable("shop_id") Integer shopId) {
        return shopManager.getShopBasicInfo(shopId);
    }

    @ApiOperation(value = "获取店铺信息(已登录状态)", response = ShopVO.class)
    @GetMapping("/apply")
    public ShopVO get() {
        Buyer buyer = UserContext.getBuyer();
        return shopManager.getShopByMemberId(buyer.getUid());
    }

    @ApiOperation(value = "查询店铺列表", response = ShopListVO.class)
    @GetMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_no", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_size", value = "分页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "店铺名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "order", value = "按好评率排序", required = false, dataType = "String", paramType = "query")
    })
    public Page list(@ApiIgnore @NotNull(message = "页码不能为空") Integer pageNo, @ApiIgnore @NotNull(message = "每页数量不能为空") Integer pageSize, String name, String order) {
        ShopParamsVO shopParams = new ShopParamsVO();
        shopParams.setPageNo(pageNo);
        shopParams.setPageSize(pageSize);
        shopParams.setShopName(name);
        shopParams.setOrder(order);
        return shopManager.listShopBasicInfo(shopParams);
    }

    @GetMapping(value = "/{ids}/check/receipt")
    @ApiOperation(value = "检测商家开票功能是否可用")
    @ApiImplicitParams({@ApiImplicitParam(name = "ids", value = "商家ID集合", required = true, dataType = "int", paramType = "path", allowMultiple = true)})
    public ShopReceiptDTO checkReceipt(@PathVariable("ids") Integer[] ids) {

        return this.shopManager.checkSellerReceipt(ids);
    }
}

