package com.enation.app.javashop.seller.api.shop;


import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.enation.app.javashop.core.shop.model.dos.ShopDetailDO;
import com.enation.app.javashop.core.shop.model.enums.ShopThemesEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.javashop.core.shop.model.vo.ShopThemesVO;
import com.enation.app.javashop.core.shop.model.vo.operator.SellerEditShop;
import com.enation.app.javashop.core.shop.service.ShopManager;
import com.enation.app.javashop.core.shop.service.ShopThemesManager;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.security.model.Seller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 店铺模版API
 * @author zhangjiping
 * @version v7.0.0
 * @since v7.0.0
 * 2018年3月30日 上午11:25:40
 */
@Api(description="店铺模版API")
@RestController
@RequestMapping("/seller/shops/themes")
@Validated
public class ShopThemesSellerController {
	
	@Autowired
	private ShopThemesManager shopThemesManager;

	@Autowired
	private ShopManager shopManager;
	
	@ApiOperation(value = "更换店铺模板",response = SellerEditShop.class)
	@ApiImplicitParam(name = "themes_id" , value = "模版id" , required = true , dataType = "int" , paramType = "path")
	@PutMapping(value="/{themes_id}")
	public SellerEditShop changeShopThemes(@ApiIgnore @PathVariable("themes_id") Integer themesId){
		SellerEditShop sellerEdit = new SellerEditShop();
		Seller seller = UserContext.getSeller();
		sellerEdit.setSellerId(seller.getUid());
		sellerEdit.setOperator("修改店铺模版");
		shopThemesManager.changeShopThemes(themesId);
		return sellerEdit;
	}
	
	@ApiOperation(value = "获取模版列表")
	@ApiImplicitParam(name = "type" , value = "模版类型 PC/WAP" , required = true , dataType = "String" , paramType = "query")
	@GetMapping()
	public List<ShopThemesVO> list(@NotEmpty(message = "模版类型必填") String type){
		List<ShopThemesVO> list = shopThemesManager.list(type);
		ShopDetailDO shopDetail = shopManager.getShopDetail(UserContext.getSeller().getSellerId());
		//获取店铺的默认模版
		Integer id = 0;
		if(type.equals(ShopThemesEnum.PC.name())){
			id = shopDetail.getShopThemeid();
		}else {
			id = shopDetail.getWapThemeid();
		}
		for (ShopThemesVO shopThemesVO : list) {
			if(shopThemesVO.getId().equals(id)){
				shopThemesVO.setCurrentUse(1);
			}else {
				shopThemesVO.setCurrentUse(0);
			}
		}
		return list;
	}
}
