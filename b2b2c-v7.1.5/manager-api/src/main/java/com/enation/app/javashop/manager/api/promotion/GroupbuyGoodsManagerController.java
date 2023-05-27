package com.enation.app.javashop.manager.api.promotion;

import com.enation.app.javashop.core.promotion.groupbuy.model.dos.GroupbuyActiveDO;
import com.enation.app.javashop.core.promotion.groupbuy.model.vo.GroupbuyGoodsVO;
import com.enation.app.javashop.core.promotion.groupbuy.model.vo.GroupbuyQueryParam;
import com.enation.app.javashop.core.promotion.groupbuy.service.GroupbuyGoodsManager;
import com.enation.app.javashop.framework.database.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;


/**
 * 平台团购商品管理控制器
 * @author Snow create in 2018/4/25
 * @version v2.0
 * @since v7.0.0
 */
@RestController
@RequestMapping("/admin/promotion/group-buy-goods")
@Api(description = "团购商品管理API")
@Validated
public class GroupbuyGoodsManagerController {

    @Autowired
    private GroupbuyGoodsManager groupbuyGoodsManager;

    @ApiOperation(value	= "查询团购商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name	= "act_id", value = "团购活动id",required = true,dataType = "int",paramType =	"query"),
            @ApiImplicitParam(name	= "goods_name", value = "商品名称", dataType = "String",	paramType =	"query"),
            @ApiImplicitParam(name	= "seller_id", value = "店铺ID", dataType = "int",	paramType =	"query"),
            @ApiImplicitParam(name	= "gb_name", value = "团购名称", dataType = "String",	paramType =	"query"),
            @ApiImplicitParam(name	= "gb_status", value = "审核状态 0:待审核，1：通过审核，2：未通过审核", dataType = "int",paramType =	"query", allowableValues = "0,1,2"),
            @ApiImplicitParam(name	= "page_no", value = "页码", dataType = "int",	paramType =	"query"),
            @ApiImplicitParam(name	= "page_size", value = "条数", dataType = "int",	paramType =	"query")
    })
    @GetMapping
    public Page<GroupbuyGoodsVO> list(@ApiIgnore @NotNull(message = "活动ID必传") Integer actId, @ApiIgnore String goodsName, @ApiIgnore Integer sellerId, @ApiIgnore String gbName,
                                      @ApiIgnore Integer gbStatus, @ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize) {

        GroupbuyQueryParam param = new GroupbuyQueryParam();
        param.setActId(actId);
        param.setGoodsName(goodsName);
        param.setSellerId(sellerId);
        param.setGbName(gbName);
        param.setGbStatus(gbStatus);
        param.setPage(pageNo);
        param.setPageSize(pageSize);
        param.setClientType("ADMIN");
        Page webPage = this.groupbuyGoodsManager.listPage(param);
        return webPage;
    }


    @ApiOperation(value	= "查询团购商品信息", response = GroupbuyActiveDO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name	= "gb_id",	value =	"团购商品信息",	required = true, dataType = "int",	paramType =	"path")
    })
    @GetMapping(value = "/{gb_id}")
    public GroupbuyGoodsVO get(@ApiIgnore @PathVariable("gb_id") Integer gbId) {
        GroupbuyGoodsVO goodsDO = this.groupbuyGoodsManager.getModel(gbId);
        return goodsDO;
    }

}
