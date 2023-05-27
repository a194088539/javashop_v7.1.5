package com.enation.app.javashop.seller.api.shop;

import com.enation.app.javashop.core.shop.model.dos.ShopMenu;
import com.enation.app.javashop.core.shop.model.vo.ShopMenuVO;
import com.enation.app.javashop.core.shop.model.vo.ShopMenusVO;
import com.enation.app.javashop.core.shop.service.ShopMenuManager;
import com.enation.app.javashop.core.system.model.dos.Menu;
import com.enation.app.javashop.framework.util.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

/**
 * 菜单管理控制器
 *
 * @author zh
 * @version v7.0
 * @since v7.0.0
 * 2018-06-19 09:46:02
 */
@RestController
@RequestMapping("/seller/shops/menus")
@Api(description = "菜单管理相关API")
public class MenuSellerController {

    @Autowired
    private ShopMenuManager shopMenuManager;


    @ApiOperation(value = "根据父id查询所有菜单", response = Menu.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parent_id", value = "菜单父id，如果查询顶级菜单则传0", required = true, dataType = "int", paramType = "path")
    })
    @GetMapping("/{parent_id}/children")
    public List<ShopMenusVO> getMenuTree(@PathVariable("parent_id") @ApiIgnore Integer parentId) {
        return this.shopMenuManager.getMenuTree(parentId);
    }


    @ApiOperation(value = "添加菜单", response = ShopMenu.class)
    @PostMapping
    public ShopMenu add(@Valid ShopMenuVO menu) {
        return this.shopMenuManager.add(menu);
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "修改菜单", response = ShopMenu.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "int", paramType = "path")
    })
    public ShopMenu edit(@Valid ShopMenuVO menuVO, @PathVariable Integer id) {
        ShopMenu menu = new ShopMenu();
        BeanUtil.copyProperties(menuVO, menu);
        return this.shopMenuManager.edit(menu, id);
    }


    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要删除的菜单管理主键", required = true, dataType = "int", paramType = "path")
    })
    public String delete(@PathVariable Integer id) {
        this.shopMenuManager.delete(id);
        return "";
    }


    @GetMapping(value = "/{id}")
    @ApiOperation(value = "查询一个菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要查询的菜单管理主键", required = true, dataType = "int", paramType = "path")
    })
    public ShopMenu get(@PathVariable Integer id) {
        ShopMenu menu = this.shopMenuManager.getModel(id);
        return menu;
    }

}
