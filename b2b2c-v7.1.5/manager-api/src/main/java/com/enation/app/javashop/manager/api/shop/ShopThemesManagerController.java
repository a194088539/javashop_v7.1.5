package com.enation.app.javashop.manager.api.shop;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.ServiceException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enation.app.javashop.core.shop.ShopErrorCode;
import com.enation.app.javashop.core.shop.model.dos.ShopThemesDO;
import com.enation.app.javashop.core.shop.model.vo.operator.AdminEditShop;
import com.enation.app.javashop.core.shop.service.ShopThemesManager;

/**
 * 店铺模版控制器
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-27 14:17:32
 */
@RestController
@RequestMapping("/admin/shops/themes")
@Api(description = "店铺模版相关API")
@Validated
public class ShopThemesManagerController	{
	
	@Autowired
	private	ShopThemesManager shopThemesManager;
				

	@ApiOperation(value	= "查询店铺模版列表", response = ShopThemesDO.class)
	@ApiImplicitParams({
		 @ApiImplicitParam(name	= "page_no",	value =	"页码",	required = true, dataType = "int",	paramType =	"query"),
		 @ApiImplicitParam(name	= "page_size",	value =	"每页显示数量",	required = true, dataType = "int",	paramType =	"query"),
		 @ApiImplicitParam(name	= "type",	value =	"模版类型",	required = true, dataType = "String",	paramType =	"query",allowableValues = "PC,WAP")
	})
	@GetMapping
	public Page list(@ApiIgnore @NotNull(message="页码不能为空") Integer pageNo,@ApiIgnore @NotNull(message="每页数量不能为空") Integer pageSize,@ApiIgnore@NotEmpty(message="模版类型必填")String type)	{
		
		return	this.shopThemesManager.list(pageNo,pageSize,type);
	}
	
	
	@ApiOperation(value	= "添加店铺模版", response = ShopThemesDO.class)
	@PostMapping
	public ShopThemesDO add(@Valid ShopThemesDO shopThemes)	{
		
		this.shopThemesManager.add(shopThemes);
		
		return	shopThemes;
	}
				
	@PutMapping(value = "/{id}")
	@ApiOperation(value	= "修改店铺模版", response = ShopThemesDO.class)
	@ApiImplicitParams({
		 @ApiImplicitParam(name	= "id",	value =	"主键",	required = true, dataType = "int",	paramType =	"path")
	})
	public	ShopThemesDO edit(@Valid ShopThemesDO shopThemes, @PathVariable Integer id) {
		
		this.shopThemesManager.edit(shopThemes,id);
		
		return	shopThemes;
	}
			
	
	@DeleteMapping(value = "/{id}")
	@ApiOperation(value	= "删除店铺模版")
	@ApiImplicitParams({
		 @ApiImplicitParam(name	= "id",	value =	"要删除的店铺模版主键",	required = true, dataType = "int",	paramType =	"path")
	})
	public	AdminEditShop	delete(@PathVariable Integer id) {
		
		AdminEditShop adminEditShop = new AdminEditShop();
		adminEditShop.setOperator("管理员删除店铺模版");
		this.shopThemesManager.delete(id);
		
		return adminEditShop;
	}
				
	
	@GetMapping(value =	"/{id}")
	@ApiOperation(value	= "查询一个店铺模版", response = ShopThemesDO.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id",	value = "要查询的店铺模版主键",	required = true, dataType = "int",	paramType = "path")	
	})
	public	ShopThemesDO get(@PathVariable("id")	Integer	id)	{
		
		ShopThemesDO shopThemes = this.shopThemesManager.getModel(id);
		
		if(shopThemes==null) {
			throw new ServiceException(ShopErrorCode.E202.name(), "模版不存在");
		}
		
		return	shopThemes;
	}
				
}
