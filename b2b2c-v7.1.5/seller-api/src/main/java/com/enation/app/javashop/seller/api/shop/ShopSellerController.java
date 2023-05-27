package com.enation.app.javashop.seller.api.shop;


import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enation.app.javashop.core.base.context.Region;
import com.enation.app.javashop.core.base.context.RegionFormat;
import com.enation.app.javashop.core.shop.model.dos.Clerk;
import com.enation.app.javashop.core.shop.model.dto.ShopReceiptDTO;
import com.enation.app.javashop.core.shop.model.dto.ShopSettingDTO;
import com.enation.app.javashop.core.shop.model.enums.ShopStatusEnum;
import com.enation.app.javashop.core.shop.model.vo.ShopInfoVO;
import com.enation.app.javashop.core.shop.service.ClerkManager;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.framework.util.UserTokenPrefix;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.enation.app.javashop.core.shop.model.vo.ShopSettingVO;
import com.enation.app.javashop.core.shop.model.vo.ShopVO;
import com.enation.app.javashop.core.shop.model.vo.operator.SellerEditShop;
import com.enation.app.javashop.core.shop.service.ShopManager;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.security.model.Seller;

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
 * 2018年3月30日 上午10:54:57
 */
@Api(description = "店铺相关API")
@RestController
@RequestMapping("/seller/shops")
@Validated
public class ShopSellerController {
    @Autowired
    private ShopManager shopManager;

    @Autowired
    private ClerkManager clerkManager;

    public static String noShop = "NO_SHOP";

    @ApiOperation(value = "修改店铺设置", response = ShopSettingVO.class)
    @ApiImplicitParam(name = "shop_name", value = "店铺名称", required = true, dataType = "String", paramType = "query")
    @PutMapping()
    public ShopSettingVO edit(@Valid ShopSettingDTO shopSetting, @RegionFormat @RequestParam("shop_region") Region shopRegion, String shopName) {
        //修改店铺信息
        ShopVO shopVO = this.shopManager.getShop(UserContext.getSeller().getSellerId());
        shopVO.setShopName(shopName);
        this.shopManager.editShopInfo(shopVO);
        //修改店铺详细信息
        shopSetting.setShopProvince(shopRegion.getProvince());
        shopSetting.setShopCity(shopRegion.getCity());
        shopSetting.setShopCounty(shopRegion.getCounty());
        shopSetting.setShopTown(shopRegion.getTown());
        shopSetting.setShopProvinceId(shopRegion.getProvinceId());
        shopSetting.setShopCityId(shopRegion.getCityId());
        shopSetting.setShopCountyId(shopRegion.getCountyId());
        shopSetting.setShopTownId(shopRegion.getTownId());
        this.shopManager.editShopSetting(shopSetting);
        ShopSettingVO shopSettingVO = new ShopSettingVO();
        BeanUtils.copyProperties(shopSetting, shopSettingVO);
        shopSettingVO.setShopName(shopName.trim());
        return shopSettingVO;
    }


    @ApiOperation(value = "修改店铺Logo", response = SellerEditShop.class)
    @PutMapping(value = "/logos")
    @ApiImplicitParam(name = "logo", value = "图片路径", required = true, dataType = "String", paramType = "query")
    public SellerEditShop editShopLogo(@ApiIgnore @NotEmpty(message = "店铺logo必填") String logo) {
        SellerEditShop sellerEdit = new SellerEditShop();
        Seller seller = UserContext.getSeller();
        sellerEdit.setSellerId(seller.getSellerId());
        sellerEdit.setOperator("修改店铺logo");
        shopManager.editShopOnekey("shop_logo", logo);
        return sellerEdit;
    }


    @ApiOperation(value = "修改货品预警数设置", response = SellerEditShop.class)
    @PutMapping(value = "/warning-counts")
    @ApiImplicitParam(name = "warning_count", value = "预警货品数", required = true, dataType = "String", paramType = "query")
    public SellerEditShop editWarningCount(@ApiIgnore @NotNull(message = "预警货品数必填") String warningCount) {
        SellerEditShop sellerEdit = new SellerEditShop();
        Seller seller = UserContext.getSeller();
        sellerEdit.setSellerId(seller.getUid());
        sellerEdit.setOperator("修改货品预警数");
        this.shopManager.editShopOnekey("goods_warning_count", warningCount);
        return sellerEdit;
    }

    @ApiOperation(value = "获取店铺信息", response = ShopVO.class)
    @GetMapping
    public ShopInfoVO get() {
        Seller seller = UserContext.getSeller();
        ShopInfoVO shop = shopManager.getShopInfo(seller.getSellerId());
        return shop;
    }

    @ApiOperation(value = "获取店铺状态")
    @GetMapping(value = "/status")
    public String getShopStatus() {
        Clerk clerk = clerkManager.getClerkByMemberId(UserContext.getBuyer().getUid());
        if (clerk != null && clerk.getUserState().equals(0)) {
            ShopVO shopVO = shopManager.getShop(clerk.getShopId());
            return shopVO.getShopDisable();
        }
        return noShop;

    }

    @ApiOperation(value = "商家发票设置")
    @PutMapping(value = "/receipt")
    public void receiptSetting(@Valid ShopReceiptDTO shopReceiptDTO) {

        this.shopManager.receiptSetting(shopReceiptDTO);
    }
}
