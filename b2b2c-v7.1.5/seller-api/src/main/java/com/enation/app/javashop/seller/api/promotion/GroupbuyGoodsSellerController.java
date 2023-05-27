package com.enation.app.javashop.seller.api.promotion;

import com.enation.app.javashop.core.promotion.groupbuy.model.dos.GroupbuyActiveDO;
import com.enation.app.javashop.core.promotion.groupbuy.model.dos.GroupbuyGoodsDO;
import com.enation.app.javashop.core.promotion.groupbuy.model.vo.GroupbuyGoodsVO;
import com.enation.app.javashop.core.promotion.groupbuy.model.vo.GroupbuyQueryParam;
import com.enation.app.javashop.core.promotion.groupbuy.service.GroupbuyActiveManager;
import com.enation.app.javashop.core.promotion.groupbuy.service.GroupbuyGoodsManager;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.NoPermissionException;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.exception.SystemErrorCodeV1;
import com.enation.app.javashop.framework.security.model.Seller;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

/**
 * 团购商品控制器
 * @author Snow
 * @version v7.0.0
 * @since v7.0.0
 * 2018-04-02 16:57:26
 */
@RestController
@RequestMapping("/seller/promotion/group-buy-goods")
@Api(description = "团购商品相关API")
@Validated
public class GroupbuyGoodsSellerController	{

	@Autowired
	private GroupbuyGoodsManager groupbuyGoodsManager;

	@Autowired
	private GroupbuyActiveManager groupbuyActiveManager;

	@ApiOperation(value	= "查询团购商品列表", response = GroupbuyGoodsDO.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name	= "page_no",	value =	"页码",	required = true, dataType = "int",	paramType =	"query"),
			@ApiImplicitParam(name	= "page_size",	value =	"每页显示数量",	required = true, dataType = "int",	paramType =	"query"),
			@ApiImplicitParam(name	= "goods_name", value = "商品名称", dataType = "String",	paramType =	"query"),
			@ApiImplicitParam(name	= "act_name", value = "团购活动名称", dataType = "String",	paramType =	"query"),
			@ApiImplicitParam(name	= "gb_name", value = "团购名称", dataType = "String",	paramType =	"query"),
			@ApiImplicitParam(name	= "gb_status",	value =	"审核状态 0:待审核,1:审核通过,2:未通过审核", dataType = "int",	paramType =	"query",allowableValues = "0,1,2"),
			@ApiImplicitParam(name	= "act_status",	value =	"活动状态 NOT_STARTED:未开始,STARTED:进行中,OVER:已结束", dataType = "String",	paramType =	"query",allowableValues = "NOT_STARTED,STARTED,OVER")
	})
	@GetMapping
	public Page list(@ApiIgnore  Integer pageNo,@ApiIgnore  Integer pageSize, @ApiIgnore String goodsName, @ApiIgnore String actName,
					 @ApiIgnore String gbName, @ApiIgnore Integer gbStatus, @ApiIgnore String actStatus)	{
		Seller seller = UserContext.getSeller();
		GroupbuyQueryParam param = new GroupbuyQueryParam();
		param.setSellerId(seller.getSellerId());
		param.setPage(pageNo);
		param.setPageSize(pageSize);
		param.setGoodsName(goodsName);
		param.setActName(actName);
		param.setGbName(gbName);
		param.setGbStatus(gbStatus);
		param.setActStatus(actStatus);
		param.setClientType("SELLER");
		return this.groupbuyGoodsManager.listPage(param);
	}


	@ApiOperation(value	= "添加团购商品", response = GroupbuyGoodsDO.class)
	@ApiImplicitParam(name = "groupbuyGoods", value = "团购商品信息", required = true, dataType = "GroupbuyGoodsDO", paramType = "body")
	@PostMapping
	public GroupbuyGoodsDO add(@Valid @RequestBody GroupbuyGoodsDO groupbuyGoods) {
		groupbuyGoods.setAddTime(DateUtil.getDateline());
		this.verifyParam(groupbuyGoods);
		Seller seller = UserContext.getSeller();
		groupbuyGoods.setSellerId(seller.getSellerId());
		groupbuyGoods.setSellerName(seller.getSellerName());
		groupbuyGoods.setBuyNum(0);
		groupbuyGoods.setViewNum(0);
		this.groupbuyGoodsManager.add(groupbuyGoods);

		return	groupbuyGoods;
	}

	@PutMapping(value = "/{id}")
	@ApiOperation(value	= "修改团购商品", response = GroupbuyGoodsDO.class)
	@ApiImplicitParams({
		 @ApiImplicitParam(name	= "id",	value =	"主键",	required = true, dataType = "int",	paramType =	"path")
	})
	public	GroupbuyGoodsDO edit(@Valid @RequestBody GroupbuyGoodsDO groupbuyGoods, @PathVariable Integer id) {

		this.verifyParam(groupbuyGoods);
		this.groupbuyGoodsManager.verifyAuth(id);
		this.groupbuyGoodsManager.edit(groupbuyGoods,id);

		return	groupbuyGoods;
	}


	@DeleteMapping(value = "/{id}")
	@ApiOperation(value	= "删除团购商品")
	@ApiImplicitParams({
		 @ApiImplicitParam(name	= "id",	value =	"要删除的团购商品主键",	required = true, dataType = "int",	paramType =	"path")
	})
	public	String	delete(@PathVariable Integer id) {

		this.groupbuyGoodsManager.verifyAuth(id);
		this.groupbuyGoodsManager.delete(id);

		return "";
	}


	@GetMapping(value =	"/{id}")
	@ApiOperation(value	= "查询一个团购商品")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id",	value = "要查询的团购商品主键",	required = true, dataType = "int",	paramType = "path")
	})
	public GroupbuyGoodsVO get(@PathVariable Integer id)	{
		GroupbuyGoodsVO groupbuyGoods = this.groupbuyGoodsManager.getModelAndQuantity(id);
		Seller seller = UserContext.getSeller();
		if(groupbuyGoods == null || groupbuyGoods.getSellerId().intValue() != seller.getSellerId() ){
			throw new NoPermissionException("无权操作");
		}
		return	groupbuyGoods;
	}


	@ApiOperation(value	= "查询可以参与的团购活动列表", response = GroupbuyGoodsDO.class)
	@GetMapping(value = "/active")
	public List<GroupbuyActiveDO> listActive()	{

		return	this.groupbuyActiveManager.getActiveList();
	}


	/**
	 * 验证参数
	 * @param goodsDO
	 */
	private void verifyParam(GroupbuyGoodsDO goodsDO){

		String gbName = goodsDO.getGbName();
		if(!StringUtil.validMaxLen(gbName,30)){
			throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER,"团购名称字数超限");
		}

	}

}
