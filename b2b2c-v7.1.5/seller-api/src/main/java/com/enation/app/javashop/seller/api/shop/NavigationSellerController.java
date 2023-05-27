package com.enation.app.javashop.seller.api.shop;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.security.model.Seller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.enation.app.javashop.core.shop.model.dos.NavigationDO;
import com.enation.app.javashop.core.shop.model.vo.operator.SellerEditShop;
import com.enation.app.javashop.core.shop.service.NavigationManager;

/**
 * 店铺导航管理控制器
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-28 20:44:54
 */
@RestController
@RequestMapping("/seller/shops/navigations")
@Api(description = "店铺导航管理相关API")
public class NavigationSellerController	{
	
	@Autowired
	private	NavigationManager navigationManager;
				

	@ApiOperation(value	= "查询店铺导航管理列表", response = NavigationDO.class)
	@ApiImplicitParams({
		 @ApiImplicitParam(name	= "page_no",	value =	"页码",	required = true, dataType = "int",	paramType =	"query"),
		 @ApiImplicitParam(name	= "page_size",	value =	"每页显示数量",	required = true, dataType = "int",	paramType =	"query")
	})
	@GetMapping
	public Page list(@ApiIgnore @NotNull(message="页码不能为空") Integer pageNo,@ApiIgnore @NotNull(message="每页数量不能为空") Integer pageSize)	{
		return	this.navigationManager.list(pageNo,pageSize,UserContext.getSeller().getSellerId());
	}
	
	
	@ApiOperation(value	= "添加店铺导航", response = NavigationDO.class)
	@PostMapping
	public NavigationDO add(@Valid NavigationDO navigation)	{
		Seller seller = UserContext.getSeller();
		navigation.setShopId(seller.getSellerId());
		this.navigationManager.add(navigation);
		return	navigation;
	}
				
	@PutMapping(value = "/{id}")
	@ApiOperation(value	= "修改店铺导航", response = NavigationDO.class)
	@ApiImplicitParams({
		 @ApiImplicitParam(name	= "id",	value =	"主键",	required = true, dataType = "int",	paramType =	"path")
	})
	public	NavigationDO edit(@Valid NavigationDO navigation, @PathVariable Integer id) {
		
		this.navigationManager.edit(navigation,id);
		
		return	navigation;
	}
			
	
	@DeleteMapping(value = "/{id}")
	@ApiOperation(value	= "删除店铺导航")
	@ApiImplicitParams({
		 @ApiImplicitParam(name	= "id",	value =	"要删除的店铺导航管理主键",	required = true, dataType = "int",	paramType =	"path")
	})
	public	SellerEditShop	delete(@PathVariable Integer id) {
		Seller seller = UserContext.getSeller();
		SellerEditShop sellerEditShop = new SellerEditShop();
		sellerEditShop.setSellerId(seller.getSellerId());
		sellerEditShop.setOperator("删除店铺导航");
		this.navigationManager.delete(id);
		return sellerEditShop;
	}
				
}
