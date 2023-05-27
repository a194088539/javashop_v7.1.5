package com.enation.app.javashop.manager.api.promotion;

import com.enation.app.javashop.core.promotion.seckill.model.dto.SeckillQueryParam;
import com.enation.app.javashop.core.promotion.seckill.model.vo.SeckillApplyVO;
import com.enation.app.javashop.core.promotion.seckill.service.SeckillGoodsManager;
import com.enation.app.javashop.framework.database.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 限时抢购商品列表
 *
 * @author Snow create in 2018/6/13
 * @version v2.0
 * @since v7.0.0
 */
@RestController
@RequestMapping("/admin/promotion/seckill-applys")
@Api(description = "限时抢购商品管理API")
@Validated
public class SeckillApplyManagerController {

    @Autowired
    private SeckillGoodsManager seckillApplyManager;

    @ApiOperation(value	= "查询限时抢购商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name	= "seckill_id", value = "限时抢购活动id", dataType = "int",	paramType =	"query"),
            @ApiImplicitParam(name	= "status", value = "审核状态", dataType = "String",	paramType =	"query",
                    allowableValues = "APPLY,PASS,FAIL",example = "APPLY:待审核，PASS：通过审核，FAIL：未通过审核"),
            @ApiImplicitParam(name	= "goods_name", value = "商品名称", dataType = "String",	paramType =	"query"),
            @ApiImplicitParam(name	= "seller_id", value = "商家ID", dataType = "int",	paramType =	"query"),
            @ApiImplicitParam(name	= "page_no", value = "取消原因", dataType = "int",	paramType =	"query"),
            @ApiImplicitParam(name	= "page_size", value = "取消原因", dataType = "int",	paramType =	"query")
    })
    @GetMapping()
    public Page<SeckillApplyVO> list(@ApiIgnore Integer seckillId, @ApiIgnore String status, @ApiIgnore String goodsName,
                                     @ApiIgnore Integer sellerId, @ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize) {

        SeckillQueryParam queryParam = new SeckillQueryParam();
        queryParam.setPageNo(pageNo);
        queryParam.setPageSize(pageSize);
        queryParam.setSeckillId(seckillId);
        queryParam.setStatus(status);
        queryParam.setGoodsName(goodsName);
        queryParam.setSellerId(sellerId);
        Page webPage = this.seckillApplyManager.list(queryParam);
        return webPage;
    }

}
