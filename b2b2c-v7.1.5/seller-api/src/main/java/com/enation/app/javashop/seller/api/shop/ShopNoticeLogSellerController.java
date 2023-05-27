package com.enation.app.javashop.seller.api.shop;

import com.enation.app.javashop.core.member.model.dos.MemberNoticeLog;
import com.enation.app.javashop.core.shop.model.dos.ShopNoticeLogDO;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.enation.app.javashop.framework.database.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import com.enation.app.javashop.core.shop.service.ShopNoticeLogManager;

/**
 * 店铺站内消息控制器
 *
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-07-10 10:21:45
 */
@RestController
@RequestMapping("/seller/shops/shop-notice-logs")
@Api(description = "店铺站内消息相关API")
public class ShopNoticeLogSellerController {

    @Autowired
    private ShopNoticeLogManager shopNoticeLogManager;


    @ApiOperation(value = "查询店铺站内消息列表", response = ShopNoticeLogDO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_no", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_size", value = "每页显示数量", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "is_read", value = "是否已读 1已读 0未读", required = false, dataType = "int", paramType = "query", allowableValues = "0,1"),
            @ApiImplicitParam(name = "type", value = "消息类型", required = false, dataType = "String", paramType = "query", allowableValues = ",ORDER,GOODS,AFTERSALE")
    })
    @GetMapping
    public Page list(@ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize, @ApiIgnore Integer isRead, String type) {

        return this.shopNoticeLogManager.list(pageNo, pageSize, type, isRead);
    }

    @DeleteMapping(value = "/{ids}")
    @ApiOperation(value = "删除店铺站内消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "要删除的消息主键", required = true, dataType = "int", paramType = "path", allowMultiple = true)
    })
    public void delete(@PathVariable("ids") Integer[] ids) {

        this.shopNoticeLogManager.delete(ids);
 
    }

    @PutMapping(value = "/{ids}/read")
    @ApiOperation(value = "将消息设置为已读", response = MemberNoticeLog.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "要设置为已读消息的id", required = true, dataType = "int", paramType = "path", allowMultiple = true)
    })
    public String read(@PathVariable Integer[] ids) {
        this.shopNoticeLogManager.read(ids);
        return null;
    }

}
