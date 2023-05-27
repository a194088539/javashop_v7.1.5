package com.enation.app.javashop.buyer.api.shop;

import com.enation.app.javashop.core.shop.model.dos.ShopSildeDO;
import com.enation.app.javashop.core.shop.service.ShopSildeManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 店铺幻灯片控制器
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-28 18:50:58
 */
@RestController
@RequestMapping("/shops/sildes")
@Api(description = "店铺幻灯片相关API")
@Validated
public class ShopSildeBuyerController {
	
	@Autowired
	private	ShopSildeManager shopSildeManager;
				

	@ApiOperation(value	= "查询店铺幻灯片列表", response = ShopSildeDO.class)
	@ApiImplicitParam(name = "shop_id" , value = "店铺id" ,required = true , dataType = "int" , paramType = "path" )
	@GetMapping("/{shop_id}")
	public List<ShopSildeDO> list(@PathVariable("shop_id") Integer shopId)	{
		return	this.shopSildeManager.list(shopId);
	}
				
}