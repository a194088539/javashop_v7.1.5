package com.enation.app.javashop.seller.api.shop;

import com.enation.app.javashop.core.shop.model.dos.ShopLogisticsSetting;
import com.enation.app.javashop.core.shop.model.vo.ShopLogisticsSettingVO;
import com.enation.app.javashop.core.system.model.dto.KDNParams;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.security.model.Seller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

import com.enation.app.javashop.core.shop.model.vo.operator.SellerEditShop;
import com.enation.app.javashop.core.shop.service.ShopLogisticsCompanyManager;
import javax.validation.Valid;

/**
 * 店铺物流公司相关API
 *
 * @author zhangjiping
 * @version v7.0.0
 * @since v7.0.0
 * 2018年3月30日 上午10:56:11
 */
@RestController
@RequestMapping("/seller/shops/logi-companies")
@Api(description = "店铺物流公司相关API")
public class ShopLogisticsCompanySellerController {

    @Autowired
    private ShopLogisticsCompanyManager shopLogisticsCompanyManager;


    @ApiOperation(value = "查询物流公司列表", response = ShopLogisticsSettingVO.class)
    @GetMapping
    public List list() {
        List list = this.shopLogisticsCompanyManager.list();

        return list;
    }


    @ApiOperation(value = "开启物流公司")
    @PostMapping("/{logi_id}")
    @ApiImplicitParam(name = "logi_id", value = "物流公司id", required = true, dataType = "int", paramType = "path")
    public SellerEditShop add(@ApiIgnore @PathVariable("logi_id") Integer logiId) {
        Seller seller = UserContext.getSeller();
        SellerEditShop sellerEditShop = new SellerEditShop();
        sellerEditShop.setSellerId(seller.getSellerId());
        sellerEditShop.setOperator("店铺开启物流公司");
        this.shopLogisticsCompanyManager.open(logiId);

        return sellerEditShop;
    }


    @ApiOperation(value = "设置电子面单")
    @PostMapping("/setting/{logi_id}")
    @ApiImplicitParam(name = "logi_id", value = "物流公司id", required = true, dataType = "int", paramType = "path")
    public void setting(@ApiIgnore @PathVariable("logi_id") Integer logiId, @RequestBody @Valid KDNParams kdnParams) {
        ShopLogisticsSetting shopLogisticsSetting = this.shopLogisticsCompanyManager.query(logiId, UserContext.getSeller().getSellerId());
        shopLogisticsSetting.setParams(kdnParams);
        this.shopLogisticsCompanyManager.edit(shopLogisticsSetting, shopLogisticsSetting.getId());
    }


    @DeleteMapping(value = "/{logi_id}")
    @ApiOperation(value = "关闭物流公司")
    @ApiImplicitParam(name = "logi_id", value = "要删除的物流公司主键", required = true, dataType = "int", paramType = "path")
    public SellerEditShop delete(@ApiIgnore @PathVariable("logi_id") Integer logiId) {
        Seller seller = UserContext.getSeller();
        SellerEditShop sellerEditShop = new SellerEditShop();
        sellerEditShop.setSellerId(seller.getSellerId());
        sellerEditShop.setOperator("店铺关闭物流公司");
        this.shopLogisticsCompanyManager.delete(logiId);

        return sellerEditShop;
    }

}
