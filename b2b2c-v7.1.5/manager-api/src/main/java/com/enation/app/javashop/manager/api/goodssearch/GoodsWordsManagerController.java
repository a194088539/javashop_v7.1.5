package com.enation.app.javashop.manager.api.goodssearch;

import com.enation.app.javashop.core.goodssearch.service.GoodsWordsManager;
import com.enation.app.javashop.core.goodssearch.enums.GoodsWordsType;
import com.enation.app.javashop.core.goodssearch.model.CustomWords;
import com.enation.app.javashop.framework.database.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
* @author liuyulei
 * @version 1.0
 * @Description:  搜索提示词设置
 * @date 2019/6/14 15:38
 * @since v7.0
 */

@RestController
@RequestMapping("/admin/goodssearch/goods-words")
@Api(description = "搜索提示词相关API")
public class GoodsWordsManagerController {


    @Autowired
    private GoodsWordsManager goodsWordsManager;



    @ApiOperation(value = "查询提示词列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_no", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_size", value = "每页显示数量", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "keywords", value = "关键字", required = false, dataType = "string", paramType = "query")
    })
    @GetMapping
    public Page list(@ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize, @ApiIgnore String keywords) {

        return this.goodsWordsManager.listPage(pageNo, pageSize,keywords);
    }


    @ApiOperation(value = "添加自定义提示词", response = CustomWords.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "words", value = "提示词", required = true, dataType = "string", paramType = "query")
    })
    @PostMapping
    public void add(@Ignore @Length(max = 30 ,message = "最大长度30") String words) {

        this.goodsWordsManager.addWord(words);

    }

    @PutMapping(value = "/{id}/words")
    @ApiOperation(value = "修改自定义提示词", response = CustomWords.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "words", value = "提示词", required = true, dataType = "string", paramType = "query")
    })
    public void edit(@Ignore @Length(max = 30 ,message = "最大长度30") String  words, @PathVariable Integer id) {

        this.goodsWordsManager.updateWords(words, id);

    }


    @PutMapping(value = "/{id}/sort")
    @ApiOperation(value = "修改提示词排序")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "sort", value = "序号", required = true, dataType = "int", paramType = "query")
    })
    public void updateSort(@PathVariable Integer id,@NotNull(message = "必须输入序号") @Min(value = 0,message = "序号必须大于等于0")@Max(value = 999999,message = "最大值为999999")  Integer sort) {
        this.goodsWordsManager.updateSort(id, sort);

    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除提示词")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "int", paramType = "path")
    })
    public void delete(@PathVariable Integer id) {
        this.goodsWordsManager.delete(GoodsWordsType.PLATFORM,id);
    }

}
