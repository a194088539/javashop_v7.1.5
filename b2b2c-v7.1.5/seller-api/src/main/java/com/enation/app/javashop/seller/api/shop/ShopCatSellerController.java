package com.enation.app.javashop.seller.api.shop;

import com.enation.app.javashop.core.shop.ShopCatShowTypeEnum;
import com.enation.app.javashop.core.shop.model.dos.ShopCatDO;
import com.enation.app.javashop.core.shop.service.ShopCatManager;
import com.enation.app.javashop.framework.context.UserContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;
import java.util.List;

/**
 * 店铺分组控制器
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-04-24 11:18:37
 */
@RestController
@RequestMapping("/seller/shops/cats")
@Api(description = "店铺分组相关API")
public class ShopCatSellerController	{
	
	@Autowired
	private ShopCatManager shopCatManager;
				

	@ApiOperation(value	= "查询店铺分组列表", response = ShopCatDO.class)
	@GetMapping
	public List list()	{
		return	this.shopCatManager.list(UserContext.getSeller().getSellerId(), ShopCatShowTypeEnum.ALL.name());
	}
	
	
	@ApiOperation(value	= "添加店铺分组", response = ShopCatDO.class)
	@PostMapping
	public ShopCatDO add(@Valid ShopCatDO shopCat)	{
		
		this.shopCatManager.add(shopCat);
		
		return	shopCat;
	}
				
	@PutMapping(value = "/{id}")
	@ApiOperation(value	= "修改店铺分组", response = ShopCatDO.class)
	@ApiImplicitParams({
		 @ApiImplicitParam(name	= "id",	value =	"主键",	required = true, dataType = "int",	paramType =	"path")
	})
	public	ShopCatDO edit(@Valid ShopCatDO shopCat, @PathVariable("id") Integer id) {
		this.shopCatManager.edit(shopCat,id);
		
		return	shopCat;
	}
			
	
	@DeleteMapping(value = "/{id}")
	@ApiOperation(value	= "删除店铺分组")
	@ApiImplicitParams({
		 @ApiImplicitParam(name	= "id",	value =	"要删除的店铺分组主键",	required = true, dataType = "int",	paramType =	"path")
	})
	public	String	delete(@PathVariable("id") Integer id) {
		
		this.shopCatManager.delete(id);
		
		return "";
	}
				
	
	@GetMapping(value =	"/{id}")
	@ApiOperation(value	= "查询一个店铺分组")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id",	value = "要查询的店铺分组主键",	required = true, dataType = "int",	paramType = "path")	
	})
	public	ShopCatDO get(@PathVariable	Integer	id)	{

		ShopCatDO shopCat = this.shopCatManager.getModel(id);
		
		return	shopCat;
	}


}
