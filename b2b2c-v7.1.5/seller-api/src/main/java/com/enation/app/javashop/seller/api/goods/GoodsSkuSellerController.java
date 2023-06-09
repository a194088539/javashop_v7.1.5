package com.enation.app.javashop.seller.api.goods;

import java.util.List;

import com.enation.app.javashop.core.goods.model.dto.GoodsQueryParam;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.Page;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.goods.model.vo.GoodsSkuVO;
import com.enation.app.javashop.core.goods.service.GoodsQueryManager;
import com.enation.app.javashop.core.goods.service.GoodsSkuManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 商品sku控制器
 * 
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018-03-21 11:48:40
 */
@RestController
@RequestMapping("/seller/goods")
@Api(description = "商品sku相关API")
public class GoodsSkuSellerController {

	@Autowired
	private GoodsQueryManager goodsQueryManager;

	@Autowired
	private GoodsSkuManager goodsSkuManager;

	@ApiOperation(value = "商品sku信息信息获取api")
	@ApiImplicitParam(name	= "goods_id", value = "商品id",	required = true, dataType = "int",	paramType =	"path")
	@GetMapping("/{goods_id}/skus")
	public List<GoodsSkuVO> queryByGoodsId(@PathVariable(name = "goods_id") Integer goodsId) {

		CacheGoods goods = goodsQueryManager.getFromCache(goodsId);

		return  goods.getSkuList();
	}



	@GetMapping(value = "/skus/{sku_ids}/details")
	@ApiOperation(value = "查询多个商品的基本信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "sku_ids", value = "要查询的SKU主键", required = true, dataType = "int", paramType = "path",allowMultiple = true) })
	public List<GoodsSkuVO> getGoodsDetail(@PathVariable("sku_ids") Integer[] skuIds) {

		return this.goodsSkuManager.query(skuIds);
	}


	@ApiOperation(value = "查询SKU列表", response = GoodsSkuVO.class)
	@GetMapping("/skus")
	public Page list(GoodsQueryParam param, @ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize) {
		param.setPageNo(pageNo);
		param.setPageSize(pageSize);
		param.setSellerId(UserContext.getSeller().getSellerId());
		return this.goodsSkuManager.list(param);
	}

}
