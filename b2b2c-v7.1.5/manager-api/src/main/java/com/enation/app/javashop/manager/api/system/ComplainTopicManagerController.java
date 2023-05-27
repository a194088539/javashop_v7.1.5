package com.enation.app.javashop.manager.api;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.enation.app.javashop.framework.database.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import com.enation.app.javashop.core.system.model.ComplainTopic;
import com.enation.app.javashop.core.system.service.ComplainTopicManager;

/**
 * 投诉主题控制器
 * @author fk
 * @version v2.0
 * @since v2.0
 * 2019-11-26 16:06:44
 */
@RestController
@RequestMapping("/admin/systems/complain-topics")
@Api(description = "投诉主题相关API")
public class ComplainTopicManagerController {
	
	@Autowired
	private	ComplainTopicManager complainTopicManager;
				

	@ApiOperation(value	= "查询投诉主题列表", response = ComplainTopic.class)
	@ApiImplicitParams({
		 @ApiImplicitParam(name	= "page_no",	value =	"页码",	required = true, dataType = "int",	paramType =	"query"),
		 @ApiImplicitParam(name	= "page_size",	value =	"每页显示数量",	required = true, dataType = "int",	paramType =	"query")
	})
	@GetMapping
	public Page list(@ApiIgnore Integer pageNo,@ApiIgnore Integer pageSize)	{
		
		return	this.complainTopicManager.list(pageNo,pageSize);
	}
	
	
	@ApiOperation(value	= "添加投诉主题", response = ComplainTopic.class)
	@PostMapping
	public ComplainTopic add(@Valid ComplainTopic complainTopic)	{
		
		this.complainTopicManager.add(complainTopic);
		
		return	complainTopic;
	}
				
	@PutMapping(value = "/{id}")
	@ApiOperation(value	= "修改投诉主题", response = ComplainTopic.class)
	@ApiImplicitParams({
		 @ApiImplicitParam(name	= "id",	value =	"主键",	required = true, dataType = "int",	paramType =	"path")
	})
	public	ComplainTopic edit(@Valid ComplainTopic complainTopic, @PathVariable Integer id) {
		
		this.complainTopicManager.edit(complainTopic,id);
		
		return	complainTopic;
	}
			
	
	@DeleteMapping(value = "/{id}")
	@ApiOperation(value	= "删除投诉主题")
	@ApiImplicitParams({
		 @ApiImplicitParam(name	= "id",	value =	"要删除的投诉主题主键",	required = true, dataType = "int",	paramType =	"path")
	})
	public	String	delete(@PathVariable Integer id) {
		
		this.complainTopicManager.delete(id);
		
		return "";
	}
				
	
	@GetMapping(value =	"/{id}")
	@ApiOperation(value	= "查询一个投诉主题")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id",	value = "要查询的投诉主题主键",	required = true, dataType = "int",	paramType = "path")	
	})
	public	ComplainTopic get(@PathVariable	Integer	id)	{
		
		ComplainTopic complainTopic = this.complainTopicManager.getModel(id);
		
		return	complainTopic;
	}
				
}