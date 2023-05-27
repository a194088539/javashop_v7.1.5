package com.enation.app.javashop.buyer.api.member;

import com.enation.app.javashop.core.base.model.vo.SuccessMessage;
import com.enation.app.javashop.core.member.model.dos.MemberCollectionShop;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.enation.app.javashop.framework.database.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enation.app.javashop.core.member.service.MemberCollectionShopManager;

/**
 * 会员收藏店铺表控制器
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-30 20:34:23
 */
@RestController
@RequestMapping("/members")
@Api(description = "会员收藏店铺表相关API")
@Validated
public class MemberCollectionShopBuyerController {

    @Autowired
    private MemberCollectionShopManager memberCollectionShopManager;


    @ApiOperation(value = "查询会员收藏店铺列表", response = MemberCollectionShop.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_no", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_size", value = "每页显示数量", required = true, dataType = "int", paramType = "query")
    })
    @GetMapping("/collection/shops")
    public Page list(@ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize) {
        return this.memberCollectionShopManager.list(pageNo, pageSize);
    }


    @ApiOperation(value = "添加会员收藏店铺", response = MemberCollectionShop.class)
    @ApiImplicitParam(name = "shop_id", value = "店铺id", required = true, dataType = "int", paramType = "query")
    @PostMapping("/collection/shop")
    public MemberCollectionShop add(@NotNull(message = "店铺id不能为空") @ApiIgnore Integer shopId) {
        MemberCollectionShop memberCollectionShop = new MemberCollectionShop();
        memberCollectionShop.setShopId(shopId);
        try {
            return this.memberCollectionShopManager.add(memberCollectionShop);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    @DeleteMapping(value = "/collection/shop/{shop_id}")
    @ApiOperation(value = "删除会员收藏店铺")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shop_id", value = "要删除的店铺id", required = true, dataType = "int", paramType = "path")
    })
    public String delete(@PathVariable("shop_id") Integer shopId) {
        this.memberCollectionShopManager.delete(shopId);
        return "";
    }

    @GetMapping(value = "/collection/shop/{id}")
    @ApiOperation(value = "查看会员是否收藏店铺")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要检索的收藏店铺id", required = true, dataType = "int", paramType = "path")
    })
    public SuccessMessage get(@PathVariable Integer id) {
        SuccessMessage successMessage = new SuccessMessage(this.memberCollectionShopManager.isCollection(id));
        return successMessage;
    }

}