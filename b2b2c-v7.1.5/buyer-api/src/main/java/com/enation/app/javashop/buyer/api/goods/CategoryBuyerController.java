package com.enation.app.javashop.buyer.api.goods;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.javashop.core.goods.model.dos.BrandDO;
import com.enation.app.javashop.core.goods.model.vo.BrandVO;
import com.enation.app.javashop.core.goods.model.vo.CategoryVO;
import com.enation.app.javashop.core.goods.service.BrandManager;
import com.enation.app.javashop.core.goods.service.CategoryManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 商品分类控制器
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018-03-15 17:22:06
 */
@RestController
@RequestMapping("/goods/categories")
@Api(description = "商品分类相关API")
public class CategoryBuyerController {

    @Autowired
    private CategoryManager categoryManager;

    @Autowired
    private BrandManager brandManager;

    @ApiOperation(value = "首页等商品分类数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parent_id", value = "分类id，顶级为0", required = true, dataType = "int", paramType = "path"),})
    @GetMapping(value = "/{parent_id}/children")
    public List<CategoryVO> list(@PathVariable("parent_id") Integer parentId) {

        List<CategoryVO> catTree = categoryManager.listAllChildren(parentId);

        for (CategoryVO cat : catTree) {
            // 一级分类显示关联的品牌
            if (cat.getParentId().compareTo(parentId) == 0) {

                List<BrandDO> brandList = brandManager.getBrandsByCategory(cat.getCategoryId());

                List<BrandVO> brandNavList = new ArrayList<>();
                for (BrandDO brand : brandList) {
                    BrandVO brandNav = new BrandVO();
                    BeanUtils.copyProperties(brand,brandNav);
                    brandNavList.add(brandNav);
                }
                cat.setBrandList(brandNavList);
            }
        }

        return catTree;
    }
}