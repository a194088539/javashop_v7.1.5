package com.enation.app.javashop.manager.api.promotion;

import com.enation.app.javashop.core.promotion.groupbuy.model.dos.GroupbuyCatDO;
import com.enation.app.javashop.core.promotion.groupbuy.service.GroupbuyCatManager;
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

/**
 * 团购分类控制器
 * @author Snow
 * @version v7.0.0
 * @since v7.0.0
 * 2018-04-02 16:08:03
 */
@RestController
@RequestMapping("/admin/promotion/group-buy-cats")
@Api(description = "团购分类相关API")
@Validated
public class GroupbuyCatManagerController	{

	@Autowired
	private GroupbuyCatManager groupbuyCatManager;


	@ApiOperation(value	= "查询团购分类列表", response = GroupbuyCatDO.class)
	@ApiImplicitParams({
		 @ApiImplicitParam(name	= "page_no",	value =	"页码",	dataType = "int",	paramType =	"query"),
		 @ApiImplicitParam(name	= "page_size",	value =	"每页显示数量", dataType = "int",	paramType =	"query")
	})
	@GetMapping
	public Page list(@ApiIgnore Integer pageNo,@ApiIgnore Integer pageSize)	{
		return	this.groupbuyCatManager.list(pageNo,pageSize);
	}


	@ApiOperation(value	= "添加团购分类", response = GroupbuyCatDO.class)
	@PostMapping
	public GroupbuyCatDO add(@Valid @RequestBody GroupbuyCatDO groupbuyCat)	{

		this.groupbuyCatManager.add(groupbuyCat);

		return	groupbuyCat;
	}

	@PutMapping(value = "/{id}")
	@ApiOperation(value	= "修改团购分类", response = GroupbuyCatDO.class)
	@ApiImplicitParams({
		 @ApiImplicitParam(name	= "id",	value =	"主键",	required = true, dataType = "int",	paramType =	"path")
	})
	public	GroupbuyCatDO edit(@Valid @RequestBody GroupbuyCatDO groupbuyCat, @PathVariable Integer id) {

		this.groupbuyCatManager.edit(groupbuyCat,id);

		return	groupbuyCat;
	}


	@DeleteMapping(value = "/{id}")
	@ApiOperation(value	= "删除团购分类")
	@ApiImplicitParams({
		 @ApiImplicitParam(name	= "id",	value =	"要删除的团购分类主键",	required = true, dataType = "int",	paramType =	"path")
	})
	public	String	delete(@PathVariable Integer id) {
		this.groupbuyCatManager.delete(id);
		return "";
	}


	@GetMapping(value =	"/{id}")
	@ApiOperation(value	= "查询一个团购分类")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id",	value = "要查询的团购分类主键",	required = true, dataType = "int",	paramType = "path")
	})
	public	GroupbuyCatDO get(@PathVariable	Integer	id)	{
		GroupbuyCatDO groupbuyCat = this.groupbuyCatManager.getModel(id);
		return	groupbuyCat;
	}

}
