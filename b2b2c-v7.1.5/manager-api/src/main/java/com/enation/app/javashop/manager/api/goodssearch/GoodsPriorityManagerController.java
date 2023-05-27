package com.enation.app.javashop.manager.api.goodssearch;

import com.enation.app.javashop.core.goods.model.dto.GoodsQueryParam;
import com.enation.app.javashop.core.goods.model.vo.GoodsPriorityVO;
import com.enation.app.javashop.core.goods.service.GoodsManager;
import com.enation.app.javashop.core.goods.service.GoodsQueryManager;
import com.enation.app.javashop.framework.database.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
* @author liuyulei
 * @version 1.0
 * @Description:  商品优先级相关API
 * @date 2019/6/10 14:49
 * @since v7.0
 */
@RestController
@RequestMapping("/admin/goodssearch/priority")
@Api(description = "商品优先级相关API")
public class GoodsPriorityManagerController {


    @Autowired
    private GoodsQueryManager goodsQueryManager;

    @Autowired
    private GoodsManager goodsManager;

    @ApiOperation(value = "查询商品优先级列表", response = GoodsPriorityVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_no", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_size", value = "每页显示数量", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "keywords", value = "关键字", required = false, dataType = "string", paramType = "query")
    })
    @GetMapping
    public Page list(@ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize, @ApiIgnore String keywords) {
        GoodsQueryParam goodsQueryParam = new GoodsQueryParam();
        goodsQueryParam.setPageNo(pageNo);
        goodsQueryParam.setPageSize(pageSize);
        goodsQueryParam.setKeyword(keywords);
        return this.goodsQueryManager.goodsPriorityList(goodsQueryParam);
    }

    @ApiOperation(value = "修改商品优先级")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goods_id", value = "商品id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "priority", value = "优先级", required = true, dataType = "int", paramType = "query",allowableValues = "1,2,3",
            example = "高(3),中(2),低(1)")
    })
    @PutMapping
    public void updatePriority(@ApiIgnore Integer goodsId, @ApiIgnore Integer priority) {
        this.goodsManager.updatePriority(goodsId,priority);
    }

}
