package com.enation.app.javashop.seller.api.shop;

import com.enation.app.javashop.core.shop.model.dos.ShipTemplateDO;
import com.enation.app.javashop.core.shop.model.vo.ShipTemplateSellerVO;
import com.enation.app.javashop.core.shop.model.vo.ShipTemplateVO;
import com.enation.app.javashop.core.shop.model.vo.operator.SellerEditShop;
import com.enation.app.javashop.core.shop.service.ShipTemplateManager;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.security.model.Seller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

/**
 * 运费模版控制器
 *
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-28 21:44:49
 */
@RestController
@RequestMapping("/seller/shops/ship-templates")
@Api(description = "运费模版相关API")
@Validated
public class ShipTemplateSellerController {

    @Autowired
    private ShipTemplateManager shipTemplateManager;


    @ApiOperation(value = "查询运费模版列表", response = ShipTemplateVO.class)
    @GetMapping
    public List<ShipTemplateSellerVO> list() {
        return this.shipTemplateManager.getStoreTemplate(UserContext.getSeller().getSellerId());
    }


    @ApiOperation(value = "添加运费模版", response = ShipTemplateDO.class)
    @PostMapping
    public ShipTemplateDO add(@Valid @RequestBody ShipTemplateSellerVO shipTemplate) {

        return this.shipTemplateManager.save(shipTemplate);
    }

    @PutMapping(value = "/{template_id}")
    @ApiOperation(value = "修改运费模版", response = ShipTemplateDO.class)
    @ApiImplicitParam(name = "template_id", value = "模版id", required = true, dataType = "int", paramType = "path")
    public ShipTemplateDO edit(@Valid @RequestBody ShipTemplateSellerVO shipTemplate, @ApiIgnore @PathVariable("template_id") Integer templateId) {
        shipTemplate.setId(templateId);
        return this.shipTemplateManager.edit(shipTemplate);
    }


    @DeleteMapping(value = "/{template_id}")
    @ApiOperation(value = "删除运费模版")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "template_id", value = "要删除的运费模版主键", required = true, dataType = "int", paramType = "path")
    })
    public SellerEditShop delete(@ApiIgnore @PathVariable("template_id") Integer templateId) {
        Seller seller = UserContext.getSeller();
        SellerEditShop sellerEditShop = new SellerEditShop();
        sellerEditShop.setSellerId(seller.getSellerId());
        sellerEditShop.setOperator("删除店铺运费模版");
        this.shipTemplateManager.delete(templateId);
        return sellerEditShop;
    }

    @GetMapping(value = "/{template_id}")
    @ApiOperation(value = "查询一个运费模版")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "template_id", value = "要查询的运费模版主键", required = true, dataType = "int", paramType = "path")
    })
    public ShipTemplateSellerVO get(@ApiIgnore @PathVariable("template_id") Integer templateId) {

        ShipTemplateSellerVO shipTemplate = this.shipTemplateManager.getFromDB(templateId);

        return shipTemplate;
    }

}
