package com.enation.app.javashop.seller.api.shop;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.security.model.Seller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import javax.validation.Valid;

import com.enation.app.javashop.core.shop.model.dos.ShopSildeDO;
import com.enation.app.javashop.core.shop.model.vo.operator.SellerEditShop;
import com.enation.app.javashop.core.shop.service.ShopSildeManager;

/**
 * 店铺幻灯片控制器
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-28 18:50:58
 */
@RestController
@RequestMapping("/seller/shops/sildes")
@Api(description = "店铺幻灯片相关API")
@Validated
public class ShopSildeSellerController	{
	
	@Autowired
	private	ShopSildeManager shopSildeManager;
				

	@ApiOperation(value	= "查询店铺幻灯片列表", response = ShopSildeDO.class)
	@GetMapping()
	public List<ShopSildeDO> list()	{
		return	this.shopSildeManager.list(UserContext.getSeller().getSellerId());
	}
	
	@PutMapping()
	@ApiOperation(value	= "修改店铺幻灯片", response = SellerEditShop.class)
	public	SellerEditShop edit(@Valid @RequestBody List<ShopSildeDO> list) {
		SellerEditShop sellerEditShop = new SellerEditShop();
		Seller seller = UserContext.getSeller();
		sellerEditShop.setSellerId(seller.getSellerId());
		sellerEditShop.setOperator("修改店铺幻灯片");
		this.shopSildeManager.edit(list);
		return	sellerEditShop;
	}

	
	@DeleteMapping(value = "/{id}")
	@ApiOperation(value	= "删除店铺幻灯片")
	@ApiImplicitParams({
		 @ApiImplicitParam(name	= "id",	value =	"要删除的店铺幻灯片主键",	required = true, dataType = "int",	paramType =	"path")
	})
	public	SellerEditShop	delete(@PathVariable("id") Integer id) {
		SellerEditShop sellerEditShop = new SellerEditShop();
		Seller seller = UserContext.getSeller();
		sellerEditShop.setSellerId(seller.getSellerId());
		sellerEditShop.setOperator("删除幻灯片");
		
		this.shopSildeManager.delete(id);
		
		return sellerEditShop;
	}
				
}
