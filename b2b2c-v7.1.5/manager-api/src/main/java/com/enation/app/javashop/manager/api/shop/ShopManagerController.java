package com.enation.app.javashop.manager.api.shop;


import com.enation.app.javashop.core.base.context.Region;
import com.enation.app.javashop.core.base.context.RegionFormat;
import com.enation.app.javashop.core.client.goods.TagClient;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.service.MemberManager;
import com.enation.app.javashop.core.shop.ShopErrorCode;
import com.enation.app.javashop.core.shop.model.dos.Clerk;
import com.enation.app.javashop.core.shop.model.dos.ShopThemesDO;
import com.enation.app.javashop.core.shop.model.enums.ShopStatusEnum;
import com.enation.app.javashop.core.shop.model.enums.ShopThemesEnum;
import com.enation.app.javashop.core.shop.model.vo.ShopParamsVO;
import com.enation.app.javashop.core.shop.model.vo.ShopVO;
import com.enation.app.javashop.core.shop.model.vo.operator.AdminEditShop;
import com.enation.app.javashop.core.shop.service.ClerkManager;
import com.enation.app.javashop.core.shop.service.ShopManager;
import com.enation.app.javashop.core.shop.service.ShopThemesManager;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.ServiceException;
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
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 店铺相关API
 *
 * @author zhangjiping
 * @version v7.0.0
 * @since v7.0.0
 * 2018年3月30日 上午10:58:45
 */
@Api(description = "店铺相关API")
@RestController
@RequestMapping("/admin/shops")
@Validated
public class ShopManagerController {
    @Autowired
    private ShopManager shopManager;

    @Autowired
    private ShopThemesManager shopThemesManager;

    @Autowired
    private MemberManager memberManager;

    @Autowired
    private TagClient tagClient;

    @Autowired
    private ClerkManager clerkManager;

    @ApiOperation(value = "分页查询店铺列表", response = ShopVO.class)
    @GetMapping()
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_no", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_size", value = "分页数", required = true, dataType = "int", paramType = "query")
    })
    public Page listPage(ShopParamsVO shopParams, @ApiIgnore @NotNull(message = "页码不能为空") Integer pageNo, @ApiIgnore @NotNull(message = "每页数量不能为空") Integer pageSize) {
        shopParams.setPageNo(pageNo);
        shopParams.setPageSize(pageSize);
        return shopManager.list(shopParams);
    }

    @ApiOperation(value = "分页查询列表", response = ShopVO.class)
    @GetMapping(value = "/list")
    public List<ShopVO> list() {
        return this.shopManager.list();
    }

    @ApiOperation(value = "管理员禁用店铺", response = AdminEditShop.class)
    @PutMapping(value = "/disable/{shop_id}")
    @ApiImplicitParam(name = "shop_id", value = "店铺id", required = true, dataType = "int", paramType = "path")
    public AdminEditShop disShop(@ApiIgnore @PathVariable("shop_id") Integer shopId) {
        AdminEditShop adminEditShop = new AdminEditShop();
        adminEditShop.setSellerId(shopId);
        adminEditShop.setOperator("管理员禁用店铺");
        shopManager.disShop(shopId);
        return adminEditShop;
    }

    @ApiOperation(value = "管理员恢复店铺使用", response = AdminEditShop.class)
    @PutMapping(value = "/enable/{shop_id}")
    @ApiImplicitParam(name = "shop_id", value = "店铺id", required = true, dataType = "int", paramType = "path")
    public AdminEditShop useShop(@ApiIgnore @PathVariable("shop_id") Integer shopId) {
        AdminEditShop adminEditShop = new AdminEditShop();
        adminEditShop.setSellerId(shopId);
        adminEditShop.setOperator("管理员恢复店铺");
        shopManager.useShop(shopId);
        return adminEditShop;
    }

    @ApiOperation(value = "管理员获取店铺详细", response = ShopVO.class)
    @GetMapping("/{shop_id}")
    @ApiImplicitParam(name = "shop_id", value = "店铺id", required = true, dataType = "int", paramType = "path")
    public ShopVO getShopVO(@ApiIgnore @PathVariable("shop_id") @NotNull(message = "店铺id不能为空") Integer shopId) {
        ShopVO shop = shopManager.getShop(shopId);
        if (shop == null) {
            throw new ServiceException(ShopErrorCode.E206.name(), "店铺不存在");
        }
        return shop;
    }

    @ApiOperation(value = "管理员修改审核店铺信息", response = ShopVO.class)
    @PutMapping("/{shop_id}")
    @ApiImplicitParam(name = "pass", value = "是否通过审核 1 通过 0 拒绝 编辑操作则不需传递", paramType = "query", dataType = "int")
    public ShopVO edit(@Valid ShopVO shop, Integer pass, @ApiIgnore @PathVariable("shop_id") Integer shopId,
                       @RegionFormat @RequestParam("license_region") Region licenseRegion,
                       @RegionFormat @RequestParam("bank_region") Region bankRegion,
                       @RegionFormat @RequestParam("shop_region") Region shopRegion) {

        shop = this.info(shop, licenseRegion, bankRegion, shopRegion);

        /**
         * 判断审核还是修改状态
         */
        if (pass != null) {
            ShopVO shopVO = this.getShopVO(shopId);
            if (pass == 1 && shopVO != null && !shopVO.getShopDisable().equals(ShopStatusEnum.OPEN.toString())) {
                ShopThemesDO pcThemes = shopThemesManager.getDefaultShopThemes(ShopThemesEnum.PC.name());
                ShopThemesDO wapThemes = shopThemesManager.getDefaultShopThemes(ShopThemesEnum.WAP.name());

                if (pcThemes == null || wapThemes == null) {
                    throw new ServiceException(ShopErrorCode.E202.name(), "店铺模版不存在,请设置店铺模版");
                }
                //设置模版信息
                shop.setShopThemeid(pcThemes.getId());
                shop.setShopThemePath(pcThemes.getMark());
                shop.setWapThemeid(wapThemes.getId());
                shop.setWapThemePath(wapThemes.getMark());
                //更新店铺信息
                shop.setShopDisable(ShopStatusEnum.OPEN.toString());
                shop.setShopCreatetime(DateUtil.getDateline());
                //更新会员信息
                Member member = memberManager.getModel(shop.getMemberId());
                member.setShopId(shopId);
                member.setHaveShop(1);
                memberManager.edit(member, member.getMemberId());
                //添加店铺商品标签
                tagClient.addShopTags(shopId);
                //将此会员添加为店员
                Clerk clerk = new Clerk();
                clerk.setClerkName(member.getUname());
                clerk.setRoleId(0);
                clerk.setFounder(1);
                clerk.setShopId(shop.getShopId());
                clerk.setMemberId(member.getMemberId());
                clerkManager.addSuperClerk(clerk);
            } else {
                shop.setShopDisable(ShopStatusEnum.REFUSED.toString());
            }
        }
        this.shopManager.editShopInfo(shop);
        return shop;
    }

    @ApiOperation(value = "后台添加店铺", response = ShopVO.class)
    @PostMapping()
    public ShopVO save(@Valid ShopVO shop,
                       @RegionFormat @RequestParam("license_region") Region licenseRegion,
                       @RegionFormat @RequestParam("bank_region") Region bankRegion,
                       @RegionFormat @RequestParam("shop_region") Region shopRegion) {

        shop = this.info(shop, licenseRegion, bankRegion, shopRegion);
        this.shopManager.registStore(shop);
        return shop;
    }

    private ShopVO info(ShopVO shop, Region licenseRegion, Region bankRegion, Region shopRegion) {
        //填充地区信息
        shop.setLicenseProvince(licenseRegion.getProvince());
        shop.setLicenseCity(licenseRegion.getCity());
        shop.setLicenseCounty(licenseRegion.getCounty());
        shop.setLicenseTown(licenseRegion.getTown());
        shop.setLicenseProvinceId(licenseRegion.getProvinceId());
        shop.setLicenseCityId(licenseRegion.getCityId());
        shop.setLicenseCountyId(licenseRegion.getCountyId());
        shop.setLicenseTownId(licenseRegion.getTownId());
        shop.setBankProvince(bankRegion.getProvince());
        shop.setBankCity(bankRegion.getCity());
        shop.setBankCounty(bankRegion.getCounty());
        shop.setBankTown(bankRegion.getTown());
        shop.setBankProvinceId(bankRegion.getProvinceId());
        shop.setBankCityId(bankRegion.getCityId());
        shop.setBankCountyId(bankRegion.getCountyId());
        shop.setBankTownId(bankRegion.getTownId());
        shop.setShopProvince(shopRegion.getProvince());
        shop.setShopCity(shopRegion.getCity());
        shop.setShopCounty(shopRegion.getCounty());
        shop.setShopTown(shopRegion.getTown());
        shop.setShopProvinceId(shopRegion.getProvinceId());
        shop.setShopCityId(shopRegion.getCityId());
        shop.setShopCountyId(shopRegion.getCountyId());
        shop.setShopTownId(shopRegion.getTownId());
        return shop;
    }
}
