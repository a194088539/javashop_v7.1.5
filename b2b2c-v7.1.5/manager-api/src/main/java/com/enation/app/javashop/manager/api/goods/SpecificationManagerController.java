package com.enation.app.javashop.manager.api.goods;

import com.enation.app.javashop.core.goods.model.dos.SpecificationDO;
import com.enation.app.javashop.core.goods.service.SpecificationManager;
import com.enation.app.javashop.framework.database.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * 规格项控制器
 * 
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018-03-20 09:31:27
 */
@RestController
@RequestMapping("/admin/goods/specs")
@Api(description = "规格项相关API")
public class SpecificationManagerController {

	@Autowired
	private SpecificationManager specificationManager;

	@ApiOperation(value = "查询规格项列表", response = SpecificationDO.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page_no", value = "页码", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "page_size", value = "每页显示数量", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "keyword", value = "规格名称", dataType = "string", paramType = "query")
			})
	@GetMapping
	public Page list(@ApiIgnore @NotEmpty(message = "页码不能为空") Integer pageNo,
			@ApiIgnore @NotEmpty(message = "每页数量不能为空") Integer pageSize, String keyword) {

		return this.specificationManager.list(pageNo, pageSize, keyword);
	}

	@ApiOperation(value = "添加规格项", response = SpecificationDO.class)
	@PostMapping
	public SpecificationDO add(@Valid SpecificationDO specification) {
		// 平台的规格
		specification.setSellerId(0);
		this.specificationManager.add(specification);

		return specification;
	}

	@PutMapping(value = "/{id}")
	@ApiOperation(value = "修改规格项", response = SpecificationDO.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "int", paramType = "path") })
	public SpecificationDO edit(@Valid SpecificationDO specification, @PathVariable Integer id) {

		this.specificationManager.edit(specification, id);

		return specification;
	}

	@DeleteMapping(value = "/{ids}")
	@ApiOperation(value = "删除规格项")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "ids", value = "要删除的规格项主键", required = true, dataType = "int", paramType = "path",allowMultiple=true) })
	public String delete(@PathVariable Integer[] ids) {

		this.specificationManager.delete(ids);

		return "";
	}

	@GetMapping(value = "/{id}")
	@ApiOperation(value = "查询一个规格项")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "要查询的规格项主键", required = true, dataType = "int", paramType = "path") })
	public SpecificationDO get(@PathVariable Integer id) {

		SpecificationDO specification = this.specificationManager.getModel(id);

		return specification;
	}

}
