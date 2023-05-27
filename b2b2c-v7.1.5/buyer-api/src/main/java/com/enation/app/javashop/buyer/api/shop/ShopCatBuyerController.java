package com.enation.app.javashop.buyer.api.shop;

import com.enation.app.javashop.core.shop.model.dos.ShopCatDO;
import com.enation.app.javashop.core.shop.service.ShopCatManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 店铺分组控制器
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-04-24 11:18:37
 */
@RestController
@RequestMapping("/shops/cats")
@Api(description = "店铺分组相关API")
public class ShopCatBuyerController {

	@Autowired
	private ShopCatManager shopCatManager;
				

	@ApiOperation(value	= "查询店铺分组列表", response = ShopCatDO.class)
	@GetMapping("/{shop_id}")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "shop_id", value = "店铺id", required = true, dataType = "int", paramType = "path"),
			@ApiImplicitParam(name = "display", value = "是否展示,根据分类的显示状态查询：ALL(全部),SHOW(显示),HIDE(隐藏)", required = false, dataType = "string", paramType = "query",
					allowableValues = "ALL,SHOW,HIDE")
	})
	public List list(@PathVariable("shop_id") Integer shopId,@ApiIgnore  String display)	{
		return	this.shopCatManager.list(shopId,display);
	}

}