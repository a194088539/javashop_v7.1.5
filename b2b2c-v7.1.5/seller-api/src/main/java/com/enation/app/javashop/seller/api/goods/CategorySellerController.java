package com.enation.app.javashop.seller.api.goods;

import com.enation.app.javashop.core.goods.model.dos.BrandDO;
import com.enation.app.javashop.core.goods.model.dos.CategoryDO;
import com.enation.app.javashop.core.goods.model.vo.CategoryVO;
import com.enation.app.javashop.core.goods.model.vo.GoodsParamsGroupVO;
import com.enation.app.javashop.core.goods.service.BrandManager;
import com.enation.app.javashop.core.goods.service.CategoryManager;
import com.enation.app.javashop.core.goods.service.GoodsParamsManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 商品分类控制器
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018-03-15 17:22:06
 */
@RestController
@RequestMapping("/seller/goods")
@Api(description = "商品分类相关API")
public class CategorySellerController {

    @Autowired
    private CategoryManager categoryManager;
    @Autowired
    private GoodsParamsManager goodsParamsManager;
    @Autowired
    private BrandManager brandManager;

    @ApiOperation(value = "商品发布，获取当前登录用户选择经营类目的所有父")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category_id", value = "分类id，顶级为0", required = true, dataType = "int", paramType = "path"),})
    @GetMapping(value = "/category/{category_id}/children")
    public List<CategoryDO> list(@ApiIgnore @PathVariable("category_id") Integer categoryId) {
        List<CategoryDO> list = this.categoryManager.getSellerCategory(categoryId);
        return list;
    }


    @ApiOperation(value = "商家分类")
    @GetMapping(value = "/category/seller/children")
    public List<CategoryVO> listSeller() {
        List<CategoryVO> list = this.categoryManager.listAllSellerChildren();
        return list;
    }

    @ApiOperation(value = "商品发布，获取所选分类关联的参数信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category_id", value = "分类id", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "goods_id", value = "商品id", required = true, dataType = "int", paramType = "path"),
    })
    @GetMapping(value = "/category/{category_id}/{goods_id}/params")
    public List<GoodsParamsGroupVO> queryParams(@PathVariable("category_id") Integer categoryId, @PathVariable("goods_id") Integer goodsId) {

        List<GoodsParamsGroupVO> list = this.goodsParamsManager.queryGoodsParams(categoryId, goodsId);

        return list;
    }

    @ApiOperation(value = "发布商品，获取所选分类关联的参数信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category_id", value = "分类id", required = true, dataType = "int", paramType = "path")
    })
    @GetMapping(value = "/category/{category_id}/params")
    public List<GoodsParamsGroupVO> queryParams(@PathVariable("category_id") Integer categoryId) {

        List<GoodsParamsGroupVO> list = this.goodsParamsManager.queryGoodsParams(categoryId);

        return list;
    }

    @ApiOperation(value = "修改商品，获取所选分类关联的品牌信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category_id", value = "分类id", required = true, dataType = "int", paramType = "path"),
    })
    @GetMapping(value = "/category/{category_id}/brands")
    public List<BrandDO> queryBrands(@PathVariable("category_id") Integer categoryId) {

        List<BrandDO> list = this.brandManager.getBrandsByCategory(categoryId);

        return list;
    }


}
