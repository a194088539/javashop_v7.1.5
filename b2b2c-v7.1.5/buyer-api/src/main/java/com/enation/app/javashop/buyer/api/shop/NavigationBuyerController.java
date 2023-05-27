package com.enation.app.javashop.buyer.api.shop;

import com.enation.app.javashop.core.shop.model.dos.NavigationDO;
import com.enation.app.javashop.core.shop.service.NavigationManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 店铺导航管理控制器
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-28 20:44:54
 */
@RestController
@RequestMapping("/shops/navigations")
@Api(description = "店铺导航管理相关API")
public class NavigationBuyerController {
	
	@Autowired
	private	NavigationManager navigationManager;
				

	@ApiOperation(value	= "查询店铺导航管理列表", response = NavigationDO.class)
	@ApiImplicitParam(name = "shop_id" , value = "店铺id" ,required = true , dataType = "int" , paramType = "path" )
	@GetMapping("/{shop_id}")
	public List<NavigationDO> list(@PathVariable("shop_id") Integer shopId)	{
		List<NavigationDO> list = this.navigationManager.list(shopId,true);
		return list;
	}

}