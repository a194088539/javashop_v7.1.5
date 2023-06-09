package com.enation.app.javashop.buyer.api.goods;

import com.enation.app.javashop.core.goodssearch.model.GoodsSearchDTO;
import com.enation.app.javashop.core.goodssearch.model.GoodsWords;
import com.enation.app.javashop.core.goodssearch.service.GoodsSearchManager;
import com.enation.app.javashop.framework.database.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * @author fk
 * @version v2.0
 * @Description: 商品全文检索
 * @date 2018/6/1915:55
 * @since v7.0.0
 */
@RestController
@RequestMapping("/goods/search")
@Api(description = "商品检索相关API")
public class GoodsSearchBuyerController {

    @Autowired
    private GoodsSearchManager goodsSearchManager;

    @ApiOperation(value = "查询商品列表")
    @GetMapping
    public Page searchGoods(@ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize,GoodsSearchDTO goodsSearch){

        goodsSearch.setPageNo(pageNo);
        goodsSearch.setPageSize(pageSize);

        return goodsSearchManager.search(goodsSearch);
    }

    @ApiOperation(value = "查询商品选择器")
    @GetMapping("/selector")
    public Map searchGoodsSelector(GoodsSearchDTO goodsSearch){

        return goodsSearchManager.getSelector(goodsSearch);
    }

    @ApiOperation(value = "查询商品分词对应数量")
    @ApiImplicitParam(name = "keyword", value = "搜索关键字", required = true, dataType = "string", paramType = "query")
    @GetMapping("/words")
    public List<GoodsWords> searchGoodsWords(String keyword){

        return goodsSearchManager.getGoodsWords(keyword);
    }


}
