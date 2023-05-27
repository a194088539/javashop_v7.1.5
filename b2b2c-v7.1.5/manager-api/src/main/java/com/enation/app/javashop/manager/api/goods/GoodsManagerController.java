package com.enation.app.javashop.manager.api.goods;

import com.enation.app.javashop.core.goods.model.dto.GoodsAuditParam;
import com.enation.app.javashop.core.goods.model.dto.GoodsQueryParam;
import com.enation.app.javashop.core.goods.model.enums.Permission;
import com.enation.app.javashop.core.goods.model.vo.GoodsSelectLine;
import com.enation.app.javashop.core.goods.service.GoodsManager;
import com.enation.app.javashop.core.goods.service.GoodsQueryManager;
import com.enation.app.javashop.framework.database.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 商品控制器
 * 
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018-03-21 11:23:10
 */
@RestController
@RequestMapping("/admin/goods")
@Api(description = "商品相关API")
@Validated
public class GoodsManagerController {

	@Autowired
	private GoodsManager goodsManager;
	@Autowired
	private GoodsQueryManager goodsQueryManager;

	@ApiOperation(value = "查询商品或者审核列表")
	@GetMapping
	public Page list(GoodsQueryParam param,@ApiIgnore Integer pageSize,@ApiIgnore Integer pageNo) {

		param.setPageNo(pageNo);
		param.setPageSize(pageSize);
		return this.goodsQueryManager.list(param);
	}

	
	@ApiOperation(value = "管理员下架商品",notes = "管理员下架商品时使用")
	@ApiImplicitParams({
		@ApiImplicitParam(name="goods_id",value="商品ID",required=true,paramType="path",dataType="int"),
		@ApiImplicitParam(name="reason",value="下架理由",required=true,paramType="query",dataType="string")
	})
	@PutMapping(value = "/{goods_id}/under")
	public String underGoods(@PathVariable("goods_id") Integer goodsId,@NotEmpty(message = "下架原因不能为空") String reason){
		
		this.goodsManager.under(new Integer[]{goodsId},reason,Permission.ADMIN);
		
		return null;
	}
	
	@ApiOperation(value = "管理员上架商品",notes = "管理员上架商品时使用")
	@ApiImplicitParams({
		@ApiImplicitParam(name="goods_id",value="商品ID",required=true,paramType="path",dataType="int"),
	})
	@PutMapping(value = "/{goods_id}/up")
	public String unpGoods(@PathVariable("goods_id") Integer goodsId){
		
		this.goodsManager.up(goodsId);
		
		return null;
	}
	

	@ApiOperation(value = "管理员批量审核商品",notes = "审核商品时使用")
	@PostMapping(value = "/batch/audit")
	public String batchAudit(@Valid @RequestBody GoodsAuditParam param) {

		this.goodsManager.batchAuditGoods(param);

		return null;
	}

	@GetMapping(value = "/{goods_ids}/details")
	@ApiOperation(value = "查询多个商品的基本信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "goods_ids", value = "要查询的商品主键", required = true, dataType = "int", paramType = "path",allowMultiple = true) })
	public List<GoodsSelectLine> getGoodsDetail(@PathVariable("goods_ids") Integer[] goodsIds) {

		return this.goodsQueryManager.query(goodsIds,null);
	}
	
}
