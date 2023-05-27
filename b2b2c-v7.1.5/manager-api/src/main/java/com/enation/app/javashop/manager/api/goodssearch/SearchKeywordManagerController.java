package com.enation.app.javashop.manager.api.goodssearch;

import com.enation.app.javashop.core.goodssearch.model.SearchKeywordDO;
import com.enation.app.javashop.core.goodssearch.service.SearchKeywordManager;
import com.enation.app.javashop.framework.database.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
* @author liuyulei
 * @version 1.0
 * @Description: 关键字搜索历史相关API
 * @date 2019/5/27 11:33
 * @since v7.0
 */
@RestController
@RequestMapping("/admin/goodssearch/keywords")
@Api(description = "关键字搜索历史相关API")
public class SearchKeywordManagerController {


    @Autowired
    private SearchKeywordManager searchKeywordManager;

    @ApiOperation(value = "查询关键字历史列表", response = SearchKeywordDO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_no", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_size", value = "每页显示数量", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "keywords", value = "关键字", required = false, dataType = "string", paramType = "query")
    })
    @GetMapping
    public Page list(@ApiIgnore Integer pageNo,@ApiIgnore Integer pageSize,@ApiIgnore String keywords){
        return this.searchKeywordManager.list(pageNo,pageSize,keywords);
    }
}
