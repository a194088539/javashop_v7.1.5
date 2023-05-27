package com.enation.app.javashop.manager.api.goods;

import com.enation.app.javashop.core.goods.model.dos.CategoryBrandDO;
import com.enation.app.javashop.core.goods.model.dos.CategoryDO;
import com.enation.app.javashop.core.goods.model.dos.CategorySpecDO;
import com.enation.app.javashop.core.goods.model.vo.ParameterGroupVO;
import com.enation.app.javashop.core.goods.model.vo.SelectVO;
import com.enation.app.javashop.core.goods.service.BrandManager;
import com.enation.app.javashop.core.goods.service.CategoryManager;
import com.enation.app.javashop.core.goods.service.ParameterGroupManager;
import com.enation.app.javashop.core.goods.service.SpecificationManager;
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
 * 商品分类控制器
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018-03-15 17:22:06
 */
@RestController
@RequestMapping("/admin/goods/categories")
@Api(description = "商品分类相关API")
@Validated
public class CategoryManagerController {

    @Autowired
    private CategoryManager categoryManager;

    @Autowired
    private BrandManager brandManager;

    @Autowired
    private SpecificationManager specificationManager;

    @Autowired
    private ParameterGroupManager parameterGroupManager;

    @ApiOperation(value = "查询某分类下的子分类列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parent_id", value = "父id，顶级为0", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "format", value = "类型，如果值是plugin则查询插件使用的格式数据", required = false, dataType = "string", paramType = "query"),})
    @GetMapping(value = "/{parent_id}/children")
    public List list(@ApiIgnore @PathVariable("parent_id") Integer parentId, @ApiIgnore String format) {

        List list = this.categoryManager.list(parentId, format);

        return list;
    }

    @ApiOperation(value = "查询某分类下的全部子分类列表")
    @ApiImplicitParam(name = "parent_id", value = "父id，顶级为0", required = true, dataType = "int", paramType = "path")
    @GetMapping(value = "/{parent_id}/all-children")
    public List list(@ApiIgnore @PathVariable("parent_id") Integer parentId) {

        List list = this.categoryManager.listAllChildren(parentId);

        return list;
    }

    @ApiOperation(value = "添加商品分类", response = CategoryDO.class)
    @PostMapping
    public CategoryDO add(@Valid CategoryDO category) {

        this.categoryManager.add(category);

        return category;
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "修改商品分类", response = CategoryDO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "int", paramType = "path")})
    public CategoryDO edit(@Valid CategoryDO category, @ApiIgnore @PathVariable Integer id) {

        this.categoryManager.edit(category, id);

        return category;
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除商品分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要删除的商品分类主键", required = true, dataType = "int", paramType = "path")})
    public String delete(@PathVariable Integer id) {

        this.categoryManager.delete(id);

        return "";
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "查询一个商品分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要查询的商品分类主键", required = true, dataType = "int", paramType = "path")})
    public CategoryDO get(@PathVariable Integer id) {

        CategoryDO category = this.categoryManager.getModel(id);

        return category;
    }

    @ApiOperation(value = "查询分类品牌", notes = "查询某个分类绑定的品牌,包括未选中的品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category_id", value = "分类id", required = true, dataType = "int", paramType = "path"),})
    @GetMapping(value = "/{category_id}/brands")
    public List<SelectVO> getCatBrands(@PathVariable("category_id") Integer categoryId) {

        List<SelectVO> brands = brandManager.getCatBrand(categoryId);

        return brands;
    }

    @ApiOperation(value = "管理员操作分类绑定品牌", notes = "管理员操作分类绑定品牌使用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category_id", value = "分类id", required = true, paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "choose_brands", value = "品牌id数组", required = true, paramType = "query", dataType = "int", allowMultiple = true)})
    @PutMapping(value = "/{category_id}/brands")
    public List<CategoryBrandDO> saveBrand(@PathVariable("category_id") Integer categoryId, @ApiIgnore @RequestParam(value = "choose_brands",required = false) Integer[] chooseBrands) {
        return this.categoryManager.saveBrand(categoryId, chooseBrands);
    }

    @ApiOperation(value = "查询分类规格", notes = "查询所有规格，包括分类绑定的规格，selected为true")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category_id", value = "分类id", required = true, dataType = "int", paramType = "path"),})
    @GetMapping(value = "/{category_id}/specs")
    public List<SelectVO> getCatSpecs(@PathVariable("category_id") Integer categoryId) {

        List<SelectVO> brands = specificationManager.getCatSpecification(categoryId);

        return brands;
    }

    @ApiOperation(value = "管理员操作分类绑定规格", notes = "管理员操作分类绑定规格使用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category_id", value = "分类id", required = true, paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "choose_specs", value = "规格id数组", required = true, paramType = "query", dataType = "int", allowMultiple = true),})
    @PutMapping(value = "/{category_id}/specs")
    public List<CategorySpecDO> saveSpec(@PathVariable("category_id") Integer categoryId, @ApiIgnore @RequestParam(value = "choose_specs",required = false) Integer[] chooseSpecs) {

        return this.categoryManager.saveSpec(categoryId, chooseSpecs);
    }

    @ApiOperation(value = "查询分类参数", notes = "查询分类绑定的参数，包括参数组和参数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category_id", value = "分类id", required = true, dataType = "int", paramType = "path"),})
    @GetMapping(value = "/{category_id}/param")
    public List<ParameterGroupVO> getCatParam(@PathVariable("category_id") Integer categoryId) {

        return parameterGroupManager.getParamsByCategory(categoryId);
    }

}
