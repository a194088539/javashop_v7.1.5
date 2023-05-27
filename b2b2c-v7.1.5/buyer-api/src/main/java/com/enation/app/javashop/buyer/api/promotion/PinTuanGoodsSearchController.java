package com.enation.app.javashop.buyer.api.promotion;

import com.enation.app.javashop.core.promotion.pintuan.model.PtGoodsDoc;
import com.enation.app.javashop.core.promotion.pintuan.service.PinTuanSearchManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * Created by kingapex on 2019-01-21.
 * 搜索拼团商品
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2019-01-21
 */
@Api(description = "拼团搜索API")
@RestController
@RequestMapping("/pintuan/goods")
public class PinTuanGoodsSearchController {


    @Autowired
    private PinTuanSearchManager pinTuanSearchManager;

    @GetMapping("/skus")
    @ApiOperation(value = "搜索拼团商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category_id", value = "分类id", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_no", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_size", value = "页大小", required = true, dataType = "int", paramType = "query")


    })
    public List<PtGoodsDoc> search(@ApiIgnore Integer categoryId, @ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize) {
        List<PtGoodsDoc> list  = pinTuanSearchManager.search(categoryId, pageNo, pageSize);
        return  list;
    }

}
