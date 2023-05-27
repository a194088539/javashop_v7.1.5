package com.enation.app.javashop.buyer.api.goods;

import com.enation.app.javashop.core.goods.constraint.annotation.MarkType;
import com.enation.app.javashop.core.goods.model.dos.TagsDO;
import com.enation.app.javashop.core.goods.model.vo.BuyCountVO;
import com.enation.app.javashop.core.goods.model.vo.GoodsSelectLine;
import com.enation.app.javashop.core.goods.model.vo.TagGoodsNum;
import com.enation.app.javashop.core.goods.service.GoodsQueryManager;
import com.enation.app.javashop.core.goods.service.TagsManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 商品标签控制器
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018-03-28 14:49:36
 */
@RestController
@RequestMapping("/goods")
@Api(description = "标签商品相关API")
@Validated
public class TagsBuyerController {

    @Autowired
    private TagsManager tagsManager;

    @Autowired
    private GoodsQueryManager goodsQueryManager;

    @ApiOperation(value = "查询标签商品列表", response = TagsDO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "seller_id", value = "卖家id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "mark", value = "hot热卖 new新品 recommend推荐", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "num", value = "查询数量", required = true, dataType = "int", paramType = "query")
    })
    @GetMapping("/tags/{mark}/goods")
    public List<GoodsSelectLine> list(@ApiIgnore @NotNull(message = "店铺不能为空") Integer sellerId, Integer num, @MarkType @PathVariable String mark) {

        if (num == null) {
            num = 5;
        }

        return tagsManager.queryTagGoods(sellerId, num, mark);
    }



    @ApiOperation(value = "查询商品销量", response = TagsDO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goods_id", value = "商品id", required = true, dataType = "int", paramType = "query",allowMultiple = true)
    })
    @GetMapping("/tags/count")
    public List<BuyCountVO> list(@ApiIgnore @RequestParam(name = "goods_ids",value = "goods_id") Integer[] goodsId) {
        return goodsQueryManager.queryBuyCount(goodsId);
    }

    @ApiOperation(value = "查询某店铺标签商品的数量", notes = "查询标签商品的数量")
    @ApiImplicitParam(name = "shop_id", value = "店铺id", required = true, paramType = "path", dataType = "int")
    @GetMapping(value = "/tags/{shop_id}/goods-num")
    public TagGoodsNum tagGoodsNum(@PathVariable("shop_id") Integer shopId){

        return tagsManager.queryGoodsNumByShopId(shopId);
    }

}